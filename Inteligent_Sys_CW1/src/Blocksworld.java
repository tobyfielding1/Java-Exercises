
/**
 * @author tobyf
 *
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Blocksworld {

	/**
	 * @param args
	 *            - class name of search type.
	 */
	public static void main(String args[]) {
		System.out.println(new AStar().getClass().getName());
		System.out.println("*********************" + args[0] + "**********************");
		try {
			Class<?> clazz = Class.forName(args[0]);
			Constructor<?> ctor = clazz.getConstructor();

			for (int i = 1; i < 15; i++) {
				State start = getStart(i);

				for (int j = 0; j < 20; j++) {
					TreeSearch treeSearcher = (TreeSearch) ctor.newInstance();

					Node n = treeSearcher.treeSearch(start);

					if (n == null)
						System.out.println("Failed");
					else {
						System.out.println("solved in " + n.depth + " steps");
						System.out.println(treeSearcher.nodesExpanded + " nodes expanded");
						System.out.println("showing steps: ");
						System.out.println(treeSearcher.maxActiveNodes);
						n.print();
					}
				}
			}
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			System.err.println("please give valid search class name as only argument");
		}
	}

	// uses A* search to generate a start state which is n optimal moves from
	// completion
	public static State getStart(int difficulty) {
		State start = new State(4);
		TreeSearch treeSearcher = new IterativeDeepening();
		Node n = treeSearcher.treeSearch(start);
		int totalDepth = n.depth;
		while (n.parent != null && totalDepth - n.depth < difficulty)
			n = n.parent;
		System.out.println("****************** Difficulty level: " + (totalDepth - n.depth));
		return n.state;
	}

}
