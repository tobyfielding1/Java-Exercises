
public class IterativeDeepening extends DepthLimited {

	@Override
	public Node treeSearch(State start) {
		Node node = null;
		int n = 1;
		while (node == null)
			node = limitedSearch(start, n++);
		return node;
	}

}
