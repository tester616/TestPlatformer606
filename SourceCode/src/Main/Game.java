package Main;

import javax.swing.JFrame;

public class Game {
	
	public static JFrame window = new JFrame("606");
	
	public static void main(String[] args) {
		window.setContentPane(new GamePanel());
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.pack();
		window.setVisible(true);
<<<<<<< HEAD
=======
		window.setLocationRelativeTo(null);
>>>>>>> 2.03
	}
}
