package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.components.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

//Classes {
public class StreamFrame extends JFrame implements AssetProvider, MoodProvider {
	
	//Fields {
	public static final int MANAGER_PADDING = 10;
	public static final int STREAM_RES_HEIGHT = 1080;
	public static final int STREAM_RES_WIDTH = 1920;
	public static final int STREAM_FPS = 60;
	public static final int SCALE;
	public static final int CAP_X;
	public static final int CAP_Y;
	public static final int CAP_W;
	public static final int CAP_H;
	public static final int CHAT_X;
	public static final int CHAT_Y;
	public static final int CHAT_W;
	public static final int CHAT_H;
	public static final int LOGO_X;
	public static final int LOGO_Y;
	public static final int LOGO_W;
	public static final int LOGO_H;
	public static final int INFO_X;
	public static final int INFO_Y;
	public static final int INFO_W;
	public static final int INFO_H;
	
	private static final BufferedImage bgImg;
	
	private final LogoDisplay display;
	private final JPanel background = makeBackgroundPane();
	//} Fields
	
	//Constructor {
	static {
		final int capX = 4;
		final int capY = 18;
		final int capW = 288;
		final int capH = 162;
		final int chatX = 300;
		final int chatY = 4;
		final int chatW = 80;
		final int chatH = 124;
		final int logoX = 300;
		final int logoY = 132;
		final int logoW = 80;
		final int logoH = 80;
		final int infoX = 168;
		final int infoY = 188;
		final int infoW = 128;
		final int infoH = 24;
		{
			BufferedImage temp;
			try {
				temp = ImageIO.read(Objects.requireNonNull(StreamFrame.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/stream_bg.png")));
				Objects.requireNonNull(temp);
			} catch (Exception e) {
				final int w = 384;
				final int h = 216;
				temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				{
					Graphics g = temp.getGraphics();
					g.setColor(new Color(0xBAC0C7));
					g.fillRect(0, 0, w, h);
					g.setColor(new Color(0x9C0000));
					g.fillRect(capX - 1, capY - 1, capW + 2, capH + 2);
					g.setColor(new Color(0x9C9C00));
					g.fillRect(capX, capY, capW, capH);
					g.setColor(new Color(0x009C00));
					g.fillRect(chatX - 1, chatY - 1, chatW + 2, chatH + 2);
					g.setColor(new Color(0x009C9C));
					g.fillRect(chatX, chatY, chatW, chatH);
					g.setColor(new Color(0x00009C));
					g.fillRect(logoX - 1, logoY - 1, logoW + 2, logoH + 2);
					g.setColor(new Color(0x9C009C));
					g.fillRect(logoX, logoY, logoW, logoH);
					g.setColor(new Color(0x636363));
					g.fillRect(infoX - 1, infoY - 1, infoW + 2, infoH + 2);
					g.setColor(new Color(0xCCCCCC));
					g.fillRect(infoX, infoY, infoW, infoH);
					g.setColor(Color.RED);
					g.drawString("Error: Could not find", 1, 195);
					g.drawString("background image.", 34, 210);
					g.dispose();
				}
			}
			bgImg = new BufferedImage(STREAM_RES_WIDTH, STREAM_RES_HEIGHT, BufferedImage.TYPE_INT_RGB);
			{
				Graphics g = bgImg.getGraphics();
				g.drawImage(temp, 0, 0, STREAM_RES_WIDTH, STREAM_RES_HEIGHT, null);
				g.dispose();
			}
			SCALE = STREAM_RES_HEIGHT / temp.getHeight();
		}
		CAP_X = SCALE * capX;
		CAP_Y = SCALE * capY;
		CAP_W = SCALE * capW;
		CAP_H = SCALE * capH;
		CHAT_X = SCALE * chatX;
		CHAT_Y = SCALE * chatY;
		CHAT_W = SCALE * chatW;
		CHAT_H = SCALE * chatH;
		LOGO_X = SCALE * logoX;
		LOGO_Y = SCALE * logoY;
		LOGO_W = SCALE * logoW;
		LOGO_H = SCALE * logoH;
		INFO_X = SCALE * infoX;
		INFO_Y = SCALE * infoY;
		INFO_W = SCALE * infoW;
		INFO_H = SCALE * infoH;
	}
	
	public StreamFrame() throws HeadlessException {
		this(null, null);
	}
	
	public StreamFrame(AssetProvider defAssets, MoodProvider defMoods) throws HeadlessException {
		super("Stream Manager");
		setSize(2304, 1296);
		setLocation(100,100);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		display = new LogoDisplay(defAssets, defMoods);
		final Timer blinkTimer = new Timer(5000, _ -> {
			display.blink = true;
			display.repaint();
			Timer minor = new Timer(300, _ -> {
				display.blink = false;
				repaint();
			}
			);
			minor.setRepeats(false);
			minor.start();
		}
		);
		blinkTimer.setRepeats(true);
		blinkTimer.start();
		JPanel root = new JPanel(new RootLayout());
		{
			JPanel logos = new JPanel(new LogoLayout());
			{
				logos.add(display);
			}
			logos.setBounds(LOGO_X, LOGO_Y, LOGO_W, LOGO_H);
			background.add(logos);
			JComponent chat = JavaFxWrapper.getWrapper().getChatPanel();
			chat.setBounds(CHAT_X, CHAT_Y, CHAT_W, CHAT_H);
			background.add(chat);
			JButton info = new JButton("Info Placeholder");
			info.setBounds(INFO_X, INFO_Y, INFO_W, INFO_H);
			background.add(info);
		}
		root.add(background, RootLayout.STREAM);
		JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
		{
			JPanel lc = new JPanel(new BorderLayout(0,5));
			{
				JCheckBox blinkBox = new JCheckBox("Enable blinking");
				{
					blinkBox.setSelected(true);
					blinkBox.setBorder(BorderFactory.createLoweredBevelBorder());
					blinkBox.setBorderPainted(true);
					blinkBox.addActionListener(_ -> {
						if (blinkBox.isSelected()) {
							blinkTimer.restart();
						} else {
							blinkTimer.stop();
							display.blink = false;
							display.repaint();
						}
					});
				}
				lc.add(blinkBox, BorderLayout.NORTH);
				JPanel big = new JPanel(new GridLayout(3,1,0,5));
				{
					AtomicBoolean ignoreListener = new AtomicBoolean(false);
					PresetLibrary presetLibrary = new PresetLibrary(display, display);
					AssetSelector assetSelector = new AssetSelector(display);
					MoodSelector moodSelector = new MoodSelector();
					assetSelector.addAssetChangedListener(() -> {
						if (ignoreListener.get()) return;
						for (RegionEnum reg : RegionEnum.values()) {
							display.setAsset(reg, assetSelector.getAsset(reg));
						}
						display.repaint();
					});
					moodSelector.addMoodChangedListener(() -> {
						if (ignoreListener.get()) return;
						for (RegionEnum reg : RegionEnum.values()) {
							display.setMood(reg, moodSelector.getMood(reg));
						}
						display.repaint();
						assetSelector.repaint();
					});
					presetLibrary.addMoodChangedListener(() -> {
						ignoreListener.set(true);
						for (RegionEnum reg : RegionEnum.values()) {
							moodSelector.setMood(reg, display.getMood(reg));
							assetSelector.setAsset(reg, display.getAsset(reg));
						}
						display.repaint();
						ignoreListener.set(false);
					});
					presetLibrary.setBorder(BorderFactory.createLoweredBevelBorder());
					assetSelector.setBorder(BorderFactory.createLoweredBevelBorder());
					moodSelector.setBorder(BorderFactory.createLoweredBevelBorder());
					big.add(presetLibrary);
					big.add(assetSelector);
					big.add(moodSelector);
				}
				lc.add(big, BorderLayout.CENTER);
			}
			tabs.addTab("Logo", lc);
			JPanel sc = new JPanel(new BorderLayout(0,5));
			{
				JPanel chatControlPanel = JavaFxWrapper.getWrapper().getChatControlPanel();
				chatControlPanel.setBorder(BorderFactory.createLoweredBevelBorder());
				sc.add(chatControlPanel, BorderLayout.NORTH);
			}
			tabs.addTab("Stream", sc);
			tabs.addTab("Remote", new JButton("Remote Control"));
		}
		root.add(tabs, RootLayout.TABS);
		root.add(new JButton("south"), RootLayout.SOUTH);
		setContentPane(root);
		DisplayFrame.loadFrameIcon(this, "streaming_frame_icon");
		setVisible(true);
		repaint();
	}
	//} Constructor
	
	//Methods {
	private JPanel makeBackgroundPane() {
		return new JPanel(null,true) {
			//Overrides {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(bgImg, 0,0, null);
			}
			//} Overrides
		};
	}
	//} Methods
	
	//Overrides {
	@Override
	public void setAsset(RegionEnum reg, AssetEnum asset) {
		display.setAsset(reg, asset);
	}
	
	@Override
	public AssetEnum getAsset(RegionEnum reg) {
		return display.getAsset(reg);
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodEnum mood) {
		display.setMood(reg, mood);
	}
	
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return display.getMood(reg);
	}
	//} Overrides
	
