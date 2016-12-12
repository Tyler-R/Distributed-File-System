package main.java.dfs.message.response;

import main.java.dfs.message.Message;

public class ErrorMessage implements Message {
	
	public static final String METHOD_ID = "ERROR";
	
	public ErrorMessage() {
		
	}
	
	@Override
	public void execute() {
		
	}
}
