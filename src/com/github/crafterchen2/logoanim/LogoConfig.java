package com.github.crafterchen2.logoanim;

public class LogoConfig implements ImmutableAssetProvider, ImmutableMoodProvider {
	
	final ImmutableAssetProvider asset;
	final ImmutableMoodProvider mood;
	
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
	
	@Override
	public AssetEnum getAsset(RegionEnum reg) {
		return asset.getAsset(reg);
	}
	
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return mood.getMood(reg);
	}
}
