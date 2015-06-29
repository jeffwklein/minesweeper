
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameScreen extends JPanel {
	
	public static final int
		TOP_PIXEL_BUFFER = 60,
		LEFT_PIXEL_BUFFER = 15,
		RIGHT_PIXEL_BUFFER = 15,
		BOTTOM_PIXEL_BUFFER = 15,
		SQUARE_SIZE = 20;
	
	private Square[][] board;
	private javax.swing.Timer timer;
	private int boardRows = 24, boardCols = 24, mines = 99, flags = mines;	
	private int xPixels = LEFT_PIXEL_BUFFER + (SQUARE_SIZE * boardCols) + RIGHT_PIXEL_BUFFER,
				yPixels = TOP_PIXEL_BUFFER + (SQUARE_SIZE * boardRows) + BOTTOM_PIXEL_BUFFER;
	
	private static boolean gameStarted = false, gameLost = false, gameWon = false;
	
	private static long time;

	public GameScreen() {
		newBoard();
		
		timer = new javax.swing.Timer(1000, new TimerListener());
		
		addMouseListener(new MouseListener());
		setFocusable(true);
		
		setPreferredSize(new Dimension(xPixels,yPixels));
		setBackground(Color.black);
		
		repaint();
	}
	
	public void newBoard() {
		flags = mines;
		board = new Square[boardRows][boardCols];
		for (int r = 0; r < boardRows; r++) {
			for (int c = 0; c < boardCols; c++) {
				board[r][c] = new Square(r, c);
			}
		}
	}

	public static boolean gameWon() {
		return gameWon;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(new Font("default", Font.PLAIN, 14));

		drawBackgroundShapes(g);
		drawScore(g);
		drawBoard(g);
	}

	private void drawBoard(Graphics g) {
		checkBoard();
		if (gameLost) {
			loseGame();
		} else if (gameWon) {
			winGame();
		}
		for (int r = 0; r < boardRows; r++) {
			for (int c = 0; c < boardCols; c++) {
				board[r][c].draw(g);
			}
		}
		NewGameButton.draw(g);
	}

	private void drawBackgroundShapes(Graphics g) {
		g.setColor(Color.lightGray);
		g.fillRect(0, 0, xPixels, yPixels);
		g.setColor(Color.decode("#909090"));
		g.fillRect(10, 10, 490, 40);
		g.fillRect(LEFT_PIXEL_BUFFER - 5, TOP_PIXEL_BUFFER - 5,
			xPixels - LEFT_PIXEL_BUFFER - RIGHT_PIXEL_BUFFER + 10,
			yPixels - TOP_PIXEL_BUFFER - BOTTOM_PIXEL_BUFFER + 10);
		g.setColor(Color.black);
		g.fillRect(LEFT_PIXEL_BUFFER, 15, xPixels - LEFT_PIXEL_BUFFER - RIGHT_PIXEL_BUFFER, 30);
	}
	
	private void drawScore(Graphics g) {
		String str = "" + flags;
		g.setColor(Color.white);
		g.drawString(str, 40, 35);
		long i = System.currentTimeMillis() - time;
		String timeMessage = "Time: " + (i/1000);
		if (gameLost) {
			timeMessage = "Time: " + (time/1000);
		}
		if (gameStarted) {
			g.drawString(timeMessage, 300, 35);
		}
	}

	private void checkBoard() {
		if (gameWon) {
			return;
		}
		gameWon = true;
		for (int r = 0; r < boardRows; r++) {
			for (int c = 0; c < boardCols; c++) {
				if (!board[r][c].isMine() && !board[r][c].isRevealed()) {
					gameWon = false;
					return;
				}
			}
		}
	}

	private void loseGame() {
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				if ((board[i][j].isFlagged() || board[i][j].isMine()) && !board[i][j].isRevealed()) {
					board[i][j].reveal();
				}
			}
		}
	}

	private void winGame() {
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				board[i][j].reveal();
			}
		}
	}

	public void generateBoard(int row, int col) {
		boolean[][] flags = new boolean[boardRows][boardCols];
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				if (board[i][j].isFlagged()) {
					flags[i][j] = true;
				}
				else {
					flags[i][j] = false;
				}
			}
		}
		board = new Square[boardRows][boardCols];
		int minesLeft = mines;
		int count = boardRows * boardCols;
		boolean placeMine = false;
		for (int i = 0; i < boardRows; i++) {
			for (int j = 0; j < boardCols; j++) {
				int rand = 1 + (int)(Math.random() * count);
				if (row-i <= 1 && row-i >= -1 && col-j <= 1 && col-j >= -1) {
					placeMine = false;
				}
				else if (rand <= minesLeft) {
					placeMine = true;
					minesLeft--;
				}
				else {
					placeMine = false;
				}
				board[i][j] = new Square(i, j, placeMine);
				if (flags[i][j] == true) {
					board[i][j].toggleFlag(true);
				}
				count--;
			}
		}
		for (int r = 0; r < boardRows; r++) {
			for (int c = 0; c < boardCols; c++) {
				if (r != 0) {
					//north
					if (board[r-1][c].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (r != 0 && c != boardCols-1) {
					//northeast
					if (board[r-1][c+1].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (c != boardCols-1) {
					//east
					if (board[r][c+1].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (r != boardRows-1 && c != boardCols-1) {
					//southeast
					if (board[r+1][c+1].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (r != boardRows-1) {
					//south
					if (board[r+1][c].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (r != boardRows-1 && c != 0) {
					//southwest
					if (board[r+1][c-1].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (c != 0) {
					//west
					if (board[r][c-1].isMine()) {
						board[r][c].incrementNumber();
					}
				}
				if (r != 0 && c != 0) {
					//northwest
					if (board[r-1][c-1].isMine()) {
						board[r][c].incrementNumber();
					}
				}
			}
		}
	}
	
	private void recursiveReveal(int r, int c) {
		if (board[r][c].getNumber() == 0) {
			Square current = null;
			if (r != 0) {
				//north
				current = board[r-1][c];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r-1, c);
					}
				}
			}
			if (r != 0 && c != boardCols-1) {
				//northeast
				current = board[r-1][c+1];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r-1, c+1);
					}
				}
			}
			if (c != boardCols-1) {
				//east
				current = board[r][c+1];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r, c+1);
					}
				}
			}
			if (r != boardRows-1 && c != boardCols-1) {
				//southeast
				current = board[r+1][c+1];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r+1, c+1);
					}
				}
			}
			if (r != boardRows-1) {
				//south
				current = board[r+1][c];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r+1, c);
					}
				}
			}
			if (r != boardRows-1 && c != 0) {
				//southwest
				current = board[r+1][c-1];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r+1, c-1);
					}
				}
			}
			if (c != 0) {
				//west
				current = board[r][c-1];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r, c-1);
					}
				}
			}
			if (r != 0 && c != 0) {
				//northwest
				current = board[r-1][c-1];
				if (!current.isMine() && !current.isFlagged() && !current.isRevealed()) {
					current.reveal();
					if (current.getNumber() == 0) {
						recursiveReveal(r-1, c-1);
					}
				}
			}
		}
	}
	
	public void newGame() {
		NewGameButton.reset();
		gameWon = false;
		gameStarted = false;
		gameLost = false;
		newBoard();
		repaint();
	}
	
	private class TimerListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			repaint();
			if (gameLost || gameWon) {
				timer.stop();
			}
		}
	}
	
	private class MouseListener extends MouseAdapter {
		public void mouseReleased(MouseEvent event)
		{
			int row = -1, col = -1;
			for (int i = 0; i < boardRows; i++) {
				for (int j = 0; j < boardCols; j++) {
					if (board[i][j].isPressed()) {
						row = i;
						col = j;
						board[i][j].setPressed(false);
					}
				}
			}
			if (row > -1 && !gameLost) {
				if (!gameStarted) {
					time = System.currentTimeMillis();
					timer.start();
				}
				if (!gameStarted && !SwingUtilities.isRightMouseButton(event) && !board[row][col].isFlagged()) {
					generateBoard(row, col);
					gameStarted = true;
				}
				if (SwingUtilities.isRightMouseButton(event)) {
					board[row][col].toggleFlag(!board[row][col].isFlagged());
					if (board[row][col].isFlagged()) {
						flags--;
					}
					else {
						flags++;
					}
				}
				else if (!board[row][col].isFlagged() && gameStarted) {
					board[row][col].reveal();
					if (board[row][col].isMine()) {
						gameLost = true;
						board[row][col].explode();
						time = System.currentTimeMillis()-time;
					}
					else {
						recursiveReveal(row,col);
					}
				}
			}
			else if (event.getX() >= NewGameButton.getX() &&
					 event.getX() <= NewGameButton.getX()+NewGameButton.getWidth() &&
					 event.getY() >= NewGameButton.getY() &&
					 event.getY() <= NewGameButton.getY()+NewGameButton.getHeight()) {
				NewGameButton.pressed(false);
				if (SwingUtilities.isRightMouseButton(event)) {
					gameWon = true;
				}
				else {
					newGame();
				}
			}
			repaint();
		}
		public void mousePressed(MouseEvent event) {
			int col = (event.getX()-LEFT_PIXEL_BUFFER)/20;
			int row = (event.getY()-TOP_PIXEL_BUFFER)/20;
			
			if (row >= 0 && row < boardRows && col >= 0 && col < boardCols &&
					!board[row][col].isRevealed() && !gameLost) {
				board[row][col].setPressed(true);
				if (!SwingUtilities.isRightMouseButton(event)) {
					repaint();
				}
			}
			else if (event.getX() >= NewGameButton.getX() &&
					 event.getX() <= NewGameButton.getX()+NewGameButton.getWidth() &&
					 event.getY() >= NewGameButton.getY() &&
					 event.getY() <= NewGameButton.getY()+NewGameButton.getHeight()) {
				NewGameButton.pressed(true);
				repaint();
			}
		}
	}
}
