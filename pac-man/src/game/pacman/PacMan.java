package game.pacman;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.*;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.game.engine.Game;
import org.game.engine.GameApplication;

public class PacMan extends Game {
	
	public static void main(String[] args) {
		GameApplication.start(new PacMan());
	}

	final int STEP = 2;
	
	
	BufferedImage packman;
	BufferedImage ghost;
	BufferedImage target;
	int frame; // refreshing frame length
	int frame2;
	int reqDir, ableDir; //requesting direction & current direction
	int M1reqDir, M1ableDir; // up 0     down 1    left 2    right 3
	int M2reqDir, M2ableDir;
	int M3reqDir, M3ableDir;
	int M4reqDir, M4ableDir;
	int M5reqDir, M5ableDir;
	int M6reqDir, M6ableDir;
	int M7reqDir, M7ableDir;
	int M8reqDir, M8ableDir;
	
	
	int x, y; // keep track of the pacman position
	int M1x, M1y;
	int M2x, M2y;
	int M3x, M3y;
	int M4x, M4y;
	int M5x, M5y;
	int M6x, M6y;
	int M7x, M7y;
	int M8x, M8y;
	int columns, rows; // total columns and rows in a maze.txt
	int score = 0;
	final int timeLimit = 60;
	int time = timeLimit;
	int timeCount = 0;
	boolean timeOut = false;
	boolean dead = false;
	int Markx, Marky;
	
	
	ArrayList<String> lines = new ArrayList<String>();
	BufferedImage[] mazeImages = new BufferedImage[4];
	int mazeNo;
	Maze[] mazes = new Maze[4];
	char[][] copy; // copy of chars in current maze
	
