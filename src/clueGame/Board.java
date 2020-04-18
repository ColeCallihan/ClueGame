/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * Board class to instantiate all aspects of the board
 */ 
package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Board extends JPanel{

	//Buttons
	private JButton submit;
	private JButton cancel;
	
	//instance variables for integers that keep track of the board columns, rows, and max board size
	private int numRows;
	private int numColumns;
	public final int MAX_BOARD_SIZE = 50;

	//instance variables to keep track of the current player, guess, roll, and the result to answer the current guess
	private int currentPlayer = -1;//-1 ensure at the start of the first turn, it always starts on player 0
	private int currentRoll;
	private Solution currentGuess;
	private Card currentGuessResult;

	//instance variables of lists of players and cards
	private ArrayList<Player> players = new ArrayList<Player>();
	private ArrayList<Card> deck = new ArrayList<Card>();
	private ArrayList<Card> tempDeck = deck;
	private ArrayList<Card> returnDeck = new ArrayList<Card>();
	private ArrayList<Card> playerCards = new ArrayList<Card>();
	private ArrayList<Card> weaponCards = new ArrayList<Card>();
	private ArrayList<Card> roomCards = new ArrayList<Card>();

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
	private String playerConfigFile;
	private String cardConfigFile;
	public boolean playerGuessMade = false;
	public static Solution theAnswer;

	// variable used for singleton pattern
	private static Board theInstance = new Board();

	// constructor is private to ensure only one can be created
	private Board() {
		super();
		addMouseListener(new TargetListener());
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
			loadCards();
			loadPeople();
			dealCards();

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
							createCellAddToMap(rows, rowLayout, i, DoorDirection.DOWN);
							break;
						case 'U':
							createCellAddToMap(rows, rowLayout, i, DoorDirection.UP);
							break;
						case 'R':
							createCellAddToMap(rows, rowLayout, i, DoorDirection.RIGHT);
							break;
						case 'L':
							createCellAddToMap(rows, rowLayout, i, DoorDirection.LEFT);
							break;
						case 'N':
							createCellAddToMap(rows, rowLayout, i, DoorDirection.NONE);
							board[rows][i].setDisplayName(true);
							board[rows][i].setRoomName(legend.get(board[rows][i].getInitial()));
							break;
						default:
							//the door direction is not a direction, meaning it is N, ignore it
							createCellAddToMap(rows, rowLayout, i, DoorDirection.NONE);
						}
					}
					//adds the normal BoardCell to the board
					else {
						createCellAddToMap(rows, rowLayout, i, DoorDirection.NONE);
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
	 * Creates a cell with the given parameters and adds it to the list of cells and the board
	 */
	public void createCellAddToMap(int rows, String[] rowLayout, int i, DoorDirection direction) {
		BoardCell currentCell = new BoardCell(rows, i, rowLayout[i].charAt(0), direction);
		board[rows][i] = currentCell;
		myCells.add(currentCell);
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
		boardConfigFile = "./src/data/" + string;
		roomConfigFile = "./src/data/" + string2;
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

	/*
	 * Sets the config files to the corresponding file names
	 */
	public void setConfigFiles(String string, String string2, String string3, String string4) {
		boardConfigFile = "./src/data/" + string;
		roomConfigFile = "./src/data/" + string2;
		playerConfigFile = "./src/data/" + string3;
		cardConfigFile = "./src/data/" + string4;
	}

	/*
	 * Reads in the board people file and creates new players to send to the board's list of players
	 */
	public void loadPeople() throws FileNotFoundException{
		FileReader inPlayers = new FileReader(playerConfigFile);
		Scanner playerInfo = new Scanner(inPlayers);

		//reads through the file and separates the player info into making new players
		while(playerInfo.hasNextLine()) {
			String currentPlayer = playerInfo.nextLine();
			String[] playerDetails = currentPlayer.split(",");//splits the file line entries into an array

			String playerName = playerDetails[0];
			Color playerColor = convertColor(playerDetails[1].toUpperCase());
			String playerStatus = playerDetails[2];
			int playerRow = Integer.parseInt(playerDetails[3]);
			int playerColumn = Integer.parseInt(playerDetails[4]);

			Player newPlayer;

			if(playerStatus.equals("Human")) {
				newPlayer = new HumanPlayer(playerName, playerColor, playerStatus, playerRow, playerColumn);
			}
			else {
				newPlayer = new ComputerPlayer(playerName, playerColor, playerStatus, playerRow, playerColumn);
			}
			players.add(newPlayer);
		}
	}

	/*
	 * Reads in the card config file and creates new cards to add to the board deck
	 */
	public void loadCards() throws FileNotFoundException, BadConfigFormatException {
		FileReader inCards = new FileReader(cardConfigFile);
		Scanner cardInfo = new Scanner(inCards);

		//Initializing the current card type to person just so there isn't a null pointer when a new card is added to the deck
		//These statements assume the config file is formatted correctly
		String cardEnum = cardInfo.nextLine();
		CardType currentType = CardType.PERSON;
		if(cardEnum.equals("PEOPLE")) {
			currentType = CardType.PERSON;
		}
		else {
			throw new BadConfigFormatException("Card file not formatted correctly");
		}

		//Depending on the type of card, the card gets added to the respective card list
		while(cardInfo.hasNextLine()) {
			String currentCard = cardInfo.nextLine();
			if(currentCard.equals("")) {
				String nextType = cardInfo.nextLine();
				if(nextType.equals("WEAPONS")) {
					currentType = CardType.WEAPON;
				}
				else if(nextType.equals("ROOMS")) {
					currentType = CardType.ROOM;
				}
				else {
					throw new BadConfigFormatException("Card file not formatted correctly");
				}
				currentCard = cardInfo.nextLine();
			}

			//determines the type of card and adds it to its corresponding type list
			Card newCard = new Card(currentCard, currentType);
			if(currentType.equals(CardType.PERSON)) {
				playerCards.add(newCard);
			}
			else if(currentType.equals(CardType.WEAPON)) {
				weaponCards.add(newCard);
			}
			else if(currentType.equals(CardType.ROOM)) {
				roomCards.add(newCard);
			}
			else {
				throw new BadConfigFormatException("Card type in card file not valid");
			}
			deck.add(newCard);
		}
		returnDeck = (ArrayList<Card>) deck.clone();
	}

	/*
	 * Deals out cards to each player, holding 3 for the solution
	 */
	public void dealCards() {
		ArrayList<Card> shuffledDeck = new ArrayList<Card>();

		selectAnswer();
		//To get solution, take random card from each card type list and put into solution
		shuffledDeck = tempDeck;

		//Gives each player a card
		int i = 0;
		int max = shuffledDeck.size();
		for(int j = 0; j < max; j++) {
			Random rand = new Random();
			int random = rand.nextInt(shuffledDeck.size());
			players.get(i).addCard(shuffledDeck.get(random));
			i += 1;
			i %= players.size();
			shuffledDeck.remove(shuffledDeck.get(random));
		}
	}

	/*
	 * Returns the board deck of cards
	 */
	public ArrayList<Card> getDeck() {
		return returnDeck;
	}

	/*
	 * Returns the board's list of players in the game
	 */
	public ArrayList<Player> getPlayers() {
		return players;
	}

	// Be sure to trim the color, we don't want spaces around the name
	public Color convertColor(String strColor) {
		Color color; 
		try {
			// We can use reflection to convert the string to a color
			Field field =Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color)field.get(null);
		} catch (Exception e) {  
			color = null; // Not defined  
		}
		return color;
	}

	/*
	 * Returns the number of weapons in the card deck
	 */
	public Integer getWeaponsCount() {
		return weaponCards.size();
	}

	/*
	 * Returns the number of players in the card deck
	 */
	public Integer getPlayersCount() {
		return playerCards.size();
	}

	/*
	 * Returns the number of rooms in the card deck
	 */
	public Integer getRoomsCount() {
		return roomCards.size();
	}

	/*
	 * Returns the list of weapons in the card deck
	 */
	public ArrayList<Card> getWeaponCards() {
		return weaponCards;
	}

	/*
	 * Returns the list of players in the card deck
	 */
	public ArrayList<Card> getPlayerCards() {
		return playerCards;
	}

	/*
	 * Returns the list of rooms in the card deck
	 */
	public ArrayList<Card> getRoomCards() {
		return roomCards;
	}

	/*
	 * Sets the list of players to a given list of players
	 */
	public void setPlayers(ArrayList<Player> tempPlayers) {
		players = tempPlayers;
	}

	/*
	 * Asks each player if they can disprove the solution presented by the player passed in
	 */
	public Card handleSuggestion(Solution solution, Player player1) {
		int currentPlayerIndex = players.indexOf(player1);
		currentGuess = solution;
		Card answer = null;

		//iterates to each player until either every player was tested or a player has a card to disprove the solution
		while(true){
			currentPlayerIndex++;
			currentPlayerIndex %= players.size();
			if(currentPlayerIndex == players.indexOf(player1)) {
				//Change result to null if no one can disprove
				currentGuessResult = null;
				return null;
			}
			else if(players.get(currentPlayerIndex).disproveSuggestion(solution) != null) {
				answer = players.get(currentPlayerIndex).disproveSuggestion(solution);
				currentGuessResult = answer;
				return answer;
			}
			else
			{
				currentGuessResult = null;
			}
		}
	}

	/*
	 * Returns whether or not the accusation matched the solution
	 */
	public boolean checkAccusation(Solution testSolution) {
		//If it is true
		if(Board.theAnswer.person.equals(testSolution.person) && Board.theAnswer.weapon.equals(testSolution.weapon) && Board.theAnswer.room.equals(testSolution.room)) {
			
			//Making dialog for win screen
			JDialog winScreen = new JDialog();
			JPanel panel = new JPanel();
			JLabel win = new JLabel("Congratulations! " + getCurrentPlayer().getName() + " has figured out the solution!");
			JButton okWin = new JButton("OK!");
			
			winScreen.setSize(new Dimension(500, 625));
			
			panel.add(win);
			panel.add(okWin);
			winScreen.add(panel);
			winScreen.setTitle("Game Over!");
			
			//Add action listener to end the game
			class WinButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
			okWin.addActionListener(new WinButtonListener());
			winScreen.setVisible(true);

			
			return true;
		}
		else {
			//If they are incorrect, continue game but let them know
			JDialog incorrectScreen = new JDialog();
			JPanel panel = new JPanel();
			JLabel lose = new JLabel("Sorry that is not the correct solution.");
			JButton okLose = new JButton("OK!");
			
			incorrectScreen.setSize(new Dimension(500, 625));
			
			panel.add(lose);
			panel.add(okLose);
			incorrectScreen.add(panel);
			incorrectScreen.setTitle("Incorrect Accusation");
			
			//Making sure they know they are a loser
			class LoseButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					getCurrentPlayer().setStartTurn(false);
					incorrectScreen.setVisible(false);
				}
			}
			okLose.addActionListener(new LoseButtonListener());
			incorrectScreen.setVisible(true);
			
			return false;
		}
	}

	/*
	 * Sets the answer to 3 random cards
	 */
	public void selectAnswer() {
		Random rand = new Random();

		//Grabs a random person card
		int randomIndex = rand.nextInt(playerCards.size());
		Card currentSolutionPersonCard = playerCards.get(randomIndex);
		tempDeck.remove(currentSolutionPersonCard);

		//Grabs a random weapon card
		randomIndex = rand.nextInt(weaponCards.size());
		Card currentSolutionWeaponCard = weaponCards.get(randomIndex);
		tempDeck.remove(currentSolutionWeaponCard);

		//Grabs a random room card
		randomIndex = rand.nextInt(roomCards.size());
		Card currentSolutionRoomCard = roomCards.get(randomIndex);
		tempDeck.remove(currentSolutionRoomCard);

		//Sets the answer
		theAnswer = new Solution(currentSolutionPersonCard.getCardName(), currentSolutionWeaponCard.getCardName(), currentSolutionRoomCard.getCardName());
	}

	/*
	 * Prints the entire board when it is called to be drawn in the GUI
	 * It prints each boardCell and player
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Sets the background color for the rooms then prints each BoardCell that is a Walkway
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 552, 552);
		for(int i = 0; i < board.length; i++) {
			for(int j = 0; j < board[i].length; j++) {
				board[i][j].draw(g);
			}
		}

		//Iterates through every player and draws them on the board
		for(Player currentPlayer : players) {
			Color currentColor = currentPlayer.getColor();
			g.setColor(currentColor);//test

			int currentRow = currentPlayer.getRow();
			int currentCol = currentPlayer.getColumn();
			int cellWidth = board[currentRow][currentCol].getCellWidth();
			int cellHeight = board[currentRow][currentCol].getCellHeight();
			int startY = currentRow * board[currentRow][currentCol].getCellHeight();
			int startX = currentCol * board[currentRow][currentCol].getCellWidth();

			g.fillOval(startX, startY, cellWidth, cellHeight);
		}
	}

	/*
	 * Starts and runs the turn of the current player
	 * Rolls the dice, determines targets
	 */
	public void startNextTurn() {
		rollDice();
		calcTargets(getCellAt(players.get(currentPlayer).getRow(), players.get(currentPlayer).getColumn()), currentRoll);

		//If the player is human, set the targets cells to be targets so they are drawn blue
		if(players.get(currentPlayer).getStatus().equals("Human")) {
			for(BoardCell target : targets) {
				//System.out.println(target.getStartX() + " " + target.getStartY() + " " + target.getCellWidth() + " " + target.getCellHeight());
				target.setIsTarget(true);
			}

			//if the player made it into a room, make a suggestion
			if(players.get(currentPlayer).getCurrentRoom().isDoorway()) {
				//make suggestion
			}
		}
	}

	/*
	 * TargetListener class attached to the board to listen for clicks on valid target cells
	 */
	private class TargetListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			//grabs the coordinates of the mouse click
			int mouseX = e.getX();
			int mouseY = e.getY();

			boolean validClick = true;

			//If it is the player's turn
			//If it is not the player's turn, print an error messsage
			if(players.get(currentPlayer).getStatus().equals("Human")) {
				//System.out.println("Human's turn");
				
				//If the player is not done with their turn, check if any targets were clicked
				//If the player is done with their turn, do nothing
				if(players.get(currentPlayer).getDoneTurn() == false) {
					for(BoardCell target : targets) {
						Rectangle rect = new Rectangle(target.getStartX(), target.getStartY(), target.getCellWidth(), target.getCellHeight());
						if(rect.contains(new Point(mouseX, mouseY))) {
							players.get(currentPlayer).makeMove(target);
							for(BoardCell target1 : targets) {
								target1.setIsTarget(false);
							}
							repaint();
							//System.out.println("Valid target");
							validClick = true;
							break;
						}
						else {
							validClick = false;
						}
					}
					//If a target was not clicked, print an error message
					if(validClick == false) {
						JOptionPane splashScreen = new JOptionPane();
						splashScreen.showMessageDialog(Board.getInstance(), "Invalid target selection. Please try again.", "Error", JOptionPane.INFORMATION_MESSAGE);
						//System.out.println(Integer.toString(mouseX) + " " + Integer.toString(mouseY));
					}
					if(players.get(currentPlayer).getCurrentRoom().isDoorway()) {
						getSuggestionFromPlayer(players.get(currentPlayer).getCurrentRoom());
					}
				}
			}
			else {
				JOptionPane splashScreen = new JOptionPane();
				splashScreen.showMessageDialog(Board.getInstance(), "Not your turn. Please advance to the next player.", "Error", JOptionPane.INFORMATION_MESSAGE);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}
	}

	/*
	 * rolls a random number and sets the current roll
	 */
	public void rollDice() {
		Random rand = new Random();
		currentRoll = rand.nextInt(6) + 1;
	}

	/*
	 * returns the name of the currentPlayer
	 */
	public String getCurrentPlayerName() {
		return players.get(currentPlayer).getName();
	}

	/*
	 * Returns the Player object for the current player
	 */
	public Player getCurrentPlayer() {
		//If the game has just begun, just return the first player
		if(currentPlayer == -1) {
			return players.get(0);
		}
		return players.get(currentPlayer);
	}

	/*
	 * Return the current guess
	 */
	public String getGuess() {
		if(currentGuess == null) {
			return null;
		}
		return currentGuess.person + " in the " + currentGuess.room + " with the " + currentGuess.weapon;
	}

	/*
	 * Return the current guess result
	 */
	public String getGuessResult() {
		if(currentGuessResult == null) {
			return null;
		}
		return currentGuessResult.getCardName();
	}

	/*
	 * Return the current roll
	 */
	public int getCurrentRoll() {
		return currentRoll;
	}

	/*
	 * Return the current index of the current player
	 */
	public int getCurrentPlayerIndex() {
		return currentPlayer;
	}

	/*
	 * Increment the current player to the next player
	 */
	public void advanceNextPlayer() {
		currentPlayer++;
		currentPlayer %= players.size();
	}
	//This will create the guess box for the player and stores and tests guess
	public void getSuggestionFromPlayer(BoardCell currentRoom) {
		JDialog suggestionBox = new JDialog();
		
		//Making a guess
		suggestionBox.setTitle("Make A Guess");
		suggestionBox.setSize(new Dimension(500, 625));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,2));
		
		//Making the dialog popup box
		JLabel yourRoom = new JLabel("Your Room");
		JLabel personLabel = new JLabel("Person");
		JLabel weaponLabel = new JLabel("Weapon");
		
		JComboBox<String> rooms = new JComboBox<String>();
		rooms.addItem(legend.get(currentRoom.getInitial()));
		
		//Filling in all of the people
		JComboBox<String> people = new JComboBox<String>();
		
		for(Card card : getPlayerCards()) {
			people.addItem(card.getCardName());
		}
		
		JComboBox<String> weapons = new JComboBox<String>();
		
		//Filling in all of the weapons
		for(Card card : getWeaponCards()) {
			weapons.addItem(card.getCardName());
		}
		
		//Making the buttons
		submit = new JButton("Submit");
		cancel = new JButton("Cancel");
		
		//Adding it to panel
		panel.add(yourRoom);
		panel.add(rooms);
		panel.add(personLabel);
		panel.add(people);
		panel.add(weaponLabel);
		panel.add(weapons);
		panel.add(submit);
		panel.add(cancel);
		
		//Adding it to JDialog Box
		suggestionBox.add(panel);
		suggestionBox.setVisible(true);
		
		//If they click submit
		class SubmitButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				
				//Create suggestion made by player
				String personGuess = (String) people.getSelectedItem();
				String weaponsGuess = (String) weapons.getSelectedItem();
				
				Solution playerSolution = new Solution(personGuess, weaponsGuess, legend.get(currentRoom.getInitial()));
				
				//Check said suggestion
				handleSuggestion(playerSolution, getCurrentPlayer());
				
				//Close the window and update GUI
				GameControlPanel.updatePanelGuessAndResult();
				players.get(currentPlayer).setDoneTurn(true);
				suggestionBox.setVisible(false);
			}
		}
		//If they click cancel
		class CancelButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				//No suggestion made
				players.get(currentPlayer).setDoneTurn(true);
				suggestionBox.setVisible(false);
			}
		}
		//Making sure the buttons function
		submit.addActionListener(new SubmitButtonListener());
		cancel.addActionListener(new CancelButtonListener());
		
	}
	//Takes accusation and checks it only if they are human and it is the beginning of their turn
	public void getAccusationFromPlayer() {
		//Only allows accusation from player if they are human
		if(getCurrentPlayer().getStatus().equals("Human") && getCurrentPlayer().getStartTurn()) {

			JDialog suggestionBox = new JDialog();

			//Making the title and size
			suggestionBox.setTitle("Make A Guess");
			suggestionBox.setSize(new Dimension(500, 625));

			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(4,2));

			//Creating options
			JLabel yourRoom = new JLabel("Your Room");
			JLabel personLabel = new JLabel("Person");
			JLabel weaponLabel = new JLabel("Weapon");

			JComboBox<String> rooms = new JComboBox<String>();

			//Filling in all of the rooms
			for(Card card : getRoomCards()) {
				rooms.addItem(card.getCardName());
			}
			//Filling in all of the people
			JComboBox<String> people = new JComboBox<String>();

			for(Card card : getPlayerCards()) {
				people.addItem(card.getCardName());
			}
			//Filling in all of the weapons
			JComboBox<String> weapons = new JComboBox<String>();

			for(Card card : getWeaponCards()) {
				weapons.addItem(card.getCardName());
			}

			//Making the two buttons
			JButton submit = new JButton("Submit");
			JButton cancel = new JButton("Cancel");

			//adding it to the jpanel
			panel.add(yourRoom);
			panel.add(rooms);
			panel.add(personLabel);
			panel.add(people);
			panel.add(weaponLabel);
			panel.add(weapons);
			panel.add(submit);
			panel.add(cancel);

			//adding the panel to jdialog
			suggestionBox.add(panel);
			suggestionBox.setVisible(true);

			//if they click submit
			class SubmitButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent e) {
					//create solution
					String personGuess = (String) people.getSelectedItem();
					String weaponsGuess = (String) weapons.getSelectedItem();
					String roomGuess = (String) rooms.getSelectedItem();

					//check solution
					Solution playerSol = new Solution(personGuess, weaponsGuess, roomGuess);
					suggestionBox.setVisible(false);
					checkAccusation(playerSol);


				}
			}
			//if they click cancel
			class CancelButtonListener implements ActionListener{
				public void actionPerformed(ActionEvent e) {

					suggestionBox.setVisible(false);
				}
			}

			//adding listener to the buttons so they work
			submit.addActionListener(new SubmitButtonListener());
			cancel.addActionListener(new CancelButtonListener());
		}
		else {
			JOptionPane splashScreen = new JOptionPane();
			splashScreen.showMessageDialog(this, "You cannot make an accusation at this time", "Error", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
