package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall4ChaseExtra;
import Handlers.Content;

// chases until it gets close enough and splits into two balls that go in -90 and 90 degree directions and split again after a while until out of total splits
public class BossBall4Chase extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;

	private double initialTravelX;
	private double initialTravelY;
	
	private double diagonalSpeed;
	
	private int chaseCyclesLeft;
	
	private int currentAnimation;
	
	private final int IDLE = 0;
	private final int INITIALLAUNCHTRAVEL = 1;
	private final int CHASESTART = 2;
	private final int CHASEIDLE = 3;
	
	private long idleTime;
	private long idleStart;
	
	private long initialLaunchTravelTime;
	private long initialLaunchTravelStart;

	private long chaseIdleStart;
	private long chaseIdleTime;


	
	public BossBall4Chase(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 2.5;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 5;
		cheight = 5;
		
		health = maxHealth = 2;
		damage = 9;
		
		diagonalSpeed = 2.0;
		
		chaseCyclesLeft = 8;

		idleTime = 400;
		initialLaunchTravelTime = 700;
		chaseIdleTime = 400;
		
		initialLaunchTravelStart = System.nanoTime();
		
		currentAnimation = INITIALLAUNCHTRAVEL;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.BossBall4Chase[0];
		
		spritesM = Content.BossBall4ChaseM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(380);
		
		// extra data contains splits left
		setExtraData(mode, extraData);
	}

	private void getNextPosition() {
		if(currentAnimation == INITIALLAUNCHTRAVEL) {
			dx = initialTravelX;
			dy = initialTravelY;
		}
		else if(currentAnimation == CHASESTART) {
			if(playerX >= x) dx = diagonalSpeed;
			else if(playerX < x) dx = diagonalSpeed * (-1);
			else System.out.println("Error in BossBall4Chase with playerX & x " + playerX + " " + x);
			if(playerY >= y) dy = diagonalSpeed;
			else if(playerY < y) dy = diagonalSpeed * (-1);
			else System.out.println("Error in BossBall4Chase with playerY & y " + playerY + " " + y);
		}
		else if(currentAnimation == CHASEIDLE) {
			// nothing here, just neutral flying
		}
		else if(currentAnimation == IDLE) {
			dx = 0;
			dy = 0;
		}
		else System.out.println("Wrong currentAnimation " + currentAnimation + " in BossBall4Chase");
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - idleStart) / 1000000;
			
			if(passedTime >= idleTime) {
				if(chaseCyclesLeft > 0) chaseCyclesLeft--;
				else dead = true;
				
				currentAnimation = CHASESTART;
			}
		}
		else if(currentAnimation == INITIALLAUNCHTRAVEL) {
			long passedTime = (System.nanoTime() - initialLaunchTravelStart) / 1000000;
			
			if(passedTime >= initialLaunchTravelTime) {
				currentAnimation = CHASESTART;
			}
		}
		else if(currentAnimation == CHASESTART) {
			currentAnimation = CHASEIDLE;
			chaseIdleStart = System.nanoTime();
		}
		else if(currentAnimation == CHASEIDLE) {
			long passedTime = (System.nanoTime() - chaseIdleStart) / 1000000;
			
			if(passedTime >= chaseIdleTime) {
				currentAnimation = IDLE;
				idleStart = System.nanoTime();
			}
		}
	}

	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > flinchTime) {
				flinching = false;
			}
		}
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) return;
		}
		
		super.draw(g);
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public void checkTileMapCollision() {
		
		double decimalCurrCol = x / tileSize;
		double decimalCurrRow = y / tileSize;
		
		if(decimalCurrCol <= 0) currCol = (int) decimalCurrCol - 1;
		else currCol = (int) decimalCurrCol;
		if(decimalCurrRow <= 0) currRow = (int) decimalCurrRow - 1;
		else currRow = (int) decimalCurrRow;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		
		if(dy < 0) {
			if(topLeft || topRight) {
				dy *= -1;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy *= -1;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx *= -1;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx *= -1;
			}
			else {
				xtemp += dx;
			}
		}
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			sprites = spritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			sprites = spritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		animation.swapFrames(sprites);
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		BossBall4ChaseExtra ed = (BossBall4ChaseExtra) extraData;
		this.initialTravelX = ed.initialTravelX;
		this.initialTravelY = ed.initialTravelY;
	}
}









