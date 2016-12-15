package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.RecoveryLog;
import main.java.dfs.Transaction;
import main.java.dfs.TransactionManager;
import main.java.dfs.TransactionStatus;
import main.java.dfs.message.Message;
import main.java.dfs.message.response.AckMessage;
import main.java.dfs.message.response.ErrorCode;
import main.java.dfs.message.response.ErrorMessage;

public class AbortMessage implements Message {

	public static final String METHOD_ID = "ABORT";
	
	private BigInteger transactionID = null;
	private ClientConnection client = null;
	
	public AbortMessage(BigInteger transactionID, ClientConnection client) {
		this.transactionID = transactionID;
		this.client = client;
	}
	
	
	@Override
	public void execute() {
		TransactionManager transactionManager = TransactionManager.getInstance();
		Transaction transaction = transactionManager.getTransaction(transactionID);
		
		// transactionID should be one of the active transactionID's.
		// transaction is null when the writes transactionID is not the same as a open transaction
		if(transaction == null) {
			Message response = new ErrorMessage(
					transactionID.toString(), ErrorCode.INVALID_TRANSACTION_ID,
					"Transaction with ID (" + transactionID.toString() + ") does not exist",
					client);
			
			response.execute();
			return;
		}
		synchronized(transaction) {
			// cannot abort a transaction that has previously been aborted or committed.
			TransactionStatus status = transaction.getStatus();
			if(status == TransactionStatus.COMMITTED) {
				Message response = new ErrorMessage(
						transactionID.toString(), ErrorCode.INVALID_OPERATION,
						"Transaction with ID (" + transactionID.toString() + ") was commited already, so it cannot be aborted",
						client);
				
				response.execute();
				return;
			} else if(status == TransactionStatus.ABORTED) {
				Message response = new ErrorMessage(
						transactionID.toString(), ErrorCode.INVALID_OPERATION,
						"Transaction with ID (" + transactionID.toString() + ") was aborted already, so it cannot be aborted again",
						client);
				
				response.execute();
				return;
			} else if(status == TransactionStatus.TIMER_EXPIRED) {
				Message response = new ErrorMessage(
						transactionID.toString(), ErrorCode.INVALID_OPERATION,
						"Transaction with ID (" + transactionID.toString() + ") timed out already, so it cannot be aborted",
						client);
				
				response.execute();
				return;
			}
			
			
			transaction.abort();
			
			Message response = new AckMessage(transactionID.toString(), "0", client);
			
			RecoveryLog.log(METHOD_ID, transactionID.toString(), "0", "ABORT");
			response.execute();
		}
		
	}
	
}
