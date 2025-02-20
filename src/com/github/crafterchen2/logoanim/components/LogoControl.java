package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

//Classes {
public class LogoControl extends JPanel {
	
	//Fields {
	private final JSlider slider;
	private final ImageSelector leftSelector, rightSelector, smileSelector, decoSelector;
	private final MoodSelector moodSelector = new MoodSelector();
	private LogoFrame logo;
	//} Fields
	
	//Constructor {
	public LogoControl() {
		this(null);
	}
	
	public LogoControl(LogoFrame logo) {
		setLogo(logo);
		setLayout(new BorderLayout(3,3));
		setPreferredSize(moodSelector.getPreferredSize());
		slider = new JSlider(10, 80, 20);
		leftSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.EYE), 3, RegionEnum.LEFT_EYE);
		rightSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.EYE), 3, RegionEnum.RIGHT_EYE);
		smileSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.SMILE), 1, RegionEnum.SMILE);
		decoSelector = new ImageSelector(filterArray(AssetEnum.values(), AssetType.DECO), 1, RegionEnum.DECO);
		JButton repaintButton = new JButton("Repaint");
		leftSelector.setSelected(4);
		rightSelector.setSelected(4);
		smileSelector.setSelected(2);
		decoSelector.setSelected(0);
		moodSelector.setMood(RegionEnum.LEFT_EYE, MoodEnum.NORMAL);
		moodSelector.setMood(RegionEnum.RIGHT_EYE, MoodEnum.NORMAL);
		moodSelector.setMood(RegionEnum.SMILE, MoodEnum.NORMAL);
		moodSelector.setMood(RegionEnum.DECO, null);
		slider.setMinorTickSpacing(5);
		slider.setMajorTickSpacing(20);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setOrientation(JSlider.VERTICAL);
		slider.addChangeListener(_ -> getLogo().setScale(slider.getValue()));
		repaintButton.addActionListener(_ -> repaintLogo(true));
		ImageSelector.Listener imgListener = () -> repaintLogo(true);
		leftSelector.addImageChangedListener(imgListener);
		rightSelector.addImageChangedListener(imgListener);
		smileSelector.addImageChangedListener(imgListener);
		decoSelector.addImageChangedListener(imgListener);
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
		JPanel p = new JPanel(new GridLayout(2,2));
		p.add(rightSelector);
		p.add(leftSelector);
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
