package com.github.crafterchen2.logoanim;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

//Enums {
public enum AssetEnum {
	
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
	UP("up_eye", AssetType.EYE),
	DOWN("down_eye", AssetType.EYE),
	NEUTRAL("neutral_smile", AssetType.SMILE),
	NORMAL("normal_smile", AssetType.SMILE),
	SAD("sad_smile", AssetType.SMILE),
	MULTI("multi_border_deco", AssetType.DECO),
	SOLID("solid_border_deco", AssetType.DECO),
	;
	
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
	
	//Methods {
	public static BufferedImage recolorImg(MoodEnum mood, BufferedImage img) {
		if (mood == null) throw new IllegalArgumentException("mood must not be null.");
		if (img == null) throw new IllegalArgumentException("img must not be null.");
		Color color = mood.getColor();
		int n = img.getColorModel().getNumComponents();
		float[] scales = new float[n];
		if (n > 0) scales[0] = color.getRed() / 255.0f;
		if (n > 1) scales[1] = color.getGreen() / 255.0f;
		if (n > 2) scales[2] = color.getBlue() / 255.0f;
		for (int i = 3; i < n; i++) scales[i] = 1.0f;
		RescaleOp rescaleOp = new RescaleOp(scales, new float[n], null);
		img = rescaleOp.filter(img, null);
		return img;
	}
	
	public void paint(Graphics g, RegionEnum reg, MoodEnum mood) {
		BufferedImage img = getImg();
		Rectangle2D b = g.getClip().getBounds2D();
		if (mood != null) img = recolorImg(mood, img);
		int w = (int) (b.getWidth() * reg.w * img.getWidth() / (Math.abs(reg.w) * RegionEnum.base));
		int h = (int) (b.getHeight() * reg.h * img.getHeight() / (Math.abs(reg.h) * RegionEnum.base));
		int x = (int) (b.getX() + b.getWidth() * reg.x + Math.min(w, 0));
		int y = (int) (b.getY() + b.getHeight() * reg.y + Math.min(h, 0));
		w = Math.abs(w);
		h = Math.abs(h);
		g.drawImage(img, x, y, w, h, null);
	}
	
	//Getter {
	public String getName() {
		return name;
	}
	
	public AssetType getType() {
		return type;
	}
	
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
	//} Getter
	
}
//} Enums
