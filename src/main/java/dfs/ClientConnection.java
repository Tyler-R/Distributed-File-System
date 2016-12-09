package main.java.dfs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientConnection {
	private BufferedReader inStream = null;
	private DataOutputStream outStream = null;
	
	private Socket socket = null;
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		
		try {
			inStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("ERROR creating read/write streams for client");
		}
	}

	
	public void listen() {
		while(!socket.isClosed()) {
			try {
				String receivedMessage = inStream.readLine();
				outStream.writeBytes(receivedMessage);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
