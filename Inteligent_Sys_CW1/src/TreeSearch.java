import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public abstract class TreeSearch {

	Collection<Node> fringe;

	// these variables are entirely to keep track of space complexity
	int nodesExpanded = 0;
	long maxActiveNodes = 0;
	Random r = new Random();

	public abstract Node removeFront();

	// method to keep track of space complexity. Counts all nodes accessible by
	// percolating up from the fringe
	private void countActiveNodes() {
		if (r.nextInt(50) == 0) {
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
		// System.out.print("\nexpanding node at depth " + node.depth);
		// node.state.print();
		Collection<Node> successors = new ArrayList<Node>();

		for (Object[] actionResult : successorFn(node))
			successors.add(new Node(node, (Action) actionResult[0], (State) actionResult[1]));
		nodesExpanded += successors.size();
		return successors;
	}

	public Collection<Object[]> successorFn(Node node) {
		Collection<Object[]> actionResults = new ArrayList<Object[]>();
		Action[] actions = Action.values();
		shuffleActions(actions);
		State s;
		for (Action action : actions) {
			s = node.state.copy();
			if (s.move(action))
				actionResults.add(new Object[] { action, s });
		}

		return actionResults;
	}

	// method to prevent depth first getting stuck in an intinite loop
	// used on all searches for consistency
	static void shuffleActions(Action[] ar) {
		Random rnd = ThreadLocalRandom.current();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			Action a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
		}
	}

}
