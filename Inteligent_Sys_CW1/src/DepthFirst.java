import java.util.Stack;

public class DepthFirst extends TreeSearch {

	public DepthFirst() {
		fringe = new Stack<Node>();
	}

	@Override
	public Node removeFront() {
		return ((Stack<Node>) fringe).pop();
	}

}
