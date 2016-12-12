package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.message.Message;

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
		
	}
	
}
