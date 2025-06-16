package com.github.crafterchen2.logoanim;

import java.util.Map;

//Interfaces {
public interface ImmutableMoodProvider extends Provider {
	
	//Methods {
	MoodData getMood(RegionEnum reg);
	//} Methods
	
	//Getter {
	default ImmutableMoodProvider getMood() {
		return new Default(
				getMood(RegionEnum.LEFT_EYE),
				getMood(RegionEnum.RIGHT_EYE),
				getMood(RegionEnum.SMILE),
				getMood(RegionEnum.DECO)
		);
	}
	//} Getter
	
	//Classes {
	class Default extends Provider.Default<MoodData> implements ImmutableMoodProvider {
		
		//Constructor {
		public Default() {
			super(null, null, null, null);
		}
		
		protected Default(Map<RegionEnum, MoodData> moods) {
			super(moods);
		}
		
		public Default(MoodData leftEye, MoodData rightEye, MoodData smile, MoodData deco) {
			super(leftEye, rightEye, smile, deco);
		}
		
		public Default(ImmutableMoodProvider moodProvider) {
			super(makeMap(moodProvider));
		}
		//} Constructor
		
		//Methods {
		private static Map<RegionEnum, MoodData> makeMap(ImmutableMoodProvider prov) {
			if (prov == null) return Map.of();
			return makeMap(
					prov.getMood(RegionEnum.LEFT_EYE),
					prov.getMood(RegionEnum.RIGHT_EYE),
					prov.getMood(RegionEnum.SMILE),
					prov.getMood(RegionEnum.DECO)
						  );
		}
		//} Methods
		
		//Overrides {
		@Override
		public MoodData getMood(RegionEnum reg) {
			return map.get(reg);
		}
		//} Overrides
	}
	//} Classes
}
//} Interfaces
