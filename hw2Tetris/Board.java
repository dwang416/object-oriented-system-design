import java.util.Arrays;

// Board.java

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	boolean committed;
	
	private int[] widths;	// stores how many filled spots there are in each row
	private int[] heights;	// stores the height to which each column has been filled
	
	private int maxHeight;	// stores the maximum column height, 
							// updated when place() or clearRows() called
	
	// variables for backup (for undo())
	private boolean[][] xGrid;
	private int[] xWidths;
	private int[] xHeights;
	private int xMaxHeight;
	
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		committed = true;
		
		// widths and heights are updated when place() or clearRows called
		widths = new int[height];
		heights = new int[width];
		Arrays.fill(widths, 0);
		Arrays.fill(heights, 0);
		
		maxHeight = 0;
		
		// initialization for backup variables
		xGrid = new boolean[width][height];
		xWidths = new int[height];
		xHeights = new int[width];
		xMaxHeight = 0;
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	public int[] getWidths() {
		return widths;
	}
	
	public int[] getHeights() {
		return heights;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight;
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {
			int[] checkWidths = new int[height];
			int[] checkHeights = new int[width];
			int checkMaxHeight = 0;
			for (int i = 0; i < width; i++) {
				for (int j = 0; j < height; j++) {
					if (grid[i][j]) {
						checkWidths[j]++;
						checkHeights[i] = j+1;
					}
				}
				checkMaxHeight = Math.max(checkMaxHeight, checkHeights[i]);
			}
			
			StringBuilder desc = new StringBuilder();
			desc.append("\nchecking heights:\n");
			String prep;
			boolean notSane = false;
			for (int i = 0; i < width; i++) {
				if (heights[i] == checkHeights[i]) prep = " and ";
				else {
					prep = " but ";
					notSane = true;
				}
				desc.append("column " + i + " should be " + heights[i] + prep + "it is " + checkHeights[i] + "\n");
			}
			desc.append("checking widths:\n");
			for (int j = 0; j < height; j++) {
				if (widths[j] == checkWidths[j]) prep = " and ";
				else {
					prep = " but ";
					notSane = true;
				}
				desc.append("row " + j + " should be " + widths[j] + prep + "it is " + checkWidths[j] + "\n");
			}
			desc.append("checking maxHeight:\n");
			prep = (maxHeight == checkMaxHeight) ? " and " : " but ";
			desc.append("maxHeight should be " + maxHeight + prep + "it is " + checkMaxHeight + "\n");
			
			if (notSane) {
				System.out.println(this);
				throw new RuntimeException(desc.toString());
			}
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length) time complexity.
	*/
	public int dropHeight(Piece piece, int x) {
		int[] skirt = piece.getSkirt();
		int imax = x;	// assume piece and columns contact at col x
		for (int i = 1; i < skirt.length;i ++) {
			if (getColumnHeight(imax) - getColumnHeight(i)
					< skirt[imax-x] - skirt[i]) imax = x + i;
		}
		return heights[imax] + 1 - skirt[imax-x];
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x];
	}
	
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y];
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) return true;
		return grid[x][y];
	}
	
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
		// flag !committed problem
		if (!committed) throw new RuntimeException("place commit problem");
		
		backup();
		committed = false;
			
		int result = PLACE_OK;
		
		TPoint[] body = piece.getBody();
		boolean flagClearRows = false;
		int px, py;
		for (TPoint p: body) {
			px = x + p.x;
			py = y + p.y;
			
			if (px < 0 || px >= width || py < 0 || py >= height) {
				result = PLACE_OUT_BOUNDS;
				continue;
			} else if (grid[px][py]) {
				if (result != PLACE_OUT_BOUNDS) result = PLACE_BAD;
				continue;
			}
			
			grid[px][py] = true;	// filled now
			if (widths[py] == width-1) flagClearRows = true;
			widths[py]++;
			heights[px] = Math.max(heights[px], py+1);
			maxHeight = Math.max(maxHeight, heights[px]);
		}
		
		if (flagClearRows && result == PLACE_OK) result = PLACE_ROW_FILLED;
		
		if (result <= PLACE_ROW_FILLED) sanityCheck();
		
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		
		if (committed) {
			// just in case a client calls clearRows() without calling place() first
			committed = false;
			return 0;	// since place() hasn't been called, there should be no rows clearable
		}
			
		
		int rowsCleared = 0;
		for (int j = 0; j < height; j++) {
			if (widths[j] == 0) break;	// all empty rows above from j
			if (widths[j] == width) {	// is full row, cleared
				rowsCleared++;
				widths[j] = 0;	// defensive, in case rows above j are all empty
				
				clearRow(j);
				maxHeight--;
			}
			else if (rowsCleared > 0) {	// shift down
				copyRow(j-rowsCleared, j);
				clearRow(j);
				widths[j-rowsCleared] = widths[j];
				widths[j] = 0;
			}
		}
		
		for (int i = 0; i < width; i++) {
			// heights[i]--; // wrong -> e.g. hole in between cleared row and rows below
			// find the first true grid from the top
			heights[i] = 0; // in case below for loop is not reached for any j
			for (int j = height-1; j >= 0; j--) {
				if (grid[i][j]) {
					heights[i] = j + 1;
					break;
				}
			}
			
			
		}
		
		sanityCheck();
		
		return rowsCleared;
	}



	/**
	 Reverts the board to its state before up to one place()
	 and one clearRows();
	 
	 Strategy: arraycopy() for backup, and swap main and copy for undo()
	 Implementations: 
	 (1) do backup first in place() and clearRows(), note, we should only 
	 	 perform backup if committed == true, otherwise it is meaningless to
	 	 backup for an "unsaved" state 
	 	 e.g. backup -> place() (-> NO NEED TO BACKUP HERE) -> clearRows() -> undo()
	 	 actually only place() needs to call backup(), clearRows doesn't need to
	 (2) swap the main and copy in an undo() call
	 
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		if (committed) return;
		
		boolean[][] tmpGrid = grid;
		int[] tmpWidths = widths;
		int[] tmpHeights = heights;
		int tmpMaxHeight = maxHeight;
		
		grid = xGrid;
		widths = xWidths;
		heights = xHeights;
		maxHeight = xMaxHeight;
		
		xGrid = tmpGrid;
		xWidths = tmpWidths;
		xHeights = tmpHeights;
		xMaxHeight = tmpMaxHeight;

		sanityCheck();

		commit();
	}
	
	private void backup() {
		if (!committed) return;
		for (int i = 0; i < grid.length; i++) {
			System.arraycopy(grid[i], 0, xGrid[i], 0, grid[0].length);
		}
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		System.arraycopy(heights, 0, xHeights, 0, heights.length);
		xMaxHeight = maxHeight;
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		committed = true;
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
	
	/**
	 * helper method for clearRows(), fill row j with all false
	 * @param j: row j to be cleared
	 */
	private void clearRow(int j) {
		for (int i = 0; i < width; i++) grid[i][j] = false;
	}
	
	/**
	 * helper method for clearRows(),  copy the values of row j2 into j1
	 * @param j1: row to be copied into (lower row in board)
	 * @param j2: row to be copied from (higher row in board)
	 */
	private void copyRow(int j1, int j2) {
		for (int i = 0; i < width; i++) {
			grid[i][j1] = grid[i][j2];
		}
	}
}


