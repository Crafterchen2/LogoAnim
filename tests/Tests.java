import com.github.crafterchen2.logoanim.*;
import com.github.crafterchen2.logoanim.components.AssetSelector;
import com.github.crafterchen2.logoanim.components.LogoDisplay;
import com.github.crafterchen2.logoanim.components.MoodSelector;
import com.github.crafterchen2.logoanim.frames.StreamFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;

//Classes {
public class Tests {
	
	//Methods {
	public static void main(String[] args) {
		//testStream();
		//idRegexTest();
		colorParseTest();
		colorParseTestFor();
	}
	
	/**
	 * Pattern:
	 * <code>
	 * byte in = [R|r|G|g|B|b]
	 * int out = [0xFF|R*4|r*4|G*4|g*4|B*4|b*4]
	 * </code>
	 */
	private static void colorParseTestFor() {
		byte[] ins = new byte[]{
				(byte) 0b00_00_00,
				(byte) 0b10_10_10,
				(byte) 0b01_01_01,
				(byte) 0b11_11_11,
				(byte) 0b00_01_10,
				(byte) 0b11_10_01,
				};
		for (int i = 0; i < ins.length; i++) {
			byte in = ins[i];
			int out = 0;
			for (int j = 0; j < 6; j++) {
				out = ((out |((in & (1 << j)) << (31 - j))) >> 3) >>> 1;
			}
			out = (out >>> 7) | 0xff << 24;
			System.out.println(i + ": 0b" + Long.toBinaryString(in) + " > 0x" + Integer.toHexString(out));
		}
	}
	
	/**
	 * Pattern:
	 * <code>
	 * byte in = [R|r|G|g|B|b]
	 * int out = [0xFF|R*4|r*4|G*4|g*4|B*4|b*4]
	 * </code>
	 */
	private static void colorParseTest() {
		byte[] ins = new byte[]{
				(byte) 0b00_00_00,
				(byte) 0b10_10_10,
				(byte) 0b01_01_01,
				(byte) 0b11_11_11,
				(byte) 0b00_01_10,
				(byte) 0b11_10_01,
				};
		for (int i = 0; i < ins.length; i++) {
			byte in = ins[i];
			int out = (in & 0b00_00_01) << 31;
			out >>= 3;
			out >>>= 1;
			out |= (in & 0b00_00_10) << 30;
			out >>= 3;
			out >>>= 1;
			out |= (in & 0b00_01_00) << 29;
			out >>= 3;
			out >>>= 1;
			out |= (in & 0b00_10_00) << 28;
			out >>= 3;
			out >>>= 1;
			out |= (in & 0b01_00_00) << 27;
			out >>= 3;
			out >>>= 1;
			out |= (in & 0b10_00_00) << 26;
			out >>= 3;
			out >>>= 8;
			out |= 0xee << 24;
			System.out.println(i + ": 0b" + Long.toBinaryString(in) + " > 0x" + Integer.toHexString(out));
		}
	}
	
	private static void idRegexTest() {
		String[] texts = new String[]{
				"https://www.youtube.com/watch?v=O_1z0UhvY2Y",
				"O_1z0UhvY2Y",
				"https://www.youtube.com/watch?v=O_1z0:UhvY2Y",
				"O_1z0:UhvY2Y",
				"https://www.youtube.com/watch?v=O_1z0:UhvY2",
				"O_1z0:UhvY2",
		};
		
		for (String text : texts) {
			if (!text.matches(".*[A-Za-z0-9_-]{11}$")) {
				System.out.println("no match");
				continue;
			}
			text = text.replaceFirst(".*(?=[A-Za-z0-9_-]{11}$)", "");
			System.out.println(text);
		}
	}
	
	private static void testStream(){
		Component[] cs = new Component[]{
				new JButton("1"),
				//new JLabel("2"),
				//new JRootPane(),
				//new LogoDisplay(new AssetProvider.Default(AssetEnum.EYE_2X2, AssetEnum.EYE_2X2, AssetEnum.SMIRK, null), new MoodProvider.Default(MoodEnum.STAR, MoodEnum.GOOD, null, MoodEnum.SANS)),
				//new LogoDisplay(new AssetProvider.Default(AssetEnum.EYE_3X1, AssetEnum.EYE_2X2, AssetEnum.SMIRK, null), new MoodProvider.Default(MoodEnum.STAR, MoodEnum.GOOD, null, MoodEnum.SANS)),
				//new LogoDisplay(new AssetProvider.Default(AssetEnum.EYE_3X3, AssetEnum.EYE_2X2, AssetEnum.SMIRK, null), new MoodProvider.Default(MoodEnum.STAR, MoodEnum.GOOD, null, MoodEnum.SANS)),
				//new JButton("3"),
				};
		LogoDisplay[] ds = Arrays.stream(cs)
				.filter(component -> component instanceof LogoDisplay)
				.toList().toArray(new LogoDisplay[0]);
		System.out.println(ds.length);
	}
	
	private static void streamFrameTest(){
		StreamFrame f = new StreamFrame(false);
		
	}
	
	private static void streamFrameConceptTest() {
		JFrame frame = new JFrame("streamFrameConceptTest");
		frame.setUndecorated(true);
		frame.setLocationRelativeTo(null);
		frame.setSize(500,500);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setVisible(true);
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Graphics g = frame.getGraphics();
				g.setColor(Color.BLACK);
				g.drawString(frame.getSize().toString(), 100,100);
				g.dispose();
			}
		});
	}
	
	private static void assetSelectorTestFrame() {
		JFrame frame = new JFrame("AssetSelectorTestFrame");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new AssetSelector(new MoodProvider.Default()));
		frame.setVisible(true);
	}
	
	private static void moodSelectorTestFrame() {
		JFrame frame = new JFrame("AssetSelectorTestFrame");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(600, 600);
		frame.setLocationRelativeTo(null);
		frame.setContentPane(new MoodSelector());
		frame.setVisible(true);
	}
	//} Methods
	
}
//} Classes
