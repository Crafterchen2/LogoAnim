package com.github.crafterchen2.logoanim;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

//Enums {
public enum AssetEnum implements AssetData {
	
	EYE_2X1("2x1", AssetType.EYE),
	EYE_3X1("3x1", AssetType.EYE),
	EYE_1X1("1x1", AssetType.EYE),
	EYE_2X2("2x2", AssetType.EYE),
	EYE_3X3("3x3", AssetType.EYE),
	EYE_2X3("2x3", AssetType.EYE),
	BIG_TRIANGLE_RIGHT("big_triangle_right", AssetType.EYE),
	BIG_TRIANGLE_LEFT("big_triangle_left", AssetType.EYE),
	TRIANGLE_RIGHT("triangle_right", AssetType.EYE),
	TRIANGLE_LEFT("triangle_left", AssetType.EYE),
	CROSS("cross", AssetType.EYE),
	PLUS("plus", AssetType.EYE),
	CIRCLE("circle", AssetType.EYE),
	UP("up", AssetType.EYE),
	RIGHT("right", AssetType.EYE),
	DOWN("down", AssetType.EYE),
	LEFT("left", AssetType.EYE),
	NEUTRAL("neutral", AssetType.SMILE),
	NORMAL("normal", AssetType.SMILE),
	HAPPY("very_happy", AssetType.SMILE),
	SAD("sad", AssetType.SMILE),
	VERY_SAD("very_sad", AssetType.SMILE),
	SMIRK("smirk", AssetType.SMILE),
	MULTI("multi_border", AssetType.DECO),
	SOLID("solid_border", AssetType.DECO),
	BLUSH("blush", AssetType.DECO),
	QUESTION("question", AssetType.DECO),
	EXCLAMATION("exclamation", AssetType.DECO),
	DOTS("3_dot", AssetType.DECO),
	ROBOT("robot", AssetType.DECO),
	;
	
	//Fields {
	private static final HashMap<AssetData, BufferedImage> cache = HashMap.newHashMap(16);
	private final String name;
	private final AssetType type;
	//} Fields
	
	//Constructor {
	AssetEnum(String name, AssetType type) {
		this.name = name;
		this.type = type;
	}
	//} Constructor
	
	//Overrides {
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public AssetType getType() {
		return type;
	}
	
	@Override
	public BufferedImage getImg() {
		if (!cache.containsKey(this)) {
			try {
				String path = "/com/github/crafterchen2/logoanim/assets/logo/";
				path += switch (type) {
					case EYE -> "eyes/";
					case SMILE -> "smiles/";
					case DECO -> "decos/";
				};
				path += name;
				path += ".png";
				BufferedImage read = ImageIO.read(Objects.requireNonNull(AssetEnum.class.getResourceAsStream(path)));
				if (read.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
					BufferedImage img = new BufferedImage(read.getWidth(), read.getHeight(), BufferedImage.TYPE_INT_ARGB);
					Graphics g = img.getGraphics();
					g.drawImage(read, 0, 0, null);
					g.dispose();
					read = img;
				}
				cache.put(this, read);
			} catch (IOException e) {
				BufferedImage img = new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB);
				Graphics g = img.getGraphics();
				g.setColor(Color.MAGENTA);
				g.fillRect(0, 0, 2, 1);
				g.fillRect(2, 1, 1, 1);
				g.fillRect(0, 2, 1, 1);
				g.dispose();
				cache.put(this, img);
			}
		}
		return cache.get(this);
	}
	//} Overrides
	
}
//} Enums
