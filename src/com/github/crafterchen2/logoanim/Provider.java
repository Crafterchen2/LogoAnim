package com.github.crafterchen2.logoanim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Interfaces {
public interface Provider {
	
	//Classes {
	abstract class Default<T> implements Provider {
		
		//Fields {
		protected final Map<RegionEnum, T> map;
		//} Fields
		
		//Constructor {
		public Default() {
			this(null, null, null, null);
		}
		
		protected Default(Map<RegionEnum, T> map) {
			this.map = map;
		}
		
		public Default(T leftEye, T rightEye, T smile, T deco) {
			this(makeMap(leftEye, rightEye, smile, deco));
		}
		//} Constructor
		
		//Methods {
		protected static <T> Map<RegionEnum, T> makeMap(T leftEye, T rightEye, T smile, T deco) {
			final HashMap<RegionEnum, T> demo = HashMap.newHashMap(RegionEnum.values().length);
			if (leftEye != null) demo.put(RegionEnum.LEFT_EYE, leftEye);
			if (rightEye != null) demo.put(RegionEnum.RIGHT_EYE, rightEye);
			if (smile != null) demo.put(RegionEnum.SMILE, smile);
			if (deco != null) demo.put(RegionEnum.DECO, deco);
			final List<Map.Entry<RegionEnum, T>> list = demo.entrySet().stream().toList();
			final int size = list.size();
			if (size == 0) return Map.of();
			/* IMPROVEME: This is janky. We don't like generic arrays >[
			 * The problem: Map.ofEntries(Map.Entry<RegionEnum, T>... vararg).
			 * We need to create a generic array, which is not allowed and Array.newInstance(...)
			 * returns unfortunately not an array type, but a regular Object, which we can't cast since
			 * we can't create the target type and can't cast to generics.
			 */
			final Map.Entry<RegionEnum, T> e0, e1, e2, e3;
			e0 = list.get(0);
			e1 = (size > 1) ? list.get(1) : null;
			e2 = (size > 2) ? list.get(2) : null;
			e3 = (size > 3) ? list.get(3) : null;
			return switch (size) {
				case 1 -> Map.ofEntries(e0);
				case 2 -> Map.ofEntries(e0, e1);
				case 3 -> Map.ofEntries(e0, e1, e2);
				default -> Map.ofEntries(e0, e1, e2, e3);
			};
		}
		//} Methods
		
	}
	//} Classes
	
}
//} Interfaces
