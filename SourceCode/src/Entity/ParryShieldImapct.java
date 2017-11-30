package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;

import TileMap.TileMap;
import Handlers.Content;

public class ParryShieldImapct extends Ally {

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

	private double fadeStartDecimal;
	private float currentOpacity;
	
	public ParryShieldImapct(TileMap tm, int mode, Object extraData) {
		
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
		maxScale = 6.0;
		scale = minScale;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.ParryShieldImpact[0];
		
		//spritesM = Content.ParryShieldImpact[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		lifeTime = 150;
		creationTime = System.nanoTime();
		
		fadeStartDecimal = 1.0;
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

	public void setCurrentOpacity(float currentOpacity) {
		this.currentOpacity = currentOpacity;
	}

	private void setScaledValues() {
		scale = minScale + (maxScale - minScale) * ((double) lifeTimePassed / (double) lifeTime);
		width = (int) (originalWidth * scale);
		height = (int) (originalHeight * scale);
		cwidth = (int) (originalCwidth * scale);
		cheight = (int) (originalCheight * scale);
	}
	
	// currentOpacity
	private void setFadeableValues() {
		double lifeTimePassedOfTotalInDecimal = (double) lifeTimePassed / (double) lifeTime;
		double lifeTimeLeftOfTotalDecimal = 1.0 - lifeTimePassedOfTotalInDecimal;
		if(lifeTimeLeftOfTotalDecimal <= fadeStartDecimal) {
			double lifeTimePassedAtFadeStartDecimal = 1.0 - fadeStartDecimal;

			long lifeTimeLastPart = (long) (lifeTime * fadeStartDecimal);
			long lifeTimePassedOfLastPart = (long) (lifeTimePassed - (lifeTime * lifeTimePassedAtFadeStartDecimal));
			
			double lifeTimePassedOfLastPartInDecimal = (double) lifeTimePassedOfLastPart / (double) lifeTimeLastPart;
			float fadedOpacity = (float) (Support.TRANSPARENT - (Support.TRANSPARENT * lifeTimePassedOfLastPartInDecimal));
			if(fadedOpacity < 0.0) fadedOpacity = (float) 0.0;

			setCurrentOpacity(fadedOpacity);
		}
		else {
			setCurrentOpacity(Support.TRANSPARENT);
		}
	}
	
	public void update() {
		
		setLifeTimePassed();
		
		setFadeableValues();
		
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









