package main.java.dfs.message.response;

import java.io.IOException;

import main.java.dfs.ClientConnection;
import main.java.dfs.message.Message;

public class AskResendMessage implements Message {

	public static final String METHOD_ID = "ASK_RESEND";
	
	private String transactionID = "";
	private String sequenceNumber = "";
	private ClientConnection client = null;
	
	public AskResendMessage(String transactionID, String sequenceNumber, ClientConnection client) {
		this.transactionID = transactionID;
		this.sequenceNumber = sequenceNumber;
		this.client = client;
	}
	
	@Override
	public void execute() {
		StringBuilder message = new StringBuilder();
		
		// data and errorCode can be any value is response message
		message.append(METHOD_ID)
				.append(" ")
				.append(transactionID)
				.append(" ")
				.append(sequenceNumber)
				.append(" ")
				.append(ErrorCode.ASK_RESEND.getCode())
				.append(" ")
				.append("0") // there is no reason message so length is 0.
				.append("\r\n\r\n\r\n");
		
		try {
			client.sendMessage(message.toString());
		} catch (IOException e) {
			System.out.println("Could not send ASK_RESEND message for transactionID: (" 
					+ transactionID + ") and sequenceNumber: (" + sequenceNumber 
					+ "). IO exception \"" + e.getMessage() + "\" occured, which is likely a broken TCP pipe.");
		}
	}
	
}
