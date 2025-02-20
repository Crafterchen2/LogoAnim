package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.AssetEnum;
import com.github.crafterchen2.logoanim.MoodEnum;
import com.github.crafterchen2.logoanim.RegionEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Objects;

//Classes {
public class ImageSelector extends JComponent {
	
	//Fields {
	public final RegionEnum reg;
	public final int scale;
	private final AssetEnum[] arr;
	private final JToggleButton[] buttons;
	private final ButtonGroup group = new ButtonGroup();
	private final ArrayList<Listener> listeners = new ArrayList<>();
	private MoodEnum mood = MoodEnum.NORMAL;
	//} Fields
	
	//Constructor {
	public ImageSelector(AssetEnum[] arr, int cols, RegionEnum reg) {
		this(arr, cols, reg, 10, true);
	}
	
	public ImageSelector(AssetEnum[] arr, int cols, RegionEnum reg, int scale, boolean allowEmpty) {
		if (cols < 1) throw new IllegalArgumentException("cols must be at least 1.");
		if (arr == null) throw new IllegalArgumentException("arr must not be null.");
		if (reg == null) throw new IllegalArgumentException("reg must not be null.");
		this.arr = arr;
		buttons = new JToggleButton[this.arr.length];
		this.reg = reg;
		this.scale = scale;
		setLayout(new GridLayout(Math.ceilDiv(0, cols), cols));
		if (allowEmpty) {
			JToggleButton button = new JToggleButton("", true);
			button.setBackground(new Color(0, 0, 0));
			button.addActionListener(_ -> signalUpdate());
			add(button);
			group.add(button);
		}
		for (int i = 0; i < arr.length; i++) {
			buttons[i] = new JToggleButton(getIcon(arr[i]));
			buttons[i].setBackground(new Color(0, 0, 0));
			buttons[i].addActionListener(_ -> signalUpdate());
			add(buttons[i]);
			group.add(buttons[i]);
		}
	}
	//} Constructor
	
	//Methods {
	public void removeImageChangedListener(Listener listener) {
		listeners.remove(listener);
	}
	
	public void addImageChangedListener(Listener listener) {
		listeners.add(listener);
	}
	
	private void signalUpdate() {
		listeners.forEach(Listener::imageChanged);
	}
	
	private void updateButtons() {
		for (int i = 0; i < arr.length; i++) {
			buttons[i].setIcon(getIcon(arr[i]));
		}
	}
	
	private ImageIcon getIcon(AssetEnum asset) {
		BufferedImage img = asset.getImg();
		if (mood != null) img = AssetEnum.recolorImg(mood, img);
		int w = (int) (scale * RegionEnum.base * (Math.abs(reg.w) * img.getWidth() / (Math.abs(reg.w) * RegionEnum.base)));
		int h = (int) (scale * RegionEnum.base * (Math.abs(reg.h) * img.getHeight() / (Math.abs(reg.h) * RegionEnum.base)));
		return new ImageIcon(img.getScaledInstance(w, h, Image.SCALE_FAST));
	}
	//} Methods
	
	//Getter {
	public AssetEnum getSelected() {
		ButtonModel model = group.getSelection();
		if (model == null) return null;
		for (int i = 0; i < arr.length; i++) {
			if (Objects.equals(buttons[i].getModel(), model)) return arr[i];
		}
		return null;
	}
	
	public MoodEnum getMood() {
		return mood;
	}
	//} Getter
	
	//Setter {
	public void setSelected(int index) {
		if (index < 0 || index >= group.getButtonCount()) throw new IndexOutOfBoundsException();
		Enumeration<AbstractButton> elements = group.getElements();
		AbstractButton button = null;
		int counter = 0;
		while (elements.hasMoreElements()) {
			button = elements.nextElement();
			if (counter == index) break;
			counter++;
		}
		Objects.requireNonNull(button).setSelected(true);
	}
	
	public void setMood(MoodEnum mood) {
		this.mood = mood;
		updateButtons();
	}
	//} Setter
	
	//Interfaces {
	@FunctionalInterface
	public interface Listener extends EventListener {
		
		//Methods {
		void imageChanged();
		//} Methods
		
	}
	//} Interfaces
	
}
//} Classes
