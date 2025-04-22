package com.github.crafterchen2.logoanim.remote;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.frames.DisplayFrame;
import com.github.crafterchen2.logoanim.frames.LogoFrame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServerTest {

	private static HashMap<ServerConnection.SocketInfo, LogoFrame> frames = new HashMap<>();

	public static void main(String[] args) {
		NetworkingDetails.addToWhitelist("/127.0.0.1");
		try {
			ServerConnection serverConnection = new ServerConnection(new ServerConnection.RequestHandler() {
				@Override
				public String handleMessage(ServerConnection.SocketInfo socket, String msg) {
					LogoConfig config = parseConfig(msg);
					ServerConnection.SocketInfo reduced = new ServerConnection.SocketInfo(socket.ip(), -1, socket.alias());
					if (!frames.containsKey(reduced)) {
						frames.put(reduced, new LogoFrame());
					}
					LogoFrame frame = frames.get(reduced);
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
			System.in.read();
			System.out.println("Closing");
			frames.forEach((k, v) -> {
				v.dispose();
			});
			serverConnection.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static LogoConfig parseConfig(String config) {
		String[] parts = config.split(" ");
		AssetEnum leftEyeAsset = parseAsset(parts[0]);
		AssetEnum rightEyeAsset = parseAsset(parts[1]);
		AssetEnum smileAsset = parseAsset(parts[2]);
		AssetEnum decoAsset = parseAsset(parts[3]);
		ImmutableAssetProvider assetProvider = new ImmutableAssetProvider.Default(leftEyeAsset, rightEyeAsset, smileAsset, decoAsset);
		MoodEnum leftEyeMood = parseMood(parts[4]);
		MoodEnum rightEyeMood = parseMood(parts[5]);
		MoodEnum smileMood = parseMood(parts[6]);
		MoodEnum decoMood = parseMood(parts[7]);
		ImmutableMoodProvider moodProvide = new ImmutableMoodProvider.Default(leftEyeMood, rightEyeMood, smileMood, decoMood);
		return new LogoConfig(assetProvider, moodProvide);
	}

	private static AssetEnum parseAsset(String part) {
		if (Objects.equals(part, "null")) {
			return null;
		}
		return AssetEnum.valueOf(part);
	}

	private static MoodEnum parseMood(String part) {
		if (Objects.equals(part, "null")) {
			return null;
		}
		return MoodEnum.valueOf(part);
	}

}
