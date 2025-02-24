package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class LogoDisplay extends JComponent implements AssetProvider, MoodProvider {
	
	//Fields {
	HashMap<RegionEnum, AssetEnum> assets = HashMap.newHashMap(RegionEnum.values().length);
	HashMap<RegionEnum, MoodEnum> moods = HashMap.newHashMap(RegionEnum.values().length);
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
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(new Color(0, 0, 0));
		int w = getWidth();
		int h = getHeight();
		if (w <= 0 && h <= 0) return;
		g.setClip(0,0,w,h);
		g.fillRect(0, 0, w, h);
		RegionEnum[] regs = RegionEnum.values();
		for (int i = regs.length - 1; i >= 0; i--) {
			if (getAsset(regs[i]) != null) getAsset(regs[i]).paint(g, regs[i], getMood(regs[i]));
		}
	}
	
	//Getter {	
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return moods.get(reg);
	}
	
	@Override
	public AssetEnum getAsset(RegionEnum reg) {
		return assets.get(reg);
	}
	//} Getter
	@Override
	public void setMood(RegionEnum reg, MoodEnum mood) {
		moods.put(reg, mood);
	}
	
	@Override
	public void setAsset(RegionEnum reg, AssetEnum asset) {
		assets.put(reg, asset);
	}
	//} Setter

}
