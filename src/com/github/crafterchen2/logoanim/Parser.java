package com.github.crafterchen2.logoanim;

import java.util.Objects;

public class Parser {
	
	public Parser() {
		
	}
	
	public static MoodEnum parseMood(String string, MoodEnum def) {
		try {
			if (string == null || string.isBlank() || string.contentEquals("null")) return null;
			if (string.contentEquals("keep")) return def;
			return MoodEnum.valueOf(string);
		} catch (Exception _) {
			System.err.println(string + " is not a valid mood.");
			System.exit(1);
			return null;
		}
	}
	
	public static AssetEnum parseAsset(String string, AssetType validType, AssetEnum def) {
		final String errMsg = string + " is not a valid asset with type " + validType + ".";
		try {
			if (string == null || string.isBlank() || string.contentEquals("null")) return null;
			if (string.contentEquals("keep")) return def;
			AssetEnum parsed = AssetEnum.valueOf(string);
			if (parsed.getType() != validType) throw new IllegalArgumentException(errMsg);
			return parsed;
		} catch (Exception _) {
			System.err.println(errMsg);
			System.exit(1);
			return null;
		}
	}
	
	public static int parseScale(String string) {
		String errMsg = "scale must be an integer between 10 and 80 (inclusive).";
		try {
			int parsed = Integer.parseInt(string);
			if (parsed < RegionEnum.base || parsed > RegionEnum.base * 8) throw new IllegalArgumentException(errMsg);
			return parsed;
		} catch (Exception _) {
			System.err.println(errMsg);
			System.exit(1);
			return Integer.MIN_VALUE;
		}
	}
	
	@SafeVarargs
	public static <T> boolean arrContains(T[] arr, T... toFind) {
		if (arr == null || toFind == null) return false;
		for (T t : arr) {
			for (T f : toFind) {
				if (Objects.equals(t, f)) return true;
			}
		}
		return false;
	}
}
