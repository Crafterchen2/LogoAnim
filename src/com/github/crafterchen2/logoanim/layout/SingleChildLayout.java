package com.github.crafterchen2.logoanim.layout;

import java.awt.*;

//Classes {
public abstract class SingleChildLayout implements LayoutManager {
	
	//Methods {
	protected abstract void layoutChild(Component child, Container parent);
	
	protected abstract void applyBounds(Component child, int x, int y, int width, int height);
	
	protected Component getAffectedChild(Container parent) {
		synchronized (parent.getTreeLock()) {
			Component[] cs = parent.getComponents();
			if (cs.length < 1) return null;
			return cs[0];
		}
	}
	//} Methods
	
	//Overrides {
	@Override
	public void addLayoutComponent(String name, Component comp) {
		
	}
	
	@Override
	public void removeLayoutComponent(Component comp) {
		
	}
	
	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Component child = getAffectedChild(parent);
			if (child != null) layoutChild(child, parent);
		}
	}
	//} Overrides
	
}
//} Classes
