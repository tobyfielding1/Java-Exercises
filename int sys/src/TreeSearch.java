import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class TreeSearch {

	Collection<Node> fringe;
	static Random r = new Random();
	Action[] actions = Action.values();
	
	// these variables are entirely to keep track of space complexity
	int nodesExpanded = 0;
	long maxActiveNodes = 0;

	public abstract Node removeFront();

	// method to keep track of space complexity. Counts all nodes accessible by
	// percolating up from the fringe
	private void countActiveNodes() {
		if (r.nextInt(5) == 0) {
			Set<Node> activeNodes = new HashSet<Node>();
			for (Node n : fringe) {
				activeNodes.add(n);
				do
					n = n.parent;
				while (n != null && activeNodes.add(n));
			}
			long numNodes = activeNodes.size();
			if (maxActiveNodes < numNodes)
				maxActiveNodes = numNodes;
		}
	}

	public Node treeSearch(State start) {
		fringe.add(new Node(start));

		while (true) {
			if (fringe.isEmpty())
				return null;
			Node node = removeFront();
			if (node.state.isComplete())
				return node;
			fringe.addAll(expand(node));

			countActiveNodes();
		}
	}

	public Collection<Node> expand(Node node) {
		Collection<Node> successors = new ArrayList<Node>();

		for (Object[] actionResult : successorFn(node))
			successors.add(new Node(node, (Action) actionResult[0], (State) actionResult[1]));
		nodesExpanded += successors.size();
		return successors;
	}

	public Collection<Object[]> successorFn(Node node) {
		Collection<Object[]> actionResults = new ArrayList<Object[]>();
		State s;
		for (Action action : actions) {
			s = node.state.clone();
			if (s.change(action))
				actionResults.add(new Object[] { action, s });
		}

		return actionResults;
	}

}
