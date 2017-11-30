package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Sentry extends Enemy {

	private BufferedImage[] spinSprites;

	private BufferedImage[] spinSpritesC;

	private BufferedImage[] spinSpritesM;
	
	// how frequently a gem is spawned while spinning up
	private long gemInterval;
	// possible values for gemInterval range between these
	private long gemIntervalMin;
	private long gemIntervalMax;
	private long lastGemIntervalChangeTime;
	// how long it takes to shorten gemInterval, comparable role to spinupSpeed
	private long gemIntervalSpinupSpeed;
	private long lastGemCreationTime;
	
	// how long a frame of the spinning animation lasts
	private long spinFrameTime;
	// possible values for spinupFrameTime range between these
	private long spinMinFrameTime;
	private long spinMaxFrameTime;
	private long lastFrameTimeChangeTime;
	
	// how long it takes to increase spinup to the next level, in other words how long a spinup takes
	private long spinupSpeed;
	// same for spindown
	private long spindownSpeed;
	// how many times it has spun up in its current position of the map
	private int timesSpunUp;
	// how many till teleport
	private int spinupTotalBeforeTeleport;
	// spinupTotalBeforeTeleport is a random value between these
	private int spinupTotalBeforeTeleportMin;
	private int spinupTotalBeforeTeleportMax;
	
	private long idleSpinStart;
	private long idleSpinTime;
	private int idleSpinTimeMin;
	private int idleSpinTimeMax;
	
	private int greenGemsToSpawn;
	private int redGemsToSpawn;
	
	private int currentAnimation;
	
	private static final int IDLESPIN = 0;
	private static final int SPINUP = 1;
	private static final int SPINDOWN = 2;
	private static final int TELEPORT = 3;
	
	private Point teleportPoint;

	private double spinupFireThreshold;

	private boolean lastGemIntervalChangeTimeIsSet;
	private boolean lastGemCreationTimeIsSet;
	
	
	public Sentry(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 5.5;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		facingRight = true;
		
		health = maxHealth = 1;
		damage = 0;
		
		cost = Support.COST_SENTRY;

		gemIntervalMin = 1;
		gemIntervalMax = 100;
		gemInterval = gemIntervalMax;
		spinMinFrameTime = 1;
		spinMaxFrameTime = 60;
		spinFrameTime = spinMaxFrameTime;
		spinupSpeed = 50;
		spindownSpeed = (long) (spinupSpeed * 1.25);
		timesSpunUp = 0;
		spinupFireThreshold = 0.5;
		spinupTotalBeforeTeleportMin = 1;
		spinupTotalBeforeTeleportMax = 5;
		setSpinupTotalBeforeTeleport();
		setGemIntervalSpinupSpeed();
		
		greenGemsToSpawn = 0;
		redGemsToSpawn = 0;
		
		pacified = true;
		invulnerable = false;
		
		updateColorMode();
		
		spinSpritesC = Content.Sentry[0];
		
		spinSpritesM = Content.SentryM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(spinSprites);
		animation.setDelay(spinMaxFrameTime);
		
		currentAnimation = IDLESPIN;
		idleSpinStart = System.nanoTime();
		idleSpinTimeMin = 2500;
		idleSpinTimeMax = 6500;
		setIdleSpinTime();
		
		teleportPoint = new Point(0, 0);
		
		setExtraData(mode, extraData);
	}

	private void getNextPosition() {
		if(currentAnimation == IDLESPIN) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == TELEPORT) {
			dx = 0;
			dy = 0;
			if (!(teleportPoint.x == 0 && teleportPoint.y == 0)) {
				x = teleportPoint.x;
				y = teleportPoint.y;
			}
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLESPIN) {
			long passedTime = (System.nanoTime() - idleSpinStart) / 1000000;
			
			if(passedTime >= idleSpinTime) {
				currentAnimation = SPINUP;
				lastFrameTimeChangeTime = System.nanoTime();
				gemInterval = gemIntervalMax;
				lastGemIntervalChangeTimeIsSet = false;
				lastGemCreationTimeIsSet = false;
			}
		}
		else if(currentAnimation == SPINUP) {
			spinup();
			
			if(animation.getDelay() <= spinMinFrameTime) {
				if(timesSpunUp < spinupTotalBeforeTeleport) {
					currentAnimation = SPINDOWN;
					timesSpunUp++;
				}
				else {
					currentAnimation = TELEPORT;
					teleportPoint = getSuitableRandomSpawnPoint(true, true);
					timesSpunUp = 0;
					setSpinupTotalBeforeTeleport();
				}
			}
		}
		else if(currentAnimation == SPINDOWN) {
			spindown();
			
			if(animation.getDelay() >= spinMaxFrameTime) {
				currentAnimation = IDLESPIN;
				setIdleSpinTime();
				idleSpinStart = System.nanoTime();
			}
		}
		else if(currentAnimation == TELEPORT) {
			if(teleportPoint.x == 0 && teleportPoint.y == 0) {
				// teleport point hasn't been found, searching again
				teleportPoint = getSuitableRandomSpawnPoint(true, true);
			}
			else {
				currentAnimation = SPINDOWN;
				lastFrameTimeChangeTime = System.nanoTime();
			}
		}
	}
	
	private void spinup() {
		double spinupFramesPassed = Math.abs(spinMaxFrameTime - spinFrameTime);
		double spinupFrameAmount = spinMaxFrameTime - spinMinFrameTime;
		double spinupAmountPassedDecimal = (double) spinupFramesPassed / (double) spinupFrameAmount;
		
		// minus since frame delay shortens while spinning up
		spinFrameTime = animation.getDelay() - getFrameTimeChange(spinupSpeed);
		if(spinFrameTime < spinMinFrameTime) spinFrameTime = spinMinFrameTime;

		// fasten the framerate over time
		animation.setDelay(spinFrameTime);

		// start shooting gems once a certain % of the total spinup time has passed
		if(spinupAmountPassedDecimal >= spinupFireThreshold) {
			shootGems();
		}
	}
	
	private void shootGems() {
		if(!lastGemIntervalChangeTimeIsSet) {
			lastGemIntervalChangeTime = System.nanoTime();
			lastGemIntervalChangeTimeIsSet = true;
		}
		gemInterval -= getGemIntervalChange(gemIntervalSpinupSpeed);
		if(gemInterval < gemIntervalMin) gemInterval = gemIntervalMin;
		
		if(!lastGemCreationTimeIsSet) {
			lastGemCreationTime = System.nanoTime();
			lastGemCreationTimeIsSet = true;
		}
		long elapsed = (System.nanoTime() - lastGemCreationTime) / 1000000;
		
		while(elapsed >= gemInterval) {
			elapsed -= gemInterval;
			lastGemCreationTime = lastGemCreationTime + gemInterval * 1000000;
			addRandomGem();
		}
	}

	private void addRandomGem() {
		if(Support.randInt(0, 1) == 0) {
			greenGemsToSpawn++;
		}
		else {
			redGemsToSpawn++;
		}
	}

	private void spindown() {
		//double spindownFramesPassed = Math.abs(spinMinFrameTime - spinFrameTime);
		//double spindownFrameAmount = spinMaxFrameTime - spinMinFrameTime;
		//double spindownAmountPassedDecimal = (double) spindownFramesPassed / (double) spindownFrameAmount;
		
		// plus since frame delay lengthens while spinning down
		spinFrameTime = animation.getDelay() + getFrameTimeChange(spindownSpeed);
		if(spinFrameTime > spinMaxFrameTime) spinFrameTime = spinMaxFrameTime;
		
		// slow down the framerate over time
		animation.setDelay(spinFrameTime);
	}
	
	// returns how much the delay of a frame should be changed depending on givem frameTime and last change
	private long getFrameTimeChange(long delayTime) {
		long frameTimeChange = 0;
		if (delayTime < 1) {
			System.out.println("Error in getFrameTimeChange(), delayTime is too low " + delayTime);
			return frameTimeChange;
		}
		long elapsed = (System.nanoTime() - lastFrameTimeChangeTime) / 1000000;
		
		while(elapsed >= delayTime) {
			frameTimeChange++;
			elapsed -= delayTime;
			lastFrameTimeChangeTime = lastFrameTimeChangeTime + (delayTime * 1000000);
		}
		
		return frameTimeChange;
	}
	
	// returns how much the delay of a frame should be changed depending on givem frameTime and last change
	private long getGemIntervalChange(long delayTime) {
		long gemIntervalChange = 0;
		if (delayTime < 1) {
			System.out.println("Error in getGemIntervalChange(), delayTime is too low " + delayTime);
			return gemIntervalChange;
		}
		long elapsed = (System.nanoTime() - lastGemIntervalChangeTime) / 1000000;
		
		while(elapsed >= delayTime) {
			gemIntervalChange++;
			elapsed -= delayTime;
			lastGemIntervalChangeTime = lastGemIntervalChangeTime + (delayTime * 1000000);
		}
		
		return gemIntervalChange;
	}

	private void setIdleSpinTime() {
		idleSpinTime = Long.valueOf(Support.randInt(
				idleSpinTimeMin,
				idleSpinTimeMax
		));
	}
	
	private void setSpinupTotalBeforeTeleport() {
		spinupTotalBeforeTeleport = Support.randInt(
				spinupTotalBeforeTeleportMin,
				spinupTotalBeforeTeleportMax
		);
	}
	
	private void setGemIntervalSpinupSpeed() {
		double totalTimeLeftOfSpinup = (double) (spinupSpeed * (spinMaxFrameTime - spinMinFrameTime)) * (1 - spinupFireThreshold);
		gemIntervalSpinupSpeed = (long) (totalTimeLeftOfSpinup / ((double) gemIntervalMax - (double) gemIntervalMin));
		if(gemIntervalSpinupSpeed < 1) gemIntervalSpinupSpeed = 1;
	}
	
	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		//TODO: something? no?
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(dead) {
			EntitySpawnData esd = new EntitySpawnData(
				new Explosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
		}
		
		while(greenGemsToSpawn > 0) {
			EntitySpawnData esd = new EntitySpawnData(
				new SentryGemGreen(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			greenGemsToSpawn--;
		}
		
		while(redGemsToSpawn > 0) {
			EntitySpawnData esd = new EntitySpawnData(
				new SentryGemRed(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			redGemsToSpawn--;
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			spinSprites = spinSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			spinSprites = spinSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		animation.swapFrames(spinSprites);
	}
}









