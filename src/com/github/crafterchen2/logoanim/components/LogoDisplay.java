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
	
	public LogoDisplay(AssetProvider defAssets, MoodProvider defMoods) {
		painter = new LogoPainter(defAssets, defMoods);
		setDoubleBuffered(true);
	}
	//} Constructor
	
	//Overrides {
	@Override
	protected void paintComponent(Graphics g) {
		painter.blink = blink;
		painter.paint(g, getWidth(), getHeight());
	}
	
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return painter.getMood(reg);
	}
	
	@Override
	public AssetEnum getAsset(RegionEnum reg) {
		return painter.getAsset(reg);
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodEnum mood) {
		painter.setMood(reg, mood);
	}
	
	@Override
	public void setAsset(RegionEnum reg, AssetEnum asset) {
		painter.setAsset(reg, asset);
	}
	//} Overrides
	
}

//} Classes