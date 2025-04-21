package com.github.crafterchen2.logoanim.remote;

import java.io.IOException;

public class ClientTest {
	
	public static void main(String[] args) {
		ClientConnection clientConnection = ConnectionManager.initClientConnection("localhost",7777);
		try {
			clientConnection.sendString("Hello World");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
