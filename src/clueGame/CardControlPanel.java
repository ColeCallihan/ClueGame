/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * Card Control Class to display the current cards to the player
 */ 

package clueGame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardControlPanel extends JPanel {

	//using the board to get current player cards
	Board board;
	//the current player cards recieved from the board
	ArrayList<Card> playerCards = new ArrayList<Card>();

	//Creates the class to show the cards to the player
	public CardControlPanel()
	{
		//Assuming human player is always the first in the config file
		board = Board.getInstance();
		playerCards = (ArrayList<Card>) board.getPlayers().get(0).getCards().clone();

		//Creating different array lists for the different card types
		ArrayList<Card> playerWeapons = new ArrayList<Card>();
		ArrayList<Card> playerRooms = new ArrayList<Card>();
		ArrayList<Card> playerPeople = new ArrayList<Card>();

		//Separating player cards into their own array list based off of their type
		for(int i = 0; i < playerCards.size(); i++)	{
			if (playerCards.get(i).getCardType() == CardType.PERSON) {
				playerPeople.add(playerCards.get(i));
			}
			if (playerCards.get(i).getCardType() == CardType.ROOM) {
				playerRooms.add(playerCards.get(i));
			}
			if (playerCards.get(i).getCardType() == CardType.WEAPON) {
				playerWeapons.add(playerCards.get(i));
			}
		}

		//Setting overall layout
		setLayout(new GridLayout(3,1));
		setPreferredSize(new Dimension(300,300));
		setBorder(new TitledBorder(new EtchedBorder(),"My Cards"));

		//Adding the different card types to the main jpanel
		add(createPeople(playerPeople));
		add(createWeapons(playerWeapons));
		add(createRooms(playerRooms));
	}

	//Creates the weapon cards to display to the player
	private JPanel createWeapons(ArrayList<Card> cards) {
		//JPanel for weapons
		JPanel wepPane = new JPanel();
		wepPane.setBorder(new TitledBorder(new EtchedBorder(),"Weapons"));

		//Adding any number of cards from 0-infinity to the Jpanel for display
		for(int i = 0; i < cards.size(); i++) {
			JTextField dispText = new JTextField();
			dispText.setEditable(false);
			dispText.setPreferredSize(new Dimension(100,100));
			dispText.setText(cards.get(i).getCardName());
			dispText.setHorizontalAlignment(JTextField.CENTER);
			wepPane.add(dispText);
		}

		return wepPane;
	}

	//Creates the room cards to display to the player
	private JPanel createRooms(ArrayList<Card> cards) {
		//JPanel for roooms
		JPanel roomPane = new JPanel();
		roomPane.setBorder(new TitledBorder(new EtchedBorder(),"Rooms"));

		//Adding any number of cards from 0-infinity to the Jpanel for display
		for(int i = 0; i < cards.size(); i++) {
			JTextField dispText = new JTextField();
			dispText.setEditable(false);
			dispText.setPreferredSize(new Dimension(100,100));
			dispText.setText(cards.get(i).getCardName());
			dispText.setHorizontalAlignment(JTextField.CENTER);
			roomPane.add(dispText);
		}

		return roomPane;
	}

	//Creates the people cards to display to the player
	private JPanel createPeople(ArrayList<Card> cards) {
		//JPanel for roooms
		JPanel peepsPane = new JPanel();
		peepsPane.setBorder(new TitledBorder(new EtchedBorder(),"People"));

		//Adding any number of cards from 0-infinity to the Jpanel for display
		for(int i = 0; i < cards.size(); i++) {
			JTextField dispText = new JTextField();
			dispText.setEditable(false);
			dispText.setPreferredSize(new Dimension(100,100));
			dispText.setText(cards.get(i).getCardName());
			dispText.setHorizontalAlignment(JTextField.CENTER);
			peepsPane.add(dispText);
		}

		return peepsPane;
	}



}
