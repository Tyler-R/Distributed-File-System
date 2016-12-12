package main.java.dfs.message.Request;

import java.math.BigInteger;

import main.java.dfs.message.Message;

public class ReadMessage implements Message{

	public static final String METHOD_ID = "READ";
	private String fileName = "";

	public ReadMessage(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void execute() {	
		
	} 
	
}
