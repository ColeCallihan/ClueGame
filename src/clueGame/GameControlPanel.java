package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

// @author: Cole Callihan and Carter Pasqualini
// This is the basic Layout for our GUI for ClueGame
// Section B

public class GameControlPanel extends JPanel {

	//Adds components to the window
	public GameControlPanel()
	{
		JPanel boardPanel = new JPanel();
		boardPanel.setPreferredSize(new Dimension(1008,550));
		boardPanel.setBorder(new TitledBorder(new EtchedBorder(),"Board"));
		
		//Creates the whole panel
		createLowerInformationPanel();
	}

	public void createLowerInformationPanel() {
		//Creates large panel with two rows and infinite columns to house upper and lower parts of the bottom of the screen
		JPanel outerTwoRows = new JPanel();
		outerTwoRows.setLayout(new GridLayout(2,0));
		
		//Upper panel housing the buttons and turn box
		JPanel upperButtonsAndTurn = new JPanel();
		upperButtonsAndTurn.setLayout(new GridLayout(1,0));
		
		//Creating initial panel and making turn labels, adds them to the upper panel
		JPanel panel = createTurnLabel();
		upperButtonsAndTurn.add(panel);

		//Creates buttons to add to the upper panel
		JButton currentButton = new JButton();

		//Adding next player button
		currentButton = createButtonPanelPlayer();
		upperButtonsAndTurn.add(currentButton);
		
		//Adding the accusation button
		currentButton = createButtonPanelAccusation();
		upperButtonsAndTurn.add(currentButton);
		
		//Adds buttons to the upper panel
		outerTwoRows.add(upperButtonsAndTurn);
		
		//Creates panel for lower text boxes
		JPanel lowerDieAndGuesses = new JPanel();
		
		//Adding Dice Panel
		panel = createDiePanel();
		lowerDieAndGuesses.add(panel);

		//Adding Guess made panel
		panel = createGuessPanel();
		lowerDieAndGuesses.add(panel);

		//Adding Guess Result panel
		panel = createGuessResult();
		lowerDieAndGuesses.add(panel);
		
		//Adds lower panel to the large 2 row GridLayout
		outerTwoRows.add(lowerDieAndGuesses);
		
		//Adds the entire 2 row GridLayout panel to the JFrame
		add(outerTwoRows, BorderLayout.SOUTH);
	}

	//Creates turn label
	private JPanel createTurnLabel() {

		//Initial Constructors
		JLabel whoseTurn = new JLabel("Whose Turn?");
		JPanel panel = new JPanel();
		JTextField text = new JTextField();
		
		//Setting size and text box preferences
		text.setPreferredSize(new Dimension(150, 100));
		text.setEditable(false);
		
		//Setting dimensions
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(150, 100));
		
		//Adding label to panel to be returned above text box
		panel.add(whoseTurn, BorderLayout.NORTH);
		panel.add(text);
		
		return panel;
	}

	//Creates Dice Panel
	private JPanel createDiePanel() {

		//Initial Constructors
		JLabel label = new JLabel("Roll");
		JPanel panel = new JPanel();
		JTextField text = new JTextField();
		
		//Settings dimensions and size
		text.setPreferredSize(new Dimension(50, 20));
		text.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Die"));
		
		//Adding to return panel
		panel.add(label);
		panel.add(text);
		
		return panel;
	}

	//Creates Guess Panel
	private JPanel createGuessPanel() {
		//Initial Constructors
		JLabel label = new JLabel("Guess");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(300, 80));
		JTextField text = new JTextField();
		
		//Settings dimensions and size
		text.setPreferredSize(new Dimension(500, 25));
		text.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Guess"));
		
		//Adding to return panel
		panel.add(label, BorderLayout.CENTER);
		panel.add(text, BorderLayout.SOUTH);
		return panel;
	}
	
	//Creates Guess Result
	private JPanel createGuessResult() {
		//Initial Constructors
		JLabel label = new JLabel("Response");
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 60));
		JTextField text = new JTextField();
		
		//Settings dimensions and size
		text.setPreferredSize(new Dimension(150, 25));
		text.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Guess Result"));
		
		//Adding to return panel
		panel.add(label);
		panel.add(text);
		
		return panel;
		
	}

	//Creates the buttons
	private JButton createButtonPanelPlayer() {
		//Initial Constructors
		JButton nextPlayer = new JButton("Next player");
		//JPanel panel = new JPanel();
		
		//Setting button dimensions
		//nextPlayer.setPreferredSize(new Dimension(150, 80));
		
		//Making sure buttons are side by side
		//panel.setLayout(new GridLayout(1,2));
		//panel.setPreferredSize(new Dimension(400, 100));
		
		//Adding to return panel
		//panel.add(nextPlayer);
		
		return nextPlayer;
	}
	
	private JButton createButtonPanelAccusation() {
		//Initial Constructors
		JButton makeAccusation = new JButton("Make an accusation");
		//JPanel panel = new JPanel();
		
		//Setting button dimensions
		//makeAccusation.setPreferredSize(new Dimension(150, 80));
		
		//Adding to return panel
		//panel.add(makeAccusation);
		
		return makeAccusation;
	}

	
	//Opens and sets window preferences
	public static void main(String[] args) {

		// Create the JFrame
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ClueGameGUI");
		frame.setSize(1920, 825);

		// Create the JPanel and add it to the JFrame
		GameControlPanel gui = new GameControlPanel();
		frame.add(gui, BorderLayout.CENTER);

		frame.setVisible(true);
	}


}