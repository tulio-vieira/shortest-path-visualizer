package appFiles;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class DotsPanel extends JPanel implements MouseListener, ActionListener, ChangeListener, MouseMotionListener {
	/**
	 * Takes care of the grid GUI with the dots, walls and paths. Calls other classes related to logic
	 */
	private static final long serialVersionUID = -8018747344459566085L;
	
	// logic variables
	private int numCells;
 	private int res;
	private boolean[][] isBlocked;
	private int startI = -1;
	private int startJ = -1;
	private int endI = -1;
	private int endJ = -1;
	private int screenSize;
	private int currentI, currentJ;
	
	// Listener variables
	private String editCommand = "Wall";
	// private boolean isMousePressed = false;
	
	// GUI variables
	private Color gridColor = Color.black;
	private Color backgroundColor = new Color(66, 66, 66);
	private Color borderColor = new Color(45, 45, 45);
	private Color wallColor = Color.red;
	private Color unselectedColor = Color.gray;
	private Color selectedColor = wallColor;
	private Color pathColor = Color.white;
	private int dotSize;
	private int wallWidth;
	private ArrayList<Shape> walls = new ArrayList<Shape>();
	private ArrayList<Shape> paths = new ArrayList<Shape>();
	private ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
	private Menu menu;
	
    
	DotsPanel(Menu menu, int screenSize, int numCells) {
		// initialize variables
		this.res = screenSize / numCells;
		this.numCells = numCells;
		this.isBlocked = new boolean[numCells][numCells];
		this.screenSize = screenSize;
		this.menu = menu;
		// set up event listeners
		menu.addEventListeners(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		// GUI setup
		dotSize = res / 4;
		setPreferredSize(new Dimension(screenSize, screenSize));
		wallWidth = (int) (res * 0.1);
		wallWidth = (wallWidth < 2) ? 2 : wallWidth;
		setBorder(BorderFactory.createLineBorder(borderColor, (int) (0.35 * res)));
		setBackground(backgroundColor);
	}
	
    public void paintComponent(Graphics g) {
    	((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	super.paintComponent(g);
    	handleDots(g);
    	handleStartAndEnd(g);
    	handleWalls(g);
    	handlePaths(g);
    }
		
	public void shortestPath() {
		if (startI == -1 || endI == -1) return;
		int algorithmIndex = menu.dropDownList.getSelectedIndex();
		if (algorithmIndex == 0) {
			Dijkstra dijkstra = new Dijkstra(startI, startJ, endI, endJ, isBlocked, numCells, res);
			points = dijkstra.go();
			dijkstra.updateResultLabels(menu);
			pathColor = Color.white;
		} else {
			AStar aStar = new AStar(startI, startJ, endI, endJ, isBlocked, numCells, res);
			points = aStar.go();
			aStar.updateResultLabels(menu);
			pathColor = new Color(78, 235, 252);
		}
		paths = new ArrayList<Shape>();
		if (points.size() != 1) makePaths();
		repaint();
	}
	
	public void setStart() {
		if (isBlocked[currentI][currentJ] || (currentI == endI && currentJ == endJ)) return;
		startI = currentI;
		startJ = currentJ;
		repaint();
	}
	
	public void setEnd() {
		if (isBlocked[currentI][currentJ] || (currentI == startI && currentJ == startJ)) return;
		endI = currentI;
		endJ = currentJ;
		repaint();
	}
	
	public void updateWallsGUI() {
		MarchingSquares mSquares = new MarchingSquares(isBlocked, numCells, res);
		walls = mSquares.makeWalls();
		repaint();
	}
    
    private void makePaths() {
    	for(int i = 0; i < points.size() - 1; i++) {
    		paths.add(new Line2D.Float(points.get(i), points.get(i + 1)));
    	}
    }
    
    private void handlePaths(Graphics g) {
    	if (paths.size() == 0) return;
    	g.setColor(pathColor);
    	for (Point2D.Float point: points) {
    		g.fillOval((int) (point.x - dotSize/2), (int) point.y - dotSize/2, dotSize, dotSize);
    	}
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setStroke(new BasicStroke(wallWidth + 2));
    	for (Shape path: paths) {
    		g2d.draw(path);
    	}
    }
    
    private void handleStartAndEnd(Graphics g) {
    	if (startI != -1) {
    		handleStartAndEndHelper(g, startI, startJ, new Color(158, 255, 165), new Color(0, 186, 13));
    	}
    	if (endI != -1) {
    		handleStartAndEndHelper(g, endI, endJ, new Color(181, 133, 0), new Color(255, 214, 99));
    	}
    }

    private void handleStartAndEndHelper(Graphics g, int i, int j, Color backgroundColor, Color dotColor) {
		int x0 = j * res;
		int y0 = i * res;
		g.setColor(backgroundColor);
		g.fillOval(x0 + res / 2 - dotSize*3/2, y0 + res / 2 - dotSize*3/2, dotSize*3, dotSize*3);
		g.setColor(dotColor); 
		g.fillOval(x0 + res / 2 - dotSize/2, y0 + res / 2 - dotSize/2, dotSize, dotSize);
    }
    
    private void handleDots(Graphics g) {
        for(int i = 0; i < numCells; i++) {
        	for (int j = 0; j < numCells; j++) {
    			int x0 = j * res;
    			int y0 = i * res;
    			g.setColor(isBlocked[i][j] ? selectedColor : unselectedColor);
    			g.fillOval(x0 + res / 2 - dotSize/2, y0 + res / 2 - dotSize/2, dotSize, dotSize);
    			g.setColor(gridColor);
    			g.drawRect(x0, y0, res, res);
        	}
        }
    }
    
    private void handleWalls(Graphics g) {
    	g.setColor(wallColor);
    	Graphics2D g2d = (Graphics2D) g;
    	g2d.setStroke(new BasicStroke(wallWidth));
    	for (Shape wall: walls) {
    		g2d.draw(wall);
    	}
    }
    
    private void generateWalls(float probability) {
		isBlocked = new boolean[numCells][numCells];
		startI = startJ = endI = endJ = -1;
		paths = new ArrayList<Shape>();
        for(int i = 0; i < numCells; i++) {
        	for (int j = 0; j < numCells; j++) {
        		isBlocked[i][j] = Math.random() > probability;
        	}
        }
        updateWallsGUI();
    }
    
    private void updateWalls() {
    	if ((currentI == startI && currentJ == startJ) || (currentI == endI && currentJ == endJ)) return;
    	isBlocked[currentI][currentJ] = !isBlocked[currentI][currentJ];
    	updateWallsGUI();
    }
    
//    -------------- LISTENER METHODS ------------------

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch (command) {
		case "Run":
			shortestPath();
			break;
		case "Clear grid":
			clear();
			break;
		case "Generate walls":
			generateWalls(0.7f);
			break;
		default:
			editCommand = command;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		currentJ = Math.floorDiv(e.getX(), res);
		currentI = Math.floorDiv(e.getY(), res);
		if (currentI > numCells - 1 || currentJ > numCells - 1) return;
		switch (editCommand) {
		case "Start point":
			setStart();
			break;
		case "End point":
			setEnd();
			break;
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (editCommand != "Wall") return;
		currentJ = Math.floorDiv(e.getX(), res);
		currentI = Math.floorDiv(e.getY(), res);
		if (currentI > numCells - 1 || currentJ > numCells - 1) return;
		updateWalls();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if (editCommand != "Wall") return;
		int j = Math.floorDiv(e.getX(), res);
		int i = Math.floorDiv(e.getY(), res);
		if (i == currentI && j == currentJ) return;
		currentJ = j;
		currentI = i;
		if (currentI > numCells - 1 || currentJ > numCells - 1) return;
		updateWalls();
	}
	
	@Override
	public void stateChanged (ChangeEvent e) {
		int value = ((JSlider) e.getSource()).getValue();
		if (value % 5 != 0 || value == numCells) return;
		
		numCells = value;
		res = screenSize / numCells;
		dotSize = res / 4;
		wallWidth = (int) (res * 0.1);
		wallWidth = (wallWidth < 2) ? 2 : wallWidth;
		setBorder(BorderFactory.createLineBorder(borderColor, (int) (0.35 * res)));

		clear();
		menu.setNumCellsLabel(numCells);
	}
	
	private void clear() {
		isBlocked = new boolean[numCells][numCells];
		startI = startJ = endI = endJ = -1;
		paths = new ArrayList<Shape>();
		walls = new ArrayList<Shape>();
		repaint();
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseMoved(MouseEvent e) {}
}