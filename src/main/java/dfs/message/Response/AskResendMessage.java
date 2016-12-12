package main.java.dfs.message.Response;

import main.java.dfs.message.Message;

public class AskResendMessage implements Message {

	public static final String METHOD_ID = "ASK_RESEND";
	
	public AskResendMessage(String trasactionID, String sequenceNumber) {
		
	}
	
	@Override
	public void execute() {
		
	}
	
}
