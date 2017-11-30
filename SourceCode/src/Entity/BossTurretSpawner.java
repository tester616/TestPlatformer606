package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossTurretGemExtra;
import EntityExtraData.BossTurretListExtra;
import EntityExtraData.BossTurretSpawnerExtra;
import Handlers.Content;
import Main.GamePanel;

public class BossTurretSpawner extends Enemy {

	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	private final int IDLE = 0;
	private final int ATTACK = 1;
	
	private int currentAnimation;

	private boolean spawnAttack;
	
	public BossTurretSpawnerExtra ed;
	
	public ArrayList<BossTurretListExtra> attackList;
	
	private long attackSpawnInterval;
	private int attackSpawnIntervalCurrentFrame;
	private int attackSpawnIntervalTotalFrames;
	
	private int spawnBurstAmount;

	private long initialIdleTime;
	private int idleCurrentFrame;
	private int idleTotalFrames;
	
	public BossTurretSpawner(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 31;
		height = 31;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		idleTotalFrames = (int) (((double) initialIdleTime / 1000) * GamePanel.TARGETFPS);
		idleCurrentFrame = 0;
		
		attackSpawnIntervalTotalFrames = (int) (((double) attackSpawnInterval / 1000) * GamePanel.TARGETFPS);
		attackSpawnIntervalCurrentFrame = 0;
		
		updateColorMode();

		spritesC = Content.Invisible[0];

		spritesM = Content.Invisible[0];
		
        setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		currentAnimation = IDLE;
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			idleCurrentFrame++;
			if(idleCurrentFrame >= idleTotalFrames) {
				currentAnimation = ATTACK;
				attackSpawnIntervalCurrentFrame = attackSpawnIntervalTotalFrames;
			}
		}
		else if(currentAnimation == ATTACK) {
			attackSpawnIntervalCurrentFrame++;
			if(attackSpawnIntervalCurrentFrame >= attackSpawnIntervalTotalFrames) {
				if(!ed.attackList.isEmpty()) {
					spawnAttack = true;
					attackSpawnIntervalCurrentFrame = 0;
				}
				else dead = true;
			}
		}
	}

	public void update() {
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
	
	@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(spawnAttack) {
			for(int i = 0; i < spawnBurstAmount; i++) {
				int color = attackList.get(0).color;
				double totalSpeed = attackList.get(0).totalSpeed;
				double degrees = attackList.get(0).degrees;
				double radians = Math.toRadians(degrees);
				ArrayList<Double> xySpeeds = Support.getCircleXYPos(totalSpeed, radians);
				
				// if color is random, change to red or green
				if(color == 2) color = Support.randInt(0, 1);
				
				BossTurretGemExtra ed = new BossTurretGemExtra(xySpeeds.get(0), xySpeeds.get(1));
				
				// red
				if(color == 0) {
					EntitySpawnData esd = new EntitySpawnData(
						new BossTurretGemRed(tileMap, 0, ed),
						getx(),
						gety()
					);
					esdList.add(esd);
				}
				// green
				else if(color == 1) {
					EntitySpawnData esd = new EntitySpawnData(
						new BossTurretGemGreen(tileMap, 0, ed),
						getx(),
						gety()
					);
					esdList.add(esd);
				}
				else System.out.println("Wrong color (" + color + ") for spawnAttack in BossTurretSpawner, getEntitiesToSpawn");
				
				// after each spawned gem, the first position of the list of attacking gems is removed until nothing is left
				attackList.remove(0);
				
				if(attackList.isEmpty()) {
					dead = true;
					break;
				}
			}
			
			spawnAttack = false;
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
	public void setExtraData(int mode, Object extraData) {
		ed = (BossTurretSpawnerExtra) extraData;
		
		attackList = new ArrayList<BossTurretListExtra>(ed.attackList);
		attackSpawnInterval = ed.spawnInterval;
		spawnBurstAmount = ed.spawnBurstAmount;
		initialIdleTime = ed.initialIdleTime;
	}
}







