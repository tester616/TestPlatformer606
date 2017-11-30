package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Handlers.Content;

public class BossBall2 extends Enemy {


	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;

	private int currentAnimation;
	
	private final int GROWING = 0;
	private final int RELEASING = 1;
	private final int FLYING = 2;
	
	private double totalSpeed;
	
	private int originalWidth;
	private int originalHeight;
	private int originalCwidth;
	private int originalCheight;
	
	/*private int maximumWidth;
	private int maximumHeight;
	private int maximumCWidth;
	private int maximumCHeight;*/
	
	private double minScale;
	private double maxScale;
	private double scale;
	
	private long growTime;
	private long growStart;
	private long growTimePassed;
	
	private int growBreakDistance;
	
	public BossBall2(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 2.0;
		
		totalSpeed = 2.0;
		
		originalWidth = 26;
		originalHeight = 26;
		originalCwidth = 17;
		originalCheight = 17;
		
		/*maximumWidth = 131;
		maximumHeight = 131;
		maximumCWidth = 115;
		maximumCHeight = 115;*/
		
		width = originalWidth;
		height = originalHeight;
		cwidth = originalCwidth;
		cheight = originalCheight;
		
		minScale = 1;
		maxScale = 7;
		
		growTime = 4500;
		growStart = System.nanoTime();
		
		growBreakDistance = 230;
		
		health = maxHealth = 1;
		damage = 7;
		
		currentAnimation = 0;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.BossBall2[0];
		
		spritesM = Content.BossBall2M[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(55);
		
		JukeBox.playWithRecommendedVolume("bossspiralballgrow");
	}

	private void updateGrowTime() {
		if(currentAnimation == GROWING) {
			growTimePassed = (System.nanoTime() - growStart) / 1000000;
		}
	}
	
	public void grow() {
		if(currentAnimation == GROWING) {
			scale = minScale + (maxScale - minScale) * ((double) growTimePassed / (double) growTime);
			width = (int) (originalWidth * scale);
			height = (int) (originalHeight * scale);
			cwidth = (int) (originalCwidth * scale);
			cheight = (int) (originalCheight * scale);
		}
	}

	private void getNextPosition() {
		if(currentAnimation == GROWING) {
			// nothing here
		}
		else if(currentAnimation == RELEASING) {
			// direction x and y values to player
			ArrayList<Double> dirXY = getDirectionToPoint(playerX, playerY);
			//dirXY = getDirectionToPoint(playerX, playerY);
			
			// gives dx and dy their final values, but in reverse as it tries to knockback
			knockback(dirXY.get(0), dirXY.get(1), totalSpeed, true, true);
			
			// reverse values taken times -1 to get real values
			dx *= -1;
			dy *= -1;
		}
		else if(currentAnimation == FLYING) {
			// nothing here
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == GROWING) {
			if(growTimePassed > growTime || isPlayerOutsideGrowDistance()) {
				currentAnimation = RELEASING;
				JukeBox.stop("bossspiralballgrow");
				JukeBox.playWithRecommendedVolume("bossspiralballrelease");
			}
		}
		else if(currentAnimation == RELEASING) {
			currentAnimation = FLYING;
		}
		else if(currentAnimation == FLYING) {
			// nothing here
		}
	}
	
	private boolean isPlayerOutsideGrowDistance() {
		double distanceX = playerX - x;
		double distanceY = playerY - y;
		return Math.hypot(distanceX, distanceY) > growBreakDistance;
	}

	public void update() {
		if(currentAnimation == GROWING) {
			updateGrowTime();
			grow();
			
		}
		// update position
		getNextPosition();
		checkTileMapCollision();
		checkOutOfBounds();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
		
		if(currentAnimation == GROWING) {
			Color orig = g.getColor();
			g.setColor(Color.WHITE);
			g.drawOval(
				(int)xmap + (int)x - growBreakDistance,
				(int)ymap + (int)y - growBreakDistance,
				growBreakDistance * 2,
				growBreakDistance * 2
			);
			g.setColor(orig);
		}
	}

	private void checkOutOfBounds() {
		if(x < -50 - width / 2) dead = true;
		if(x > tileMap.getWidth() + 50 + width / 2) dead = true;
		if(y < -50 - height / 2) dead = true;
		if(y > tileMap.getHeight() + 50 + height / 2) dead = true;
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
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
}









