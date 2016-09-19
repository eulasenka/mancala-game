/**
 * The Game - Mancala
 * Lecturer - Piotr Witonski
 * @author Eduard Ulasenka, 273173
 */

import java.awt.EventQueue;

/**
 * Game's main class
 * Creates classes: View, Model, Controller
 * @author Eduard Ulasenka, 273173
 */
public class MainMVC {

	public static void main(String[] args){
   	 EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View theView = new View();
 					Model theModel = new Model();
 					Controller theController = new Controller(theView, theModel);
 					
 					theView.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }
}
