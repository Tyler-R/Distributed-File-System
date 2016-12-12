package main.java.dfs.message.Request;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class WriteMessage implements Message {
	public static String METHOD_ID = "WRITE";

	private BigInteger transcationID = null;
	private BigInteger sequenceNumber = null;
	private String data = "";
	
	public WriteMessage(BigInteger transcationID, BigInteger sequenceNumber, String data) {
		this.transcationID = transcationID;
		this.sequenceNumber = sequenceNumber;
		this.data = data;
	}

	@Override
	public void execute() {
		
	}
	
	
}
