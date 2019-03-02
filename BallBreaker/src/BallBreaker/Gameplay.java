package BallBreaker;

// relevant imports
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.JPanel;

// main class extending JPanel for components/GUI
public class Gameplay extends JPanel implements KeyListener, ActionListener {
	private boolean play = false;
	private int score = 0;
	private int totalBricks = 21;
	private Timer timer;
	private int delay = 7;
	private int playerX = 310;
	private int ballPosX = 120;
	private int ballPosY = 350;
	private int ballXdirection = -1;
	private int ballYdirection = -2;
	
	private MapGenerator map;
	
	// sets-up gameplay method
	public Gameplay() { 
		map = new MapGenerator(3, 7);
		addKeyListener(this); 						// *key-listener* for receiving keyboard events (keystrokes)
		setFocusable(true); 						// 	Does not effect component - set to default val (true)
		setFocusTraversalKeysEnabled(false); 		// *setFocusTraversalKeysEnabled* for traversal along JPanel component (movement along X/Y axis)
		timer = new Timer(delay, this);				// *timer* via package, used for scheduling intervaled executions of background thread.
		timer.start();								// 	starts timer
	}
	
	// paint method to draw assets/components	
	public void paint(Graphics g) {
		
		// background set
		g.setColor(Color.BLACK);
		g.fillRect(1, 1, 692, 592);
		
		// draws map/sets map
		map.draw((Graphics2D)g);
		
		// borders set
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		// scores set
		g.setColor(Color.RED);
		g.setFont(new Font("Monospaced", Font.BOLD, 25));
		g.drawString("" + score, 640, 35);
		
		// paddle created
		g.setColor(Color.WHITE);
		g.fillRect(playerX, 550, 100, 8);
		
		// ball created
		g.setColor(Color.RED);
		g.fillOval(ballPosX, ballPosY, 20, 20);
		
		// checks if all bricks have been destroyed - end game = player win
		if(totalBricks <= 0) {
			play = false;
			ballXdirection = 0;
			ballYdirection = 0;
			g.setColor(Color.GREEN);
			g.setFont(new Font("Monospaced", Font.BOLD, 25));
			g.drawString("YOU WIN - SCORE: ", 190, 300);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced", Font.BOLD, 15));
			g.drawString("Press Enter to play again", 250, 350);
		}
		
		// checks if ball passes paddle (player loses - end/game over msg)
		if(ballPosY > 570) {
			play = false;
			ballXdirection = 0;
			ballYdirection = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("Monospaced", Font.BOLD, 25));
			g.drawString("GAME OVER - SCORE: ", 190, 300);
			
			g.setColor(Color.WHITE);
			g.setFont(new Font("Monospaced", Font.BOLD, 15));
			g.drawString("Press Enter to play again", 230, 330);
		}
		
		g.dispose();
		
	}
	
	// sets action events for ball positioning and direction
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start(); 																							// timer begins for game initialisation
		if(play) { 																							// checks for game state
			if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 7))) {	
				ballYdirection = -ballYdirection;
			}
			
			// detect if brick set in 2D array is accessed
			A: for(int i = 0; i < map.map.length; i++) { // accessed 2D array and map inside variable of mGame
				for(int j = 0; j <map.map[0].length; j++) {
					if(map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickWidth + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						// creates rectangle around brick - border/boundaries
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
						Rectangle brickRect = rect;
						
						// if brick intersects brick rectangle - call map func, set value 0
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks--; // decrements bricks when hit
							score += 5; // sets brick value in score to 5
							
							if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
								ballXdirection = -ballXdirection;
							} else {
								ballYdirection = -ballYdirection;
							}
							
							break A;
						}
					}
				}
			}
			
			
			
			// ball positioning X/Y equivalency to direction X/Y
			ballPosX += ballXdirection;
			ballPosY += ballYdirection;
			
			// checks boundary against left border
			if(ballPosX < 0) { 
				ballXdirection = -ballXdirection;
			}
			
			// checks boundary against top border
			if(ballPosY < 0) {
				ballYdirection = -ballYdirection;
			}
			
			// checks boundary against right border
			if(ballPosX > 670) {
				ballXdirection = -ballXdirection;
			}
		}
		
		repaint();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
	}
	@Override
	public void keyReleased(KeyEvent e) {
	}

	// sets key events for left and right input movement
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
			}
		}
		
		// sets up ENTER key event - (restart process(Game))
		// restarts vars/comps to re-initiate process
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(!play) {
				play = true;
				ballPosX = 120;
				ballPosY = 350;
				ballXdirection = -1;
				ballYdirection = -2;
				playerX = 310;
				score = 0;
				totalBricks = 21;
				map = new MapGenerator(3, 7);
				
				repaint();
			}
		}
		
	}
	
	// movement of paddle left/right - set to horizontal limit
	// sets method for moving paddle right bound
	public void moveRight() {
		play = true;
		playerX += 20;
	}
	
	// sets method for moving paddle left bound
	public void moveLeft() {
		play = true;
		playerX -= 20;
	}
	
}
