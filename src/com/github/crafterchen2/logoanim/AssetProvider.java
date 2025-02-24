package com.github.crafterchen2.logoanim;

import java.util.HashMap;

//Interfaces {
public interface AssetProvider {
	//Methods {
	void setAsset(RegionEnum reg, AssetEnum asset);
	
	AssetEnum getAsset(RegionEnum reg);
	
	default AssetProvider getAsset() {
		return new Default(getAsset(RegionEnum.LEFT_EYE), getAsset(RegionEnum.RIGHT_EYE), getAsset(RegionEnum.SMILE), getAsset(RegionEnum.DECO));
	}
	//} Methods
	
	//Setter {
	default void setAsset(AssetProvider assets) {
		if (assets == null) return;
		for (RegionEnum reg : RegionEnum.values()) {
			setAsset(reg, assets.getAsset(reg));
		}
	}
	//} Setter
	
	//Classes {
	class Default implements AssetProvider {
		
		//Fields {
		private final HashMap<RegionEnum, AssetEnum> assets = HashMap.newHashMap(RegionEnum.values().length);
		//} Fields
		
		//Constructor {
		public Default() {
			this(null, null, null, null);
		}
		
		public Default(AssetEnum leftEye, AssetEnum rightEye, AssetEnum smile, AssetEnum deco) {
			setAsset(RegionEnum.LEFT_EYE, leftEye);
			setAsset(RegionEnum.RIGHT_EYE, rightEye);
			setAsset(RegionEnum.SMILE, smile);
			setAsset(RegionEnum.DECO, deco);
		}
		//} Constructor
		
		//Overrides {
		@Override
		public void setAsset(RegionEnum reg, AssetEnum asset) {
			assets.put(reg, asset);
		}
		
		@Override
		public AssetEnum getAsset(RegionEnum reg) {
			return assets.get(reg);
		}
		//} Overrides
	}
	//} Classes
}
//} Interfaces
