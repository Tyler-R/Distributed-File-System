package main.java.dfs;

public class Main {
	
	
	public static void printHelpMessage() {
		System.out.println("ERROR server must take 3 arguments: ./server <ipAddress> <port> <directory>");
		System.out.println("For example: ./server 127.0.0.1 8080 /mnt/test");
	}
	
	public static void main(String args[]) {
		if(args.length != 3) {
			printHelpMessage();
			System.exit(1);
		}
		
		// parse command line arguments
		String ipAddress = args[0];
		int port = -1;
		try {
			port = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			System.out.println("ERROR: Could not covert \"" + args[1] + "\" into a number. Enter a valid number for the port.");
			System.exit(1);
		}
		String fileSystemDirectory = args[2];
		
		Network network = new Network(ipAddress, port);
		
		ClientConnection connection = network.getClientConnection();
		
	}
}
