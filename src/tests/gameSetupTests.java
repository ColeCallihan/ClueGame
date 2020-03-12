package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import clueGame.Board;

public class gameSetupTests {
private static Board board;
	@BeforeClass
	public static void setUp(){
		board = Board.getInstance();
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt", "SpacePlayers.txt", "SpaceCards.txt");
		
		board.loadPeople();
		board.loadCards();
		
		
		Card[] deck = board.getDeck();
		Player[] allPlayers = board.getPlayers();
		
	}

	@Test
	public void testPeopleLoading() {
		
		
		//Checking Human Player in first position
		Color currentColor = "Blue";
		assertEquals(6, allPlayers.length);
		assertEquals("Spock", allPlayers[0].getName());
		assertEquals("Human", allPlayers[0].getStatus());
		assertEquals(currentColor, allPlayers[0].getColor());
		assertEquals(28, allPlayers[0].getRow());
		assertEquals(9, allPlayers[0].getColumn());
		
		//Checking the 3rd computer player in position 4
		currentColor = "Green";
		assertEquals(6, allPlayers.length);
		assertEquals("Alien", allPlayers[3].getName());
		assertEquals("Computer", allPlayers[3].getStatus());
		assertEquals(currentColor, allPlayers[3].getColor());
		assertEquals(21, allPlayers[3].getRow());
		assertEquals(0, allPlayers[3].getColumn());
		
		//Checking the last computer player in position 6
		currentColor = "White";
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
		
	}
}

