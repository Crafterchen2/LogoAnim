package com.github.crafterchen2.logoanim;

import java.util.HashMap;

//Interfaces {
public interface MoodProvider extends ImmutableMoodProvider {
	//Methods {
	void setMood(RegionEnum reg, MoodData mood);
	//} Methods
	
	//Setter {
	default void setMood(ImmutableMoodProvider moods) {
		if (moods == null) return;
		for (RegionEnum reg : RegionEnum.values()) {
			setMood(reg, moods.getMood(reg));
		}
	}
	//} Setter
	
	//Overrides {
	@Override
	default MoodProvider getMood() {
		return new Default(
				getMood(RegionEnum.LEFT_EYE),
				getMood(RegionEnum.RIGHT_EYE),
				getMood(RegionEnum.SMILE),
				getMood(RegionEnum.DECO)
		);
	}
	//} Overrides
	
	//Classes {
	class Default extends ImmutableMoodProvider.Default implements MoodProvider {
		
		//Constructor {
		public Default() {
			this(null, null, null, null);
		}
		
		public Default(MoodData leftEye, MoodData rightEye, MoodData smile, MoodData deco) {
			super(HashMap.newHashMap(RegionEnum.values().length));
			setMood(RegionEnum.LEFT_EYE, leftEye);
			setMood(RegionEnum.RIGHT_EYE, rightEye);
			setMood(RegionEnum.SMILE, smile);
			setMood(RegionEnum.DECO, deco);
		}
		
		public Default(ImmutableMoodProvider moodProvider) {
			super(HashMap.newHashMap(RegionEnum.values().length));
			setMood(moodProvider);
		}
		//} Constructor
		
		//Overrides {
		@Override
		public void setMood(RegionEnum reg, MoodData mood) {
			map.put(reg, mood);
		}
		//} Overrides
	}
	//} Classes
}
//} Interfaces
