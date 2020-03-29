package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public class Player {


	private String playerName;
	private String status;
	private int row;
	private int column;
	private Color color;
	
	private ArrayList<Card> myCards;
	
	public Player(String playerName2, Color playerColor, String playerStatus, int playerRow, int playerColumn) {
		this.playerName = playerName2;
		this.color = playerColor;
		this.status = playerStatus;
		this.row = playerRow;
		this.column = playerColumn;
	}

	public String getName() {
		return playerName;
	}

	public String getStatus() {
		return status;
	}

	public Color getColor() {
		return color;
	}

	public Integer getRow() {
		return row;
	}

	public Integer getColumn() {
		return column;
	}
	
	public ArrayList<Card> getCards(){
		return myCards;
	}
	
	public void addCard(Card newCard) {
		myCards.add(newCard);
	}
	
	/*
	public Card disproveSuggestion(Solution suggestion) {
		
	}
	*/
}
