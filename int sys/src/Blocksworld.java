
/**
 * @author tobyf
 *
 */

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Blocksworld {

	/**
	 * @param args - class name of search type.
	 */
	public static void main(String args[]) {
		System.out.println("*********************" + args[0] + "**********************");
		try {
			Class<?> clazz = Class.forName(args[0]);
			Constructor<?> ctor = clazz.getConstructor();
			
			//repeated for each of 14 levels of difficuylty
			for (int i = 5; i < 6; i++) {
				State start = getStart(i);
				
				//20 cycles for each difficulty
				for (int j = 0; j < 1; j++) {
					TreeSearch treeSearcher = (TreeSearch) ctor.newInstance();
					
					//returns final (state = complete) node or null.
					Node n = treeSearcher.treeSearch(start);

					if (n == null)
						System.out.println("Failed");
					else {
						System.out.println("solved in " + n.depth + " steps");
						System.out.println(treeSearcher.nodesExpanded + " nodes expanded");
						System.out.println("showing steps: ");
						System.out.println("space complexity: "+ treeSearcher.maxActiveNodes);
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
