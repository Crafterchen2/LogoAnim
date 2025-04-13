package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.frames.DisplayFrame;

import javax.swing.*;
import java.awt.*;

//Classes {
public class LogoControl extends JPanel {
	
	//Fields {
	private final JSlider slider = new JSlider();
	private final MoodSelector moodSelector = new MoodSelector();
	private final AssetSelector assetSelector = new AssetSelector(moodSelector);
	private final JCheckBox blinkBox = new JCheckBox("Enable blinking");
	private final JButton repaintButton = new JButton("Repaint");
	private final JButton updateButton = new JButton("Update config");
	private DisplayFrame logo;
	private boolean hasLogo;
	private boolean ignoreListener = false;
	//} Fields
	
	//Constructor {
	public LogoControl() {
		this(null);
	}
	
	public LogoControl(DisplayFrame logo) {
		setLogo(logo);
		setLayout(new BorderLayout(3, 3));
		setPreferredSize(moodSelector.getPreferredSize());
		{
			DisplayFrame l = getLogo();
			if (!hasLogo) return;
			slider.setMaximum(l.getMaxScale());
			slider.setMinimum(l.getMinScale());
			final int major = (int) (Math.pow(10, Math.ceil(Math.log10(l.getMaxScale())) - 1));
			if (major > 1 && major % 2 == 0) {
				slider.setMinorTickSpacing(major / 2);
			}
			slider.setMajorTickSpacing(major);
		}
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setOrientation(JSlider.VERTICAL);
		slider.addChangeListener(_ -> {
			if (ignoreListener) return;
			DisplayFrame l = getLogo();
			if (!hasLogo) return;
			l.setScale(slider.getValue());
		});
		moodSelector.addMoodChangedListener(() -> {
			if (ignoreListener) return;
			MoodProvider l = getLogo();
			if (!hasLogo) return;
			for (RegionEnum reg : RegionEnum.values()) {
				l.setMood(reg, moodSelector.getMood(reg));
			}
			assetSelector.repaint();
			repaintLogo(false);
		});
		assetSelector.addAssetChangedListener(() -> {
			if (ignoreListener) return;
			AssetProvider l = getLogo();
			if (!hasLogo) return;
			for (RegionEnum reg : RegionEnum.values()) {
				l.setAsset(reg, assetSelector.getAsset(reg));
			}
			repaintLogo(false);
		});
		blinkBox.addActionListener(_ -> {
			if (ignoreListener) return;
			repaintLogo(true);
		});
		repaintButton.addActionListener(_ -> {
			if (ignoreListener) return;
			repaintLogo(true);
		});
		updateButton.addActionListener(_ -> {
			if (ignoreListener) return;
			ignoreListener = true;
			DisplayFrame l = getLogo();
			if (hasLogo) {
				slider.setValue(l.getScale());
				for (RegionEnum reg : RegionEnum.values()) {
					moodSelector.setMood(reg, l.getMood(reg));
					assetSelector.setAsset(reg, l.getAsset(reg));
				}
				blinkBox.setSelected(l.getShouldBlink());
			}
			ignoreListener = false;
		});
		add(slider, BorderLayout.WEST);
		add(moodSelector, BorderLayout.NORTH);
		add(assetSelector, BorderLayout.CENTER);
		JPanel s = new JPanel(new BorderLayout());
		s.add(blinkBox, BorderLayout.WEST);
		s.add(repaintButton, BorderLayout.CENTER);
		s.add(updateButton, BorderLayout.EAST);
		add(s, BorderLayout.SOUTH);
		repaintLogo(true);
	}
	//} Constructor
	
	//Methods {
	private void repaintLogo(boolean query) {
		DisplayFrame l = getLogo();
		if (!hasLogo) return;
		if (query) {
			l.setScale(slider.getValue());
			for (RegionEnum reg : RegionEnum.values()) {
				l.setMood(reg, moodSelector.getMood(reg));
				l.setAsset(reg, assetSelector.getAsset(reg));
			}
			l.setShouldBlink(blinkBox.isSelected());
		}
		l.repaint();
	}
	//} Methods
	
	//Getter {
	
	/**
	 Returns the current logo enables / disables the components accordingly.
	 It is highly recommended to check the value of {@code hasLogo} before proceeding,
	 as this method can return {@code null}.
	 
	 @return The current logo or {@code null} if no logo is set.
	 */
	public DisplayFrame getLogo() {
		hasLogo = logo != null;
		setEnabled(hasLogo);
		return logo;
	}
	//} Getter
	
	//Setter {
	public void setLogo(DisplayFrame logo) {
		this.logo = logo;
		getLogo();
		if (hasLogo) {
			slider.setValue(this.logo.getScale());
			for (RegionEnum reg : RegionEnum.values()) {
				moodSelector.setMood(reg, this.logo.getMood(reg));
				assetSelector.setAsset(reg, this.logo.getAsset(reg));
			}
			blinkBox.setSelected(this.logo.getShouldBlink());
		} else {
			slider.setValue(RegionEnum.base);
			moodSelector.setMood(RegionEnum.LEFT_EYE, MoodEnum.NORMAL);
			moodSelector.setMood(RegionEnum.RIGHT_EYE, MoodEnum.NORMAL);
			moodSelector.setMood(RegionEnum.SMILE, MoodEnum.NORMAL);
			moodSelector.setMood(RegionEnum.DECO, null);
			assetSelector.setAsset(RegionEnum.LEFT_EYE, AssetEnum.EYE_2X2);
			assetSelector.setAsset(RegionEnum.RIGHT_EYE, AssetEnum.EYE_2X2);
			assetSelector.setAsset(RegionEnum.SMILE, AssetEnum.NORMAL);
			assetSelector.setAsset(RegionEnum.DECO, null);
			blinkBox.setSelected(true);
		}
	}
	//} Setter
	
	
	
	//Overrides {
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		slider.setEnabled(enabled);
		moodSelector.setEnabled(enabled);
		assetSelector.setEnabled(enabled);
		blinkBox.setEnabled(enabled);
		repaintButton.setEnabled(enabled);
		updateButton.setEnabled(enabled);
	}
	//} Overrides
}
//} Classes
