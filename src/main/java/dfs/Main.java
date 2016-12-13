package main.java.dfs;

import main.java.dfs.message.Message;
import main.java.dfs.message.MessageFactory;
import main.java.dfs.message.request.NewTransactionMessage;
import main.java.dfs.message.request.WriteMessage;
import main.java.dfs.message.response.ErrorMessage;

public class Main {
	
	
	public static void printHelpMessage() {
		System.out.println("ERROR server must take 3 arguments: ./server <ipAddress> <port> <directory>");
		System.out.println("For example: ./server 127.0.0.1 8080 /mnt/test");
	}
	
	public static Message parseMessage(String header, String data, ClientConnection connection) {
		System.out.println("---------------------------------------------------------");
				
		
		String parsedHeader[] = header.split(" ");
		System.out.println("header elements: " + parsedHeader.length);
		
		if(parsedHeader.length != 4) {
			System.out.println("Error: Input from client is not in correct format. Length is " + parsedHeader.length + " when it should be 4");
			
			// should return an error
			return null;
		}
		
		String method = parsedHeader[0];
		String transactionID = parsedHeader[1];
		String sequenceNumber = parsedHeader[2];
		String contentLength = parsedHeader[3];
		
		if(data.length() < Integer.valueOf(contentLength)) {
			System.out.println("ERROR: data too short, get more.  Data was length " + data.length() + " when it should be " + Integer.valueOf(contentLength));
		}
		
//		String data = dataBuilder.substring(0, Integer.valueOf(contentLength) - 1);
		
		
		
		System.out.println("method: " + method);
		System.out.println("transactionID: " + transactionID);
		System.out.println("sequenceNumber: " + sequenceNumber);
		System.out.println("contentLength: " + contentLength);
		System.out.println("data length: " + data.length());
		System.out.println("data: " + data);
		
		Message requestMessage = MessageFactory.makeRequestMessage(method, transactionID, sequenceNumber, data, connection);
		
		System.out.println("---------------------------------------------------------");
		
		return requestMessage;
	}
	
	public static void main(String args[]) {
		if(args.length != 3) {
			printHelpMessage();
			System.exit(1);
		}
		
		// parse command line arguments
		String ipAddress = args[0];
		int port = -1;
		try {
			port = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			System.out.println("ERROR: Could not covert \"" + args[1] + "\" into a number. Enter a valid number for the port.");
			System.exit(1);
		}
		String fileSystemDirectory = args[2];
		AtomicFile.directory = fileSystemDirectory;
		
		Network network = new Network(ipAddress, port);
		
		
		while(true) {
			
			ClientConnection connection = network.getClientConnection();
			System.out.println("RECEIVED CONNECTION:");
			NetworkMessage networkMessage = connection.getMessage();
			if(networkMessage != null) {
				Message message = parseMessage(networkMessage.header, networkMessage.data, connection);
				
				message.execute();
			}

		}
	}
}
