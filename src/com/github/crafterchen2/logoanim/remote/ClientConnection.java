package com.github.crafterchen2.logoanim.remote;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientConnection {
	
	private final String ip;
	private final int port;
	
	public ClientConnection(String ip, int port){
		this.ip = ip;
		this.port = port;
	}
	
	public void sendString(String str) throws IOException {
		// Establish Connection
		Socket socket = new Socket(ip, port);
		// get the output stream from the socket.
		OutputStream outputStream = socket.getOutputStream();
		// create a data output stream from the output stream so we can send data through it
		DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
		
		System.out.println("Sending string to the ServerSocket");
		
		// write the message we want to send
		dataOutputStream.writeUTF(str);
		dataOutputStream.flush(); // send the message
		dataOutputStream.close(); // close the output stream when we're done.
		socket.close();
	}
	
}
