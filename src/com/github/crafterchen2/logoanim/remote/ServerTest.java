package com.github.crafterchen2.logoanim.remote;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.frames.LogoFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ServerTest {

	private static HashMap<ServerConnection.SocketInfo, LogoFrame> frames = new HashMap<>();

	public static void main(String[] args) {
		NetworkingDetails.addToWhitelist("/127.0.0.1");
		NetworkingDetails.addToWhitelist("/0:0:0:0:0:0:0:1");
		try {
			ServerConnection serverConnection = new ServerConnection(new ServerConnection.RequestHandler() {
				@Override
				public String handleMessage(ServerConnection.SocketInfo socket, String msg) {
					LogoConfig config = Parser.parseLogo(msg, Parser.DEFAULT);
					ServerConnection.SocketInfo reduced = new ServerConnection.SocketInfo(socket.ip(), -1, socket.alias());
					if (!frames.containsKey(reduced)) {
						frames.put(reduced, new LogoFrame());
					}
					LogoFrame frame = frames.get(reduced);
					frame.setScale(20);
					frame.setAsset(config);
					frame.setMood(config);
					frame.repaint();
					return "Received: " + msg;
				}

				@Override
				public void handleRefuse(ServerConnection.SocketInfo socket, ServerConnection.RefuseReason reason) {
					System.out.println("✖ " + socket + " : " + reason);
				}
			});
			serverConnection.start();
			System.out.println(serverConnection.getLocalPort());
			System.in.read();
			System.out.println("Closing");
			frames.forEach((k, v) -> v.dispose());
			serverConnection.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
