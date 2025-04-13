package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.AssetProvider;
import com.github.crafterchen2.logoanim.MoodProvider;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class StreamFrame extends DisplayFrame {
	
	private int scale;
	private final BufferedImage bgImg;
	
	public StreamFrame() throws HeadlessException {
		this(true);
	}
	
	public StreamFrame(boolean fullscreen) throws HeadlessException {
		this(fullscreen, null, null);
	}
	
	public StreamFrame(boolean fullscreen, AssetProvider defAssets, MoodProvider defMoods) throws HeadlessException {
		super("Stream Background", defAssets, defMoods);
		try {
			bgImg = ImageIO.read(Objects.requireNonNull(DisplayFrame.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/stream_bg.png")));
			Objects.requireNonNull(bgImg);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setFullscreen(fullscreen);
		JComponent bgc = new JComponent() {
			@Override
			protected void paintComponent(Graphics g) {
				final int w = bgImg.getWidth() * getScale();
				final int h = bgImg.getHeight() * getScale();
				g.drawImage(bgImg, 0,0, w, h,null);
			}
		};
		setContentPane(bgc);
		setLayout(null);
		add(display);
		loadFrameIcon(this, "streaming_frame_icon");
		repaint();
	}
	
	@Override
	protected void applyMouseAdapter() {
		DisplayMouseAdapter mouseAdapter = new DisplayMouseAdapter(makeMenu(), this) {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isFullscreen()) return;
				super.mouseDragged(e);
			}
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	@Override
	protected JMenu makeMenu() {
		JMenu menu = super.makeMenu();
		menu.addSeparator();
		JMenuItem item = new JMenuItem("Toggle Fullscreen");
		item.addActionListener(_ -> setFullscreen(!isFullscreen()));
		menu.add(item);
		return menu;
	}
	
	private boolean isFullscreen() {
		return getScale() == getMaxScale();
	}
	
	@Override
	public int getScale() {
		return scale;
	}
	
	public void setFullscreen(boolean fullscreen) {
		setScale((fullscreen) ? getMaxScale() : getMinScale());
	}
	
	@Override
	public void setScale(int scale) {
		this.scale = Math.clamp(scale, getMinScale(), getMaxScale());
		if (isFullscreen()) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		} else {
			setExtendedState(JFrame.NORMAL);
			setSize(bgImg.getWidth() * getScale(), bgImg.getHeight() * getScale());
		}
		display.setBounds(bgImg.getWidth() * getScale() - 84 * this.scale, bgImg.getHeight() * getScale() - 84 * this.scale, 80 * this.scale, 80 * this.scale);
	}
	
	@Override
	public int getMaxScale() {
		return 5;
	}
	
	@Override
	public int getMinScale() {
		return 1;
	}
}
