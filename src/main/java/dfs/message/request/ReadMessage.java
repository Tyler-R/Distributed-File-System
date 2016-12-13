package main.java.dfs.message.request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;

import main.java.dfs.AtomicFile;
import main.java.dfs.ClientConnection;
import main.java.dfs.message.Message;
import main.java.dfs.message.MessageFactory;
import main.java.dfs.message.response.ErrorCode;

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
				client.sendMessage(content);
			} catch(IOException e) {
				System.out.println("ERROR sending response message to client. TCP pipe is probably broken.");
			}
		
		} catch(FileNotFoundException e) {
			Message errorMessage = 
					MessageFactory.makeResponseMessage("ERROR", "-1", "0", ErrorCode.FILE_NOT_FOUND, 
					"Could not read \"" + fileName + "\" as it does not exist on the server\n", client);
			
			errorMessage.execute();
		} catch(IOException e) {
			Message errorMessage = 
					MessageFactory.makeResponseMessage("ERROR", "-1", "0", ErrorCode.FILE_NOT_FOUND, 
							"error reading \"" + fileName + "\" because: " + e.getMessage() + "\n", client);
			
			errorMessage.execute();
		}
		
		
	} 
	
}
