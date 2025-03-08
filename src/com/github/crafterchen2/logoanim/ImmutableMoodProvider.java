package com.github.crafterchen2.logoanim;

import java.util.Map;

public interface ImmutableMoodProvider extends Provider {
	
	MoodEnum getMood(RegionEnum reg);
	default ImmutableMoodProvider getMood() {
		return new Default(
				getMood(RegionEnum.LEFT_EYE),
				getMood(RegionEnum.RIGHT_EYE),
				getMood(RegionEnum.SMILE),
				getMood(RegionEnum.DECO));
	}
	
	class Default extends Provider.Default<MoodEnum> implements ImmutableMoodProvider {
		
		public Default() {
			super(null, null, null, null);
		}
		
		protected Default(Map<RegionEnum, MoodEnum> moods) {
			super(moods);
		}
		
		public Default(MoodEnum leftEye, MoodEnum rightEye, MoodEnum smile, MoodEnum deco) {
			super(leftEye, rightEye, smile, deco);
		}
		
		public Default(ImmutableMoodProvider moodProvider) {
			super(makeMap(moodProvider));
		}
		
		private static Map<RegionEnum, MoodEnum> makeMap(ImmutableMoodProvider prov) {
			if (prov == null) return Map.of();
			return makeMap(
					prov.getMood(RegionEnum.LEFT_EYE),
					prov.getMood(RegionEnum.RIGHT_EYE),
					prov.getMood(RegionEnum.SMILE),
					prov.getMood(RegionEnum.DECO));
		}
		
		@Override
		public MoodEnum getMood(RegionEnum reg) {
			return map.get(reg);
		}
	}
}
