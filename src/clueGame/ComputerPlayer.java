/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * ComputerPlayer class that defines methods specific to computer players
 */ 

package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.Random;

public class ComputerPlayer extends Player{



	/*
	 * ComputerPlayer constructor that calls super constructor, setting the names, color, status, row, and column
	 */
	public ComputerPlayer(String playerName2, Color playerColor, String playerStatus, int playerRow, int playerColumn) {
		super(playerName2, playerColor, playerStatus, playerRow, playerColumn);
	}

	/*
	 * Default ComputerPlayer constructor that calls the super default constructor
	 */
	public ComputerPlayer() {
		super();
	}

	/*
	 * Same as createSuggestion method in the instructors UML Diagram
	 * Constructs a solution that becomes an accusation when the computer has narrowed down the solution
	 * Becomes a suggestion otherwise
	 */
	@Override
	public Solution generateSolution() {

		//If the computer player has narrowed down to the solution, they will make an accusation, ending the game.
		//A null will never be returned assuming they narrowed down the solution properly
		if(suspectPeople.size() == 1 && suspectWeapons.size() == 1 & suspectRooms.size() == 1) {
			makeAccusation(suspectPeople.get(0).getCardName(), suspectWeapons.get(0).getCardName(), suspectRooms.get(0).getCardName());
			return null;
		}
		else {
			//grabs the name of the room at the current position of the computer player
			String guessRoom = Board.getInstance().getLegend().get(currentRoom.getInitial());
			Random rand = new Random();
			int guessPersonIndex = rand.nextInt(suspectPeople.size());
			String guessPerson = suspectPeople.get(guessPersonIndex).getCardName();
			int guessWeaponIndex = rand.nextInt(suspectWeapons.size());
			String guessWeapon = suspectWeapons.get(guessWeaponIndex).getCardName();

			return new Solution(guessPerson, guessWeapon, guessRoom);
		}
	}

	/*
	 * Picks a location to move to from the given list of targets
	 */
	public BoardCell pickLocation(Set<BoardCell> targets) {
		ArrayList<BoardCell> roomList = new ArrayList<BoardCell>();

		for(BoardCell currentTarget : targets) {
			if(currentTarget.isDoorway()) {
				roomList.add(currentTarget);
			}
		}
		if(prevRoom != null) {
			for(BoardCell target : targets) {
				if(target.isDoorway() && prevRoom.isDoorway() && (prevRoom.getInitial() == target.getInitial())) {
					roomList.remove(target);
				}
			}
		}

		Random rand = new Random();
		//If the roomList does not contain any new rooms, pick a random target
		if(roomList.isEmpty() == true) {
			int stop = rand.nextInt(targets.size());
			int count = 0;
			for(BoardCell target : targets) {
				if(count == stop) {
					return target;
				}
				else {
					count++;
				}
			}
		}
		else {
			//Random Room is chosen
			int stop = rand.nextInt(roomList.size());
			return roomList.get(stop);
		}
		//Fail safe
		return null;
	}

	/*
	 * Ends the game if the player guessed the right thing, otherwise continues the game
	 */
	public void makeAccusation(String person, String weapon, String room) {
		Board.getInstance().checkAccusation(new Solution(person, weapon, room));
		//Ends the game
	}

	/*
	 * Override the makeMove method from player.
	 * Makes the computer player pick a location from the list of targets
	 * then sets the computer player's current room, previous room, new location, and makes their suggestion
	 * if they are now in a room.
	 */
	@Override
	public void makeMove(Set<BoardCell> targets) {
		startTurn = false;
		BoardCell newLocation = pickLocation(targets);
		BoardCell lastRoom = super.getCurrentRoom();
		
		//System.out.println("about to move " + getName());

		if(newLocation.isDoorway()) {
			setPrevRoom(newLocation);
			//make suggestion
		}

		this.setRow(newLocation.getRow());
		this.setColumn(newLocation.getColumn());
		Board board = Board.getInstance();
		super.setCurrentRoom(board.getCellAt(this.getRow(), this.getColumn()));
		//System.out.println("Computer player moved");
	}

}
