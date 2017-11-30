package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class BallRepelShield extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	
	private double knockbackForce;

	private long lifeTimePassed;

	private float currentOpacity;
	private float originalOpacity;

	private double fadeStartDecimal;

	private double baseKnockbackForce;

	
	public BallRepelShield(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 141;
		height = 141;
		cwidth = 120;
		cheight = 120;
		
		baseKnockbackForce = -0.6;
		knockbackForce = baseKnockbackForce;
		
		fadeStartDecimal = 0.5;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.ShieldBall[0];
		
		spritesM = Content.ShieldBallM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);

		originalOpacity = Support.TRANSPARENT;
		currentOpacity = originalOpacity;
		
		lifeTime = 10000;
		creationTime = System.nanoTime();
	}
	
	public double getKnockbackForce() {
		return knockbackForce;
	}
	
	public void setKnockbackForce(double knockbackForce) {
		this.knockbackForce = knockbackForce;
	}

	public void setCurrentOpacity(float currentOpacity) {
		this.currentOpacity = currentOpacity;
	}
	
	private void getNextPosition() {
		dx = 0;
		dy = 0;
	}

	private void setLifeTimePassed() {
		lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
	}
	
	private void checkLifeTime() {
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}

	// currentOpacity & knockbackForce
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
			double fadedKnockbackForce = baseKnockbackForce - (baseKnockbackForce * lifeTimePassedOfLastPartInDecimal);
			// true if fadedKnockbackForce goes beyond 0 and works even for a negative baseKnockbackForce
			if(Math.abs(baseKnockbackForce - fadedKnockbackForce) > Math.abs(baseKnockbackForce)) fadedKnockbackForce = 0.0;
			
			setCurrentOpacity(fadedOpacity);
			setKnockbackForce(fadedKnockbackForce);
		}
		else {
			setCurrentOpacity(originalOpacity);
			setKnockbackForce(baseKnockbackForce);
		}
	}
	
	public void update() {
		
		setLifeTimePassed();
		
		setFadeableValues();
		
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









