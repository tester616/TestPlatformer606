package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall4SplitExtra;
import Handlers.Content;
import Main.GamePanel;

// chases until it gets close enough and splits into two balls that go in -90 and 90 degree directions and split again after a while until out of total splits
public class BossBall4Split extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;
	
	// travel values saved since dx & dy go to 0 while idling before a split
	private double dxTravel;
	private double dyTravel;
	
	// x and y speeds combined in length
	private double totalSpeedVector;
	
	// how accurately it chases the player, -1 is no homing, 0 is a neutral homing and 1 is perfect homing, values under -1 and over 1 are treated as 0
	private double homingStrength;
	
	private int splitsLeft;
	
	private int currentAnimation;
	
	private final int IDLE = 0;
	private final int INITIALLAUNCHTRAVEL = 1;
	private final int CHASE = 2;
	private final int SPLITTRAVEL = 3;
	
	private final int MODE_INITIAL = 0;
	private final int MODE_SPLITTED = 1;
	
	private long idleTime;
	private long idleStart;
	
	private long initialLaunchTravelTime;
	private long initialLaunchTravelStart;
	
	private long splitTravelTime;
	// timers might give minor differences in amount of frames, so a precise amount is used
	//private long splitTravelStart;
	private int splitTravelTotalFrames;
	private int splitTravelCurrentFrame;
	
	private boolean shouldSplit;
	
	public BossBall4Split(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 2.5;
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 9;

		idleTime = 400;
		initialLaunchTravelTime = 700;
		splitTravelTime = 400;
		splitTravelTotalFrames = (int) (((double) splitTravelTime / 1000) * GamePanel.TARGETFPS);
		splitTravelCurrentFrame = 0;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.BossBall4Split[0];
		
		spritesM = Content.BossBall4SplitM[0];
		
		setBufferedImages();
		
		// animation is set in setExtraData
		
		speedMultiplier = 1.0;
		homingStrength = -0.9993;
		
		// extra data contains splits left
		setExtraData(mode, extraData);
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
		else if(currentAnimation == INITIALLAUNCHTRAVEL) {
			// nothing here, just neutral flying
		}
		else if(currentAnimation == SPLITTRAVEL) {
			dx = dxTravel;
			dy = dyTravel;
		}
		else if(currentAnimation == IDLE) {
			dx = 0;
			dy = 0;
		}
		else System.out.println("Wrong currentAnimation " + currentAnimation + " in BossBall4Split");
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - idleStart) / 1000000;
			
			if(passedTime >= idleTime) {
				if(splitsLeft > 0) {
					shouldSplit = true;
					splitsLeft--;
				}
				dead = true;
			}
		}
		else if(currentAnimation == INITIALLAUNCHTRAVEL) {
			long passedTime = (System.nanoTime() - initialLaunchTravelStart) / 1000000;
			
			if(passedTime >= initialLaunchTravelTime) {
				currentAnimation = CHASE;
			}
		}
		else if(currentAnimation == CHASE) {
			
			if(getDistanceToPlayer() < 80) {
				if(splitsLeft > 0) {
					shouldSplit = true;
					splitsLeft--;
				}
				dxTravel = dx;
				dyTravel = dy;
				dead = true;
			}
		}
		else if(currentAnimation == SPLITTRAVEL) {
			splitTravelCurrentFrame++;
			if(splitTravelCurrentFrame > splitTravelTotalFrames) {
				currentAnimation = IDLE;
				idleStart = System.nanoTime();
			}
		}
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
	
	private int getDistanceToPlayer() {
		return (int) Math.abs(Math.hypot(playerX - x, playerY - y));
	}
	
	private void reportWrongMode() {
		System.out.println("Wrong mode " + mode + " in BossBall4.");
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public void checkTileMapCollision() {
		
		double decimalCurrCol = x / tileSize;
		double decimalCurrRow = y / tileSize;
		
		if(decimalCurrCol <= 0) currCol = (int) decimalCurrCol - 1;
		else currCol = (int) decimalCurrCol;
		if(decimalCurrRow <= 0) currRow = (int) decimalCurrRow - 1;
		else currRow = (int) decimalCurrRow;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		calculateCorners(x, ydest);
		
		if(dy < 0) {
			if(topLeft || topRight) {
				dead = true;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dead = true;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dead = true;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dead = true;
			}
			else {
				xtemp += dx;
			}
		}
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(shouldSplit) {
			double split1dxTravel;
			double split1dyTravel;
			double split2dxTravel;
			double split2dyTravel;
			double dir;
			double changedDir;
			if(dxTravel == 0) {
				split1dxTravel = dyTravel;
				split1dyTravel = 0;
				split2dxTravel = dyTravel * (-1);
				split2dyTravel = 0;
			}
			else if(dyTravel == 0) {
				split1dxTravel = 0;
				split1dyTravel = dxTravel;
				split2dxTravel = 0;
				split2dyTravel = dxTravel * (-1);
			}
			else {
				dir = Support.getMovementDirection(dxTravel, dyTravel);
				changedDir = (1 / dir) * (-1);
				split1dxTravel = dyTravel;
				split1dyTravel = dxTravel;
				split2dxTravel = split1dxTravel;
				split2dyTravel = split1dyTravel;
				// if changedDir is positive, one answer is ++ and reverse -- and if not, one answer is +- and reverse -+
				if(changedDir > 0) {
					split1dxTravel = Math.abs(split1dxTravel);
					split1dyTravel = Math.abs(split1dyTravel);
					split2dxTravel = Math.abs(split2dxTravel) * (-1);
					split2dyTravel = Math.abs(split2dyTravel) * (-1);
				}
				else if(changedDir < 0) {
					split1dxTravel = Math.abs(split1dxTravel);
					split1dyTravel = Math.abs(split1dyTravel) * (-1);
					split2dxTravel = Math.abs(split2dxTravel) * (-1);
					split2dyTravel = Math.abs(split2dyTravel);
				}
				else System.out.println("Error in BossBall4Split, changedDir was " + changedDir);
			}

			BossBall4SplitExtra ed = new BossBall4SplitExtra(split1dxTravel, split1dyTravel, splitsLeft, animation);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall4Split(tileMap, MODE_SPLITTED, ed),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			BossBall4SplitExtra ed2 = new BossBall4SplitExtra(split2dxTravel, split2dyTravel, splitsLeft, animation);
			EntitySpawnData esd2 = new EntitySpawnData(
				new BossBall4Split(tileMap, MODE_SPLITTED, ed2),
				getx(),
				gety()
			);
			esdList.add(esd2);
			
			shouldSplit = false;
		}
		
		return esdList;
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
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		
		BossBall4SplitExtra ed = (BossBall4SplitExtra) extraData;
		
		if(mode == MODE_INITIAL) {
			dx = ed.dx;
			dy = ed.dy;
			currentAnimation = INITIALLAUNCHTRAVEL;
			initialLaunchTravelStart = System.nanoTime();
		}
		else if(mode == MODE_SPLITTED) {
			dxTravel = ed.dx;
			dyTravel = ed.dy;
			currentAnimation = SPLITTRAVEL;
		}
		else reportWrongMode();
		
		splitsLeft = ed.splitsLeft;
		
		if(ed.animation != null) {
			animation = ed.animation;
		}
		else {
			animation = new Animation();
			animation.setFrames(sprites);
			animation.setDelay(380);
		}
	}
}









