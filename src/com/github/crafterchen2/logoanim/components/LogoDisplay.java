package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

//Classes {
public class LogoDisplay extends JComponent implements AssetProvider, MoodProvider {
	
	//Fields {
	private final HashMap<RegionEnum, AssetEnum> assets = HashMap.newHashMap(RegionEnum.values().length);
	private final HashMap<RegionEnum, MoodEnum> moods = HashMap.newHashMap(RegionEnum.values().length);
	
	public boolean blink = false;
	//} Fields
	
	//Constructor {
	
	
	public LogoDisplay() {
		this(null, null);
	}
	
	public LogoDisplay(AssetProvider defAssets, MoodProvider defMoods) {
		setAsset(defAssets);
		setMood(defMoods);
		setDoubleBuffered(true);
	}
	//} Constructor
	
	//Overrides {
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		int w = getWidth();
		int h = getHeight();
		if (w <= 0 && h <= 0) return;
		g.setClip(0, 0, w, h);
		g.fillRect(0, 0, w, h);
		RegionEnum[] regs = RegionEnum.values();
		for (int i = regs.length - 1; i >= 0; i--) {
			if (regs[i].type == AssetType.EYE && blink) continue;
			AssetEnum asset = getAsset(regs[i]);
			if (asset != null) asset.paint(g, regs[i], getMood(regs[i]));
		}
	}
	
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return moods.get(reg);
	}
	
	@Override
	public AssetEnum getAsset(RegionEnum reg) {
		return assets.get(reg);
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodEnum mood) {
		moods.put(reg, mood);
	}
	
	@Override
	public void setAsset(RegionEnum reg, AssetEnum asset) {
		assets.put(reg, asset);
	}
	//} Overrides
	
}

//} Classes