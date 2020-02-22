package experiment;
import java.util.*;

public class IntBoard {//Test
	Set<BoardCell> myCells = new HashSet<BoardCell>();
	Map<BoardCell, Set<BoardCell>> adjacencyList = new HashMap<BoardCell, Set<BoardCell>>();
	private BoardCell[][] grid = new BoardCell[4][4];
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
	public void calcAdjacencies() {
		Set<BoardCell> adjacentCellSet;
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
	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjacencyList.get(cell);
	}
	public void calcTargets(BoardCell startCell, int pathLength) {
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		visited.add(startCell);
		findAllTargets(startCell, pathLength);
	}
	public Set<BoardCell> findAllTargets(BoardCell thisCell, int numSteps){
		for(BoardCell adjacent : getAdjList(thisCell)) {
			if(visited.contains(adjacent) != true) {
				visited.add(adjacent);
				if(numSteps == 1) {
					targets.add(adjacent);
				}
				else {
					findAllTargets(adjacent, numSteps - 1);
				}
				visited.remove(adjacent);
			}
		}
		return targets;
	}
	public Set<BoardCell> getTargets(){
		return targets;
	}
	public IntBoard(Set<BoardCell> myCells) {
		super();
		this.myCells = myCells;
		for(BoardCell cell : this.myCells) {
			grid[cell.getRow()][cell.getColumn()] = cell;
		}
		calcAdjacencies();
	}
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
