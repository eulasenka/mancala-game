/**
 * The Game - Mancala
 * Lecturer - Piotr Witonski
 * @author Eduard Ulasenka, 273173
 */

import java.io.*;

import sun.audio.*;

/** Makes all calculations 
 * @author Eduard Ulasenka, 273173
*/
public class Model {
	private int cellNo = 12;
	private int mancalNo = 2;
	private int stonesPerCell = 4;
	private int[] cellsAndMancals;
	/** The variable contains data about cells that are on the opposite side */
	private int[] frontedCells;
	
	//Flags
	private boolean gameStarted = false;
	private boolean firstPlayerTurn = false;
	private boolean gameOverFirstPlWon = false;
	private boolean gameOverSecondPlWon = false;
	
	//Music
	private InputStream in;
	private AudioStream as;
	
	/**
	 * creates and initializes all cells, fronted cells, mancals and game flags.
	 * Creates audio stream
	 */
	public Model(){
		cellsAndMancals = new int[cellNo+mancalNo];
		frontedCells = new int[cellNo+1];
		
		//Fill cells with the value "stonesPerCell", don't fill mancals
		for(int i = 0; i < cellNo+mancalNo; i++)
		{
			if (i != 6 && i != 13)
			{
				cellsAndMancals[i] = stonesPerCell;
			}
			else
			{
				cellsAndMancals[i] = 0;
			}
		}
		
		//For the example: sixth cell's opposite is seventh, and seventh cell's oposite is sixth
		for(int i=0; i<13; i++)
		{
			if(i==6) 
				continue;
			
			frontedCells[i] = 12-i;
		}
		
		try{
			in = new FileInputStream("music.wav");
			// Create an AudioStream object from the input stream.
			as = new AudioStream(in);         
			// Use the static class member "player" from class AudioPlayer to play clip.
			AudioPlayer.player.start(as);
		}catch(IOException error) {}; 
	}
	
	/** Stop music */
	public void musicStop()
	{
		AudioPlayer.player.stop(as);
	}
	
	/** Start music */
	public void musicStart()
	{
		AudioPlayer.player.start(as);
	}
	
	/** Set Up all values to start new game 
	 * @return gameStarted flag*/
	public boolean newGame()
	{
		//Set Up flags
		firstPlayerTurn = true;
		gameStarted = true;
		gameOverFirstPlWon = false;
		gameOverSecondPlWon = false;
		
		//Set Up Cells
		for(int i = 0; i < cellNo+mancalNo; i++)
		{
			if (i != 6 && i != 13)
			{
				cellsAndMancals[i] = stonesPerCell;
			}
			else
			{
				cellsAndMancals[i] = 0;
			}
		}
		
		return gameStarted;
	}
	
