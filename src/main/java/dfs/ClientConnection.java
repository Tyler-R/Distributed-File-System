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
	private InputStream inStream = null;
	private OutputStream outStream = null;
	
	private Socket socket = null;
	
	public static final int BUFFER_LENGTH = 4096;
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		
		try {
			inStream = socket.getInputStream();
			outStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("ERROR creating read/write streams for client");
		}
	}

	/*Receives an arbitrarily length message*/
	public String getMessage() {
		
		byte buffer[] = new byte[BUFFER_LENGTH];
		String message = "";
		int bytesRead = 0;
		
		do {
			bytesRead = 0;
			try {
				bytesRead = inStream.read(buffer, 0, buffer.length);
				if(bytesRead > -1) {
					message += new String(buffer, 0, bytesRead);
				}
				
			} catch (SocketException e) {					
				System.out.println("pipe broken");
				try {
					socket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				break;
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		} while(bytesRead > 0);
						
		return message;
	}
}
