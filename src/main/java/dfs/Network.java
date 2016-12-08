package main.java.dfs;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Network {
		
	public static final int BACKLOG = 500;
	
	private ServerSocket serverSocket;
	
	public Network(String ipAddress, int port) {		
        try {
        	serverSocket = new ServerSocket(port, BACKLOG, convertToInetAddress(ipAddress));
		} catch (IOException e) {
			System.out.println("ERROR creating socket to listen on port \"" + port 
					+ "\" on address \"" + convertToInetAddress(ipAddress).getHostAddress() 
					+ "\" with backlog \"" + BACKLOG + "\"");
		}
	}
	
	private InetAddress convertToInetAddress(String ipAddress) {
		InetAddress inetAddress = null;
		
		try {
			inetAddress = InetAddress.getByName(ipAddress);
		} catch (UnknownHostException e1) {
			System.out.println("ERROR: could not resolve \"" + ipAddress + "\" as a valid IP address");
			System.exit(1);
		}
		
		return inetAddress;
	}
	
	public ClientConnection getClientConnection() {
		Socket socket = null;
		while(socket == null){
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				System.out.println("ERROR: server could not accept connection on \"" + getAddress() + ":" + getPort() + "\" " );
			}	
		}
		
		return new ClientConnection(socket);
	}
	
	public int getPort() {
		return serverSocket.getLocalPort();
	}
	
	public String getAddress() {
		return serverSocket.getLocalSocketAddress().toString();
	}

}
