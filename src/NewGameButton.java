
import java.awt.Color;
import java.awt.Graphics;

public class NewGameButton {
	
	private static boolean pressed = false, released = false;
	private static int x = 410, y = 20, height = 20, width = 490-x;
	
	public NewGameButton() {}
	
	public static int getX() {
		return x;
	}
	public static int getY() {
		return y;
	}
	public static int getHeight() {
		return height;
	}
	public static int getWidth() {
		return width;
	}
	
	public static void pressed(boolean b) {
		pressed = b;
	}
	public static void released(boolean b) {
		released = b;
	}
	public static boolean isPressed() {
		return pressed;
	}
	public static boolean isReleased() {
		return released;
	}
	public static void reset() {
		pressed = false;
		released = false;
	}
	
	public static void draw(Graphics g) {
		g.setColor(Color.decode("#E0E0E0"));
		g.fill3DRect(x, y, width, height, !pressed);
		g.setColor(Color.black);
		g.drawString("NEW GAME", x+3, 35);
	}

}
