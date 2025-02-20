package com.github.crafterchen2.logoanim;

import com.github.crafterchen2.logoanim.components.LogoControl;

import javax.swing.*;

//Classes {
public class Main {
	
	//Methods {
	public static void main(String[] args) {
		if (args.length == 0) {
			LogoFrame l = new LogoFrame();
			l.setScale(20);
			l.leftEye = AssetEnum.Eye2x2;
			l.rightEye = AssetEnum.Eye2x2;
			l.smile = AssetEnum.NORMAL;
			l.leftEyeMood = MoodEnum.NORMAL;
			l.rightEyeMood = MoodEnum.NORMAL;
			l.smileMood = MoodEnum.NORMAL;
			l.repaint();
			return;
		}
		LogoControl controller = new LogoControl();
		LogoFrame logo = controller.getLogo();
		JFrame controlFrame = new JFrame("Logo Controller");
		controlFrame.setMinimumSize(controller.getPreferredSize());
		controlFrame.setSize(300, 600);
		controlFrame.setLocationRelativeTo(logo);
		controlFrame.setLocation(controlFrame.getX() + logo.getWidth() + 50, controlFrame.getY());
		controlFrame.setContentPane(controller);
		controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlFrame.setVisible(true);
	}
	//} Methods
	
}
//} Classes