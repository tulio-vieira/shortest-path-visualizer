package appFiles;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

public class Menu extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7070572685701442257L;
	
	private JLabel lengthLabel = new JLabel("Path length: 0");
	private JLabel checksLabel = new JLabel("Checks: 0");
	private JLabel numCellsLabel = new JLabel("20x20");
	public JComboBox<String> dropDownList;
	public JSlider slider = new JSlider(JSlider.HORIZONTAL, 10, 50, 20);
	private JButton[] buttons = new JButton[] {
		new JButton("Wall"),
		new JButton("Start point"),
		new JButton("End point"),
		new JButton("Clear grid"),
		new JButton("Generate walls"),
		new JButton("Run")
	};
	private Color backgroundColor = new Color(66, 66, 66);
    private GridBagConstraints gbc = new GridBagConstraints();	
	
	Menu() {
		setBackground(backgroundColor);
		setBorder(BorderFactory.createLineBorder(new Color(0,0,0,0), 10));
        
        JLabel menuTitleLabel = new JLabel("Menu", JLabel.CENTER);
        menuTitleLabel.setFont(new Font("Montserrat", Font.PLAIN, 20));
        menuTitleLabel.setForeground(Color.WHITE);
        
        // ORGANIZING COMPONENTS IN GRID BAG LAYOUT
        
        setLayout(new GridBagLayout());
        
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0,0,10,0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
        add(menuTitleLabel, gbc);
        
        setupEditButtons();
        setupAlgorithmBox();
        setupResultsBox();
        setupCredits();
	}
	
	public void addEventListeners(ActionListener listener) {
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].addActionListener(listener);
			if (i < 3) buttons[i].addActionListener(this);
		}
		slider.addChangeListener((ChangeListener) listener);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton buttonPressed = (JButton) e.getSource();
		for(int i = 0; i < 3; i++) {
			buttons[i].setEnabled(true);
		}
		buttonPressed.setEnabled(false);
	}
	
	public void setLengthLabel(float pathLength) {
		if (pathLength == (float) 5000) {
			lengthLabel.setText("Path length: Path not found");
		} else {
			lengthLabel.setText(String.format("Path length: %.2f", pathLength));
		}
	}
	
	public void setChecksLabel(int checks) {
		checksLabel.setText("Checks: " + checks);
	}
	
	public void setNumCellsLabel(int numCells) {
		numCellsLabel.setText(numCells + "x" + numCells);
	}
	
	private void setupEditButtons() {		
		gbc.insets = new Insets(0,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		
		JLabel editLabel = new JLabel("Edit:", JLabel.LEFT);
		editLabel.setFont(new Font("Montserrat", Font.PLAIN, 15));
		editLabel.setForeground(Color.WHITE);
		add(editLabel, gbc);
		
		gbc.insets = new Insets(0,0,0,0);
		gbc.gridx = 0;
		gbc.gridy++;
		add(buttons[0], gbc);

		gbc.gridx = 1;
		add(buttons[1], gbc);

		gbc.gridx = 2;
		add(buttons[2], gbc);
		
		gbc.gridwidth = 3;
		gbc.gridx = 0;
		gbc.gridy++;
		gbc.insets = new Insets(10,0,0,0);
		add(buttons[3], gbc);

		gbc.gridy++;
		gbc.insets = new Insets(0,0,0,0);
		add(buttons[4], gbc);
		
		gbc.gridy++;
		gbc.insets = new Insets(10,0,0,0);
		add(setupSlider(), gbc);
	}
	
	private JPanel setupSlider() {
		JLabel label1 = new JLabel("Size: ");
		label1.setForeground(Color.WHITE);
		
		numCellsLabel.setForeground(Color.WHITE);
		
		JPanel slideBar = new JPanel();
		slideBar.setLayout(new BorderLayout());
		
		slider.setOpaque(false);
		slider.setMajorTickSpacing(5);
		slider.setSnapToTicks(true);
		
		slideBar.add(label1, BorderLayout.WEST);
		slideBar.add(slider, BorderLayout.CENTER);
		slideBar.add(numCellsLabel, BorderLayout.EAST);
		slideBar.setOpaque(false);
		return slideBar;
	}
	
	private void setupAlgorithmBox() {		
		gbc.insets = new Insets(20,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		
		JLabel algorithmLabel = new JLabel("Algorithm:", JLabel.LEFT);
		algorithmLabel.setFont(new Font("Montserrat", Font.PLAIN, 15));
		algorithmLabel.setForeground(Color.WHITE);
		add(algorithmLabel, gbc);
				
		dropDownList = new JComboBox<String>();
		dropDownList.addItem("Dijkstra");
		dropDownList.addItem("A*");
		
		gbc.insets = new Insets(0,0,0,5);
		gbc.gridy++;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		add(dropDownList, gbc);
		
		gbc.gridx = 2;
		gbc.gridwidth = 1;
		add(buttons[5], gbc);
	}
	
	private void setupResultsBox() {		
		gbc.insets = new Insets(20,0,5,0);
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy++;
		
		JLabel resultsTitle = new JLabel("Results:", JLabel.LEFT);
		resultsTitle.setFont(new Font("Montserrat", Font.PLAIN, 15));
		resultsTitle.setForeground(Color.WHITE);
		add(resultsTitle, gbc);
		
		setResultStyle(lengthLabel, 1);
		setResultStyle(checksLabel, 1);
		
		gbc.gridwidth = 3;
		gbc.insets = new Insets(0,10,0,0);
		gbc.gridy++;
		add(lengthLabel, gbc);
		
		gbc.gridy++;
		add(checksLabel, gbc);
	}
	
	private void setupCredits() {
		JLabel creatorLabel = new JLabel("Created by: Tulio Vieira", JLabel.RIGHT);
		JLabel githubLabel = new JLabel("github.com/tulio-vieira", JLabel.RIGHT);
		setResultStyle(creatorLabel, 0);
		setResultStyle(githubLabel, 1);
		
		gbc.insets = new Insets(100,0,0,0);
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridwidth = 2;
		gbc.gridx = 1;
		gbc.gridy++;
		add(creatorLabel, gbc);
		
		gbc.insets = new Insets(0,0,0,0);
		gbc.gridy++;
		add(githubLabel, gbc);
	}
	
	private void setResultStyle(JLabel label, int weight) {
		label.setFont(new Font("Montserrat", weight, 13));
		label.setForeground(Color.WHITE);
	}
}
