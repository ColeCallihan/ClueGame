/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * IntBoardTests class that tests possible adjacency calculations and target calculations
 */
package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import experiment.TestBoardCell;
import experiment.IntBoard;

import org.junit.*;

class IntBoardTests {
	//Instance variable board that is defines in the before method.
	IntBoard board;
	
	/*
	 * The beforeAll method instantiates the IntBoard that is used before each test is executed
	 */
	@BeforeEach
	public void beforeAll() {
		
		Set<TestBoardCell> initialCells = new HashSet<TestBoardCell>();//instantiates a set of empty BoardCells
		
		for(int i = 0; i < 4; i++)
		{
			for(int j = 0; j < 4; j++)
				initialCells.add(new TestBoardCell(i,j));//Defines the row and column for each BoardCell in initialCells
		}
		
		board = new IntBoard(initialCells);//instantiates the board with the new set of BoardCells
	}

	/*
	 * Test adjacencies for top left corner
	 */
	@Test
	public void testAdjacency0()
	{
		TestBoardCell cell = board.getCell(0,0);//Retrieves a cell at 0,0
		Set<TestBoardCell> testList = board.getAdjList(cell);//retrieves the adjacency list of 0,0
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	/*
	 * Test adjacencies for bottom right corner
	 */
	@Test
	public void testAdjacency1()
	{
		TestBoardCell cell = board.getCell(3,3);//Retrieves a cell at 3,3
		Set<TestBoardCell> testList = board.getAdjList(cell);//retrieves the adjacency list of 3,3
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertEquals(2, testList.size());
	}
	/*
	 * Test adjacencies for a right edge
	 */
	@Test
	public void testAdjacency2()
	{
		TestBoardCell cell = board.getCell(1,3);//Retrieves a cell at 1,3
		Set<TestBoardCell> testList = board.getAdjList(cell);//retrieves the adjacency list of 1,3
		assertTrue(testList.contains(board.getCell(0, 3)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(3, testList.size());
	}
	/*
	 * Test adjacencies for a left edge
	 */
	@Test
	public void testAdjacency3()
	{
		TestBoardCell cell = board.getCell(1,0);//Retrieves a cell at 1,0
		Set<TestBoardCell> testList = board.getAdjList(cell);//retrieves the adjacency list of 1,0
		assertTrue(testList.contains(board.getCell(1, 1)));
		assertTrue(testList.contains(board.getCell(2, 0)));
		assertTrue(testList.contains(board.getCell(0, 0)));
		assertEquals(3, testList.size());
	}
	/*
	 * Test adjacencies for the second column
	 */
	@Test
	public void testAdjacency4()
	{
		TestBoardCell cell = board.getCell(1,1);//Retrieves a cell at 1,1
		Set<TestBoardCell> testList = board.getAdjList(cell);//retrieves the adjacency list of 1,1
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(4, testList.size());
	}
	/*
	 * Test adjacencies for the third column
	 */
	@Test
	public void testAdjacency5()
	{
		TestBoardCell cell = board.getCell(2,2);//Retrieves a cell at 2,2
		Set<TestBoardCell> testList = board.getAdjList(cell);//retrieves the adjacency list of 2,2
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(4, testList.size());
	}
	/*
	 * Tests targets 1 away from the point 0,0
	 */
	@Test
	public void testTargets0_1()
	{
		TestBoardCell cell = board.getCell(0, 0);//retrieves the cell at position 0,0
		board.calcTargets(cell, 1);//calculates the list of targets 1 away from the position and stores them into the targets list
		Set<TestBoardCell> targets = board.getTargets();//retrieves the list of targets
		assertEquals(2, targets.size());
		
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));

	}
	/*
	 * Tests targets 2 away from the point 0,0
	 */
	@Test
	public void testTargets0_2()
	{
		TestBoardCell cell = board.getCell(0, 0);//retrieves the cell at position 0,0
		board.calcTargets(cell, 2);//calculates the list of targets 2 away from the position and stores them into the targets list
		Set<TestBoardCell> targets = board.getTargets();//retrieves the list of targets
		assertEquals(3, targets.size());
		
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));

	}
	/*
	 * Tests targets 3 away from the point 0,0
	 */
	@Test
	public void testTargets0_3()
	{
		TestBoardCell cell = board.getCell(0, 0);//retrieves the cell at position 0,0
		board.calcTargets(cell, 3);//calculates the list of targets 3 away from the position and stores them into the targets list
		Set<TestBoardCell> targets = board.getTargets();//retrieves the list of targets
		assertEquals(6, targets.size());
		
		//Valid for #3
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		
		//Valid for #1
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 1)));
	}
	/*
	 * Tests targets 4 away from the point 0,0
	 */
	@Test
	public void testTargets0_4()
	{
		TestBoardCell cell = board.getCell(0, 0);//retrieves the cell at position 0,0
		board.calcTargets(cell, 4);//calculates the list of targets 4 away from the position and stores them into the targets list
		Set<TestBoardCell> targets = board.getTargets();//retrieves the list of targets
		assertEquals(6, targets.size());
		
		//Valid for #4
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		
		//Valid for #2
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
	}
	/*
	 * Tests targets 5 away from the point 0,0
	 */
	@Test
	public void testTargets0_5()
	{
		TestBoardCell cell = board.getCell(0, 0);//retrieves the cell at position 0,0
		board.calcTargets(cell, 5);//calculates the list of targets 5 away from the position and stores them into the targets list
		Set<TestBoardCell> targets = board.getTargets();//retrieves the list of targets
		assertEquals(8, targets.size());

		//Valid for #1	
		assertTrue(targets.contains(board.getCell(0, 1)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		
		//Valid for #3
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(0, 3)));
		
		//Valid for #5
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
	/*
	 * Tests targets 6 away from the point 0,0
	 */
	@Test
	public void testTargets0_6()
	{
		TestBoardCell cell = board.getCell(0, 0);//retrieves the cell at position 0,0
		board.calcTargets(cell, 6);//calculates the list of targets 6 away from the position and stores them into the targets list
		Set<TestBoardCell> targets = board.getTargets();//retrieves the list of targets
		assertEquals(7, targets.size());

		//Valid for #6
		assertTrue(targets.contains(board.getCell(3, 3)));
		
		//Valid for #4
		assertTrue(targets.contains(board.getCell(3, 1)));
		assertTrue(targets.contains(board.getCell(2, 2)));
		assertTrue(targets.contains(board.getCell(1, 3)));
		
		//Valid for #2
		assertTrue(targets.contains(board.getCell(0, 2)));
		assertTrue(targets.contains(board.getCell(1, 1)));
		assertTrue(targets.contains(board.getCell(2, 0)));
	}
}
