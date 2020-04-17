package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

	private static String currentPlayerName = "";
	
	Board board = Board.getInstance();
	
	private JButton nextPlayer;
	private JButton makeAccusation;
	private static JTextField turnText;
	private static JTextField dieText;
	private static JTextField guessText;
	private static JTextField guessResultText;
	
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
		JButton playerButton = new JButton();

		//Adding next player button
		playerButton = createButtonPanelPlayer();
		upperButtonsAndTurn.add(playerButton);
		
		//Adding the accusation button
		JButton accusationButton = createButtonPanelAccusation();
		upperButtonsAndTurn.add(accusationButton);
		
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
		turnText = new JTextField();
		
		//Setting size and text box preferences
		turnText.setPreferredSize(new Dimension(150, 100));
		turnText.setEditable(false);
		turnText.setText(currentPlayerName);
		
		//Setting dimensions
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(150, 100));
		
		//Adding label to panel to be returned above text box
		panel.add(whoseTurn, BorderLayout.NORTH);
		panel.add(turnText);
		
		return panel;
	}

	//Creates Dice Panel
	private JPanel createDiePanel() {

		//Initial Constructors
		JLabel label = new JLabel("Roll");
		JPanel panel = new JPanel();
		dieText = new JTextField();
		
		//Settings dimensions and size
		dieText.setPreferredSize(new Dimension(50, 20));
		dieText.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Die"));
		
		//Adding to return panel
		panel.add(label);
		panel.add(dieText);
		
		return panel;
	}

	//Creates Guess Panel
	private JPanel createGuessPanel() {
		//Initial Constructors
		JLabel label = new JLabel("Guess");
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(300, 80));
		guessText = new JTextField();
		
		//Settings dimensions and size
		guessText.setPreferredSize(new Dimension(500, 25));
		guessText.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Guess"));
		
		//Adding to return panel
		panel.add(label, BorderLayout.CENTER);
		panel.add(guessText, BorderLayout.SOUTH);
		return panel;
	}
	
	//Creates Guess Result
	private JPanel createGuessResult() {
		//Initial Constructors
		JLabel label = new JLabel("Response");
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(300, 60));
		guessResultText = new JTextField();
		
		//Settings dimensions and size
		guessResultText.setPreferredSize(new Dimension(150, 25));
		guessResultText.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Guess Result"));
		
		//Adding to return panel
		panel.add(label);
		panel.add(guessResultText);
		
		return panel;
		
	}

	//Creates the buttons
	private JButton createButtonPanelPlayer() {
		//Initial Constructors
		nextPlayer = new JButton("Next player");
		
		class NextPlayerButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				if(board.getCurrentPlayer().getStatus().equals("Human")) {
					if(board.getCurrentPlayerIndex() == -1) {
						board.advanceNextPlayer();
						board.startNextTurn();
						updatePanelDiceAndPlayer();
						if(board.getCurrentPlayer().getStatus().equals("Human")){
							board.repaint();
							//suggestion possibility
							
						}
						//updatePanelGuessAndResult();
					}
					else if(board.getCurrentPlayer().getDoneTurn() == false) {
						System.out.println("You cannot advance to next player yet");
					}
					else {
						board.advanceNextPlayer();
						board.startNextTurn();
						updatePanelDiceAndPlayer();
						if(board.getCurrentPlayer().getStatus().equals("Human")){
							board.repaint();
							//suggestion possibility
							
						}
						//updatePanelGuessAndResult();
						System.out.println(board.getCurrentPlayerIndex());
					}
				}
				else {
					if(board.getCurrentPlayer().getStatus().equals("Computer")) {
						board.startNextTurn();
						System.out.println(board.getCurrentPlayerIndex());
						System.out.println("current player is" + board.getPlayers().get(board.getCurrentPlayerIndex()));
						updatePanelDiceAndPlayer();
						board.getCurrentPlayer().makeMove(board.getTargets());
						board.repaint();
						//updatePanelGuessAndResult();
						board.advanceNextPlayer();
					}
				}
			}
		}
		board.repaint();
		
		nextPlayer.addActionListener(new NextPlayerButtonListener());
		
		return nextPlayer;
	}
	
	private JButton createButtonPanelAccusation() {
		//Initial Constructors
		makeAccusation = new JButton("Make an accusation");
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

	public static void setTurnText(String playerName) {
		turnText.setText(playerName);
	}
	
	public static void setGuessText(String playerGuess) {
		guessText.setText(playerGuess);
	}
	
	public static void setGuessResultText(String playerGuess) {
		guessResultText.setText(playerGuess);
	}

	public static void setDieText(int dieRoll) {
		dieText.setText(Integer.toString(dieRoll));
	}
	
	public void updatePanelGuessAndResult() {
		setGuessText(board.getGuess());
		setGuessResultText(board.getGuessResult());
	}
	
	public void updatePanelDiceAndPlayer() {
		setDieText(board.getCurrentRoll());
		setTurnText(board.getCurrentPlayerName());
	}
}