package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.components.ClientConnector;

import javax.swing.*;
import java.awt.*;

//Classes {
public class ClientConnectorFrame extends JFrame {
	
	//Fields {
	private final ClientConnector connector;
	//} Fields
	
	//Constructor {
	public ClientConnectorFrame(DisplayFrame logo) throws HeadlessException {
		super("Client Connector");
		connector = new ClientConnector(logo);
		setResizable(true);
		Dimension min = connector.getMinimumSize();
		setMinimumSize(new Dimension(min.width, min.height + 40));
		setSize(350, 110);
		setLocationRelativeTo(logo);
		setContentPane(connector);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DisplayFrame.loadFrameIcon(this, "remote_client");
		setVisible(true);
	}
	//} Constructor
	
	//Overrides {
	@Override
	public void dispose() {
		if (connector.isConnected()) {
			int result = JOptionPane.showConfirmDialog(this, "Closing this frame will disconnect from server. Proceed?", "Disconnect?", JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION) return;
			connector.setLogo(null);
		}
		super.dispose();
	}
	//} Overrides
}
//} Classes
