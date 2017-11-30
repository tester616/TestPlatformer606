package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class SentryGemGreen extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// max speed in it can roll in x or y direction minus 2 decimals
	private int speedMax;

	private int animDelayMin;
	private int animDelayMax;
	
	private int attackEnergyDamage;
	private int rewindEnergyDamage;
	
	public SentryGemGreen(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 1;
		attackEnergyDamage = 100000;
		rewindEnergyDamage = 40000;
		
		speedMax = 230;
		animDelayMin = 30;
		animDelayMax = 60;
		
		setRandomSpeed();
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.SentryGemGreen[0];
		
		spritesM = Content.SentryGemGreenM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(Support.randInt(animDelayMin, animDelayMax));
	}
	
	private void getNextPosition() {
		
	}
	
	private void setRandomSpeed() {
		dx = Support.getDoubleWithXExtraDecimals(Support.randInt(speedMax * (-1), speedMax), 2);
		dy = Support.getDoubleWithXExtraDecimals(Support.randInt(speedMax * (-1), speedMax), 2);
	}
	
	public void update() {
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		checkOutOfBounds();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
	}

	private void checkOutOfBounds() {
		// just simple checks with minimum math and processing time
		if(x < -50) dead = true;
		if(x > tileMap.getWidth() + 50) dead = true;
		if(y < -50) dead = true;
		if(y > tileMap.getHeight() + 50) dead = true;
	}

	public int getAttackEnergyDamage() {
		return attackEnergyDamage;
	}

	public int getRewindEnergyDamage() {
		return rewindEnergyDamage;
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
				dead = true;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dead = true;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dead = true;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dead = true;
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
}









