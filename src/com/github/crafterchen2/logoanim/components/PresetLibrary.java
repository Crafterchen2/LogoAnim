package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.frames.DisplayFrame;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import static com.github.crafterchen2.logoanim.AssetEnum.*;
import static com.github.crafterchen2.logoanim.MoodEnum.*;

//Classes {
public class PresetLibrary extends JComponent {
	
	//Fields {
	private final JSlider slider = new JSlider(5, 20, 10);
	private final JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
	private final AssetProvider asset;
	private final MoodProvider mood;
	private final ArrayList<AssetSelector.Listener> assetListeners = new ArrayList<>();
	private final ArrayList<MoodSelector.Listener> moodListeners = new ArrayList<>();
	//} Fields
	
	//Constructor {
	public PresetLibrary() {
		this(null, null);
	}
	
	public PresetLibrary(DisplayFrame frame) {
		this(frame.getAsset(), frame.getMood());
	}
	
	public PresetLibrary(AssetProvider asset, MoodProvider mood) {
		setLayout(new BorderLayout());
		this.asset = asset;
		this.mood = mood;
		addDisplay(grid, EYE_2X2, EYE_2X2, AssetEnum.NORMAL, null, MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL);
		addDisplay(grid, TRIANGLE_RIGHT, TRIANGLE_LEFT, SAD, null, MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL);
		addDisplay(grid, EYE_2X2, EYE_2X3, AssetEnum.NORMAL, null, MoodEnum.NORMAL, SANS, MoodEnum.NORMAL, MoodEnum.NORMAL);
		addDisplay(grid, UP, UP, HAPPY, SOLID, MoodEnum.NORMAL, MoodEnum.NORMAL, MoodEnum.NORMAL, SANS);
		addDisplay(grid, EYE_2X2, EYE_2X2, AssetEnum.NORMAL, null);
		addDisplay(grid, EYE_3X1, EYE_3X1, NEUTRAL, null);
		addDisplay(grid, EYE_2X2, EYE_2X2, AssetEnum.NORMAL, null, null, SANS, null, null);
		addDisplay(grid, EYE_3X1, EYE_3X3, NEUTRAL, null, null, SANS, null, null);
		addDisplay(grid, BIG_TRIANGLE_LEFT, BIG_TRIANGLE_RIGHT, AssetEnum.NORMAL, null, MAD, MAD, MAD, null);
		addDisplay(grid, BIG_TRIANGLE_LEFT, BIG_TRIANGLE_RIGHT, VERY_SAD, null, MAD, MAD, MAD, null);
		addDisplay(grid, EYE_2X2, EYE_3X1, SMIRK, null, GOOD, GOOD, GOOD, GOOD);
		addDisplay(grid, UP, UP, AssetEnum.NORMAL, SOLID, GOOD, GOOD, GOOD, GOOD);
		JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.getVerticalScrollBar().setBlockIncrement(40);
		scrollPane.getVerticalScrollBar().setUnitIncrement(40);
		JViewport viewport = scrollPane.getViewport();
		viewport.addChangeListener(_ -> {
			int width = viewport.getExtentSize().width;
			if (width - width % RegionEnum.base <= calcDisplaySize()) {
				updateSizes(width);
			}
			calcPrefSize(width);
		});
		scrollPane.setViewportView(grid);
		slider.setOrientation(JSlider.HORIZONTAL);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.addChangeListener(_ -> {
			int width = viewport.getExtentSize().width;
			if (width - width % RegionEnum.base >= calcDisplaySize()) {
				updateSizes(width);
				calcPrefSize(width);
				scrollPane.validate();
			}
		});
		add(slider, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
	}
	//} Constructor
	
	//Methods {
	public void removeAssetChangedListener(AssetSelector.Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		assetListeners.remove(listener);
	}
	
	public void addAssetChangedListener(AssetSelector.Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		assetListeners.add(listener);
	}
	
	private void signalAssetUpdate() {
		assetListeners.forEach(AssetSelector.Listener::assetChanged);
	}
	
	public void removeMoodChangedListener(MoodSelector.Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		moodListeners.remove(listener);
	}
	
	public void addMoodChangedListener(MoodSelector.Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		moodListeners.add(listener);
	}
	
	private void signalMoodUpdate() {
		moodListeners.forEach(MoodSelector.Listener::moodChanged);
	}
	
	private void calcPrefSize(int width) {
		final int size = calcDisplaySize(width);
		int count;
		synchronized (grid.getTreeLock()) {
			count = grid.getComponentCount();
		}
		Dimension prefSize = new Dimension(width, 0);
		int max = size * count;
		prefSize.height = Math.min(Math.ceilDiv(max, Math.max(width - width % size, 1)) * size, max);
		grid.setPreferredSize(prefSize);
	}
	
	private void updateSizes(int width) {
		int size = calcDisplaySize(width);
		Dimension dim = new Dimension(size, size);
		for (Component component : grid.getComponents()) {
			component.setPreferredSize(dim);
			component.invalidate();
		}
	}
	
	private void addDisplay(JPanel grid, AssetEnum leftEye, AssetEnum rightEye, AssetEnum smile, AssetEnum deco) {
		addDisplay(grid, leftEye, rightEye, smile, deco, null, null, null, null);
	}
	
	private void addDisplay(JPanel grid, AssetEnum leftEye, AssetEnum rightEye, AssetEnum smile, AssetEnum deco, MoodEnum leftEyeMood, MoodEnum rightEyeMood, MoodEnum smileMood, MoodEnum decoMood) {
		LogoDisplay display = new LogoDisplay(new AssetProvider.Default(leftEye, rightEye, smile, deco), new MoodProvider.Default(leftEyeMood, rightEyeMood, smileMood, decoMood));
		int size = calcDisplaySize();
		display.setPreferredSize(new Dimension(size, size));
		Border inactive = BorderFactory.createLineBorder(new Color(96, 96, 96), 2);
		Border active = BorderFactory.createLineBorder(new Color(192, 192, 192), 3);
		Border pressed = BorderFactory.createLineBorder(new Color(192, 192, 192), 1);
		display.setBorder(inactive);
		display.addMouseListener(makeMouseAdapter(display, inactive, active, pressed));
		grid.add(display);
	}
	
	private MouseAdapter makeMouseAdapter(LogoDisplay display, Border inactive, Border active, Border pressed) {
		return new MouseAdapter() {
			//Overrides {
			@Override
			public void mousePressed(MouseEvent e) {
				display.setBorder(pressed);
				if (asset != null) {
					asset.setAsset(display.getAsset());
					signalAssetUpdate();
				}
				if (mood != null) {
					mood.setMood(display.getMood());
					signalMoodUpdate();
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				display.setBorder(active);
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				display.setBorder(active);
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				display.setBorder(inactive);
			}
			//} Overrides
		};
	}
	
	private int calcDisplaySize(int width) {
		return Math.clamp(width - width % RegionEnum.base, RegionEnum.base, calcDisplaySize());
	}
	
	private int calcDisplaySize() {
		return slider.getValue() * RegionEnum.base;
	}
	//} Methods
}
//} Classes
