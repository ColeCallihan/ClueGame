package clueGame;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.junit.BeforeClass;

public class MainTest {
	
	private static final int NUM_CARDS_PER_PLAYER = 3;
	private static Board board;
	
	private static ArrayList<Card> deck = new ArrayList<Card>();
	private static ArrayList<Player> allPlayers = new ArrayList<Player>();
	
	public static void main(String[] args) throws FileNotFoundException, BadConfigFormatException {
		board = Board.getInstance();
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt", "SpacePlayers.txt", "SpaceCards.txt");
		
		board.initialize();
		
		board.loadPeople();
		board.loadCards();
		board.dealCards();

		deck = board.getDeck();
		allPlayers = board.getPlayers();
		
		for(Player p : allPlayers) {
			System.out.println(allPlayers.size());
		}
	}
}
