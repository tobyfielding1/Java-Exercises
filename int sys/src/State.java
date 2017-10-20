import java.util.Arrays;

public class State implements Cloneable {

	final static int[][] COMPLETE_BLOCKS = { { 2, 3 }, { 2, 2 }, { 2, 1 } };

	int[][] blocks = new int[3][2];
	int[] agent = new int[2];
	int gridSize; // can be increased or decreased to make the problem harder or
					// easier (not actually used in my test)

	public State(int n) {
		this.gridSize = n;
		int i = 1;
		for (int[] block : blocks) {
			block[0] = i++;
			block[1] = 1;
		}
		agent = new int[] { n, 1 };
	}

	public State(int[][] blocks, int[] agent, int n) {
		this.gridSize = n;
		this.blocks = blocks;
		this.agent = agent;
	}

	public boolean change(Action direction) {
		int[] oldAgent = agent.clone();

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

		if (agent[0] > gridSize || agent[0] <= 0 || agent[1] > gridSize || agent[1] <= 0)
			return false;

		for (int i = 0; i < 3; i++) {
			if (Arrays.equals(agent, blocks[i])) {
				blocks[i] = oldAgent;
				break;
			}
		}

		return true;
	}

	@Override
	public State clone() {
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
				if (Arrays.equals(agent, new int[] { i, j }))
					s = " o";
				int r = 0;
				for (int[] block : blocks) {
					if (Arrays.equals(block, new int[] { i, j }))
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
