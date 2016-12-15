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
		synchronized(this) {
			transactions.add(transaction);
		}
		
		return transaction;
		
	}
	
	public Transaction getTransaction(BigInteger transactionID) {
		synchronized(this) {
			for(Transaction transaction: transactions) {
				if(transaction.getTransactionID().equals(transactionID)) {
					return transaction;
				}
			}
			
			return null;
		}
	}
	
	public void addExistingTransaction(Transaction transaction) {
		
		transactions.add(transaction);
		
		if(transaction.getTransactionID().compareTo(BigInteger.valueOf(currentTransactionIDCounter.get())) > 0) {
			currentTransactionIDCounter.set(transaction.getTransactionID().longValue());
		}
	}
	
	public void removeTransaction(Transaction transaction) {
		transactions.remove(transaction);
		transaction.stopTimeout();
		
		if(transaction.getTransactionID().equals(BigInteger.valueOf(currentTransactionIDCounter.get()))) {
			currentTransactionIDCounter.decrementAndGet();
		}
	}
	
	public static TransactionManager getInstance() {
		return Holder.instance;
	}
}
