package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.RecoveryLog;
import main.java.dfs.Transaction;
import main.java.dfs.TransactionManager;
import main.java.dfs.message.Message;
import main.java.dfs.message.MessageFactory;
import main.java.dfs.message.response.AckMessage;
import main.java.dfs.message.response.ErrorCode;
import main.java.dfs.message.response.ErrorMessage;

public class NewTransactionMessage implements Message {
	public static final String METHOD_ID = "NEW_TXN";
	
	private BigInteger transactionID = null;
	private BigInteger sequenceNumber = null;
	private String fileName = "";
	private ClientConnection client = null;
	
	
	public NewTransactionMessage(BigInteger transactionID, BigInteger sequenceNumber, String fileName, ClientConnection client) {
		this.transactionID = transactionID;
		this.sequenceNumber = sequenceNumber;
		this.fileName = fileName;
		this.client = client;
	}

	@Override
	public void execute() {
		// sequenceNumber should be 0,
		// this handles when sequenceNumber is not 0
		if(!sequenceNumber.equals(BigInteger.ZERO)) {
			Message response = MessageFactory.makeResponseMessage(
					ErrorMessage.METHOD_ID, transactionID.toString(), "0", ErrorCode.INVALID_OPERATION, 
					"sequence number for " + METHOD_ID + " must be 0. The sequence number received by the server was (" + sequenceNumber.toString() + ").", 
					client);
			
			response.execute();
			return;
		}
		
		// transaction ID should be -1
		// this handles when transactionID is not -1
		if(!transactionID.equals(BigInteger.valueOf(-1))) {
			Message response = MessageFactory.makeResponseMessage(
					ErrorMessage.METHOD_ID, transactionID.toString(), "0", ErrorCode.INVALID_TRANSACTION_ID, 
					"Transaction ID for " + METHOD_ID + " must be -1. The Transaction ID received by the server was (" + transactionID.toString() + ").", 
					client);
			
			response.execute();
			return;
		}
		
		TransactionManager manager = TransactionManager.getInstance();
		Transaction newTransaction = manager.startNewTransaction(fileName);
		synchronized(newTransaction) {
		
			Message response = MessageFactory.makeResponseMessage(
					AckMessage.METHOD_ID, 
					newTransaction.getTransactionID().toString(), 
					"0", ErrorCode.ACK, "", client);
			
			RecoveryLog.log(METHOD_ID, newTransaction.getTransactionID().toString(), "0", fileName);
			response.execute();
		}
		
	}
}
