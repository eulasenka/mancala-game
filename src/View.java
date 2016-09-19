/**
 * The Game - Mancala
 * Lecturer - Piotr Witonski
 * @author Eduard Ulasenka, 273173
 */

import java.awt.*;
import java.io.*;
import java.awt.event.*;

import javax.imageio.ImageIO;
import javax.swing.*;

/** Paints and shows all graphic objects in the game 
 * @author Eduard Ulasenka, 273173
 */
public class View extends JFrame{
	
	// Flags
	private boolean gameStarted = false;
	private boolean firstPlayerTurn = false;
	private boolean gameOverFirstPlWon = false;
	private boolean gameOverSecondPlWon = false;
	
	private int[] cellsAndMancals;
	
	// declare MenuButtons
	private JButton newGame = new JButton("New Game");
	private JButton fAQ = new JButton("FAQ");
	private JButton stopMusic = new JButton("stopMusic");
	
	private JButton multiplayer = new JButton("Multiplayer");
	private JButton client = new JButton("Client");
	private JButton server = new JButton("Server");
	
	private JButton backToMenu = new JButton("Back");
	private JButton backToMenuAn = new JButton("Back");
	
	/* Declare playing buttons
	*     1_2  2_2 .... 6_2      
	* |_]                   |_|  
	*      1    2  ....  6       
	*/
	private JButton firstCellfirstPlayer = new JButton("1");
	private JButton secondCellfirstPlayer = new JButton("2");
	private JButton thirdCellfirstPlayer = new JButton("3");
	private JButton fourthCellfirstPlayer = new JButton("4");
	private JButton fifthCellfirstPlayer = new JButton("5");
	private JButton sixthCellfirstPlayer = new JButton("6");
	
	private JButton firstCellsecondPlayer = new JButton("1_2");
	private JButton secondCellsecondPlayer = new JButton("2_2");
	private JButton thirdCellsecondPlayer = new JButton("3_2");
	private JButton fourthCellsecondPlayer = new JButton("4_2");
	private JButton fifthCellsecondPlayer = new JButton("5_2");
	private JButton sixthCellsecondPlayer = new JButton("6_2");
	
	private JTextField serverIp = new JTextField();

	/// Create background
	private Image image = null;
	private Image imageFAQ = null;
	private Image imageMultiplayer = null;
	// Create App's icon
	private Image icon = null;
	// Create panel
	private JPanel currPanel;
	
	// Set up font
	private Font scoreFont = new Font("Courier", Font.ITALIC, 50);
	
	// Our panels
	private MenuView menuPanel = new MenuView();
	private FAQView fAQPanel = new FAQView();
	private MultiplayerView multiplayerPanel = new MultiplayerView();
	
