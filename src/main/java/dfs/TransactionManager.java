package main.java.dfs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionManager {
	
	// allows for lazy loaded the singleton, along with concurrent access to getInstance().
	// This is possible because static classes are not loaded until they are accessed, 
	// and static class variables are always instantiated before class is accessible.
	private static class Holder {
		static TransactionManager instance = new TransactionManager();
	} 
	
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	private AtomicLong currentTransactionIDCounter = new AtomicLong();
	
	private TransactionManager() {
		
	}
	
	public Transaction startNewTransaction(String fileName) {
		long newTransactionID = currentTransactionIDCounter.incrementAndGet();
		
		Transaction transaction = new Transaction(BigInteger.valueOf(newTransactionID), fileName);
		transactions.add(transaction);
		
		return transaction;
		
	}
	
	public boolean containTransaction(BigInteger transactionID) {
		for(Transaction transaction: transactions) {
			if(transaction.getTransactionID().equals(transactionID)) {
				return true;
			}
		}
		
		return false;
	}
	
	public Transaction getTransaction(BigInteger transactionID) {
		
		for(Transaction transaction: transactions) {
			if(transaction.getTransactionID().equals(transactionID)) {
				return transaction;
			}
		}
		
		return null;
		
	}
	
	public static TransactionManager getInstance() {
		return Holder.instance;
	}
}
