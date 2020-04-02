package tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;
import clueGame.Board;

class gameActionTest {

	private static final int NUM_CARDS_PER_PLAYER = 3;
	private static Board board;

	private static ArrayList<Card> deck = new ArrayList<Card>();
	private static ArrayList<Player> allPlayers = new ArrayList<Player>();
	
	static ComputerPlayer player;
	static ComputerPlayer playerUnseen;
	
	@BeforeAll
	public static void setUp() throws FileNotFoundException, BadConfigFormatException {
		player = new ComputerPlayer();
		playerUnseen = new ComputerPlayer();
		
		board = Board.getInstance();
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt", "SpacePlayers.txt", "SpaceCards.txt");

		board.initialize();

		board.loadPeople();
		board.loadCards();
	}

	@Test
	public void computerTargets() {

		//if no rooms in list, select randomly
		// Pick a location with no rooms in target, just four targets
		board.calcTargets(21, 21, 1);
		boolean loc_22_21 = false;
		boolean loc_20_21 = false;
		boolean loc_21_22 = false;
		boolean loc_21_20 = false;
		// Run the test a large number of times
		for (int i=0; i<100; i++) {
			BoardCell selected = player.pickLocation(board.getTargets());
			if (selected == board.getCellAt(20, 21))
				loc_20_21 = true;
			else if (selected == board.getCellAt(22, 21))
				loc_22_21 = true;
			else if (selected == board.getCellAt(21, 20))
				loc_21_20 = true;
			else if (selected == board.getCellAt(21, 22))
				loc_21_22 = true;
			else
				fail("Invalid target selected");
		}
		// Ensure each target was selected at least once
		assertTrue(loc_20_21);
		assertTrue(loc_22_21);
		assertTrue(loc_21_20);
		assertTrue(loc_21_22);
		
		
		//if room just visited is in list, each target (including room) selected randomly
		player.setPrevLocation("Lounge");
		board.calcTargets(14,20,1);
		boolean loc_14_19 = false;
		boolean loc_14_21 = false;
		boolean loc_15_20 = false;
		boolean loc_13_20 = false;
		for (int i=0; i<100; i++) {
			BoardCell selected = player.pickLocation(board.getTargets());
			if (selected == board.getCellAt(13, 20))
				loc_13_20 = true;
			else if (selected == board.getCellAt(15, 20))
				loc_15_20 = true;
			else if (selected == board.getCellAt(14, 21))
				loc_14_21 = true;
			else if (selected == board.getCellAt(14, 19))
				loc_14_19 = true;
			else
				fail("Invalid target selected");
		}
		// Ensure each target was selected at least once
		assertTrue(loc_20_21);
		assertTrue(loc_22_21);
		assertTrue(loc_21_20);
		assertTrue(loc_21_22);
		
		//if room in list that was not just visited, must select it
		board.calcTargets(15,21,1);
		BoardCell selected = player.pickLocation(board.getTargets());
		assertEquals(selected, board.getCellAt(15, 22));
	
	}

	@Test
	public void testAccusation() {

		Solution solution = new Solution("Spock","Probe","Bridge");
		board.theAnswer = solution;
		
		//solution that is correct
		assertTrue(player.makeAccusation("Spock","Probe","Bridge"));
		
		//solution with wrong person
		assertFalse(player.makeAccusation("Alien","Probe","Bridge"));
		//solution with wrong weapon
		assertFalse(player.makeAccusation("Spock","Lazer","Bridge"));
		//solution with wrong room
		assertFalse(player.makeAccusation("Spock","Probe","Lounge"));
	}


	@Test
	public void testSuggestion() {
		
		//Only one weapon and person left
		player.removeWeapons("Freeze Gun");
		player.removeWeapons("Face Hugger");
		player.removeWeapons("Probe");
		player.removeWeapons("Lazer");
		player.removeWeapons("FlameThrower");

		player.removeSuspects("Spock");
		player.removeSuspects("Android");
		player.removeSuspects("Alien");
		player.removeSuspects("Tourist");
		player.removeSuspects("Engineer");
		
		//Player with more options
		playerUnseen.removeWeapons("Freeze Gun");
		playerUnseen.removeWeapons("Face Hugger");
		playerUnseen.removeWeapons("Probe");
		
		playerUnseen.removeSuspects("Spock");
		playerUnseen.removeSuspects("Android");
		playerUnseen.removeSuspects("Alien");
		
		//Room matches current location
		Solution mySol = new Solution("Spock","Probe","Bridge");
		player.setRow(14);
		player.setColumn(22);
		assertTrue(mySol.room == player.getRoom());
		
		//If only one weapon not seen, it's selected
		mySol = player.generateSolution();
		assertEquals(mySol.weapon, "Scapel");
		//If only one person not seen, it's selected (can be same test as weapon)
		assertEquals(mySol.person, "Captain");
		
		
		//If multiple weapons not seen, one of them is randomly selected
		//If multiple persons not seen, one of them is randomly selected
		Boolean LazerGuess = false;
		Boolean FlameThrowerGuess = false;
		Boolean ScapelGuess = false;
		Boolean TouristGuess = false;
		Boolean EngineerGuess = false;
		Boolean CaptainGuess = false;
		for(int i = 0; i < 100; i++)
		{
			mySol = playerUnseen.generateSolution();
			//Guessing weapons
			if(mySol.weapon == "Lazer") {
				LazerGuess = true;
			}
			if(mySol.weapon == "FlameThrower") {
				FlameThrowerGuess = true;
			}
			if(mySol.weapon == "Scapel") {
				ScapelGuess = true;
			}
			//Guessing People
			if(mySol.person == "Tourist") {
				TouristGuess = true;
			}
			if(mySol.person == "Engineer") {
				EngineerGuess = true;
			}
			if(mySol.person == "Captain") {
				CaptainGuess = true;
			}
		}
		assertTrue(LazerGuess);
		assertTrue(FlameThrowerGuess);
		assertTrue(ScapelGuess);
		assertTrue(TouristGuess);
		assertTrue(EngineerGuess);
		assertTrue(CaptainGuess);
	}

