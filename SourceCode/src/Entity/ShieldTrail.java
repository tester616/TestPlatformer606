package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class ShieldTrail extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;

	private float currentOpacity;

	private float opacityMin;
	private float opacityMax;
	
	public ShieldTrail(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		health = maxHealth = 1;
		damage = 0;

		opacityMin = Support.INVISIBLEOPACITY;
		opacityMax = Support.NORMALOPACITY;
		opacityMax = (float) 0.6;
		currentOpacity = opacityMax;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.Shield[0];
		
		spritesM = Content.ShieldM[0];
		
		//spritesM = Content.convertToGrayScale(spritesC);
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		lifeTime = 200;
		creationTime = System.nanoTime();
	}
	
	private void getNextPosition() {
		
	}
	
	private void checkLifeTime() {
		
		long lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
		
		currentOpacity = (float) (opacityMax + (opacityMin - opacityMax) * ((double) lifeTimePassed / (double) lifeTime));
		if(currentOpacity < 0.0) {
			currentOpacity = Support.INVISIBLEOPACITY;
		}
		
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
		
		currentOpacity *= Support.surroundingsOpacity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentOpacity));
		
		super.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
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









