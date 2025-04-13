package com.github.crafterchen2.logoanim;

import java.util.Objects;

//Classes {
public class Parser {
	
	public static final int FULLSCREEN = -2;
	//Fields {
	public static final String KEEP = "keep";
	public static final String NULL = "null";
	public static final String CSV_KEY = "leftEye, rightEye, smile, deco, leftEyeMood, rightEyeMood, smileMood, decoMood";
	public static final String CSV_FULL_KEY = "scale, blink, " + CSV_KEY;
	public static final FullLogoConfig DEFAULT = new FullLogoConfig(
			20, true,
			new ImmutableAssetProvider.Default(AssetEnum.EYE_2X2, AssetEnum.EYE_2X2, AssetEnum.NORMAL, null),
			new ImmutableMoodProvider.Default(MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL, null)
	);
	//} Fields
	
	//Constructor {
	public Parser() {
		
	}
	//} Constructor
	
	//Methods {
	private static FullLogoConfig parseFullLogo(String entry) {
		String[] arr = entry.split(", ", 3);
		RegionEnum[] regs = RegionEnum.values();
		int scale = parseScaleOrFullscreen(arr[0], DEFAULT.scale);
		boolean blink = parseBlink(arr[1], DEFAULT.blink);
		return new FullLogoConfig(scale, blink, parseLogo(arr[2]));
	}
	
	private static LogoConfig parseLogo(String entry) {
		String[] arr = entry.split(", ");
		int iArr = 0;
		RegionEnum[] regs = RegionEnum.values();
		AssetProvider assets = new AssetProvider.Default();
		MoodProvider moods = new MoodProvider.Default();
		for (RegionEnum reg : regs) {
			assets.setAsset(reg, parseAsset(arr[iArr++], reg.type, DEFAULT.getAsset(reg)));
		}
		for (RegionEnum reg : regs) {
			moods.setMood(reg, parseMood(arr[iArr++], DEFAULT.getMood(reg)));
		}
		return new LogoConfig(new ImmutableAssetProvider.Default(assets), new ImmutableMoodProvider.Default(moods));
	}
	
	public static MoodEnum parseMood(String string, MoodEnum def) {
		try {
			if (string == null || string.isBlank() || string.contentEquals(NULL)) return null;
			if (string.contentEquals(KEEP)) return def;
			return MoodEnum.valueOf(string);
		} catch (Exception _) {
			System.err.println(string + " is not a valid mood.");
			System.exit(1);
			return null;
		}
	}
	
	public static AssetEnum parseAsset(String string, AssetType validType, AssetEnum def) {
		IllegalArgumentException exc = new IllegalArgumentException(string + " is not a valid asset with type " + validType + ".");
		try {
			if (string == null || string.isBlank() || string.contentEquals(NULL)) return null;
			if (string.contentEquals(KEEP)) return def;
			AssetEnum parsed = AssetEnum.valueOf(string);
			if (parsed.getType() != validType) {
				throw exc;
			}
			return parsed;
		} catch (Exception _) {
			throw exc;
		}
	}
	
	public static int parseScaleOrFullscreen(String string, int def) {
		if (string.contentEquals(KEEP)) return def;
		if (string.contentEquals("true")) return FULLSCREEN;
		if (string.contentEquals("false")) return FULLSCREEN * 2;
		IllegalArgumentException exc = new IllegalArgumentException("scale must be an integer between 10 and 80 (inclusive) or \"true\" / \"false\" for fullscreen.");
		try {
			int parsed = Integer.parseInt(string);
			if (parsed < RegionEnum.base || parsed > RegionEnum.base * 8) {
				throw exc;
			}
			return parsed;
		} catch (Exception _) {
			throw exc;
		}
	}
	
	public static boolean parseBlink(String string, boolean def) {
		if (string.contentEquals(KEEP)) return def;
		IllegalArgumentException exc = new IllegalArgumentException("blink must be either true or false.");
		try {
			if (string.contentEquals("true")) return true;
			if (string.contentEquals("false")) return false;
			throw exc;
		} catch (Exception _) {
			throw exc;
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
	//} Methods
}
//} Classes
