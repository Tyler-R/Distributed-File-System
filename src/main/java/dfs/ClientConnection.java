package main.java.dfs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientConnection {
	private BufferedReader inStream = null;
	private OutputStream outStream = null;
	
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

	/*Receives an arbitrarily length message*/
	public NetworkMessage getMessage() {
				

		try {
			NetworkMessage message = new NetworkMessage();
			StringBuilder data = new StringBuilder();
			
			// read header until the first /r/n/
			message.header = inStream.readLine();
			// squash the second /r/n line that separates data length from data.
			inStream.readLine();
			data.append(inStream.readLine());
			
			while(inStream.ready()) {
				char character = (char) inStream.read();
				data.append(character);
			}
			
			message.data = data.toString();
			
			return message;
			
		} catch (SocketException e) {					
			System.out.println("pipe broken");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

						
		return null;
	}
	
	public void sendMessage(String message) throws IOException {
		outStream.write(message.getBytes());
		outStream.flush();
		outStream.flush();
	}
}
