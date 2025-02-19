package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.AssetManager;
import com.github.crafterchen2.logoanim.MoodEnum;
import com.github.crafterchen2.logoanim.RegionEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class ImageSelector<T extends AssetManager> extends JComponent {
	
	private MoodEnum mood = MoodEnum.NORMAL;
	private final T[] arr;
	private final JToggleButton[] buttons;
	public final RegionEnum reg;
	public final int scale;
	private final ButtonGroup group = new ButtonGroup();
	
	public ImageSelector(T[] arr, int cols, RegionEnum reg) {
		this(arr, cols, reg, 10, true);
	}
	
	public ImageSelector(T[] arr, int cols, RegionEnum reg, int scale, boolean allowEmpty) {
		if (cols < 1) throw new IllegalArgumentException("cols must be at least 1.");
		if (arr == null) throw new IllegalArgumentException("arr must not be null.");
		if (reg == null) throw new IllegalArgumentException("reg must not be null.");
		this.arr = arr;
		buttons = new JToggleButton[this.arr.length];
		this.reg = reg;
		this.scale = scale;
		setLayout(new GridLayout(Math.ceilDiv(arr.length, cols), cols));
		if (allowEmpty) {
			JToggleButton button = new JToggleButton("", true);
			button.setBackground(new Color(0,0,0));
			add(button);
			group.add(button);
		}
		for (int i = 0; i < arr.length; i++) {
			buttons[i] = new JToggleButton(getIcon(arr[i]));
			buttons[i].setBackground(new Color(0, 0, 0));
			add(buttons[i]);
			group.add(buttons[i]);
		}
	}
	
	public T getSelected() {
		ButtonModel model = group.getSelection();
		if (model == null) return null;
		for (int i = 0; i < arr.length; i++) {
			if (Objects.equals(buttons[i].getModel(), model)) return arr[i];
		}
		return null;
	}
	
	private void updateButtons() {
		for (int i = 0; i < arr.length; i++) {
			buttons[i].setIcon(getIcon(arr[i]));
		}
	}
	
	public void setMood(MoodEnum mood){
	    this.mood = mood;
		updateButtons();
	}
	
	public MoodEnum getMood(){
	    return mood;
	}
	
	private ImageIcon getIcon(AssetManager asset) {
		BufferedImage img = asset.getImg();
		BufferedImage rv = AssetManager.recolorImg((mood != null) ? mood : MoodEnum.NORMAL , img);
		int w = (int) (scale * RegionEnum.base * (Math.abs(reg.w) * img.getWidth() / (Math.abs(reg.w) * RegionEnum.base)));
		int h = (int) (scale * RegionEnum.base * (Math.abs(reg.h) * img.getHeight() / (Math.abs(reg.h) * RegionEnum.base)));
		return new ImageIcon(rv.getScaledInstance(w,h, Image.SCALE_FAST));
	}
	
}
