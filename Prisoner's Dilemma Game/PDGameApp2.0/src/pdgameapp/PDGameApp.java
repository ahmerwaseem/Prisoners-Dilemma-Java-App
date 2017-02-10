/**
 * Name: Ahmer Waseem & Jade Steffen
 * Assignment: 3
 * Date Due: 3/23/15
 * Program: Prisoner Dilemma's game that allows user to pick computers strategy
 * for each game played, allows user to pick his choice every round, and calculates
 * amount of years each player gets from those choices. Results are displayed when
 * user doesn't want to play a new game.
 */

package pdgameapp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.*;
import java.awt.event.*;
import java.awt.event.ActionListener; 
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

/*****************************************
 * This PDGameApp class is newed up each 
 * time a user starts a new game. It
 * contains all the information needed
 * to handle an instance of a single game.
 *******************************************/
public class PDGameApp extends JFrame{   

    // Define Swing UI Elements
    private final JButton startB = new JButton("Start Game");  //pointer to jbutton
    private final JButton silentButton = new JButton("Remain Silent");
    private final JButton testifyButton = new JButton("Testify");
    private JTextField playerSentenceTF   = new JTextField(10);   
    private JTextField roundsPlayedTF     = new JTextField(10);
    private JTextField computerSentenceTF = new JTextField(10);
    private JTextField computerStrategyTF = new JTextField(10);
    private JTextField winnerTF           = new JTextField(10);
    private static final DefaultListModel<String> listModel = new DefaultListModel<>(); //to hold finished games
    private static final JList finishedGamesList = new JList(listModel);          //when filled in will hold list of completed games in upper left corner
    private static final JComboBox  computerStrategyBox = new JComboBox<>();    //when filled in will hold pointer to a box that lists the 5 strategies
    protected JTextArea output = new JTextArea(15,35);    //GUI output
    protected PDGame currentgamePD = null;                //filled in the constructor and also in start button logic 
    HashMap<String,GameStat> statsMap = new HashMap<>();  // Holds GameStat Objects by Time key
    GameStat GS = null; // Will hold gamestat object to track game stats
    String gameStartTime;
    int rounds = 0; // Number of rounds played
    
