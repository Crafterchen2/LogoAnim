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

//Classes {
public class MoodSelector extends JComponent {
	
	//Fields {
	public final int outerMargin = 10;
	public final int innerMargin = 5;
	public final int minSizeButtons = 75;
	private final HashMap<RegionEnum, MoodEnum> moods = HashMap.newHashMap(RegionEnum.values().length);
	private final ArrayList<Listener> listeners = new ArrayList<>();
	private final Color disabledColor = new Color(192,192,192);
	//} Fields
	
	//Constructor {
	public MoodSelector() {
		setDoubleBuffered(true);
		Border emptyBorder = BorderFactory.createEmptyBorder(outerMargin * 2, outerMargin * 2, outerMargin * 2, outerMargin * 2);
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED), emptyBorder));
		setLayout(new GridLayout(2, 2, outerMargin * 3, outerMargin * 3));
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
		listeners.remove(listener);
	}
	
	public void addMoodChangedListener(Listener listener) {
		listeners.add(listener);
	}
	
	private void signalUpdate() {
		if (isEnabled()) listeners.forEach(Listener::moodChanged);
	}
	
	public void setMood(RegionEnum reg, MoodEnum mood) {
		moods.put(reg, mood);
		repaint();
	}
	
	public MoodEnum getMood(RegionEnum reg) {
		return moods.get(reg);
	}
	
	private Color safeGetMoodColor(RegionEnum reg) {
		if (!isEnabled()) return disabledColor;
		MoodEnum mood = getMood(reg);
		return (mood != null) ? mood.getColor() : new Color(0, 0, 0);
	}
	
	private static void setEnabled(Container c, boolean enabled) {
		for (Component component : c.getComponents()) {
			component.setEnabled(enabled);
			if (component instanceof Container container) {
				if (container.getComponents().length > 0) setEnabled(container, enabled);
			}
		}
	}
	//} Methods
	
	//Overrides {
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
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		setEnabled(this, enabled);
	}	
	//} Overrides
	
	//Classes {
	private class MoodButton extends JButton {
		
		//Fields {
		public final RegionEnum reg;
		public final MoodEnum mood;
		//} Fields
		
		//Constructor {
		public MoodButton(RegionEnum reg, MoodEnum mood) {
			this.reg = reg;
			this.mood = mood;
			setBorder(BorderFactory.createLineBorder(new Color(96, 96, 96), 3));
			setMinimumSize(new Dimension(minSizeButtons, minSizeButtons));
			addActionListener(_ -> {
				setMood(reg, mood);
				signalUpdate();
			});
		}
		//} Constructor
		
		//Overrides {
		@Override
		protected void paintComponent(Graphics g) {
			if (!isEnabled()) {
				g.setColor(disabledColor);
			} else {
				g.setColor((mood != null) ? mood.getColor() : new Color(0, 0, 0));
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
