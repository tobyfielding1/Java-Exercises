
import java.util.LinkedList;

public class BreadthFirst extends TreeSearch {

	public BreadthFirst() {
		fringe = new LinkedList<Node>();
	}

	@Override
	public Node removeFront() {
		return ((LinkedList<Node>) fringe).remove();
	}

}
