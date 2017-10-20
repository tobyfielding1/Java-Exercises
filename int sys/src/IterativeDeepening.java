
public class IterativeDeepening extends DepthLimited {

	//carries out DLS up to depth n for n = 1 to infinity
	@Override
	public Node treeSearch(State start) {
		Node node = null;
		int n = 1;
		while (node == null)
			node = limitedSearch(start, n++);
		return node;
	}

}
