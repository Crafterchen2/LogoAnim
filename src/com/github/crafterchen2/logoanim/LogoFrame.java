package com.github.crafterchen2.logoanim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LogoFrame extends JFrame {
	
	private int scale = 20;
	public AssetManager leftEye = null;
	public AssetManager rightEye = null;
	public AssetManager smile = null;
	public AssetManager deco = null;
	public MoodEnum leftEyeMood = null;
	public MoodEnum rightEyeMood = null;
	public MoodEnum smileMood = null;
	public MoodEnum decoMood = null;
	
	public LogoFrame() throws HeadlessException {
		super("Logo Display");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setScale(scale);
		setLocationRelativeTo(null);
		setUndecorated(true);
		setContentPane(new JPanel(false) {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(new Color(0,0,0));
				g.fillRect(0,0,getWidth(), getHeight());
				if (deco != null) deco.paint(g, RegionEnum.DECO, decoMood);
				if (smile != null) smile.paint(g, RegionEnum.SMILE, smileMood);
				if (leftEye != null) leftEye.paint(g, RegionEnum.LEFT_EYE, leftEyeMood);
				if (rightEye != null) rightEye.paint(g, RegionEnum.RIGHT_EYE, rightEyeMood);
			}
		});
		MouseAdapter mouseAdapter = new MouseAdapter() {
			private Point prev = null;
			
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
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		setVisible(true);
	}
	
	public void setScale(int scale){
	    this.scale = scale;
		setSize(RegionEnum.base * scale, RegionEnum.base * scale);
	}
	
	public int getScale(){
	    return scale;
	}
}
