package Entity.Enemies;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Darkmist extends Enemy {

	private BufferedImage[] chaseSprites;
	
	private BufferedImage[] chaseSpritesC;
	
	private BufferedImage[] chaseSpritesM;
	
	private double playerX;
	private double playerY;
	
	private double totalSpeedVector;
	
	private double homingStrength;
	
	private int currentAnimation;
	
	private final int CHASE = 0;
	
	public Darkmist(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.35;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 31;
		cwidth = 9;
		cheight = 9;
		
		health = maxHealth = 1;
		damage = 5;
		
		cost = Support.COST_DARKMIST;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		chaseSpritesC = Content.Darkmist[0];
		
		chaseSpritesM = Content.DarkmistM[0];
		
		setBufferedImages();
		
		currentAnimation = CHASE;
		
		animation = new Animation();
		animation.setFrames(chaseSprites);
		animation.setDelay(35);
		
		speedMultiplier = 1.0;
		homingStrength = -0.99996;
	}
	
	private void getNextPosition() {
		if(currentAnimation == CHASE) {
			double xDifference = playerX - x;
			double yDifference = playerY - y;
			
			if(homingStrength > 0.0 && homingStrength <= 1.0) {
				dx = dx * (1 - Math.abs(homingStrength));
				dy = dy * (1 - Math.abs(homingStrength));
			}
			
			if(homingStrength < 0.0 && homingStrength >= -1.0) {
				xDifference = xDifference * (1 - Math.abs(homingStrength));
				yDifference = yDifference * (1 - Math.abs(homingStrength));
			}
			
			dx = (dx + xDifference) * speedMultiplier;
			dy = (dy + yDifference) * speedMultiplier;
			
			totalSpeedVector = Math.hypot(dx, dy);
			if(totalSpeedVector > maxSpeed) {
				double totalSpeedInDecimal = totalSpeedVector / maxSpeed;
				 dx = dx / totalSpeedInDecimal;
				 dy = dy / totalSpeedInDecimal;
			}
		}
	}
	
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	/*@Override
	public boolean spawnEntity() {
		return false;
	}*/
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		animation.update();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		//float opacity = (float) 1.0;
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
		super.draw(g);
		//opacity = (float) 1.0;
		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
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
		if(currentAnimation == CHASE) {
			animation.swapFrames(chaseSprites);
		}
	}
}










