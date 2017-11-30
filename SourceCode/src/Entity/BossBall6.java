package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall6Extra;
import EntityExtraData.BossBall6ParticleExtra;
import Handlers.Content;

public class BossBall6 extends Enemy {

	private BufferedImage[] chaseSprites;

	private BufferedImage[] chaseSpritesC;

	private BufferedImage[] chaseSpritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;
	
	// x and y speeds combined in length
	private double totalSpeedVector;
	
	// how accurately it chases the player, -1 is no homing, 0 is a neutral homing and 1 is perfect homing, values under -1 and over 1 are treated as 0
	private double homingStrength;
	
	private long stardustInterval;
	private long lastStardustCreationTime;
	
	private int currentAnimation;
	
	private static final int CHASE = 0;
	private static final int EXPLOSIONCHARGE = 1;
	private static final int EXPLOSION = 2;

	private long chaseStart;
	private long chaseTime;
	private int chaseTimeMin;
	private int chaseTimeMax;

	private long explosionChargeStart;
	private long explosionChargeTime;
	private long explosionGrowTime;
	
	// comparison values to determine if particle is created, acceleration doesn't create, deceleration does
	private double dxPrevious;
	private double dyPrevious;

	private boolean spawnMovingParticle;
	private boolean spawnStillParticles;

	private long movingParticleLifeTime;
	private long stillParticleLifeTime;

