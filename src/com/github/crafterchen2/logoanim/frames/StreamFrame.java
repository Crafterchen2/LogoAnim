package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
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
		JPanel contentPane = makeContentPane();
		{
			contentPane.setLayout(new RootLayout());
			JPanel logos = new JPanel(new LogoLayout());
			{
				logos.add(display);
			}
			contentPane.add(logos, RootLayout.LOGO);
			JavaFxWrapper wrapper = JavaFxWrapper.getWrapper();
			//TODO: remove
			wrapper.setVideoID("UB7BOqQ_WPE");
			wrapper.setChatVisible(true);
			//remove end
			contentPane.add(wrapper.getChatPanel(), RootLayout.CHAT);
		}
		setContentPane(contentPane);
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
	
	private Rectangle calcChatPos() {
		int w = 80;
		int h = 124;
		int x = 300;
		int y = 4;
		int s = getScale();
		return new Rectangle(x * s, y *s, w * s, h * s);
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
		return 4;
	}
	
	private class RootLayout implements LayoutManager {
		
		public static final String CHAT = "chat";
		public static final String LOGO = "logo";
		
		private HashMap<Component, String> map = HashMap.newHashMap(2);
		
		@Override
		public void addLayoutComponent(String name, Component comp) {
			if (name == null || (!name.equals(CHAT) && !name.equals(LOGO))) throw new IllegalArgumentException("Illegal key for layout");
			map.put(comp, name);
		}
		
		@Override
		public void removeLayoutComponent(Component comp) {
			map.remove(comp);
		}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return minimumLayoutSize(parent);
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(bgImg.getWidth() * getScale(), bgImg.getHeight() * getScale());
		}
		
		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				for (Component c : parent.getComponents()) {
					String v = map.get(c);
					if (v == null) continue;
					switch (v) {
						case CHAT -> c.setBounds(calcChatPos());
						case LOGO -> c.setBounds(calcLogoPos());
						default -> {}
					}
				}
			}
		}
		
	}
	
	private class LogoLayout implements LayoutManager {
		
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
				for (Component c : parent.getComponents()) {
					Dimension s = calcLogoPos().getSize();
					c.setBounds(0,0,s.width, s.height);
				}
			}
		}
	}
}
