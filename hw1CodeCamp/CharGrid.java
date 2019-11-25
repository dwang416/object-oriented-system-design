
// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		if (grid.length == 0 || grid[0].length == 0) return 0;
		int istart = -1, iend = -1, jstart = -1, jend = -1;
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[0].length; j++) {
				if (ch == grid[i][j]) {
					if (istart == -1) {
						istart = i;
						jstart = j;
					}
					iend = i;
					jend = j;
				}
			}
		}
		if (istart == -1) return 0;
		return (iend - istart + 1) * (jend - jstart + 1);
	}
	
	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		if (grid.length <= 2 || grid[0].length <= 2) return 0;
		int res = 0;
		for (int i = 1; i < grid.length-1; i++) {
			for (int j = 1; j < grid[0].length-1; j++) {
				if (grid[i][j] == ' ') continue;
				int arm = 0;
				int count = 0;
				while (true) {
					count = checkArm(i, j, arm+1);
					if (count < 4) break;
					arm++;
				}
				if (count == 0 && arm >= 1) res++;
			}
		}
		return res;
	}
	
	/**
	 * check whether the the four-directioned arm distant point are all
	 * equal to grid[x][y]
	 * @param x x-coordinate of center
	 * @param y y-coordinate of center
	 * @param arm the distance from center to current edge
	 * @return int[] res. res[0] is the number of 4 edges equal to center,
	 * res[1] is the number of 4 edges which is out of grid's boundary
	 */
	private int checkArm(int x, int y, int arm) {
		int cntEqual = 0;
		int m = grid.length, n = grid[0].length; 
		for (int[] xy: new int[][]{{x-arm, y}, {x+arm, y}, {x, y-arm}, {x, y+arm}}) {
			if (xy[0] < 0 || xy[0] >= m || xy[1] < 0 || xy[1] >= n) continue;
			if (grid[xy[0]][xy[1]] == grid[x][y]) cntEqual++;
		}
		
		return cntEqual;
	}
}
