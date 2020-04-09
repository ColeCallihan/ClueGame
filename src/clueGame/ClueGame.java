/*
 * @authors Cole Callihan, Carter Pasqualini
 * 
 * ClueGame runs the GUI that manages and displays game interactions
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ClueGame extends JFrame{

	//Sets instance variables for each panel and the static board
	private DetectiveNotes playerNotes;
	private GameControlPanel controlPanel;
	Board board;
	
	/*
	 * Constructor for ClueGame. Creates all of the panels, windows, and sets config files
	 */
	public ClueGame() {
		//Sets preferences for the JFrame
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Clue Game");
		setSize(960, 825);
		
		//Creates the game control panel at the bottom of the GUI
		controlPanel = new GameControlPanel();
		add(controlPanel, BorderLayout.SOUTH);
		
		//Sets the game's config files and draws the board in the center of the GUI
		board = Board.getInstance();
		board.setConfigFiles("SpaceStationBoard.csv", "ClueRooms.txt.txt", "SpacePlayers.txt", "SpaceCards.txt");
		board.initialize();
		add(board, BorderLayout.CENTER);
		
		//Creates the menu bar in the top left of the GUI
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(createFileMenu());
	}
	
	/*
	 * Sets the File tab of the GUI's menu bar
	 */
	private JMenu createFileMenu() {
		//Creates a new menu and adds the detective notes and exit options
		JMenu menu = new JMenu("File");
		menu.add(createDetectiveNotesItem());
		menu.add(createFileExitItem());
		return menu;
	}
	
	/*
	 * Creates the Detective notes option that opens a dialog box with the detective notes
	 */
	private JMenuItem createDetectiveNotesItem() {
		JMenuItem detectiveNotes = new JMenuItem("Detective Notes");
		
		//Allows the option to be clicked and opens the the notes
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				playerNotes = new DetectiveNotes();
				playerNotes.setVisible(true);
			}
		}
		detectiveNotes.addActionListener(new MenuItemListener());
		
		return detectiveNotes;
	}
	
	/*
	 * Creates the exit option in the file menu tab
	 */
	private JMenuItem createFileExitItem() {
		JMenuItem item = new JMenuItem("Exit");
		
		//Allows the exit option to close the GUI
		class MenuItemListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		}
		item.addActionListener(new MenuItemListener());
		
		return item;
	}
	
	/*
	 * Runs the ClueGame by setting the GUI to visible and calling the constructor
	 */
	public static void main(String[] args) {
		// Create the JFrame
		ClueGame frame = new ClueGame();


		frame.setVisible(true);
	}

}
