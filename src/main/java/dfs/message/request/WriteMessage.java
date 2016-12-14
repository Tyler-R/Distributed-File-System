package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.DuplicateMessageException;
import main.java.dfs.Transaction;
import main.java.dfs.TransactionManager;
import main.java.dfs.TransactionStatus;
import main.java.dfs.message.Message;
import main.java.dfs.message.response.ErrorCode;
import main.java.dfs.message.response.ErrorMessage;

public class WriteMessage implements Message {
	public static String METHOD_ID = "WRITE";

	private BigInteger transactionID = null;
	private BigInteger sequenceNumber = null;
	private String data = "";
	private ClientConnection client = null;
	
	public WriteMessage(BigInteger transactionID, BigInteger sequenceNumber, String data, ClientConnection client) {
		this.transactionID = transactionID;
		this.sequenceNumber = sequenceNumber;
		this.data = data;
		this.client = client;
	}

	@Override
	public void execute() {
		TransactionManager transactionManager = TransactionManager.getInstance();
		Transaction transaction = transactionManager.getTransaction(transactionID);
		
		if(transaction == null) {
			Message response = new ErrorMessage(
					transactionID.toString(), ErrorCode.INVALID_TRANSACTION_ID,
					"Transaction with ID (" + transactionID.toString() + 
					") does not exist. Write with sequence number (" + sequenceNumber.toString() + ")could not be executed",
					client);
			
			response.execute();
			return;
		}
		
		TransactionStatus status = transaction.getStatus();
		
		if(status == TransactionStatus.COMMITTED) {
			return;
		} else if(status == TransactionStatus.ABORTED) {
			return;
		}
		
		try {
			transaction.addWriteOperation(this);
		} catch(DuplicateMessageException e) {
			
		}
		
	}
	
	public BigInteger getSequenceNumber() {
		return sequenceNumber;
	}
	
	public String getData() {
		return this.data;
	}
	
	
}
