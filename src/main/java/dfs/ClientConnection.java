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
	
	public static final int BUFFER_LENGTH = 4096;
	
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
	public String getMessage() {
		
		byte buffer[] = new byte[BUFFER_LENGTH];
		String message = "";
		
		System.out.println("starting to read network msg");
		

		try {
			// read header
			message = inStream.readLine();
			// squash a line to remove /r/n/r/n that seperates data length from data.
			inStream.readLine();
			// read data
			String data = inStream.readLine();
			// add back on data and newline buffer to message
			message += "\r\n\r\n" + data;
			
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
		

						
		return message;
	}
	
	public void sendMessage(String message) throws IOException {
		outStream.write(message.getBytes());
		outStream.flush();
		outStream.flush();
	}
}
