package com.github.crafterchen2.logoanim;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Objects;

//Classes {
public abstract class Parser {
	
	//Fields {
	public static final int FULLSCREEN = -2;
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
	
	public static MoodData parseMood(String string, MoodData def) {
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
	
	public static AssetData parseAsset(String string, AssetType validType, AssetData def) {
		IllegalArgumentException exc = new IllegalArgumentException(string + " is not a valid asset with type " + validType + ".");
		try {
			if (string == null || string.isBlank() || string.contentEquals(NULL)) return null;
			if (string.contentEquals(KEEP)) return def;
			AssetData parsed = AssetEnum.valueOf(string);
			if (parsed.getType() != validType) {
				throw exc;
			}
			return parsed;
		} catch (Exception _) {
			throw exc;
		}
	}
	
	/**
	 Generates a LogoConfig with custom Mood- and AssetProvider to reflect bitmap and date given in the longs.
	 
	 @param meta Bitmap for the border and colors. Every mood consists of 6 bits in <code>[R|r|G|g|B|b]</code> format.
	 this format converts to this opaque 32-bit ARGB color: <code>[0xFF|R*4|r*4|G*4|g*4|B*4|b*4]</code>.
	 Overall order:
	 <p><code>
	 [version|border|leftEyeMood|rightEyeMood|smileMood|BorderMood]
	 <p>
	 [63...60|59..24|23.......18|17........12|11......6|5........0]
	 </code>
	 @param face Bitmap for the face. Order:
	 <p><code>
	 [leftEye|rightEye|smile]
	 <p>
	 [63...48|47...32|31...0]
	 </code>
	 
	 @return The decoded configuration.
	 */
	private static LogoConfig parseLogo(final long meta, final long face) {
		int version = (int) ((face & 0xf_000000000_000_000L) >>> 60);
		if (version != 0) throw new IllegalArgumentException("Wrong version for this encoding! Only Version 0 accepted.");
		final int leftMap = (int) ((face & 0xffff_0000_00000000L) >>> 48);
		final int rightMap = (int) ((face & 0x0000_ffff_00000000L) >>> 32);
		final int smileMap = (int) (face & 0x0000_0000_ffffffffL);
		final long borderMap = (meta & 0x0_fffffffff_000_000L) >>> 24;
		final byte leftMood = (byte) ((meta & 0x0_000000000_fc0_000L) >>> 18);
		final byte rightMood = (byte) ((meta & 0x0_000000000_03f_000L) >>> 12);
		final byte smileMood = (byte) ((meta & 0x0_000000000_000_fc0L) >>> 6);
		final byte borderMood = (byte) (meta & 0x0_000000000_000_03fL);
		final Color leftColor = parseColor(leftMood);
		final Color rightColor = parseColor(rightMood);
		final Color smileColor = parseColor(smileMood);
		final Color borderColor = parseColor(borderMood);
		final ImmutableAssetProvider assetProvider = new ImmutableAssetProvider() {
			
			//Fields {
			final AssetData leftData = makeAssetData(leftMap, AssetType.EYE, meta, face);
			final AssetData rightData = makeAssetData(rightMap, AssetType.EYE, meta, face);
			final AssetData smileData = makeAssetData(smileMap, AssetType.SMILE, meta, face);
			final AssetData borderData = makeAssetData(borderMap, meta, face);
			//} Fields
			
			//Overrides {
			@Override
			public AssetData getAsset(final RegionEnum reg) {
				if (reg == null) throw new IllegalArgumentException("reg must not be null.");
				return switch (reg) {
					case LEFT_EYE -> leftData;
					case RIGHT_EYE -> rightData;
					case SMILE -> smileData;
					case DECO -> borderData;
				};
			}
			//} Overrides
		};
		final ImmutableMoodProvider moodProvider = reg -> switch (reg) {
			case LEFT_EYE -> () -> leftColor;
			case RIGHT_EYE -> () -> rightColor;
			case SMILE -> () -> smileColor;
			case DECO -> () -> borderColor;
		};
		return new LogoConfig(assetProvider, moodProvider);
	}
	
	private static AssetData makeAssetData(int map, AssetType type, long meta, long face) {
		return new AssetData() {
			//Fields {
			private final BufferedImage img = parseBitmap(map, (type == AssetType.SMILE) ? 8 : 4, 4);
			//} Fields
			
			//Overrides {
			@Override
			public BufferedImage getImg() {
				return img;
			}
			
			@Override
			public AssetType getType() {
				return type;
			}
			
			@Override
			public String getName() {
				return type + "(" + Long.toHexString(meta) + "-" + Long.toHexString(face) + ")";
			}
			//} Overrides
		};
	}
	
	private static AssetData makeAssetData(long map, long meta, long face) {
		return new AssetData() {
			//Fields {
			private final BufferedImage img = parseBorder(map, 10, 10);
			//} Fields
			
			//Overrides {
			@Override
			public BufferedImage getImg() {
				return img;
			}
			
			@Override
			public AssetType getType() {
				return AssetType.DECO;
			}
			
			@Override
			public String getName() {
				return getType() + "(" + Long.toHexString(meta) + "-" + Long.toHexString(face) + ")";
			}
			//} Overrides
		};
	}
	
	private static BufferedImage parseBorder(long map, int w, int h) {
		BufferedImage rv = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		int i = 0;
		for (int x = 0; x < 9; x++) {
			if ((map & (1L << i++)) != 0) rv.setRGB(x, 0, 0xffff_ffff);
		}
		for (int y = 0; y < 9; y++) {
			if ((map & (1L << i++)) != 0) rv.setRGB(w - 1, y, 0xffff_ffff);
		}
		for (int x = 9; x >= 0; x--) {
			if ((map & (1L << i++)) != 0) rv.setRGB(x, h - 1, 0xffff_ffff);
		}
		for (int y = 9; y >= 0; y--) {
			if ((map & (1L << i++)) != 0) rv.setRGB(0, y, 0xffff_ffff);
		}
		return rv;
	}
	
	private static BufferedImage parseBitmap(int map, int w, int h) {
		BufferedImage rv = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int i = y * w + x;
				if ((map & (1 << i)) != 0) rv.setRGB(x, y, 0xffff_ffff);
			}
		}
		return rv;
	}
	
	private static Color parseColor(byte color) {
		int rgb = 0;
		for (int j = 0; j < 6; j++) {
			rgb = ((rgb | ((color & (1 << j)) << (31 - j))) >> 3) >>> 1;
		}
		rgb >>>= 7;
		return new Color(rgb);
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
