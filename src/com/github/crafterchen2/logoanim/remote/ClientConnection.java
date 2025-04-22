package com.github.crafterchen2.logoanim.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientConnection {
	
	private final String ip;
	private final int port;
	private final String alias;
	private int timeout;
	
	public ClientConnection(String ip, int port, String alias){
		this.ip = ip;
		this.port = port;
		this.alias = alias;
		this.timeout = 2000;
	}

	/**
	 * Send String to specified endpoint.
	 *
	 * @return Response from endpoint
	 * @param str String to send
	 * @throws IOException
	 * @throws java.net.SocketTimeoutException if specified timeout has been reached
	 */
	public String sendString(String str) throws IOException {
		// establish Connection
		Socket socket = new Socket(ip, port);
		socket.setSoTimeout(timeout);
		// get the output stream from the socket.
		PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
		
		// write the header
		out.println(NetworkingDetails.VERSION);
		out.println(alias);
		// write the message
		out.println(str);

		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		String response = in.readLine();

		out.close();
		in.close();
		socket.close();
		return response;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
}
