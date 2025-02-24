package com.github.crafterchen2.logoanim;

//Enums {
public enum RegionEnum {
	
	LEFT_EYE(4.0, 4.0, -3.0, -3.0, AssetType.EYE),
	RIGHT_EYE(6.0, 4.0, 3.0, -3.0, AssetType.EYE),
	SMILE(2.0, 6.0, 6.0, 2.0, AssetType.SMILE),
	DECO(0.0, 0.0, 10.0, 10.0, AssetType.DECO),
	;
	
	//Fields {
	public static final int base = 10;
	
	public final double x, y, w, h;
	public final AssetType type;
	//} Fields
	
	//Constructor {
	RegionEnum(double x, double y, double w, double h, AssetType type) {
		this.x = x / base;
		this.y = y / base;
		this.w = w / base;
		this.h = h / base;
		this.type = type;
	}
	//} Constructor
}
//} Enums
