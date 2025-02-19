package com.github.crafterchen2.logoanim;

import java.util.Base64;

public enum RegionEnum {
	
	RIGHT_EYE(4.0, 4.0, -3.0, -3.0),
	LEFT_EYE(6.0, 4.0, 3.0, -3.0),
	SMILE(2.0, 6.0, 6.0, 2.0),
	DECO(0.0,0.0,10.0,10.0),
	;
	
	public static final int base = 10; 
	
	public final double x, y, w, h;
	
	RegionEnum(double x, double y, double w, double h) {
		this.x = x / base;
		this.y = y / base;
		this.w = w / base;
		this.h = h / base;
	}
}
