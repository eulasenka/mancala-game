/**
 * The Game - Mancala
 * Lecturer - Piotr Witonski
 * @author Eduard Ulasenka, 273173
 */

import java.awt.event.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.SwingWorker;

import java.net.*;

/** Provides interaction between View and Model classes 
 * @author Eduard Ulasenka, 273173
*/
public class Controller {
	private View theView;
	private Model theModel;
	
	//Multiplayer's variables
	private boolean multiplayerMode;
	private boolean serverMode;		
	private boolean anotherPlayerConnected;
	
	/** Music flag */
	private boolean musicOn;
	
	private Socket connection;
	private DataOutputStream outP;
	private DataInputStream inP;
	
	/**
	 * Takes two arguments, which are View and Model classes
	 * @param theView
	 * @param theModel
	 */
	public Controller(View theView, Model theModel)
	{
		this.theView = theView;
		this.theModel = theModel;
		
		musicOn = true;
		
		multiplayerMode = false;
		anotherPlayerConnected = false;
		
		this.theView.addMouseListener(new MouseListener());
	}
	
	/** MouseListener class 
	 * @author Eduard Ulasenka, 273173
	*/
	private class MouseListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			
			//Check which button was pressed
			if (e.getSource() == theView.getButtonNewGame())
			{
				//if previous game was online game
				multiplayerMode = false;
				anotherPlayerConnected = false;
				
				theView.setGameStartedFlag(theModel.newGame());
				theView.setScore(theModel.getScore(), theModel.getFirstPlayerTurnFlag(),
									theModel.getGameStartedFlag(),
									theModel.getGameOverFirstPlWonFlag(),
									theModel.getGameOverSecondPlWonFlag());
				theView.repaint();
			}
			
			if (e.getSource() == theView.getButtonFAQ())
			{
				theView.setPanel(theView.getFAQView());
			}
			
			if (e.getSource() == theView.getButtonStopMusic())
			{
				if(musicOn)
				{
					theModel.musicStop();
					musicOn = false;
				}
				else
				{
					theModel.musicStart();
					musicOn = true;
				}
			}
			
			if (e.getSource() == theView.getButtonMultiplayer())
			{
				theView.setPanel(theView.getMultiplayerView());
			}
			
			if (e.getSource() == theView.getButtonBackToMenu()
					|| e.getSource() == theView.getButtonBackToMenuAn())
			{
				theView.setPanel(theView.getMenuView());
			}
			
			if (e.getSource() == theView.getButtonClient())
			{
				multiplayerMode = true;
				serverMode = false;

				theView.setPanel(theView.getMenuView());
				
				new workerForJoin().execute();
			}
			
			if (e.getSource() == theView.getButtonServer())
			{
				multiplayerMode = true;
				serverMode = true;

				theView.setPanel(theView.getMenuView());
				
				new workerForCreate().execute();
			}
			
