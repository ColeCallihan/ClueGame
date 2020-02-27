/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * Board class to run it all
 */ 
package clueGame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class Board {
	private int numRows;
	private int numColumns;
	public final int MAX_BOARD_SIZE = 50;
	private BoardCell[][] board;
	private Map<Character, String> legend = new HashMap<Character, String>();
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
	private Set<BoardCell> myCells = new HashSet<BoardCell>();
	private Set<BoardCell> visited = new HashSet<BoardCell>();
	private Set<BoardCell> targets = new HashSet<BoardCell>();
	private String boardConfigFile;
	private String roomConfigFile;

	// variable used for singleton pattern
	private static Board theInstance = new Board();

	// constructor is private to ensure only one can be created
	private Board() {
		super();

		//initialize();
		//calcAdjacencies();

	}

	/*
	 * Returns the instance of the board. It is static which means it can be called without specifying the Board.
	 */
	public static Board getInstance() {
		return theInstance;
	}

	public void initialize() {

		try {
			loadRoomConfig();
			loadBoardConfig();

		}catch(BadConfigFormatException e) {

		}catch(FileNotFoundException e) {

		}
		calcAdjacencies();
	}

	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader inRoom = new FileReader(roomConfigFile);
		Scanner legendInfo = new Scanner(inRoom);

		while(legendInfo.hasNextLine()) {
			String currentRoom = legendInfo.nextLine();
			String[] roomDetails = currentRoom.split(", ");
			
			if(roomDetails[2].equals("Card") || roomDetails[2].equals("Other")) {
				char letter = roomDetails[0].charAt(0);
				legend.put(letter, roomDetails[1]);
			}
			else {
				throw new BadConfigFormatException("Incorrect legend format");
			}
		}
	}

	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{
		FileReader inputFileForCalc = new FileReader(boardConfigFile);
		Scanner rowColBoard = new Scanner(inputFileForCalc);


		int calcRows = 0;
		int calcCols = 0;
		while(rowColBoard.hasNextLine()) {
			calcRows++;

			String currentRow = rowColBoard.nextLine();
			String[] rowLayout = currentRow.split(",");

			calcCols = rowLayout.length;
		}

		board = new BoardCell[calcRows][calcCols];
		numRows = calcRows;
		numColumns = calcCols;

		FileReader inputFile = new FileReader(boardConfigFile);
		Scanner inBoard = new Scanner(inputFile);

		int rows = 0;
		int numColsFirstRow = 0;
		while(inBoard.hasNextLine()) {
			String currentRow = inBoard.nextLine();
			String[] rowLayout = currentRow.split(",");

			if(rows == 0) {
				numColsFirstRow = rowLayout.length;
			}

			for(int i = 0; i < rowLayout.length; i++) {
				if(rowLayout.length != numColsFirstRow) {
					throw new BadConfigFormatException("Incorrect number of columns in row " + ++rows);
				}				
				else if(legend.containsKey(rowLayout[i].charAt(0))) {
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
							BoardCell currentCell4 = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.NONE);
							board[rows][i] = currentCell4;
							myCells.add(currentCell4);
						}
					}
					else {
						BoardCell currentCell = new BoardCell(rows, i, rowLayout[i].charAt(0), DoorDirection.NONE);
						board[rows][i] = currentCell;
						myCells.add(currentCell);
					}
					numColumns = i + 1;
				}
				else {
					throw new BadConfigFormatException("Room not in legend");
				}
			}

			rows++;
			numRows = rows;
		}
	}

	/*
	 * Sets the adjacency matrix by calculating BoardCells that are adjacent to each BoardCell
	 */
	public void calcAdjacencies() {
		Set<BoardCell> adjacentCellSet;//Creates uninstantiated set to hold the current BoardCell's adjacent cells
		for(BoardCell currentCell : myCells) {
			adjacentCellSet = new HashSet<BoardCell>();
			//Cell above
			if(currentCell.getRow() > 0) {
				adjacentCellSet.add(board[currentCell.getRow() - 1][currentCell.getColumn()]);
			}
			//Cell below
			if(currentCell.getRow() < 3) {
				adjacentCellSet.add(board[currentCell.getRow() + 1][currentCell.getColumn()]);
			}
			//Cell left
			if(currentCell.getColumn() > 0) {
				adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() - 1]);
			}
			//Cell right
			if(currentCell.getColumn() < 3) {
				adjacentCellSet.add(board[currentCell.getRow()][currentCell.getColumn() + 1]);
			}

			adjMatrix.put(currentCell, adjacentCellSet);//Adds the adjacency list and the current cell to the adjacency map
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
	 * returns the adjacency list of the BoardCell passed in as a parameter
	 */
	public Set<BoardCell> getAdjList(BoardCell cell){
		return adjMatrix.get(cell);
	}

	/*
	 * Sets config file names to the names passed in
	 */
	public void setConfigFiles(String string, String string2){
		boardConfigFile = string;
		roomConfigFile = string2;
	}

	public Map<Character, String> getLegend() {
		return legend;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}
}
