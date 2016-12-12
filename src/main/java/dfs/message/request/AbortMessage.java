package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class AbortMessage implements Message {

	public static final String METHOD_ID = "ABORT";
	
	private BigInteger transactionID = null;
	
	public AbortMessage(BigInteger transactionID) {
		this.transactionID = transactionID;
	}
	
	
	@Override
	public void execute() {
		
	}
	
}