	//Classes {
	private class RootLayout implements LayoutManager {
		
		//Fields {
		public static final String STREAM = "stream";
		public static final String SOUTH = "south";
		public static final String TABS = "tabs";
		
		private final HashMap<Component, String> map = HashMap.newHashMap(2);
		private final int left;
		private final int right;
		private final int top;
		private final int bottom;
		
		//} Fields
		
		public RootLayout() {
			Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
			left = screenInsets.left;
			right = screenInsets.right;
			top = screenInsets.top;
			bottom = screenInsets.bottom;
		}
		
		//Overrides {
		@Override
		public void addLayoutComponent(String name, Component comp) {
			if (name == null || (!name.equals(STREAM) && !name.equals(SOUTH) && !name.equals(TABS))) throw new IllegalArgumentException("Illegal key for layout");
			map.put(comp, name);
		}
		
		@Override
		public void removeLayoutComponent(Component comp) {
			map.remove(comp);
		}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return null;
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(getWidth(), getHeight());
		}
		
		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				for (Component c : parent.getComponents()) {
					String v = map.get(c);
					if (v == null) continue;
					switch (v) {
						case STREAM -> c.setBounds(MANAGER_PADDING + left, MANAGER_PADDING + top, STREAM_RES_WIDTH, STREAM_RES_HEIGHT);
						case SOUTH -> c.setBounds(MANAGER_PADDING + left, STREAM_RES_HEIGHT + MANAGER_PADDING * 2 + top, STREAM_RES_WIDTH, getHeight() - (MANAGER_PADDING * 3 + STREAM_RES_HEIGHT + top + bottom));
						case TABS -> c.setBounds((MANAGER_PADDING * 2) + STREAM_RES_WIDTH + left, MANAGER_PADDING + top, getWidth() - (MANAGER_PADDING * 3 + STREAM_RES_WIDTH + left + right + 16), getHeight() - (MANAGER_PADDING * 2 + top + bottom));
						default -> System.out.println("ignoring component: " + c.toString());
					}
				}
			}
		}
		//} Overrides
		
	}
	
	private static class LogoLayout implements LayoutManager {
		
		//Overrides {
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
			return new Dimension(LOGO_W, LOGO_H);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				for (Component c : parent.getComponents()) {
					//TODO: implement square place alg
					c.setBounds(0, 0, LOGO_W, LOGO_H);
				}
			}
		}
		//} Overrides
	}
	//} Classes
}
//} Classes
