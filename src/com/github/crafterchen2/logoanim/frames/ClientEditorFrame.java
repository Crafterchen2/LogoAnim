package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.ImmutableAssetProvider;
import com.github.crafterchen2.logoanim.ImmutableMoodProvider;
import com.github.crafterchen2.logoanim.RegionEnum;
import com.github.crafterchen2.logoanim.components.*;
import com.github.crafterchen2.logoanim.layout.RatioLayout;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientEditorFrame extends DisplayFrame {
	
	public ClientEditorFrame(ImmutableAssetProvider defAssets, ImmutableMoodProvider defMoods) throws HeadlessException {
		super("Client Editor", defAssets, defMoods);
		setMinimumSize(new Dimension(900, 600));
		setSize(900, 600);
		setResizable(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadFrameIcon(this, "remote_client_editor");
		JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		{
			JSplitPane left = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
			{
				JPanel logoPanel = new JPanel(new RatioLayout(200,200));
				{
					logoPanel.add(display);
				}
				left.setTopComponent(logoPanel);
				left.setBottomComponent(new ClientConnector(this));
			}
			root.setLeftComponent(left);
			JSplitPane right = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
			{
				AtomicBoolean ignoreListener = new AtomicBoolean(false);
				PresetLibrary presetLibrary = new PresetLibrary(display, display);
				AssetSelector assetSelector = new AssetSelector(display);
				MoodSelector moodSelector = new MoodSelector();
				assetSelector.addAssetChangedListener(() -> {
					if (ignoreListener.get()) return;
					for (RegionEnum reg : RegionEnum.values()) {
						display.setAsset(reg, assetSelector.getAsset(reg));
					}
					display.repaint();
				});
				moodSelector.addMoodChangedListener(() -> {
					if (ignoreListener.get()) return;
					for (RegionEnum reg : RegionEnum.values()) {
						display.setMood(reg, moodSelector.getMood(reg));
					}
					assetSelector.repaint();
					display.repaint();
				});
				presetLibrary.addMoodChangedListener(() -> {
					ignoreListener.set(true);
					for (RegionEnum reg : RegionEnum.values()) {
						moodSelector.setMood(reg, display.getMood(reg));
						assetSelector.setAsset(reg, display.getAsset(reg));
					}
					ignoreListener.set(false);
					display.repaint();
				});
				presetLibrary.setBorder(BorderFactory.createLoweredBevelBorder());
				assetSelector.setBorder(BorderFactory.createLoweredBevelBorder());
				moodSelector.setBorder(BorderFactory.createLoweredBevelBorder());
				JPanel configurators = new JPanel(new BorderLayout(0, 5));
				{
					{
						blinkBox.setBorder(BorderFactory.createLoweredBevelBorder());
						blinkBox.setBorderPainted(true);
						blinkBox.addActionListener(_ -> {
							if (blinkBox.isSelected()) {
								blinkTimer.restart();
							} else {
								blinkTimer.stop();
								display.blink = false;
								display.repaint();
							}
						});
						blinkBox.doClick();
					}
					configurators.add(blinkBox, BorderLayout.NORTH);
					JPanel selectors = new JPanel(new GridLayout(0,1, 0, 5));
					{
						selectors.add(moodSelector);
						selectors.add(assetSelector);
					}
					configurators.add(selectors);
				}
				right.setLeftComponent(configurators);
				right.setRightComponent(presetLibrary);
			}
			root.setRightComponent(right);
		}
		setContentPane(root);
	}
	
	public ClientEditorFrame() {
		this(null, null);
	}
	
	@Override
	public int getScale() {
		return 1;
	}
	
	@Override
	public int getMaxScale() {
		return 1;
	}
	
	@Override
	public int getMinScale() {
		return 1;
	}
	
	@Override
	public void setScale(int scale) {
		
	}
	
	@Override
	protected void applyMouseAdapter() {
		
	}
	
	@Override
	public void rebuildMenu() {
		
	}
	
	@Override
	protected DisplayMouseAdapter makeMouseAdapter() {
		return null;
	}
	
	@Override
	public boolean isUndecorated() {
		return false;
	}
}
