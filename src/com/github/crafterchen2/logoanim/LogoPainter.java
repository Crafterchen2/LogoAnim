package com.github.crafterchen2.logoanim;

import java.awt.*;
import java.util.HashMap;

//Classes {
public class LogoPainter implements AssetProvider, MoodProvider {
	
	//Fields {
	private final HashMap<RegionEnum, AssetData> assets = HashMap.newHashMap(RegionEnum.values().length);
	private final HashMap<RegionEnum, MoodData> moods = HashMap.newHashMap(RegionEnum.values().length);
	
	public boolean blink = false;
	//} Fields
	
	//Constructor {
	
	
	public LogoPainter() {
		this(null, null);
	}
	
	public LogoPainter(ImmutableAssetProvider defAssets, ImmutableMoodProvider defMoods) {
		setAsset(defAssets);
		setMood(defMoods);
	}
	//} Constructor
	
	//Methods {
	public void paint(Graphics g, int w, int h) {
		paint(g, 0,0, w, h);
	}
	
	public void paint(Graphics g, int x, int y, int w, int h) {
		if (w <= 0 || h <= 0) return;
		g = g.create(x,y,w,h);
		RegionEnum[] regs = RegionEnum.values();
		for (int i = regs.length - 1; i >= 0; i--) {
			if (regs[i].type == AssetType.EYE && blink) continue;
			AssetData asset = getAsset(regs[i]);
			if (asset != null) asset.paint(g, regs[i], getMood(regs[i]));
		}
	}
	//} Methods
	
	//Overrides {
	@Override
	public MoodData getMood(RegionEnum reg) {
		return moods.get(reg);
	}
	
	@Override
	public AssetData getAsset(RegionEnum reg) {
		return assets.get(reg);
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodData mood) {
		moods.put(reg, mood);
	}
	
	@Override
	public void setAsset(RegionEnum reg, AssetData asset) {
		assets.put(reg, asset);
	}
	//} Overrides
	
}
//} Classes
