package com.github.crafterchen2.logoanim;

import com.github.crafterchen2.logoanim.components.LogoControl;

import javax.swing.*;

public class Main {
	
	public static void main(String[] args) {
		LogoControl controller = new LogoControl();
		LogoFrame logo = controller.getLogo();
		JFrame controlFrame = new JFrame("Logo Controller");
		controlFrame.setSize(300,600);
		controlFrame.setLocationRelativeTo(logo);
		controlFrame.setLocation(controlFrame.getX() + logo.getWidth() + 50, controlFrame.getY());
		controlFrame.add(controller);
		controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		controlFrame.setVisible(true);
	}
	
}