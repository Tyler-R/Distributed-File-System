package main.java.dfs.message.request;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;

import main.java.dfs.ClientConnection;
import main.java.dfs.Transaction;
import main.java.dfs.TransactionManager;
import main.java.dfs.TransactionStatus;
import main.java.dfs.message.Message;
import main.java.dfs.message.response.AckMessage;
import main.java.dfs.message.response.AskResendMessage;
import main.java.dfs.message.response.ErrorCode;
import main.java.dfs.message.response.ErrorMessage;

public class CommitMessage implements Message {
	public static final String METHOD_ID = "COMMIT";

	private BigInteger transactionID = null;
	private BigInteger sequenceNumber = null;
	private ClientConnection client = null;

	public CommitMessage(BigInteger transactionID, BigInteger sequenceNumber, ClientConnection client) {
		this.transactionID = transactionID;
		this.sequenceNumber = sequenceNumber;
		this.client = client;
	}

	@Override
	public void execute() {
		TransactionManager transactionManager = TransactionManager.getInstance();
		Transaction transaction = transactionManager.getTransaction(transactionID);
		
		// transactionID should be one of the active transactionID's.
		// transaction is null when the writes transactionID is not the same as a open transaction
		if(transaction == null) {
			Message response = new ErrorMessage(
					transactionID.toString(), ErrorCode.INVALID_TRANSACTION_ID,
					"Transaction with ID (" + transactionID.toString() + ") does not exist",
					client);
			
			response.execute();
			return;
		}

		// cannot commit a transaction that has already been commited, or has previously been aborted.
		TransactionStatus status = transaction.getStatus();
		if(status == TransactionStatus.COMMITTED) {
			Message response = new ErrorMessage(
					transactionID.toString(), ErrorCode.INVALID_OPERATION,
					"Transaction with ID (" + transactionID.toString() + ") has already been committed",
					client);
			
			response.execute();
			return;
		} else if(status == TransactionStatus.ABORTED) {
			Message response = new ErrorMessage(
					transactionID.toString(), ErrorCode.INVALID_OPERATION,
					"Transaction with ID (" + transactionID.toString() + ") was aborted and so it cannot be aborted",
					client);
			
			response.execute();
			return;
		}

		// sequence number should equal the number writes that have been received
		ArrayList<BigInteger> missingWriteSequenceNumbers = transaction.getMissingWritesSequenceNumbers(sequenceNumber);

		if (missingWriteSequenceNumbers == null) {
			try {
				transaction.writeMessageToDisk();
			} catch (FileNotFoundException e) {
				Message response = new ErrorMessage(transactionID.toString(), ErrorCode.FILE_NOT_FOUND, 
						"Error writing \"" + transaction.getFileName() + "\" to disk because file could not be found. "
								+ "It is possible the server does not have permissions to write to this directory "
								+ "or the file path is incorrect. Diagnostic message: " + e.getMessage(), 
						client);
				
				response.execute();
				return;
				
			} catch (IOException e) {
				Message response = new ErrorMessage(transactionID.toString(), ErrorCode.FILE_IO_ERROR, 
						"could not write \"" + transaction.getFileName() + "\" to disk because: " + e.getMessage(), 
						client);
				
				response.execute();
				return;
			}

			Message response = new AckMessage(transactionID.toString(), "0", client);
			response.execute();
			return;
			
		} else { // send message to client asking to resent missed write messages
			transaction.setCommitMessage(this);
			
			for (BigInteger missingTransactionID : missingWriteSequenceNumbers) {
				Message response = new AskResendMessage(transactionID.toString(), missingTransactionID.toString(), client);
				response.execute();
			}
			
			return;
		}

	}

	public BigInteger getSequenceNumber() {
		return sequenceNumber;
	}

}
