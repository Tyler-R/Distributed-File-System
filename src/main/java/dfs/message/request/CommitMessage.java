package main.java.dfs.message.request;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.message.Message;

public class CommitMessage implements Message {
	public static final String METHOD_ID = "COMMIT";
	
	private BigInteger transcationID = null;
	private BigInteger sequenceNumber = null;
	private ClientConnection client = null;
	
	public CommitMessage(BigInteger transcationID, BigInteger sequenceNumber, ClientConnection client) {
		this.transcationID = transcationID;
		this.sequenceNumber = sequenceNumber;
		this.client = client;
	}

	@Override
	public void execute() {
		// sequence number should equal the number writes that have been received
		// transactionID should be one of the active transactionID's.
		
	}

}
