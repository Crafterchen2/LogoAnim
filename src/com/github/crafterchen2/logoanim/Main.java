package com.github.crafterchen2.logoanim;

import com.github.crafterchen2.logoanim.frames.LogoControlFrame;
import com.github.crafterchen2.logoanim.frames.LogoFrame;
import com.github.crafterchen2.logoanim.frames.PresetLibraryFrame;

//Classes {
public class Main {
	
	//Methods {
	public static void main(String[] args) {
		boolean help = Parser.arrContains(args, "-h", "-?", "--help", "help");
		boolean list = Parser.arrContains(args, "-l", "--list");
		if (help) {
			System.out.println("Tool to create an interactive Crafterchen2 logo.");
			System.out.println("Usage: [OPTIONS] [scale [leftEye rightEye smile deco [leftEyeMood rightEyeMood smileMood decoMood]]]");
			System.out.println("-h, -?, --help, help : Prints this help and exits successfully.");
			System.out.println("-c, --controller     : Opens with a control frame attached.");
			System.out.println("-p, --presets        : Opens with a presets frame attached.");
			System.out.println("-l, --list           : Displays the possible values for the initial configuration and exits successfully.");
		}
		if (list) {
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
			scale = Parser.parseScale(args[defIndex]);
			defIndex++;
			if (defIndex < args.length - 3) {
				leftEye = Parser.parseAsset(args[defIndex++], AssetType.EYE, leftEye);
				rightEye = Parser.parseAsset(args[defIndex++], AssetType.EYE, rightEye);
				smile = Parser.parseAsset(args[defIndex++], AssetType.SMILE, smile);
				deco = Parser.parseAsset(args[defIndex++], AssetType.DECO, deco);
				if (defIndex < args.length - 3) {
					leftEyeMood = Parser.parseMood(args[defIndex++], leftEyeMood);
					rightEyeMood = Parser.parseMood(args[defIndex++], rightEyeMood);
					smileMood = Parser.parseMood(args[defIndex++], smileMood);
					decoMood = Parser.parseMood(args[defIndex++], decoMood);
					if (defIndex < args.length) {
						System.err.println("Too many arguments.");
						System.exit(1);
					}
				} else if (defIndex < args.length) {
					System.err.println("Not enough default mood values found.");
					System.exit(1);
				}
			} else if (defIndex < args.length) {
				System.err.println("Not enough default asset values found.");
				System.exit(1);
			}
		}
		LogoFrame l = new LogoFrame();
		l.setScale(scale);
		l.setAsset(RegionEnum.LEFT_EYE, leftEye);
		l.setAsset(RegionEnum.RIGHT_EYE, rightEye);
		l.setAsset(RegionEnum.SMILE, smile);
		l.setAsset(RegionEnum.DECO, deco);
		l.setMood(RegionEnum.LEFT_EYE, leftEyeMood);
		l.setMood(RegionEnum.RIGHT_EYE, rightEyeMood);
		l.setMood(RegionEnum.SMILE, smileMood);
		l.setMood(RegionEnum.DECO, decoMood);
		l.repaint();
		l.setShouldBlink(true);
		if (Parser.arrContains(args, "-c", "--controller")) new LogoControlFrame(l);
		if (Parser.arrContains(args, "-p", "--presets")) new PresetLibraryFrame(l);
	}
	//} Methods
	
}
//} Classes