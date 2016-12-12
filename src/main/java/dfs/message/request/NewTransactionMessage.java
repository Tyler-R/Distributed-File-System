package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class NewTransactionMessage implements Message {
	public static final String METHOD_ID = "NEW_TXN";
	
	private BigInteger transcationID = null;
	private BigInteger sequenceNumber = null;
	private String fileName = "";
	
	
	public NewTransactionMessage(BigInteger transcationID, BigInteger sequenceNumber, String fileName) {
		this.transcationID = transcationID;
		this.sequenceNumber = sequenceNumber;
		this.fileName = fileName;
	}

	@Override
	public void execute() {
		// sequenceNumber should be 0, transaction ID should be -1		
	}
}
