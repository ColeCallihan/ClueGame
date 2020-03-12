/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * Board class to instantiate all aspects of the board
 */ 
package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {

	//instance variables for integers that keep track of the board columns, rows, and max board size
	private int numRows;
	private int numColumns;
	public final int MAX_BOARD_SIZE = 50;

	//initial 2D array of BoardCells
	private BoardCell[][] board;

	//Instantiates the legend map and the adjacent matrix map
	private Map<Character, String> legend = new HashMap<Character, String>();
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();

	//instantiates the set containing all of the BoardCells, the set containing visited cells for calc adjacencies and the set containing the target cells for calc targets
	private Set<BoardCell> myCells = new HashSet<BoardCell>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();

	//name of board file to read in and of room/legend file to read in
	private String boardConfigFile;
	private String roomConfigFile;

	// variable used for singleton pattern
	private static Board theInstance = new Board();

	// constructor is private to ensure only one can be created
	private Board() {
		super();
	}

	/*
	 * Returns the instance of the board. It is static which means it can be called without specifying the Board.
	 */
	public static Board getInstance() {
		return theInstance;
	}

	/*
	 * initialize method reads in the room and board files, defining the board and fills the adjacencies and target sets and maps
	 */
	public void initialize() {

		try {
			loadRoomConfig();
			loadBoardConfig();

		}catch(BadConfigFormatException e) {

			System.out.println(e);
		}catch(FileNotFoundException e) {

			System.out.println(e);
		}
		calcAdjacencies();
	}

	/*
	 * reads in the legend file and storing each room type into the legend map
	 */
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader inRoom = new FileReader(roomConfigFile);
		Scanner legendInfo = new Scanner(inRoom);

		char letter = ' ';
		while(legendInfo.hasNextLine()) {
			String currentRoom = legendInfo.nextLine();
			String[] roomDetails = currentRoom.split(", ");//splits the file line entries into an array

			if(roomDetails[2].equals("Card") || roomDetails[2].equals("Other")) {//makes sure the room is a valid room
				letter = roomDetails[0].charAt(0);
				legend.put(letter, roomDetails[1]);
			}
			else {
				throw new BadConfigFormatException("Incorrect legend format");//if the legend input file is not formatted correctly, it throws back to initialize
			}
		}
	}

	/*
	 * reads in the board .csv file and fills the board with newly created BoardCells
	 */
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader inputFileForCalc = new FileReader(boardConfigFile);
		Scanner rowColBoard = new Scanner(inputFileForCalc);

		int calcRows = 0;
		int calcCols = 0;
		//reads through the file to figure out how many columns and rows there are to instantiate the board 2D array
		while(rowColBoard.hasNextLine()) {
			calcRows++;

			String currentRow = rowColBoard.nextLine();
			String[] rowLayout = currentRow.split(",");

			calcCols = rowLayout.length;
		}

		board = new BoardCell[calcRows][calcCols];
		numRows = calcRows;
		numColumns = calcCols;
		
		//resets the file reader
		FileReader inputFile = new FileReader(boardConfigFile);
		Scanner inBoard = new Scanner(inputFile);

		//iterates through the board .csv and adds a BoardCell to the board for each cell label
		int rows = 0;
		int numColsFirstRow = 0;
		while(inBoard.hasNextLine()) {
			String currentRow = inBoard.nextLine();
			String[] rowLayout = currentRow.split(",");

			if(rows == 0) {
				numColsFirstRow = rowLayout.length;
			}

			for(int i = 0; i < rowLayout.length; i++) {
				//if the current row is not equal to the number of rows in the beginning, throws error back up to initialize
				if(rowLayout.length != numColsFirstRow) {
					throw new BadConfigFormatException("Incorrect number of columns in row " + ++rows);
				}		
				//otherwise if the row is valid and it has a label
				else if(legend.containsKey(rowLayout[i].charAt(0))) {
					//if the label has a door direction attached
					if(rowLayout[i].length() == 2) {
						switch(rowLayout[i].charAt(1)) {
						case 'D':
							BoardCell currentCell = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.DOWN);
							board[rows][i] = currentCell;
							myCells.add(currentCell);
							break;
						case 'U':
							BoardCell currentCell1 = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.UP);
							board[rows][i] = currentCell1;
							myCells.add(currentCell1);
							break;
						case 'R':
							BoardCell currentCell2 = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.RIGHT);
							board[rows][i] = currentCell2;
							myCells.add(currentCell2);
							break;
						case 'L':
							BoardCell currentCell3 = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.LEFT);
							board[rows][i] = currentCell3;
							myCells.add(currentCell3);
							break;
						default:
							//the door direction is not a direction, meaning it is N, ignore it
							BoardCell currentCell4 = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.NONE);
							board[rows][i] = currentCell4;
							myCells.add(currentCell4);
						}
					}
					//adds the normal BoardCell to the board
					else {
						BoardCell currentCell = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.NONE);
						board[rows][i] = currentCell;
						myCells.add(currentCell);
					}
					numColumns = i + 1;
				}
				//else the room type was not in the legend, so throw an error
				else {
					throw new BadConfigFormatException("Room not in legend");
				}
			}
			//instantiate the total number of rows in the board
			rows++;
			numRows = rows;
		}
	}

	/*
	 * Sets the adjacency matrix by calculating BoardCells that are adjacent to each BoardCell
	 */
	public void calcAdjacencies() {
		//Creates uninstantiated set to hold the current BoardCell's adjacent cells
		Set<BoardCell> adjacentCellSet;
		for(BoardCell currentCell : myCells) {
			adjacentCellSet = new HashSet<BoardCell>();
			//If the cell is a doorway, it needs to only test adjacencies to the cell corresponding to the door direction of the door
			if(currentCell.isDoorway()) {
				DoorDirection currentDirection = currentCell.getDoorDirection();

				if(currentDirection == DoorDirection.UP) {
					//Cell above
					if(currentCell.getRow() > 0) {
						if(board[currentCell.getRow() - 1][currentCell.getColumn()].isWalkway()) {
							adjacentCellSet.add(board[currentCell.getRow() - 1][currentCell.getColumn()]);
						}
						else if(board[currentCell.getRow() - 1][currentCell.getColumn()].isDoorway()) {
							if(board[currentCell.getRow() - 1][currentCell.getColumn()].getDoorDirection() == DoorDirection.DOWN) {
								adjacentCellSet.add(board[currentCell.getRow() - 1][currentCell.getColumn()]);
							}
						}
					}
				}
				else if(currentDirection == DoorDirection.DOWN) {
					//Cell below
					if(currentCell.getRow() < (numRows - 1)) {
						if(board[currentCell.getRow() + 1][currentCell.getColumn()].isWalkway()) {
							adjacentCellSet.add(board[currentCell.getRow() + 1][currentCell.getColumn()]);
						}
						else if(board[currentCell.getRow() + 1][currentCell.getColumn()].isDoorway()) {
							if(board[currentCell.getRow() + 1][currentCell.getColumn()].getDoorDirection() == DoorDirection.UP) {
								adjacentCellSet.add(board[currentCell.getRow() + 1][currentCell.getColumn()]);
							}
						}
					}
				}
				else if(currentDirection == DoorDirection.LEFT) {
					//Cell left
					if(currentCell.getColumn() > 0) {
						if(board[currentCell.getRow()][currentCell.getColumn() - 1].isWalkway()) {
							adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() - 1]);
						}
						else if(board[currentCell.getRow()][currentCell.getColumn() - 1].isDoorway()) {
							if(board[currentCell.getRow()][currentCell.getColumn() - 1].getDoorDirection() == DoorDirection.RIGHT) {
								adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() - 1]);
							}
						}
					}
				}
				else if(currentDirection == DoorDirection.RIGHT){
					//Cell right
					if(currentCell.getColumn() < (numColumns- 1)) {
						if(board[currentCell.getRow()][currentCell.getColumn() + 1].isWalkway()) {
							adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() + 1]);
						}
						else if(board[currentCell.getRow()][currentCell.getColumn() + 1].isDoorway()) {
							if(board[currentCell.getRow()][currentCell.getColumn() + 1].getDoorDirection() == DoorDirection.LEFT) {
								adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() + 1]);
							}
						}
					}
				}
			}
			//it is a normal walkway cell
			else if (currentCell.isWalkway()) {
				//Cell above
				if(currentCell.getRow() > 0) {
					if(board[currentCell.getRow() - 1][currentCell.getColumn()].isWalkway()) {
						adjacentCellSet.add(board[currentCell.getRow() - 1][currentCell.getColumn()]);
					}
					else if(board[currentCell.getRow() - 1][currentCell.getColumn()].isDoorway()) {
						if(board[currentCell.getRow() - 1][currentCell.getColumn()].getDoorDirection() == DoorDirection.DOWN) {
							adjacentCellSet.add(board[currentCell.getRow() - 1][currentCell.getColumn()]);
						}
					}
				}
				//Cell below
				if(currentCell.getRow() < (numRows - 1)) {
					if(board[currentCell.getRow() + 1][currentCell.getColumn()].isWalkway()) {
						adjacentCellSet.add(board[currentCell.getRow() + 1][currentCell.getColumn()]);
					}
					else if(board[currentCell.getRow() + 1][currentCell.getColumn()].isDoorway()) {
						if(board[currentCell.getRow() + 1][currentCell.getColumn()].getDoorDirection() == DoorDirection.UP) {
							adjacentCellSet.add(board[currentCell.getRow() + 1][currentCell.getColumn()]);
						}
					}
				}
				//Cell left
				if(currentCell.getColumn() > 0) {
					if(board[currentCell.getRow()][currentCell.getColumn() - 1].isWalkway()) {
						adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() - 1]);
					}
					else if(board[currentCell.getRow()][currentCell.getColumn() - 1].isDoorway()) {
						if(board[currentCell.getRow()][currentCell.getColumn() - 1].getDoorDirection() == DoorDirection.RIGHT) {
							adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() - 1]);
						}
					}
				}
				//Cell right
				if(currentCell.getColumn() < (numColumns- 1)) {
					if(board[currentCell.getRow()][currentCell.getColumn() + 1].isWalkway()) {
						adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() + 1]);
					}
					else if(board[currentCell.getRow()][currentCell.getColumn() + 1].isDoorway()) {
						if(board[currentCell.getRow()][currentCell.getColumn() + 1].getDoorDirection() == DoorDirection.LEFT) {
							adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() + 1]);
						}
					}
				}
			}
			//Adds the current adjacency set to the adjacency matrix in case the cell was a room and it has no adjacencies
			//If it wasn't a room, it is just re-added to the map, not changing any values
			adjMatrix.put(currentCell, adjacentCellSet);
		}
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
			//If the cell has not been visited before, check to see if it is a target
			if(visited.contains(adjacent) != true) {
				visited.add(adjacent);
				//If it is on the last step, add the adjacent tile to the target set
				if(numSteps == 1 || adjacent.isDoorway()) {
					targets.add(adjacent);
				}
				else {
					findAllTargets(adjacent, numSteps - 1);
					//check the adjacent tiles from that adjacent tile with one less step
				}
				visited.remove(adjacent);
				//remove the current adjacent tile because it was only a temporary step
			}
		}
		return targets;
		//returns the target list/set
	}

	/*
	 * returns the adjacency list of the BoardCell passed in as a parameter
	 */
	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjMatrix.get(cell);
	}

	/*
	 * Sets config file names to the names passed in
	 */
	public void setConfigFiles(String string, String string2){
		boardConfigFile = "./data/" + string;
		roomConfigFile = "./data/" + string2;
	}

	/*
	 * Returns the legend map
	 */
	public Map<Character, String> getLegend() {
		return legend;
	}

	/*
	 * Returns the number of rows in the board
	 */
	public int getNumRows() {
		return numRows;
	}

	/*
	 * Returns the number of columns in the board
	 */
	public int getNumColumns() {
		return numColumns;
	}

	/*
	 * Returns the BoardCell at the provided row and column
	 */
	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}

	/*
	 * Returns the adjacency list
	 */
	public Set<BoardCell> getAdjList(int i, int j) {
		return adjMatrix.get(getCellAt(i, j));
	}

	/*
	 * Calculates the targets corresponding to the cell at row i, column j, and moves k
	 */
	public void calcTargets(int i, int j, int k) {
		calcTargets(getCellAt(i, j), k);
	}

	/*
	 * returns the set of targets corresponding to the last calcTargets cell
	 */
	public Set<BoardCell> getTargets() {
		return targets;
	}

}
