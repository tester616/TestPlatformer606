package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Spikes extends Enemy {

	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	public Spikes(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		moveSpeed = 1.0;
		maxSpeed = 1.0;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		health = maxHealth = 1;
		damage = 9;
		
		cost = Support.COST_SPIKES;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.Spikes[0];
		
		spritesM = Content.SpikesM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(40);
		
		if(Support.randInt(0, 1) == 1) right = true;
		else left = true;
		
		if(Support.randInt(0, 1) == 1) up = true;
		else down = true;
		
		if(Support.randInt(0, 1) == 1) facingRight = true;
	}
	
	private void getNextPosition() {
		
		// x movement
		if(left) {
			dx = -moveSpeed;
		}
		else if(right) {
			dx = moveSpeed;
		}
		
		// y movement
		if(up) {
			dy = -moveSpeed;
		}
		else if(down) {
			dy = moveSpeed;
		}
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
		
		setDirection();
		
		// update animation
		animation.update();
	}
	
	private void setDirection() {
		// x direction update		
		if(left && dx == 0 || x - cwidth / 2 - 1 < 0) {
			right = true;
			left = false;
			//facingRight = true;
		}
		else if(right && dx == 0 || x + cwidth / 2 + 1 > tileMap.getWidth()) {
			right = false;
			left = true;
			//facingRight = false;
		}
		// y direction update
		if(up && dy == 0 || y - cheight / 2 - 1 < 0) {
			up = false;
			down = true;
		}
		else if(down && dy == 0 || y + cheight / 2 + 1 > tileMap.getHeight()) {
			up = true;
			down = false;
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
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









