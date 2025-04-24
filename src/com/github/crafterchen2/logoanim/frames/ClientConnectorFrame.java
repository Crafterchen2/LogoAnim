package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.components.ClientConnector;

import javax.swing.*;
import java.awt.*;

public class ClientConnectorFrame extends JFrame {
	
	private final ClientConnector connector;
	
	public ClientConnectorFrame(DisplayFrame logo) throws HeadlessException {
		super("Client Connector");
		connector = new ClientConnector(logo);
		setResizable(false);
		setSize(300,90);
		setLocationRelativeTo(logo);
		setContentPane(connector);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		DisplayFrame.loadFrameIcon(this, "remote_client_frame_icon");
		setVisible(true);
	}
	
	@Override
	public void dispose() {
		if (connector.isConnected()) {
			int result = JOptionPane.showConfirmDialog(this, "Closing this frame will disconnect from server. Proceed?", "Disconnect?",JOptionPane.YES_NO_OPTION);
			if (result != JOptionPane.YES_OPTION) return;
			connector.setLogo(null);
		}
		super.dispose();
	}
}
