package com.github.crafterchen2.logoanim;

import java.awt.*;

public enum MoodEnum {
	
	NORMAL(new Color(0, 0, 255)),
	MAD(new Color(255, 0, 0)),
	GOOD(new Color(0, 255, 0)),
	STUNNED(new Color(255, 255, 255)),
	;
	
	private Color color;
	
	MoodEnum(Color color) {
		this.color = color;
	}
	
	public Color getColor(){
	    return color;
	}
}
