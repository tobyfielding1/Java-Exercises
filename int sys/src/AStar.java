import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class AStar extends TreeSearch {
	
	@Override
	public Node treeSearch(State start) {
		fringe.add(new Node(start));

		while (true) {
			if (fringe.isEmpty())
				return null;
			Node node = removeFront();
			if (node.state.isComplete())
				return node;
			fringe.addAll(expand(node));
		}
	}
	public class CostComparator implements Comparator<Node> {
		@Override
		public int compare(Node x, Node y) {
			return x.f() - y.f();
		}
	}

	public AStar() {
		fringe = new PriorityQueue<Node>(100, new CostComparator());
	}

	@Override
	public Node removeFront() {
		Node n = ((PriorityQueue<Node>) fringe).remove();
		// System.out.println("f(n) = " + n.depth + " + " + n.h() + " = " +
		// n.f());
		return n;
	}

}
