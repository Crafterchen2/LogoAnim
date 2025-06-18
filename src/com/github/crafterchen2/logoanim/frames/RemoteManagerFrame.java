package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.components.RemoteManager;

import javax.swing.*;
import java.awt.*;

//Classes {
public class RemoteManagerFrame extends JFrame {
	
	//Constructor {
	public RemoteManagerFrame(StreamFrame logo) throws HeadlessException {
		super("Remote Manager");
		RemoteManager manager = new RemoteManager(logo);
		setMinimumSize(manager.getPreferredSize());
		setSize(300, 600);
		setResizable(false);
		setLocationRelativeTo(logo);
		setContentPane(manager);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DisplayFrame.loadFrameIcon(this, "remote_manager");
		setVisible(true);
	}
	//} Constructor
}
//} Classes
