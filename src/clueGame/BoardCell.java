/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * BoardCell class defines the cells that make up the board, and the specific row and column they lie in
 */
package clueGame;

import java.awt.Color;
import java.awt.Graphics;

public class BoardCell {
	
	//instance variables to define the position, row and column, of the BoardCell
	private int row;
	private int cellWidth = 19;
	private int cellHeight = 19;
	private int column;
	private int startX;
	private int startY;
	
	private char initial;
	private String roomName;
	private DoorDirection doordir;
	private boolean displayName = false;
	private boolean isTarget = false;

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
	 * Returns whether or not the board cell is going to display the room name
	 */
	public boolean isDisplayName() {
		return displayName;
	}

	/*
	 * Sets whether the board is going to display the room name or not
	 */
	public void setDisplayName(boolean displayName) {
		this.displayName = displayName;
	}
	
	/*
	 * Sets the room name of the cell assumming it will print the room name
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	
	/*
	 * Returns the room name of the board cell assuming it will print the name
	 */
	public String getRoomName() {
		return roomName;
	}
	
	/*
	 * returns the cell width of the board cell
	 */
	public int getCellWidth() {
		return cellWidth;
	}
	
	/*
	 * Returns the cell height of the board cell
	 */
	public int getCellHeight() {
		return cellHeight;
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
	
	/*
	 * Draw function that is called by the board so each cell can be printed in the GUI
	 */
	public void draw(Graphics g) {
		//Calculates the pixel placement of each boardCell
		startY = row * cellHeight;
		startX = column * cellWidth;
		
		//If the cell is a walkway, it is printed yellow with a border
		if(isWalkway()) {
			g.setColor(Color.YELLOW);
			if(isTarget) {
				g.setColor(Color.CYAN);
			}
			g.fillRect(startX, startY, cellWidth, cellHeight);
			g.setColor(Color.BLACK);
			g.drawRect(startX, startY, cellWidth, cellHeight);
		}
		//If the cell has been chosen in the config files to print the room name, it prints it
		else if(isDisplayName()) {
			g.setColor(Color.BLUE);
			g.drawString(getRoomName(), startX, startY);
		}
		//If the cell is a door, it prints a blue line where the door is
		else if(isDoorway()) {
			if(isTarget) {
				g.setColor(Color.CYAN);
				g.fillRect(startX, startY, cellWidth, cellHeight);
				g.setColor(Color.BLACK);
				g.drawRect(startX, startY, cellWidth, cellHeight);
			}
			g.setColor(Color.BLUE);
			switch (getDoorDirection()) {
			case UP:
				g.fillRect(startX, startY, cellWidth, 4);
				break;
			case LEFT:
				g.fillRect(startX, startY, 4, cellHeight);
				break;
			case RIGHT:
				g.fillRect(startX + cellWidth - 4, startY, 4, cellHeight);
				break;
			case DOWN:
				g.fillRect(startX, startY + cellHeight - 4, cellWidth, 4);
				break;
			}
		}
	}
	
	/*
	 * Returns the x location of the upper left corner of the board cell
	 */
	public int getStartX() {
		return startX;
	}
	
	/*
	 * Returns the y location of the upper left corner of the board cell
	 */
	public int getStartY() {
		return startY;
	}
	
	/*
	 * Sets whether or not the board cell is a target, to be later displayed on the board
	 */
	public void setIsTarget(boolean targetValue) {
		isTarget = targetValue;
	}
	
	/*
	 * Returns whether or not a boardCell is currently a highlighted target to be displayed on the board
	 */
	public boolean getIsTarget() {
		return isTarget;
	}
}
