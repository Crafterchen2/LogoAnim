package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.components.LogoControl;

import javax.swing.*;
import java.awt.*;

public class LogoControlFrame extends JFrame {
	
	public LogoControlFrame(LogoFrame logo) throws HeadlessException {
		super("Logo Controller");
		LogoControl controller = new LogoControl(logo);
		setMinimumSize(controller.getPreferredSize());
		setSize(300, 600);
		setLocationRelativeTo(logo);
		setContentPane(controller);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
