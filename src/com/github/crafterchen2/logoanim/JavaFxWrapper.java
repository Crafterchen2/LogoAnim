package com.github.crafterchen2.logoanim;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javax.swing.*;

public class JavaFxWrapper {
	
	private final JFXPanel chatPanel;
	private final FxThread fx = new FxThread();
	
	private boolean chatVisible = false;
	
	private static JavaFxWrapper singleton = null;
	
	private JavaFxWrapper() {
		chatPanel = new JFXPanel();
		SwingUtilities.invokeLater(() -> Platform.runLater(fx));
		chatPanel.setVisible(false);
	}
	
	public static JavaFxWrapper getWrapper() {
		if (singleton == null) singleton = new JavaFxWrapper();
		return singleton;
	}
	
	public JComponent getChatPanel(){
		return chatPanel;
	}
	
	public boolean getChatVisible(){
	    return chatVisible;
	}
	
	public void setChatVisible(boolean chatVisible){
	    this.chatVisible = chatVisible;
		chatPanel.setVisible(this.chatVisible && fx.getVideoID() != null);
	}
	
	public void setVideoID(String id) {
		if (id == null) {
			chatPanel.setVisible(false);
		} else {
			fx.setVideoID(id);
			chatPanel.setVisible(chatVisible);
		}
	}
	
	public String getChatURL() {
		return fx.getChatURL();
	}
	
	public String getVideoID() {
		return fx.getVideoID();
	}
	
	private class FxThread extends Thread {
		
		private WebEngine engine = null;
		private String videoID = null;
		
		@Override
		public void run() {
			WebView view = new WebView();
			engine = view.getEngine();
			updateChatURL();
			engine.setUserStyleSheetLocation(getClass().getResource("/com/github/crafterchen2/logoanim/assets/chat.css").toExternalForm());
			chatPanel.setScene(new Scene(view));
		}
		
		private void updateChatURL() {
			if (videoID != null && engine != null) engine.load(getChatURL());
		}
		
		public String getChatURL() {
			return "https://www.youtube.com/live_chat?is_popout=1&v=" + getVideoID();
		}
		
		public String getVideoID() {
			return videoID;
		}
		
		public void setVideoID(String videoID) {
			this.videoID = videoID;
			updateChatURL();
		}
		
	}
	
}
