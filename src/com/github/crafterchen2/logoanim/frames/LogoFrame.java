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
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

//Classes {
public class LogoFrame extends JFrame implements AssetProvider, MoodProvider {
	
	//Fields {
	private final LogoDisplay display;
	private final JCheckBox blinkBox = new JCheckBox("Enable blinking");
	private final Timer blinkTimer;
	private int scale = 20;
	private boolean shouldBlink = false;
	//} Fields
	
	//Constructor {
	public LogoFrame() throws HeadlessException {
		this(20, null, null);
	}
	
	public LogoFrame(int scale, AssetProvider defAssets, MoodProvider defMoods) throws HeadlessException {
		super("Logo Display");
		display = new LogoDisplay(defAssets, defMoods);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setScale(scale);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setContentPane(display);
		MouseAdapter mouseAdapter = makeMouseAdapter();
		addMouseWheelListener(mouseAdapter);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
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
		loadFrameIcon(this, "logo_frame_icon");
		setVisible(true);
	}
	//} Constructor
	
	//Methods {
	public static void loadFrameIcon(JFrame frame, String name) {
		try {
			BufferedImage read = ImageIO.read(Objects.requireNonNull(AssetEnum.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/" + name + ".png")));
			frame.setIconImage(read);
		} catch (IOException ignored) {
		}
	}
	
	private MouseAdapter makeMouseAdapter() {
		LogoFrame me = this;
		return new MouseAdapter() {
			//Fields {
			private final JMenu menu = makeMenu();
			private Point prev = null;
			//} Fields
			
			//Overrides {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int wheelRotation = e.getWheelRotation();
				if (getScale() + wheelRotation < RegionEnum.base) wheelRotation = Math.max(0, wheelRotation);
				if (getScale() + wheelRotation > RegionEnum.base * 8) wheelRotation = Math.min(0, wheelRotation);
				setScale(getScale() + wheelRotation);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					blinkBox.setSelected(getShouldBlink());
					menu.getPopupMenu().show(me, e.getX(), e.getY());
				} else {
					repaint();
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
		};
	}
	
	private void exportToClipboard() {
		BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		display.paint(g);
		g.dispose();
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		TransferableImage trans = new TransferableImage(img);
		c.setContents(trans, null);
	}
	
	private JMenu makeMenu() {
		JMenu menu = new JMenu();
		JLabel title = new JLabel("Logo Animator Menu");
		title.setFont(title.getFont().deriveFont(16f).deriveFont(Font.BOLD));
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(_ -> dispose());
		JMenuItem closeAll = new JMenuItem("Close all windows");
		closeAll.addActionListener(_ -> System.exit(0));
		JMenuItem openController = new JMenuItem("Open new controller");
		openController.addActionListener(_ -> new LogoControlFrame(this));
		JMenuItem openLibrary = new JMenuItem("Open new library");
		openLibrary.addActionListener(_ -> new PresetLibraryFrame(this));
		JMenuItem copyToClipboard = new JMenuItem("Copy to Clipboard");
		copyToClipboard.addActionListener(_ -> exportToClipboard());
		blinkBox.addActionListener(_ -> {
			if (blinkBox.isSelected() == getShouldBlink()) return;
			setShouldBlink(blinkBox.isSelected());
		});
		menu.add(title);
		menu.addSeparator();
		menu.add(blinkBox);
		menu.addSeparator();
		menu.add(openController);
		menu.add(openLibrary);
		menu.addSeparator();
		menu.add(copyToClipboard);
		menu.addSeparator();
		menu.add(close);
		menu.add(closeAll);
		return menu;
	}
	//} Methods
	
	//Getter {
	public int getScale() {
		return scale;
	}
	
	public boolean getShouldBlink() {
		return shouldBlink;
	}
	//} Getter
	
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
	
	public void setScale(int scale) {
		this.scale = scale;
		setSize(RegionEnum.base * scale, RegionEnum.base * scale);
	}
	//} Setter
	
	//Overrides {
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return display.getMood(reg);
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
	public void setAsset(RegionEnum reg, AssetEnum asset) {
		display.setAsset(reg, asset);
	}
	//} Overrides
	
	//Classes {
	private record TransferableImage(Image i) implements Transferable {
		
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
	//} Classes
}
//} Classes
