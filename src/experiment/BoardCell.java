/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * BoardCell class defines the cells that make up the board, and the specific row and column they lie in
 */
package experiment;

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
	
}
