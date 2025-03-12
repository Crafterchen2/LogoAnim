import com.github.crafterchen2.logoanim.MoodProvider;
import com.github.crafterchen2.logoanim.components.AssetSelector;
import com.github.crafterchen2.logoanim.components.MoodSelector;

import javax.swing.*;

//Classes {
public class Tests {
	
	//Methods {
	public static void main(String[] args) {
		
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
