/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * DetectiveNotes is the Dialog box where players can formulate their guesses
 */

package clueGame;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotes extends JDialog{
	
	//instance variable for the static instance of the board
	Board board;
	
	/*
	 * Constructor for Detective Notes to set the JFrame
	 */
	public DetectiveNotes() {
		board = Board.getInstance();
		
		setTitle("Detective Notes");
		setSize(new Dimension(500, 625));
		setLayout(new GridLayout(3, 2));
		
		JPanel people = new JPanel();
		people.setBorder(new TitledBorder(new EtchedBorder(),"People"));
		
		//Iterates through the players and creates checkboxes and combobox entries for each
		JCheckBox currentPerson;
		
		JPanel personGuess = new JPanel();
		personGuess.setBorder(new TitledBorder(new EtchedBorder(),"Person Guess"));
		
		JComboBox<String> gamePlayers = new JComboBox<String>();
		gamePlayers.addItem("Unsure");
		
		for(Player currentPlayer : board.getPlayers()) {
			currentPerson = new JCheckBox(currentPlayer.getName());
			people.add(currentPerson);
			
			gamePlayers.addItem(currentPlayer.getName());
		}
		
		add(people);
		add(gamePlayers);
		
		//Iterates through the weapons and creates checkboxes and combobox entries for each
		JPanel weapon = new JPanel();
		weapon.setBorder(new TitledBorder(new EtchedBorder(),"Weapons"));
		
		JCheckBox addWeapon;
		
		JPanel weaponGuess = new JPanel();
		weaponGuess.setBorder(new TitledBorder(new EtchedBorder(),"Weapon Guess"));
		
		JComboBox<String> gameWeapons = new JComboBox<String>();
		gameWeapons.addItem("Unsure");
		
		for(Card currentWeapon : board.getWeaponCards()) {
			addWeapon = new JCheckBox(currentWeapon.getCardName());
			weapon.add(addWeapon);
			
			gameWeapons.addItem(currentWeapon.getCardName());
		}
		add(weapon);
		add(gameWeapons);
		
		//Iterates through the rooms and creates checkboxes and combobox entries for each
		JPanel rooms = new JPanel();
		rooms.setBorder(new TitledBorder(new EtchedBorder(),"Rooms"));
		
		JCheckBox addRoom;
		
		JPanel roomGuess = new JPanel();
		roomGuess.setBorder(new TitledBorder(new EtchedBorder(),"Room Guess"));
		
		JComboBox<String> gameRooms = new JComboBox<String>();
		gameRooms.addItem("Unsure");
		
		for(Card currentRoom : board.getRoomCards()) {
			addRoom = new JCheckBox(currentRoom.getCardName());
			rooms.add(addRoom);
			
			gameRooms.addItem(currentRoom.getCardName());
		}
		
		add(rooms);
		add(gameRooms);
	}
}
