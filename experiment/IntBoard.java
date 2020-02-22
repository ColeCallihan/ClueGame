package experiment;
import java.util.*;

public class IntBoard {
	Set<BoardCell> myCells = new HashSet<BoardCell>();
	private BoardCell[][] grid;
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	
	public void calcAdjacencies() {
		
	}
	public Set<BoardCell> getAdjList(BoardCell ohBoi){
		return myCells;
	}
	public void calcTargets(BoardCell startCell, int pathLength) {
		
	}
	public Set<BoardCell> getTargets(){
		return null;
	}
	public IntBoard(Set<BoardCell> myCells) {
		super();
		this.myCells = myCells;
		calcAdjacencies();
	}
	public BoardCell getCell(int row, int col) {
		return grid[row][col];
	}
}