	/** 
	 * Here is all the game's logic
	 * @param button - the button was press
	 */
	public void playGame(int button)
	{
		//It is the number of cell that is next after cell in which the last stone will be dropped
		//For the example the last stone will be dropped in 7th, so the count value is 8
		int count = button+cellsAndMancals[button]+1;
		
		//As first we have to check, if chosen cell is empty
		//If not then nothing to be done, and player has to choose another one
		if (cellsAndMancals[button] != 0)
		{
			//Now we get out all stones from chosen cell 
			cellsAndMancals[button] = 0;
			
			//Here we dropping our stones in following cells (also in our mancal)
			for(int i = button+1; i < count; i++)
			{
				//Omitting other's player mancal
				if(firstPlayerTurn && i == 13) 
				{
					i=0;
					count = count - 13;
					cellsAndMancals[i]++;
					continue;
				}
				else if(!firstPlayerTurn && i==6)  
				{
					i++;
					cellsAndMancals[i]++;
					count++;
					continue;
				}
				
				cellsAndMancals[i]++;
				
				//After the last cell (second's player mancal) we have to back to the first cell
				if(i == 13)
				{
					i=-1;
					count = count - 13 - 1;
				}
			}
			
			//Check if the last was dropped in empty cell
			// && in the cell of player which now goes
			// && on the opposite side there are stones in cell
			if (firstPlayerTurn && count < 7 && count != 0 && cellsAndMancals[count-1] == 1 
					&& cellsAndMancals[frontedCells[count-1]] != 0)
			{
				cellsAndMancals[count-1] = 0;
				cellsAndMancals[6]++;
				cellsAndMancals[6]+=cellsAndMancals[frontedCells[count-1]];
				cellsAndMancals[frontedCells[count-1]]=0;
				firstPlayerTurn = false;
			}
			else if (!firstPlayerTurn && count > 7 && cellsAndMancals[count-1] == 1 
					&& cellsAndMancals[frontedCells[count-1]] != 0)
			{
				cellsAndMancals[count-1] = 0;
				cellsAndMancals[13]++;
				cellsAndMancals[13]+=cellsAndMancals[frontedCells[count-1]];
				cellsAndMancals[frontedCells[count-1]]=0;
				firstPlayerTurn = true;
			}
			//Check if the last was dropped in the mancal
			//If it's positive then don't turn the turn
			else if (firstPlayerTurn && count != 7)
			{
				firstPlayerTurn = false;
			}
			else if (!firstPlayerTurn && count != 0)
			{
				firstPlayerTurn = true;
			}
			
			//Check if the game ended
			//As first we check if one of players has more than 50% jf stones in his mancal
			if (cellsAndMancals[6] >= 25)
			{
				gameOverFirstPlWon = true;
				gameStarted = false;
			}
			else if (cellsAndMancals[13] >= 25)
			{
				gameOverSecondPlWon = true;
				gameStarted = false;
			}
			
			//Also we have to check if one of players has all his cells empty
			if (!gameOverFirstPlWon && !gameOverSecondPlWon)
			{
				for(int i = 0; i < 6; i++)
				{
					if (cellsAndMancals[i] == 0 && i!=5)
						continue;
					//If all first player's cells are empty
					else if (cellsAndMancals[i] == 0 && i==5)
					{
						//Drop all stones from cells to the second's player mancal
						for(int j = 7; j< 13; j++)
						{
							cellsAndMancals[13] += cellsAndMancals[j];
							cellsAndMancals[j] = 0;
						}
						//Count stones and choose a winner
						if(cellsAndMancals[13] > cellsAndMancals[6])
						{
							gameOverSecondPlWon = true;
						}
						else if (cellsAndMancals[13] == cellsAndMancals[6])
						{
							gameOverFirstPlWon = true;
							gameOverSecondPlWon = true;
						}
						else
						{
							gameOverFirstPlWon = true;
						}
					
						gameStarted = false;
					}
					else
						break;
				}
			}
			//The same for another player
			if (!gameOverFirstPlWon && !gameOverSecondPlWon)
			{
				for(int i = 7; i < 13; i++)
				{
					if (cellsAndMancals[i] == 0 && i!=12)
						continue;
					else if (cellsAndMancals[i] == 0 && i==12)
					{

						for(int j = 0; j< 6; j++)
						{
							cellsAndMancals[6] += cellsAndMancals[j];
							cellsAndMancals[j] = 0;
						}
						if(cellsAndMancals[13] > cellsAndMancals[6])
						{
							gameOverSecondPlWon = true;
						}
						else if (cellsAndMancals[13] == cellsAndMancals[6])
						{
							gameOverFirstPlWon = true;
							gameOverSecondPlWon = true;
						}
						else
						{
							gameOverFirstPlWon = true;
						}
					
						gameStarted = false;
					}
					else
						break;
				}
			}//end of if (!gameOverFirstPlWon && !gameOverSecondPlWon)
		}//end of if (cellsAndMancals[button] != 0)
	}//end of playGame()

	/** @return gameStarted flag value in Model*/
	public boolean getGameStartedFlag()
	{
		return gameStarted;
	}
	
	/** @return Score array in Model*/
	public int[] getScore()
	{
		return cellsAndMancals;
	}
	
	/** @return FirstPlayerTurn flag value in Model*/
	public boolean getFirstPlayerTurnFlag()
	{
		return firstPlayerTurn;
	}
	
	/**
	 * @param turn - server(first player) or not server (second player) 
	 * @return 'is it my turn?' flag value in Model*/
	public boolean ifItIsMyTurn(boolean turn)
	{
		if (turn == firstPlayerTurn)
			return true;
		else
			return false;
	}

	/** @return getGameOverFirstPlWonFlag flag value in Model*/
	public boolean getGameOverFirstPlWonFlag()
	{
		return gameOverFirstPlWon;
	}
	
	/** @return gameOverSecondPlWon flag value in Model*/
	public boolean getGameOverSecondPlWonFlag()
	{
		return gameOverSecondPlWon;
	}
}
