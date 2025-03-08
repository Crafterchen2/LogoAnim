package com.github.crafterchen2.logoanim;

import java.util.*;

public interface ImmutableAssetProvider extends Provider{
	
	AssetEnum getAsset(RegionEnum reg);
	default ImmutableAssetProvider getAsset() {
		return new Default(
				getAsset(RegionEnum.LEFT_EYE),
				getAsset(RegionEnum.RIGHT_EYE),
				getAsset(RegionEnum.SMILE),
				getAsset(RegionEnum.DECO));
	}
	
	class Default extends Provider.Default<AssetEnum> implements ImmutableAssetProvider {
		
		public Default() {
			super(null, null, null, null);
		}
		
		protected Default(Map<RegionEnum, AssetEnum> assets) {
			super(assets);
		}
		
		public Default(AssetEnum leftEye, AssetEnum rightEye, AssetEnum smile, AssetEnum deco) {
			super(leftEye, rightEye, smile, deco);
		}
		
		public Default(ImmutableAssetProvider assetProvider) {
			super(makeMap(assetProvider));
		}
		
		private static Map<RegionEnum, AssetEnum> makeMap(ImmutableAssetProvider prov) {
			if (prov == null) return Map.of();
			return makeMap(
					prov.getAsset(RegionEnum.LEFT_EYE),
					prov.getAsset(RegionEnum.RIGHT_EYE),
					prov.getAsset(RegionEnum.SMILE),
					prov.getAsset(RegionEnum.DECO));
		}
		
		@Override
		public AssetEnum getAsset(RegionEnum reg) {
			return map.get(reg);
		}
	}
	
}
