
import java.util.LinkedList;

public class BreadthFirst extends TreeSearch {
	
	//Linked list acts as a FIFO (first in first out queue). 
	//This is the only thing required to turn a Tree Search into a BFS
	public BreadthFirst() {
		fringe = new LinkedList<Node>();
	}

	@Override
	public Node removeFront() {
		return ((LinkedList<Node>) fringe).remove();
	}

}
