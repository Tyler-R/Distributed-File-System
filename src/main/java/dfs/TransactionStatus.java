package main.java.dfs;

public enum TransactionStatus {
	IN_PROGRESS,
	COMMIT_WHEN_ALL_WRITES_RECEIVED,
	COMMITTED,
	ABORTED, 
	TIMER_EXPIRED;

}
