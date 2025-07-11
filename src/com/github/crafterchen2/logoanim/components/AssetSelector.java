package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Objects;

//Classes {
public class AssetSelector extends Selector implements AssetProvider {
	
	//Fields {
	private final HashMap<RegionEnum, AssetData> assets = HashMap.newHashMap(RegionEnum.values().length);
	private final ArrayList<Listener> listeners = new ArrayList<>();
	private final MoodProvider moodProvider;
	//} Fields
	
	//Constructor {
	/*IMPROVEME: Layout suboptimal
	 * Goal: opt 1 OR opt 2
	 *       L | R || L | R
	 *       --+-- || --+--
	 *        SMI  || S | D
	 *       ----- ||
	 *        DEC  ||
	 * +2: Same layout as MoodSelector
	 * +1: smiles are wider than high; can be bigger here
	 * -2: might get crammed
	 * +2: not as tall
	 * */
	public AssetSelector(MoodProvider moodProvider) {
		this.moodProvider = (moodProvider != null) ? moodProvider : new MoodProvider.Default();
		setLayout(new GridLayout(2, 2));
		JPanel leftPanel = new JPanel(new GridLayout(3, 0, 3, 3));
		JPanel rightPanel = new JPanel(new GridLayout(3, 0, 3, 3));
		JPanel smilePanel = new JPanel(new GridLayout(0, 3, 3, 3));
		JPanel decoPanel = new JPanel(new GridLayout(0, 3, 3, 3));
		leftPanel.add(new AssetButton(RegionEnum.LEFT_EYE, null));
		rightPanel.add(new AssetButton(RegionEnum.RIGHT_EYE, null));
		smilePanel.add(new AssetButton(RegionEnum.SMILE, null));
		decoPanel.add(new AssetButton(RegionEnum.DECO, null));
		for (AssetData asset : AssetEnum.values()) {
			switch (asset.getType()) {
				case EYE -> {
					leftPanel.add(new AssetButton(RegionEnum.LEFT_EYE, asset));
					rightPanel.add(new AssetButton(RegionEnum.RIGHT_EYE, asset));
				}
				case SMILE -> smilePanel.add(new AssetButton(RegionEnum.SMILE, asset));
				case DECO -> decoPanel.add(new AssetButton(RegionEnum.DECO, asset));
			}
		}
		add(leftPanel);
		add(rightPanel);
		add(smilePanel);
		add(decoPanel);
	}
	//} Constructor
	
	//Methods {
	public void removeAssetChangedListener(Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		listeners.remove(listener);
	}
	
	public void addAssetChangedListener(Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		listeners.add(listener);
	}
	
	private void signalUpdate() {
		listeners.forEach(AssetSelector.Listener::assetChanged);
	}
	//} Methods
	
	//Overrides {
	public AssetData getAsset(RegionEnum reg) {
		if (reg == null) throw new IllegalArgumentException("reg must not be null.");
		return assets.get(reg);
	}
	
	public void setAsset(RegionEnum reg, AssetData asset) {
		if (reg == null) throw new IllegalArgumentException("reg must not be null.");
		if (asset == null || reg.type == asset.getType()) {
			assets.put(reg, asset);
			repaint();
			signalUpdate();
		} else {
			throw new IllegalArgumentException("asset.getType() must be the same as reg.type.");
		}
	}
	//} Overrides
	
	//Classes {
	private class AssetButton extends SelectorButton<AssetData> {
		
		//Constructor {
		public AssetButton(RegionEnum reg, AssetData subject) {
			super(reg, subject);
			addActionListener(_ -> setAsset(reg, subject));
		}
		//} Constructor
		
		//Overrides {
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(Objects.equals(assets.get(reg), subject) ? new Color(192, 192, 192) : new Color(96, 96, 96));
			int w = getWidth();
			int h = getHeight();
			g.fillRect(0, 0, w, h);
			final int m = Math.min(w, h);
			final int c = m - m % RegionEnum.base;
			int x = w / 2 - c / 2;
			int y = h / 2 - c / 2;
			g.setColor(new Color(0, 0, 0));
			g.fillRect(x, y, c, c);
			if (subject == null) return;
			MoodData mood = moodProvider.getMood(reg);
			BufferedImage img = subject.getImg();
			if (mood != null) img = AssetData.recolorImg(mood, img);
			w = (reg.ratio < 1.0) ? (int) (c * reg.ratio) : c;
			h = (reg.ratio > 1.0) ? (int) (c / reg.ratio) : c;
			//IMPROVEME: replace if with math if possible
			if (w % RegionEnum.base != 0 || h % RegionEnum.base != 0) {
				final int temp = w - w % RegionEnum.base - ((h % RegionEnum.base != 0) ? RegionEnum.base : 0);
				h -= h % RegionEnum.base - ((w % RegionEnum.base != 0) ? RegionEnum.base : 0);
				w = temp;
			}
			w = (int) (w * img.getWidth() / (Math.abs(reg.w) * RegionEnum.base));
			h = (int) (h * img.getHeight() / (Math.abs(reg.h) * RegionEnum.base));
			w -= w % img.getWidth();
			h -= h % img.getHeight();
			x += c / 2 - w / 2;
			y += c / 2 - h / 2;
			g.drawImage(img, x, y, Math.abs(w), Math.abs(h), null);
		}
		//} Overrides
	}
	//} Classes
	
	//Interfaces {
	@FunctionalInterface
	public interface Listener extends EventListener {
		
		//Methods {
		void assetChanged();
		//} Methods
		
	}
	//} Interfaces
	
}
//} Classes