    // Constructor - GUI is built
    PDGameApp() {                                        
        super("Prisoner's  Dillemma");               //call frame class constructor,recommended by java
        currentgamePD = new PDGame("strategy.txt");  //fills in pointer up top
        setLayout(new BorderLayout());               //set layout for frame, no reference is needed here since we are in a non-static method 
        Container contentPanel = getContentPane();
        contentPanel.setBackground(Color.black);

        //create and add west panel
        JPanel panelLeft = new JPanel(new BorderLayout());       //then set color, borders on panel1
        TitledBorder titled = new TitledBorder("List of Games"); // set title
        panelLeft.setBorder(titled);                             // add titled border
        panelLeft.setBackground(new Color(149,228,175));         // set color
        contentPanel.add(panelLeft,BorderLayout.WEST);

        JPanel panelRight = new JPanel(new BorderLayout());      // then set color, borders on panel1
        panelRight.setBackground(new Color(153,204,255));        // set color
        contentPanel.add(panelRight, BorderLayout.EAST);         // set layout

        finishedGamesList.setVisibleRowCount(10);                // set row count
        finishedGamesList.setFixedCellWidth(350);                // and cell width

        
        JScrollPane outputPane = new JScrollPane(output);        // new scroll pane
        outputPane.setPreferredSize(new Dimension(400,250 ));    // size
        panelRight.add(outputPane, BorderLayout.SOUTH);          // add

        // Set up scrolling on games list
        JScrollPane gamesList = new JScrollPane(finishedGamesList);
        panelLeft.add(gamesList, BorderLayout.NORTH); 
        panelLeft.setPreferredSize(new Dimension(450, 375));
        panelRight.setPreferredSize(new Dimension(445, 375));

        // Add game stats panel
        JPanel panelLeftBottom = new JPanel(new GridLayout(5,2));
        panelLeftBottom.setOpaque(false);
        panelLeftBottom.setPreferredSize(new Dimension(225, 125));

        // Add rounds played
        panelLeftBottom.add(new JLabel("Rounds Played"));
        roundsPlayedTF.setEditable(false);
        panelLeftBottom.add(roundsPlayedTF);

        // Add computer strategy
        panelLeftBottom.add(new JLabel("Computer Strategy"));
        computerStrategyTF.setEditable(false);
        panelLeftBottom.add(computerStrategyTF);

        // Add player sentence
        panelLeftBottom.add(new JLabel("Player Sentence"));
        playerSentenceTF.setEditable(false);
        panelLeftBottom.add(playerSentenceTF);

        // Add computer sentence
        panelLeftBottom.add(new JLabel("Computer Sentence"));
        computerSentenceTF.setEditable(false);
        panelLeftBottom.add(computerSentenceTF);

        // Add winner
        panelLeftBottom.add(new JLabel("Winner"));
        winnerTF.setEditable(false);
        panelLeftBottom.add(winnerTF);
        panelLeft.add(panelLeftBottom, BorderLayout.SOUTH);
        
        // Add top right panel
        JPanel panelTopRight = new JPanel(new GridLayout(2,1));
        panelTopRight.setOpaque(false);
        panelTopRight.setPreferredSize(new Dimension(400, 75));
        
        // Strategy Panel
        JPanel panelStrategy = new JPanel();
        panelStrategy.setOpaque(false);
        panelStrategy.add(new JLabel("Computer Strategy"));
        panelStrategy.add(computerStrategyBox);
        panelStrategy.add(startB);
        
        // Decision/interaction panel
        JPanel panelDecision = new JPanel();
        panelDecision.setOpaque(false);
        panelDecision.add(new JLabel("Your decision this round?"));
        panelDecision.add(silentButton);
        panelDecision.add(testifyButton);
        panelDecision.add(new JButton("Your decision this round?"));
        
        // Add sub-panels
        panelTopRight.add(panelStrategy);
        panelTopRight.add(panelDecision);        
        panelRight.add(panelTopRight, BorderLayout.NORTH);
        
        // Add Listeners
        startB.addActionListener( new startGameEvent() );
        silentButton.addActionListener( new cooperateEvent() );
        testifyButton.addActionListener( new testifyEvent() );
        finishedGamesList.addListSelectionListener(listSelectionListener);
        
        // Set Buttons Disabled
        silentButton.setEnabled( false );
        testifyButton.setEnabled( false );
        
        // Pack frame
        pack();  
    }

    
    // Program Entry Point Below
    public static void main(String[] args) {
        
        // All the setup we need is located below
        createAndShowGUI();
        populateStrategyBox();
  
    }
    
    //***************************************************************************
    // Name:        writeOutput
    // Purpose:     Simple helper function, appends output to text area in the GUI
    // Parameters:  String output
    // Return:      void
    //***************************************************************************
    public void writeOutput( String outputS ) {
        output.append(outputS);
    }
    
    //***************************************************************************
    // Name:        displayPrompt
    // Purpose:     Simpler helper function. Appends prompt to text area in GUI.
    // Parameters:  none
    // Return:      void
    //***************************************************************************
    public void displayPrompt() {
        output.append("\nRound: " + (rounds+1) + "\n1.Cooperate with your partner and remain silent."
                + "\n2.Betray and testify against your partner.\n"
                + "\nWhat is your decision this round?");
    }
    
