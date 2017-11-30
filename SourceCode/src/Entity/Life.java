package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Handlers.Content;
import Support.Support;
import TileMap.TileMap;

public class Life extends Collectible {
	
	private boolean remove;
	
	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	private final int IDLE = 0;
	private final int SHINE = 1;
	
	private int currentAnimation;
	
	private int maxOffset;

	private int spawnShineMinTime;
	private int spawnShineMaxTime;
	private long spawnShineTime;
	private long spawnShineStart;

	private boolean spawnShine;

	
	public Life(TileMap tm) {
		
		super(tm);
		
		moveSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 14;
		cheight = 14;
		
		currentAnimation = IDLE;
		
		maxOffset = 17;
		
		spawnShineMinTime = 800;
		spawnShineMaxTime = 3000;
		spawnShineTime = getRandomShineTime();
		spawnShineStart = System.nanoTime();
		
		spritesC = Content.Life[0];
		
		spritesM = Content.LifeM[0];
		
		setBufferedImages();
			
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	private long getRandomShineTime() {
		return Support.randInt(spawnShineMinTime, spawnShineMaxTime);
	}

	public boolean shouldRemove() { return remove; }
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - spawnShineStart) / 1000000;
			
			if(passedTime >= spawnShineTime) {
				currentAnimation = SHINE;
			}
		}
		else if(currentAnimation == SHINE) {
			spawnShine = true;
			currentAnimation = IDLE;
			spawnShineTime = getRandomShineTime();
			spawnShineStart = System.nanoTime();
		}
	}
	
	public void update() {
		
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		animation.update();
		
		updateCurrentAnimation();
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(spawnShine) {
			EntitySpawnData esd = new EntitySpawnData(
				new LifeShine(tileMap),
				getx() + Support.randInt(-maxOffset, maxOffset),
				gety() + Support.randInt(-maxOffset, maxOffset)
			);
			esdList.add(esd);
			
			spawnShine = false;
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




















