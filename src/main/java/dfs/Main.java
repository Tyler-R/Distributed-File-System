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
	
	public static Message parseMessage(String message, ClientConnection connection) {
		System.out.println("---------------------------------------------------------\n"
				+ "RECEIVED: message with length: " + message.length());
		
		String parsedMessageHeaderAndData[] = new String(message).split("\r\n\r\n");
		
		
		System.out.println("Full Message: \n" + new String(message));
		String header[] = parsedMessageHeaderAndData[0].split(" ");
		System.out.println("header length: " + header.length);
		
		if(header.length != 4) {
			System.out.println("Error: Input from client is not in correct format. Length is " + header.length + " when it should be 4");
			
			// get more data from connection and add it to current message 
			String moreData = connection.getMessage();
			if(moreData.length() != 0) {
				System.out.println(moreData.length() + " bytes were added");
				String newMessage = (new String(message) + new String(moreData));
				return parseMessage(newMessage, connection);
			} else {
				return null;

			}
		}
		
		String method = header[0];
		String transactionID = header[1];
		String sequenceNumber = header[2];
		String contentLength = header[3];
		
		// start at 1 so that you do not add the header to the data.
		StringBuilder dataBuilder = new StringBuilder();
		for(int i = 1; i < parsedMessageHeaderAndData.length; i++) {
			dataBuilder.append(parsedMessageHeaderAndData[i]);
		}
		
		if(dataBuilder.length() < Integer.valueOf(contentLength)) {
			System.out.println("ERROR: data too short, get more.  Data was length " + dataBuilder.length());
		}
		
		String data = dataBuilder.substring(0, Integer.valueOf(contentLength));
		
		
		
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
			Message message = parseMessage(connection.getMessage(), connection);
			
			message.execute();

		}
	}
}
