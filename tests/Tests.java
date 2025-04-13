import com.github.crafterchen2.logoanim.MoodProvider;
import com.github.crafterchen2.logoanim.components.AssetSelector;
import com.github.crafterchen2.logoanim.components.MoodSelector;
import com.github.crafterchen2.logoanim.frames.StreamFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

//Classes {
public class Tests {
	
	//Methods {
	public static void main(String[] args) {
		streamFrameTest();
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
