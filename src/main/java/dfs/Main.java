package main.java.dfs;

import main.java.dfs.message.Message;
import main.java.dfs.message.MessageFactory;
import main.java.dfs.message.request.NewTransactionMessage;
import main.java.dfs.message.request.WriteMessage;
import main.java.dfs.message.response.ErrorCode;
import main.java.dfs.message.response.ErrorMessage;

public class Main {
	
	
	public static void printHelpMessage() {
		System.out.println("ERROR server must take 3 arguments: ./server <ipAddress> <port> <directory>");
		System.out.println("For example: ./server 127.0.0.1 8080 /mnt/test");
	}
	
	public static Message parseMessage(String header, String data, ClientConnection connection) {				
		
		String parsedHeader[] = header.split(" ");
		
		if(parsedHeader.length < 4) {
			System.out.println("Error: Input from client is not in correct format. Length is " + parsedHeader.length + " when it should be 4");
			
			// should return an error
			String transactionID = parsedHeader.length >= 2 ? parsedHeader[1] : "1";
			return new ErrorMessage(transactionID, ErrorCode.INVALID_OPERATION, 
					"Header for message was not parsed properly.  The following incorrect header was received: " + header,
					connection);
		}
		
		String method = parsedHeader[0];
		String transactionID = parsedHeader[1];
		String sequenceNumber = parsedHeader[2];
		String contentLength = parsedHeader[3];
		
		try {
			if(data.length() < Integer.valueOf(contentLength)) {
				return parseMessage(header, data + connection.getData(), connection);
			}		
		} catch(NumberFormatException e) {
			return new ErrorMessage(transactionID, ErrorCode.INVALID_OPERATION, 
					"Content length is(" + contentLength + ") which is not an integer.",
					connection);
		}
		
		/*System.out.println("RECEIVED: " 
				+ method + " " 
				+ transactionID + " " 
				+ sequenceNumber + " "
				+ contentLength + "\n"
				+ data);*/
		
		Message requestMessage = MessageFactory.makeRequestMessage(method, transactionID, sequenceNumber, data, connection);
				
		return requestMessage;
	}
	
	public static void main(String[] args) {
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
		
		RecoveryLog.file = new AtomicFile(".recovery.log");
		
		RecoveryLog.reconstructTransactions();
		
		
		Network network = new Network(ipAddress, port);
		
		int processors = Runtime.getRuntime().availableProcessors();
		processors = (processors < 4 ? 4 : processors);
		
		Thread[] threads = new Thread[processors];
		
		for (int i = 0; i < threads.length; i++) {
			
			threads[i] = new Thread(()-> {
				while(true){
					ClientConnection connection = network.getClientConnection();
					
					NetworkMessage networkMessage = connection.getMessage();
					if(networkMessage != null) {
						Message message = parseMessage(networkMessage.header, networkMessage.data, connection);
						
						message.execute();
					}
				}
			});
			
			threads[i].start();
		}
		
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				System.out.println("Thread join failed");
			}
		}

		
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
