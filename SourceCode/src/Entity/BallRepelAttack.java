package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class BallRepelAttack extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private static final int FLYING = 0;
	
	private long lifeTime;
	private long creationTime;
	
	private double playerX;
	private double playerY;
	
	private ArrayList<Double> dirXY;
	
	private double baseKnockbackForce;
	private double knockbackForce;

	private double chargeTotalSpeed;

	private long lifeTimePassed;

	private float currentOpacity;
	private float originalOpacity;

	private int currentAnimation;

	private double chargeXSpeed;
	private double chargeYSpeed;

	private boolean shouldSetChargeXY;

	private double fadeStartDecimal;
	
	public BallRepelAttack(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 141;
		height = 141;
		cwidth = 120;
		cheight = 120;
		
		baseKnockbackForce = 0.7;
		knockbackForce = baseKnockbackForce;
		
		health = maxHealth = 1;
		damage = 0;
		
		chargeTotalSpeed = 3.0;
		
		dirXY = new ArrayList<Double>();
		
		shouldSetChargeXY = true;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.ShieldBall[0];
		
		spritesM = Content.ShieldBallM[0];
		
		//spritesM = Content.convertToGrayScale(spritesC);
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		currentAnimation = FLYING;
		
		fadeStartDecimal = 0.2;
		
		originalOpacity = Support.TRANSPARENT;
		currentOpacity = originalOpacity;
		
		lifeTime = 5000;
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
		if(currentAnimation == FLYING) {
			if(shouldSetChargeXY) {
				// dummy object to use in method that holds player position data
				Enemy playerObject = new Enemy(tileMap, 0, null);
				playerObject.setPosition(playerX, playerY);
				
				// direction x and y values to player
				dirXY = getDirectionToMapObject(playerObject);
				
				// normally knockback, but since this one is invulnerable it uses it's own version
				setXYSpeeds(dirXY.get(0), dirXY.get(1), chargeTotalSpeed);
				
				// dx and dy are reversed and saved for ease of access
				chargeXSpeed = dx * (-1);
				chargeYSpeed = dy * (-1);
				
				shouldSetChargeXY = false;
			}
			
			dx = chargeXSpeed;
			dy = chargeYSpeed;
		}
	}

	private void setLifeTimePassed() {
		lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
	}
	
	private void checkLifeTime() {
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}
	
	// closely resembles knockback method, but runs even while invulnerable
	public void setXYSpeeds(double xDir, double yDir, double knockbackStrength) {
		double x = 0;
		// xDir is 0, no formula needed, all of knockbackStrength is in yDir
		if(xDir == 0 & yDir != 0) {
			if(yDir > 0) dy += knockbackStrength * (-1);
			else if(yDir < 0) dy += knockbackStrength;
		}
		// yDir is 0, no formula needed, all of knockbackStrength is in xDir
		else if(xDir != 0 & yDir == 0) {
			if(xDir > 0) dx += knockbackStrength *(-1);
			else if(xDir < 0) dx += knockbackStrength;
		}
		// xDir & yDir are both 0, objects are centered inside each other, direction is straight up
		else if(xDir == 0 && yDir == 0) {
			dy += knockbackStrength * (-1);
		}
		// normal cases
		else {
			// (xDir * x)^2 + (yDir * x)^2 = knockbackStrength^2 solved for x
			x = Support.pythGetXYMultiplier(xDir, yDir, knockbackStrength);
			
			dx += xDir * x * (-1);
			dy += yDir * x * (-1);
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
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		touchingLeftWall = false;
		touchingRightWall = false;
		
		ytemp += dy;
		xtemp += dx;
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
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









