package Entity.Enemies;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Entity.Animation;
import Entity.Enemy;
import Entity.EntitySpawnData;
import Entity.Explosion;
import Entity.SquareLock;
import Entity.SquareWall;
import EntityExtraData.SquareWallExtra;
import Handlers.Content;
import Main.GamePanel;

// chases until it gets close enough and splits into two balls that go in -90 and 90 degree directions and split again after a while until out of total splits
public class Square extends Enemy {
	
	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;
	
	// travel values saved since dx & dy go to 0 while idling before a split
	private double dxTravelLast;
	private double dyTravelLast;
	
	private double travelSpeed;
	
	private int currentAnimation;
	private int idleAnimationSpeed;
	private int travelAnimationSpeed;
	
	private final int IDLE = 0;
	private final int TRAVEL = 1;
	private final int TELEPORT = 2;

	private Point teleportPoint;
	
	private final int TRAVELMODE_X = 0;
	private final int TRAVELMODE_Y = 1;
	
	private long idleTime;
	private long idleStart;
	
	private long travelTime;
	private int travelTotalFrames;
	private int travelCurrentFrame;
	
	private int stopPoint1x;
	private int stopPoint1y;
	private int stopPoint2x;
	private int stopPoint2y;
	private int stopPoint3x;
	private int stopPoint3y;
	private int stopPoint4x;
	private int stopPoint4y;
	private int stopPoint5x;
	private int stopPoint5y;
	
	private final int TRAVELDIRECTION_UP = 0;
	private final int TRAVELDIRECTION_DOWN = 1;
	private final int TRAVELDIRECTION_LEFT = 2;
	private final int TRAVELDIRECTION_RIGHT = 3;
	
	private float travelWallOpacity;
	
	private boolean firstUpdate;
	
	private boolean spawnWall;
	private boolean spawnWallPrison;
	private boolean spawnSquareLock;
	
	
	public Square(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		width = 31;
		height = 31;
		cwidth = 21;
		cheight = 21;
		
		facingRight = true;
		
		health = maxHealth = 2;
		damage = 3;
		
		travelSpeed = 2.5;

		idleTime = 730;
		travelTime = 730;
		travelTotalFrames = (int) (((double) travelTime / 1000) * GamePanel.TARGETFPS);
		travelCurrentFrame = 0;
		
		travelWallOpacity = 0.3f;
		
		pacified = false;
		invulnerable = false;
		
		currentAnimation = IDLE;
		idleStart = System.nanoTime();
		idleAnimationSpeed = 115;
		travelAnimationSpeed = 45;
		
		updateColorMode();
		
		spritesC = Content.Square[0];
		
		spritesM = Content.SquareM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(travelAnimationSpeed);
		
		firstUpdate = true;
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - idleStart) / 1000000;
			
