package BallBreaker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class MapGenerator {
	public int map[][]; // initialises 2D array
	public int brickWidth;
	public int brickHeight;
	
	// receive number of rows/columns to be generted for number of bricks
	public MapGenerator(int row, int column) {
		map = new int[row][column];
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = 1; // set to 1 - detects if stated brick is interacted with ball
			}
		}
		
		// sets basic brick dimens
		brickWidth = 540/column;
		brickHeight = 150/row;
	}
	
	// draws brick layout via 2D *map* array
	public void draw(Graphics2D g) {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				if(map[i][j] > 0) {
					g.setColor(Color.WHITE);
					g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
				
					g.setStroke(new BasicStroke(3));
					g.setColor(Color.BLACK);
					g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
					
				}
			}
		}
	}
	
	// method to allow intersection with individual bricks
	public void setBrickValue(int value, int row, int column) {
		map[row][column] = value;
	}
}
