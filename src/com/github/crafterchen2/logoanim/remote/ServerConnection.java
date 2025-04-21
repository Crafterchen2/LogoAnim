package com.github.crafterchen2.logoanim.remote;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class ServerConnection {
	
	private final ServerSocket serverSocket;
	private final ArrayList<MessageListener> listeners = new ArrayList<>();
	private final Thread thread = new Thread(this::mainLoop);
	
	public ServerConnection(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(1);
	}
	
	public void close() throws IOException {
		if (thread.isAlive()) {
			thread.interrupt();
		}
		while (thread.isAlive()) {
		}
		serverSocket.close();
	}
	
	public void addMessageListener(MessageListener listener) {
		listeners.add(listener);
	}
	
	//try(stuff) {
	// 
	// }
	
	private void mainLoop() {
		try {
			while (!thread.isInterrupted()) {
				try{
					Socket client = serverSocket.accept();
					if (ConnectionManager.isInWhitelist(client.getInetAddress().toString())) {
						// get the input stream from the connected socket
						InputStream inputStream = client.getInputStream();
						// create a DataInputStream so we can read data from it.
						DataInputStream dataInputStream = new DataInputStream(inputStream);
						
						// read the message from the socket
						String message = dataInputStream.readUTF();
						for (MessageListener listener : listeners) {
							listener.handleMessage(new SocketInfo(client.getInetAddress().toString(), client.getPort()), message);
						}
						client.close();
					}else {
						System.out.println("Refused connection from "+new SocketInfo(client.getInetAddress().toString(),client.getPort()));
					}
				}catch (SocketTimeoutException timeoutException){
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void start() throws RuntimeException {
		if (thread.isAlive()) {
			throw new RuntimeException("Thread already running");
		} else {
			thread.start();
		}
	}
	
	public record SocketInfo(String ip, int port) {
	
	}
	
	public interface MessageListener {
		void handleMessage(SocketInfo socket, String msg);
	}
	
}
