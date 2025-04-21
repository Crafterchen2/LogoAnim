package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.AssetEnum;
import com.github.crafterchen2.logoanim.AssetProvider;
import com.github.crafterchen2.logoanim.MoodEnum;
import com.github.crafterchen2.logoanim.MoodProvider;
import com.github.crafterchen2.logoanim.components.LogoDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
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
		setContentPane(makeContentPane());
		setLayout(new Layout());
		add(display);
		add(new LogoDisplay(new AssetProvider.Default(AssetEnum.EYE_2X2, AssetEnum.EYE_2X2, AssetEnum.SMIRK, null), new MoodProvider.Default(MoodEnum.STAR, MoodEnum.GOOD, null, MoodEnum.SANS)));
		loadFrameIcon(this, "streaming_frame_icon");
		repaint();
	}
	
	private JPanel makeContentPane() {
		return new JPanel(true) {
			@Override
			protected void paintComponent(Graphics g) {
				final int w = bgImg.getWidth() * getScale();
				final int h = bgImg.getHeight() * getScale();
				g.drawImage(bgImg, 0, 0, w, h, null);
			}
		};
	}
	
	private Rectangle calcLogoPos() {
		int logo = 80;
		int margin = 4;
		int indent = logo + margin;
		int scale = getScale();
		return new Rectangle((bgImg.getWidth() - indent) * scale, (bgImg.getHeight() - indent) * scale, logo * scale, logo * scale);
	}
	
	@Override
	protected void applyMouseAdapter() {
		DisplayMouseAdapter mouseAdapter = new DisplayMouseAdapter(makeMenu(), this) {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (isFullscreen()) return;
				super.mouseDragged(e);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON2) {
					setFullscreen(!isFullscreen());
				} else {
					super.mouseClicked(e);
				}
			}
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	@Override
	protected JMenu makeMenu() {
		JMenu menu = super.makeMenu();
		menu.addSeparator();
		menu.add("Toggle Fullscreen").addActionListener(_ -> setFullscreen(!isFullscreen()));
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
	}
	
	@Override
	public int getMaxScale() {
		return 5;
	}
	
	@Override
	public int getMinScale() {
		return 1;
	}
	
	private class Layout implements LayoutManager {
		
		@Override
		public void addLayoutComponent(String name, Component comp) {
		
		}
		
		@Override
		public void removeLayoutComponent(Component comp) {
		
		}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return minimumLayoutSize(parent);
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			int s = 80 * getScale();
			return new Dimension(s, s);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				LogoDisplay[] ds = Arrays.stream(parent.getComponents())
						.filter(component -> component instanceof LogoDisplay)
						.toList().toArray(new LogoDisplay[0]); //Is warning, but works reliably because of the filter.
				if (ds.length == 0) return;
				if (ds.length == 1) {
					ds[0].setBounds(calcLogoPos());
					return;
				}
				Rectangle p = calcLogoPos();
				final Dimension mainMin = new Dimension(p.width / 2, p.height / 2);
				
			}
		}
	}
}
