package main.java.dfs.message.Request;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class Abort implements Message {

	public static final String METHOD_ID = "ABORD";
	
	private BigInteger transactionID = null;
	
	public Abort(BigInteger transactionID) {
		this.transactionID = transactionID;
	}
	
	
	@Override
	public void execute() {
		
	}
	
}
