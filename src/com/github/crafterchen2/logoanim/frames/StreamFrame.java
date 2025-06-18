package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.AssetProvider;
import com.github.crafterchen2.logoanim.JavaFxWrapper;
import com.github.crafterchen2.logoanim.MoodProvider;
import com.github.crafterchen2.logoanim.components.LogoDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

//Classes {
public class StreamFrame extends DisplayFrame {
	
	//Fields {
	private final BufferedImage bgImg;
	private final JMenuItem toggleChat = new JMenuItem("Chat is booting...");
	private final JPanel logoPanel;
	private int scale;
	//} Fields
	
	//Constructor {
	public StreamFrame() throws HeadlessException {
		this(true);
	}
	
	public StreamFrame(boolean fullscreen) throws HeadlessException {
		this(fullscreen, null, null);
	}
	
	public StreamFrame(boolean fullscreen, AssetProvider defAssets, MoodProvider defMoods) throws HeadlessException {
		super("Stream Background", defAssets, defMoods);
		toggleChat.addActionListener(_ -> JavaFxWrapper.getWrapper().setControlVisibility(!JavaFxWrapper.getWrapper().getControlVisibility()));
		toggleChat.setEnabled(false);
		try {
			bgImg = ImageIO.read(Objects.requireNonNull(StreamFrame.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/stream/background.png")));
			Objects.requireNonNull(bgImg);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		setFullscreen(fullscreen);
		JPanel contentPane = makeContentPane();
		{
			logoPanel = new JPanel(new LogoLayout());
			{
				logoPanel.add(display, "");
			}
			contentPane.add(logoPanel, RootLayout.LOGO);
			new Thread(() -> {
				JavaFxWrapper wrapper = JavaFxWrapper.getWrapper();
				contentPane.add(wrapper.getChatPanel(), RootLayout.CHAT);
				toggleChat.setText("Toggle Chat Controller");
				toggleChat.setEnabled(true);
			}).start();
		}
		setContentPane(contentPane);
		loadFrameIcon(this, "stream");
		addMenuEntry(0, "Open new remote manager").addActionListener(_ -> new RemoteManagerFrame(this));
		addMenuEntry(-100, "Toggle Fullscreen").addActionListener(_ -> setFullscreen(!isFullscreen()));
		addMenuEntry(-100, toggleChat);
		rebuildMenu();
		repaint();
	}
	//} Constructor
	
	//Methods {
	public boolean tryAddLogo(String name, LogoDisplay display) {
		if (name == null || name.isBlank()) return false;
		logoPanel.add(display, name);
		return true;
	}
	
	public void updateLogoPanel() {
		logoPanel.updateUI();
	}
	
	public void removeLogo(LogoDisplay display) {
		logoPanel.remove(display);
	}
	
	private JPanel makeContentPane() {
		return new JPanel(new RootLayout(), true) {
			//Overrides {
			@Override
			protected void paintComponent(Graphics g) {
				final int w = bgImg.getWidth() * getScale();
				final int h = bgImg.getHeight() * getScale();
				g.drawImage(bgImg, 0, 0, w, h, null);
			}
			//} Overrides
		};
	}
	
	private Rectangle calcChatPos() {
		int w = 80;
		int h = 124;
		int x = 300;
		int y = 4;
		int s = getScale();
		return new Rectangle(x * s, y * s, w * s, h * s);
	}
	
	private Rectangle calcLogoPos() {
		int logo = 80;
		int margin = 4;
		int indent = logo + margin;
		int scale = getScale();
		return new Rectangle((bgImg.getWidth() - indent) * scale, (bgImg.getHeight() - indent) * scale, logo * scale, logo * scale);
	}
	//} Methods
	
	//Getter {
	private boolean isFullscreen() {
		return getScale() == getMaxScale();
	}
	//} Getter
	
	//Setter {
	public void setFullscreen(boolean fullscreen) {
		setScale((fullscreen) ? getMaxScale() : getMinScale());
	}
	//} Setter
	
	//Overrides {
	@Override
	protected DisplayMouseAdapter makeMouseAdapter() {
		return new DisplayMouseAdapter(this) {
			//Overrides {
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
			//} Overrides
		};
	}
	
	@Override
	public int getScale() {
		return scale;
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
	//} Overrides
	
	//Classes {
	private class RootLayout implements LayoutManager {
		
		//Fields {
		public static final String CHAT = "chat";
		public static final String LOGO = "logo";
		
		private final HashMap<Component, String> map = HashMap.newHashMap(2);
		//} Fields
		
		//Overrides {
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
						default -> {
						}
					}
				}
			}
		}
		//} Overrides
		
	}
	
	private class LogoLayout implements LayoutManager {
		
		//Fields {
		public static final long displayID = -1L;
		private final HashMap<Component, Long> displayMap = new HashMap<>();
		//} Fields
		
		//Overrides {
		@Override
		public void addLayoutComponent(String name, Component comp) {
			if (name == null || name.isEmpty()) {
				if (Objects.equals(comp, display)) {
					displayMap.put(display, displayID);
				} else {
					throw new IllegalArgumentException("Only the hosts display may use null or an empty string as a name.");
				}
			} else {
				if (name.isBlank()) throw new IllegalArgumentException("name must not be blank.");
				displayMap.put(comp, System.currentTimeMillis());
			}
		}
		
		@Override
		public void removeLayoutComponent(Component comp) {
			displayMap.remove(comp);
		}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			Dimension min = minimumLayoutSize(parent);
			return new Dimension(min.width * getScale(), min.height * getScale());
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(80, 80);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				Component display = null;
				final ArrayList<Component> guests = new ArrayList<>();
				for (Component c : parent.getComponents()) {
					if (c != null && displayMap.containsKey(c)) {
						if (displayMap.get(c) == displayID) {
							display = c;
						} else {
							guests.add(c);
						}
					}
				}
				Dimension pref = preferredLayoutSize(parent);
				int w = pref.width;
				int h = pref.height;
				int nDisplays = displayMap.size();
				switch (nDisplays) {
					case 0 -> {
					}
					case 1 -> {
						if (display != null) {
							display.setBounds(0, 0, w, h);
						}
					}
					case 2 -> {
						int y = h / 4;
						w /= 2;
						h /= 2;
						if (display != null) {
							display.setBounds(0, y, w, h);
						}
						if (!guests.isEmpty()) {
							guests.getFirst().setBounds(w, y, w, h);
						}
					}
					case 3 -> {
						int x = w / 4;
						w /= 2;
						h /= 2;
						if (display != null) {
							display.setBounds(x, 0, w, h);
						}
						for (int i = 0; i < guests.size() && i < 2; i++) {
							guests.get(i).setBounds(w * i, h, w, h);
						}
					}
					default -> {
						//TODO: Square placer algo here (currently a lesser, temporary algo implemented.)
						int gridSize = (int) Math.ceil(Math.log(nDisplays) / Math.log(2));
						w = pref.width / gridSize;
						h = pref.height / gridSize;
						if (display != null) {
							display.setBounds(0, 0, w, h);
						}
						for (int i = 1; i <= guests.size(); i++) {
							guests.get(i - 1).setBounds(w * (i % gridSize), h * (i / gridSize), w, h);
						}
					}
				}
			}
		}
		//} Overrides
	}
	//} Classes
}
//} Classes
