package com.github.crafterchen2.logoanim.frames;

import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.components.AssetSelector;
import com.github.crafterchen2.logoanim.components.MoodSelector;
import com.github.crafterchen2.logoanim.components.PresetLibrary;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.GDI32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.ByteBuffer;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_AAC;
import static org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_H264;
import static org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_ARGB;

//Classes {
public class StreamFrame extends JFrame implements AssetProvider, MoodProvider {
	
	//Fields {
	public static final int STREAM_RES_WIDTH = 1920;
	public static final int STREAM_RES_HEIGHT = 1080;
	public static final int CAPTURE_RES_WIDTH = 3840;//2560;
	public static final int CAPTURE_RES_HEIGHT = 2160;//1440;
	public static final int PREVIEW_FPS = 60;
	public static final int REC_FPS = 60;
	public static final int SAMPLE_RATE = 48_000;
	public static final int AUDIO_CHANNELS = 2;
	public static final int AUDIO_BITRATE = 160_000;
	public static final int VIDEO_BITRATE = 51000_000;
	public static final int VIDEO_CODEC = AV_CODEC_ID_H264;
	public static final int AUDIO_CODEC = AV_CODEC_ID_AAC;
	public static final long REC_MILLIS;
	public static final long PREVIEW_MILLIS;
	public static final int PREVIEW_MIN_WIDTH;
	public static final int PREVIEW_MIN_HEIGHT;
	public static final int SCALE;
	public static final int CAP_X;
	public static final int CAP_Y;
	public static final int CAP_W;
	public static final int CAP_H;
	public static final int CHAT_X;
	public static final int CHAT_Y;
	public static final int CHAT_W;
	public static final int CHAT_H;
	public static final int LOGO_X;
	public static final int LOGO_Y;
	public static final int LOGO_W;
	public static final int LOGO_H;
	public static final int INFO_X;
	public static final int INFO_Y;
	public static final int INFO_W;
	public static final int INFO_H;
	
	private static final BufferedImage bgImg;
	private static final String STOP_BUTTON = "stopButton";
	private static final String STOP_PROG = "stopProg";
	//Constructor {
	static {
		final int capX = 4;
		final int capY = 18;
		final int capW = 288;
		final int capH = 162;
		final int chatX = 300;
		final int chatY = 4;
		final int chatW = 80;
		final int chatH = 124;
		final int logoX = 300;
		final int logoY = 132;
		final int logoW = 80;
		final int logoH = 80;
		final int infoX = 168;
		final int infoY = 188;
		final int infoW = 128;
		final int infoH = 24;
		{
			BufferedImage temp;
			try {
				temp = ImageIO.read(Objects.requireNonNull(StreamFrame.class.getResourceAsStream("/com/github/crafterchen2/logoanim/assets/stream_bg.png")));
				Objects.requireNonNull(temp);
			} catch (Exception e) {
				final int w = 384;
				final int h = 216;
				temp = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				{
					Graphics g = temp.getGraphics();
					g.setColor(new Color(0xBAC0C7));
					g.fillRect(0, 0, w, h);
					g.setColor(new Color(0x9C0000));
					g.fillRect(capX - 1, capY - 1, capW + 2, capH + 2);
					g.setColor(new Color(0x9C9C00));
					g.fillRect(capX, capY, capW, capH);
					g.setColor(new Color(0x009C00));
					g.fillRect(chatX - 1, chatY - 1, chatW + 2, chatH + 2);
					g.setColor(new Color(0x009C9C));
					g.fillRect(chatX, chatY, chatW, chatH);
					g.setColor(new Color(0x00009C));
					g.fillRect(logoX - 1, logoY - 1, logoW + 2, logoH + 2);
					g.setColor(new Color(0x9C009C));
					g.fillRect(logoX, logoY, logoW, logoH);
					g.setColor(new Color(0x636363));
					g.fillRect(infoX - 1, infoY - 1, infoW + 2, infoH + 2);
					g.setColor(new Color(0xCCCCCC));
					g.fillRect(infoX, infoY, infoW, infoH);
					g.setColor(Color.RED);
					g.drawString("Error: Could not find", 1, 195);
					g.drawString("background image.", 34, 210);
					g.dispose();
				}
			}
			PREVIEW_MIN_WIDTH = temp.getWidth();
			PREVIEW_MIN_HEIGHT = temp.getHeight();
			bgImg = new BufferedImage(STREAM_RES_WIDTH, STREAM_RES_HEIGHT, BufferedImage.TYPE_INT_RGB);
			{
				Graphics g = bgImg.getGraphics();
				g.drawImage(temp, 0, 0, STREAM_RES_WIDTH, STREAM_RES_HEIGHT, null);
				g.dispose();
			}
		}
		SCALE = STREAM_RES_WIDTH / PREVIEW_MIN_WIDTH;
		CAP_X = SCALE * capX;
		CAP_Y = SCALE * capY;
		CAP_W = SCALE * capW;
		CAP_H = SCALE * capH;
		CHAT_X = SCALE * chatX;
		CHAT_Y = SCALE * chatY;
		CHAT_W = SCALE * chatW;
		CHAT_H = SCALE * chatH;
		LOGO_X = SCALE * logoX;
		LOGO_Y = SCALE * logoY;
		LOGO_W = SCALE * logoW;
		LOGO_H = SCALE * logoH;
		INFO_X = SCALE * infoX;
		INFO_Y = SCALE * infoY;
		INFO_W = SCALE * infoW;
		INFO_H = SCALE * infoH;
		PREVIEW_MILLIS = (long) ((1.0 / (double) PREVIEW_FPS) * 1000.0);
		REC_MILLIS = (long) ((1.0 / (double) REC_FPS) * 1000.0);
	}
	//} Constructor
	
