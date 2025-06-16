package com.github.crafterchen2.logoanim;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

//Enums {
public enum AssetEnum implements AssetData{
	
	EYE_2X1("2x1_eye", AssetType.EYE),
	EYE_3X1("3x1_eye", AssetType.EYE),
	EYE_1X1("1x1_eye", AssetType.EYE),
	EYE_2X2("2x2_eye", AssetType.EYE),
	EYE_3X3("3x3_eye", AssetType.EYE),
	EYE_2X3("2x3_eye", AssetType.EYE),
	BIG_TRIANGLE_RIGHT("big_triangle_right_eye", AssetType.EYE),
	BIG_TRIANGLE_LEFT("big_triangle_left_eye", AssetType.EYE),
	TRIANGLE_RIGHT("triangle_right_eye", AssetType.EYE),
	TRIANGLE_LEFT("triangle_left_eye", AssetType.EYE),
	CROSS("cross_eye", AssetType.EYE),
	PLUS("plus_eye", AssetType.EYE),
	O("o_eye", AssetType.EYE),
	UP("up_eye", AssetType.EYE),
	RIGHT("right_eye", AssetType.EYE),
	DOWN("down_eye", AssetType.EYE),
	LEFT("left_eye", AssetType.EYE),
	NEUTRAL("neutral_smile", AssetType.SMILE),
	NORMAL("normal_smile", AssetType.SMILE),
	HAPPY("very_happy_smile", AssetType.SMILE),
	SAD("sad_smile", AssetType.SMILE),
	VERY_SAD("very_sad_smile", AssetType.SMILE),
	SMIRK("smirk_smile", AssetType.SMILE),
	MULTI("multi_border_deco", AssetType.DECO),
	SOLID("solid_border_deco", AssetType.DECO),
	BLUSH("blush_deco", AssetType.DECO),
	QUESTION("question_deco", AssetType.DECO),
	EXCLAMATION("exclamation_deco", AssetType.DECO),
	DOTS("3_dot_deco", AssetType.DECO),
	ROBOT("robot_deco", AssetType.DECO),
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
	
	//Getter {
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
				BufferedImage read = ImageIO.read(Objects.requireNonNull(AssetEnum.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/" + getName() + ".png")));
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
	//} Getter
	
}
//} Enums
