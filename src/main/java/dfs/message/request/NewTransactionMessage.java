package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.Transaction;
import main.java.dfs.TransactionManager;
import main.java.dfs.message.Message;

public class NewTransactionMessage implements Message {
	public static final String METHOD_ID = "NEW_TXN";
	
	private BigInteger transcationID = null;
	private BigInteger sequenceNumber = null;
	private String fileName = "";
	private ClientConnection client = null;
	
	
	public NewTransactionMessage(BigInteger transcationID, BigInteger sequenceNumber, String fileName, ClientConnection client) {
		this.transcationID = transcationID;
		this.sequenceNumber = sequenceNumber;
		this.fileName = fileName;
		this.client = client;
	}

	@Override
	public void execute() {
		// sequenceNumber should be 0, transaction ID should be -1
		TransactionManager manager = TransactionManager.getInstance();
		
		Transaction newTransaction = manager.startNewTransaction(fileName);
	}
}
