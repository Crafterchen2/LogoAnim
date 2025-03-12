package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.MoodEnum;
import com.github.crafterchen2.logoanim.MoodProvider;
import com.github.crafterchen2.logoanim.RegionEnum;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;

//Classes {
public class MoodSelector extends Selector implements MoodProvider {
	
	//Fields {
	private final HashMap<RegionEnum, MoodEnum> moods = HashMap.newHashMap(RegionEnum.values().length);
	private final ArrayList<Listener> listeners = new ArrayList<>();
	//} Fields
	
	//Constructor {
	public MoodSelector() {
		setLayout(new GridLayout(2, 2, outerMargin * 3, outerMargin * 3));
		Border emptyBorder = BorderFactory.createEmptyBorder(outerMargin * 2, outerMargin * 2, outerMargin * 2, outerMargin * 2);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), emptyBorder));
		int nButtons = MoodEnum.values().length + 1;
		int sqrtMoods = (int) Math.ceil(Math.sqrt(nButtons));
		int minHeight = Math.ceilDiv(nButtons, sqrtMoods) * minSizeButtons + Math.floorDiv(nButtons, sqrtMoods) * innerMargin;
		minHeight += 2 * outerMargin * 2;
		minHeight += outerMargin * 3;
		int minWidth = Math.min(nButtons, sqrtMoods) * minSizeButtons + Math.max(0, Math.min(nButtons, sqrtMoods) - 1) * minSizeButtons;
		minWidth += 2 * outerMargin * 2;
		minWidth += outerMargin * 3;
		setPreferredSize(new Dimension(minWidth, minHeight));
		for (RegionEnum reg : RegionEnum.values()) {
			JPanel panel = new JPanel(new GridLayout(0, sqrtMoods, innerMargin, innerMargin));
			panel.setOpaque(false);
			panel.add(new MoodButton(reg, null));
			for (MoodEnum mood : MoodEnum.values()) {
				panel.add(new MoodButton(reg, mood));
			}
			add(panel);
		}
	}
	//} Constructor
	
	//Methods {
	public void removeMoodChangedListener(Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		listeners.remove(listener);
	}
	
	public void addMoodChangedListener(Listener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null.");
		listeners.add(listener);
	}
	
	private void signalUpdate() {
		if (isEnabled()) listeners.forEach(Listener::moodChanged);
	}
	
	private Color safeGetMoodColor(RegionEnum reg) {
		if (!isEnabled()) return disabledColor;
		MoodEnum mood = getMood(reg);
		return (mood != null) ? mood.getColor() : new Color(0, 0, 0);
	}
	//} Methods
	
	//Overrides {
	public void setMood(RegionEnum reg, MoodEnum mood) {
		if (reg == null) throw new IllegalArgumentException("reg must not be null.");
		moods.put(reg, mood);
		repaint();
		signalUpdate();
	}
	
	public MoodEnum getMood(RegionEnum reg) {
		if (reg == null) throw new IllegalArgumentException("reg must not be null.");
		return moods.get(reg);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(safeGetMoodColor(RegionEnum.DECO));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(safeGetMoodColor(RegionEnum.SMILE));
		int width = getWidth() - outerMargin * 2;
		int height = getHeight() - outerMargin * 2;
		g.fillRect(outerMargin, outerMargin, width, height);
		g.setColor(safeGetMoodColor(RegionEnum.RIGHT_EYE));
		g.fillRect(outerMargin, outerMargin, width, height /= 2);
		g.setColor(safeGetMoodColor(RegionEnum.LEFT_EYE));
		g.fillRect(outerMargin, outerMargin, width / 2, height);
	}
	//} Overrides
	
	//Classes {
	private class MoodButton extends SelectorButton<MoodEnum> {
		
		//Constructor {
		public MoodButton(RegionEnum reg, MoodEnum subject) {
			super(reg, subject);
			//setBorder(BorderFactory.createLineBorder(new Color(96, 96, 96), 3));
			addActionListener(_ -> setMood(reg, subject));
		}
		//} Constructor
		
		//Overrides {
		@Override
		protected void paintComponent(Graphics g) {
			if (!isEnabled()) {
				g.setColor(disabledColor);
			} else {
				g.setColor((subject != null) ? subject.getColor() : new Color(0, 0, 0));
			}
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		//} Overrides
	}
	//} Classes
	
	//Interfaces {
	@FunctionalInterface
	public interface Listener extends EventListener {
		
		//Methods {
		void moodChanged();
		//} Methods
		
	}
	//} Interfaces
	
}
//} Classes
