package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Shield extends Enemy {

	private BufferedImage[] shieldSprites;

	private BufferedImage[] shieldSpritesC;

	private BufferedImage[] shieldSpritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;
	
	// x and y speeds combined in length
	private double totalSpeedVector;
	
	private int currentAnimation;
	
	private static final int IDLE = 0;
	private static final int REPEL = 1;
	private static final int BALLREPELATTACK = 2;
	private static final int BALLREPELSHIELD = 3;
	private static final int RANDOMMOVEMENT = 4;
	private static final int RANDOMCHASE = 5;
	private static final int RANDOMCHARGE = 6;
	
	/*private boolean teleportTimeSet;

	private long teleportStart;
	private long teleportTime;
	private int teleportTimeMin;
	private int teleportTimeMax;
	
	private Point teleportPoint;*/
	
	private long animationStart;
	private long animationTime;
	
	private int idleTimeMin;
	private int idleTimeMax;
	
	private int movementTimeMin;
	private int movementTimeMax;
	private double xMovementStartSpeed;
	private double xMovementEndSpeed;
	private double yMovementStartSpeed;
	private double yMovementEndSpeed;
	private int movementRollMinSpeed;
	private int movementRollMaxSpeed;
	
	private int chaseTimeMin;
	private int chaseTimeMax;
	
	// how accurately it chases the player, -1 is no homing, 0 is a neutral homing and 1 is perfect homing, values under -1 and over 1 are treated as 0
	private double homingStrength;
	private int homingStrengthMin;
	private int homingStrengthMax;
	
	private int chargeTimeMin;
	private int chargeTimeMax;
	private int chargeSpeedMin;
	private int chargeSpeedMax;
	private ArrayList<Double> dirXY;

	private double ballsAvailable;

	private boolean createRepel;
	private boolean createBallRepelAttack;
	private boolean createBallRepelShield;

	private boolean repelled;

	private int idleTimeAfterRepelMin;
	private int idleTimeAfterRepelMax;

	private long lastShieldTrailCreationTime;

	private long shieldTrailInterval;
	private boolean createShieldTrail;

	private boolean shouldSetChargeXY;

	private double chargeTotalSpeed;

	private double chargeYSpeed;

	private double chargeXSpeed;

	//private ArrayList<BallRepelShield> ballRepelShields;

	//private boolean changeShieldColor;
	
	
	public Shield(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 1.8;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 20;
		
		health = maxHealth = 3;
		damage = 12;
		
		cost = Support.COST_SHIELD;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		shieldSpritesC = Content.Shield[0];
		
		shieldSpritesM = Content.ShieldM[0];
		
		//shieldSpritesM = Content.convertToGrayScale(shieldSpritesC);
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(shieldSprites);
		animation.setDelay(-1);
		
		currentAnimation = IDLE;

		idleTimeMin = 2000;
		idleTimeMax = 2000;

		idleTimeAfterRepelMin = 1400;
		idleTimeAfterRepelMax = 1400;
		
		movementTimeMin = 1000;
		movementTimeMax = 2500;
		
		chaseTimeMin = 2000;
		chaseTimeMax = 5000;
		// 4 decimals added to these later
		homingStrengthMin = -9999;
		homingStrengthMax = -9985;
		
		speedMultiplier = 1.0;
		homingStrength = -0.9995;
		
		chargeTimeMin = 1000;
		chargeTimeMax = 2000;
		// 2 decimals added to these later
		chargeSpeedMin = 100;
		chargeSpeedMax = 300;
		dirXY = new ArrayList<Double>();
		
		movementRollMinSpeed = -300;
		movementRollMaxSpeed = 300;
		
		ballsAvailable = 0.0;
		
		createRepel = false;
		createBallRepelAttack = false;
		createBallRepelShield = false;
		repelled = false;
		
		//teleportPoint = new Point(0, 0);
		
		shieldTrailInterval = 70;
		//lastShieldTrailCreationTime = System.nanoTime();
		
		//ballRepelShields = new ArrayList<BallRepelShield>();
		//changeShieldColor = false;
		
		animationTime = Long.valueOf(Support.randInt(
				idleTimeMin,
				idleTimeMax
		));
		animationStart = System.nanoTime();
	}
	
	private void getNextPosition() {
		if(currentAnimation == IDLE) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == REPEL) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == BALLREPELATTACK) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == BALLREPELSHIELD) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == RANDOMMOVEMENT) {
			long elapsed = (System.nanoTime() - animationStart) / 1000000;
			
			dx = xMovementStartSpeed + (xMovementEndSpeed - xMovementStartSpeed) * ((double) elapsed / (double) animationTime);
			dy = yMovementStartSpeed + (yMovementEndSpeed - yMovementStartSpeed) * ((double) elapsed / (double) animationTime);
		}
		else if(currentAnimation == RANDOMCHASE) {
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
		else if(currentAnimation == RANDOMCHARGE) {
			if(shouldSetChargeXY) {
				// random charge total speed from min to max values divided with 100
				int r = Support.randInt(chargeSpeedMin, chargeSpeedMax);
				chargeTotalSpeed = Support.getDoubleWithXExtraDecimals(r, 2);
				
				// direction x and y values to player
				dirXY = getDirectionToPoint(playerX, playerY);
				
				// gives dx and dy their final values, but in reverse as it tries to knockback
				knockback(dirXY.get(0), dirXY.get(1), chargeTotalSpeed, true, true);
				
				// dx and dy are reversed and saved for ease of access
				chargeXSpeed = dx * (-1);
				chargeYSpeed = dy * (-1);
				
				// make sure this doesn't run again in vain
				shouldSetChargeXY = false;
			}
			
			dx = chargeXSpeed;
			dy = chargeYSpeed;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			if(animationTimeHasElapsed()) {
				// more balls over time
				ballsAvailable += 0.13;
				
				// one kind of repel was used last time, next action is movement
				if(repelled) {
					// movement is chosen from 3 versions with 80-10-10 distribution
					int rand = Support.randInt(1, 10000);
					if(rand <= 8000) {
						currentAnimation = RANDOMMOVEMENT;
						animationTime = Long.valueOf(Support.randInt(
								movementTimeMin,
								movementTimeMax
						));
						animationStart = System.nanoTime();
						xMovementStartSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(movementRollMinSpeed, movementRollMaxSpeed), 2);
						xMovementEndSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(movementRollMinSpeed, movementRollMaxSpeed), 2);
						yMovementStartSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(movementRollMinSpeed, movementRollMaxSpeed), 2);
						yMovementEndSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(movementRollMinSpeed, movementRollMaxSpeed), 2);
					}
					else if(rand >= 8001 && rand <= 9000) {
						currentAnimation = RANDOMCHASE;
						animationTime = Long.valueOf(Support.randInt(
								chaseTimeMin,
								chaseTimeMax
						));
						animationStart = System.nanoTime();
						homingStrength = Support.getDoubleWithXExtraDecimals(Support.randInt(homingStrengthMin, homingStrengthMax), 4);
					}
					else if(rand >= 9001) {
						currentAnimation = RANDOMCHARGE;
						animationTime = Long.valueOf(Support.randInt(
								chargeTimeMin,
								chargeTimeMax
						));
						animationStart = System.nanoTime();
						shouldSetChargeXY = true;
					}
					
					repelled = false;
				}
				// one kind of movement was used last time, next action is repel
				else {
					// repel is chosen from 3 versions with 80-10-10 distribution
					if(ballsAvailable >= 1.0) {
						int rand = Support.randInt(1, 10000);
						if(rand <= 8000) {
							currentAnimation = REPEL;
						}
						else if(rand >= 8001 && rand <= 9000) {
							currentAnimation = BALLREPELATTACK;
							ballsAvailable--;
						}
						else if(rand >= 9001) {
							currentAnimation = BALLREPELSHIELD;
							ballsAvailable--;
						}
					}
					else {
						currentAnimation = REPEL;
					}
					
					repelled = true;
				}
			}
		}
		else if(currentAnimation == REPEL) {
			currentAnimation = IDLE;
			animationTime = Long.valueOf(Support.randInt(
					idleTimeAfterRepelMin,
					idleTimeAfterRepelMax
			));
			animationStart = System.nanoTime();
			createRepel = true;
			//JukeBox.playWithRecommendedVolume("shieldrepel");
		}
		else if(currentAnimation == BALLREPELATTACK) {
			currentAnimation = IDLE;
			animationTime = Long.valueOf(Support.randInt(
					idleTimeMin,
					idleTimeMax
			));
			animationStart = System.nanoTime();
			createBallRepelAttack = true;
		}
		else if(currentAnimation == BALLREPELSHIELD) {
			currentAnimation = IDLE;
			animationTime = Long.valueOf(Support.randInt(
					idleTimeMin,
					idleTimeMax
			));
			animationStart = System.nanoTime();
			createBallRepelShield = true;
		}
		else if(currentAnimation == RANDOMMOVEMENT) {
			createShieldTrail = shieldTrailIntervalHasElapsed();
			if(animationTimeHasElapsed()) {
				currentAnimation = IDLE;
				animationTime = Long.valueOf(Support.randInt(
						idleTimeMin,
						idleTimeMax
				));
				animationStart = System.nanoTime();
			}
		}
		else if(currentAnimation == RANDOMCHASE) {
			createShieldTrail = shieldTrailIntervalHasElapsed();
			if(animationTimeHasElapsed()) {
				currentAnimation = IDLE;
				animationTime = Long.valueOf(Support.randInt(
						idleTimeMin,
						idleTimeMax
				));
				animationStart = System.nanoTime();
			}
		}
		else if(currentAnimation == RANDOMCHARGE) {
			createShieldTrail = shieldTrailIntervalHasElapsed();
			if(animationTimeHasElapsed()) {
				currentAnimation = IDLE;
				animationTime = Long.valueOf(Support.randInt(
						idleTimeMin,
						idleTimeMax
				));
				animationStart = System.nanoTime();
			}
		}
	}
	
	public boolean animationTimeHasElapsed() {
		long elapsed = (System.nanoTime() - animationStart) / 1000000;
		if(elapsed > animationTime) {
			return true;
		}
		return false;
	}
	
	public boolean shieldTrailIntervalHasElapsed() {
		long elapsed = (System.nanoTime() - lastShieldTrailCreationTime) / 1000000;
		if(elapsed > shieldTrailInterval) {
			lastShieldTrailCreationTime = System.nanoTime();
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
			if(elapsed > flinchTime) {
				flinching = false;
			}
		}
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		setMapPosition();
		
		super.draw(g);
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
				dy = 0;
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
				dy = 0;
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
				dx = 0;
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
				dx = 0;
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
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
		
		// out of map bounds prevention
		if(xdest - cwidth / 2 - 1 < 0) {
			dx = 0;
			xtemp = currCol * tileSize + cwidth / 2;
		}
		else if(xdest + cwidth / 2 + 1 > tileMap.getWidth()) {
			dx = 0;
			xtemp = (currCol + 1) * tileSize - cwidth / 2;
		}
		if(ydest - cheight / 2 - 1 < 0) {
			dy = 0;
			ytemp = currRow * tileSize + cheight / 2;
		}
		else if(ydest + cheight / 2 + 1 > tileMap.getHeight()) {
			dy = 0;
			ytemp = (currRow + 1) * tileSize - cheight / 2;
		}
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
		
		if(createShieldTrail) {
			EntitySpawnData esd = new EntitySpawnData(
				new ShieldTrail(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createShieldTrail = false;
		}
		
		if(createRepel) {
			EntitySpawnData esd = new EntitySpawnData(
				new ShieldRepel(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createRepel = false;
		}
		
		if(createBallRepelAttack) {
			EntitySpawnData esd = new EntitySpawnData(
				new BallRepelAttack(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createBallRepelAttack = false;
		}
		
		if(createBallRepelShield) {
			EntitySpawnData esd = new EntitySpawnData(
				new BallRepelShield(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createBallRepelShield = false;
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			shieldSprites = shieldSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			shieldSprites = shieldSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == IDLE) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == REPEL) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == BALLREPELATTACK) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == BALLREPELSHIELD) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == RANDOMMOVEMENT) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == RANDOMCHASE) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == RANDOMCHARGE) {
			animation.swapFrames(shieldSprites);
		}
		
		//changeShieldColor = true;
	}
}









