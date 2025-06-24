package com.github.crafterchen2.logoanim.layout;

import java.awt.*;

public class RatioLayout extends CenterLayout {
	
	//Fields {
	private final int ratioWidth, ratioHeight, minWidth, minHeight;
	//} Fields
	
	//Constructor {
	public RatioLayout(int minWidth, int minHeight) {
		int ggt = (minHeight == minWidth) ? minHeight : ggt(minWidth, minHeight);
		ratioWidth = minWidth / ggt;
		ratioHeight = minHeight / ggt;
		this.minWidth = minWidth;
		this.minHeight = minHeight;
	}
	//} Constructor
	
	//Methods {
	private static int ggt(int a, int b) {
		while (b != 0) {
			int h = a % b;
			a = b;
			b = h;
		}
		return a;
	}
	//} Methods
	
	//Overrides {
	@Override
	public Dimension preferredLayoutSize(Container parent) {
		int w = parent.getWidth();
		int h = parent.getHeight();
		w -= w % ratioWidth;
		h -= h % ratioHeight;
		int nw = (h * ratioWidth) / ratioHeight;
		int nh = (w * ratioHeight) / ratioWidth;
		if (nw > w) {
			h = nh;
		} else {
			w = nw;
		}
		return new Dimension(Math.max(w, minWidth), Math.max(h, minHeight));
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return new Dimension(minWidth, minHeight);
	}
	
	@Override
	protected void applyBounds(Component child, int x, int y, int width, int height) {
		child.setBounds(x,y,width,height);
	}
	//} Overrides
}
