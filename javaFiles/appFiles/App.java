package appFiles;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.util.Locale;

public class App extends JFrame {
	/**
	 * Contains the dotsPanel and the menu
	 */
	private static final long serialVersionUID = -3490590247647407489L;
 	private DotsPanel dotsPanel;
 	public Menu menu;
 	
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		@SuppressWarnings("unused")
		App myApp= new App();
	}
 	
	App() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Shortest Path Visualizer");
        
        menu = new Menu();
        dotsPanel = new DotsPanel(menu, 700, 20);
        
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(dotsPanel, BorderLayout.CENTER);
        pane.add(menu, BorderLayout.EAST);
        
        pack();
        
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
	}
}
