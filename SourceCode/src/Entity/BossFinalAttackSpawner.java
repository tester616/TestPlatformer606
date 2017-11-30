package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall5Extra;
import EntityExtraData.BossFinalAttackSpawnerExtra;
import Handlers.Content;
import Main.GamePanel;

public class BossFinalAttackSpawner extends Enemy {

	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	private static final int IDLE = 0;
	private static final int ATTACK1 = 1;
	private static final int ATTACK2 = 2;
	private static final int ATTACK3 = 3;
<<<<<<< HEAD
	private static final int ATTACK4 = 1;
	private static final int ATTACK5 = 2;
	private static final int ATTACK6 = 3;
=======
	private static final int ATTACK4 = 4;
	private static final int ATTACK5 = 5;
	private static final int ATTACK6 = 6;
>>>>>>> 2.03
	
	private int currentAnimation;

	private boolean spawnAttack1;
	private boolean spawnAttack2;
	private boolean spawnAttack3;
	@SuppressWarnings("unused")
	private boolean spawnAttack4;
	@SuppressWarnings("unused")
	private boolean spawnAttack5;
	@SuppressWarnings("unused")
	private boolean spawnAttack6;
	private boolean spawnFlash;
	
	public BossFinalAttackSpawnerExtra ed;
	
	private int mode;

	private int currentDegreeMode;
	
	private long attack2SpawnInterval;
	private int attack2SpawnTotalFrames;
	private int attack2SpawnCurrentFrame;

	private int attack3FrameGemSpawnAmount;
	private long attack3HastenInterval;
	private long attack3HastenStart;
	private long attack3MinSpawnInterval;
	private long attack3MaxSpawnInterval;
	private long attack3CurrentSpawnInterval;
	private long attack3PassedTime;
	private long attack3SpawnTimeStart;
	private int attack3InnerSpawnMinRadius;
	@SuppressWarnings("unused")
	private int attack3InnerSpawnMaxRadius;
	private int attack3CurrentInnerSpawnRadius;
	private int attack3OuterSpawnMinRadius;
	private int attack3OuterSpawnMaxRadius;
	private int attack3CurrentOuterSpawnRadius;
	
	// once it goes below this spawning stops to prevent lag
	private int minimumToleratedFramerate;
	
	
	
	public BossFinalAttackSpawner(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 31;
		height = 31;
		cwidth = 0;
		cheight = 0;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();

		spritesC = Content.Invisible[0];

		spritesM = Content.Invisible[0];
		
        setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		currentAnimation = IDLE;
		
		setModeSpecificValues();
		
		minimumToleratedFramerate = (int) (GamePanel.TARGETFPS * 1.25);
	}
	
	private void setModeSpecificValues() {
		if(mode == 2) {
			attack2SpawnInterval = ed.spawnInterval;
			attack2SpawnTotalFrames = (int) (((double) attack2SpawnInterval / 1000) * GamePanel.TARGETFPS);
			attack2SpawnCurrentFrame = 0;
		}
		else if(mode == 3) {
			attack3HastenInterval = 7000;
			attack3HastenStart = System.nanoTime();
			attack3MinSpawnInterval = 4;
			attack3MaxSpawnInterval = 75;
			attack3CurrentSpawnInterval = attack3MaxSpawnInterval;
			attack3PassedTime = System.nanoTime();
			attack3SpawnTimeStart = System.nanoTime();
			attack3InnerSpawnMinRadius = 75;
			attack3InnerSpawnMaxRadius = 75;
			attack3CurrentInnerSpawnRadius = attack3InnerSpawnMinRadius;
			attack3OuterSpawnMinRadius = 85;
			attack3OuterSpawnMaxRadius = 170;
			attack3CurrentOuterSpawnRadius = attack3OuterSpawnMinRadius;
		}
	}

	private void updateAttack3Values() {
		long passedTime = (System.nanoTime() - attack3HastenStart) / 1000000;
		
		double percentPassed = (double) passedTime / (double) attack3HastenInterval;
		if(percentPassed > 1.0) percentPassed = 1.0;
		
		attack3CurrentSpawnInterval = (long) (attack3MaxSpawnInterval - percentPassed * (attack3MaxSpawnInterval - attack3MinSpawnInterval));
		//attack3CurrentInnerSpawnRadius = (int) (attack3InnerSpawnMinRadius - percentPassed * (attack3InnerSpawnMinRadius - attack3InnerSpawnMaxRadius));
		attack3CurrentOuterSpawnRadius = (int) (attack3OuterSpawnMinRadius - percentPassed * (attack3OuterSpawnMinRadius - attack3OuterSpawnMaxRadius));
	}