	final String[] Mazes = { "0", "1", "2", "3"};
	public PacMan() {
		   String MazeChoice = (String) JOptionPane.showInputDialog(
			        GameApplication.frame, "Please Choose a Maze:", "Maze Selection",
			        JOptionPane.PLAIN_MESSAGE, null, Mazes, Mazes[0]);
		   mazeNo = Integer.parseInt(MazeChoice);
		
		// load mazes information
		for (int m=0; m<4; m++) {
			mazes[m] = new Maze(m);
		}
		
		// update the information from mazes[mazeNo]
		// get copy cells
		copy = mazes[mazeNo].getCells();
		rows = mazes[mazeNo].rows;
		columns = mazes[mazeNo].columns;
		// initial position for packman
		y = mazes[mazeNo].row;
		x = mazes[mazeNo].column;
		Marky = mazes[mazeNo].row;
		Markx = mazes[mazeNo].column;
		
		M1y = mazes[mazeNo].Mrow;
		M1x = mazes[mazeNo].Mcolumn;
		M2y = mazes[mazeNo].Mrow;
		M2x = mazes[mazeNo].Mcolumn;
		M3y = mazes[mazeNo].Mrow;
		M3x = mazes[mazeNo].Mcolumn;
		M4y = mazes[mazeNo].Mrow;
		M4x = mazes[mazeNo].Mcolumn;
		/*		M5y = mazes[mazeNo].Mrow;
		M5x = mazes[mazeNo].Mcolumn;
		M6y = mazes[mazeNo].Mrow;
		M6x = mazes[mazeNo].Mcolumn;
		M7y = mazes[mazeNo].Mrow;
		M7x = mazes[mazeNo].Mcolumn;
		M8y = mazes[mazeNo].Mrow;
		M8x = mazes[mazeNo].Mcolumn;*/
		// size of the game screen
		width = mazes[mazeNo].width;
		height = mazes[mazeNo].height;
		
		title = "PIC20A PacMan";
		frame = 0;
		ableDir = reqDir = KeyEvent.VK_RIGHT;
		M1ableDir = M1reqDir = 0;
		M2ableDir = M2reqDir = 1;
		M3ableDir = M3reqDir = 2;
		M4ableDir = M4reqDir = 3;
		M5ableDir = M5reqDir = 0;
		M6ableDir = M6reqDir = 1;
		M7ableDir = M7reqDir = 2;
		M8ableDir = M8reqDir = 3;
		try {
			packman = ImageIO.read(new File("images/packman.png"));
			ghost = ImageIO.read(new File("images/pac2.png"));
			target = ImageIO.read(new File("images/target.png"));
			
			for (int m=0; m<4; m++) {
				mazeImages[m] = ImageIO.read(new File("images/"+m+m+".png"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (37 <= key && key <= 40) {
			reqDir = key;
		}
		else if (key == KeyEvent.VK_SPACE){
			x = Markx;
			y = Marky;
		}
		else if (key == KeyEvent.VK_M){
			Markx = x;
			Marky = y;
		}
	}
	 
	
	@Override
	public void update() {
		frame++;
		
		if (frame > 5) {
			frame = 0;
		}
		
		frame2++;
		if(frame2 > 24) {
			frame2 =0;
		}
		
		timeCount++;
		if (timeCount >= 1000/delay){
			time--;
			if (time <= timeLimit/2){
				M1reqDir = (int)Math.floor(Math.random()*4);
				M2reqDir = (int)Math.floor(Math.random()*4);
				M3reqDir = (int)Math.floor(Math.random()*4);
				M4reqDir = (int)Math.floor(Math.random()*4);
				M5reqDir = (int)Math.floor(Math.random()*4);
				M6reqDir = (int)Math.floor(Math.random()*4);
				M7reqDir = (int)Math.floor(Math.random()*4);
				M8reqDir = (int)Math.floor(Math.random()*4);
			}
			
			if (time <= 0){
				timeOut = true;
				over = true;
			}
			timeCount = 0;
		}
		
		double Jcor = 14;
		if ((x+Jcor > M1x && x-Jcor < M1x && y+Jcor > M1y && y-Jcor < M1y)||(x+Jcor > M2x && x-Jcor < M2x && y+Jcor > M2y && y-Jcor < M2y)||(x+Jcor > M3x && x-Jcor < M3x && y+Jcor > M3y && y-Jcor < M3y)||(x+Jcor > M4x && x-Jcor < M4x && y+Jcor > M4y && y-Jcor < M4y)||(x+Jcor > M5x && x-Jcor < M5x && y+Jcor > M5y && y-Jcor < M5y)||(x+Jcor > M6x && x-Jcor < M6x && y+Jcor > M6y && y-Jcor < M6y)||(x+Jcor > M7x && x-Jcor < M7x && y+Jcor > M7y && y-Jcor < M7y)||(x+Jcor > M8x && x-Jcor < M8x && y+Jcor > M8y && y-Jcor < M8y)){
			dead = true;
			over = true;
		}
		
		if ((score > 273 && mazeNo == 3) || (score > 261 && mazeNo == 0) || (score > 283 && mazeNo == 1) || (score > 279 && mazeNo == 2)){
			over = true;
		}
		
		// if the the request direction is possible, change the direction of pacman
		// otherwise, stick with the current direction
		if (move(reqDir) == SUCCESS) {
			ableDir = reqDir;
		} else {
			move(ableDir);
		}
		
		if (M1move(M1reqDir) == SUCCESS) {
			M1ableDir = M1reqDir;
		} else {
			M1move(M1ableDir);
		}
		if (M2move(M2reqDir) == SUCCESS) {
			M2ableDir = M2reqDir;
		} else {
			M2move(M2ableDir);
		}
		if (M3move(M3reqDir) == SUCCESS) {
			M3ableDir = M3reqDir;
		} else {
			M3move(M3ableDir);
		}
		if (M4move(M4reqDir) == SUCCESS) {
			M4ableDir = M4reqDir;
		} else {
			M4move(M4ableDir);
		}
		if (M5move(M5reqDir) == SUCCESS) {
			M5ableDir = M5reqDir;
		} else {
			M5move(M5ableDir);
		}
		if (M6move(M6reqDir) == SUCCESS) {
			M6ableDir = M6reqDir;
		} else {
			M6move(M6ableDir);
		}
		if (M7move(M7reqDir) == SUCCESS) {
			M7ableDir = M7reqDir;
		} else {
			M7move(M7ableDir);
		}
		if (M8move(M8reqDir) == SUCCESS) {
			M8ableDir = M8reqDir;
		} else {
			M8move(M8ableDir);
		}
		
		
		
		
		// updating the pills
		if (copy[y][x] == '2') {
			// eat the pill
			copy[y][x] = '1';
			score = score + 1;
		} else if (copy[y][x] == '3') {
			// eat the power pill
			copy[y][x] = '1';
			score = score + 10;
			delay -= 5;
		}
		
		
	}
	
	static int SUCCESS = 1, FAIL = 0;
	
	private int move(int reqDir) {
		switch (reqDir) {
		case KeyEvent.VK_LEFT: // 37
			if (x > 0 && mazes[mazeNo].charAt(y, x-1) != '0') {
				x -= 1;
				return SUCCESS;
			} 
			if (x == 0 && copy[y][columns-1] == '1' ) {
				x = columns-1;
				return SUCCESS;
			}
			break;
		case KeyEvent.VK_UP:   // 38
			if (y > 0 && mazes[mazeNo].charAt(y-1, x) != '0') {
				y -= 1;
				return SUCCESS;
			}
			break;
		case KeyEvent.VK_RIGHT: // 39
			if (x < columns-1 && mazes[mazeNo].charAt(y, x+1) != '0') {
				x += 1;
				return SUCCESS;
			}
			if (x == columns-1 && copy[y][0] == '1' ) {
				x = 0;
				return SUCCESS;
			}
			break;
		case KeyEvent.VK_DOWN:  // 40
			if (y < rows-1 && mazes[mazeNo].charAt(y+1, x) != '0') {
				y += 1;
				return SUCCESS;
			}
			break;
		}
			
		return FAIL;
		
	}
	
	private int M1move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M1x > 0 && mazes[mazeNo].charAt(M1y, M1x-1) != '0') {
				M1x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M1y > 0 && mazes[mazeNo].charAt(M1y-1, M1x) != '0') {
				M1y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M1x < columns-1 && mazes[mazeNo].charAt(M1y, M1x+1) != '0') {
				M1x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M1y < rows-1 && mazes[mazeNo].charAt(M1y+1, M1x) != '0') {
				M1y += 1;
				return SUCCESS;
			}
			break;
		}
		M1reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	
	private int M2move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M2x > 0 && mazes[mazeNo].charAt(M2y, M2x-1) != '0') {
				M2x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M2y > 0 && mazes[mazeNo].charAt(M2y-1, M2x) != '0') {
				M2y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M2x < columns-1 && mazes[mazeNo].charAt(M2y, M2x+1) != '0') {
				M2x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M2y < rows-1 && mazes[mazeNo].charAt(M2y+1, M2x) != '0') {
				M2y += 1;
				return SUCCESS;
			}
			break;
		}
		M2reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	private int M3move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M3x > 0 && mazes[mazeNo].charAt(M3y, M3x-1) != '0') {
				M3x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M3y > 0 && mazes[mazeNo].charAt(M3y-1, M3x) != '0') {
				M3y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M3x < columns-1 && mazes[mazeNo].charAt(M3y, M3x+1) != '0') {
				M3x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M3y < rows-1 && mazes[mazeNo].charAt(M3y+1, M3x) != '0') {
				M3y += 1;
				return SUCCESS;
			}
			break;
		}
		M3reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	private int M4move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M4x > 0 && mazes[mazeNo].charAt(M4y, M4x-1) != '0') {
				M4x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M4y > 0 && mazes[mazeNo].charAt(M4y-1, M4x) != '0') {
				M4y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M4x < columns-1 && mazes[mazeNo].charAt(M4y, M4x+1) != '0') {
				M4x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M4y < rows-1 && mazes[mazeNo].charAt(M4y+1, M4x) != '0') {
				M4y += 1;
				return SUCCESS;
			}
			break;
		}
		M4reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	private int M5move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M5x > 0 && mazes[mazeNo].charAt(M5y, M5x-1) != '0') {
				M5x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M5y > 0 && mazes[mazeNo].charAt(M5y-1, M5x) != '0') {
				M5y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M5x < columns-1 && mazes[mazeNo].charAt(M5y, M5x+1) != '0') {
				M5x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M5y < rows-1 && mazes[mazeNo].charAt(M5y+1, M5x) != '0') {
				M5y += 1;
				return SUCCESS;
			}
			break;
		}
		M5reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	private int M6move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M6x > 0 && mazes[mazeNo].charAt(M6y, M6x-1) != '0') {
				M6x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M6y > 0 && mazes[mazeNo].charAt(M6y-1, M6x) != '0') {
				M6y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M6x < columns-1 && mazes[mazeNo].charAt(M6y, M6x+1) != '0') {
				M6x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M6y < rows-1 && mazes[mazeNo].charAt(M6y+1, M6x) != '0') {
				M6y += 1;
				return SUCCESS;
			}
			break;
		}
		M6reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	private int M7move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M7x > 0 && mazes[mazeNo].charAt(M7y, M7x-1) != '0') {
				M7x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M7y > 0 && mazes[mazeNo].charAt(M7y-1, M7x) != '0') {
				M7y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M7x < columns-1 && mazes[mazeNo].charAt(M7y, M7x+1) != '0') {
				M7x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M7y < rows-1 && mazes[mazeNo].charAt(M7y+1, M7x) != '0') {
				M7y += 1;
				return SUCCESS;
			}
			break;
		}
		M7reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}
	private int M8move(int reqDir) {
		switch (reqDir) {
		case 2: // left
			if (M8x > 0 && mazes[mazeNo].charAt(M8y, M8x-1) != '0') {
				M8x -= 1;
				return SUCCESS;
			} 
			break;
		case 0:   // up
			if (M8y > 0 && mazes[mazeNo].charAt(M8y-1, M8x) != '0') {
				M8y -= 1;
				return SUCCESS;
			}
			break;
		case 3: // right
			if (M8x < columns-1 && mazes[mazeNo].charAt(M8y, M8x+1) != '0') {
				M8x += 1;
				return SUCCESS;
			}
			break;
		case 1:  // left
			if (M8y < rows-1 && mazes[mazeNo].charAt(M8y+1, M8x) != '0') {
				M8y += 1;
				return SUCCESS;
			}
			break;
		}
		M8reqDir = (int)Math.floor(Math.random()*4);
		return FAIL;
	}

	@Override
	public void draw(Graphics2D g) {
		// draw maze
		g.drawImage(mazeImages[mazeNo], 0, 0, null);
		// draw pills
		
		for (int r=0; r<mazes[mazeNo].rows; r++) {
			for (int c=0; c<mazes[mazeNo].columns; c++) {
				if (copy[r][c] == '2') {
					// draw pill
					g.setColor(Color.white);
					g.fillRect(c*STEP-3, r*STEP-3, 6, 6);
				} else if (copy[r][c] == '3') {
					// draw power pill
					g.setColor(Color.RED);
					Ellipse2D.Double circle = new Ellipse2D.Double(c*STEP-8, r*STEP-8,15,15);
					g.fill(circle);
					g.draw(circle);
				}
			}
		}
		g.drawImage(target,Markx*STEP-14, Marky*STEP-14, null);
		// draw pacman
		g.drawImage(packman.getSubimage((frame/2)*30, (ableDir-37)*30, 28, 28),
				x*STEP-14, y*STEP-14, null);
		
		g.drawImage(ghost.getSubimage((frame2/5)*29, 0, 28, 28),M1x*STEP-14, M1y*STEP-14, null);
		g.drawImage(ghost.getSubimage((frame2/5)*29, 29, 28, 28),M2x*STEP-14, M2y*STEP-14, null);
		g.drawImage(ghost.getSubimage((frame2/5)*29, 58, 28, 28),M3x*STEP-14, M3y*STEP-14, null);
		g.drawImage(ghost.getSubimage((frame2/5)*29, 87, 28, 28),M4x*STEP-14, M4y*STEP-14, null);
		
		
		// draw score
		g.setColor(Color.DARK_GRAY);
		g.setFont(new Font("Batang", Font.BOLD, 25)); 
		
		String totalScore = "";
		if (mazeNo == 3){
			totalScore = "/274";
		}
		else if (mazeNo == 0){
			totalScore = "/262";
		}
		else if (mazeNo == 1){
			totalScore = "/284";
		}
		else{
			totalScore = "/280";
		}
		g.drawString("Scores: " + score + totalScore, 10, 540);
		
		g.setColor(Color.BLUE);
		g.drawString("Time:" + time , 220, 540);
		
		g.setColor(Color.RED);
		if (over&&!timeOut&&!dead){
			g.drawString("You win!", 350, 540);
       	    popWindow("You Win!");
       			 over = false;
		}
		if (over&&timeOut){
			g.drawString("Time out!", 330, 540);
			popWindow("Time out!");
  			 over = false;
		}
		if (over&&dead){
			g.drawString("Your dead!", 330, 540);
			popWindow("Your dead!");
  			 over = false;
		}
	}
	public void popWindow(String s){
		
		final JFrame myFrame = new JFrame();
		myFrame.setLocationRelativeTo(null);;
		myFrame.setSize(300,100);
		JPanel myPanel = new JPanel();
		JLabel myLabel = new JLabel(s);
		JButton exitButton = new JButton("Exit");
		JButton startButton = new JButton("Start Over");
		
				
		myFrame.add(myPanel,BorderLayout.CENTER);
		myPanel.add(myLabel);
		myPanel.add(startButton);
		myPanel.add(exitButton);
		
		myFrame.setVisible(true);
		myPanel.setVisible(true);
		
		exitButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		startButton.addActionListener(new ActionListener(){

			
			@Override
			public void actionPerformed(ActionEvent e) {
				GameApplication.frame.dispose();
				myFrame.dispose();
				GameApplication.start(new PacMan());
				
			}
		});

	}


}








