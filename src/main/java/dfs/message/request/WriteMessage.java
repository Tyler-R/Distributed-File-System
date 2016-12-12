package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.DuplicateMessageException;
import main.java.dfs.Transaction;
import main.java.dfs.TransactionManager;
import main.java.dfs.message.Message;

public class WriteMessage implements Message {
	public static String METHOD_ID = "WRITE";

	private BigInteger transcationID = null;
	private BigInteger sequenceNumber = null;
	private String data = "";
	private ClientConnection client = null;
	
	public WriteMessage(BigInteger transcationID, BigInteger sequenceNumber, String data, ClientConnection client) {
		this.transcationID = transcationID;
		this.sequenceNumber = sequenceNumber;
		this.data = data;
		this.client = client;
	}

	@Override
	public void execute() {
		TransactionManager transactionManager = TransactionManager.getInstance();
		Transaction transaction = transactionManager.getTransaction(transcationID);
		
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
