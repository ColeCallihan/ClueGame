/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * BoardCell class defines the cells that make up the board, and the specific row and column they lie in
 */
package clueGame;

public class BoardCell {
	
	//instance variables to define the position, row and column, of the BoardCell
	private int row;
	private int column;
	private char initial;
	private DoorDirection doordir;
	
	/*
	 * Constructor that sets the row and column of the BoardCell to zero by default
	 */
	public BoardCell() {
		super();
		row = 0;
		column = 0;
		doordir = DoorDirection.NONE;
	}

	/*
	 * Constructor that takes in the row and column as parameters
	 */
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
		doordir = DoorDirection.NONE;
	}
	
	/*
	 * Constructor that takes in row, column, and BoardCell letter
	 */
	public BoardCell(int row, int column, char initial) {
		super();
		this.row = row;
		this.column = column;
		this.initial = initial;
		doordir = DoorDirection.NONE;
	}
	
	/*
	 * Constructor that also reads in door direction
	 */
	public BoardCell(int row, int column, char initial, DoorDirection doordir) {
		super();
		this.row = row;
		this.column = column;
		this.initial = initial;
		this.doordir = doordir;
	}

	/*
	 * Getter method to return the row of the BoardCell
	 */
	public int getRow() {
		return row;
	}

	/*
	 * Getter method to return the column of the BoardCell
	 */
	public int getColumn() {
		return column;
	}
	
	//returns if this cell is a walkway or not as boolean
	public boolean isWalkway() {
		if(initial == 'W') {
			return true;
		}
		else {
			return false;
		}
	}
	
	//returns if this cell is a room or not as boolean
	public boolean isRoom() {
		if(initial == 'W') {
			return false;
		}
		else if (isDoorway()){
			return false;
		}
		else {
			return true;
		}
	}
	
	//returns if this cell is a doorway or not as boolean
	public boolean isDoorway() {
		if(doordir == DoorDirection.NONE) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/*
	 * returns the door direction of the cell
	 */
	public DoorDirection getDoorDirection() {
		return doordir;
	}

	/*
	 * returns the initial of the cell
	 */
	public char getInitial() {
		return initial;
	}
	
	/*
	 * equals statement so that the testing can compared BoardCells
	 */
	public boolean equals(BoardCell compareCell) {
		if(compareCell.getInitial() == (this.initial) && compareCell.getRow() == this.row && compareCell.getColumn() == this.column) {
			return true;
		}
		return false;
	}
}