	@Test
	public void testHandleSuggestion() {

		HumanPlayer player1 = new HumanPlayer();
		ComputerPlayer player2 = new ComputerPlayer();
		ComputerPlayer player3 = new ComputerPlayer();
		ComputerPlayer player4 = new ComputerPlayer();
		
		Card player1card1 = new Card("Spock", CardType.PERSON);
		Card player1card2 = new Card("Freeze Gun", CardType.WEAPON);
		Card player1card3 = new Card("Trash Compactor", CardType.ROOM);
		
		Card player2card1 = new Card("Android", CardType.PERSON);
		Card player2card2 = new Card("Face Hugger", CardType.WEAPON);
		Card player2card3 = new Card("Bridge", CardType.ROOM);
		
		Card player3card1 = new Card("Alien", CardType.PERSON);
		Card player3card2 = new Card("Probe", CardType.WEAPON);
		Card player3card3 = new Card("Air Lock", CardType.ROOM);
		
		Card player4card1 = new Card("Tourist", CardType.PERSON);
		Card player4card2 = new Card("Lazer", CardType.WEAPON);
		Card player4card3 = new Card("Lounge", CardType.ROOM);
		
		Solution realBoi = new Solution("Engineer","FlameThrower","Sick Bay");
		board.theAnswer = realBoi;
		
		player1.addCard(player1card1);
		player1.addCard(player1card2);
		player1.addCard(player1card3);
		
		player2.addCard(player2card1);
		player2.addCard(player2card2);
		player2.addCard(player2card3);
		
		player3.addCard(player3card1);
		player3.addCard(player3card2);
		player3.addCard(player3card3);
		
		player4.addCard(player4card1);
		player4.addCard(player4card2);
		player4.addCard(player4card3);
		
		ArrayList<Player> tempPlayers = new ArrayList<Player>();
		board.setPlayers(tempPlayers);

		
		//Suggestion no one can disprove returns null
		Solution nullBoi = new Solution("Engineer","FlameThrower","Sick Bay");
		assertEquals(null, board.makeSuggestion(nullBoi, player1));
		//Suggestion only accusing player can disprove returns null
		Solution almostRight = new Solution("Android","FlameThrower","Sick Bay");
		assertEquals(null, board.makeSuggestion(almostRight, player2));
		//Suggestion only human can disprove returns answer (i.e., card that disproves suggestion)
		Solution closeEnough = new Solution("Spock","FlameThrower","Sick Bay");
		assertEquals("Spock", board.makeSuggestion(closeEnough, player3).getCardName());
		//Suggestion only human can disprove, but human is accuser, returns null
		assertEquals(null, board.makeSuggestion(closeEnough, player1));
		//Suggestion that two players can disprove, correct player (based on starting with next player in list) returns answer
		Solution twoWrong = new Solution("Alien","Lazer","Sick Bay");
		assertEquals("Alien", board.makeSuggestion(twoWrong, player2).getCardName());
		assertEquals("Alien", board.makeSuggestion(twoWrong, player2).getCardName());
		//Suggestion that human and another player can disprove, other player is next in list, ensure other player returns answer
		twoWrong = new Solution("Spock","Face Hugger","Sick Bay");
		assertEquals("Spock", board.makeSuggestion(twoWrong, player4).getCardName());
		
		
		
		//Disprove Suggestion
		//Player that can disprove
		Card wrongPerson = new Card("Spock", CardType.PERSON);
		Card wrongWeapon = new Card("Scapel", CardType.WEAPON);
		Card wrongRoom = new Card("Bridge", CardType.ROOM);
		
		player2.addCard(wrongWeapon);
		player2.addCard(wrongPerson);
		player2.addCard(wrongRoom);
		
		Solution soClose = new Solution("Engineer","Scapel","Sick Bay");
		Solution wrong = new Solution("Spock","Scapel","Bridge");
		//If player has only one matching card it should be returned
		assertEquals("Scapel", board.makeSuggestion(soClose, player1).getCardName());
		//If players has >1 matching card, returned card should be chosen randomly
		Card random1 = new Card("Spock", CardType.PERSON);
		Card random2 = new Card("Scapel", CardType.WEAPON);
		Card random3 = new Card("Bridge", CardType.ROOM);
		Boolean random1Chosen = false;
		Boolean random2Chosen = false;
		Boolean random3Chosen = false;
		
		for(int i = 0; i < 100; i++) {
			Card returned = board.makeSuggestion(wrong, player1);
			if(returned == random1) {
				random1Chosen = true;
			}
			if(returned == random2) {
				random2Chosen = true;
			}
			if(returned == random3) {
				random3Chosen = true;
			}
		}
		
		assertTrue(random1Chosen);
		assertTrue(random2Chosen);
		assertTrue(random3Chosen);
		
		//If player has no matching cards, null is returned
		assertEquals(null, board.makeSuggestion(realBoi, player1));
		
	}

}
