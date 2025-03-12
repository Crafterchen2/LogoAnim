package com.github.crafterchen2.logoanim;

public class FullLogoConfig extends LogoConfig{
	
	public final int scale;
	public final boolean blink;
	
	public FullLogoConfig(int scale, boolean blink, LogoConfig config) {
		this(scale, blink, config.getAsset(), config.getMood());
	}
	
	public FullLogoConfig(int scale, boolean blink, ImmutableAssetProvider asset, ImmutableMoodProvider mood) {
		super(asset, mood);
		this.scale = scale;
		this.blink = blink;
	}
}
