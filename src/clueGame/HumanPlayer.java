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
	
	/*
	 * Sets the human player's location to the target that was passed in
	 * Also sets the turn to be complete and changes their current
	 */
	@Override
	public void makeMove(BoardCell target) {
		startTurn = false;
		this.setRow(target.getRow());
		this.setColumn(target.getColumn());
		Board board = Board.getInstance();
		super.setCurrentRoom(board.getCellAt(this.getRow(), this.getColumn()));
		if(!getCurrentRoom().isDoorway()) {
			setDoneTurn(true);
		}
		
		//System.out.println("Yo, I moved");
	}
	
	/*
	 * returns whether the human player is done with their turn or not
	 */
	@Override
	public boolean getDoneTurn() {
		return doneTurn;
	}
	
	/*
	 * Sets the doneTurn boolean if the human player is done with their turn
	 */
	public void setDoneTurn(boolean done) {
		doneTurn = done;
	}
}
