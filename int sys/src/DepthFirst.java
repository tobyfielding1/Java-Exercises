import java.util.Collection;
import java.util.Stack;

public class DepthFirst extends TreeSearch {

	// FILO data structure makes tree search into Depth First
	public DepthFirst() {
		fringe = new Stack<Node>();
	}

	@Override
	public Node removeFront() {
		return ((Stack<Node>) fringe).pop();
	}

	@Override
	public Collection<Object[]> successorFn(Node node) {
		this.shuffleActions(actions);
		return super.successorFn(node);
	}

	// method to prevent depth first getting stuck in an intinite loop
	private void shuffleActions(Action[] acts) {
		for (int i = acts.length - 1; i > 0; i--) {
			int index = r.nextInt(i + 1);
			Action a = acts[index];
			acts[index] = acts[i];
			acts[i] = a;
		}
	}

}
