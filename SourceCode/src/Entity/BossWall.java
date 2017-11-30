package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Handlers.Content;

public class BossWall extends Enemy {


	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;

	private int currentAnimation;
	
	private final int FLYING = 0;

	private boolean spawnBall;
	
	private double speed;
	
	public static final int MODE_LEFT = 0;
	public static final int MODE_RIGHT = 1;
	
	public BossWall(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 31;
		height = 279;
		cwidth = 13;
		cheight = 261;
		
		health = maxHealth = 3;
		damage = 15;
		
		speed = 0.9;
		
		currentAnimation = 0;
		
		setSpeed();
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.BossWall[0];
		
		spritesM = Content.BossWallM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(50);
		
		JukeBox.playWithRecommendedVolume("bosswall");
	}
	
	private void setSpeed() {
		dy = 0.0;
		if(facingRight) dx = speed;
		else dx = -speed;
	}

	private void getNextPosition() {
		if(currentAnimation == FLYING) {
			// nothing here
			spawnBall = true;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FLYING) {
			// nothing here
		}
	}

	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		checkOutOfBounds();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}

	private void checkOutOfBounds() {
		if(x < -50 - width / 2) dead = true;
		if(x > tileMap.getWidth() + 50 + width / 2) dead = true;
		if(y < -50 - height / 2) dead = true;
		if(y > tileMap.getHeight() + 50 + height / 2) dead = true;
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
		
		if(spawnBall) {
			int xPotentialDifference = (int)(width / 2 * 0.8);
			int spawnXDifference = Support.randInt(0, xPotentialDifference * 2) - xPotentialDifference;
			
			int yPotentialDifference = (int)(height / 2 * 0.9);
			int spawnYDifference = Support.randInt(0, yPotentialDifference * 2) - yPotentialDifference;
			
			EntitySpawnData esd = new EntitySpawnData(
				new BossWallBall(tileMap, 0, null),
				getx() + spawnXDifference,
				gety() + spawnYDifference
			);
			esdList.add(esd);
			
			spawnBall = false;
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
		if(mode == MODE_LEFT) facingRight = false;
		else if(mode == MODE_RIGHT) facingRight = true;
		else System.out.println("Wrong mode (" + mode + ") in BossWall setExtraData.");
	}
}









