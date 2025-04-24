package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.LogoChangedListener;
import com.github.crafterchen2.logoanim.LogoConfig;
import com.github.crafterchen2.logoanim.frames.DisplayFrame;
import com.github.crafterchen2.logoanim.remote.ClientConnection;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ClientConnector extends JPanel implements LogoChangedListener {
	
	//Fields {
	private final JLabel status = new JLabel();
	private final JTextField alias = new JTextField();
	private final JTextField ip = new JTextField();
	private final JButton button = new JButton();
	private DisplayFrame logo;
	private ClientConnection connection = null;
	private final Timer cooldown = new Timer(250, e -> send());
	//} Fields
	
	//Constructor {
	public ClientConnector() {
		this(null);
	}
	
	public ClientConnector(DisplayFrame logo) {
		super(new GridLayout(2,2));
		setLogo(logo);
		button.addActionListener(e -> toggleConnect());
		add(status);
		add(button);
		add(alias);
		add(ip);
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
		connection = null;
		updateStatus();
		updateButton();
	}
	
	public boolean connect() {
		if (isConnected()) return false;
		try {
			String[] parts = ip.getText().split(":", 2);
			String a = alias.getText();
			a = a.substring(0, Math.min(24, a.length()));
			connection = new ClientConnection(parts[0], Integer.parseInt(parts[1]), a);
			send();
			updateStatus();
			updateButton();
			return true;
		} catch (RuntimeException e) {
			Throwable t = e.getCause();
			if (t == null) {
				System.out.println("An unknown error occurred while trying to connect.");	
			} else {
				System.out.println("Error while connecting: " + t.getMessage());
			}
			connection = null;
			return false;
		} catch (Exception e) {
			System.out.println("Error while connecting: " + e.getMessage());
			connection = null;
			return false;
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
			status.setText("Status: ❌ No frame.");
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
