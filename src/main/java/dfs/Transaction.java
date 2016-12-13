package main.java.dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

import main.java.dfs.message.Message;
import main.java.dfs.message.request.WriteMessage;

public class Transaction {
	private ConcurrentSkipListSet<WriteMessage> writeMessages = 
			new ConcurrentSkipListSet<WriteMessage>(
					(WriteMessage a, WriteMessage b) -> a.getSequenceNumber().compareTo(b.getSequenceNumber())
			);
	
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
		
		if(!writeMessages.add(newMessage)) {
			// could not add message to set
			throw new DuplicateMessageException();
		}		
	}
	
	public ArrayList<BigInteger> getMissingWritesSequenceNumbers(BigInteger expectedSize) {
		int numberOfWritesMissed = expectedSize.subtract(BigInteger.valueOf(writeMessages.size())).intValue();

		if(numberOfWritesMissed > 0) {
			ArrayList<BigInteger> missingWrites = new ArrayList<BigInteger>(numberOfWritesMissed);
			
			BigInteger i = BigInteger.valueOf(1);
			
			for(WriteMessage message : writeMessages) {
				while(!message.getSequenceNumber().equals(i)) {
					missingWrites.add(new BigInteger(i.toString()));
					i.add(BigInteger.ONE);
				}
				
				i.add(BigInteger.ONE);
			}
			
			while(!i.equals(expectedSize)) {
				missingWrites.add(new BigInteger(i.toString()));
			}
			
			return missingWrites;
		}
		
		return null; 
	}
	
	private String getWriteMessage() {
		
		String message = "";
		for(WriteMessage writeMessage : writeMessages) {
			message += writeMessage.getData();
		}
		
		return message;
	}

	public void writeMessageToDisk() {
		String message = getWriteMessage();
		
		AtomicFile file = new AtomicFile(fileName);
		try {
			file.append(message);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
