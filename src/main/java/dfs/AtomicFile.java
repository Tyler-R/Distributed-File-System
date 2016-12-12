package main.java.dfs;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.SocketException;
import java.nio.channels.FileLock;

public class AtomicFile {
	
	private String fileName = "";
	public static String directory = "";
	
	public static final int BUFFER_LENGTH = 4096;
	
	public AtomicFile(String fileName) {
		this.fileName = fileName;
	}
	
	public void Append(String data) {
		
	}
	
	public String read() throws FileNotFoundException, IOException {
		File file = new File(directory + fileName);
		FileInputStream inStream = new FileInputStream(file);
		
		byte buffer[] = new byte[BUFFER_LENGTH];
		String message = "";
		int bytesRead = 0;
		
		try {
			FileLock lock = inStream.getChannel().lock(0L, Long.MAX_VALUE, true);
			try {
				do {
					bytesRead = 0;

					bytesRead = inStream.read(buffer, 0, buffer.length);
					if(bytesRead > -1) {
						message += new String(buffer, 0, bytesRead);
					}
				} while(bytesRead > 0);
								
			} finally {
				lock.release();
			} 
			
		} catch (IOException e) {
			String errorMessage = "Issue reading " + fileName + " because: " + (e.getMessage() != null ? e.getMessage() : "file could no be read");
			throw(new IOException(errorMessage)); 
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				String errorMessage = "Issue closing " + fileName + " because: " + (e.getMessage() != null ? e.getMessage() : " file could not be closed");
				throw(new IOException(errorMessage)); 
			}
		}
		
		
		
		return message;
	}
	
	
}
