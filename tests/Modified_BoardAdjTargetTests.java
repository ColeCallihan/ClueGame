package tests;

/*
 * @author Cole Callihan, Carter Pasqualini
 * 
 * This program tests that adjacencies and targets are calculated correctly.
 */

import java.util.Set;

//Doing a static import allows me to write assertEquals rather than
//assertEquals
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class Modified_BoardAdjTargetTests {
	// We make the Board static because we can load it one time and 
	// then do all the tests. 
	private static Board board;
	@BeforeClass
	public static void setUp() {
		// Board is singleton, get the only instance
		board = Board.getInstance();
		// Using Our Config Files
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt");		
		// Initialize will load BOTH config files 
		board.initialize();
	}

	// Ensure that player does not move around within room
	// These cells are ORANGE on the planning spreadsheet
	@Test
	public void testAdjacenciesInsideRooms()
	{
		// Test a corner
		Set<BoardCell> testList = board.getAdjList(28, 0);
		assertEquals(0, testList.size());
		// Test one that has walkway underneath
		testList = board.getAdjList(7, 26);
		assertEquals(0, testList.size());
		// Test one that has walkway above
		testList = board.getAdjList(23, 0);
		assertEquals(0, testList.size());
		// Test one that is in middle of room
		testList = board.getAdjList(14, 25);
		assertEquals(0, testList.size());
		// Test one beside a door
		testList = board.getAdjList(10, 14);
		assertEquals(0, testList.size());
		// Test one in a corner of room
		testList = board.getAdjList(22, 13);
		assertEquals(0, testList.size());
	}

	// Ensure that the adjacency list from a doorway is only the
	// walkway. NOTE: This test could be merged with door 
	// direction test. 
	// These tests are DARK PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyRoomExit()
	{
		// TEST DOORWAY RIGHT 
		Set<BoardCell> testList = board.getAdjList(16, 7);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(16, 8)));
		// TEST DOORWAY LEFT 
		testList = board.getAdjList(16, 22);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(16, 21)));
		//TEST DOORWAY DOWN
		testList = board.getAdjList(7, 27);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 27)));
		//TEST DOORWAY UP
		testList = board.getAdjList(10, 15);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(9, 15)));
		//TEST DOORWAY RIGHT, WHERE THERE'S A WALKWAY ABOVE
		testList = board.getAdjList(23, 8);
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(22, 8)));
		
	}
	
	// Test adjacency at entrance to rooms
	// These tests are GREEN in planning spreadsheet
	@Test
	public void testAdjacencyDoorways()
	{
		// Test beside a door direction RIGHT
		Set<BoardCell> testList = board.getAdjList(1, 9);
		assertTrue(testList.contains(board.getCellAt(1, 8)));
		assertTrue(testList.contains(board.getCellAt(0, 9)));
		assertTrue(testList.contains(board.getCellAt(2, 9)));
		assertEquals(3, testList.size());
		// Test beside a door direction DOWN
		testList = board.getAdjList(9, 7);
		assertTrue(testList.contains(board.getCellAt(9, 6)));
		assertTrue(testList.contains(board.getCellAt(9, 8)));
		assertTrue(testList.contains(board.getCellAt(8, 7)));
		assertTrue(testList.contains(board.getCellAt(10, 7)));
		assertEquals(4, testList.size());
		// Test beside a door direction UP
		testList = board.getAdjList(9, 15);
		assertTrue(testList.contains(board.getCellAt(9, 16)));
		assertTrue(testList.contains(board.getCellAt(9, 4)));
		assertTrue(testList.contains(board.getCellAt(8, 15)));
		assertTrue(testList.contains(board.getCellAt(10, 15)));
		assertEquals(4, testList.size());
		// Test beside a door direction LEFT
		testList = board.getAdjList(14, 10);
		assertTrue(testList.contains(board.getCellAt(14, 9)));
		assertTrue(testList.contains(board.getCellAt(14, 11)));
		assertTrue(testList.contains(board.getCellAt(13, 10)));
		assertTrue(testList.contains(board.getCellAt(15, 10)));
		assertEquals(4, testList.size());
	}

	// Test a variety of walkway scenarios
	// These tests are LIGHT PURPLE on the planning spreadsheet
	@Test
	public void testAdjacencyWalkways()
	{
		// Test on top edge of board, just two walkway pieces
		Set<BoardCell> testList = board.getAdjList(0, 25);
		assertTrue(testList.contains(board.getCellAt(0, 24)));
		assertTrue(testList.contains(board.getCellAt(1, 25)));
		assertEquals(2, testList.size());
		
		// Test on left edge of board, two walkway pieces
		testList = board.getAdjList(10, 0);
		assertTrue(testList.contains(board.getCellAt(9, 0)));
		assertTrue(testList.contains(board.getCellAt(10, 1)));
		assertEquals(2, testList.size());

		// Test between two rooms, walkways up and down
		testList = board.getAdjList(5, 9);
		assertTrue(testList.contains(board.getCellAt(6, 9)));
		assertTrue(testList.contains(board.getCellAt(4, 9)));
		assertEquals(2, testList.size());

		// Test surrounded by 4 walkways
		testList = board.getAdjList(20, 9);
		assertTrue(testList.contains(board.getCellAt(20, 8)));
		assertTrue(testList.contains(board.getCellAt(20, 10)));
		assertTrue(testList.contains(board.getCellAt(19, 9)));
		assertTrue(testList.contains(board.getCellAt(21, 9)));
		assertEquals(4, testList.size());
		
		// Test on bottom edge of board, next to 1 room piece
		testList = board.getAdjList(28, 23);
		assertTrue(testList.contains(board.getCellAt(28, 22)));
		assertTrue(testList.contains(board.getCellAt(27, 23)));
		assertEquals(2, testList.size());
		
		// Test on right edge of board, next to 2 room pieces
		testList = board.getAdjList(17, 28);
		assertTrue(testList.contains(board.getCellAt(18, 28)));
		assertEquals(1, testList.size());

		// Test on walkway next to  door that is not in the needed
		// direction to enter
		testList = board.getAdjList(22, 8);
		assertTrue(testList.contains(board.getCellAt(22, 7)));
		assertTrue(testList.contains(board.getCellAt(22, 9)));
		assertTrue(testList.contains(board.getCellAt(21, 8)));
		assertEquals(3, testList.size());
	}
	
	
	// Tests of just walkways, 1 step, includes on edge of board
	// and beside room
	// Have already tested adjacency lists on all four edges, will
	// only test two edges here
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsOneStep() {
		board.calcTargets(21, 0, 1);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(20, 0)));
		assertTrue(targets.contains(board.getCellAt(21, 1)));	
		assertTrue(targets.contains(board.getCellAt(22, 0)));
		
		board.calcTargets(28, 9, 1);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(27, 9)));
			
	}
	
	// Tests of just walkways, 2 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsTwoSteps() {
		board.calcTargets(21, 0, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(20, 1)));
		assertTrue(targets.contains(board.getCellAt(21, 2)));
		assertTrue(targets.contains(board.getCellAt(22, 1)));
		
		board.calcTargets(28, 9, 2);
		targets= board.getTargets();
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(26, 9)));
		
	}
	
	// Tests of just walkways, 4 steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsFourSteps() {
		board.calcTargets(21, 0, 4);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(6, targets.size());
		assertTrue(targets.contains(board.getCellAt(21, 4)));
		assertTrue(targets.contains(board.getCellAt(20, 3)));
		assertTrue(targets.contains(board.getCellAt(22, 3)));
		assertTrue(targets.contains(board.getCellAt(20, 1)));
		assertTrue(targets.contains(board.getCellAt(21, 2)));
		assertTrue(targets.contains(board.getCellAt(22, 1)));
		
		// Includes a path that doesn't have enough length
		board.calcTargets(14, 0, 4);
		targets= board.getTargets();
		assertEquals(2, targets.size());
		assertTrue(targets.contains(board.getCellAt(25, 10)));
		assertTrue(targets.contains(board.getCellAt(24, 9)));	

	}	
	
	// Tests of just walkways plus one door, 6 steps
	// These are LIGHT BLUE on the planning spreadsheet

	@Test
	public void testTargetsSixSteps() {
		board.calcTargets(21, 0, 6);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(9, targets.size());
		//Could access with 4
		assertTrue(targets.contains(board.getCellAt(21, 4)));
		assertTrue(targets.contains(board.getCellAt(20, 3)));
		assertTrue(targets.contains(board.getCellAt(22, 3)));
		assertTrue(targets.contains(board.getCellAt(20, 1)));
		assertTrue(targets.contains(board.getCellAt(21, 2)));
		assertTrue(targets.contains(board.getCellAt(22, 1)));
		
		//New Access with 6 moves
		assertTrue(targets.contains(board.getCellAt(21, 6)));
		assertTrue(targets.contains(board.getCellAt(20, 5)));
		assertTrue(targets.contains(board.getCellAt(22, 5)));
	}	
	
	// Test getting into a room
	// These are LIGHT BLUE on the planning spreadsheet

	@Test 
	public void testTargetsIntoRoom()
	{
		// One room is exactly 2 away
		board.calcTargets(23, 20, 2);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(8, targets.size());
		// directly left and right
		assertTrue(targets.contains(board.getCellAt(23, 18)));
		assertTrue(targets.contains(board.getCellAt(23, 22)));
		// directly up and down
		assertTrue(targets.contains(board.getCellAt(21, 20)));
		assertTrue(targets.contains(board.getCellAt(25, 20)));
		// one up/down, one left/right
		assertTrue(targets.contains(board.getCellAt(22, 19)));
		assertTrue(targets.contains(board.getCellAt(24, 19)));
		assertTrue(targets.contains(board.getCellAt(24, 21)));
		assertTrue(targets.contains(board.getCellAt(22, 21)));
	}
	
	// Test getting into room, doesn't require all steps
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testTargetsIntoRoomShortcut() 
	{
		board.calcTargets(17, 8, 3);
		Set<BoardCell> targets= board.getTargets();
		assertEquals(12, targets.size());
		// directly up and down
		assertTrue(targets.contains(board.getCellAt(14, 8)));
		assertTrue(targets.contains(board.getCellAt(20, 8)));
		// Cannot go directly left or right
		// right then down
		assertTrue(targets.contains(board.getCellAt(18, 10)));
		assertTrue(targets.contains(board.getCellAt(19, 9)));
		// right then up
		assertTrue(targets.contains(board.getCellAt(16, 10)));
		assertTrue(targets.contains(board.getCellAt(15, 9)));
		// into the rooms
		assertTrue(targets.contains(board.getCellAt(15, 7)));
		assertTrue(targets.contains(board.getCellAt(16, 7)));		
		// 
		assertTrue(targets.contains(board.getCellAt(18, 8)));		
		assertTrue(targets.contains(board.getCellAt(16, 8)));		
		assertTrue(targets.contains(board.getCellAt(17, 9)));	
	}

	// Test getting out of a room
	// These are LIGHT BLUE on the planning spreadsheet
	@Test
	public void testRoomExit()
	{
		// Take one step, essentially just the adj list
		board.calcTargets(23, 8, 1);
		Set<BoardCell> targets= board.getTargets();
		// Ensure doesn't exit through the wall
		assertEquals(1, targets.size());
		assertTrue(targets.contains(board.getCellAt(23, 9)));
		// Take two steps
		board.calcTargets(23, 8, 2);
		targets= board.getTargets();
		assertEquals(3, targets.size());
		assertTrue(targets.contains(board.getCellAt(22, 9)));
		assertTrue(targets.contains(board.getCellAt(23, 10)));
		assertTrue(targets.contains(board.getCellAt(24, 9)));
	}

}
