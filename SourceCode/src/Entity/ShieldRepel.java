package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Support.Support;

import TileMap.TileMap;
import Handlers.Content;

public class ShieldRepel extends Enemy {

	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	private double scale;
	private double minScale;
	private double maxScale;
	private int originalWidth;
	private int originalHeight;
	private int originalCwidth;
	private int originalCheight;
	
	private double knockbackForce;

	private long lifeTimePassed;
	
	public ShieldRepel(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 21;
		cheight = 25;
		
		originalWidth = width;
		originalHeight = height;
		originalCwidth = cwidth;
		originalCheight = cheight;

		minScale = 1.0;
		maxScale = 7.5;
		scale = minScale;
		
		knockbackForce = 11.0;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.Shield[4];
		
		spritesM = Content.ShieldM[4];
		
		//spritesM = Content.convertToGrayScale(spritesC);
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		lifeTime = 250;
		creationTime = System.nanoTime();
	}
	
	public double getKnockbackForce() {
		return knockbackForce;
	}
	
	private void getNextPosition() {
		
	}

	private void setLifeTimePassed() {
		lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
	}
	
	private void checkLifeTime() {
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}

	private void setScaledValues() {
		scale = minScale + (maxScale - minScale) * ((double) lifeTimePassed / (double) lifeTime);
		width = (int) (originalWidth * scale);
		height = (int) (originalHeight * scale);
		cwidth = (int) (originalCwidth * scale);
		cheight = (int) (originalCheight * scale);
	}
	
	public void update() {
		
		setLifeTimePassed();
		
		checkLifeTime();
		
		setScaledValues();
		
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









