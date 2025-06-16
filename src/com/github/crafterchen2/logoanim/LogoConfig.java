package com.github.crafterchen2.logoanim;

//Classes {
public class LogoConfig implements ImmutableAssetProvider, ImmutableMoodProvider {
	
	//Fields {
	private final ImmutableAssetProvider asset;
	private final ImmutableMoodProvider mood;
	//} Fields
	
	//Constructor {
	public LogoConfig() {
		this(null, null);
	}
	
	public LogoConfig(ImmutableAssetProvider asset, ImmutableMoodProvider mood) {
		this.asset = (asset != null) ? asset : new ImmutableAssetProvider.Default();
		this.mood = (mood != null) ? mood : new ImmutableMoodProvider.Default();
	}
	
	public LogoConfig(LogoConfig other) {
		this((other != null) ? other.getAsset() : null, (other != null) ? other.getMood() : null);
	}
	//} Constructor
	
	//Overrides {
	@Override
	public AssetData getAsset(RegionEnum reg) {
		return asset.getAsset(reg);
	}
	
	@Override
	public MoodData getMood(RegionEnum reg) {
		return mood.getMood(reg);
	}

	@Override
	public String toString() {
		return asset.getAsset(RegionEnum.LEFT_EYE) + " " +
				asset.getAsset(RegionEnum.RIGHT_EYE) + " " +
				asset.getAsset(RegionEnum.SMILE) + " " +
				asset.getAsset(RegionEnum.DECO) + " " +
				mood.getMood(RegionEnum.LEFT_EYE) + " " +
				mood.getMood(RegionEnum.RIGHT_EYE) + " " +
				mood.getMood(RegionEnum.SMILE) + " " +
				mood.getMood(RegionEnum.DECO);
	}
	//} Overrides
}
//} Classes
