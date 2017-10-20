import java.util.ArrayList;
import java.util.Collection;

public class DepthLimited extends DepthFirst {

	int depthLimit;

	public Node limitedSearch(State start, int depthLimit) {
		this.depthLimit = depthLimit;
		return super.treeSearch(start);
	}

	@Override
	public Collection<Object[]> successorFn(Node node) {
		if (node.depth == depthLimit)
			return new ArrayList<Object[]>();
		else
			return super.successorFn(node);
	}

}
