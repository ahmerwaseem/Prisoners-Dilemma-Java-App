/**
 * Name: Ahmer Waseem & Jade Steffen
 * Assignment: 2
 * Date Due: 2/16/15
 * Program: Prisoner Dilemma's game that allows user to pick computers strategy
 * for each game played, allows user to pick his choice every round, and calculates
 * amount of years each player gets from those choices. Results are displayed when
 * user doesn't want to play a new game.
 */

package pdgameapp;

import java.util.Scanner; //java libraries that are accessed by the code
import java.util.InputMismatchException;
import java.util.*;
import java.awt.*; 
import javax.swing.*;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;


//import statc;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections; 
//import java.util.Comparator; 
//import java.util.List;

/*****************************************
 * This PDGameApp class is newed up each 
 * time a user starts a new game. It
 * contains all the information needed
 * to handle an instance of a single game.
 ******************************************//**
 *
 * 
 */
public class PDGameApp extends JFrame{   

    private JButton startB = new JButton("Start Button");  //pointer to jbutton
    private JTextField playerSentenceTF=new JTextField(10);   //pointer to player sentence in years
    private JTextField roundsPlayedTF=new JTextField(10);
    private JTextField computerSentenceTF=new JTextField(10);
    private JTextField computerStrategyTF=new JTextField(10);
    private JTextField winnerTF=new JTextField(10);
    private final DefaultListModel<String> listModel = new DefaultListModel<String>();  //list model goes in jlist below
    private JList<String> finishedGamesList;           //when filled in will hold list of completed games in upper left corner
    private static JComboBox  computerStrategyBox = new JComboBox<>();    //when filled in will hold pointer to a box that lists the 5 strategies
    private JTextArea output = new JTextArea(15,35);
    private PDGame currentgamePD =null;                //filled in the constructor and also in start button logic 
    
    public static void main(String[] args) {
        
        createAndShowGUI();
        
        HashMap<String,GameStat> statsMap = new HashMap<>();
        boolean validInput = false, 
                playNewGame = true, 
                validFlag = false;
        PDGame currentGame = null;
        GameStat GS = null;
        Scanner scan = new Scanner(System.in);
        Scanner scan1 = new Scanner(System.in);
        int userInput = 0, count;
        String userInputString;
        String gameStartTime;
        
        while (playNewGame)
        {

            // Create new game here:
            currentGame = new PDGame("strategy.txt");
            count = 0;
            validInput = false;

            System.out.println("***Prisoner's Dilemma***\n");
            System.out.println("Start one game- one game consists of 5 rounds...\n");
            // Get Game Start Time - THE KEY TO HASH MAP
            gameStartTime = (new Date().toString());
        
            System.out.println("Strategies available to choose for the computer:\n");
            for (String s: currentGame.getStrategies())
            {
                setStrategyBox(s);
                count++;
                System.out.println( count + ". " + s);
            }
            
            // Strategy Selection
            System.out.println("Select strategy from above for the computer\n"
                        + "to use in the 5 rounds (1-5):");
            while(!validInput){
            try {
                    userInput = scan.nextInt();
                    if( userInput >= 1 && userInput <= 5 )
                    {
                        currentGame.setStrategy(userInput);
                        validInput = true;
                    }
                    else 
                        throw new Exception("Invalid Integer");
                }
            catch (InputMismatchException e)
                {
                    System.out.println("Please enter a valid integer (1-5):");
                    scan.next();
                }
            catch (Exception e)
                {
                    System.out.println("Please enter a valid integer (1-5):");
                //    scan.next();
                }
            }             
            
            // Play Five Rounds
            for(int rounds = 0; rounds < 5; rounds++)
            {
                System.out.println("\n1.Cooperate\n2.Betray and testify against.\n"
                        + "What is your decision this round?");
                // Ensure good input for user move
                validInput = false;
                while(!validInput){
                try {
                    userInput = scan.nextInt();
                    if( userInput == 1 || userInput == 2 )
                    {
                        validInput = true;
                        System.out.println(currentGame.playRound(userInput));
                    }
                    else 
                        throw new Exception("Invalid Integer");
                }
                catch (InputMismatchException e)
                {
                    System.out.println("Please enter 1 to cooperate or 2 to betray:");
                    scan.next();
                }
                catch (Exception e)
                {
                    System.out.println("Please enter 1 to cooperate or 2 to betray:");
                //    scan.next();
                }
            }

            }
            
            // To load a gameStats object in
            statsMap.put( gameStartTime, currentGame.getStats());
            
            GS = currentGame.getStats();
            System.out.println("\nYour prison sentence is: " + GS.userFinal());
            System.out.println("Your partner's prison sentence is: " + GS.compFinal());
            
            validFlag = false;
            while(validFlag == false) {
                System.out.println("Would you like to play another game? (y/n)");

                userInputString = scan1.next().toLowerCase();
                if (!userInputString.equals("y") && !userInputString.equals("n") )
                    System.out.println("Invalid input, please input y or n");
                else if( userInputString.equals("n")) 
                {
                    validFlag = true;
                    playNewGame = false;
                }
                    else
                        validFlag = true;
            
            }
         
        }
                    
     //  Print out each game details
        for (String key : statsMap.keySet())
        {
            GS = statsMap.get(key);
           // finishedGamesList.addElement(key);
            System.out.println(key + ": Winner: " + GS.getWinner() + ". Strategy Used By Computer: " + 
                                GS.getStrategy() + ".");
        }
    }
    
