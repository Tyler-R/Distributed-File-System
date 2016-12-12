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
	
	public ClientConnection(Socket socket) {
		this.socket = socket;
		
		try {
			inStream = socket.getInputStream();
			outStream = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			System.out.println("ERROR creating read/write streams for client");
		}
	}

	
	public byte[] getMessage() {
		
		byte buffer[] = new byte[4096];
		int bytesRead = 0;
		int totalBytesRead = 0;
		
		do {
			bytesRead = 0;
			try {
				bytesRead = inStream.read(buffer);
				if(bytesRead > -1) {
					totalBytesRead += bytesRead;
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
		
		byte bufferCopy[] = new byte[totalBytesRead];
		
		System.arraycopy(buffer, 0, bufferCopy, 0, totalBytesRead);
		return bufferCopy;
	}
}
