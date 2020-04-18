package clueGame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

// @author: Cole Callihan and Carter Pasqualini
// This is the basic Layout for our GUI for ClueGame
// Section B

public class GameControlPanel extends JPanel {

	private static String currentPlayerName = "";

	static Board board = Board.getInstance();

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
		panel.setPreferredSize(new Dimension(400, 80));
		guessText = new JTextField();

		//Settings dimensions and size
		guessText.setPreferredSize(new Dimension(600, 25));
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

		System.out.println(board.theAnswer.person + " " + board.theAnswer.weapon + " " + board.theAnswer.room );
		//Creates a button listener that will increment to the next player's turn if they are finished
		class NextPlayerButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				//If the current player is a human
				if(board.getCurrentPlayer().getStatus().equals("Human")) {
					//If it is the beginning of the game, the current player is the first player, and start their turn
					if(board.getCurrentPlayerIndex() == -1) {
						board.advanceNextPlayer();
						board.getCurrentPlayer().setStartTurn(true);
						board.startNextTurn();
						updatePanelDiceAndPlayer();
						//if the current player is human, repaint the board to display the targets
						if(board.getCurrentPlayer().getStatus().equals("Human")){
							board.repaint();
							//suggestion possibility

						}
						updatePanelGuessAndResult();
					}
					//if the human player is not done with their turn, display an error message
					else if(board.getCurrentPlayer().getDoneTurn() == false) {
						//System.out.println("You cannot advance to next player yet");
						JOptionPane splashScreen = new JOptionPane();
						splashScreen.showMessageDialog(Board.getInstance(), "You need to finish your turn!", "Error", JOptionPane.INFORMATION_MESSAGE);
					}
					//if the human player is done with their turn, advance to the next player and start the next turn
					else {
						board.getCurrentPlayer().setDoneTurn(false);
						board.advanceNextPlayer();
						board.startNextTurn();
						updatePanelDiceAndPlayer();
						updatePanelGuessAndResult();
						//if the next player is human, repaint their new location on the board and reset their doneTurns status
						if(board.getCurrentPlayer().getStatus().equals("Human")){
							board.repaint();
							board.getCurrentPlayer().setDoneTurn(false);
						}
						//the next player is not human, so just make them move and repaint their new location
						else {
							board.getCurrentPlayer().makeMove(board.getTargets());
							if(board.getCurrentPlayer().getCurrentRoom().isDoorway()) {
								board.handleSuggestion(board.getCurrentPlayer().generateSolution(), board.getCurrentPlayer());
							}
							board.repaint();
						}
						updatePanelGuessAndResult();
						//System.out.println(board.getCurrentPlayerIndex());
					}
				}
				//If the current player is not a human
				else {
					//Double checks the current player is a computer, then advances to the next player, running their turn
					//If the next player is a human, it just sets up their turn
					if(board.getCurrentPlayer().getStatus().equals("Computer")) {
						board.advanceNextPlayer();
						board.getCurrentPlayer().setStartTurn(true);
						board.startNextTurn();
						//System.out.println(board.getCurrentPlayerIndex());
						//System.out.println("current player is" + board.getPlayers().get(board.getCurrentPlayerIndex()));
						updatePanelDiceAndPlayer();
						//If the next player is a computer, force the computer to move
						if(board.getCurrentPlayer().getStatus().equals("Computer")) {
							board.getCurrentPlayer().makeMove(board.getTargets());
							if(board.getCurrentPlayer().getCurrentRoom().isDoorway()) {
								board.handleSuggestion(board.getCurrentPlayer().generateSolution(), board.getCurrentPlayer());
								updatePanelGuessAndResult();
							}
						}
						//Mostly updating stuff
						board.repaint();
						updatePanelGuessAndResult();
					}
				}
			}
		}
		//board.repaint();

		//Adds the button listener to the button
		nextPlayer.addActionListener(new NextPlayerButtonListener());

		return nextPlayer;
	}





	/*
	 * Creates the accusation button
	 */
	private JButton createButtonPanelAccusation() {
		//Initial Constructors
		makeAccusation = new JButton("Make an accusation");

		//If they click the accusation button
		class AccButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				board.getAccusationFromPlayer();
			}
		}
		//Adds it to the button
		makeAccusation.addActionListener(new AccButtonListener());
		
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

	/*
	 * sets the text of the current turn text box
	 */
	public static void setTurnText(String playerName) {
		turnText.setText(playerName);
	}

	/*
	 * Sets the text of the current guess text box
	 */
	public static void setGuessText(String playerGuess) {
		if(playerGuess == null)
		{
			guessText.setText("No Current Guess");
		}
		else {
			guessText.setText(playerGuess);
		}
	}

	/*
	 * Sets the text of the current guess result text box
	 */
	public static void setGuessResultText(String playerGuess) {
		if(playerGuess == null)
		{
			guessResultText.setText("No Card Returned");
		}
		else {
			guessResultText.setText(playerGuess);
		}

	}

	/*
	 * Sets the text of the die text box
	 */
	public static void setDieText(int dieRoll) {
		dieText.setText(Integer.toString(dieRoll));
	}

	/*
	 * Sets the text for the guess and guess result text boxes
	 */
	public static void updatePanelGuessAndResult() {
		setGuessText(board.getGuess());
		setGuessResultText(board.getGuessResult());
	}

	/*
	 * Sets the text for the die and current player turn text boxes
	 */
	public void updatePanelDiceAndPlayer() {
		setDieText(board.getCurrentRoll());
		setTurnText(board.getCurrentPlayerName());
	}
}