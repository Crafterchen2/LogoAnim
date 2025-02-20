package com.github.crafterchen2.logoanim;

import java.awt.*;

//Enums {
public enum MoodEnum {
	
	NORMAL(new Color(0, 0, 255)),
	MAD(new Color(255, 0, 0)),
	GOOD(new Color(0, 255, 0)),
	STUNNED(new Color(255, 255, 255)),
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
	public Color getColor() {
		return color;
	}
	//} Getter
}
//} Enums
