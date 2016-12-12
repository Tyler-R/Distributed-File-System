package main.java.dfs;

import java.math.BigInteger;
import java.util.ArrayList;

import main.java.dfs.message.Message;
import main.java.dfs.message.request.WriteMessage;

public class Transaction {
	private ArrayList<WriteMessage> writeMessages = new ArrayList<WriteMessage>();
	
	private BigInteger transactionID = null;
	private String fileName = "";
	
	public Transaction(BigInteger transactionID, String fileName) {
		this.transactionID = transactionID;
		this.fileName = fileName;
	}

	public Object getTransactionID() {
		return transactionID;
	}

	public void addWriteOperation(WriteMessage newMessage) throws DuplicateMessageException {
		
		BigInteger newMessageSequenceNumber = newMessage.getSequenceNumber();
		
		for(WriteMessage message : writeMessages) {
			if(message.getSequenceNumber().equals(newMessageSequenceNumber)) {
				throw new DuplicateMessageException();
			}
		}
		
		writeMessages.add(newMessage);
		
	}
	
	
}