    //***************************************************************************
    // Name:        populateStrategyBox
    // Purpose:     Simple helper function. Uses a temporary PDGame object to
    //                 fill the strategies list.
    // Parameters: 
    // Return: 
    //***************************************************************************
    public static void populateStrategyBox(){
        PDGame tGame = new PDGame("strategy.txt");
        for (String s: tGame.getStrategies()) {
            computerStrategyBox.addItem(s);
        }
    }
 
   
private class cooperateEvent implements ActionListener {
    //***************************************************************************
    // Name:         actionPerformed
    // Purpose:      An event handler for the remain silent button.
    // Parameters:   event
    // Return:       void
    //**************************************1*************************************
    @Override
    public void actionPerformed(ActionEvent event)
    {
        writeOutput( "\n\n" + currentgamePD.playRound(1) +  "\n\n" );
        rounds++;   // Increase rounds played
    
        // Cap at 5 rounds
        if ( rounds == 5) { endRound();}
        else displayPrompt();// Display prompt to continue game
    }
}

private class testifyEvent implements ActionListener {
    //***************************************************************************
    // Name:         actionPErformed
    // Purpose:      An event handler for the testify button.
    // Parameters:   Event.
    // Return:       Void.
    //***************************************************************************
    @Override
    public void actionPerformed(ActionEvent event)
    {
        writeOutput( "\n\n" + currentgamePD.playRound(2) +  "\n\n" );
        rounds++;   // Increase rounds played
        
        // Cap at 5 rounds
        if ( rounds == 5)    {
            endRound();
        }
        else
        // Display prompt to continue game
        displayPrompt();
    }
}

// End Round, Display results and load game stat object in
    //***************************************************************************
    // Name:        endRound
    // Purpose:     Finalize a given game
    // Parameters:  none
    // Return:      void
    //***************************************************************************
    public void endRound() {
    
    // Set Buttons Disabled
    silentButton.setEnabled( false );
    testifyButton.setEnabled( false );
    
    // Enable buttons needed for new game
    computerStrategyBox.setEnabled (true);
    startB.setEnabled(true);
        
    // Add game to stats list
    statsMap.put( gameStartTime, currentgamePD.getStats());
    GS = currentgamePD.getStats(); 
    listModel.addElement( gameStartTime );
    writeOutput("\nYour prison sentence is: " + GS.userFinal() 
            + "\nYour partner's prison sentence is: " + GS.compFinal());            

}

private class startGameEvent implements ActionListener {
    //***************************************************************************
    // Name:        actionPErformed
    // Purpose:     An event handler for a new game
    // Parameters:  Event
    // Return:      void
    //***************************************************************************
    @Override
    public void actionPerformed(ActionEvent event){ 
        // Create new game
        currentgamePD = new PDGame("strategy.txt"); 

        computerStrategyBox.setEnabled (false);
        startB.setEnabled(false);
        
        // Get Game Start Time - THE KEY TO HASH MAP
        gameStartTime = (new Date().toString());           
        
        // Initialize Rounds
        rounds = 0;

        // Set Strategy of new game
        currentgamePD.setStrategy(computerStrategyBox.getSelectedIndex());
        
        // Display initial information  
        
        output.setText("");
        writeOutput("\n***Prisoner's Dilema***\n");
        displayPrompt();  
        
        // Set Buttons Enabled
        silentButton.setEnabled( true );
        testifyButton.setEnabled( true );
        
    }
}
ListSelectionListener listSelectionListener = new ListSelectionListener() {
    @Override
    //***************************************************************************
    // Name:        valueChanged
    // Purpose:     An event handler for the games list, on value change update stats.
    // Parameters:  Event
    // Return:      None
    //***************************************************************************
    public void valueChanged(ListSelectionEvent event){
        if (!event.getValueIsAdjusting())
        {
            JList source = (JList)event.getSource();
            String key = source.getSelectedValue().toString();
            GS = statsMap.get(key); //get GameStat object
            playerSentenceTF.setText(Integer.toString(GS.userFinal()) + " years");   //pointer to player sentence in years
            computerSentenceTF.setText(Integer.toString(GS.compFinal()) + " years");
            roundsPlayedTF.setText(Integer.toString(GS.getRounds()));
            computerStrategyTF.setText(GS.getStrategy());
            winnerTF.setText(GS.getWinner());
            
        }
    }

};
    //***************************************************************************
    // Name:        Create and Show GUI
    // Purpose:     New up and create the GUI in code, then display
    // Parameters:  None
    // Return:      None
    //***************************************************************************
    public static void createAndShowGUI() {
        PDGameApp frame1 = new PDGameApp(); //new up  this class, & call constructor, --due to extends, it is a frame
        frame1.setSize(900, 350); //starting size w/h
        frame1.setResizable(false); // Stop window resizing
        frame1.setDefaultCloseOperation(EXIT_ON_CLOSE); //needed to close 
        frame1.setVisible(true); //frames are invisible must set this true\
    }
    
    
}
