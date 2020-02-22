/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * IntBoard class that holds and manages the properties of a board of BoardCells including their adjacency and movement targets
 */
package experiment;
import java.util.*;

public class IntBoard {
	Set<BoardCell> myCells = new HashSet<BoardCell>();//defines a list of all of the BoardCells that will be in the IntBoard
	Map<BoardCell, Set<BoardCell>> adjacencyList = new HashMap<BoardCell, Set<BoardCell>>();//Map defining the adjacent BoardCells to each BoardCell
	private BoardCell[][] grid = new BoardCell[4][4];//2D matrix to hold all of the BoardCells
	//Instance variables to hold sets of the current targets of a BoardCell and the current visited BoardCells used in calculating targets
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
	/*
	 * Method that calculates the adjacencies of every BoardCell in the IntBoard
	 */
	public void calcAdjacencies() {
		Set<BoardCell> adjacentCellSet;//Creates uninstantiated set to hold the current BoardCell's adjacent cells
		for(BoardCell currentCell : myCells) {
			adjacentCellSet = new HashSet<BoardCell>();
			//Cell above
			if(currentCell.getRow() > 0) {
				adjacentCellSet.add(grid[currentCell.getRow() - 1][currentCell.getColumn()]);
			}
			//Cell below
			if(currentCell.getRow() < 3) {
				adjacentCellSet.add(grid[currentCell.getRow() + 1][currentCell.getColumn()]);
			}
			//Cell left
			if(currentCell.getColumn() > 0) {
				adjacentCellSet.add(grid[currentCell.getRow()][currentCell.getColumn() - 1]);
			}
			//Cell right
			if(currentCell.getColumn() < 3) {
				adjacentCellSet.add(grid[currentCell.getRow()][currentCell.getColumn() + 1]);
			}

			adjacencyList.put(currentCell, adjacentCellSet);//Adds the adjacency list and the current cell to the adjacency map
		}
	}
	/*
	 * returns the adjacency list of the BoardCell passed in as a parameter
	 */
	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjacencyList.get(cell);
	}
	/*
	 * instantiates the sets that will hold the visited and target cells, then calls findAllTargets to determine the targets
	 */
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	/*
	 * Recursive method that iterates through the adjacent cells of the passed in cell and adds them to the targets list if they have
	 * not been visited before and they are the last space to travel with when numSteps is equal to 1
	 */
	public Set<BoardCell> findAllTargets(BoardCell thisCell, int numSteps){
		for(BoardCell adjacent : getAdjList(thisCell)) {
			if(visited.contains(adjacent) != true) {//If the cell has not been visited before, check to see if it is a target
				visited.add(adjacent);
				if(numSteps == 1) {//If it is on the last step, add the adjacent tile to the target set
					targets.add(adjacent);
				}
				else {
					findAllTargets(adjacent, numSteps - 1);//check the adjacent tiles from that adjacent tile with one less step
				}
				visited.remove(adjacent);//remove the current adjacent tile because it was only a temporary step
			}
		}
		return targets;//returns the target list/set
	}
	/*
	 * Returns the target list
	 */
	public Set<BoardCell> getTargets(){
		return targets;
	}
	/*
	 * Constructor for IntBoard with a set of BoardCells passed in as a parameter. This also
	 * defines every position in the grid with each cell in the passed in set, then calculates and
	 * defines the adjacency list for the new board.
	 */
	public IntBoard(Set<BoardCell> myCells) {
		super();
		this.myCells = myCells;
		for(BoardCell cell : this.myCells) {
			grid[cell.getRow()][cell.getColumn()] = cell;
		}
		calcAdjacencies();
	}
	/*
	 * Getter method to return the cell at the provided row and column from the grid.
	 */
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
