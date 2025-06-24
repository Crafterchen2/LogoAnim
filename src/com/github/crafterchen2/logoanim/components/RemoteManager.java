package com.github.crafterchen2.logoanim.components;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.frames.StreamFrame;
import com.github.crafterchen2.logoanim.layout.VerticalListLayout;
import com.github.crafterchen2.logoanim.remote.NetworkingDetails;
import com.github.crafterchen2.logoanim.remote.ServerConnection;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Classes {
public class RemoteManager extends JPanel implements RemoteConnectionListener {
	
	//Fields {
	private static final HashMap<ServerConnection.SocketInfo, SeatEntry> seats = HashMap.newHashMap(1);
	private static final ArrayList<RemoteConnectionListener> listeners = new ArrayList<>();
	private static final int size = 40;
	private static ServerConnection connection = null;
	static {
		NetworkingDetails.addToWhitelist("/127.0.0.1");
		NetworkingDetails.addToWhitelist("/0:0:0:0:0:0:0:1");
		NetworkingDetails.addToWhitelist("/192.168.178.103");
	}
	private final JLabel portLabel = new JLabel();
	private final JLabel ipLabel = new JLabel();
	private final JPanel seatPanel = new JPanel(new VerticalListLayout());
	private final StreamFrame streamFrame;
	//} Fields
	
	//Constructor {
	public RemoteManager() {
		this(null);
	}
	
	public RemoteManager(StreamFrame streamFrame) throws HeadlessException {
		super(new BorderLayout());
		this.streamFrame = streamFrame;
		addRemoteConnectionListener(this);
		ipLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(0xA6A6A6)));
		portLabel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(0xFFFFFF)));
		Insets buttonInsets = new Insets(5, 1, 5, 1);
		JButton kickAll = new JButton("Kick all");
		JButton closeConnection = new JButton("Close connection");
		JButton copyIpPort = new JButton("⎘");
		kickAll.addActionListener(_ -> closeAllSeats());
		kickAll.setMargin(buttonInsets);
		closeConnection.setMargin(buttonInsets);
		closeConnection.addActionListener(_ -> closeConnection());
		closeConnection.setEnabled(false);
		copyIpPort.addActionListener(e -> copyIpPort((e.getModifiers() & InputEvent.SHIFT_MASK) != 0)); //BUG: InputEvent.SHIFT_MASK is deprecated
		copyIpPort.setMargin(buttonInsets);
		copyIpPort.setFont(copyIpPort.getFont().deriveFont(24f));
		JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 3, 0));
		{
			header.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
			header.add(closeConnection);
			header.add(kickAll);
			JPanel info = new JPanel(new GridLayout(2, 1));
			{
				info.add(ipLabel);
				info.add(portLabel);
			}
			header.add(info);
			header.add(copyIpPort);
		}
		add(header, BorderLayout.NORTH);
		add(seatPanel, BorderLayout.CENTER);
		openConnection();
	}
	//} Constructor
	
	//Methods {
	public static void addRemoteConnectionListener(RemoteConnectionListener listener) {
		listeners.add(listener);
	}
	
	public static void removeRemoteConnectionListener(RemoteConnectionListener listener) {
		listeners.remove(listener);
	}
	
	private static void notifyAboutList() {
		listeners.forEach(RemoteConnectionListener::listChanged);
	}
	
	private static void notifyAboutConnection() {
		listeners.forEach(RemoteConnectionListener::connectionChanged);
	}
	
	private void copyIpPort(boolean useLocalHost) {
		if (useLocalHost) {
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection("localhost:" + connection.getLocalPort()), null);
		}
		//TODO: Copy ip:port
	}
	
	public void openConnection() {
		if (connection != null) return;
		//TODO: Can we figure out the health of the connection? INVESTIGATE!
		RemoteManager me = this;
		try {
			connection = new ServerConnection(new ServerConnection.RequestHandler() {
				//Overrides {
				@Override
				public String handleMessage(ServerConnection.SocketInfo socket, String msg) {
					ServerConnection.SocketInfo reduced = new ServerConnection.SocketInfo(socket.ip(), -1, socket.alias());
					openSeat(reduced, Parser.parseLogo(msg, Parser.DEFAULT));
					return "Received: " + msg;
				}
				
				@Override
				public void handleRefuse(ServerConnection.SocketInfo socket, ServerConnection.RefuseReason reason) {
					JOptionPane.showMessageDialog(me, "A Client could not connect.\nReason: " + reason.name(), "Connection Failed", JOptionPane.ERROR_MESSAGE);
				}
				//} Overrides
			});
			connection.start();
			notifyAboutConnection();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void closeConnection() {
		if (connection == null) return;
		closeAllSeats();
		//TODO: Safely close the connection. 
		notifyAboutConnection();
	}
	
	public void openSeat(ServerConnection.SocketInfo reduced, LogoConfig initial) {
		SeatEntry entry;
		if ((entry = seats.get(reduced)) == null) {
			entry = new SeatEntry(reduced);
			seats.put(reduced, entry);
		}
		if (initial != null) {
			entry.setAsset(initial);
			entry.setMood(initial);
		}
		notifyAboutList();
	}
	
	public void closeAllSeats() {
		seats.clear();
		seatPanel.removeAll();
		notifyAboutList();
	}
	
	public void closeSeat(ServerConnection.SocketInfo info) {
		SeatEntry seat = seats.remove(info);
		if (seat != null) {
			seatPanel.remove(seat);
			streamFrame.removeLogo(seat.display);
		}
		notifyAboutList();
	}
	
	private void closeSeats(List<SeatEntry> seatsToClose) {
		for (SeatEntry seat : seatsToClose) {
			seat = seats.remove(seat.info);
			if (seat != null) {
				seatPanel.remove(seat);
				streamFrame.removeLogo(seat.display);
			}
		}
		notifyAboutList();
	}
	//} Methods
	
	//Getter {
	public String getIpPort() {
		//BUG: ipLabel does not only contain the ip, retrieve the ip from somewhere else.
		return ipLabel.getText() + ":" + connection.getLocalPort();
	}
	//} Getter
	
	//Overrides {
	@Override
	public void connectionChanged() {
		//TODO: Do I need to expose the Port? INVESTIGATE!
		if (connection == null) {
			setEnabled(false);
			//TODO: Should this always happen? INVESTIGATE!
			if (!seats.isEmpty()) {
				seats.clear();
				notifyAboutList();
			}
		} else {
			setEnabled(true);
		}
		ipLabel.setText("Ip: "); //TODO: Find Server Ip
		portLabel.setText("Port: " + connection.getLocalPort());
	}
	
	@Override
	public void listChanged() {
		System.out.println("changed!");
		final ArrayList<SeatEntry> seatsToClose = new ArrayList<>();
		seats.forEach((_, seatEntry) -> {
			boolean success = streamFrame.tryAddLogo(seatEntry.getName(), seatEntry.display);
			if (success) {
				seatPanel.add(seatEntry);
			} else {
				System.out.println(seatEntry.hashCode() + " had an illegal name (" + seatEntry.getName() + "), closing...");
				seatsToClose.add(seatEntry);
			}
		});
		seatPanel.updateUI();
		streamFrame.updateLogoPanel();
		if (!seatsToClose.isEmpty()) closeSeats(seatsToClose);
	}
	//} Overrides
	
	//Classes {
	private class SeatEntry extends JComponent implements AssetProvider, MoodProvider {
		
		//Fields {
		private final LogoDisplay display = new LogoDisplay();
		private final ServerConnection.SocketInfo info;
		//} Fields
		
		//Constructor {
		public SeatEntry(ServerConnection.SocketInfo info) {
			this.info = info;
			setLayout(new FlowLayout(FlowLayout.LEFT, 4, 4));
			setName(info.alias());
			//setLayout(null);
			//setSize(new Dimension(size * 6, size));
			JButton terminate = new JButton("❌");
			JLabel alias = new JLabel();
			JLabel ip = new JLabel();
			terminate.setMargin(new Insets(0, 0, 0, 0));
			terminate.addActionListener(_ -> closeSeat(info));
			terminate.setPreferredSize(new Dimension(size, size));
			//terminate.setLocation(0, 0);
			display.setPreferredSize(new Dimension(size, size));
			//display.setLocation(size, 0);
			alias.setText(info.alias());
			alias.setPreferredSize(new Dimension(size * 2, size));
			//alias.setLocation(size * 2, 0);
			ip.setText(info.ip());
			ip.setPreferredSize(new Dimension(size * 2, size));
			//ip.setLocation(size * 4, 0);
			add(terminate);
			add(display);
			add(alias);
			//add(new JSeparator(JSeparator.VERTICAL));
			add(ip);
		}
		//} Constructor
		
		//Overrides {
		@Override
		public void setAsset(RegionEnum reg, AssetData asset) {
			display.setAsset(reg, asset);
		}
		
		@Override
		public AssetData getAsset(RegionEnum reg) {
			return display.getAsset(reg);
		}
		
		@Override
		public void setMood(RegionEnum reg, MoodData mood) {
			display.setMood(reg, mood);
		}
		
		@Override
		public MoodData getMood(RegionEnum reg) {
			return display.getMood(reg);
		}
		//} Overrides
	}
	//} Classes
}
//} Classes
