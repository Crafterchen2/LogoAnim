package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

//Classes {
public class LogoControl extends JPanel {
	
	//Fields {
	private final JSlider slider;
	private final ImageSelector<AssetEnum> leftSelector, rightSelector, smileSelector, decoSelector;
	private final MoodSelector moodSelector = new MoodSelector();
	private LogoFrame logo;
	//} Fields
	
	//Constructor {
	public LogoControl() {
		this(null);
	}
	
	public LogoControl(LogoFrame logo) {
		setLogo(logo);
		slider = new JSlider(0, 100, 20);
		leftSelector = new ImageSelector<>(filterArray(AssetEnum.values(), AssetType.EYE), 3, RegionEnum.LEFT_EYE);
		rightSelector = new ImageSelector<>(filterArray(AssetEnum.values(), AssetType.EYE), 3, RegionEnum.RIGHT_EYE);
		smileSelector = new ImageSelector<>(filterArray(AssetEnum.values(), AssetType.SMILE), 1, RegionEnum.SMILE);
		decoSelector = new ImageSelector<>(filterArray(AssetEnum.values(), AssetType.DECO), 1, RegionEnum.DECO);
		JButton repaintButton = new JButton("Repaint");
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(20);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.addChangeListener(_ -> getLogo().setScale(slider.getValue()));
		repaintButton.addActionListener(_ -> repaintLogo(true));
		moodSelector.addMoodChangedListener(() -> {
			LogoFrame l = getLogo();
			MoodEnum mood = moodSelector.getMood(RegionEnum.LEFT_EYE);
			l.leftEyeMood = mood;
			leftSelector.setMood(mood);
			mood = moodSelector.getMood(RegionEnum.RIGHT_EYE);
			l.rightEyeMood = mood;
			rightSelector.setMood(mood);
			mood = moodSelector.getMood(RegionEnum.SMILE);
			l.smileMood = mood;
			smileSelector.setMood(mood);
			mood = moodSelector.getMood(RegionEnum.DECO);
			l.decoMood = mood;
			decoSelector.setMood(mood);
			repaintLogo(false);
		});
		add(moodSelector);
		add(leftSelector);
		add(rightSelector);
		add(smileSelector);
		add(decoSelector);
		add(slider);
		add(repaintButton);
		setLayout(new GridLayout(0, 1));
	}
	//} Constructor
	
	//Methods {
	
	private <T extends AssetManager> JComboBox<T> setupComboBox(T[] src, AssetType filter) {
		return new JComboBox<>(filterArray(src, filter));
	}
	
	private static <T extends AssetManager> T[] filterArray(T[] src, AssetType filter) {
		return Arrays.stream(src).filter(assetEnum -> assetEnum.getType() == filter).toList().toArray(Arrays.copyOf(src, 0));
	}
	
	private void repaintLogo(boolean query) {
		LogoFrame l = getLogo();
		if (query) {
			l.setScale(slider.getValue());
			l.leftEye = leftSelector.getSelected();
			l.rightEye = rightSelector.getSelected();
			l.smile = smileSelector.getSelected();
			l.deco = decoSelector.getSelected();
			l.leftEyeMood = moodSelector.getMood(RegionEnum.LEFT_EYE);
			l.rightEyeMood = moodSelector.getMood(RegionEnum.RIGHT_EYE);
			l.smileMood = moodSelector.getMood(RegionEnum.SMILE);
			l.decoMood = moodSelector.getMood(RegionEnum.DECO);
		}
		l.repaint();
	}
	
	private void createLogo() {
		if (logo == null) {
			logo = new LogoFrame() {
				//Overrides {
				@Override
				public void dispose() {
					setLogo(null);
					super.dispose();
				}
				//} Overrides
			};
		}
	}
	//} Methods
	
	//Getter {
	public LogoFrame getLogo() {
		if (logo == null) createLogo();
		return logo;
	}
	//} Getter
	
	//Setter {
	public void setLogo(LogoFrame logo) {
		this.logo = logo;
	}
	//} Setter
	
}
//} Classes