			if(passedTime >= idleTime) {
				currentAnimation = TRAVEL;
				travelCurrentFrame = 0;
				setTravelSpeed();
				updateLastTravelSpeeds();
				animation.setDelay(travelAnimationSpeed);
			}
		}
		else if(currentAnimation == TRAVEL) {
			travelCurrentFrame++;
			if(travelCurrentFrame > travelTotalFrames) {
				dx = 0.0;
				dy = 0.0;
				advanceToNextStopPoint();
				saveStopPoint();
				if(hasCreatedWallPrison()) {
					currentAnimation = TELEPORT;
					spawnWallPrison = true;
					spawnSquareLock = true;
					teleportPoint = getSuitableRandomSpawnPoint(false, true);
					JukeBox.playWithRecommendedVolume("squarelock");
				}
				else {
					currentAnimation = IDLE;
					idleStart = System.nanoTime();
					animation.setDelay(idleAnimationSpeed);
					spawnWall = true;
				}
			}
		}
		else if(currentAnimation == TELEPORT) {
			if(teleportPoint.x == 0 && teleportPoint.y == 0) {
				// teleport point hasn't been found, searching again
				teleportPoint = getSuitableRandomSpawnPoint(false, true);
			}
			else {
				x = teleportPoint.x;
				y = teleportPoint.y;
				teleportPoint.x = 0;
				teleportPoint.y = 0;
				resetStopPoints();
				currentAnimation = IDLE;
				idleStart = System.nanoTime();
				animation.setDelay(idleAnimationSpeed);
			}
		}
	}
	
	private boolean hasCreatedWallPrison() {
		return stopPoint1x == stopPoint5x && stopPoint1y == stopPoint5y;
	}
	
	// makes all stop points equal to current position
	private void resetStopPoints() {
		stopPoint5x = (int) x;
		stopPoint5y = (int) y;
		stopPoint4x = stopPoint5x;
		stopPoint4y = stopPoint5y;
		stopPoint3x = stopPoint5x;
		stopPoint3y = stopPoint5y;
		stopPoint2x = stopPoint5x;
		stopPoint2y = stopPoint5y;
		stopPoint1x = stopPoint5x;
		stopPoint1y = stopPoint5y;
	}

	// moves stop points one step further down the line until they are forgotten after last saved point
	private void advanceToNextStopPoint() {
		stopPoint5x = stopPoint4x;
		stopPoint5y = stopPoint4y;
		stopPoint4x = stopPoint3x;
		stopPoint4y = stopPoint3y;
		stopPoint3x = stopPoint2x;
		stopPoint3y = stopPoint2y;
		stopPoint2x = stopPoint1x;
		stopPoint2y = stopPoint1y;
	}
	
	// save current position as the most recent stop position, call normally after first calling advanceToNextStopPoint
	private void saveStopPoint() {
		stopPoint1x = (int) x;
		stopPoint1y = (int) y;
	}

	// sets travel speed to randomly go either 100% in x or y axis, with -/+ direction depending on which one advances closer to the player
	// backtracking to last point is not allowed
	private void setTravelSpeed() {
		HashMap<Integer, Integer> allowedDirections = new HashMap<Integer, Integer>();
		int mapID = 1;
		int finalDirection;
		
		double playerDistanceX = playerX - x;
		double playerDistanceY = playerY - y;
		
		// allowed directions, first any directions that lead the object away from the player are closed off
		boolean xPositive = playerDistanceX > 0;
		boolean xNegative = playerDistanceX < 0;
		boolean yPositive = playerDistanceY > 0;
		boolean yNegative = playerDistanceY < 0;
		
		// if the distance on x or y is 0 both directions end up as false, so both are allowed since neither leads closer to the player
		if(!xPositive && !xNegative) {
			xPositive = true;
			xNegative = true;
		}
		if(!yPositive && !yNegative) {
			yPositive = true;
			yNegative = true;
		}
		
		// one of the four alternatives always leads to backtracking and is closed off (unless of course it's the initial run and those are both 0)
		if(dxTravelLast < 0) xPositive = false;
		if(dxTravelLast > 0) xNegative = false;
		if(dyTravelLast < 0) yPositive = false;
		if(dyTravelLast > 0) yNegative = false;
		
		// allowed directions are added to a hashmap
		if(xPositive) {
			allowedDirections.put(mapID, TRAVELDIRECTION_RIGHT);
			mapID++;
		}
		if(xNegative) {
			allowedDirections.put(mapID, TRAVELDIRECTION_LEFT);
			mapID++;
		}
		if(yPositive) {
			allowedDirections.put(mapID, TRAVELDIRECTION_DOWN);
			mapID++;
		}
		if(yNegative) {
			allowedDirections.put(mapID, TRAVELDIRECTION_UP);
			mapID++;
		}
		
		finalDirection = allowedDirections.get(Support.randInt(1, allowedDirections.size()));
		
		dx = 0.0;
		dy = 0.0;
		
		if(finalDirection == TRAVELDIRECTION_UP) {
			dy = travelSpeed * (-1);
		}
		else if(finalDirection == TRAVELDIRECTION_DOWN) {
			dy = travelSpeed;
		}
		else if(finalDirection == TRAVELDIRECTION_LEFT) {
			dx = travelSpeed * (-1);
		}
		else if(finalDirection == TRAVELDIRECTION_RIGHT) {
			dx = travelSpeed;
		}
		else System.out.println("Error in setTravelSpeed for Square with finalDirection (" + finalDirection +").");
	}
	
	private void updateLastTravelSpeeds() {
		dxTravelLast = dx;
		dyTravelLast = dy;
	}
	
	private EntitySpawnData getWallEsd(int startX, int startY, int endX, int endY) {
		SquareWallExtra ed = new SquareWallExtra(
				startX,
				startY,
				endX,
				endY
			);
		return new EntitySpawnData(
			new SquareWall(
				tileMap,
				getTravelMode(
					startX,
					startY,
					endX,
					endY
				),
				ed
			),
			(startX + endX) / 2,
			(startY + endY) / 2
		);
	}

	private int getTravelMode(int startX, int startY, int endX, int endY) {
		if(startY == endY && startX != endX) {
			return TRAVELMODE_X;
		}
		else if(startY != endY && startX == endX) {
			return TRAVELMODE_Y;
		}
		else {
			System.out.println("Error in Square getTravelMode, travel direction must be either 100% in x axis or y axis");
			System.out.println("startX = " + startX + ", startY = " + startY + ", endX = " + endX + ", endY = " + endY);
			return TRAVELMODE_X;
		}
	}

	public void update() {
		if(firstUpdate) {
			saveStopPoint();
			firstUpdate = false;
		}
		// update position
		//getNextPosition();
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
		
		setMapPosition();
		
		// draw extending line like spider silk if traveling, connected to the last stopping point
		if(currentAnimation == TRAVEL) {
			Color origColor = g.getColor();
			g.setColor(Color.WHITE);
			float drawTravelWallOpacity = travelWallOpacity * Support.surroundingsOpacity;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawTravelWallOpacity));
			
			g.drawLine(
				(int) (stopPoint1x + xmap),
				(int) (stopPoint1y + ymap),
				(int) (x + xmap),
				(int) (y + ymap)
			);
			
			// reset original color & opacity
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
			g.setColor(origColor);
		}

		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		super.draw(g);
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
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
		
		if(spawnWall) {
			EntitySpawnData esd = getWallEsd(stopPoint2x, stopPoint2y, stopPoint1x, stopPoint1y);
			esdList.add(esd);
			
			spawnWall = false;
		}
		
		if(spawnWallPrison) {
			EntitySpawnData esd;
			
			esd = getWallEsd(stopPoint5x, stopPoint5y, stopPoint4x, stopPoint4y);
			esdList.add(esd);
			esd = getWallEsd(stopPoint4x, stopPoint4y, stopPoint3x, stopPoint3y);
			esdList.add(esd);
			esd = getWallEsd(stopPoint3x, stopPoint3y, stopPoint2x, stopPoint2y);
			esdList.add(esd);
			esd = getWallEsd(stopPoint2x, stopPoint2y, stopPoint1x, stopPoint1y);
			esdList.add(esd);
			
			spawnWallPrison = false;
		}
		
		if(spawnSquareLock) {
			EntitySpawnData esd = new EntitySpawnData(
				new SquareLock(tileMap, 0, null),
				(stopPoint4x + stopPoint3x + stopPoint2x + stopPoint1x) / 4,
				(stopPoint4y + stopPoint3y + stopPoint2y + stopPoint1y) / 4
			);
			esdList.add(esd);

			spawnSquareLock = false;
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
}








