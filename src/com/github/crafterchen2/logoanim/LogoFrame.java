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
				if (leftEye != null) leftEye.paint(g, RegionEnum.LEFT_EYE, leftEyeMood);
				if (rightEye != null) rightEye.paint(g, RegionEnum.RIGHT_EYE, rightEyeMood);
			}
			//} Overrides
		});
		MouseAdapter mouseAdapter = new MouseAdapter() {
			//Fields {
			private Point prev = null;
			//} Fields
			
			//Overrides {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				int wheelRotation = e.getWheelRotation();
				if (getScale() < 10) wheelRotation = Math.max(0, wheelRotation);
				if (getScale() > 80) wheelRotation = Math.min(0, wheelRotation);
				setScale(getScale() + wheelRotation);
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				repaint();
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
		addMouseWheelListener(mouseAdapter);
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setVisible(true);
	}
	//} Constructor
	
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
