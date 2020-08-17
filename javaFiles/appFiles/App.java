package appFiles;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

public class App extends JFrame {
	/**
	 * Contains the dotsPanel and the menu
	 */
	private static final long serialVersionUID = -3490590247647407489L;
 	private DotsPanel dotsPanel;
 	public Menu menu;
 	private BufferedImage icon;
 	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
            	Locale.setDefault(Locale.US);
        		@SuppressWarnings("unused")
        		App myApp= new App();
            }
        });
	}
 	
	App() {
		loadImage("/imgs/icon_1-small.png");
		setIconImage(icon);
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

	private void loadImage(String imgPath) {
		try {
			icon = ImageIO.read(getClass().getResource(imgPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
