package com.github.crafterchen2.logoanim;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

//Interfaces {
public interface AssetManager {
	
	//Methods {
	static BufferedImage recolorImg(MoodEnum mood, BufferedImage img) {
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
	
	void paint(Graphics g, RegionEnum reg, MoodEnum mood);
	//} Methods
	
	//Getter {
	String getName();
	
	BufferedImage getImg();
	
	AssetType getType();
	//} Getter
	
}
//} Interfaces
