package com.github.crafterchen2.logoanim;

import java.awt.*;

//Enums {
public enum MoodEnum implements MoodData {
	
	NORMAL(new Color(0, 0, 255)),
	SANS(new Color(0, 255, 255)),
	MAD(new Color(255, 0, 0)),
	GOOD(new Color(0, 255, 0)),
	STUNNED(new Color(170, 170, 170)),
	STAR(new Color(255, 251, 68)),
	;
	
	//Fields {
	private final Color color;
	//} Fields
	
	//Constructor {
	MoodEnum(Color color) {
		this.color = color;
	}
	//} Constructor
	
	//Getter {
	@Override
	public Color getColor() {
		return color;
	}
	//} Getter
}
//} Enums
