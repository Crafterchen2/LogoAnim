package com.github.crafterchen2.logoanim;

import com.github.crafterchen2.logoanim.components.LogoControl;

import javax.swing.*;
import java.util.Objects;
import java.util.function.Predicate;

//Classes {
public class Main {
	
	//Methods {
	public static void main(String[] args) {
		boolean help, list;
		if (help = arrContains(args, "-h", "-?", "--help", "help")) {
			System.out.println("Tool to create an interactive Crafterchen2 logo.");
			System.out.println("Usage: [OPTIONS] [scale [leftEye rightEye smile deco [leftEyeMood rightEyeMood smileMood decoMood]]]");
			System.out.println("-h, -?, --help, help : Prints this help and exits successfully.");
			System.out.println("-c, --controller     : Opens with a control frame attached.");
			System.out.println("-p, --presets        : Opens with a presets frame attached.");
			System.out.println("-l, --list           : Displays the possible values for the initial configuration and exits successfully.");
		}
		if (list = arrContains(args, "-l", "--list")) {
			System.out.println("List of possible default asset values:");
			System.out.println("Eyes (Regions: " + RegionEnum.LEFT_EYE + ", " + RegionEnum.RIGHT_EYE + "):");
			System.out.println("* null (= don't draw this region)");
			System.out.println("* keep (= don't override this region)");
			for (AssetEnum asset : AssetEnum.values()) {
				if (asset.getType() == AssetType.EYE) System.out.println("* " + asset);
			}
			System.out.println("\nSmiles (Region: " + RegionEnum.SMILE + "):");
			System.out.println("* null (= don't draw this region)");
			System.out.println("* keep (= don't override this region)");
			for (AssetEnum asset : AssetEnum.values()) {
				if (asset.getType() == AssetType.SMILE) System.out.println("* " + asset);
			}
			System.out.println("\nDecos (Region: " + RegionEnum.DECO + "):");
			System.out.println("* null (= don't draw this region)");
			System.out.println("* keep (= don't override this region)");
			for (AssetEnum asset : AssetEnum.values()) {
				if (asset.getType() == AssetType.DECO) System.out.println("* " + asset);
			}
			System.out.println("\nList of possible default mood values:");
			System.out.println("* null (= don't recolor the asset)");
			System.out.println("* keep (= don't override the recoloration)");
			for (MoodEnum mood : MoodEnum.values()) {
				System.out.println("* " + mood);
			}
		}
		if (help || list) System.exit(0);
		int defIndex = args.length;
		int scale = 20;
		AssetEnum leftEye = AssetEnum.EYE_2X2;
		AssetEnum rightEye = AssetEnum.EYE_2X2;
		AssetEnum smile = AssetEnum.NORMAL;
		AssetEnum deco = null;
		MoodEnum leftEyeMood = MoodEnum.NORMAL;
		MoodEnum rightEyeMood = MoodEnum.NORMAL;
		MoodEnum smileMood = MoodEnum.NORMAL;
		MoodEnum decoMood = null;
		for (int i = 0; i < args.length; i++) {
			if (!args[i].startsWith("-")) {
				defIndex = i;
				break;
			}
		}
		if (defIndex < args.length) {
			scale = parseInt(args[defIndex], parsed -> parsed >= RegionEnum.base && parsed <= RegionEnum.base * 8, "parsed must be between " + RegionEnum.base + " and " + RegionEnum.base * 8 + " (inclusive).");
			defIndex++;
			if (defIndex < args.length - 3) {
				leftEye = parseAsset(args[defIndex++], AssetType.EYE, leftEye);
				rightEye = parseAsset(args[defIndex++], AssetType.EYE, rightEye);
				smile = parseAsset(args[defIndex++], AssetType.SMILE, smile);
				deco = parseAsset(args[defIndex++], AssetType.DECO, deco);
				if (defIndex < args.length - 3) {
					leftEyeMood = parseMood(args[defIndex++], leftEyeMood);
					rightEyeMood = parseMood(args[defIndex++], rightEyeMood);
					smileMood = parseMood(args[defIndex++], smileMood);
					decoMood = parseMood(args[defIndex++], decoMood);
					if (defIndex < args.length) {
						System.err.println("Too many arguments!");
						System.exit(1);
					}
				} else if (defIndex < args.length) {
					System.err.println("Not enough default mood values found. Silently ignoring and not applying changes.");
				}
			} else if (defIndex < args.length) {
				System.err.println("Not enough default asset values found. Silently ignoring and not applying changes.");
			}
		}
		LogoFrame l = new LogoFrame();
		l.setScale(scale);
		l.leftEye = leftEye;
		l.rightEye = rightEye;
		l.smile = smile;
		l.deco = deco;
		l.leftEyeMood = leftEyeMood;
		l.rightEyeMood = rightEyeMood;
		l.smileMood = smileMood;
		l.decoMood = decoMood;
		l.repaint();
		if (arrContains(args, "-c", "--controller")) new LogoControlFrame(l);
		if (arrContains(args, "-p", "--presets")) {
			
		}
	}
	
	private static MoodEnum parseMood(String string, MoodEnum def) {
		try {
			if (string == null || string.isBlank() || string.contentEquals("null")) return null;
			if (string.contentEquals("keep")) return def;
			return MoodEnum.valueOf(string);
		} catch (Exception _) {
			System.err.println(string + "is not a valid mood.");
			System.exit(1);
			return null;
		}
	}
	
	private static AssetEnum parseAsset(String string, AssetType validType, AssetEnum def) {
		final String errMsg = string + "is not a valid asset with type " + validType + ".";
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
	
	private static int parseInt(String string, Predicate<Integer> validCheck, String errMsg) {
		try {
			int parsed = Integer.parseInt(string);
			if (!validCheck.test(parsed)) throw new IllegalArgumentException(errMsg);
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
	//} Methods
	
}
//} Classes