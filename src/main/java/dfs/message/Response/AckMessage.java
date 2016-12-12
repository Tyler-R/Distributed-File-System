package main.java.dfs.message.Response;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class AckMessage implements Message {
	
	public static final String METHOD_ID = "ACK";
	
	private String transactionID = "";
	private String sequenceNumber = "";
	// data and errorCode can be any value is response message
//	private String errorCode = "0";
//	private String data = "";
	
	
	public AckMessage(String transactionID, String sequenceNumber) {
		this.transactionID = transactionID;
		this.sequenceNumber = sequenceNumber;
	}
	
	@Override
	public void execute() {
		
	}
}