	private int circleX;
	private int circleY;
	private double circleCurrentRadius;
	private double circleMinRadius;
	private double circleMaxRadius;
	private double circleCurrentRadians;
	private double circleCurrentRadiansSpeed;
	private double circleMinRadiansSpeed;
	private double circleMaxRadiansSpeed;

	
	public BossBall6(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		maxSpeed = 6.2;
		
		dxPrevious = 0.0;
		dyPrevious = 0.0;
		
		width = 61;
		height = 61;
		cwidth = 45;
		cheight = 45;
		
		health = maxHealth = 1;
		damage = 8;
		
		movingParticleLifeTime = 1000;
		stillParticleLifeTime = 60;
		
		circleMinRadius = 1.0;
		circleMaxRadius = 50.0;
		circleCurrentRadius = circleMinRadius;
		circleMinRadiansSpeed = 0.06;
		circleMaxRadiansSpeed = 0.30;
		circleCurrentRadiansSpeed = circleMinRadiansSpeed;

		explosionChargeTime = 1600;
		explosionGrowTime = (long) (explosionChargeTime * 0.6);
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		chaseSpritesC = Content.Invisible[0];
		
		chaseSpritesM = Content.Invisible[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(chaseSprites);
		animation.setDelay(-1);
		
		currentAnimation = CHASE;
		
		chaseTimeMin = 4000;
		chaseTimeMax = 7000;
		
		setChasing();
		
		speedMultiplier = 1.0;
	}
	
	private void getNextPosition() {
		if(currentAnimation == CHASE) {
			// distance between the player and the star indicates how strongly it's speed changes towards the player's position
			double xDifference = playerX - x;
			double yDifference = playerY - y;
			
			// current speed interferes with homing and is lowered if homingStrength is over 0 all the way to 1, where it doesn't have any influence on the new speed
			if(homingStrength > 0.0 && homingStrength <= 1.0) {
				dx = dx * (1 - Math.abs(homingStrength));
				dy = dy * (1 - Math.abs(homingStrength));
			}
			
			// similar to the code above, but now it affects the x and y speed given by distance between player and enemy which contribute to the homing effect
			if(homingStrength < 0.0 && homingStrength >= -1.0) {
				xDifference = xDifference * (1 - Math.abs(homingStrength));
				yDifference = yDifference * (1 - Math.abs(homingStrength));
			}
			
			// new speed is the current speed + difference in x or y distance, taking into account speedMultiplier
			dx = (dx + xDifference) * speedMultiplier;
			dy = (dy + yDifference) * speedMultiplier;
			
			// finally adjust to maxSpeed
			totalSpeedVector = Math.hypot(dx, dy);
			if(totalSpeedVector > maxSpeed) {
				double totalSpeedInDecimal = totalSpeedVector / maxSpeed;
				 dx = dx / totalSpeedInDecimal;
				 dy = dy / totalSpeedInDecimal;
			}
		}
		else if(currentAnimation == EXPLOSIONCHARGE) {
			dx = 0.0;
			dy = 0.0;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == CHASE) {
			long passedTime = (System.nanoTime() - chaseStart) / 1000000;
			
			if(passedTime >= chaseTime) {
				currentAnimation = EXPLOSIONCHARGE;
				explosionChargeStart = System.nanoTime();
			}
			else {
				// speed comparison must happen before previous speeds are updated
				compareSpeed();
				
				// updates previous speeds after comparison
				updatePreviousSpeeds();
			}
		}
		else if(currentAnimation == EXPLOSIONCHARGE) {
			long passedTime = (System.nanoTime() - explosionChargeStart) / 1000000;
			
			if(passedTime >= explosionChargeTime) {
				currentAnimation = EXPLOSION;
			}
			else {
				updateCircleValues(passedTime);
				spawnStillParticles = true;
			}
		}
		else if(currentAnimation == EXPLOSION) {
			dead = true;
		}
	}
	
	private void updateCircleValues(long passedTime) {
		ArrayList<Double> circleXY = new ArrayList<Double>();
		
		double percentDoneGrowingDecimal = (double) passedTime / (double) explosionGrowTime;
		if(percentDoneGrowingDecimal > 1) percentDoneGrowingDecimal = 1;
		double percentDoneExplosionChargingDecimal = (double) passedTime / (double) explosionChargeTime;
		if(percentDoneExplosionChargingDecimal > 1) percentDoneExplosionChargingDecimal = 1;
		
		circleCurrentRadius = circleMinRadius + ((circleMaxRadius - circleMinRadius) * percentDoneGrowingDecimal);
		circleCurrentRadiansSpeed = circleMinRadiansSpeed + ((circleMaxRadiansSpeed - circleMinRadiansSpeed) * percentDoneExplosionChargingDecimal);
		
		circleCurrentRadians += circleCurrentRadiansSpeed;
		if(circleCurrentRadians > 2 * Math.PI) circleCurrentRadians -= 2 * Math.PI;
		
		circleXY = Support.getCircleXYPos(circleCurrentRadius, circleCurrentRadians);
		double cX = circleXY.get(0);
		double cY = circleXY.get(1);
		circleX = (int) cX;
		circleY = (int) cY;
	}

	private void setChasing() {
		currentAnimation = CHASE;
		setChaseTime();
		chaseStart = System.nanoTime();
	}
	
	private void setChaseTime() {
		chaseTime = Long.valueOf(Support.randInt(
				chaseTimeMin,
				chaseTimeMax
		));
	}
	
	public boolean stardustIntervalHasElapsed() {
		long elapsed = (System.nanoTime() - lastStardustCreationTime) / 1000000;
		if(elapsed > stardustInterval) {
			lastStardustCreationTime = System.nanoTime();
			return true;
		}
		return false;
	}

	private void compareSpeed() {
		// create particle only if decelerating
		if(Math.hypot(dx, dy) < Math.hypot(dxPrevious, dyPrevious)) {
			spawnMovingParticle = true;
		}
	}

	private void updatePreviousSpeeds() {
		dxPrevious = dx;
		dyPrevious = dy;
	}
	
	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(spawnMovingParticle) {
			BossBall6ParticleExtra ed = new BossBall6ParticleExtra(dx / 2.5, dy / 2.5, movingParticleLifeTime);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall6Particle(tileMap, 0, ed),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			spawnMovingParticle = false;
		}
		
		if(spawnStillParticles) {
			BossBall6ParticleExtra ed = new BossBall6ParticleExtra(0, 0, stillParticleLifeTime);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall6Particle(tileMap, 0, ed),
				getx() + circleX,
				gety() + circleY
			);
			esdList.add(esd);
			
			EntitySpawnData esd2 = new EntitySpawnData(
				new BossBall6Particle(tileMap, 0, ed),
				getx() - circleX,
				gety() - circleY
			);
			esdList.add(esd2);
			
			spawnStillParticles = false;
		}
		
		if(dead) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall6Explosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
		}
		
		return esdList;
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
			chaseSprites = chaseSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			chaseSprites = chaseSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		animation.swapFrames(chaseSprites);
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		BossBall6Extra ed = (BossBall6Extra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		homingStrength = ed.homingStrength;
	}
}









