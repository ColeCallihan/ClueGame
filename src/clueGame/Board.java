/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * Board class to run it all
 */ 
package clueGame;

import java.util.*;

public class Board {
	private int numRows;
	private int numColumns;
	public final int MAX_BOARD_SIZE = 50;
	private BoardCell[][] board;
	private Map<Character, String> legend;
	private Map<BoardCell, Set<BoardCell>> adjMatrix;
	private Set<BoardCell> targets;
	private String boardConfigFile;
	private String roomConfigFile;
	
	public static Board getInstance() {
		return null;
	}
	public void initialize() {
		
	}
	public void loadRoomConfig() {
		
	}
	public void loadBoardConfig() {
		
	}
	public void calcAdjacencies() {
		
	}
	public void calcTargets(BoardCell cell, int pathLength) {
		
	}
	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
		
	}
	public Map<Character, String> getLegend() {
		// TODO Auto-generated method stub
		return null;
	}
	public int getNumRows() {
		// TODO Auto-generated method stub
		return 1;
	}
	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 2;
	}
	public BoardCell getCellAt(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}
}
