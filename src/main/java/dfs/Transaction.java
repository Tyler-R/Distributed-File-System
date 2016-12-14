package main.java.dfs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.swing.Timer;

import main.java.dfs.message.Message;
import main.java.dfs.message.request.CommitMessage;
import main.java.dfs.message.request.WriteMessage;

public class Transaction {
	private ConcurrentSkipListSet<WriteMessage> writeMessages = 
			new ConcurrentSkipListSet<WriteMessage>(
					(WriteMessage a, WriteMessage b) -> a.getSequenceNumber().compareTo(b.getSequenceNumber())
			);
	private CommitMessage commitMessage = null;
	
	private BigInteger transactionID = null;
	private String fileName = "";
	
	private TransactionStatus status = TransactionStatus.IN_PROGRESS;
	
	private static final int TIMER_DELAY_IN_MILLISECONDS = 3000;
	
	private Timer timer = new Timer(TIMER_DELAY_IN_MILLISECONDS, (e) -> {
		synchronized(this) {
			if(status == TransactionStatus.IN_PROGRESS) {
				status = TransactionStatus.TIMER_EXPIRED;
				System.out.println("Trannsaction with ID (" + transactionID.toString() + ") timed out");
			}
			this.timer.stop();
		}
	});
	
	public Transaction(BigInteger transactionID, String fileName) {
		this.transactionID = transactionID;
		this.fileName = fileName;
		
		timer.start();
	}
	
	public String getFileName() {
		return fileName;
	}

	public BigInteger getTransactionID() {
		return transactionID;
	}
	
	public void abort() {
		timer.stop();
		
		if(status == TransactionStatus.IN_PROGRESS) {
			status = TransactionStatus.ABORTED;
		}
	}

	public void addWriteOperation(WriteMessage newMessage) throws DuplicateMessageException {
		timer.restart();
		
		
		if(!writeMessages.add(newMessage)) {
			// could not add message to set
			throw new DuplicateMessageException();
		}
				
		if(status == TransactionStatus.COMMIT_WHEN_ALL_WRITES_RECEIVED 
				&& commitMessage != null
				&& readyToCommit()) 
		{
			// this execute should always succeed.
			System.out.println("Executing commit because of write");
			commitMessage.execute();
		}
	}
	
	private boolean readyToCommit() {
		
		if(!commitMessage.getSequenceNumber().equals(BigInteger.valueOf(writeMessages.size()))) {
			return false;
		}
		
		BigInteger i = BigInteger.valueOf(1);
		for(WriteMessage message : writeMessages) {
			if(!message.getSequenceNumber().equals(i)) {
				return false;
			}
			
			i = i.add(BigInteger.ONE);
		}
		
		return true;
		
	}
	
	public ArrayList<BigInteger> getMissingWritesSequenceNumbers(BigInteger expectedSize) {
		timer.restart();
		
		int numberOfWritesMissed = expectedSize.subtract(BigInteger.valueOf(writeMessages.size())).intValue();
		
		if(numberOfWritesMissed > 0) {
			ArrayList<BigInteger> missingWrites = new ArrayList<BigInteger>(numberOfWritesMissed);
			
			BigInteger i = BigInteger.valueOf(1);
			
			for(WriteMessage message : writeMessages) {
				while(!message.getSequenceNumber().equals(i)) {
					missingWrites.add(new BigInteger(i.toString()));
					i = i.add(BigInteger.ONE);
				}
				
				i = i.add(BigInteger.ONE);
			}
									
			while(i.compareTo(expectedSize) <= 0) {
				missingWrites.add(new BigInteger(i.toString()));
				i = i.add(BigInteger.ONE);
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

	public void writeMessageToDisk() throws FileNotFoundException, IOException {
		timer.stop();
		
		if(status == TransactionStatus.IN_PROGRESS) {
			String message = getWriteMessage();
			
			AtomicFile file = new AtomicFile(fileName);
	
			file.append(message);
			
			status = TransactionStatus.COMMITTED;
		}

	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setCommitMessage(CommitMessage commitMessage) {
		this.commitMessage = commitMessage;
		status = TransactionStatus.COMMIT_WHEN_ALL_WRITES_RECEIVED;
	}
	
	
}
