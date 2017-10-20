public class Node {
	final Node parent;
	final Action action;
	final State state;
	final int depth;
	private Integer evalFunc = null;

	public Node(Node parent, Action action, State state) {
		this.parent = parent;
		this.action = action;
		depth = parent.depth + 1;
		this.state = state;
	}

	public Node(State state) {
		depth = 0;
		this.state = state;
		parent = null;
		this.action = null;
	}

	// recursive print used to display solution path
	public void print() {
		state.print();

		if (this.parent == null)
			return;

		this.parent.print();
	}

	// used in A*
	public Integer f() {
		if (evalFunc == null)
			evalFunc = depth + h();
		return evalFunc;
	}

	// heuristic used in A*
	public Integer h() {
		return this.state.minStepsToGo();
	}

}
