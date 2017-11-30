package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Star extends Enemy {

	private BufferedImage[] chaseSprites;
	private BufferedImage[] teleportSprites;

	private BufferedImage[] chaseSpritesC;
	private BufferedImage[] teleportSpritesC;

	private BufferedImage[] chaseSpritesM;
	private BufferedImage[] teleportSpritesM;
	
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
	private static final int TELEPORT = 1;
	private static final int TELEPORTIDLE = 2;

	private long chaseStart;
	private long chaseTime;
	private int chaseTimeMin;
	private int chaseTimeMax;

	private long teleportIdleStart;
	private long teleportIdleTime;
	private int teleportIdleTimeMin;
	private int teleportIdleTimeMax;
	
	private Point teleportPoint;
	
	public Star(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 5.3;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		health = maxHealth = 2;
		damage = 14;
		
		cost = Support.COST_STAR;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		chaseSpritesC = Content.Star[0];
		teleportSpritesC = Content.Invisible[0];
		
		chaseSpritesM = Content.StarM[0];
		teleportSpritesM = Content.Invisible[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(teleportSprites);
		animation.setDelay(35);
		
		setTeleportIdling();
		
		chaseTimeMin = 1000;
		chaseTimeMax = 10000;
		teleportIdleTimeMin = 3000;
		teleportIdleTimeMax = 5000;
		
		teleportPoint = new Point(0, 0);
		
		speedMultiplier = 1.0;
		homingStrength = -0.9995;
		
		stardustInterval = 50;
		lastStardustCreationTime = System.nanoTime();
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
		else if(currentAnimation == TELEPORT) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == TELEPORTIDLE) {
			dx = 0;
			dy = 0;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == CHASE) {
			long passedTime = (System.nanoTime() - chaseStart) / 1000000;
			
			if(passedTime >= chaseTime) {
				setTeleporting();
			}
		}
		else if(currentAnimation == TELEPORT) {
			if(teleportPoint.x == 0 && teleportPoint.y == 0) {
				// teleport point hasn't been found, searching again
				teleportPoint = getSuitableRandomSpawnPoint(true, true);
			}
			else {
				x = teleportPoint.x;
				y = teleportPoint.y;
				setTeleportIdling();
			}
		}
		else if(currentAnimation == TELEPORTIDLE) {
			long passedTime = (System.nanoTime() - teleportIdleStart) / 1000000;
			
			if(passedTime >= teleportIdleTime) {
				setChasing();
			}
		}
	}
	
	private void setChasing() {
		currentAnimation = CHASE;
		animation.setFrames(chaseSprites);
		animation.setDelay(35);
		pacified = false;
		invulnerable = false;
		setChaseTime();
		chaseStart = System.nanoTime();
		//JukeBox.playWithRecommendedVolume("starappear");
	}
	
	private void setTeleporting() {
		currentAnimation = TELEPORT;
		animation.setFrames(teleportSprites);
		animation.setDelay(-1);
		pacified = true;
		invulnerable = true;
		teleportPoint = getSuitableRandomSpawnPoint(true, true);
		dx = 0;
		dy = 0;
	}
	
	private void setTeleportIdling() {
		currentAnimation = TELEPORTIDLE;
		animation.setFrames(teleportSprites);
		animation.setDelay(-1);
		pacified = true;
		invulnerable = true;
		setTeleportIdleTime();
		teleportIdleStart = System.nanoTime();
		dx = 0;
		dy = 0;
	}
	
	private void setChaseTime() {
		chaseTime = Long.valueOf(Support.randInt(
				chaseTimeMin,
				chaseTimeMax
		));
	}

	private void setTeleportIdleTime() {
		teleportIdleTime = Long.valueOf(Support.randInt(
				teleportIdleTimeMin,
				teleportIdleTimeMax
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
		
		// actually not necessary since BufferedImage automatically ignores rendering outside of it
		// method also doesn't work reliably
		//if(notOnScreen()) return;
		
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
		
		if(dead) {
			EntitySpawnData esd = new EntitySpawnData(
				new Explosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
		}
		
		if(stardustIntervalHasElapsed()) {
			EntitySpawnData esd = new EntitySpawnData(
				new Stardust(tileMap, 0, null),
				getx() + Support.randInt(-3, 3),
				gety() + Support.randInt(-3, 3)
			);
			esdList.add(esd);
		}
		
		return esdList;
	}
	
	@Override
	public void hit(int damage) {
		super.hit(damage);
		if(!dead) setTeleporting();
	}
	
	@Override
	public void checkTileMapCollision() {
		
		double decimalCurrCol = x / tileSize;
		double decimalCurrRow = y / tileSize;
		
		// (int) conversion drops decimals, making both -0.99 and 0.99 to 0, so the -1 row correction must happen while the values have decimals
		if(decimalCurrCol <= 0) currCol = (int) decimalCurrCol - 1;
		else currCol = (int) decimalCurrCol;
		if(decimalCurrRow <= 0) currRow = (int) decimalCurrRow - 1;
		else currRow = (int) decimalCurrRow;
		
		xdest = x + dx;
		ydest = y + dy;
		//System.out.println("xtemp at beginning "+xtemp);
		xtemp = x;
		//System.out.println("xtemp at beginning 2 (took x value) "+xtemp);
		ytemp = y;
		
		// wall touch resets
		touchingLeftWall = false;
		touchingRightWall = false;
		
		calculateCorners(x, ydest);
		//System.out.println(topLeft+" "+topRight+" "+bottomLeft+" "+bottomRight);
		//System.out.println("omfg "+currRow+" "+currCol);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy *= -0.6;
				ytemp = currRow * tileSize + cheight / 2;
				//System.out.println("top collision");
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				//System.out.println(y+" "+dy+" "+(y + dy));
				dy *= -0.6;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
				//System.out.println("bottom collision");
				//System.out.println(y+" "+ytemp);
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx *= -0.6;
				xtemp = currCol * tileSize + cwidth / 2;
				touchingLeftWall = true;
				//System.out.println("left collision");
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx *= -0.6;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
				
				//System.out.println("formula currCol "+currCol+" tileSize "+tileSize+" and cwidth "+cwidth);
				
				touchingRightWall = true;
				//System.out.println("right top n bottom "+topRight+" "+bottomRight+" and xtemp "+xtemp);
				//System.out.println("right collision");
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
			chaseSprites = chaseSpritesC;
			teleportSprites = teleportSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			chaseSprites = chaseSpritesM;
			teleportSprites = teleportSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == CHASE) {
			animation.swapFrames(chaseSprites);
		}
		else if(currentAnimation == TELEPORT) {
			animation.swapFrames(teleportSprites);
		}
	}
}









