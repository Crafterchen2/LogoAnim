package com.github.crafterchen2.logoanim;

import java.util.Map;

//Interfaces {
public interface ImmutableAssetProvider extends Provider {
	
	//Methods {
	AssetData getAsset(RegionEnum reg);
	//} Methods
	
	//Getter {
	default ImmutableAssetProvider getAsset() {
		return new Default(
				getAsset(RegionEnum.LEFT_EYE),
				getAsset(RegionEnum.RIGHT_EYE),
				getAsset(RegionEnum.SMILE),
				getAsset(RegionEnum.DECO)
		);
	}
	//} Getter
	
	//Classes {
	class Default extends Provider.Default<AssetData> implements ImmutableAssetProvider {
		
		//Constructor {
		public Default() {
			super(null, null, null, null);
		}
		
		protected Default(Map<RegionEnum, AssetData> assets) {
			super(assets);
		}
		
		public Default(AssetData leftEye, AssetData rightEye, AssetData smile, AssetData deco) {
			super(leftEye, rightEye, smile, deco);
		}
		
		public Default(ImmutableAssetProvider assetProvider) {
			super(makeMap(assetProvider));
		}
		//} Constructor
		
		//Methods {
		private static Map<RegionEnum, AssetData> makeMap(ImmutableAssetProvider prov) {
			if (prov == null) return Map.of();
			return makeMap(
					prov.getAsset(RegionEnum.LEFT_EYE),
					prov.getAsset(RegionEnum.RIGHT_EYE),
					prov.getAsset(RegionEnum.SMILE),
					prov.getAsset(RegionEnum.DECO)
						  );
		}
		//} Methods
		
		//Overrides {
		@Override
		public AssetData getAsset(RegionEnum reg) {
			return map.get(reg);
		}
		//} Overrides
	}
	//} Classes
	
}
//} Interfaces
