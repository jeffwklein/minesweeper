
import javax.swing.*;

public class GameWindow
{
	public static void main(String[] args)
	{
		JFrame window = new JFrame("Minesweeper");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GameScreen game = new GameScreen();
		window.getContentPane().add(game);
		
		window.pack();
		window.setVisible(true);
		window.setResizable(false);
	}
}