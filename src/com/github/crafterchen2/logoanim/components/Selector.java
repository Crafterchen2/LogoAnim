package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.RegionEnum;

import javax.swing.*;
import java.awt.*;

//Classes {
public abstract class Selector extends JComponent {
	
	//Fields {
	public final int outerMargin = 10;
	public final int innerMargin = 5;
	public final int minSizeButtons = 75;
	protected final Color disabledColor = new Color(192, 192, 192);
	//} Fields
	
	//Constructor {
	public Selector() {
		setDoubleBuffered(true);
	}
	//} Constructor
	
	//Methods {
	protected static void setEnabled(Container c, boolean enabled) {
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
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		setEnabled(this, enabled);
	}
	//} Overrides
	
	//Classes {
	protected static class SelectorButton<T> extends JButton {
		
		//Fields {
		public final RegionEnum reg;
		protected final T subject;
		//} Fields
		
		//Constructor {
		public SelectorButton(RegionEnum reg, T subject) {
			this.reg = reg;
			this.subject = subject;
		}
		//} Constructor
		
	}
	//} Classes
	
}
//} Classes