			//If now first player goes
			if(theModel.getFirstPlayerTurnFlag() && !theModel.getGameOverFirstPlWonFlag() 
					&& !theModel.getGameOverSecondPlWonFlag())
			{
				//If it's online game, but not my turn
				if (multiplayerMode == true && !theModel.ifItIsMyTurn(serverMode) == true)
					return;
				//Which cell was chosen
				if (e.getSource() == theView.getButtonFirstCellfirstPlayer())
					gameButtonPressed(0);
				if (e.getSource() == theView.getButtonSecondCellfirstPlayer())
					gameButtonPressed(1);
				if (e.getSource() == theView.getButtonThirdCellfirstPlayer())
					gameButtonPressed(2);
				if (e.getSource() == theView.getButtonFourthCellfirstPlayer())
					gameButtonPressed(3);
				if (e.getSource() == theView.getButtonFifthCellfirstPlayer())
					gameButtonPressed(4);
				if (e.getSource() == theView.getButtonSixthCellfirstPlayer())
					gameButtonPressed(5);
			}
			//If it's second player's turn
			else if(!theModel.getFirstPlayerTurnFlag() && !theModel.getGameOverFirstPlWonFlag() 
					&& !theModel.getGameOverSecondPlWonFlag())
			{
				//If it's online game, but not my turn
				if (multiplayerMode == true && !theModel.ifItIsMyTurn(serverMode) == true)
					return;
				
				if (e.getSource() == theView.getButtonFirstCellsecondPlayer())
					gameButtonPressed(12);
				if (e.getSource() == theView.getButtonSecondCellsecondPlayer())
					gameButtonPressed(11);
				if (e.getSource() == theView.getButtonThirdCellsecondPlayer())
					gameButtonPressed(10);
				if (e.getSource() == theView.getButtonFourthCellsecondPlayer())
					gameButtonPressed(9);
				if (e.getSource() == theView.getButtonFifthCellsecondPlayer())
					gameButtonPressed(8);
				if (e.getSource() == theView.getButtonSixthCellsecondPlayer())
					gameButtonPressed(7);
			} //end of else
			
		} //end of actionPerformed(ActionEvent e)
		
		/** Takes number of button which was press
		 * @param buttonNumber
		 */
		private void gameButtonPressed(int buttonNumber)
		{
			theModel.playGame(buttonNumber);
			
			if (multiplayerMode == true)
				multiplayerPart(buttonNumber);
				
			theView.setScore(theModel.getScore(), theModel.getFirstPlayerTurnFlag(),
					theModel.getGameStartedFlag(),
					theModel.getGameOverFirstPlWonFlag(),
					theModel.getGameOverSecondPlWonFlag());
			theView.repaint();
		}
		
		/** Swing worker fot server 
		 * @author Eduard Ulasenka, 273173
		*/
		private class workerForCreate extends SwingWorker<Void, Void>
		{
			protected Void doInBackground() throws Exception {
				
				try {
					
					ServerSocket server = new ServerSocket(3456);
					connection = server.accept();
					InputStream sin = connection.getInputStream();
					OutputStream sout = connection.getOutputStream();
					
					DataInputStream input = new DataInputStream(sin);
					DataOutputStream output = new DataOutputStream(sout);
					
					inP = input;
					outP = output;
					anotherPlayerConnected = true;

					//Create New Game
					theView.setGameStartedFlag(theModel.newGame());
					theView.setScore(theModel.getScore(), theModel.getFirstPlayerTurnFlag(),
										theModel.getGameStartedFlag(),
										theModel.getGameOverFirstPlWonFlag(),
										theModel.getGameOverSecondPlWonFlag());
					theView.repaint();
					
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	
				return null;
			}
		}
		
		/** Swing worker for client 
		 * @author Eduard Ulasenka, 273173
		*/
		private class workerForJoin extends SwingWorker<Void, Void>
		{
			protected Void doInBackground() throws Exception {
				
				try {
					//127.0.0.1 as default value
					String ipValue = "127.0.0.1";
					
					String forIp = theView.returnServerIp();
					if (!(forIp.equals("")))
					{
						ipValue = forIp;
					}
					
					InetAddress ip = InetAddress.getByName(ipValue);
					connection = new Socket(ip ,3456 );
					
					InputStream sin = connection.getInputStream();
					OutputStream sout = connection.getOutputStream();
					
					DataInputStream input = new DataInputStream(sin);
					DataOutputStream output = new DataOutputStream(sout);
					
					inP = input;
					outP = output;
					
					anotherPlayerConnected = true;
					
					//Create new game
					theView.setGameStartedFlag(theModel.newGame());
					theView.setScore(theModel.getScore(), theModel.getFirstPlayerTurnFlag(),
										theModel.getGameStartedFlag(),
										theModel.getGameOverFirstPlWonFlag(),
										theModel.getGameOverSecondPlWonFlag());
					theView.repaint();
					
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
				waitForData();
				
				return null;
			}//end of doInBackground	
		}//end of workerForJoin
	} //end of ActionListener
	
	/** extends SwingWorker
	 * wait for data from other player 
	 * @author Eduard Ulasenka, 273173
	*/
	private class waitDataWorker extends SwingWorker<Void, Void>
	{                                   
		protected Void doInBackground() throws Exception {
		
			while (theModel.ifItIsMyTurn(serverMode) == false)
			{
				String msg = null;
			
				try {
					msg = inP.readUTF();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				//We need to update our data
				theModel.playGame(Integer.parseInt(msg));
				theView.setScore(theModel.getScore(), theModel.getFirstPlayerTurnFlag(),
						theModel.getGameStartedFlag(),
						theModel.getGameOverFirstPlWonFlag(),
						theModel.getGameOverSecondPlWonFlag());
				theView.repaint();
				
				//If the game was over
				if (theModel.getGameOverFirstPlWonFlag() == true || theModel.getGameOverSecondPlWonFlag() == true)
					connection.close();
			}
			
			return null;
		}//end of doBackground
	}// end of swingWorker
	
	/** wait for data from other player */
	private void waitForData()
	{	
		new waitDataWorker().execute();
	}
	
	/**
	 * Takes the number of button which was pressed to send it to other player
	 * @param buttonNumber
	 */
	private void multiplayerPart(int buttonNumber) 
	{
		if (multiplayerMode == true && anotherPlayerConnected == false)
			return;
		
		//We have to send a button's number, which was pressed
		String msg = "" + buttonNumber;
			
		try {
			outP.writeUTF(msg);
			outP.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//If the game was over
		if (theModel.getGameOverFirstPlWonFlag() == true || theModel.getGameOverSecondPlWonFlag() == true)
		{
			try {
				connection.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			return;
		}
		
		//If we ended our turn and now need the opponent to move
		if (theModel.ifItIsMyTurn(serverMode) == false)
				waitForData();
	}
}
