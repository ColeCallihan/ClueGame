/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * HumanPlayer class that defines methods specific to human players
 */ 

package clueGame;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.util.Set;

public class HumanPlayer extends Player{
	
	/*
	 * HumanPlayer constructor that calls the super constructor to set the HumanPlayer name, color, status, row, and column
	 */
	public HumanPlayer(String playerName2, Color playerColor, String playerStatus, int playerRow, int playerColumn) {
		super(playerName2, playerColor, playerStatus, playerRow, playerColumn);
	}

	/*
	 * Default HumanPlayer constructor that calls the super default constructor
	 */
	public HumanPlayer() {
		super();
	}

	/*
	 * To be implemented
	 */
	public Solution generateSolution() {
		return null;
	}
	
	@Override
	public void makeMove(BoardCell target) {
		this.setRow(target.getRow());
		this.setColumn(target.getColumn());
		Board board = Board.getInstance();
		super.setCurrentRoom(board.getCellAt(this.getRow(), this.getColumn()));
		setDoneTurn(true);
		System.out.println("Yo, I moved");
	}
	
	@Override
	public boolean getDoneTurn() {
		return doneTurn;
	}
	
	public void setDoneTurn(boolean done) {
		doneTurn = done;
	}
}
