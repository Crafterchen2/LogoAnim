package com.github.crafterchen2.logoanim;

//Enums {
public enum RegionEnum {
	
	LEFT_EYE(4.0, 4.0, -3.0, -3.0, AssetType.EYE),
	RIGHT_EYE(6.0, 4.0, 3.0, -3.0, AssetType.EYE),
	SMILE(2.0, 6.0, 6.0, 3.0, AssetType.SMILE),
	DECO(0.0, 0.0, 10.0, 10.0, AssetType.DECO),
	;
	
	//Fields {
	public static final int base = 10;
	
	public final double x, y, w, h;
	
	/**
	 * The aspect ratio of the area.<br/>
	 * ratio == 1.0 => Area is a square<br/>
	 * ratio > 1.0 => Width > Height<br/>
	 * ratio < 1.0 => Height > Width<br/>
	 */
	public final double ratio;
	public final AssetType type;
	//} Fields
	
	//Constructor {
	RegionEnum(double x, double y, double w, double h, AssetType type) {
		this.x = x / base;
		this.y = y / base;
		this.w = w / base;
		this.h = h / base;
		this.type = type;
		ratio = Math.abs(this.w) / Math.abs(this.h);
	}
	//} Constructor
}
//} Enums
