package com.github.crafterchen2.logoanim;

import java.util.HashMap;

//Interfaces {
public interface AssetProvider extends ImmutableAssetProvider {
	//Methods {
	void setAsset(RegionEnum reg, AssetEnum asset);
	
	@Override
	default AssetProvider getAsset() {
		return new Default(
				getAsset(RegionEnum.LEFT_EYE), 
				getAsset(RegionEnum.RIGHT_EYE), 
				getAsset(RegionEnum.SMILE), 
				getAsset(RegionEnum.DECO));
	}
	//} Methods
	
	//Setter {
	default void setAsset(ImmutableAssetProvider assets) {
		if (assets == null) return;
		for (RegionEnum reg : RegionEnum.values()) {
			setAsset(reg, assets.getAsset(reg));
		}
	}
	//} Setter
	
	//Classes {
	class Default extends ImmutableAssetProvider.Default implements AssetProvider {
		
		//Constructor {
		public Default() {
			this(null, null, null, null);
		}
		
		public Default(AssetEnum leftEye, AssetEnum rightEye, AssetEnum smile, AssetEnum deco) {
			super(HashMap.newHashMap(RegionEnum.values().length));
			setAsset(RegionEnum.LEFT_EYE, leftEye);
			setAsset(RegionEnum.RIGHT_EYE, rightEye);
			setAsset(RegionEnum.SMILE, smile);
			setAsset(RegionEnum.DECO, deco);
		}
		
		public Default(ImmutableAssetProvider assetProvider) {
			super(HashMap.newHashMap(RegionEnum.values().length));
			setAsset(assetProvider);
		}
		//} Constructor
		
		//Overrides {
		@Override
		public void setAsset(RegionEnum reg, AssetEnum asset) {
			map.put(reg, asset);
		}
		//} Overrides
	}
	//} Classes
}
//} Interfaces
