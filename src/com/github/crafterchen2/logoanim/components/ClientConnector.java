package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.LogoChangedListener;
import com.github.crafterchen2.logoanim.LogoConfig;
import com.github.crafterchen2.logoanim.frames.DisplayFrame;
import com.github.crafterchen2.logoanim.remote.ClientConnection;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.io.IOException;

public class ClientConnector extends JPanel implements LogoChangedListener {
	
	//Fields {
	public static final int MAX_ALIAS_LENGTH = 16;
	
	private final JLabel status = new JLabel();
	private final JTextField alias = new JTextField();
	private final JTextField ip = new JTextField();
	private final JButton button = new JButton();
	private DisplayFrame logo;
	private ClientConnection connection = null;
	private final Timer cooldown = new Timer(250, _ -> send());
	//} Fields
	
	//Constructor {
	public ClientConnector() {
		this(null);
	}
	
	public ClientConnector(DisplayFrame logo) {
		super(new BorderLayout());
		setLogo(logo);
		button.addActionListener(e -> toggleConnect());
		alias.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Alias"));
		alias.setMinimumSize(new Dimension(60,0));
		ip.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), "Ip:Port"));
		ip.setMinimumSize(new Dimension(60,0));
		JPanel north = new JPanel(new GridLayout(1,2));
		{
			north.add(status);
			north.add(button);
		}
		add(north, BorderLayout.NORTH);
		JSplitPane center = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		center.setDividerLocation(100);
		{
			center.setLeftComponent(alias);
			center.setRightComponent(ip);
		}
		add(center, BorderLayout.CENTER);
		cooldown.stop();
		cooldown.setRepeats(false);
	}
	//} Constructor
	
	//Methods {
	private void toggleConnect() {
		if (isConnected()) {
			disconnect();
		} else {
			connect();	
		}
	}
	
	public void disconnect() {
		if (!isConnected()) return;
		connection = null;
		updateStatus();
		updateButton();
	}
	
	public void connect() {
		if (isConnected()) return;
		try {
			//Regex only checks the following:
			// * String has 1 of ':'
			// * String has at least 1 digit behind ':'
			// * No other characters than digits between ':' and end of string
			//Everything else should be checked in parsePort(String port) for proper error handling.
			//IMPROVEME: Regex accepts also when multiple ":" are present, should only do that when those ":" are surrounded by "[" and "]".
			String[] parts = ip.getText().split(":(?=\\d+$)", 2);
			String a = alias.getText();
			if (a.isBlank()) {
				status.setText("Status: ❌ No alias");
				return;
			}
			if (a.length() > MAX_ALIAS_LENGTH) {
				a = a.substring(0, MAX_ALIAS_LENGTH);
				alias.setText(a);
			}
			String portStr = (parts.length > 1) ? parts[1] : null;
			int port = parsePort(portStr);
			connection = new ClientConnection(parts[0], port, a);
			send();
			updateStatus();
			updateButton();
		} catch (RuntimeException e) {
			Throwable t = e.getCause();
			if (t == null) {
				System.out.println("An unknown error occurred while trying to connect.");	
			} else {
				printDetailError(t);
			}
			connection = null;
		} catch (Exception e) {
			printDetailError(e);
			connection = null;
		}
	}
	
	private static void printDetailError(Throwable t) {
		System.out.println("Error while connecting: " + t.getClass().getName() + '[' + t.getMessage() + ']');
	}
	
	private int parsePort(String port) {
		try {
			int rv = Integer.parseInt(port);
			if (rv < 0 || rv > 65535) throw new IllegalArgumentException("port must be between 0 and 65535 (inclusive)");
			return rv;
		} catch (Exception e) {
			status.setText("Status: ❌ Invalid port");
			throw new RuntimeException(e);
		}
	}
	
	private void updateButton() {
		if (logo == null || !isConnected()) {
			button.setText("Connect");
		} else {
			button.setText("Disconnect");
		}
	}
	
	private void updateStatus() {
		if (logo == null) {
			status.setText("Status: ❌ No frame");
		} else {
			if (isConnected()) {
				status.setText("Status: ✔️ Connected");
			} else {
				status.setText("Status: ❌ Disconnected");
			}
		}
	}
	
	private void clearFields() {
		alias.setText("");
		ip.setText("");
	}
	//} Methods
	
	//Getter {
	public DisplayFrame getLogo() {
		return logo;
	}
	
	public boolean isConnected() {
		return connection != null;
	}
	//} Getter
	
	//Setter {
	public void setLogo(DisplayFrame logo) {
		if (this.logo != null) {
			this.logo.removeLogoChangedListener(this);
			disconnect();
		}
		this.logo = logo;
		updateStatus();
		updateButton();
		if (this.logo == null) {
			clearFields();
			setEnabled(false);
		} else {
			this.logo.addLogoChangedListener(this);
			setEnabled(true);
		}
	}
	//} Setter
	
	//Overrides {
	@Override
	public void setEnabled(boolean enabled) {
		button.setEnabled(enabled);
		ip.setEnabled(enabled);
		alias.setEnabled(enabled);
		super.setEnabled(enabled);
	}
	
	@Override
	public void logoChanged() {
		if (logo == null || !isConnected()) return;
		if (!cooldown.isRunning()) cooldown.start();
	}
	
	private void send() {
		try {
			connection.sendString(new LogoConfig(logo.getAsset(), logo.getMood()).toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	//} Overrides
}
