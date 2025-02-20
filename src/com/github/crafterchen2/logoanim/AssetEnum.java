package com.github.crafterchen2.logoanim;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

//Enums {
public enum AssetEnum implements AssetManager {
	
	Eye3x1("3x1_eye", AssetType.EYE),
	Eye2x3("2x3_eye", AssetType.EYE),
	Eye1x1("1x1_eye", AssetType.EYE),
	Eye2x2("2x2_eye", AssetType.EYE),
	Eye3x3("3x3_eye", AssetType.EYE),
	BIG_TRIANGLE_RIGHT("big_triangle_right_eye", AssetType.EYE),
	BIG_TRIANGLE_LEFT("big_triangle_left_eye", AssetType.EYE),
	TRIANGLE_RIGHT("triangle_right_eye", AssetType.EYE),
	TRIANGLE_LEFT("triangle_left_eye", AssetType.EYE),
	CROSS("cross_eye", AssetType.EYE),
	PLUS("plus_eye", AssetType.EYE),
	UP("up_eye", AssetType.EYE),
	DOWN("down_eye", AssetType.EYE),
	NEUTRAL("neutral_smile", AssetType.SMILE),
	NORMAL("normal_smile", AssetType.SMILE),
	SAD("sad_smile", AssetType.SMILE);
	
	//Fields {
	private static final HashMap<AssetEnum, BufferedImage> cache = HashMap.newHashMap(16);
	
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
	public void paint(Graphics g, RegionEnum reg, MoodEnum mood) {
		BufferedImage img = getImg();
		Rectangle2D b = g.getClip().getBounds2D();
		if (mood != null) img = AssetManager.recolorImg(mood, img);
		int w = (int) (b.getWidth() * reg.w * img.getWidth() / (Math.abs(reg.w) * RegionEnum.base));
		int h = (int) (b.getHeight() * reg.h * img.getHeight() / (Math.abs(reg.h) * RegionEnum.base));
		int x = (int) (b.getX() + b.getWidth() * reg.x + Math.min(w, 0));
		int y = (int) (b.getY() + b.getHeight() * reg.y + Math.min(h, 0));
		w = Math.abs(w);
		h = Math.abs(h);
		g.drawImage(img, x, y, w, h, null);
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public AssetType getType(){
	    return type;
	}
	
	@Override
	public BufferedImage getImg() {
		if (!cache.containsKey(this)) {
			try {
				cache.put(this, ImageIO.read(Objects.requireNonNull(AssetEnum.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/" + getName() + ".png"))));
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
