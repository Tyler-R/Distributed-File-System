package main.java.dfs.message;

import java.math.BigInteger;

import main.java.dfs.ClientConnection;
import main.java.dfs.message.request.AbortMessage;
import main.java.dfs.message.request.CommitMessage;
import main.java.dfs.message.request.NewTransactionMessage;
import main.java.dfs.message.request.ReadMessage;
import main.java.dfs.message.request.WriteMessage;
import main.java.dfs.message.response.AckMessage;
import main.java.dfs.message.response.AskResendMessage;
import main.java.dfs.message.response.ErrorCode;
import main.java.dfs.message.response.ErrorMessage;

public abstract class MessageFactory {
		
	public static Message makeRequestMessage(String method, String transactionID, String sequenceNumber, String data, ClientConnection connection) {
		
		Message message = null;
		// convert the method to upper case for easier comparison.
		method = method.toUpperCase();
		
		BigInteger parsedTransactionID = null;
		BigInteger parsedSequenceNumber = null;
		
		try {
			parsedTransactionID = new BigInteger(transactionID);
		} catch(NumberFormatException e) {
			System.out.println("transaction ID message: " + e.getMessage());
			System.out.println("transaction ID not parsable because: " + e.getCause().getMessage());
		}
		
		try {
			parsedSequenceNumber = new BigInteger(sequenceNumber);
		} catch(NumberFormatException e) {
			System.out.println("sequenceNumber message: " + e.getMessage());
			System.out.println("sequenceNumber not parsable because: " + e.getCause().getMessage());
		}
		
		
		if(method.equals(ReadMessage.METHOD_ID)) {
			
			message = new ReadMessage(data, connection);
			
		} else if(method.equals(NewTransactionMessage.METHOD_ID)) {
			
			message = new NewTransactionMessage(parsedTransactionID, parsedSequenceNumber, data, connection);
			
		} else if(method.equals(WriteMessage.METHOD_ID)) {
			
			message = new WriteMessage(parsedTransactionID, parsedSequenceNumber, data, connection);
			
		} else if(method.equals(CommitMessage.METHOD_ID)) {
			
			message = new CommitMessage(parsedTransactionID, parsedSequenceNumber, connection);
		
		} else if(method.equals(AbortMessage.METHOD_ID)) {
			
			message = new AbortMessage(parsedTransactionID, connection);
		
		}
		
		return message;
		
	}
	
	public static Message makeResponseMessage(String method, String transactionID, String sequenceNumber, ErrorCode errorCode, String reason, ClientConnection connection) {
		Message message = null;
		
		if(method.equals(ErrorMessage.METHOD_ID)) {
			message = new ErrorMessage(transactionID, errorCode, reason, connection);
		} else if(method.equals(AckMessage.METHOD_ID)) {
			message = new AckMessage(transactionID, sequenceNumber, connection);
		} else if(method.equals(AskResendMessage.METHOD_ID)) {
			message = new AskResendMessage(transactionID, sequenceNumber, connection);
		}
		
		return message;
	}
	
}