	public final CardLayout stopCards = new CardLayout();
	private final LogoPainter display;
	private final BufferedImage streamCanvas = new BufferedImage(STREAM_RES_WIDTH, STREAM_RES_HEIGHT, BufferedImage.TYPE_INT_RGB);
	private final JPanel preview = makePreviewPanel();
	private final JLabel fpsLabel = new JLabel("frameDelta: -");
	private final JButton starter = new JButton("Start");
	private final JPanel stopPanel = new JPanel(stopCards);
	private FFmpegFrameRecorder screenRecorder;
	private Thread streamThread;
	private boolean repaintLogo = true;
	private boolean record = false;
	private boolean stopping = false;
	private WinDef.HDC hdcWindow;
	private WinDef.HDC hdcMemDC;
	private WinDef.HBITMAP hBitmap;
	//} Fields
	
	//Constructor {
	public StreamFrame() throws HeadlessException {
		this(null, null);
	}
	
	public StreamFrame(AssetProvider defAssets, MoodProvider defMoods) throws HeadlessException {
		super("Stream Manager");
		int initFrameWidth = 1600;
		int initFrameHeight = 900;
		setSize(initFrameWidth, initFrameHeight);
		setLocationByPlatform(true);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		display = new LogoPainter(defAssets, defMoods);
		final Timer blinkTimer = new Timer(
				5000, _ -> {
			display.blink = true;
			repaintLogo = true;
			Timer minor = new Timer(
					300, _ -> {
				display.blink = false;
				repaintLogo = true;
			}
			);
			minor.setRepeats(false);
			minor.start();
		}
		);
		blinkTimer.setRepeats(true);
		blinkTimer.start();
		JSplitPane root = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true);
		{
			root.setDividerLocation(initFrameWidth - 300);
			JSplitPane minor = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
			{
				minor.setDividerLocation(initFrameHeight - 300);
				JPanel ratioCenter = new JPanel(new RatioLayout(PREVIEW_MIN_WIDTH, PREVIEW_MIN_HEIGHT));
				{
					ratioCenter.add(preview);
				}
				minor.setTopComponent(ratioCenter);
				minor.setBottomComponent(fpsLabel);
			}
			root.setLeftComponent(minor);
			JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.WRAP_TAB_LAYOUT);
			{
				JPanel lc = new JPanel(new BorderLayout(0, 5));
				{
					JCheckBox blinkBox = new JCheckBox("Enable blinking");
					{
						blinkBox.setSelected(true);
						blinkBox.setBorder(BorderFactory.createLoweredBevelBorder());
						blinkBox.setBorderPainted(true);
						blinkBox.addActionListener(_ -> {
							if (blinkBox.isSelected()) {
								blinkTimer.restart();
							} else {
								blinkTimer.stop();
								display.blink = false;
								repaintLogo = true;
							}
						});
					}
					lc.add(blinkBox, BorderLayout.NORTH);
					JPanel big = new JPanel(new GridLayout(3, 1, 0, 5));
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
							repaintLogo = true;
						});
						moodSelector.addMoodChangedListener(() -> {
							if (ignoreListener.get()) return;
							for (RegionEnum reg : RegionEnum.values()) {
								display.setMood(reg, moodSelector.getMood(reg));
							}
							assetSelector.repaint();
							repaintLogo = true;
						});
						presetLibrary.addMoodChangedListener(() -> {
							ignoreListener.set(true);
							for (RegionEnum reg : RegionEnum.values()) {
								moodSelector.setMood(reg, display.getMood(reg));
								assetSelector.setAsset(reg, display.getAsset(reg));
							}
							ignoreListener.set(false);
							repaintLogo = true;
						});
						presetLibrary.setBorder(BorderFactory.createLoweredBevelBorder());
						assetSelector.setBorder(BorderFactory.createLoweredBevelBorder());
						moodSelector.setBorder(BorderFactory.createLoweredBevelBorder());
						big.add(presetLibrary);
						big.add(assetSelector);
						big.add(moodSelector);
					}
					lc.add(big, BorderLayout.CENTER);
				}
				tabs.addTab("Logo", lc);
				JPanel sc = new JPanel(new BorderLayout(0, 5));
				{
					JPanel chatControlPanel = JavaFxWrapper.getWrapper().getChatControlPanel();
					chatControlPanel.setBorder(BorderFactory.createLoweredBevelBorder());
					sc.add(chatControlPanel, BorderLayout.NORTH);
					starter.addActionListener(_ -> startRecording());
					sc.add(starter, BorderLayout.CENTER);
					{
						JButton stopper = new JButton("Stop");
						stopper.addActionListener(_ -> stopRecording());
						stopPanel.add(stopper, STOP_BUTTON);
						JProgressBar progressBar = new JProgressBar();
						progressBar.setIndeterminate(true);
						stopPanel.add(progressBar, STOP_PROG);
					}
					sc.add(stopPanel, BorderLayout.SOUTH);
				}
				tabs.addTab("Stream", sc);
				tabs.addTab("Remote", new JButton("Remote Control"));
			}
			root.setRightComponent(tabs);
		}
		setContentPane(root);
		DisplayFrame.loadFrameIcon(this, "streaming_frame_icon");
		setVisible(true);
		JavaFxWrapper wrapper = JavaFxWrapper.getWrapper();
		wrapper.getChatPanel().setBounds(CHAT_X, CHAT_Y, CHAT_W, CHAT_H);
		Graphics g = streamCanvas.getGraphics();
		g.drawImage(bgImg, 0, 0, null);
		Thread previewLoop = new Thread(
				() -> {
					boolean fine = true;
					while (fine) {
						preview.repaint();
						try {
							Thread.sleep(PREVIEW_MILLIS);
						} catch (InterruptedException _) {
							fine = false;
						}
					}
				}, "previewLoop"
		);
		previewLoop.start();
	}
	//} Constructor
	
	//Methods {	
	private void startRecording() {
		if (record && !stopping) return;
		starter.setEnabled(false);
		record = true;
		final boolean[] fine = {true};
		try {
			screenRecorder = FFmpegFrameRecorder.createDefault(System.currentTimeMillis() + ".avi", STREAM_RES_WIDTH, STREAM_RES_HEIGHT);
			//screenRecorder.setFrameRate(REC_FPS);
			//screenRecorder.setAudioChannels(AUDIO_CHANNELS);
			//screenRecorder.setAudioBitrate(AUDIO_BITRATE);
			//screenRecorder.setAudioCodec(AUDIO_CODEC);
			screenRecorder.setVideoBitrate(VIDEO_BITRATE);
			screenRecorder.setVideoCodec(VIDEO_CODEC);
			//screenRecorder.setSampleRate(SAMPLE_RATE);
			screenRecorder.setVideoCodecName("h264_nvenc");
			
			// Set NVENC-specific options to improve performance
			//screenRecorder.setOption("preset", "p1"); // Use p1 (highest quality) instead of p7 to increase GPU usage
			//screenRecorder.setOption("tune", "ll"); // Low latency tuning
			//screenRecorder.setOption("rc", "vbr_hq"); // Use high quality VBR mode for better quality and higher GPU usage
			//screenRecorder.setOption("zerolatency", "1"); // Minimize latency
			//screenRecorder.setOption("b_ref_mode", "0"); // Disable B-frame references for lower latency
			//screenRecorder.setOption("surfaces", "8"); // Increased from 4 to 8 for more parallel processing
			//screenRecorder.setOption("gpu", "0"); // Use first GPU (change if multiple GPUs available)
			//screenRecorder.setOption("no-scenecut", "1"); // Disable scene cut detection for better performance
			//screenRecorder.setOption("spatial-aq", "1"); // Enable spatial adaptive quantization for better quality
			//screenRecorder.setOption("temporal-aq", "1"); // Enable temporal adaptive quantization
			//screenRecorder.setOption("aq-strength", "15"); // Set adaptive quantization strength (1-15, higher values use more GPU)
			//screenRecorder.setOption("multipass", "fullres"); // Use full resolution multipass encoding (uses more GPU)
			//screenRecorder.setOption("forced-idr", "1"); // Force IDR frames for better seeking
			//screenRecorder.setOption("qp", "15"); // Lower QP value further (from 19 to 15) for better quality and higher GPU usage
			//screenRecorder.setOption("cbr", "0"); // Disable constant bitrate mode
			//screenRecorder.setOption("2pass", "1"); // Enable two-pass encoding for better quality
			//screenRecorder.setOption("bf", "0"); // Disable B-frames to reduce latency and increase GPU usage
			streamThread = new Thread(
					() -> {
						try (Java2DFrameConverter converter = new Java2DFrameConverter()) {
							long sleepTime;
							Graphics g = streamCanvas.getGraphics();
							Graphics capGraphics = g.create(CAP_X, CAP_Y, CAP_W, CAP_H);
							Graphics chatGraphics = g.create(CHAT_X, CHAT_Y, CHAT_W, CHAT_H);
							Graphics logoGraphics = g.create(LOGO_X, LOGO_Y, LOGO_W, LOGO_H);
							g.drawImage(bgImg, 0, 0, null);
							JavaFxWrapper wrapper = JavaFxWrapper.getWrapper();
							wrapper.getChatPanel().setBounds(CHAT_X, CHAT_Y, CHAT_W, CHAT_H);
							hdcWindow = User32.INSTANCE.GetDC(null);
							hdcMemDC = GDI32.INSTANCE.CreateCompatibleDC(hdcWindow);
							hBitmap = GDI32.INSTANCE.CreateCompatibleBitmap(hdcWindow, CAPTURE_RES_WIDTH, CAPTURE_RES_HEIGHT);
							GDI32.INSTANCE.SelectObject(hdcMemDC, hBitmap);
							WinGDI.BITMAPINFO bmi = new WinGDI.BITMAPINFO();
							bmi.bmiHeader.biSize = bmi.size();
							bmi.bmiHeader.biWidth = CAPTURE_RES_WIDTH;
							bmi.bmiHeader.biHeight = -CAPTURE_RES_HEIGHT; // Negative = top-down
							bmi.bmiHeader.biPlanes = 1;
							bmi.bmiHeader.biBitCount = 32;
							bmi.bmiHeader.biCompression = WinGDI.BI_RGB;
							Pointer buffer = new Memory((long) CAPTURE_RES_WIDTH * CAPTURE_RES_HEIGHT * 4);
							final int fw = Math.min(CAPTURE_RES_WIDTH / CAP_W, 1);
							final int fh = Math.min(CAPTURE_RES_HEIGHT / CAP_H, 1);
							final int capW = CAPTURE_RES_WIDTH / fw;
							final int capH = CAPTURE_RES_HEIGHT / fh;
							BufferedImage grabbedImage = new BufferedImage(capW, capH, BufferedImage.TYPE_INT_RGB);
							int[] pixels = ((DataBufferInt) grabbedImage.getRaster().getDataBuffer()).getData();
							screenRecorder.start();
							while (fine[0] && record) {
								sleepTime = System.currentTimeMillis();
								
								GDI32.INSTANCE.BitBlt(hdcMemDC, 0, 0, CAPTURE_RES_WIDTH, CAPTURE_RES_HEIGHT, hdcWindow, 0, 0, GDI32.SRCCOPY); //Grab
								
								GDI32.INSTANCE.GetDIBits(hdcWindow, hBitmap, 0, CAPTURE_RES_HEIGHT, buffer, bmi, WinGDI.DIB_RGB_COLORS); //Conv
								ByteBuffer bb = buffer.getByteBuffer(0, CAPTURE_RES_WIDTH * CAPTURE_RES_HEIGHT * 4);
								for (int y = 0; y < capH; y++) {
									for (int x = 0; x < capW; x++) {
										int index = ((y * fh) * CAPTURE_RES_WIDTH + (x * fw)) * 4;
										pixels[y * capW + x] = ((bb.get(index + 2) & 0xff) << 16) | ((bb.get(index + 1) & 0xff) << 8) | (bb.get(index) & 0xff);
									}
								}
								
								capGraphics.drawImage(grabbedImage, 0, 0, CAP_W, CAP_H, null); //Draw
								
								wrapper.paintChatPanel(chatGraphics);
								
								if (repaintLogo) {
									logoGraphics.setColor(Color.BLACK);
									logoGraphics.fillRect(0, 0, LOGO_W, LOGO_H);
									display.paint(logoGraphics, 0, 0, LOGO_W, LOGO_H);
									repaintLogo = false;
								}
								
								Frame toRecord = converter.convert(streamCanvas);
								toRecord.timestamp = sleepTime;
								screenRecorder.record(toRecord, AV_PIX_FMT_ARGB);
								
								long frameDelta = System.currentTimeMillis() - sleepTime;
								fpsLabel.setText("frameDelta: " + frameDelta);
								
								//sleepTime = REC_MILLIS - frameDelta;
								//if (sleepTime > 5) Thread.sleep(sleepTime); // Only sleep if we have at least 5ms to spare
							}
							capGraphics.dispose();
							chatGraphics.dispose();
							logoGraphics.dispose();
							g.dispose();
						} catch (Exception e) {
							fine[0] = false;
							e.printStackTrace();
						}
					}, "streamThread"
			);
			streamThread.start();
		} catch (Exception _) {
			fine[0] = false;
		}
	}
	
	private void stopRecording() {
		if (!record && !stopping) return;
		stopping = true;
		record = false;
		starter.setEnabled(false);
		stopCards.show(stopPanel, STOP_PROG);
		new Thread(() -> {
			while (streamThread.isAlive()) {
			}
			try {
				if (screenRecorder != null) {
					screenRecorder.stop();
					screenRecorder.release();
					screenRecorder = null;
				}
				GDI32.INSTANCE.DeleteObject(hBitmap);
				GDI32.INSTANCE.DeleteDC(hdcMemDC);
				User32.INSTANCE.ReleaseDC(null, hdcWindow);
				hBitmap = null;
				hdcMemDC = null;
				hdcWindow = null;
				if (streamThread != null) {
					streamThread.interrupt();
					streamThread = null;
				}
			} catch (Exception e) {
				System.err.println("Error while stopping:");
				e.printStackTrace();
			}
			Graphics g = streamCanvas.getGraphics();
			g.drawImage(bgImg, 0, 0, null);
			g.dispose();
			System.gc();
			stopping = false;
			starter.setEnabled(true);
			stopCards.show(stopPanel, STOP_BUTTON);
		}).start();
	}
	
	private JPanel makePreviewPanel() {
		return new JPanel(null, true) {
			//Overrides {
			@Override
			protected void paintComponent(Graphics g) {
				g.drawImage(streamCanvas, 0, 0, getWidth(), getHeight(), null);
			}
			//} Overrides
		};
	}
	//} Methods
	
	//Overrides {
	@Override
	public void setAsset(RegionEnum reg, AssetEnum asset) {
		display.setAsset(reg, asset);
	}
	
	@Override
	public AssetEnum getAsset(RegionEnum reg) {
		return display.getAsset(reg);
	}
	
	@Override
	public void setMood(RegionEnum reg, MoodEnum mood) {
		display.setMood(reg, mood);
	}
	
	@Override
	public MoodEnum getMood(RegionEnum reg) {
		return display.getMood(reg);
	}
	
	@Override
	public void dispose() {
		stopRecording();
		while (stopping) {
		}
		super.dispose();
	}
	//} Overrides
	
	//Classes {
	private static class RatioLayout implements LayoutManager {
		
		//Fields {
		private final int ratioWidth, ratioHeight, minWidth, minHeight;
		//} Fields
		
		//Constructor {
		public RatioLayout(int minWidth, int minHeight) {
			int ggt = ggt(minWidth, minHeight);
			ratioWidth = minWidth / ggt;
			ratioHeight = minHeight / ggt;
			this.minWidth = minWidth;
			this.minHeight = minHeight;
		}
		//} Constructor
		
		//Methods {
		private static int ggt(int a, int b) {
			while (b != 0) {
				int h = a % b;
				a = b;
				b = h;
			}
			return a;
		}
		//} Methods
		
		//Overrides {
		@Override
		public void addLayoutComponent(String name, Component comp) {
			
		}
		
		@Override
		public void removeLayoutComponent(Component comp) {
			
		}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			int w = parent.getWidth();
			int h = parent.getHeight();
			w -= w % ratioWidth;
			h -= h % ratioHeight;
			int nw = (h * ratioWidth) / ratioHeight;
			int nh = (w * ratioHeight) / ratioWidth;
			if (nw > w) {
				h = nh;
			} else {
				w = nw;
			}
			return new Dimension(Math.max(w, minWidth), Math.max(h, minHeight));
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(minWidth, minHeight);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			Component[] cs = parent.getComponents();
			if (cs.length < 1) return;
			Dimension size = preferredLayoutSize(parent);
			int x = parent.getWidth() / 2 - size.width / 2;
			int y = parent.getHeight() / 2 - size.height / 2;
			cs[0].setBounds(x, y, size.width, size.height);
		}
		//} Overrides
	}
	
	private static class LogoLayout implements LayoutManager {
		
		//Overrides {
		@Override
		public void addLayoutComponent(String name, Component comp) {
			
		}
		
		@Override
		public void removeLayoutComponent(Component comp) {
			
		}
		
		@Override
		public Dimension preferredLayoutSize(Container parent) {
			return minimumLayoutSize(parent);
		}
		
		@Override
		public Dimension minimumLayoutSize(Container parent) {
			return new Dimension(LOGO_W, LOGO_H);
		}
		
		@Override
		public void layoutContainer(Container parent) {
			synchronized (parent.getTreeLock()) {
				for (Component c : parent.getComponents()) {
					//TODO: implement square place alg
					c.setBounds(0, 0, LOGO_W, LOGO_H);
				}
			}
		}
		//} Overrides
	}
	//} Classes
}
//} Classes
