package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;

public class gameSetupTests {
private static final int NUM_CARDS_PER_PLAYER = 3;
private static Board board;
	@BeforeClass
	public static void setUp(){
		board = Board.getInstance();
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt", "SpacePlayers.txt", "SpaceCards.txt");
		
		board.loadPeople();
		board.loadCards();
		board.dealCards();
		
		Card[] deck = board.getDeck();
		Player[] allPlayers = board.getPlayers();
		
	}

	@Test
	public void testPeopleLoading() {
		
		
		//Checking Human Player in first position
		Color currentColor = convertColor("Blue");
		assertEquals(6, allPlayers.length);
		assertEquals("Spock", allPlayers[0].getName());
		assertEquals("Human", allPlayers[0].getStatus());
		assertEquals(currentColor, allPlayers[0].getColor());
		assertEquals(28, allPlayers[0].getRow());
		assertEquals(9, allPlayers[0].getColumn());
		
		//Checking the 3rd computer player in position 4
		currentColor = convertColor("Green");
		assertEquals(6, allPlayers.length);
		assertEquals("Alien", allPlayers[3].getName());
		assertEquals("Computer", allPlayers[3].getStatus());
		assertEquals(currentColor, allPlayers[3].getColor());
		assertEquals(21, allPlayers[3].getRow());
		assertEquals(0, allPlayers[3].getColumn());
		
		//Checking the last computer player in position 6
		currentColor = convertColor("White");
		assertEquals(6, allPlayers.length);
		assertEquals("Android", allPlayers[5].getName());
		assertEquals("Computer", allPlayers[5].getStatus());
		assertEquals(currentColor, allPlayers[5].getColor());
		assertEquals(0, allPlayers[5].getRow());
		assertEquals(17, allPlayers[5].getColumn());
	}

	
	@Test
	public void testCardLoading() {
		//Ensuring there is the correct number of cards
		assertEquals(21, deck.length);
		
		//Check correct number of each type of card
		assertEquals(6, board.getWeaponsCount());
		assertEquals(6, board.getPlayersCount());
		assertEquals(9, board.getRoomsCount());
		
		//Testing name of the cards
		assertEquals("Freeze Gun", board.getWeaponCards()[0].getCardName());
		assertEquals(CardType.WEAPON, board.getWeaponCards()[0].getCardType());
		assertEquals("Spock", board.getPlayerCards()[0].getCardName());
		assertEquals(CardType.PLAYER, board.getWeaponCards()[0].getCardType());
		assertEquals("Trash Compactor", board.getRoomCards()[0].getCardName());
		assertEquals(CardType.ROOM, board.getWeaponCards()[0].getCardType());	
	}
	
	@Test
	public void testCardDealing() {
		for(int i = 0; i < board.getPlayersCount(); i++) {
			
			Set cardSet = new HashSet();
		
			//If there are 6 players, specific to our SpaceBoard
			assertTrue(NUM_CARDS_PER_PLAYER, allPlayers[i].myCards.length);
			
			for(int j = 0; j < allPlayers[i].myCards.length; j++)
			{
				//Checking for unique cards
				if(cardSet.contains(allPlayers[i].myCards[j])) {
					fail("Duplicate Cards");
				}
				cardSet.add(allPlayers[i].myCards[j]);
			}
			
			assertEquals(18, cardSet.size());
		}
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



