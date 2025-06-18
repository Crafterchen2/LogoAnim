package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.components.PresetLibrary;

import javax.swing.*;
import java.awt.*;

//Classes {
public class PresetLibraryFrame extends JFrame {
	
	//Constructor {
	public PresetLibraryFrame(DisplayFrame logo) throws HeadlessException {
		super("Preset Library");
		setSize(300, 600);
		setLocationRelativeTo(logo);
		setContentPane(new PresetLibrary(logo));
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DisplayFrame.loadFrameIcon(this, "preset");
		setVisible(true);
	}
	//} Constructor
	
}
//} Classes
