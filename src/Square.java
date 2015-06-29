
import java.awt.*;

public class Square {
	
	protected int row, column, x, y, number;
	protected boolean mine = false, flagged = false, revealed = false, pressed = false, explode = false;
	
	public Square(int row, int column, boolean mine) {
		this.row = row;
		this.column = column;
		this.mine = mine;
		this.x = GameScreen.LEFT_PIXEL_BUFFER + (column * GameScreen.SQUARE_SIZE);
		this.y = GameScreen.TOP_PIXEL_BUFFER + (row * GameScreen.SQUARE_SIZE);
		this.number = 0;
	}
	
	public Square(int row, int column) {
		this.row = row;
		this.column = column;
		this.mine = false;
		this.x = 15 + (column * GameScreen.SQUARE_SIZE);
		this.y = GameScreen.TOP_PIXEL_BUFFER + (row * GameScreen.SQUARE_SIZE);
	}
	
	public int getNumber() {
		return number;
	}
	
	public boolean isMine() {
		return mine;
	}
	
	public boolean isFlagged() {
		return flagged;
	}
	
	public boolean isRevealed() {
		return revealed;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public void incrementNumber() {
		number++;
	}
	
	public void toggleFlag(boolean b) {
		flagged = b;
	}
	
	public void reveal() {
		revealed = true;
	}
	
	public void setPressed(boolean b) {
		pressed = b;
	}
	
	public void explode() {
		explode = true;
	}
	
	public void draw(Graphics g) {
		if (!revealed) {
			g.setColor(Color.decode("#E0E0E0"));
			g.fill3DRect(x, y, GameScreen.SQUARE_SIZE, GameScreen.SQUARE_SIZE, !pressed || flagged);
			if (flagged) {
				drawFlag(g);
			}
		}
		else {
			g.setColor(Color.decode("#D0D0D0"));
			if (explode) {
				g.setColor(Color.red);
			}
			else if (GameScreen.gameWon()) {
				g.setColor(Color.green);
			}
			g.fillRect(x+1, y+1, GameScreen.SQUARE_SIZE-2, GameScreen.SQUARE_SIZE-2);
			if (!mine && !flagged) {
				g.setFont(new Font("default", Font.BOLD, 14));
				switch (number) {
				case 0 : 
					g.setColor(Color.gray);
					break;
				case 1 :
					g.setColor(Color.blue);
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 2 :
					g.setColor(Color.decode("#00A000"));
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 3 :
					g.setColor(Color.red);
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 4 :
					g.setColor(Color.decode("#000080"));
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 5 :
					g.setColor(Color.decode("#800000"));
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 6 :
					g.setColor(Color.decode("#66DDAA"));
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 7 :
					g.setColor(Color.black);
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				case 8 :
					g.setColor(Color.gray);
					g.drawString(((Integer)number).toString(), x + 6, y + 15);
					break;
				}
				g.setFont(new Font("default", Font.PLAIN, 14));
			}
			else if (mine && flagged) {
				g.setColor(Color.decode("#E0E0E0"));
				g.fill3DRect(x, y, GameScreen.SQUARE_SIZE, GameScreen.SQUARE_SIZE, true);
				drawFlag(g);
			}
			else if (mine && !flagged) {
				//mine drawing
				g.setColor(Color.black);
				g.fillOval(x+4, y+4, 10, 10);
				g.drawLine(x+9, y+2, x+9, y+16);
				g.drawLine(x+2, y+9, x+16, y+9);
				g.setColor(Color.white);
				g.fillOval(x+10, y+6, 3, 3);
			}
			else if (!mine && flagged) {
				g.setColor(Color.decode("#808080"));
				g.fillOval(x+4, y+4, 10, 10);
				g.drawLine(x+9, y+2, x+9, y+16);
				g.drawLine(x+2, y+9, x+16, y+9);
				g.setColor(Color.decode("#AFAFAF"));
				g.fillOval(x+10, y+6, 3, 3);
				g.setColor(Color.red);
				g.drawLine(x, y, x+20, y+20);
				g.drawLine(x, y+20, x+20, y);
				
				g.drawLine(x, y+1, x+19, y+20);
				g.drawLine(x+1, y, x+20, y+19);
				g.drawLine(x, y+19, x+19, y);
				g.drawLine(x+1, y+20, x+20, y+1);
			}
		}
		/*
		if (pressed && !flagged) {
			g.setColor(Color.decode("#E0E0E0"));
			g.fill3DRect(x, y, GameScreen.SQUARE_SIZE, GameScreen.SQUARE_SIZE, false);
		}
		*/
	}
	
	private void drawFlag(Graphics g) {
		g.setColor(Color.black);
		g.drawLine(x+5, y+3, x+5, y+15);
		g.setColor(Color.red);
		g.fillArc(x-3, y+4, 20, 10, 0, 90);
	}



	
}
