package GUI;

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

public class ClueGameGUI extends JPanel {

	//Adds components to the window
	public ClueGameGUI()
	{
		//Creating initial panel and making turn labels
		JPanel panel = createTurnLabel();
		
		//Creating panel size for each individual one
		panel.setPreferredSize(new Dimension(500, 100));
		
		//Adding whose turn it is
		//Adding borderlayout.south so it stays at bottom of screen when more is added
		add(panel, BorderLayout.SOUTH);

		//Adding next player and accusation buttons
		panel = createButtonPanel();
		add(panel, BorderLayout.SOUTH);
		
		//Adding Dice Panel
		panel = createDiePanel();
		add(panel, BorderLayout.SOUTH);

		//Adding Guess made panel
		panel = createGuessPanel();
		add(panel, BorderLayout.SOUTH);

		//Adding Guess Result panel
		panel = createGuessResult();
		add(panel, BorderLayout.SOUTH);

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
		text.setPreferredSize(new Dimension(150, 100));
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
		JTextField text = new JTextField();
		
		//Settings dimensions and size
		text.setPreferredSize(new Dimension(150, 100));
		text.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Guess"));
		
		//Adding to return panel
		panel.add(label);
		panel.add(text);
		return panel;
	}
	
	//Creates Guess Result
	private JPanel createGuessResult() {
		//Initial Constructors
		JLabel label = new JLabel("Response");
		JPanel panel = new JPanel();
		JTextField text = new JTextField();
		
		//Settings dimensions and size
		text.setPreferredSize(new Dimension(150, 100));
		text.setEditable(false);
		
		//Setting Border Label
		panel.setBorder(new TitledBorder(new EtchedBorder(),"Guess Result"));
		
		//Adding to return panel
		panel.add(label);
		panel.add(text);
		
		return panel;
		
	}

	//Creates the buttons
	private JPanel createButtonPanel() {
		//Initial Constructors
		JButton nextPlayer = new JButton("Next player");
		JButton makeAccusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		
		//Setting button dimensions
		nextPlayer.setPreferredSize(new Dimension(150, 80));
		makeAccusation.setPreferredSize(new Dimension(150, 80));
		
		//Making sure buttons are side by side
		panel.setLayout(new GridLayout(1,2));
		panel.setPreferredSize(new Dimension(400, 100));
		
		//Adding to return panel
		panel.add(nextPlayer);
		panel.add(makeAccusation);
		
		return panel;
	}

	//Opens and sets window preferrences
	public static void main(String[] args) {

		// Create the JFrame
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("ClueGameGUI");
		frame.setSize(1920, 1080);

		// Create the JPanel and add it to the JFrame
		ClueGameGUI gui = new ClueGameGUI();
		frame.add(gui, BorderLayout.CENTER);

		frame.setVisible(true);
	}


}
