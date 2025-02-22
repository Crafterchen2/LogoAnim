package com.github.crafterchen2.logoanim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

//Classes {
public class LogoFrame extends JFrame {
	
	//Fields {
	public AssetEnum leftEye = null;
	public AssetEnum rightEye = null;
	public AssetEnum smile = null;
	public AssetEnum deco = null;
	public MoodEnum leftEyeMood = null;
	public MoodEnum rightEyeMood = null;
	public MoodEnum smileMood = null;
	public MoodEnum decoMood = null;
	private int scale = 20;
	//} Fields
	
	//Constructor {
	public LogoFrame() throws HeadlessException {
		super("Logo Display");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setScale(scale);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setContentPane(new JPanel(false) {
			//Overrides {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(0, 0, 0));
				g.fillRect(0, 0, getWidth(), getHeight());
				if (deco != null) deco.paint(g, RegionEnum.DECO, decoMood);
				if (smile != null) smile.paint(g, RegionEnum.SMILE, smileMood);
				if (rightEye != null) rightEye.paint(g, RegionEnum.RIGHT_EYE, rightEyeMood);
				if (leftEye != null) leftEye.paint(g, RegionEnum.LEFT_EYE, leftEyeMood);
			}
			//} Overrides
		});
		MouseAdapter mouseAdapter = makeMouseAdapter();
		addMouseWheelListener(mouseAdapter);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setVisible(true);
	}
	
	private MouseAdapter makeMouseAdapter() {
		LogoFrame me = this;
		return new MouseAdapter() {
			//Fields {
			private Point prev = null;
			private JMenu menu = makeMenu();
			//} Fields
			
			//Overrides {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int wheelRotation = e.getWheelRotation();
				if (getScale() < RegionEnum.base) wheelRotation = Math.max(0, wheelRotation);
				if (getScale() > RegionEnum.base * 8) wheelRotation = Math.min(0, wheelRotation);
				setScale(getScale() + wheelRotation);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
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
	//} Constructor
	
	private JMenu makeMenu(){
		JMenu menu = new JMenu();
		JLabel title = new JLabel("Logo Animator Menu");
		title.setFont(title.getFont().deriveFont(16f).deriveFont(Font.BOLD));
		JMenuItem close = new JMenuItem("Close");
		close.addActionListener(_ -> dispose());
		JMenuItem closeAll = new JMenuItem("Close all windows");
		closeAll.addActionListener(_ -> System.exit(0));
		JMenuItem openController = new JMenuItem("Open new controller");
		openController.addActionListener(_ -> new LogoControlFrame(this));
		menu.add(title);
		menu.addSeparator();
		menu.add(close);
		menu.add(closeAll);
		menu.addSeparator();
		menu.add(openController);
		return menu;
	}
	
	//Getter {
	public int getScale() {
		return scale;
	}
	//} Getter
	
	//Setter {
	public void setScale(int scale) {
		this.scale = scale;
		setSize(RegionEnum.base * scale, RegionEnum.base * scale);
	}
	//} Setter
}
//} Classes
