package com.github.crafterchen2.logoanim.layout;

import java.awt.*;

//Classes {
public class CenterLayout extends SingleChildLayout {
	
	//Overrides {
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return getAffectedChild(parent).getPreferredSize();
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return getAffectedChild(parent).getMinimumSize();
	}
	
	@Override
	protected void layoutChild(Component child, Container parent) {
		Dimension size = preferredLayoutSize(parent);
		int x = parent.getWidth() / 2 - size.width / 2;
		int y = parent.getHeight() / 2 - size.height / 2;
		applyBounds(child, x, y, size.width, size.height);
	}
	
	@Override
	protected void applyBounds(Component child, int x, int y, int width, int height) {
		child.setLocation(x, y);
	}
	//} Overrides
}
//} Classes
