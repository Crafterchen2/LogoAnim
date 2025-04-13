package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.*;

import java.awt.*;
import java.awt.event.MouseWheelEvent;

//Classes {
public class LogoFrame extends DisplayFrame {
	
	private int scale = 20;
	//} Fields
	
	//Constructor {
	public LogoFrame() throws HeadlessException {
		this(20, null, null);
	}
	
	public LogoFrame(int scale, AssetProvider defAssets, MoodProvider defMoods) throws HeadlessException {
		super("Logo Display", defAssets, defMoods);
		setScale(scale);
		setContentPane(display);
		loadFrameIcon(this, "logo_frame_icon");
	}
	
	@Override
	protected void applyMouseAdapter() {
		DisplayMouseAdapter mouseAdapter = new DisplayMouseAdapter(makeMenu(), this) {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int wheelRotation = e.getWheelRotation();
				if (getScale() + wheelRotation < getMinScale()) wheelRotation = Math.max(0, wheelRotation);
				if (getScale() + wheelRotation > getMaxScale()) wheelRotation = Math.min(0, wheelRotation);
				setScale(getScale() + wheelRotation);
			}
		};
		addMouseWheelListener(mouseAdapter);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	@Override
	public int getScale() {
		return scale;
	}
	
	@Override
	public void setScale(int scale) {
		this.scale = Math.clamp(scale, getMinScale(), getMaxScale());
		setSize(RegionEnum.base * scale, RegionEnum.base * scale);
	}
	
	@Override
	public int getMaxScale() {
		return RegionEnum.base * 8;
	}
	
	@Override
	public int getMinScale() {
		return RegionEnum.base;
	}
}
