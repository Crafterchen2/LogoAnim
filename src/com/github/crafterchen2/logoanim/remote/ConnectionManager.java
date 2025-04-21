package com.github.crafterchen2.logoanim.remote;

import java.io.IOException;
import java.util.ArrayList;

public class ConnectionManager {
	
	public static final int PORT = 7777;
	private static final ArrayList<String> whitelist = new ArrayList<>();
	private static ServerConnection serverConnection;
	private static ClientConnection clientConnection;
	
	public static void addToWhitelist(String address) {
		whitelist.add(address);
	}
	
	public static void removeFromWhitelist(String address) {
		whitelist.remove(address);
	}
	
	public static void clearWhitelist() {
		whitelist.clear();
	}
	
	public static boolean isInWhitelist(String address) {
		return whitelist.contains(address);
	}
	
	public static ServerConnection initServerConnection() {
		try {
			serverConnection = new ServerConnection(PORT);
			return serverConnection;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static ClientConnection initClientConnection(String address, int port) {
		clientConnection = new ClientConnection(address, port);
		return clientConnection;
	}
	
	public static ServerConnection getServerConnection(){
		return serverConnection;
	}
	
	public static ClientConnection getClientConnection() {
		return clientConnection;
	}
	
}
