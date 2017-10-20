import java.util.Comparator;
import java.util.PriorityQueue;

public class AStar extends TreeSearch {

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
