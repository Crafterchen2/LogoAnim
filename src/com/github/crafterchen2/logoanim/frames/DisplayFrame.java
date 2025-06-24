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
import java.util.HashMap;
import java.util.Objects;

public abstract class DisplayFrame extends JFrame implements AssetProvider, MoodProvider {
	
	//Fields {
	protected final Timer blinkTimer;
	protected final JCheckBox blinkBox = new JCheckBox("Enable blinking");
	protected final LogoDisplay display;
	private final ArrayList<LogoChangedListener> listeners = new ArrayList<>();
	private final HashMap<Integer, ArrayList<Component>> menuEntries = HashMap.newHashMap(3);
	private final DisplayMouseAdapter mouseAdapter;
	protected boolean shouldBlink = false;
	//} Fields
	
	//Constructor {
	public DisplayFrame(String title) throws HeadlessException {
		this(title, null, null);
	}
	
	public DisplayFrame(String title, ImmutableAssetProvider defAssets, ImmutableMoodProvider defMoods) throws HeadlessException {
		super(title);
		display = new LogoDisplay(defAssets, defMoods);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setUndecorated(isUndecorated());
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
		mouseAdapter = makeMouseAdapter();
		applyMouseAdapter();
		addMenuEntry(Integer.MAX_VALUE, blinkBox);
		addMenuEntry(0, "Open new controller").addActionListener(_ -> new LogoControlFrame(this));
		addMenuEntry(0, "Open new library").addActionListener(_ -> new PresetLibraryFrame(this));
		addMenuEntry(0, "Open new client connector").addActionListener(_ -> new ClientConnectorFrame(this));
		addMenuEntry(Integer.MIN_VALUE + 100, "Copy to Clipboard").addActionListener(_ -> exportToClipboard());
		addMenuEntry(Integer.MIN_VALUE, "Close").addActionListener(_ -> System.exit(0));
		rebuildMenu();
		setVisible(true);
	}
	
	public boolean isUndecorated() {
		return true;
	}
	//} Constructor
	
	//Methods {
	public static void loadFrameIcon(JFrame frame, String name) {
		try {
			BufferedImage read = ImageIO.read(Objects.requireNonNull(DisplayFrame.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/icons/" + name + ".png")));
			frame.setIconImage(read);
		} catch (IOException ignored) {
		}
	}
	
	public void addLogoChangedListener(LogoChangedListener listener) {
		Objects.requireNonNull(listener);
		listeners.add(listener);
	}
	
	public void removeLogoChangedListener(LogoChangedListener listener) {
		Objects.requireNonNull(listener);
		listeners.remove(listener);
	}
	
	private void notifyListeners() {
		if (!listeners.isEmpty()) listeners.forEach(LogoChangedListener::logoChanged);
	}
	
	protected void applyMouseAdapter() {
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
	}
	
	protected DisplayMouseAdapter makeMouseAdapter() {
		return new DisplayMouseAdapter(this);
	}
	
	public void addMenuEntry(int prio, Component c) {
		if (c == null) return;
		ArrayList<Component> v = menuEntries.computeIfAbsent(prio, integer -> new ArrayList<>());
		v.add(c);
	}
	
	public JMenuItem addMenuEntry(int prio, String label) {
		JMenuItem c = new JMenuItem(label);
		ArrayList<Component> v = menuEntries.computeIfAbsent(prio, integer -> new ArrayList<>());
		v.add(c);
		return c;
	}
	
	public void removeMenuEntry(int prio, Component c) {
		ArrayList<Component> v = menuEntries.get(prio);
		if (v == null) return;
		v.remove(c);
		if (v.isEmpty()) menuEntries.remove(prio);
	}
	
	public void rebuildMenu() {
		mouseAdapter.rebuildMenu();
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
	
	//Getter {
	public abstract int getScale();
	
	public abstract int getMaxScale();
	
	public abstract int getMinScale();
	
	public boolean getShouldBlink() {
		return shouldBlink;
	}
	//} Getter
	
	//Setter {
	public abstract void setScale(int scale);
	
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
	
	//Overrides {
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
	
	@Override
	public MoodData getMood(RegionEnum reg) {
		return display.getMood(reg);
	}
	
	//Classes {
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
		
		//Fields {
		private final JMenu menu = new JMenu();
		private final Component invoker;
		private Point prev = null;
		//} Fields
		
		//Constructor {
		public DisplayMouseAdapter(Component invoker) {
			menu.add(new JLabel("Default menu"));
			this.invoker = invoker;
		}
		//} Constructor
		
		//Methods {
		public void rebuildMenu() {
			menu.getPopupMenu().setVisible(false);
			menu.removeAll();
			JLabel title = new JLabel("Logo Animator Menu   ");
			title.setFont(title.getFont().deriveFont(16f).deriveFont(Font.BOLD));
			menu.add(title);
			java.util.List<Integer> sortedKeys = menuEntries.keySet().stream().sorted((o1, o2) -> Integer.compare(o2, o1)).toList();
			for (int k : sortedKeys) {
				menu.addSeparator();
				ArrayList<Component> v = menuEntries.get(k);
				for (Component c : v) {
					menu.add(c);
				}
			}
		}
		//} Methods
		
		//Overrides {
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				blinkBox.setSelected(getShouldBlink());
				menu.getPopupMenu().show(invoker, e.getX() - 10, e.getY() - 10);
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
		//} Overrides
	}
	//} Classes
}
