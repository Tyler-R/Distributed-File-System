package main.java.dfs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import main.java.dfs.message.request.CommitMessage;
import main.java.dfs.message.request.NewTransactionMessage;
import main.java.dfs.message.request.WriteMessage;

public class RecoveryLog {
	
	public static AtomicFile file = null;
	
	public static synchronized void log(String command, String transactionID, String sequenceNumber, String data) {
		assert(file != null);
		
		String message = command + " " + transactionID + " " + sequenceNumber + " " + data.length() + "\n" + data; 
		
		try {
			file.append(message);
		} catch (FileNotFoundException e) {
			System.out.println("File not found error occured when adding \"" + message + "\" to recovery log");
		} catch (IOException e) {
			System.out.println("IO error occured while adding \"" + message + "\" to recovery log");
		}
	}
	
	private static void processMessage(NetworkMessage message) {
		TransactionManager manager = TransactionManager.getInstance();
		String[] parsedHeader = message.header.split(" ");
		
		if(parsedHeader[0].equals(NewTransactionMessage.METHOD_ID)) {
			
			Transaction transaction = new Transaction(new BigInteger(parsedHeader[1]), message.data);
			manager.addExistingTransaction(transaction);
			
		} else if(parsedHeader[0].equals(WriteMessage.METHOD_ID)) {
			
			Transaction transaction = manager.getTransaction(new BigInteger(parsedHeader[1]));
			
			if(transaction == null) {
				System.out.println("Error recovering data for: " + message.header + "\n" + message.data);
				return;
			}
			
			WriteMessage writeMessage = 
					new WriteMessage(new BigInteger(parsedHeader[1]), new BigInteger(parsedHeader[2]), message.data, null);
			try {
				transaction.addWriteOperation(writeMessage);
			} catch (DuplicateMessageException e) {
				// do not do anything as we are rebuilding the transaction logs. This should never happen anyways.
			}
			
		} else if(parsedHeader[0].equals(CommitMessage.METHOD_ID)) {
			Transaction transaction = manager.getTransaction(new BigInteger(parsedHeader[1]));
			
			if(transaction == null) {
				System.out.println("Error recovering data for: " + message.header + "\n" + message.data);
				return;
			}
			
			if(message.data.equals("COMMIT")) {
				transaction.setStatus(TransactionStatus.COMMITTED);
			} else if(message.data.equals("ABORT")) {
				manager.removeTransaction(transaction);
			} else if(message.data.equals("TIMEOUT")) {
				manager.removeTransaction(transaction);
			} 
		}
	}
	
	public static void reconstructTransactions() {
		try {
			String logFileContent = file.read();
			
			StringBuilder headerBuilder = new StringBuilder(30);
			StringBuilder dataBuilder = null;
			
			ArrayList<NetworkMessage> messages = new ArrayList<NetworkMessage>();
						
			
			for(int i = 0; i < logFileContent.length(); i++) {
				char nextCharacter = logFileContent.charAt(i);
				if(nextCharacter == '\n') {
					String header = headerBuilder.toString();
					headerBuilder = new StringBuilder(30);
					
					int messageLength = Integer.parseInt(header.split(" ")[3]);
					
					dataBuilder = new StringBuilder(messageLength);
					
					for(int j = 0; j < messageLength; j++) {
						i++;
						char messageCharacter = logFileContent.charAt(i);
						dataBuilder.append(messageCharacter);
					}
					
					NetworkMessage newMessage = new NetworkMessage();
					newMessage.header = header;
					newMessage.data = dataBuilder.toString();
					
					processMessage(newMessage);
					
				} else {
					headerBuilder.append(nextCharacter);
				}
			}

			
		} catch (FileNotFoundException e) {
			// no log file exist. This is normal behavior for first execution.
		} catch (IOException e) {
			System.out.println("IO Error occured while reading \"" + file.getFileName() + "\"");
		}
	}
	
	public static void deleteLog() {
		
		File logFile = new File(AtomicFile.directory + file.getFileName());
		logFile.delete();
		
	}
	
}
