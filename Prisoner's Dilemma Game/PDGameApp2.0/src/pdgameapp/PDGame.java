/*
 * Name: Ahmer Waseem & Jade Steffen
 * Assignment: 3
 * Date Due: 3/23/15
 * Program: Prisoner Dilemma's game that allows user to pick computers strategy
 * for each game played, allows user to pick his choice every round, and calculates
 * amount of years each player gets from those choices. Results are displayed when
 * user doesn't want to play a new game.
 */

package pdgameapp;

// Imports
import java.util.Scanner; 
import java.io.File;
import java.util.ArrayList;
import java.io.*;


/* Class: PDGame
 * Purpose: * This PDGame class is newed up each 
 * time a user starts a new game. It
 * contains all the information needed
 * to handle an instance of a single game.
 */

public class PDGame {
    
    private Scanner inputFile;       // Input file for strategy-from-input
//    private Scanner scan;           // Scanner Object
    private ArrayList<String> computerStrategiesAL = new ArrayList<>(); // Strategies
    private ArrayList<Integer> userHistoryAL = new ArrayList<>();       // User choices are stored here
    private GameStat statsGSptr;                                        // Pointer to a stats object, newed up for each PDGame object
    private int computerStrategy;             // Computer Strategy
    
    //***************************************************************************
    // Name: PDGame 
    // Purpose: Constructor for PDGame, adds strategies to list, opens input file
    // Parameters: String inputFile
    // Return: None
    //***************************************************************************
    
    public PDGame ( String inputFile ) {            
            statsGSptr = new GameStat(); // New up a game stat
            computerStrategiesAL.add("Input-File");
            computerStrategiesAL.add("Tit-for-Tat");        // Populate Strategies List
            computerStrategiesAL.add("Tit-for-Two-Tats");
            computerStrategiesAL.add("Tit-for-Tat-With-Forgiveness");
            computerStrategiesAL.add("Random Choice by Computer");
            
            // Open input file!
            try {
                this.inputFile = new Scanner( new File(inputFile));
            } catch (Exception e) {
                System.out.println("Opening input file " + inputFile + " failed: " + e.getMessage());
            }
    }
    
    //***************************************************************************
    // Name: playRound
    // Purpose: Take User and Computer decisions, processes round results
    // and stores them.
    // Parameters: Integer, users move
    // Return: String that holds result of round
    //***************************************************************************
    
    public String playRound(int userDecision){
        int compDecision = computeComputerDecision();   // Calculate computer decision
        userHistoryAL.add(userDecision);                // Add userDecision to history
        // Both users remain silent
        if (userDecision == 1 && compDecision == 1)
        {
            statsGSptr.update(2,2);
            return "You and your partner remain silent.\nYou both receive 2 years in prison.";
        }
            
        // Computer Betrays
        else if ( userDecision == 1 && compDecision == 2)
        {
            statsGSptr.update(5,1);
            return "You remain silent, your partner betrays you. \nYou recieve 5 years in prison, they recieve 1 year.";
        }
        
        // You rat on your partner
        else if ( userDecision == 2 && compDecision == 1)
        {
            statsGSptr.update(1,5);
            return "You testify against your partner and they remain silent. \nYou get 1 year in prison, they recieve 5 year.";
        }
        else 
        {   // You and your partner betray each other
            statsGSptr.update(3,3);
            return "You and your partner betray each other. \nYou both recieve 3 years in prison.";
        }                     
  
    }
    
    //***************************************************************************
    // Name: computeComputerDecision
    // Purpose: Calculates computers move according to strategy chosen
    // Parameters: Integer, users move
    // Return: Integer of computers move
    //***************************************************************************
    
    public int computeComputerDecision(){
        int computerDecision = 0;
        if (computerStrategy == 1)  {
            try {    
                computerDecision = inputFileStrategy();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } else if (computerStrategy == 2)   {
            computerDecision = titForTat();
        } else if (computerStrategy == 3)   {
            computerDecision = titForTwoTats();
        } else if (computerStrategy == 4)   {
            computerDecision = titForTatForgiveness();
        }
        else  
            computerDecision = randomChoice();
        return computerDecision;
    
    }
    
    //***************************************************************************
    // Name: getStrategies
    // Purpose: Returns list of strategies of the computer
    // Parameters: None
    // Return: ArrayList that holds strategies 
    //***************************************************************************

    public ArrayList<String> getStrategies(){ return computerStrategiesAL; }
    
    //***************************************************************************
    // Name: getScores
    // Purpose: Gets user and computer scores and returns it
    // Parameters: None
    // Return: String that tells results of each game
    //***************************************************************************
    
    public String getScores(){        
        return "Your prison sentence is: " + statsGSptr.userFinal() + "\n" +
                "Your partner's prison sentence is: " + statsGSptr.compFinal() + "\n";
    }
    
    //***************************************************************************
    // Name: computeComputerDecision
    // Purpose: Returns statsGSptr object 
    // Parameters: None
    // Return: Returns GameStat object
    //***************************************************************************
    
    public GameStat getStats(){ return statsGSptr; }
    
    //***************************************************************************
    // Name: setStrategy
    // Purpose: Sets computers strategy and Gamestats strategy value
    // Parameters: Integer strategy, that holds user selected comp strategy
    // Return: None
    //***************************************************************************
    
    public void setStrategy( int strategy )
    {
        computerStrategy = strategy;  // Set PDGame Strategy
        statsGSptr.setStrategy(computerStrategiesAL.get(strategy));   // Set GameStat strategy
    }
    
    //***************************************************************************
    // Name: titForTat
    // Purpose: Determines computers decision based on strategy
    // Parameters: None
    // Return: Integer - Computer decision for round
    //***************************************************************************
    public int titForTat()
    {
     if ( statsGSptr.getRounds() == 0 )
         return 1;
     else
         return userHistoryAL.get(userHistoryAL.size()-1);
    }
    
    //***************************************************************************
    // Name: titForTwoTats
    // Purpose: Determines computers decision based on strategy
    // Parameters: None
    // Return: Integer - Computer decision for round
    //***************************************************************************
    public int titForTwoTats()
    {
     if ( statsGSptr.getRounds() < 2 )
         return 1;
     else if ( userHistoryAL.get(userHistoryAL.size()-1 ) == 2)
         return 2;
     else
         return 1;
    }
    
    //***************************************************************************
    // Name: titForTatForgiveness
    // Purpose: Determines computers decision based on strategy
    // Parameters: None
    // Return: Integer - Computer decision for round
    //***************************************************************************
    public int titForTatForgiveness()
    {
     if ( statsGSptr.getRounds() == 0 )
         return 1;
     else if (Math.random() < 0.1) 
         // Small chance of forgiveness ~9%
         return 1;
     else 
         return userHistoryAL.get(userHistoryAL.size()-1);     
    }
    
    //***************************************************************************
    // Name: inputFileStrategy
    // Purpose: Determines computers decision based on strategy
    // Parameters: None
    // Return: Integer - Computer decision for round
    //***************************************************************************
    public int inputFileStrategy() throws Exception
    {
       if( inputFile.hasNextInt() ) {
           return inputFile.nextInt();
       } else {
           throw new Exception("Not enough records in strategy file!");
       }
    }

    //***************************************************************************
    // Name: randomChoice
    // Purpose: Determines computers decision based on strategy
    // Parameters: None
    // Return: Integer - Computer decision for round
    //***************************************************************************
    public int randomChoice()
    {
        return ((int)(Math.random()+1));
    }
}
