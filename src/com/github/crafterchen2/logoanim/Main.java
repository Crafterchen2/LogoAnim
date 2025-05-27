package com.github.crafterchen2.logoanim;

import com.github.crafterchen2.logoanim.frames.*;

import static com.github.crafterchen2.logoanim.Parser.*;

//Classes {
public class Main {
	
	//Methods {
	public static void main(String[] args) {
		boolean help = Parser.arrContains(args, "-h", "-?", "--help", "help");
		boolean list = Parser.arrContains(args, "-l", "--list");
		if (help) printHelp();
		if (list) printList();
		if (help || list) System.exit(0);
		int defIndex = args.length;
		int scale = DEFAULT.scale;
		AssetEnum leftEye = DEFAULT.getAsset(RegionEnum.LEFT_EYE);
		AssetEnum rightEye = DEFAULT.getAsset(RegionEnum.RIGHT_EYE);
		AssetEnum smile = DEFAULT.getAsset(RegionEnum.SMILE);
		AssetEnum deco = DEFAULT.getAsset(RegionEnum.DECO);
		MoodEnum leftEyeMood = DEFAULT.getMood(RegionEnum.LEFT_EYE);
		MoodEnum rightEyeMood = DEFAULT.getMood(RegionEnum.RIGHT_EYE);
		MoodEnum smileMood = DEFAULT.getMood(RegionEnum.SMILE);
		MoodEnum decoMood = DEFAULT.getMood(RegionEnum.DECO);
		boolean blink = DEFAULT.blink;
		for (int i = 0; i < args.length; i++) {
			if (!args[i].startsWith("-")) {
				defIndex = i;
				break;
			}
		}
		if (defIndex < args.length) {
			scale = Parser.parseScaleOrFullscreen(args[defIndex++], scale);
			if (defIndex < args.length) {
				blink = Parser.parseBlink(args[defIndex++], blink);
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
		}
		if (Parser.arrContains(args, "-r", "--remote")) {
			launchRemoteOnly(leftEye, rightEye, smile, deco, leftEyeMood, rightEyeMood, smileMood, decoMood, blink);
			return;
		}
		DisplayFrame l;
		if (scale <= FULLSCREEN) {
			new StreamFrame();
			return;
		} else {
			l = new LogoFrame();
			l.setScale(scale);
		}
		l.setAsset(RegionEnum.LEFT_EYE, leftEye);
		l.setAsset(RegionEnum.RIGHT_EYE, rightEye);
		l.setAsset(RegionEnum.SMILE, smile);
		l.setAsset(RegionEnum.DECO, deco);
		l.setMood(RegionEnum.LEFT_EYE, leftEyeMood);
		l.setMood(RegionEnum.RIGHT_EYE, rightEyeMood);
		l.setMood(RegionEnum.SMILE, smileMood);
		l.setMood(RegionEnum.DECO, decoMood);
		l.repaint();
		l.setShouldBlink(blink);
		if (Parser.arrContains(args, "-c", "--controller")) new LogoControlFrame(l);
		if (Parser.arrContains(args, "-p", "--presets")) new PresetLibraryFrame(l);
	}
	
	private static void launchRemoteOnly(AssetEnum leftEye, AssetEnum rightEye, AssetEnum smile, AssetEnum deco, MoodEnum leftEyeMood, MoodEnum rightEyeMood, MoodEnum smileMood, MoodEnum decoMood, boolean blink){
		
	}
	
	private static void printHelp() {
		System.out.println("Tool to create an interactive Crafterchen2 logo.");
		System.out.println("Usage: [OPTIONS] [scale | fullscreen [blink [leftEye rightEye smile deco [leftEyeMood rightEyeMood smileMood decoMood]]]]");
		System.out.println("-h, -?, --help, help : Prints this help and exits successfully.");
		System.out.println("-c, --controller     : Opens with a control frame attached.");
		System.out.println("-p, --presets        : Opens with a presets frame attached.");
		System.out.println("-l, --list           : Displays the possible values for the initial configuration and exits successfully.");
		System.out.println("-r, --remote         : launches the program in remote-control-only mode.");
	}
	
	private static void printAssets(AssetType type, String offset) {
		AssetEnum[] values = AssetEnum.values();
		String toPrint = null;
		for (AssetEnum value : values) {
			if (value.getType() == type) {
				if (toPrint != null) System.out.println(offset + "├ " + toPrint);
				toPrint = value.toString();
			}
		}
		if (toPrint != null) System.out.println(offset + "└ " + toPrint);
	}
	
	private static void printList() {
		System.out.println("List of valid Input Parameters:");
		System.out.println("├ Scale / Fullscreen:");
		System.out.println("│ ├ " + KEEP + " (= don't override the scale)");
		System.out.println("│ ├ number between 20 and 80 (inclusive)");
		System.out.println("│ ├ true (start the streaming-mode in fullscreen)");
		System.out.println("│ └ false (start the streaming-mode in small)");
		System.out.println("├ List of possible default asset values:");
		System.out.println("│ ├ Eyes (Regions: " + RegionEnum.LEFT_EYE + ", " + RegionEnum.RIGHT_EYE + "):");
		System.out.println("│ │ ├ " + NULL + " (= don't draw this region)");
		System.out.println("│ │ ├ " + KEEP + " (= don't override this region)");
		printAssets(AssetType.EYE, "│ │ ");
		System.out.println("│ ├ Smiles (Region: " + RegionEnum.SMILE + "):");
		System.out.println("│ │ ├ " + NULL + " (= don't draw this region)");
		System.out.println("│ │ ├ " + KEEP + " (= don't override this region)");
		printAssets(AssetType.SMILE, "│ │ ");
		System.out.println("│ └ Decos (Region: " + RegionEnum.DECO + "):");
		System.out.println("│   ├ " + NULL + " (= don't draw this region)");
		System.out.println("│   ├ " + KEEP + " (= don't override this region)");
		printAssets(AssetType.DECO, "│   ");
		System.out.println("├ List of possible default mood values:");
		System.out.println("│ ├ " + NULL + " (= don't recolor the asset)");
		System.out.println("│ ├ " + KEEP + " (= don't override the recoloration)");
		MoodEnum[] moodEnums = MoodEnum.values();
		for (int i = 0; i < moodEnums.length; i++) {
			System.out.println("│ " + ((i == moodEnums.length - 1) ? '└' : '├') + " " + moodEnums[i]);
		}
		System.out.println("└ Blink:");
		System.out.println("  ├ " + KEEP + " (= don't override the blink setting)");
		System.out.println("  ├ true (= Logo will blink)");
		System.out.println("  └ false (= Logo won't blink)");
	}
	//} Methods
	
}
//} Classes