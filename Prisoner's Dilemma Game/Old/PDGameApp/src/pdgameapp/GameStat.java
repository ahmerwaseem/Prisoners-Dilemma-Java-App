/*
 * Name: Ahmer Waseem & Jade Steffen
 * Assignment: 2
 * Date Due: 2/16/15
 * Program: Prisoner Dilemma's game that allows user to pick computers strategy
 * for each game played, allows user to pick his choice every round, and calculates
 * amount of years each player gets from those choices. Results are displayed when
 * user doesn't want to play a new game.
 */

package pdgameapp;

/* Class: GameStat
 * Purpose: To keep track of statistics of Prisoners Dilemma game including
 * number of years in prison for user and computer, rounds played, and strategy
 * used by computer.
 */

public class GameStat {
    
    private int userSentenceYrs = 0,    // Years user will spend in jail
               compSentenceYrs = 0,     // Years computer will spend in jail
               roundsPlayed = 0;        // Total number of rounds played
    
    private String computerStrategy;    
    
    //***************************************************************************
    // Name: update
    // Purpose: Updates each players years sentenced and tracks rounds played
    // Parameters: Integers userSentence, compSentence
    // Return: None
    //***************************************************************************
    public void update(int userSentence, int compSentence)
    {
        roundsPlayed++;                         // Inc Round Count
        this.userSentenceYrs += userSentence;   // Add to player sentence
        this.compSentenceYrs += compSentence;   // Add to PC sentence
    }
    
    //***************************************************************************
    // Name: getWinner
    // Purpose: Determines computers decision based on strategy
    // Parameters: None
    // Return: String - Winner of game
    //***************************************************************************
    public String getWinner()
    {
        if( userSentenceYrs > compSentenceYrs)
            return "Computer";
        else if ( userSentenceYrs < compSentenceYrs)
            return "User";
        else return "Tie game";         // If sentences match, tie
    }
    
    public void setStrategy( String strategy ) { computerStrategy = strategy; }

    //***************************************************************************
    // Name: getStrategy
    // Purpose: Getter for computer computerStrategy
    // Parameters: None
    // Return: String - Computer strategy chosen
    //***************************************************************************
    public String getStrategy(){ return computerStrategy; }
 
    //***************************************************************************
    // Name: userFinal
    // Purpose: Getter for users sentenced years
    // Parameters: None
    // Return: Integer - Users sentenced years
    //***************************************************************************
    public int userFinal() { return userSentenceYrs; }

    //***************************************************************************
    // Name: compFinal
    // Purpose: Getter for computers sentenced years
    // Parameters: None
    // Return: Integer - Computers sentenced years
    //***************************************************************************
    public int compFinal() { return compSentenceYrs; }
    
    //***************************************************************************
    // Name: getRounds
    // Purpose: Getter for rounds played
    // Parameters: None
    // Return: Integer - Number of rounds played
    //***************************************************************************
    public int getRounds() { return roundsPlayed; }
}