	/** SetUp all buttons, panels.
	 * Load images and game icon
	 */
	public View()
	{
		cellsAndMancals = new int[14];
		
		menuPanel.setLayout(null);
		fAQPanel.setLayout(null);
		multiplayerPanel.setLayout(null);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(1005,604);
		// Set the window in center
		this.setLocationRelativeTo(null);
		this.setTitle("Mancala");
		this.setResizable(false);
		
		// Set Up and place all buttons
		setUpButton(menuPanel, newGame, 314, 537, 111, 25);
		setUpButton(menuPanel, multiplayer, 439, 537, 117, 25);
		setUpButton(menuPanel, fAQ, 570, 537, 120, 25);
		setUpButton(menuPanel, stopMusic, 10, 503, 75, 65);
		
		setUpButton(multiplayerPanel, server, 419, 209, 160, 45);
		setUpButton(multiplayerPanel, client, 424, 337, 147, 45);
		
		
		setUpButton(multiplayerPanel, backToMenu, 432, 537, 135, 25);
		setUpButton(fAQPanel, backToMenuAn, 432, 537, 135, 25);
		
		setUpButton(menuPanel, firstCellfirstPlayer, 128, 336, 92, 92);
		setUpButton(menuPanel, secondCellfirstPlayer, 259, 336, 92, 92);
		setUpButton(menuPanel, thirdCellfirstPlayer, 390, 336, 92, 92);
		setUpButton(menuPanel, fourthCellfirstPlayer, 521, 336, 92, 92);
		setUpButton(menuPanel, fifthCellfirstPlayer, 651, 336, 92, 92);
		setUpButton(menuPanel, sixthCellfirstPlayer, 782, 336, 92, 92);
		
		setUpButton(menuPanel, firstCellsecondPlayer, 128, 68, 92, 92);
		setUpButton(menuPanel, secondCellsecondPlayer, 259, 68, 92, 92);
		setUpButton(menuPanel, thirdCellsecondPlayer, 390, 68, 92, 92);
		setUpButton(menuPanel, fourthCellsecondPlayer, 521, 68, 92, 92);
		setUpButton(menuPanel, fifthCellsecondPlayer, 651, 68, 92, 92);
		setUpButton(menuPanel, sixthCellsecondPlayer, 782, 68, 92, 92);
		
		// Set Up Text Field
		serverIp.setBounds(595, 410, 100, 20);
		multiplayerPanel.add(serverIp);
		
		// Choose current panel
		currPanel = menuPanel;
		this.add(menuPanel);
        
		// Load images
        try {
			icon = ImageIO.read(new File("icon.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        this.setIconImage(icon);
        
		try {
			image = ImageIO.read(new File("background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			imageFAQ = ImageIO.read(new File("faq.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			imageMultiplayer = ImageIO.read(new File("multiplayer.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** Changes current panel 
	 * @param panel - panel we want to see*/
	public void setPanel(JPanel panel) {
		this.remove(currPanel);
        currPanel = panel;
        this.add(currPanel);
        this.revalidate();
        this.repaint();
        this.getContentPane().repaint();
    }
	
	/** Set Up the View of FAQPanel 
	 * @author Eduard Ulasenka, 273173
	*/
	private class FAQView extends JPanel{
		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawImage(imageFAQ, 0, 0, null); 
			this.requestFocusInWindow();
		}
	}
	
	/** Set Up the View of multiplayerPanel 
	 * @author Eduard Ulasenka, 273173
	*/
	private class MultiplayerView extends JPanel{
		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawImage(imageMultiplayer, 0, 0, null);
			this.requestFocusInWindow();
		}
	}
	
	/** @return menuPanel */
	public MenuView getMenuView()
	{
		return menuPanel;
	}
	
	/** @return fAQPanel */
	public FAQView getFAQView()
	{
		return fAQPanel;
	}
	
	/** @return multiplayerPanel */
	public MultiplayerView getMultiplayerView()
	{
		return multiplayerPanel;
	}
	
	/** Set Up the View of menuPanel, which is the main game's panel 
	 * @author Eduard Ulasenka, 273173
	*/
	private class MenuView extends JPanel{
		public void paint(Graphics g)
		{
			super.paint(g);
			g.drawImage(image, 0, 0, null); 
			this.requestFocusInWindow();
			
			// If the game started of were end show score and all messages
			if(gameStarted || gameOverFirstPlWon || gameOverSecondPlWon)
			{
				g.setColor(Color.white);
				g.setFont(scoreFont);
				
				for(int i=0, x=153, y=402; i<14; i++)
				{
					if(i<6)
					{
						g.drawString("" + cellsAndMancals[i], x, y);
						x+=131;
					}
					else if(i==6)
					{
						g.drawString("" + cellsAndMancals[6], 885, 265);
						x=807;
						y=134;
					}
					else if(i<13)
					{
						g.drawString("" + cellsAndMancals[i], x, y);
						x-=131;
					}
					else
						g.drawString("" + cellsAndMancals[13], 55, 265);
					
				}
			}
			
			g.setColor(Color.green);
			g.setFont(scoreFont);
			if(gameStarted && firstPlayerTurn)
			{
				g.drawString("First Player Goes Now", 245, 500);
			}
			else
			{
				if(gameStarted)
					g.drawString("Second Player Goes Now", 215, 500);
			}
			
			g.setColor(Color.red);
			g.setFont(scoreFont);
			if(!gameStarted && gameOverFirstPlWon && gameOverSecondPlWon)
			{
				g.drawString("It is a draw!", 370, 500);
			}
			else if(!gameStarted && gameOverFirstPlWon)
			{
				g.drawString("First Player Won!", 300, 500);
			}
			else if(!gameStarted && gameOverSecondPlWon)
			{
				g.drawString("Second Player Won!", 265, 500);
			}
			
		} //end of paint(Graphics g)
	} //end of class MenuView
	
	/** Set Up flag 
	 * @param flag gameStarted flag */
	public void setGameStartedFlag(boolean flag)
	{
		gameStarted = flag;
	}
	
	/** Set Up score and flags values 
	 * @param score number of stones in each cell and mancal
	 * @param turn whose turn is now
	 * @param started was the game start
	 * @param overF was the game over and 1st player won
	 * @param overS was the game over and 2nd player won */
	public void setScore(int[] score, boolean turn, boolean started, boolean overF, boolean overS)
	{
		for(int i=0; i<14; i++)
		{
			cellsAndMancals[i] = score[i];
		}
		
		firstPlayerTurn = turn;
		gameStarted = started;
		gameOverFirstPlWon = overF;
		gameOverSecondPlWon = overS;
	}
	
	/**
	 * SetUp button's visible, size and panel
	 * @param panel on which panel button will be placed
	 * @param button button we settingUp
	 * @param x
	 * @param y
	 * @param width of the button
	 * @param height of the button
	 */
	public void setUpButton(JPanel panel, JButton button, int x, int y, int width, int height)
	{
		// Place it
		button.setBounds(x, y, width, height);
		// Make it invisible
		button.setFocusPainted(false);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setOpaque(false);
		// Add on panel
		panel.add(button);
	}
	
	/** @param e MouseListener */
	public void addMouseListener(ActionListener e){
		newGame.addActionListener(e);
		fAQ.addActionListener(e);
		stopMusic.addActionListener(e);
		multiplayer.addActionListener(e);
		backToMenu.addActionListener(e);
		backToMenuAn.addActionListener(e);
		
		client.addActionListener(e);
		server.addActionListener(e);
		
		firstCellfirstPlayer.addActionListener(e);
		secondCellfirstPlayer.addActionListener(e);
		thirdCellfirstPlayer.addActionListener(e);
		fourthCellfirstPlayer.addActionListener(e);
		fifthCellfirstPlayer.addActionListener(e);
		sixthCellfirstPlayer.addActionListener(e);
		
		firstCellsecondPlayer.addActionListener(e);
		secondCellsecondPlayer.addActionListener(e);
		thirdCellsecondPlayer.addActionListener(e);
		fourthCellsecondPlayer.addActionListener(e);
		fifthCellsecondPlayer.addActionListener(e);
		sixthCellsecondPlayer.addActionListener(e);
	}
	
	/** @return serverIP, if the last was written in IP's TextField */
	public String returnServerIp(){return serverIp.getText();}
	
	/* Rreturn functions for the controller
	to know which button was pressed */
	public JButton getButtonNewGame(){return newGame;}
	public JButton getButtonFAQ(){return fAQ;}
	public JButton getButtonStopMusic() {return stopMusic;}
	public JButton getButtonMultiplayer(){return multiplayer;}
	public JButton getButtonBackToMenu(){return backToMenu;}
	public JButton getButtonBackToMenuAn(){return backToMenuAn;}
	
	public JButton getButtonClient(){return client;}
	public JButton getButtonServer(){return server;}
	
	public JButton getButtonFirstCellfirstPlayer(){return firstCellfirstPlayer;}
	public JButton getButtonSecondCellfirstPlayer(){return secondCellfirstPlayer;}
	public JButton getButtonThirdCellfirstPlayer(){return thirdCellfirstPlayer;}
	public JButton getButtonFourthCellfirstPlayer(){return fourthCellfirstPlayer;}
	public JButton getButtonFifthCellfirstPlayer(){return fifthCellfirstPlayer;}
	public JButton getButtonSixthCellfirstPlayer(){return sixthCellfirstPlayer;}
	
	public JButton getButtonFirstCellsecondPlayer(){return firstCellsecondPlayer;}
	public JButton getButtonSecondCellsecondPlayer(){return secondCellsecondPlayer;}
	public JButton getButtonThirdCellsecondPlayer(){return thirdCellsecondPlayer;}
	public JButton getButtonFourthCellsecondPlayer(){return fourthCellsecondPlayer;}
	public JButton getButtonFifthCellsecondPlayer(){return fifthCellsecondPlayer;}
	public JButton getButtonSixthCellsecondPlayer(){return sixthCellsecondPlayer;}
}
