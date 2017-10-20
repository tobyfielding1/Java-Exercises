import java.util.Arrays;

public class State implements Cloneable {

	final static Integer[][] COMPLETE_BLOCKS = { { 2, 3 }, { 2, 2 }, { 2, 1 } };

	Integer[][] blocks = new Integer[3][2];
	Integer[] agent;
	int gridSize; // can be increased or decreased to make the problem harder or
					// easier, (not actually used for difficulty on this
					// occasion)

	public State(int n) {
		this.gridSize = n;
		int i = 1;
		for (Integer[] block : blocks) {
			block[0] = i++;
			block[1] = 1;
		}
		agent = new Integer[] { n, 1 };
	}

	public State(Integer[][] blocks, Integer[] agent, int n) {
		this.gridSize = n;
		this.blocks = blocks;
		this.agent = agent;
	}

	public State(int n, boolean complete) {
		this.gridSize = n;
		if (complete)
			blocks = COMPLETE_BLOCKS;
		agent = new Integer[] { n, 1 };
	}

	public boolean move(Action direction) {
		Integer[] oldAgent = agent.clone();

		switch (direction) {
		case UP:
			++agent[1];
			break;
		case DOWN:
			--agent[1];
			break;
		case RIGHT:
			++agent[0];
			break;
		case LEFT:
			--agent[0];
			break;
		}

		if (!(agent[0] <= gridSize && agent[0] > 0 && agent[1] <= gridSize && agent[1] > 0))
			return false;

		for (int i = 0; i < 3; i++) {
			if (Arrays.equals(agent, blocks[i])) {
				blocks[i] = oldAgent;
				break;
			}
		}

		return true;
	}

	public State copy() {
		return new State(blocks.clone(), agent.clone(), gridSize);
	}

	public boolean isComplete() {
		if (Arrays.deepEquals(blocks, COMPLETE_BLOCKS))
			return true;
		else
			return false;
	}

	public void print() {
		String[] letters = { " a", " b", " c" };
		for (int j = gridSize; j > 0; j--) {
			System.out.println();
			for (int i = 1; i <= gridSize; i++) {
				String s = " _";
				if (Arrays.equals(agent, new Integer[] { i, j }))
					s = " o";
				int r = 0;
				for (Integer[] block : blocks) {
					if (Arrays.equals(block, new Integer[] { i, j }))
						s = letters[r];
					r++;
				}
				System.out.print(s);
			}
		}
		System.out.println();
	}

	// used by A* as part of the heuristic
	public int minStepsToGo() {
		int steps = 0;
		for (int i = 0; i < 3; i++)
			steps = steps + Math.abs(COMPLETE_BLOCKS[i][0] - blocks[i][0])
					+ Math.abs(COMPLETE_BLOCKS[i][1] - blocks[i][1]);
		return steps;
	}

}