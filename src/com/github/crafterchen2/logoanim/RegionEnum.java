package com.github.crafterchen2.logoanim;

//Enums {
public enum RegionEnum {
	
	LEFT_EYE(4.0, 4.0, -3.0, -3.0),
	RIGHT_EYE(6.0, 4.0, 3.0, -3.0),
	SMILE(2.0, 6.0, 6.0, 2.0),
	DECO(0.0, 0.0, 10.0, 10.0),
	;
	
	//Fields {
	public static final int base = 10;
	
	public final double x, y, w, h;
	//} Fields
	
	//Constructor {
	RegionEnum(double x, double y, double w, double h) {
		this.x = x / base;
		this.y = y / base;
		this.w = w / base;
		this.h = h / base;
	}
	//} Constructor
}
//} Enums
