package com.github.crafterchen2.logoanim;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

//Interfaces {
public interface AssetData {
	
	//Methods {
	static BufferedImage recolorImg(MoodData mood, BufferedImage img) {
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
	
	default void paint(Graphics g, RegionEnum reg, MoodData mood) {
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
	//} Methods
	
	//Getter {
	BufferedImage getImg();
	
	AssetType getType();
	
	String getName();
	//} Getter
}
//} Interfaces
