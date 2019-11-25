//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	boolean[][] grid;
	int x, y;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		this.grid = grid;
		y = grid[0].length;	// height
		x = grid.length;	// width
	}
	
	
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int cleared = 0;	// number of cleared rows so far
		for (int j = 0; j < y; j++) {
			if (isFullRow(j)) cleared++;
			else if (cleared > 0) {
				copyRow(j-cleared, j);
				clearRow(j);
			}
		}
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return this.grid;
	}
	
	private boolean isFullRow(int row) {
		boolean[][] g = getGrid();
		for (int i = 0; i < x; i++) {
			if (g[i][row] == false) return false;
		}
		return true;
	}
	
	/**
	 * copy the values of row j2 into j1
	 * @param j1: row to be copied into (lower row in tetris)
	 * @param j2: row to be copied from (higher row in tetris)
	 */
	private void copyRow(int j1, int j2) {
		boolean[][] g = getGrid();
		for (int i = 0; i < x; i++) {
			g[i][j1] = g[i][j2];
		}
	}
	
	/**
	 * fill row j with all false
	 * @param j: row j to be cleared
	 */
	private void clearRow(int j) {
		boolean[][] g = getGrid();
		for (int i = 0; i < x; i++) g[i][j] = false;
	}
}
