package com.github.crafterchen2.logoanim.remote;

import java.io.IOException;

public class ServerTest {
	
	public static void main(String[] args) {
		ConnectionManager.addToWhitelist("/127.0.0.1");
		try {
			ServerConnection serverConnection = ConnectionManager.initServerConnection();
			serverConnection.addMessageListener((socket, msg) -> {
				System.out.println(socket+" : \""+msg+"\"");
			});
			serverConnection.addRefuseListener((socket, reason) -> {
				System.out.println(reason.name()+" : "+socket);
			});
			serverConnection.start();
			System.in.read();
			System.out.println("Closing");
			serverConnection.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}