    PDGameApp() {                                         //constructor where the GUI is built and added to frame, lots of code here
    super("Prisoner's  Dillemma");                   //call frame class constructor,recommended by java
    currentgamePD = new PDGame("strategy.txt");           //fills in pointer up top
    setLayout(new BorderLayout());               //set layout for frame, no reference is needed here since we are in a non-static method 
    Container contentPanel = getContentPane();
    contentPanel.setBackground(Color.black);
    
//create and add west panel
    JPanel panelLeft = new JPanel(new BorderLayout()); //then set color, borders on panel1
    TitledBorder titled = new TitledBorder("List of Games");
    panelLeft.setBorder(titled);
    panelLeft.setBackground(new Color(145,200,144));
    contentPanel.add(panelLeft,BorderLayout.WEST);
    
    finishedGamesList = new JList<String>();
   // panelLeft.add(new JScrollPane(finishedGamesList), BorderLayout.NORTH);
    JPanel panelRight = new JPanel(new BorderLayout()); //then set color, borders on panel1
    panelRight.setBackground(new Color(145,200,144));
    
    contentPanel.add(panelRight, BorderLayout.EAST);
    
    finishedGamesList.setVisibleRowCount(10);
    finishedGamesList.setFixedCellWidth(350);

    JScrollPane gamesList = new JScrollPane(finishedGamesList);
    JScrollPane outputPane = new JScrollPane(output);
    panelRight.add(outputPane, BorderLayout.SOUTH); 
    
    panelLeft.add(gamesList, BorderLayout.NORTH);
    panelLeft.setPreferredSize(new Dimension(400, 375));
    panelRight.setPreferredSize(new Dimension(500, 375));
    
    JPanel panelLeftBottom = new JPanel(new GridLayout(5,2));
    panelLeftBottom.setBackground(new Color(145,200,144));
    panelLeftBottom.setPreferredSize(new Dimension(225, 125));
    
    panelLeftBottom.add(new JLabel("Rounds Played"));
    roundsPlayedTF.setEditable(false);
    panelLeftBottom.add(roundsPlayedTF);
    
    panelLeftBottom.add(new JLabel("Computer Strategy"));
    computerStrategyTF.setEditable(false);
    panelLeftBottom.add(computerStrategyTF);
    
    panelLeftBottom.add(new JLabel("Player Sentence"));
    playerSentenceTF.setEditable(false);
    panelLeftBottom.add(playerSentenceTF);
    
    panelLeftBottom.add(new JLabel("Computer Sentence"));
    computerSentenceTF.setEditable(false);
    panelLeftBottom.add(computerSentenceTF);
    
    panelLeftBottom.add(new JLabel("Winner"));
    winnerTF.setEditable(false);
    panelLeftBottom.add(winnerTF);
    
    
    
    
    
    panelLeft.add(panelLeftBottom, BorderLayout.SOUTH);
    JPanel panelTopRight = new JPanel(new GridLayout(2,1));
    panelTopRight.setPreferredSize(new Dimension(400, 75));
    JPanel panelStrategy = new JPanel();
    panelStrategy.setBackground(new Color(200,145,144));
    JPanel panelDecision = new JPanel();
    panelDecision.setBackground(new Color(200,145,144));
    panelStrategy.add(new JLabel("Computer Strategy"));
    panelStrategy.add(computerStrategyBox);
    panelStrategy.add(startB);
    
    panelDecision.add(new JLabel("Your decision this round?"));
    JButton silentButton = new JButton("Remain Silent");
    panelDecision.add(silentButton);
    JButton testifyButton = new JButton("Testify");
    panelDecision.add(testifyButton);
    panelDecision.add(new JButton("Your decision this round?"));
    panelTopRight.add(panelStrategy);
    panelTopRight.add(panelDecision);
    panelRight.add(panelTopRight, BorderLayout.NORTH);
    pack();
    }

    public static void createAndShowGUI() {
    PDGameApp frame1 = new PDGameApp(); //new up  this class, & call constructor, --due to extends, it is a frame
    frame1.setSize(900, 350); //starting size w/h
    frame1.setResizable(false);
    frame1.setDefaultCloseOperation(EXIT_ON_CLOSE); //needed to close 
    frame1.setVisible(true); //frames are invisible must set this true

    }
    
    public static void setStrategyBox(String s){
        
        computerStrategyBox.addItem(s);
    }
}
