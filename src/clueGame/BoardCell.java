/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * BoardCell class defines the cells that make up the board, and the specific row and column they lie in
 */
package clueGame;

public class BoardCell {
	//instance variables to define the position, row and column, of the BoardCell
	private int row;
	private int column;
	private char initial;
	
	/*
	 * Constructor that sets the row and column of the BoardCell to zero by default
	 */
	public BoardCell() {
		super();
		row = 0;
		column = 0;
	}

	/*
	 * Constructor that takes in the row and column as parameters
	 */
	public BoardCell(int row, int column) {
		super();
		this.row = row;
		this.column = column;
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
		return false;
	}
	//returns if this cell is a room or not as boolean
	public boolean isRoom() {
		return false;
	}
	//returns if this cell is a doorway or not as boolean
	public boolean isDoorway() {
		return false;
	}

	public DoorDirection getDoorDirection() {
		// TODO Auto-generated method stub
		return null;
	}

	public char getInitial() {
		// TODO Auto-generated method stub
		return initial;
	}
}
