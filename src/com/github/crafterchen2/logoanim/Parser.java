package com.github.crafterchen2.logoanim;

import java.util.Arrays;
import java.util.Objects;

//Classes {
public abstract class Parser {
	
	public static final int FULLSCREEN = -2;
	//Fields {
	public static final String KEEP = "keep";
	public static final String NULL = "null";
	public static final FullLogoConfig DEFAULT = new FullLogoConfig(
			20, true,
			new ImmutableAssetProvider.Default(AssetEnum.EYE_2X2, AssetEnum.EYE_2X2, AssetEnum.NORMAL, null),
			new ImmutableMoodProvider.Default(MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL, null)
	);
	//} Fields
	
	//Methods {
	public static LogoConfig parseLogo(String string, LogoConfig def) {
		if (string == null || string.isBlank() || string.contentEquals(NULL)) return parseLogo(null, null, def);
		return parseLogo(string.split(" "), def);
	}
	
	public static LogoConfig parseLogo(String[] assetsMoods, LogoConfig def) {
		if (assetsMoods == null) return parseLogo(null, null, def);
		int l = RegionEnum.values().length;
		if (assetsMoods.length <= l) {
			return parseLogo(assetsMoods, null, def);
		} else {
			String[] assets = Arrays.copyOfRange(assetsMoods, 0, l);
			String[] moods = Arrays.copyOfRange(assetsMoods, l, l * 2);
			return parseLogo(assets, moods, def);
		}
	}
	
	public static LogoConfig parseLogo(String[] assets, String[]moods, LogoConfig def) {
		RegionEnum[] regs = RegionEnum.values();
		int l = regs.length;
		if (assets != null) {
			if (assets.length != l) assets = Arrays.copyOf(assets, l);
		} else {
			assets = new String[l];
		}
		if (moods != null) {
			if (moods.length != l) moods = Arrays.copyOf(moods, l);
		} else {
			moods = new String[l];
		}
		if (def == null) def = new LogoConfig();
		AssetProvider aProv = new AssetProvider.Default();
		MoodProvider mProv = new MoodProvider.Default();
		for (int i = 0; i < l; i++) {
			aProv.setAsset(regs[i], parseAsset(assets[i], regs[i].type, def.getAsset(regs[i])));
		}
		for (int i = 0; i < l; i++) {
			mProv.setMood(regs[i], parseMood(moods[i], def.getMood(regs[i])));
		}
		return new LogoConfig(aProv, mProv);
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
