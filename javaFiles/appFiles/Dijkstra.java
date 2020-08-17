package appFiles;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Dijkstra {
	private int res;
	private boolean[][] isBlocked;
	private int numCells;
	private int startI, startJ, endI, endJ;
	Cell[][] cells;
	
	PriorityQueue queue = new PriorityQueue();
	private ArrayList<Point2D.Float> points = new ArrayList<Point2D.Float>();
	private int checks;
		
	// Weird conditions, like start == end, are handled by the DotsPanel class
	Dijkstra(int startI, int startJ, int endI, int endJ, boolean[][] isBlocked, int numCells, int res) {
		//initialize variables
		this.startI = startI;
		this.startJ = startJ;
		this.endI = endI;
		this.endJ = endJ;
		this.isBlocked = isBlocked;
		this.numCells = numCells;
		this.res = res;
		initializeCells();
	}
	
	public void updateResultLabels(Menu menu) {
		float pathLength = cells[endI][endJ].dist;
		menu.setLengthLabel(pathLength);
		menu.setChecksLabel(checks);
	}
	
	public ArrayList<Point2D.Float> go() {
		// set up increment array to facilitate looping through the neighbor cells			
		final int incrArr[][] = {{-1,0}, {0,1}, {1,0}, {0,-1}, {-1,1}, {1,1}, {1,-1}, {-1,-1}};
		
		queue.enqueue(new Point(startI, startJ), 0);
		
		while(queue.size() > 0) {
			checks++;
			Point currentPoint = queue.dequeue();
			int row = currentPoint.x;
			int col = currentPoint.y;
			Cell currentCell = cells[row][col];
			currentCell.visited = true;
			
			// maybe use row and col differently to know if we reached the end
			if(row == endI && col == endJ) break;
			
			// loop through neighbor cells
			for(int idx = 0; idx < incrArr.length; idx++) {
				int nI = row + incrArr[idx][0];
				int nJ = col + incrArr[idx][1];
				
				if (nI > numCells - 1 || nJ > numCells - 1 || nI < 0 || nJ < 0) continue;
				
				Cell neighborCell = cells[nI][nJ];
				
				// important to check if neighbor cell is a barrier or if it was already visited
				// also check if neighbor cell is diagonally blocked
				if(neighborCell.blocked || neighborCell.visited || (idx > 3 && isDiagBlocked(row, col, nI, nJ))) continue;
				
				float newDist = currentCell.dist + (idx > 3 ? 1.41421f : 1);
				if(newDist < neighborCell.dist) {
					neighborCell.dist = newDist;
					neighborCell.prev = currentPoint;
					queue.enqueue(new Point(nI, nJ), newDist);
				}
			}
		}
		
		// print path
		Cell currentCell = cells[endI][endJ];
		
		points.add(new Point2D.Float(endJ * res  + res / 2, endI * res  + res / 2));
		
		while(currentCell.prev != null) {
			int prevI = currentCell.prev.x;
			int prevJ = currentCell.prev.y;
			
			points.add(new Point2D.Float(prevJ * res  + res / 2, prevI * res  + res / 2));
			
			currentCell = cells[prevI][prevJ];
		}
		
		return points;
	}
	
	private boolean isDiagBlocked(int row, int col, int nI, int nJ) {
		return (cells[row][nJ].blocked && cells[nI][col].blocked);
	}
	
	private void initializeCells() {
		cells = new Cell[numCells][numCells];
        for(int i = 0; i < numCells; i++) {
        	for (int j = 0; j < numCells; j++) {
        		Cell newCell = new Cell();
        		newCell.blocked = isBlocked[i][j];
        		cells[i][j] = newCell;
        	}
        }
        cells[startI][startJ].dist = 0;
	}
	
	private class Cell {
		public boolean blocked, visited;
		public float dist = 5000;
	    public Point prev;
	}
}
