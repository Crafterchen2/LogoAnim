package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.frames.LogoFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

//Classes {
public class LogoControl extends JPanel {
	
	//Fields {
	private final JSlider slider = new JSlider(RegionEnum.base, RegionEnum.base * 8);
	private final ImageSelector leftSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.EYE), 3, RegionEnum.LEFT_EYE);
	private final ImageSelector rightSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.EYE), 3, RegionEnum.RIGHT_EYE);
	private final ImageSelector smileSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.SMILE), 2, RegionEnum.SMILE);
	private final ImageSelector decoSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.DECO), 1, RegionEnum.DECO);
	private final MoodSelector moodSelector = new MoodSelector();
	private LogoFrame logo;
	private boolean hasLogo;
	private final JButton repaintButton = new JButton("Repaint");
	//} Fields
	
	//Constructor {
	public LogoControl() {
		this(null);
	}
	
	public LogoControl(LogoFrame logo) {
		setLogo(logo);
		setLayout(new BorderLayout(3, 3));
		setPreferredSize(moodSelector.getPreferredSize());
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(10);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setOrientation(JSlider.VERTICAL);
		slider.addChangeListener(_ -> {
			LogoFrame l = getLogo();
			if (!hasLogo) return;
			l.setScale(slider.getValue());
		});
		repaintButton.addActionListener(_ -> repaintLogo(true));
		ImageSelector.Listener imgListener = () -> repaintLogo(true);
		leftSelector.addImageChangedListener(imgListener);
		rightSelector.addImageChangedListener(imgListener);
		smileSelector.addImageChangedListener(imgListener);
		decoSelector.addImageChangedListener(imgListener);
		moodSelector.addMoodChangedListener(() -> {
			MoodProvider l = getLogo();
			if (!hasLogo) return;
			MoodEnum mood = moodSelector.getMood(RegionEnum.LEFT_EYE);
			l.setMood(RegionEnum.LEFT_EYE, mood);
			leftSelector.setMood(mood);
			mood = moodSelector.getMood(RegionEnum.RIGHT_EYE);
			l.setMood(RegionEnum.RIGHT_EYE, mood);
			rightSelector.setMood(mood);
			mood = moodSelector.getMood(RegionEnum.SMILE);
			l.setMood(RegionEnum.SMILE, mood);
			smileSelector.setMood(mood);
			mood = moodSelector.getMood(RegionEnum.DECO);
			l.setMood(RegionEnum.DECO, mood);
			decoSelector.setMood(mood);
			repaintLogo(false);
		});
		JPanel p = new JPanel(new GridLayout(2, 2));
		p.add(leftSelector);
		p.add(rightSelector);
		p.add(smileSelector);
		p.add(decoSelector);
		add(p, BorderLayout.CENTER);
		add(moodSelector, BorderLayout.NORTH);
		add(slider, BorderLayout.WEST);
		add(repaintButton, BorderLayout.SOUTH);
		repaintLogo(true);
	}
	//} Constructor
	
	//Methods {
	
	private static AssetEnum[] filterArray(AssetEnum[] src, AssetType filter) {
		return Arrays.stream(src).filter(assetEnum -> assetEnum.getType() == filter).toList().toArray(new AssetEnum[0]);
	}
	
	private void repaintLogo(boolean query) {
		LogoFrame l = getLogo();
		if (!hasLogo) return;
		if (query) {
			l.setScale(slider.getValue());
			l.setAsset(RegionEnum.LEFT_EYE, leftSelector.getSelected());
			l.setAsset(RegionEnum.RIGHT_EYE, rightSelector.getSelected());
			l.setAsset(RegionEnum.SMILE, smileSelector.getSelected());
			l.setAsset(RegionEnum.DECO, decoSelector.getSelected());
			l.setMood(RegionEnum.LEFT_EYE, moodSelector.getMood(RegionEnum.LEFT_EYE));
			l.setMood(RegionEnum.RIGHT_EYE, moodSelector.getMood(RegionEnum.RIGHT_EYE));
			l.setMood(RegionEnum.SMILE, moodSelector.getMood(RegionEnum.SMILE));
			l.setMood(RegionEnum.DECO, moodSelector.getMood(RegionEnum.DECO));
		}
		l.repaint();
	}
	//} Methods
	
	//Getter {
	
	/**
	 * Returns the current logo enables / disables the components accordingly.
	 * It is highly recommended to check the value of {@code hasLogo} before proceeding,
	 * as this method can return {@code null}.
	 * @return The current logo or {@code null} if no logo is set.
	 */
	public LogoFrame getLogo() {
		hasLogo = logo != null;
		setEnabled(hasLogo);
		return logo;
	}
	//} Getter
	
	//Setter {
	public void setLogo(LogoFrame logo) {
		this.logo = logo;
		getLogo();
		//FEAT: Apply default assets according to current config of logo
		leftSelector.setSelected(4);
		rightSelector.setSelected(4);
		smileSelector.setSelected(2);
		decoSelector.setSelected(0);
		decoSelector.setMood(null);
		if (hasLogo) {
			moodSelector.setMood(RegionEnum.LEFT_EYE, this.logo.getMood(RegionEnum.LEFT_EYE));
			moodSelector.setMood(RegionEnum.RIGHT_EYE, this.logo.getMood(RegionEnum.RIGHT_EYE));
			moodSelector.setMood(RegionEnum.SMILE, this.logo.getMood(RegionEnum.SMILE));
			moodSelector.setMood(RegionEnum.DECO, this.logo.getMood(RegionEnum.DECO));
			slider.setValue(this.logo.getScale());
		} else {
			moodSelector.setMood(RegionEnum.LEFT_EYE, MoodEnum.NORMAL);
			moodSelector.setMood(RegionEnum.RIGHT_EYE, MoodEnum.NORMAL);
			moodSelector.setMood(RegionEnum.SMILE, MoodEnum.NORMAL);
			moodSelector.setMood(RegionEnum.DECO, null);
			slider.setValue(RegionEnum.base);
		}
	}
	//} Setter
	
	
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		moodSelector.setEnabled(enabled);
		slider.setEnabled(enabled);
		leftSelector.setEnabled(enabled);
		rightSelector.setEnabled(enabled);
		smileSelector.setEnabled(enabled);
		decoSelector.setEnabled(enabled);
		repaintButton.setEnabled(enabled);
	}
}
//} Classes
