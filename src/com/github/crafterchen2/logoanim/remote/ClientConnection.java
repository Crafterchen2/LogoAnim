package com.github.crafterchen2.logoanim.remote;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
	
	private final String ip;
	private final int port;
	private final String alias;
	
	public ClientConnection(String ip, int port, String alias){
		this.ip = ip;
		this.port = port;
		this.alias = alias;
	}
	
	public void sendString(String str) throws IOException {
		// establish Connection
		Socket socket = new Socket(ip, port);
		// get the output stream from the socket.
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		// write the header
		out.println(ConnectionManager.VERSION);
		out.println(alias);
		// write the message
		out.println(str);
		out.close();
		socket.close();
	}
	
}