	// mode given is synchronized with currentAnimation, so 1 spawns attack1 etc.
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			currentAnimation = mode;
		}
		else if(currentAnimation == ATTACK1) {
			spawnAttack1 = true;
			dead = true;
		}
		else if(currentAnimation == ATTACK2) {
			attack2SpawnCurrentFrame++;
			if(attack2SpawnCurrentFrame >= attack2SpawnTotalFrames) {
				if(!ed.attackList.isEmpty()) {
					spawnAttack2 = true;
					attack2SpawnCurrentFrame = 0;
				}
				else dead = true;
			}
		}
		else if(currentAnimation == ATTACK3) {
			attack3PassedTime = (System.nanoTime() - attack3SpawnTimeStart) / 1000000;
			
			while(attack3PassedTime >= attack3CurrentSpawnInterval) {
				if(ed.attackList.size() >= attack3FrameGemSpawnAmount + 1) {
					attack3FrameGemSpawnAmount++;
				}
				else dead = true;

				attack3PassedTime -= attack3CurrentSpawnInterval;
			}
			
			if(attack3FrameGemSpawnAmount > 0) {
				if(GamePanel.maxFps > minimumToleratedFramerate) spawnAttack3 = true;
				else attack3FrameGemSpawnAmount = 0;
				
				// additional empty flash spawn for effect when at its most intense
				if(attack3CurrentSpawnInterval == attack3MinSpawnInterval) spawnFlash = true;
				attack3SpawnTimeStart = System.nanoTime();
			}
			
			updateAttack3Values();
		}
		else if(currentAnimation == ATTACK4) {
			spawnAttack4 = true;
			dead = true;
		}
		else if(currentAnimation == ATTACK5) {
			spawnAttack5 = true;
			dead = true;
		}
		else if(currentAnimation == ATTACK6) {
			spawnAttack6 = true;
			dead = true;
		}
	}

	public void update() {
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
	
	@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		// surround player with gems that shoot inward at the same time
		if(spawnAttack1) {
			// 0-11 upper half with 0 for reverse, 12-23 lower half with 1 for reverse
			for(int i = 0; i < 24; i++) {
				// default values if nothing else exists, 2 represents random color
				currentDegreeMode = i;
				int reverse = 0;
				int color = 2;
				long duration = 2000;
				double totalSpeed = 2.0;
				double radius = 50.0;
				int degrees = 0;
				ArrayList<Double> spawnCoordinates;
				double xCoordinate;
				double yCoordinate;
				
				// degrees is decided before mode can reset so the higher degrees are covered too
				degrees = currentDegreeMode * 15;
				
				// change color and speed if data is available
				if(ed.attackList.size() > i) {
					color = ed.attackList.get(i).color;
					duration = ed.attackList.get(i).duration;
					totalSpeed = ed.attackList.get(i).totalSpeed;
					radius = ed.attackList.get(i).radius;
				}
				
				// if color is random, change to red or green
				if(color == 2) color = Support.randInt(0, 1);
				// if reverse is random, change to yes or no (should never activate)
				if(reverse == 2) color = Support.randInt(0, 1);
				
				// extra data
				BossBall5Extra ed = new BossBall5Extra(reverse, duration, totalSpeed, currentAnimation);
				
				// get spawn x & y
				spawnCoordinates = Support.getCircleXYPos(radius, degrees);
				xCoordinate = spawnCoordinates.get(0);
				yCoordinate = spawnCoordinates.get(1);
				
				// red
				if(color == 0) {
					EntitySpawnData esd = new EntitySpawnData(
						new BossBall5GemRed(tileMap, currentDegreeMode, ed),
						getx() + (int) xCoordinate,
						gety() + (int) yCoordinate
					);
					esdList.add(esd);
				}
				// green
				else if(color == 1) {
					EntitySpawnData esd = new EntitySpawnData(
						new BossBall5GemGreen(tileMap, currentDegreeMode, ed),
						getx() + (int) xCoordinate,
						gety() + (int) yCoordinate
					);
					esdList.add(esd);
				}
				else System.out.println("Wrong color (" + color + ") for spawnAttack1 with number " + i + " of loop");
			}
			
			spawnAttack1 = false;
		}
		// continuous spiral of gems
		if(spawnAttack2) {
			// default values if nothing else exists, 2 represents random color
			int reverse = 0;
			int color = 2;
			long duration = 2000;
			double totalSpeed = 2.0;
			double radius = 100.0;
			int degrees = 0;
			ArrayList<Double> spawnCoordinates;
			double xCoordinate;
			double yCoordinate;
			
			// degrees is decided before mode can reset so the higher degrees are covered too
			degrees = currentDegreeMode * 15;
			
			color = ed.attackList.get(0).color;
			duration = ed.attackList.get(0).duration;
			totalSpeed = ed.attackList.get(0).totalSpeed;
			radius = ed.attackList.get(0).radius;
			
			// if color is random, change to red or green
			if(color == 2) color = Support.randInt(0, 1);
			// if reverse is random, change to yes or no (should never activate)
			if(reverse == 2) color = Support.randInt(0, 1);
			
			// extra data
			BossBall5Extra ed = new BossBall5Extra(reverse, duration, totalSpeed, currentAnimation);
			
			// get spawn x & y
			spawnCoordinates = Support.getCircleXYPos(radius, degrees);
			xCoordinate = spawnCoordinates.get(0);
			yCoordinate = spawnCoordinates.get(1);
			
			// red
			if(color == 0) {
				EntitySpawnData esd = new EntitySpawnData(
					new BossBall5GemRed(tileMap, currentDegreeMode, ed),
					getx() + (int) xCoordinate,
					gety() + (int) yCoordinate
				);
				esdList.add(esd);
			}
			// green
			else if(color == 1) {
				EntitySpawnData esd = new EntitySpawnData(
					new BossBall5GemGreen(tileMap, currentDegreeMode, ed),
					getx() + (int) xCoordinate,
					gety() + (int) yCoordinate
				);
				esdList.add(esd);
			}
			else System.out.println("Wrong color (" + color + ") for spawnAttack2");
			
			// after each spawned gem next one is spawned with the next mode at +15 degrees, looping back to 0 after 23
			currentDegreeMode++;
			if(currentDegreeMode > 23) currentDegreeMode = 0;
			
			// after each spawned gem, the first position of the list of attacking gems is removed until nothing is left
			this.ed.attackList.remove(0);
			
			spawnAttack2 = false;
		}
		// random spawns all around with increasing speed
		if(spawnAttack3) {
			// can spawn multiple gems per frame
			while(attack3FrameGemSpawnAmount > 0) {
				// default values if nothing else exists, 2 represents random color
				int reverse = 0;
				int color = 2;
				long duration = 2000;
				double totalSpeed = 2.0;
				double xCoordinate = 0;
				double yCoordinate = 0;
				
				// all gems have random mode and with that random direction
				currentDegreeMode = Support.randInt(0, 23);
				
				color = ed.attackList.get(0).color;
				duration = ed.attackList.get(0).duration;
				totalSpeed = ed.attackList.get(0).totalSpeed;
				
				// if color is random, change to red or green
				if(color == 2) color = Support.randInt(0, 1);
				// if reverse is random, change to yes or no (should never activate)
				if(reverse == 2) color = Support.randInt(0, 1);
				
				// extra data
				BossBall5Extra ed = new BossBall5Extra(reverse, duration, totalSpeed, currentAnimation);
				
				// get spawn x & y
				while(true) {
					xCoordinate = Support.randInt(0, attack3CurrentOuterSpawnRadius);
					if(Support.randInt(0, 1) == 0) xCoordinate *= -1;
					yCoordinate = Support.randInt(0, attack3CurrentOuterSpawnRadius);
					if(Support.randInt(0, 1) == 0) yCoordinate *= -1;
					
					if(Math.hypot(xCoordinate, yCoordinate) >= attack3CurrentInnerSpawnRadius) break;
				}

				
				// red
				if(color == 0) {
					EntitySpawnData esd = new EntitySpawnData(
						new BossBall5GemRed(tileMap, currentDegreeMode, ed),
						getx() + (int) xCoordinate,
						gety() + (int) yCoordinate
					);
					esdList.add(esd);
				}
				// green
				else if(color == 1) {
					EntitySpawnData esd = new EntitySpawnData(
						new BossBall5GemGreen(tileMap, currentDegreeMode, ed),
						getx() + (int) xCoordinate,
						gety() + (int) yCoordinate
					);
					esdList.add(esd);
				}
				else System.out.println("Wrong color (" + color + ") for spawnAttack3");
				
				// after each spawned gem, the first position of the list of attacking gems is removed until nothing is left
				this.ed.attackList.remove(0);
				
				attack3FrameGemSpawnAmount--;
			}
			
			spawnAttack3 = false;
		}
		
		if(spawnFlash) {
			double xCoordinate = 0;
			double yCoordinate = 0;
			
			while(true) {
				xCoordinate = Support.randInt(0, attack3CurrentOuterSpawnRadius);
				if(Support.randInt(0, 1) == 0) xCoordinate *= -1;
				yCoordinate = Support.randInt(0, attack3CurrentOuterSpawnRadius);
				if(Support.randInt(0, 1) == 0) yCoordinate *= -1;
				
				if(Math.hypot(xCoordinate, yCoordinate) >= attack3CurrentInnerSpawnRadius) break;
			}
			
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall5Flash(tileMap, 0, null),
				getx() + (int) xCoordinate,
				gety() + (int) yCoordinate
			);
			esdList.add(esd);
			
			while(true) {
				xCoordinate = Support.randInt(0, attack3CurrentOuterSpawnRadius);
				if(Support.randInt(0, 1) == 0) xCoordinate *= -1;
				yCoordinate = Support.randInt(0, attack3CurrentOuterSpawnRadius);
				if(Support.randInt(0, 1) == 0) yCoordinate *= -1;
				
				if(Math.hypot(xCoordinate, yCoordinate) >= attack3CurrentInnerSpawnRadius) break;
			}
			
			esd = new EntitySpawnData(
				new BossBall5Flash(tileMap, 0, null),
				getx() + (int) xCoordinate,
				gety() + (int) yCoordinate
			);
			esdList.add(esd);

			spawnFlash = false;
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
		this.mode = mode;
		ed = (BossFinalAttackSpawnerExtra) extraData;
		
		// not all modes make use of this, but the spiral for example can be started from multiple positions
		currentDegreeMode= ed.startDegreeMode; 
	}
}







