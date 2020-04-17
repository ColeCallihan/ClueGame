/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * Player class that holds player name, status, row, col, and color
 */ 

package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Player {

	//Instance variables for name, status, row, column, and color
	private String playerName;
	private String status;
	private int row;
	private int column;
	private Color color;
	
	protected boolean doneTurn = false;
	
	//Keeps track of the last room the player was in
	protected BoardCell prevRoom;
	protected BoardCell currentRoom;
	
	//Instance variables to hold player's cards
	protected ArrayList<Card> myCards = new ArrayList<Card>();
	protected ArrayList<Card> suspectPeople = new ArrayList<Card>();
	public ArrayList<Card> suspectWeapons = new ArrayList<Card>();
	protected ArrayList<Card> suspectRooms = new ArrayList<Card>();
	
	/*
	 * Constructor for player, also copies suspect lists from the board
	 */
	public Player(String playerName2, Color playerColor, String playerStatus, int playerRow, int playerColumn) {
		this.playerName = playerName2;
		this.color = playerColor;
		this.status = playerStatus;
		this.row = playerRow;
		this.column = playerColumn;
		Board board = Board.getInstance();
		suspectPeople = (ArrayList<Card>)board.getPlayerCards().clone();
		suspectWeapons = (ArrayList<Card>)board.getWeaponCards().clone();
		suspectRooms =(ArrayList<Card>)board.getRoomCards().clone();
		currentRoom = Board.getInstance().getCellAt(row, column);
	}
	
	/*
	 * Default constructor for Player
	 */
	public Player() {
		super();
		Board board = Board.getInstance();
		suspectPeople = (ArrayList<Card>)board.getPlayerCards().clone();
		suspectWeapons = (ArrayList<Card>)board.getWeaponCards().clone();
		suspectRooms =(ArrayList<Card>)board.getRoomCards().clone();
	}
	
	/*
	 * Returns player name
	 */
	public String getName() {
		return playerName;
	}

	/*
	 * Returns player status
	 */
	public String getStatus() {
		return status;
	}

	/*
	 * Returns player color
	 */
	public Color getColor() {
		return color;
	}

	/*
	 * Returns player row
	 */
	public Integer getRow() {
		return row;
	}

	/*
	 * Returns player column
	 */
	public Integer getColumn() {
		return column;
	}
	
	/*
	 * Returns player's list of cards
	 */
	public ArrayList<Card> getCards(){
		return myCards;
	}
	
	/*
	 * Adds a card to the player's list of cards, removing it from their suspect lists
	 */
	public void addCard(Card newCard) {
		
		//Adds the new card to their hand
		myCards.add(newCard);
		
		//Tests if the new card is in their suspect lists, and removes it from the list if it is in there
		if(newCard.getCardType() == CardType.PERSON) {
			for(Card suspect : suspectPeople) {
				if(suspect.equals(newCard)) {
					suspectPeople.remove(suspect);
					break;
				}
			}
		}
		else if(newCard.getCardType() == CardType.WEAPON) {
			for(Card suspect : suspectWeapons) {
				if(suspect.equals(newCard)) {
					suspectWeapons.remove(suspect);
					break;
				}
			}
		}
		else if(newCard.getCardType() == CardType.ROOM) {
			for(Card suspect : suspectRooms) {
				if(suspect.equals(newCard)) {
					suspectRooms.remove(suspect);
					break;
				}
			}
		}
	}
	
	/*
	 * Checks the cards of the player and returns a card if they can disprove the suggestion
	 */
	public Card disproveSuggestion(Solution suggestion) {
		//creates a new list of cards of possible ways to disprove the solution
		ArrayList<Card> shuffleCard = new ArrayList<Card>();
		for(Card currentCard : myCards) {
			if(currentCard.getCardName().equals(suggestion.person) || currentCard.getCardName().equals(suggestion.weapon) || currentCard.getCardName().equals(suggestion.room)) {
				shuffleCard.add(currentCard);
			}
		}
		Random rand = new Random();
		
		//If there are no cards to disprove the solution, return null, otherwise return a random card
		if(shuffleCard.isEmpty()) {
			return null;
		}
		int randomCardIndex = rand.nextInt(shuffleCard.size());
		return shuffleCard.get(randomCardIndex);
	}
	
	/*
	 * Sets the player's current column
	 */
	public void setColumn(int colNum) {
		column = colNum;
		currentRoom = Board.getInstance().getCellAt(row, column);
	}

	/*
	 * Sets the player's current row
	 */
	public void setRow(int rowNum) {
		row = rowNum;
		Board.getInstance().getCellAt(row, column);
	}

	/*
	 * Sets the player's previous room to the room they were in before they moved out
	 */
	public void setPrevRoom(BoardCell previous) {
		prevRoom = previous;
	}
	
	/*
	 * Removes a weapon from the list of suspected people
	 */
	public void removeWeapons(String weaponName) {
		for(Card currentWeapon : this.suspectWeapons) {
			if(currentWeapon.getCardName().equals(weaponName)) {
				this.suspectWeapons.remove(currentWeapon);
				break;
			}
		}
	}

	/*
	 * Removes a person from the list of suspected people
	 */
	public void removePeople(String personName) {
		for(Card currentPerson : this.suspectPeople) {
			if(currentPerson.getCardName().equals(personName)) {
				this.suspectPeople.remove(currentPerson);
				break;
			}
		}
	}
	
	/*
	 * Returns the location of the room that the player is in
	 */
	public BoardCell getCurrentRoom() {
		return Board.getInstance().getCellAt(row, column);
	}
	
	public boolean getDoneTurn() {
		return true;
	}

	public void setCurrentRoom(BoardCell cellAt) {
		currentRoom = cellAt;
	}
	
	public void makeMove(BoardCell target) {
		System.out.println("Wrong move");
	}
	
	public void makeMove(Set<BoardCell> targets) {
		System.out.println("Wrong move");
	}
}
