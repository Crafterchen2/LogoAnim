package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

//Classes {
public class LogoDisplay extends JComponent implements AssetProvider, MoodProvider {
	
	//Fields {
	public boolean blink = false;
	
	private final LogoPainter painter;
	//} Fields
	
	//Constructor {
	
	
	public LogoDisplay() {
		this(null, null);
	}
	
	public LogoDisplay(ImmutableAssetProvider defAssets, ImmutableMoodProvider defMoods) {
		painter = new LogoPainter(defAssets, defMoods);
		setDoubleBuffered(true);
		setBackground(new Color(0,0,0));
		setOpaque(true);
	}
	//} Constructor
	
	//Overrides {
	@Override
	protected void paintComponent(Graphics g) {
		painter.blink = blink;
		Color background = getBackground();
		if (background != null) {
			g.setColor(background);
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		painter.paint(g, getWidth(), getHeight());
	}
	
	@Override
	public MoodData getMood(RegionEnum reg) {
		return painter.getMood(reg);
	}
	
	@Override
	public AssetData getAsset(RegionEnum reg) {
		return painter.getAsset(reg);
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodData mood) {
		painter.setMood(reg, mood);
	}
	
	@Override
	public void setAsset(RegionEnum reg, AssetData asset) {
		painter.setAsset(reg, asset);
	}
	//} Overrides
	
}

//} Classes