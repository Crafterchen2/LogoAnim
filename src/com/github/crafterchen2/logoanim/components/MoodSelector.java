package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.MoodEnum;
import com.github.crafterchen2.logoanim.RegionEnum;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

public class MoodSelector extends JComponent {
	
	private final HashMap<RegionEnum, MoodEnum> moods = HashMap.newHashMap(RegionEnum.values().length);
	public final int outerMargin = 10;
	public final int innerMargin = 5;
	private final ArrayList<Listener> listeners = new ArrayList<>();
	
	public MoodSelector() {
		Border emptyBorder = BorderFactory.createEmptyBorder(outerMargin * 2, outerMargin * 2, outerMargin * 2, outerMargin * 2);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), emptyBorder));
		setLayout(new GridLayout(2,2, outerMargin * 3, outerMargin * 3));
		int sqrtMoods = (int) Math.ceil(Math.sqrt(MoodEnum.values().length + 1));
		for (RegionEnum reg : RegionEnum.values()) {
			JPanel panel = new JPanel(new GridLayout(sqrtMoods, sqrtMoods, innerMargin, innerMargin));
			panel.setOpaque(false);
			panel.add(new MoodButton(reg, null));
			for (MoodEnum mood : MoodEnum.values()) {
				panel.add(new MoodButton(reg, mood));
			}
			add(panel);
		}
	}
	
	public void removeMoodChangedListener(Listener listener) {
		listeners.remove(listener);
	}
	
	public void addMoodChangedListener(Listener listener) {
		listeners.add(listener);
	}
	
	private void signalUpdate(){
		listeners.forEach(Listener::moodChanged);
	}
	
	public void setMood(RegionEnum reg, MoodEnum mood) {
		moods.put(reg, mood);
		repaint();
	}
	
	public MoodEnum getMood(RegionEnum reg) {
		return moods.get(reg);
	}
	
	private Color safeGetMoodColor(RegionEnum reg) {
		MoodEnum mood = getMood(reg);
		return (mood != null) ? mood.getColor() : new Color(0,0,0);
	}
	
	@Override
	public Dimension getPreferredSize() {
		int min = Math.min(getWidth(), getHeight());
		return new Dimension(min, min);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(safeGetMoodColor(RegionEnum.DECO));
		g.fillRect(0,0,getWidth(), getHeight());
		g.setColor(safeGetMoodColor(RegionEnum.SMILE));
		int width = getWidth() - outerMargin * 2;
		int height = getHeight() - outerMargin * 2;
		g.fillRect(outerMargin, outerMargin, width, height);
		g.setColor(safeGetMoodColor(RegionEnum.LEFT_EYE));
		g.fillRect(outerMargin, outerMargin, width, height /= 2);
		g.setColor(safeGetMoodColor(RegionEnum.RIGHT_EYE));
		g.fillRect(outerMargin, outerMargin, width / 2, height);
	}
	
	private class MoodButton extends JButton {
		
		public final RegionEnum reg;
		public final MoodEnum mood;
		
		public MoodButton(RegionEnum reg, MoodEnum mood) {
			this.reg = reg;
			this.mood = mood;
			setBorder(BorderFactory.createLineBorder(new Color(96, 96, 96),3));
			addActionListener(_ -> {
				setMood(reg, mood);
				signalUpdate();
			});
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			g.setColor((mood != null) ? mood.getColor() : new Color(0,0,0));
			g.fillRect(0,0,getWidth(), getHeight());
		}
	}
	
	@FunctionalInterface
	public static interface Listener extends EventListener {
		
		void moodChanged();
		
	}
	
}
