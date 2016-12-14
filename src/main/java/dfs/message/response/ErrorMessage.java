package main.java.dfs.message.response;

import java.io.IOException;

import main.java.dfs.ClientConnection;
import main.java.dfs.message.Message;

public class ErrorMessage implements Message {
	
	public static final String METHOD_ID = "ERROR";
	
	private String transactionID = "";
	private String sequenceNumber = "";
	private String errorCode = "";
	private String reason = "";
	private ClientConnection client = null;
	
	public ErrorMessage(String transactionID, ErrorCode errorCode, String reason, ClientConnection client) {
		this.transactionID = transactionID;
		this.sequenceNumber = "0";
		this.errorCode = errorCode.getCode();
		this.reason = reason + "\n";
		this.client = client;
	}
	
	@Override
	public void execute() {
		StringBuilder message = new StringBuilder();
		
		message .append(METHOD_ID)
				.append(" ")
				.append(transactionID)
				.append(" ")
				.append(sequenceNumber)
				.append(" ")
				.append(errorCode)
				.append(" ")
				.append(Integer.toString(reason.length()))
				.append("\r\n\r\n");
		
		if(reason.length() <= 0) {
			message.append("\r\n");
		} else {
			message.append(reason);
		}
						
		try {
			client.sendMessage(message.toString());
		} catch (IOException e) {
			System.out.println("unable to send error message to client. They may have closed their tcp connection.");
		}
	}
}

