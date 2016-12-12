package main.java.dfs.message.Request;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class CommitMessage implements Message {
	public static final String METHOD_ID = "COMMIT";
	
	private BigInteger transcationID = null;
	private BigInteger sequenceNumber = null;
	
	public CommitMessage(BigInteger transcationID, BigInteger sequenceNumber) {
		this.transcationID = transcationID;
		this.sequenceNumber = sequenceNumber;
	}

	@Override
	public void execute() {
		// sequence number should equal the number writes that have been received
		// transactionID should be one of the active transactionID's.
		
	}

}
