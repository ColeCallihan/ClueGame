/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * Player class that holds player name, status, row, col, and color
 */ 

package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Set;

public class Player {

	//Instance variables for name, status, row, column, and color
	private String playerName;
	private String status;
	private int row;
	private int column;
	private Color color;
	
	//Instance variables to hold player's cards
	private ArrayList<Card> myCards = new ArrayList<Card>();
	
	/*
	 * Constructor for player
	 */
	public Player(String playerName2, Color playerColor, String playerStatus, int playerRow, int playerColumn) {
		this.playerName = playerName2;
		this.color = playerColor;
		this.status = playerStatus;
		this.row = playerRow;
		this.column = playerColumn;
	}
	public Player() {
		// TODO Auto-generated constructor stub
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
	 * Adds a card to the player's list of cards
	 */
	public void addCard(Card newCard) {
		myCards.add(newCard);
	}
	
	/*
	public Card disproveSuggestion(Solution suggestion) {
		
	}
	*/
	public void setColumn(int i) {
		// TODO Auto-generated method stub
		
	}

	public void setRow(int i) {
		// TODO Auto-generated method stub
		
	}


	public BoardCell pickLocation(Set<BoardCell> targets) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setPrevLocation(String string) {
		// TODO Auto-generated method stub
		
	}

	
	public String getRoom() {
		// TODO Auto-generated method stub
		return null;
	}
	public Solution generateSolution() {
		// TODO Auto-generated method stub
		return null;
	}
	public void removeWeapons(String string) {
		// TODO Auto-generated method stub
		
	}

	public void removeSuspects(String string) {
		// TODO Auto-generated method stub
		
	}
}
