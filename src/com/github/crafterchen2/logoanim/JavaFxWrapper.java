package com.github.crafterchen2.logoanim;

import com.github.crafterchen2.logoanim.frames.DisplayFrame;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

//Classes {
public class JavaFxWrapper {
	
	//Fields {	
	private static ChatControl chatControl = null;
	private static JavaFxWrapper singleton = null;
	private final JFXPanel chatPanel;
	private final FxThread fx = new FxThread();
	private boolean chatVisible = false;
	//} Fields
	
	//Constructor {
	private JavaFxWrapper() {
		chatPanel = new JFXPanel();
		SwingUtilities.invokeLater(() -> Platform.runLater(fx));
		chatPanel.setVisible(false);
	}
	//} Constructor
	
	//Getter {
	public static JavaFxWrapper getWrapper() {
		if (singleton == null) singleton = new JavaFxWrapper();
		return singleton;
	}
	
	public JPanel getChatControlPanel() {
		if (chatControl == null) chatControl = new ChatControl();
		return chatControl;
	} 
	
	public JComponent getChatPanel() {
		return chatPanel;
	}
	
	public boolean getChatVisible() {
		return chatVisible && getVideoID() != null;
	}
	
	public String getChatURL() {
		return fx.getChatURL();
	}
	
	public String getVideoID() {
		return fx.getVideoID();
	}
	//} Getter
	
	//Setter {
	public void setChatVisible(boolean chatVisible) {
		this.chatVisible = chatVisible;
		chatPanel.setVisible(this.chatVisible && fx.getVideoID() != null);
	}
	
	public void setVideoID(String id) {
		Platform.runLater(() -> {
			if (id == null) {
				chatPanel.setVisible(false);
			} else {
				fx.setVideoID(id);
				chatPanel.setVisible(chatVisible);
			}
		});
	}
	
	public void paintChatPanel(Graphics g) {
		if (chatVisible) chatPanel.paint(g);
	}
	//} Setter
	
	//Classes {
	private static class ChatControl extends JPanel {
		
		//Constructor {
		private ChatControl() throws HeadlessException {
			super(new BorderLayout());
			JavaFxWrapper wrapper = getWrapper();
			JTextField input = new JTextField(wrapper.getVideoID());
			JPanel buttons = new JPanel(new GridLayout(1, 0));
			{
				JButton active = new JButton(wrapper.getChatVisible() ? "Hide" : "Show");
				JButton refresh = new JButton("Refresh");
				JButton update = new JButton("Update");
				active.setEnabled(wrapper.getVideoID() != null);
				active.addActionListener(_ -> {
					boolean a = !wrapper.getChatVisible();
					active.setText(a ? "Hide" : "Show");
					wrapper.setChatVisible(a);
				});
				refresh.addActionListener(_ -> {
					String id = wrapper.getVideoID();
					input.setText((id != null) ? id : "");
					active.setEnabled(wrapper.getVideoID() != null);
					active.setText(wrapper.getChatVisible() ? "Hide " : "Show");
				});
				update.addActionListener(_ -> {
					String text = input.getText();
					if (!text.matches(".*[A-Za-z0-9_-]{11}$")) return;
					text = text.replaceFirst(".*(?=[A-Za-z0-9_-]{11}$)", "");
					wrapper.setVideoID(text);
					wrapper.setChatVisible(true);
					active.setText("Hide");
					active.setEnabled(true);
				});
				buttons.add(active);
				buttons.add(refresh);
				buttons.add(update);
			}
			add(buttons, BorderLayout.NORTH);
			add(input, BorderLayout.CENTER);
		}
		//} Constructor
	}
	
	private class FxThread extends Thread {
		
		//Fields {
		private WebEngine engine = null;
		private String videoID = null;
		//} Fields
		
		//Methods {
		private void updateChatURL() {
			if (videoID != null && engine != null) engine.load(getChatURL());
		}
		//} Methods
		
		//Getter {
		public String getChatURL() {
			return "https://www.youtube.com/live_chat?is_popout=1&v=" + getVideoID();
		}
		
		public String getVideoID() {
			return videoID;
		}
		//} Getter
		
		//Setter {
		public void setVideoID(String videoID) {
			this.videoID = videoID;
			updateChatURL();
		}
		//} Setter
		
		//Overrides {
		@Override
		public void run() {
			WebView view = new WebView();
			engine = view.getEngine();
			updateChatURL();
			URL resource = getClass().getResource("/com/github/crafterchen2/logoanim/assets/chat.css");
			if (resource != null) engine.setUserStyleSheetLocation(resource.toExternalForm());
			chatPanel.setScene(new Scene(view));
		}
		//} Overrides
		
	}
	//} Classes
	
}
//} Classes
