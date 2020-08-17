package appFiles;
import java.awt.Point;
import java.util.ArrayList;

// Priority Queue implemented as a minimum binary heap.
// Lowest priority goes to the top
public class PriorityQueue {
	
	private class Node {
		Point p;
		float priority;
	    Node(Point p, float priority){
	        this.p = p;
	        this.priority = priority;
	    }
	}
	
	private ArrayList<Node> nodesArr = new ArrayList<Node>();
		
    public int size() {
    	return nodesArr.size();
    }
	
    public void enqueue (Point p, float priority) {
        nodesArr.add(new Node(p, priority));
        
        int idx = nodesArr.size() - 1;
        int parentIdx = (int) Math.floor((idx - 1)/2);

        while(idx > 0 && nodesArr.get(idx).priority < nodesArr.get(parentIdx).priority) {
        	swap(parentIdx, idx);
            idx = parentIdx;
            parentIdx = (int) Math.floor((idx - 1)/2);
        }
    }
    
    //remove minimum value (root)
    public Point dequeue(){
        if(nodesArr.size() == 0) return null;
        
        swap(0, nodesArr.size() - 1);        
    	Node min = nodesArr.remove(nodesArr.size() - 1);
    	if(nodesArr.size() == 0) return min.p;
        
        int parentIdx = 0;

        while(true){
        	int swap = 0;
            int leftChildIdx = 2 * parentIdx + 1;
            int rightChildIdx = 2 * parentIdx + 2;
            Node parent = nodesArr.get(parentIdx);

            if(leftChildIdx < nodesArr.size()){
                if(nodesArr.get(leftChildIdx).priority < parent.priority) swap = leftChildIdx;
            }
            if(rightChildIdx < nodesArr.size()){
                if(nodesArr.get(rightChildIdx).priority < parent.priority && nodesArr.get(rightChildIdx).priority < nodesArr.get(leftChildIdx).priority) swap = rightChildIdx;
            }
            if(swap != 0){
            	nodesArr.set(parentIdx, nodesArr.get(swap));
                nodesArr.set(swap, parent);
                parentIdx = swap;
            } else break;
        }
        return min.p;
    }
    
    private void swap(int i, int j) {
    	Node temp = nodesArr.get(i);
    	nodesArr.set(i, nodesArr.get(j));
    	nodesArr.set(j, temp);
    }
}
