package main.java.dfs.message.request;

import main.java.dfs.Main;
import main.java.dfs.RecoveryLog;
import main.java.dfs.message.Message;

public class ExitMessage implements Message {
	public static String METHID_ID = "EXIT";
	
	@Override
	public void execute() {		
		 for(Thread thread : Main.threads) {
			thread.interrupt();
			
			try {
				thread.join(200);
			} catch (InterruptedException e) {
				// do nothing
			}
		}
		
		RecoveryLog.deleteLog();
		System.out.println("recovery log deleted");
		System.out.println("server exited gracefully");
		System.exit(0);
		
	}
}
