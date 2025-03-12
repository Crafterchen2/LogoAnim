package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.components.LogoControl;

import javax.swing.*;
import java.awt.*;

//Classes {
public class LogoControlFrame extends JFrame {
	
	//Constructor {
	public LogoControlFrame(LogoFrame logo) throws HeadlessException {
		super("Logo Controller");
		LogoControl controller = new LogoControl(logo);
		setMinimumSize(controller.getPreferredSize());
		setSize(300, 600);
		setLocationRelativeTo(logo);
		setContentPane(controller);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		LogoFrame.loadFrameIcon(this, "control_frame_icon");
		setVisible(true);
	}
	//} Constructor
}
//} Classes
