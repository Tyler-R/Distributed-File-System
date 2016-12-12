package main.java.dfs.message.request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import main.java.dfs.AtomicFile;
import main.java.dfs.ClientConnection;
import main.java.dfs.message.Message;

public class ReadMessage implements Message{

	public static final String METHOD_ID = "READ";
	private String fileName = "";
	
	private ClientConnection client = null;

	public ReadMessage(String fileName, ClientConnection client) {
		this.fileName = fileName;
		this.client = client;
	}

	@Override
	public void execute() {	
		
		AtomicFile file = new AtomicFile(fileName);
		String content = "";
		try {
			content = file.read();
			try {
				System.out.println("sending msg to client");
				client.sendMessage(content);
			} catch(Exception e) {
				// there was an issue writing to the client
				System.out.println("ERROR sending response message to client. TCP pipe is probably broken.");
			}
			System.out.println("file content: " + content);
		} catch(FileNotFoundException e) {
			System.out.println("FILE NOT FOUND: RETURN ERROR");
		} catch(IOException e) {
			System.out.println("IO EXCEPTION: RETURN ERROR");
		}
		
		
	} 
	
}
