package com.github.crafterchen2.logoanim.remote;

import com.github.crafterchen2.logoanim.ImmutableAssetProvider;
import com.github.crafterchen2.logoanim.ImmutableMoodProvider;
import com.github.crafterchen2.logoanim.LogoConfig;
import com.github.crafterchen2.logoanim.frames.LogoFrame;

import java.awt.*;
import java.io.IOException;

public class ClientTest {
	
	public static void main(String[] args) {
		ClientConnection clientConnection = new ClientConnection("localhost", NetworkingDetails.PORT, "Crafterchen3");
		new RemoteLogoFrame(clientConnection);

	}

	private static class RemoteLogoFrame extends LogoFrame {

		private final ClientConnection clientConnection;

		public RemoteLogoFrame(int scale, ImmutableAssetProvider defAssets, ImmutableMoodProvider defMoods, ClientConnection clientConnection) throws HeadlessException {
			super(scale, defAssets, defMoods);
			this.clientConnection = clientConnection;
		}

		public RemoteLogoFrame(ClientConnection clientConnection) throws HeadlessException {
			super();
			this.clientConnection = clientConnection;
		}

		@Override
		public void repaint() {
			super.repaint();
            try {
                clientConnection.sendString(new LogoConfig(getAsset(),getMood()).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
	}
	
}
