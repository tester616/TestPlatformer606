package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Triangle extends Enemy {

	private BufferedImage[] idleSprites;
	private BufferedImage[] riseSprites;
	private BufferedImage[] walkSprites;
	private BufferedImage[] lowerSprites;
	
	private BufferedImage[] idleSpritesC;
	private BufferedImage[] riseSpritesC;
	private BufferedImage[] walkSpritesC;
	private BufferedImage[] lowerSpritesC;
	
	private BufferedImage[] idleSpritesM;
	private BufferedImage[] riseSpritesM;
	private BufferedImage[] walkSpritesM;
	private BufferedImage[] lowerSpritesM;
	
	private static final int IDLE = 0;
	private static final int RISE = 1;
	private static final int WALK = 2;
	private static final int LOWER = 3;
	
	private int currentAnimation;
	
	private long idleTime;
	private long idleStart;
	private int idleTimeMin;
	private int idleTimeMax;
	
	private long walkTime;
	private long walkStart;
	private int walkTimeMin;
	private int walkTimeMax;
	
	private boolean outOfBounds;
	
	public Triangle(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		moveSpeed = 0.3;
		maxSpeed = 0.5;
		fallSpeed = 0.2;
		maxFallSpeed = 4.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		health = maxHealth = 1;
		damage = 9;
		
		cost = Support.COST_TRIANGLE;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();

		idleSpritesC = Content.Triangle[0];
		riseSpritesC = Content.Triangle[1];
		walkSpritesC = Content.Triangle[2];
		lowerSpritesC = Content.Triangle[3];

		idleSpritesM = Content.TriangleM[0];
		riseSpritesM = Content.TriangleM[1];
		walkSpritesM = Content.TriangleM[2];
		lowerSpritesM = Content.TriangleM[3];
		
        setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(idleSprites);
		animation.setDelay(-1);
		
		right = true;
		facingRight = true;
		
		currentAnimation = IDLE;
		
		idleTimeMin = 3000;
		idleTimeMax = 20000;
		
		walkTimeMin = 1000;
		walkTimeMax = 6000;
	}

	private void getNextPosition() {
		if(currentAnimation == IDLE) {
			dx = 0;
		}
		else if(currentAnimation == RISE) {
			dx = 0;
		}
		else if(currentAnimation == WALK) {
			if(left) {
				dx -= moveSpeed;
				if(dx < -maxSpeed) {
					dx = -maxSpeed;
				}
			}
			else if(right) {
				dx += moveSpeed;
				if(dx > maxSpeed) {
					dx = maxSpeed;
				}
			}
		}
		else if(currentAnimation == LOWER) {
			dx = 0;
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - idleStart) / 1000000;
			
			if(passedTime >= idleTime) {
				currentAnimation = RISE;
				animation.setFrames(riseSprites);
				animation.setDelay(170);
				invulnerable = false;
			}
		}
		else if(currentAnimation == RISE && animation.hasPlayedOnce()) {
			currentAnimation = WALK;
			animation.setFrames(walkSprites);
			animation.setDelay(200);
			invulnerable = false;
			setWalkTime();
			walkStart = System.nanoTime();
			if(Support.randInt(0, 1) == 1) {
				facingRight = true;
				right = true;
				left = false;
			}
			else {
				facingRight = false;
				right = false;
				left = true;
			}
		}
		else if(currentAnimation == WALK) {
			
			long passedTime = (System.nanoTime() - walkStart) / 1000000;
			
			if(passedTime >= walkTime) {
				currentAnimation = LOWER;
				animation.setFrames(lowerSprites);
				animation.setDelay(170);
				invulnerable = false;
			}
		}
		else if(currentAnimation == LOWER && animation.hasPlayedOnce()) {
			currentAnimation = IDLE;
			animation.setFrames(idleSprites);
			animation.setDelay(-1);
			invulnerable = true;
			setIdleTime();
			idleStart = System.nanoTime();
		}
	}
	
	private void setWalkTime() {
		walkTime = Long.valueOf(Support.randInt(
				walkTimeMin,
				walkTimeMax
		));
	}

	private void setIdleTime() {
		idleTime = Long.valueOf(Support.randInt(
				idleTimeMin,
				idleTimeMax
		));
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
	
	public void checkTileMapCollision() {
		super.checkTileMapCollision();

		// if it hits a wall, go other direction
		if(right && dx == 0 && currentAnimation == WALK) {
			right = false;
			left = true;
			facingRight = false;
		}
		else if(left && dx == 0 && currentAnimation == WALK) {
			right = true;
			left = false;
			facingRight = true;
		}
		
		if(y > tileMap.getHeight() + 50) {
			dead = true;
			outOfBounds = true;
		}
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(dead && !outOfBounds) {
			EntitySpawnData esd = new EntitySpawnData(
				new Explosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
		}
		
		return esdList;
	}
	
	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			idleSprites = idleSpritesC;
			riseSprites = riseSpritesC;
			walkSprites = walkSpritesC;
			lowerSprites = lowerSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			idleSprites = idleSpritesM;
			riseSprites = riseSpritesM;
			walkSprites = walkSpritesM;
			lowerSprites = lowerSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == IDLE) {
			animation.swapFrames(idleSprites);
		}
		else if(currentAnimation == RISE) {
			animation.swapFrames(riseSprites);
		}
		else if(currentAnimation == WALK) {
			animation.swapFrames(walkSprites);
		}
		else if(currentAnimation == LOWER) {
			animation.swapFrames(lowerSprites);
		}
	}
}









