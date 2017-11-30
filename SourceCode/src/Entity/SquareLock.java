package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class SquareLock extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;

	private float originalOpacity;
	private float currentOpacity;

	private double fadeStartDecimal;

	private long lifeTimePassed;
	
	public SquareLock(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		width = 111;
		height = 111;
		cwidth = 111;
		cheight = 111;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.SquareLock[0];
		
		spritesM = Content.SquareLockM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		fadeStartDecimal = 0.8;
		currentOpacity = Support.NORMALOPACITY;
		originalOpacity = Support.NORMALOPACITY;
		
		lifeTime = 600;
		creationTime = System.nanoTime();
	}
	
	@Override
	public void hit(int damage) {
		
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
	
	private void setFadeableValues() {
		double lifeTimePassedOfTotalInDecimal = (double) lifeTimePassed / (double) lifeTime;
		double lifeTimeLeftOfTotalDecimal = 1.0 - lifeTimePassedOfTotalInDecimal;
		
		if(lifeTimeLeftOfTotalDecimal <= fadeStartDecimal) {
			double lifeTimePassedAtFadeStartDecimal = 1.0 - fadeStartDecimal;

			long lifeTimeLastPart = (long) (lifeTime * fadeStartDecimal);
			long lifeTimePassedOfLastPart = (long) (lifeTimePassed - (lifeTime * lifeTimePassedAtFadeStartDecimal));
			
			double lifeTimePassedOfLastPartInDecimal = (double) lifeTimePassedOfLastPart / (double) lifeTimeLastPart;
			float fadedOpacity = (float) (originalOpacity - (originalOpacity * lifeTimePassedOfLastPartInDecimal));
			if(fadedOpacity < 0.0) fadedOpacity = (float) 0.0;
			
			currentOpacity = fadedOpacity;
		}
		else {
			currentOpacity = originalOpacity;
		}
	}
	
	public void update() {
		
		setLifeTimePassed();
		
		checkLifeTime();
		
		setFadeableValues();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		float drawOpacity = currentOpacity * Support.surroundingsOpacity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawOpacity));
		
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









