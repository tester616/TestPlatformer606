package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class BossBall1Particle extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private int currentAnimation;
	
	private final int FLYING = 0;
	private final int TURNING = 1;

	private long flyTime;
	private long flyStart;

	private int animDelayMin;
	private int animDelayMax;
	
	private long lifeTime;
	private long creationTime;
	
	private double knockbackForce;
	
	public BossBall1Particle(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 10.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 0;
		
		animDelayMin = 40;
		animDelayMax = 70;
		
		if(Support.randInt(0, 1) == 0) knockbackForce = -0.6;
		else knockbackForce = 0.6;
		
		flyTime = Support.randInt(200, 500);
		
		lifeTime = 1200;
		creationTime = System.nanoTime();
		
		currentAnimation = TURNING;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.BossBall1[1];
		
		spritesM = Content.BossBall1M[1];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(Support.randInt(animDelayMin, animDelayMax));
	}
	
	public double getKnockbackForce() {
		return knockbackForce;
	}

	private void getNextPosition() {
		if(currentAnimation == FLYING) {
			// just flying neutrally
		}
		else if(currentAnimation == TURNING) {
			dx = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 300) + 200.0, 2);
			if(Support.randInt(0, 1) == 0) dx *= -1;
			dy = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 300) + 200.0, 2);
			if(Support.randInt(0, 1) == 0) dy *= -1;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FLYING) {
			long passedTime = (System.nanoTime() - flyStart) / 1000000;
			
			if(passedTime >= flyTime) {
				currentAnimation = TURNING;
			}
		}
		else if(currentAnimation == TURNING) {
			currentAnimation = FLYING;
			flyStart = System.nanoTime();
		}
	}
	
	private void checkLifeTime() {
		long lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
		
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}
	
	public void update() {
		checkLifeTime();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
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









