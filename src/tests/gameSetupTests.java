/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * gameSetupTests that tests the proper card decks and players were created
 */ 

package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;

public class gameSetupTests {

	private static final int NUM_CARDS_PER_PLAYER = 3;
	private static Board board;

	private static ArrayList<Card> deck = new ArrayList<Card>();
	private static ArrayList<Player> allPlayers = new ArrayList<Player>();

	@BeforeAll
	public static void setUp() throws FileNotFoundException, BadConfigFormatException{
		board = Board.getInstance();
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt", "SpacePlayers.txt", "SpaceCards.txt");
		board.initialize();

		deck = board.getDeck();
		allPlayers = (ArrayList<Player>)board.getPlayers().clone();

		assertEquals(6, allPlayers.size());

	}

	@Test
	public void testPeopleLoading() {
		//Checking Human Player in first position
		Color currentColor = convertColor("BLUE");
		assertEquals(6, allPlayers.size());
		assertEquals("Spock", allPlayers.get(0).getName());
		assertEquals("Human", allPlayers.get(0).getStatus());
		assertEquals(currentColor, allPlayers.get(0).getColor());
		assertEquals(28, allPlayers.get(0).getRow());
		assertEquals(9, allPlayers.get(0).getColumn());

		//Checking the 3rd computer player in position 4
		currentColor = convertColor("GREEN");
		assertEquals(6, allPlayers.size());
		assertEquals("Alien", allPlayers.get(3).getName());
		assertEquals("Computer", allPlayers.get(3).getStatus());
		assertEquals(currentColor, allPlayers.get(3).getColor());
		assertEquals(21, allPlayers.get(3).getRow());
		assertEquals(0, allPlayers.get(3).getColumn());

		//Checking the last computer player in position 6
		currentColor = convertColor("ORANGE");
		assertEquals(6, allPlayers.size());
		assertEquals("Android", allPlayers.get(5).getName());
		assertEquals("Computer", allPlayers.get(5).getStatus());
		assertEquals(currentColor, allPlayers.get(5).getColor());
		assertEquals(0, allPlayers.get(5).getRow());
		assertEquals(17, allPlayers.get(5).getColumn());
	}


	@Test
	public void testCardLoading() {
		//Ensuring there is the correct number of cards
		assertEquals(21, deck.size());

		//Check correct number of each type of card
		assertEquals(6, board.getWeaponsCount());
		assertEquals(6, board.getPlayersCount());
		assertEquals(9, board.getRoomsCount());

		//Testing name of the cards
		assertEquals("Freeze Gun", board.getWeaponCards().get(0).getCardName());
		assertEquals(CardType.WEAPON, board.getWeaponCards().get(0).getCardType());
		assertEquals("Spock", board.getPlayerCards().get(0).getCardName());
		assertEquals(CardType.PERSON, board.getPlayerCards().get(0).getCardType());
		assertEquals("Trash Compactor", board.getRoomCards().get(0).getCardName());
		assertEquals(CardType.ROOM, board.getRoomCards().get(0).getCardType());	
	}

	@Test
	public void testCardDealing() {
		Set<Card> cardSet = new HashSet<Card>();
		for(int i = 0; i < board.getPlayersCount(); i++) {

			//If there are 6 players, specific to our SpaceBoard
			assertEquals(NUM_CARDS_PER_PLAYER, allPlayers.get(i).getCards().size());

			for(int j = 0; j < allPlayers.get(i).getCards().size(); j++)
			{
				//Checking for unique cards
				if(cardSet.contains(allPlayers.get(i).getCards().get(j))) {
					fail("Duplicate Cards");
				}
				cardSet.add(allPlayers.get(i).getCards().get(j));
			}

		}
		assertEquals(18, cardSet.size());
	}



	public Color convertColor(String strColor) {
		Color color;
		try {
			// We can use reflection to convert the string to a color
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());
			color = (Color)field.get(null);
		} catch (Exception e) {
			color = null; // Not defined
		}
		return color;
	}

}



