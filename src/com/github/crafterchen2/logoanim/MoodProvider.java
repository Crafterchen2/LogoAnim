package com.github.crafterchen2.logoanim;

import java.util.HashMap;

//Interfaces {
public interface MoodProvider {
	//Methods {
	void setMood(RegionEnum reg, MoodEnum mood);
	
	MoodEnum getMood(RegionEnum reg);
	
	default MoodProvider getMood() {
		return new Default(getMood(RegionEnum.LEFT_EYE), getMood(RegionEnum.RIGHT_EYE), getMood(RegionEnum.SMILE), getMood(RegionEnum.DECO));
	}
	//} Methods
	
	//Setter {
	default void setMood(MoodProvider moods) {
		if (moods == null) return;
		for (RegionEnum reg : RegionEnum.values()) {
			setMood(reg, moods.getMood(reg));
		}
	}
	//} Setter
	
	//Classes {
	class Default implements MoodProvider {
		
		//Fields {
		private final HashMap<RegionEnum, MoodEnum> moods = HashMap.newHashMap(RegionEnum.values().length);
		//} Fields
		
		//Constructor {
		public Default() {
			this(null, null, null, null);
		}
		
		public Default(MoodEnum leftEye, MoodEnum rightEye, MoodEnum smile, MoodEnum deco) {
			setMood(RegionEnum.LEFT_EYE, leftEye);
			setMood(RegionEnum.RIGHT_EYE, rightEye);
			setMood(RegionEnum.SMILE, smile);
			setMood(RegionEnum.DECO, deco);
		}
		//} Constructor
		
		//Overrides {
		@Override
		public void setMood(RegionEnum reg, MoodEnum mood) {
			moods.put(reg, mood);
		}
		
		@Override
		public MoodEnum getMood(RegionEnum reg) {
			return moods.get(reg);
		}
		//} Overrides
	}
	//} Classes
}
//} Interfaces
