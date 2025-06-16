package com.github.crafterchen2.logoanim.remote;

import java.util.ArrayList;

public class NetworkingDetails {

	public static final int VERSION = 2;
	@Deprecated(forRemoval = true)
	public static final int PORT = 7777;
	private static final ArrayList<String> whitelist = new ArrayList<>();
	
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
	
}
