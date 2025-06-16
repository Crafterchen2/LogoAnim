package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.components.LogoDisplay;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public abstract class DisplayFrame extends JFrame implements AssetProvider, MoodProvider {
	
	protected boolean shouldBlink = false;
	protected final Timer blinkTimer;
	protected final JCheckBox blinkBox = new JCheckBox("Enable blinking");
	protected final LogoDisplay display;
	private final ArrayList<LogoChangedListener> listeners = new ArrayList<>();
	
	public DisplayFrame(String title) throws HeadlessException {
		this(title, null, null);
	}
	
	public DisplayFrame(String title, ImmutableAssetProvider defAssets, ImmutableMoodProvider defMoods) throws HeadlessException {
		super(title);
		display = new LogoDisplay(defAssets, defMoods);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(true);
		blinkBox.addActionListener(_ -> {
			if (blinkBox.isSelected() == getShouldBlink()) return;
			setShouldBlink(blinkBox.isSelected());
		});
		blinkTimer = new Timer(
				5000, _ -> {
			display.blink = true;
			repaint();
			Timer minor = new Timer(
					300, _ -> {
				display.blink = false;
				repaint();
			}
			);
			minor.setRepeats(false);
			minor.start();
		}
		);
		blinkTimer.setRepeats(true);
		blinkTimer.stop();
		applyMouseAdapter();
		setVisible(true);		
	}
	
	public void addLogoChangedListener(LogoChangedListener listener) {
		Objects.requireNonNull(listener);
		listeners.add(listener);
	}
	
	public void removeLogoChangedListener(LogoChangedListener listener) {
		Objects.requireNonNull(listener);
		listeners.remove(listener);
	}
	
	private void notifyListeners(){
		if (!listeners.isEmpty()) listeners.forEach(LogoChangedListener::logoChanged);
	}
	
	protected void applyMouseAdapter() {
		DisplayMouseAdapter mouseAdapter = new DisplayMouseAdapter(makeMenu(), this);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	protected JMenu makeMenu() {
		JMenu menu = new JMenu();
		JLabel title = new JLabel("Logo Animator Menu");
		title.setFont(title.getFont().deriveFont(16f).deriveFont(Font.BOLD));
		menu.add(title);
		menu.addSeparator();
		menu.add(blinkBox);
		menu.addSeparator();
		menu.add("Open new controller").addActionListener(_ -> new LogoControlFrame(this));
		menu.add("Open new library").addActionListener(_ -> new PresetLibraryFrame(this));
		menu.addSeparator();
		menu.add("Copy to Clipboard").addActionListener(_ -> exportToClipboard());
		menu.addSeparator();
		menu.add("Close").addActionListener(_ -> dispose());
		menu.add("Close all windows").addActionListener(_ -> System.exit(0));
		return menu;
	}
	
	public abstract int getScale();
	
	public abstract void setScale(int scale);
	
	public abstract int getMaxScale();
	
	public abstract int getMinScale();
	
	@Override
	public void setAsset(RegionEnum reg, AssetData asset) {
		display.setAsset(reg, asset);
		notifyListeners();
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodData mood) {
		display.setMood(reg, mood);
		notifyListeners();
	}
	
	@Override
	public AssetData getAsset(RegionEnum reg) {
		return display.getAsset(reg);
	}
	
	//Overrides {
	@Override
	public MoodData getMood(RegionEnum reg) {
		return display.getMood(reg);
	}
	
	//Setter {
	public void setShouldBlink(boolean shouldBlink) {
		if (this.shouldBlink == shouldBlink && blinkTimer.isRunning() == shouldBlink) return;
		this.shouldBlink = shouldBlink;
		blinkBox.setSelected(this.shouldBlink);
		if (this.shouldBlink) {
			blinkTimer.start();
		} else {
			blinkTimer.stop();
			display.blink = false;
			repaint();
		}
	}
	
	public boolean getShouldBlink() {
		return shouldBlink;
	}
	
	protected void exportToClipboard() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		display.paint(g);
		g.dispose();
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		TransferableImage trans = new TransferableImage(img);
		c.setContents(trans, null);
	}
	
	//Methods {
	public static void loadFrameIcon(JFrame frame, String name) {
		try {
			BufferedImage read = ImageIO.read(Objects.requireNonNull(DisplayFrame.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/" + name + ".png")));
			frame.setIconImage(read);
		} catch (IOException ignored) {
		}
	}
	
	protected record TransferableImage(Image i) implements Transferable {
		
		//Overrides {
		@Override
		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
			if (flavor != null && flavor.equals(DataFlavor.imageFlavor) && i != null) {
				return i;
			} else {
				throw new UnsupportedFlavorException(flavor);
			}
		}
		
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] flavors = new DataFlavor[1];
			flavors[0] = DataFlavor.imageFlavor;
			return flavors;
		}
		
		public boolean isDataFlavorSupported(DataFlavor flavor) {
			for (DataFlavor dataFlavor : getTransferDataFlavors()) {
				if (flavor.equals(dataFlavor)) {
					return true;
				}
			}
			return false;
		}
		//} Overrides
	}
	
	protected class DisplayMouseAdapter extends MouseAdapter {
		
		private final JMenu menu;
		private final Component invoker;
		private Point prev = null;
		
		public DisplayMouseAdapter(JMenu menu, Component invoker) {
			this.menu = menu;
			this.invoker = invoker;
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				blinkBox.setSelected(getShouldBlink());
				menu.getPopupMenu().show(invoker, e.getX(), e.getY());
			} else {
				invoker.repaint();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			prev = e.getLocationOnScreen();
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			if (prev == null) {
				prev = e.getLocationOnScreen();
			} else {
				Point loc = getLocation();
				Point now = e.getLocationOnScreen();
				setLocation(loc.x + now.x - prev.x, loc.y + now.y - prev.y);
				prev = now;
			}
		}
	}
}
