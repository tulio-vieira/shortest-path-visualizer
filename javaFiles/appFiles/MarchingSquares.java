package appFiles;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class MarchingSquares {
	private boolean[][] isBlocked;
	private int res, numCells;
	private ArrayList<Shape> walls = new ArrayList<Shape>();
	
	MarchingSquares(boolean[][] isBlocked, int numCells, int res){
    	this.isBlocked = isBlocked;
    	this.res = res;
    	this.numCells = numCells;
	}
	
    public ArrayList<Shape> makeWalls() {
        for(int i = 0; i < numCells - 1; i++) {
        	for (int j = 0; j < numCells - 1; j++) {
        		updateSquare( new Point[]{ new Point(i, j), new Point(i, j + 1), new Point(i + 1, j + 1), new Point(i + 1, j) } );        		
        	}
        }
        return walls;
    }
    
    // Marching Squares logic
    private void updateSquare(Point[] corners) {
    	// loop through corners points
    	int caseNumber = 0;
    	for (int i = 0; i < 4; i++) {
    		int cellI = corners[i].x;
    		int cellJ = corners[i].y;
    		// case number is represented in binary by the four points, starting by the one in the lower left corner,
    		// that represents the unit, and going in the counter-clockwise direction
    		boolean selected = isBlocked[cellI][cellJ];
    		caseNumber += (int) ((selected ? 1 : 0) * Math.pow(2, 3 - i));
    	}
    	
    	// get a, b, c, d pixel coordinates (x and y refers to the first and second point 
    	// coordinates, but have nothing to do with the x and y from the screen).
    	// Also add res/2 for each coordinate to put the lines in the right place
    	Point2D a = new Point2D.Float(corners[0].y * res  + res / 2 + res/2, corners[0].x * res + res/2);
    	Point2D b = new Point2D.Float(corners[0].y * res + res + res/2, corners[0].x * res + res / 2 + res/2);
    	Point2D c = new Point2D.Float(corners[0].y * res  + res / 2 + res/2, corners[0].x * res + res + res/2);
    	Point2D d = new Point2D.Float(corners[0].y * res + res/2, corners[0].x * res + res / 2 + res/2);
    	
    	
        switch (caseNumber) {
        case 1:
        	walls.add(new Line2D.Float(c, d));
        	break;
        case 2:
        	walls.add(new Line2D.Float(b, c));
			break;
        case 3:
        	walls.add(new Line2D.Float(b, d));
            break;
        case 4:
        	walls.add(new Line2D.Float(a, b));
            break;
        case 5:
            walls.add(new Line2D.Float(a, d));
            walls.add(new Line2D.Float(b, c));
          break;
        case 6:  
        	walls.add(new Line2D.Float(a, c));
          break;
        case 7:  
        	walls.add(new Line2D.Float(a, d));
          break;
        case 8:  
        	walls.add(new Line2D.Float(a, d));
          break;
        case 9:  
        	walls.add(new Line2D.Float(a, c));
          break;
        case 10: 
        	walls.add(new Line2D.Float(a, b));
            walls.add(new Line2D.Float(c, d));
          break;
        case 11: 
        	walls.add(new Line2D.Float(a, b));
          break;
        case 12: 
        	walls.add(new Line2D.Float(b, d));
          break;
        case 13: 
        	walls.add(new Line2D.Float(b, c));
          break;
        case 14: 
        	walls.add(new Line2D.Float(c, d));
          break;
        }
    }
}
