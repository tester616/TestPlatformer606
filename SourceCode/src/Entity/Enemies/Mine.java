package Entity.Enemies;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Entity.*;
import Handlers.Content;

public class Mine extends Enemy {

	private BufferedImage[] greenLightNormalSprites;
	private BufferedImage[] yellowLightNormalSprites;
	private BufferedImage[] redLightNormalSprites;
	private BufferedImage[] noLightNormalSprites;
	private BufferedImage[] burrowSprites;
	private BufferedImage[] unburrowSprites;
	@SuppressWarnings("unused")
	private BufferedImage[] greenLightHideSprites;
	@SuppressWarnings("unused")
	private BufferedImage[] yellowLightHideSprites;
	private BufferedImage[] redLightHideSprites;
	private BufferedImage[] teleportSprites;

	private BufferedImage[] greenLightNormalSpritesC;
	private BufferedImage[] yellowLightNormalSpritesC;
	private BufferedImage[] redLightNormalSpritesC;
	private BufferedImage[] noLightNormalSpritesC;
	private BufferedImage[] burrowSpritesC;
	private BufferedImage[] unburrowSpritesC;
	private BufferedImage[] greenLightHideSpritesC;
	private BufferedImage[] yellowLightHideSpritesC;
	private BufferedImage[] redLightHideSpritesC;
	private BufferedImage[] teleportSpritesC;

	private BufferedImage[] greenLightNormalSpritesM;
	private BufferedImage[] yellowLightNormalSpritesM;
	private BufferedImage[] redLightNormalSpritesM;
	private BufferedImage[] noLightNormalSpritesM;
	private BufferedImage[] burrowSpritesM;
	private BufferedImage[] unburrowSpritesM;
	private BufferedImage[] greenLightHideSpritesM;
	private BufferedImage[] yellowLightHideSpritesM;
	private BufferedImage[] redLightHideSpritesM;
	private BufferedImage[] teleportSpritesM;
	
	private static final int GREENLIGHTIDLE = 0;
	private static final int YELLOWLIGHTALERT = 1;
	private static final int REDLIGHTESCAPE = 2;
	private static final int BURROW = 3;
	private static final int HIDE = 4;
	private static final int UNBURROW = 5;
	private static final int JUMPDESTRUCT = 6;
	private static final int KILLEDDESTRUCT = 7;
	private static final int TELEPORT = 8;
	
	private int currentAnimation;
	
	// idle time info
	private long greenLightIdleTime;
	private long greenLightIdleStart;
	private int greenLightIdleTimeMin;
	private int greenLightIdleTimeMax;
	
	// alert time info
	private long yellowLightAlertTime;
	private long yellowLightAlertStart;
	
	// escape time info
	private long redLightEscapeTime;
	private long redLightEscapeStart;
	
	// killed time info
	private long noLightKilledTime;
	private long noLightKilledStart;
	
	// jumpdestruct time info
	private long jumpDestructTime;
	private long jumpDestructStart;
	
	// how close player needs to be for alert and escape states to trigger, calculated in a box around the mine so a value of 2 would make a 4x4 box with the mine at the center
	private int playerAlertRange;
	private int playerEscapeRange;
	private int playerEscapeRangeMin;
	private int playerEscapeRangeMax;
	
	// determines if mine escapes or waits for player after burrowing
	private boolean escaping;
	
	// jumps up and explodes if true and hiding
	private boolean triggered;
	
	private float hidingOpacity;
	private float visibleOpacity;
	private float currentOpacity;
	
	// player position
	private double playerX;
	private double playerY;
	
	// where it will be teleported if it occurs
	private Point teleportPoint;
	private boolean jumped;
	
	public Mine(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);

		moveSpeed = 0.3;
		maxSpeed = 0.5;
		fallSpeed = 0.15;
		maxFallSpeed = 4.0;
		jumpStart = -3.25;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		health = maxHealth = 1;
		damage = 0;
		
		cost = Support.COST_MINE;
		
		pacified = true;
		invulnerable = false;
		
		escaping = false;
		
		right = true;
		facingRight = true;
		
		jumped = false;
		
		greenLightIdleTimeMin = 10000;
		greenLightIdleTimeMax = 60000;
		
		yellowLightAlertTime = 5000;
		
		redLightEscapeTime = 1900;
		
		noLightKilledTime = 3200;
		
		jumpDestructTime = 530;

		hidingOpacity = (float) 0.10;
		visibleOpacity = (float) 1.0;
		currentOpacity = visibleOpacity;

		playerAlertRange = 150;
		playerEscapeRangeMin = 75;
		playerEscapeRange = playerEscapeRangeMin;
		playerEscapeRangeMax = playerAlertRange;
		
		teleportPoint = new Point(0, 0);
		
		updateColorMode();

		greenLightNormalSpritesC = Content.Mine[0];
		yellowLightNormalSpritesC = Content.Mine[1];
		redLightNormalSpritesC = Content.Mine[2];
		noLightNormalSpritesC = Content.Mine[3];
		burrowSpritesC = Content.Mine[4];
		unburrowSpritesC = Content.Mine[5];
		greenLightHideSpritesC = Content.Mine[6];
		yellowLightHideSpritesC = Content.Mine[7];
		redLightHideSpritesC = Content.Mine[8];
		teleportSpritesC = Content.Invisible[0];

		greenLightNormalSpritesM = Content.MineM[0];
		yellowLightNormalSpritesM = Content.MineM[1];
		redLightNormalSpritesM = Content.MineM[2];
		noLightNormalSpritesM = Content.MineM[3];
		burrowSpritesM = Content.MineM[4];
		unburrowSpritesM = Content.MineM[5];
		greenLightHideSpritesM = Content.MineM[6];
		yellowLightHideSpritesM = Content.MineM[7];
		redLightHideSpritesM = Content.MineM[8];
		teleportSpritesM = Content.Invisible[0];

		/*greenLightNormalSpritesM = Content.convertToGrayScale(greenLightNormalSpritesC);
		yellowLightNormalSpritesM = Content.convertToGrayScale(yellowLightNormalSpritesC);
		redLightNormalSpritesM = Content.convertToGrayScale(redLightNormalSpritesC);
		noLightNormalSpritesM = Content.convertToGrayScale(noLightNormalSpritesC);
		burrowSpritesM = Content.convertToGrayScale(burrowSpritesC);
		unburrowSpritesM = Content.convertToGrayScale(unburrowSpritesC);
		greenLightHideSpritesM = Content.convertToGrayScale(greenLightHideSpritesC);
		yellowLightHideSpritesM = Content.convertToGrayScale(yellowLightHideSpritesC);
		redLightHideSpritesM = Content.convertToGrayScale(redLightHideSpritesC);
		teleportSpritesM = Content.convertToGrayScale(teleportSpritesC);*/
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(greenLightNormalSprites);
		animation.setDelay(-1);
		
		currentAnimation = GREENLIGHTIDLE;
		greenLightIdleStart = System.nanoTime();
		setGreenLightIdleTime();
	}
	
	private void getNextPosition() {
		if(currentAnimation == GREENLIGHTIDLE) {
			dx = 0;
		}
		else if(currentAnimation == YELLOWLIGHTALERT) {
			dx = 0;
		}
		else if(currentAnimation == REDLIGHTESCAPE) {
			dx = 0;
		}
		else if(currentAnimation == BURROW) {
			dx = 0;
		}
		else if(currentAnimation == HIDE) {
			dx = 0;
		}
		else if(currentAnimation == UNBURROW) {
			dx = 0;
		}
		else if(currentAnimation == JUMPDESTRUCT) {
			if(!falling && !jumped) {
				dy += jumpStart;
				jumped = true;
			}
			dx = 0;
		}
		else if(currentAnimation == KILLEDDESTRUCT) {
			dx = 0;
		}
		else if(currentAnimation == TELEPORT) {
			dx = 0;
			dy = 0;
			if (!(teleportPoint.x == 0 && teleportPoint.y == 0)) {
				x = teleportPoint.x;
				y = teleportPoint.y;
			}
		}
		
		// falling
		if(falling) {
			dy += fallSpeed;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == GREENLIGHTIDLE) {
			long passedTime = (System.nanoTime() - greenLightIdleStart) / 1000000;
			
			if(passedTime >= greenLightIdleTime) {
				currentAnimation = BURROW;
				animation.setFrames(burrowSprites);
				animation.setDelay(140);
				pacified = true;
				invulnerable = true;
			}
			else if(playerIsInsideAlertRange()) {
				currentAnimation = YELLOWLIGHTALERT;
				animation.setFrames(yellowLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = false;
				yellowLightAlertStart = System.nanoTime();
			}
			else if(playerIsInsideEscapeRange()) {
				currentAnimation = REDLIGHTESCAPE;
				animation.setFrames(redLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = false;
				redLightEscapeStart = System.nanoTime();
			}
			else if(health == 0) {
				currentAnimation = KILLEDDESTRUCT;
				animation.setFrames(noLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = true;
				noLightKilledStart = System.nanoTime();
			}
		}
		else if(currentAnimation == YELLOWLIGHTALERT) {
			long passedTime = (System.nanoTime() - yellowLightAlertStart) / 1000000;
			double timePassedDecimal = (double) passedTime / (double) yellowLightAlertTime;
			int escapeRangeTotalGrowth = playerEscapeRangeMax - playerEscapeRangeMin;
			playerEscapeRange = (int) (playerEscapeRangeMin + timePassedDecimal * escapeRangeTotalGrowth);
			
			if(passedTime >= yellowLightAlertTime) {
				currentAnimation = GREENLIGHTIDLE;
				animation.setFrames(greenLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = false;
			}
			else if(playerIsInsideEscapeRange()) {
				currentAnimation = REDLIGHTESCAPE;
				animation.setFrames(redLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = false;
				redLightEscapeStart = System.nanoTime();
			}
			else if(health == 0) {
				currentAnimation = KILLEDDESTRUCT;
				animation.setFrames(noLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = true;
				noLightKilledStart = System.nanoTime();
			}
		}
		else if(currentAnimation == REDLIGHTESCAPE) {
			long passedTime = (System.nanoTime() - redLightEscapeStart) / 1000000;
			
			if(passedTime >= redLightEscapeTime) {
				currentAnimation = BURROW;
				animation.setFrames(burrowSprites);
				animation.setDelay(70);
				pacified = true;
				invulnerable = true;
				escaping = true;
			}
			else if(health == 0) {
				currentAnimation = KILLEDDESTRUCT;
				animation.setFrames(noLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = true;
				noLightKilledStart = System.nanoTime();
			}
		}
		else if(currentAnimation == BURROW && animation.hasPlayedOnce()) {
			if(escaping) {
				currentAnimation = TELEPORT;
				animation.setFrames(teleportSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = true;
				escaping = false;
			}
			else {
				currentAnimation = HIDE;
				animation.setFrames(redLightHideSprites);
				animation.setDelay(-1);
				pacified = false;
				invulnerable = true;
				currentOpacity = hidingOpacity;
			}
		}
		else if(currentAnimation == HIDE && triggered) {
			currentAnimation = UNBURROW;
			animation.setFrames(unburrowSprites);
			animation.setDelay(25);
			pacified = true;
			invulnerable = true;
			currentOpacity = visibleOpacity;
			JukeBox.playWithRecommendedVolume("minejump");
		}
		else if(currentAnimation == UNBURROW && animation.hasPlayedOnce()) {
			currentAnimation = JUMPDESTRUCT;
			animation.setFrames(redLightNormalSprites);
			animation.setDelay(-1);
			pacified = true;
			invulnerable = true;
			jumped = false;
			jumpDestructStart = System.nanoTime();
		}
		else if(currentAnimation == JUMPDESTRUCT) {
			long passedTime = (System.nanoTime() - jumpDestructStart) / 1000000;
			
			if(passedTime >= jumpDestructTime) {
				dead = true;
			}
		}
		else if(currentAnimation == KILLEDDESTRUCT) {
			long passedTime = (System.nanoTime() - noLightKilledStart) / 1000000;
			
			if(passedTime >= noLightKilledTime) {
				dead = true;
			}
		}
		else if(currentAnimation == TELEPORT) {
			if(teleportPoint.x == 0 && teleportPoint.y == 0) {
				// teleport point hasn't been found, searching again
				teleportPoint = getSuitableRandomSpawnPoint(false, false);
			}
			else {
				currentAnimation = GREENLIGHTIDLE;
				animation.setFrames(greenLightNormalSprites);
				animation.setDelay(-1);
				pacified = true;
				invulnerable = false;
				setGreenLightIdleTime();
				greenLightIdleStart = System.nanoTime();
				teleportPoint.x = 0;
				teleportPoint.y = 0;
			}
		}
	}

	private boolean playerIsInsideAlertRange() {
		double xDistance = Math.abs(getx() - playerX);
		double yDistance = Math.abs(gety() - playerY);
		if(xDistance <= playerAlertRange && yDistance <= playerAlertRange) return true;
		else return false;
	}
	
	private boolean playerIsInsideEscapeRange() {
		double xDistance = Math.abs(getx() - playerX);
		double yDistance = Math.abs(gety() - playerY);
		if(xDistance <= playerEscapeRange && yDistance <= playerEscapeRange) return true;
		else return false;
	}
	
	public void setTriggered(boolean triggered) {
		this.triggered = triggered;
	}

	private void setGreenLightIdleTime() {
		greenLightIdleTime = Long.valueOf(Support.randInt(
				greenLightIdleTimeMin,
				greenLightIdleTimeMax
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
		
		float drawOpacity = currentOpacity * Support.surroundingsOpacity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawOpacity));
		
		super.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public void hit(int damage) {
		if(dead || flinching) return;
		health -= damage;
		if(health < 0) health = 0;
		if(health > 0) {
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(dead) {
			EntitySpawnData esd = new EntitySpawnData(
				new MineExplosion(tileMap, 0, null),
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
			greenLightNormalSprites = greenLightNormalSpritesC;
			yellowLightNormalSprites = yellowLightNormalSpritesC;
			redLightNormalSprites = redLightNormalSpritesC;
			noLightNormalSprites = noLightNormalSpritesC;
			burrowSprites = burrowSpritesC;
			unburrowSprites = unburrowSpritesC;
			greenLightHideSprites = greenLightHideSpritesC;
			yellowLightHideSprites = yellowLightHideSpritesC;
			redLightHideSprites = redLightHideSpritesC;
			teleportSprites = teleportSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			greenLightNormalSprites = greenLightNormalSpritesM;
			yellowLightNormalSprites = yellowLightNormalSpritesM;
			redLightNormalSprites = redLightNormalSpritesM;
			noLightNormalSprites = noLightNormalSpritesM;
			burrowSprites = burrowSpritesM;
			unburrowSprites = unburrowSpritesM;
			greenLightHideSprites = greenLightHideSpritesM;
			yellowLightHideSprites = yellowLightHideSpritesM;
			redLightHideSprites = redLightHideSpritesM;
			teleportSprites = teleportSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == GREENLIGHTIDLE) {
			animation.swapFrames(greenLightNormalSprites);
		}
		else if(currentAnimation == YELLOWLIGHTALERT) {
			animation.swapFrames(yellowLightNormalSprites);
		}
		else if(currentAnimation == REDLIGHTESCAPE) {
			animation.swapFrames(redLightNormalSprites);
		}
		else if(currentAnimation == BURROW) {
			animation.swapFrames(burrowSprites);
		}
		else if(currentAnimation == HIDE) {
			animation.swapFrames(redLightHideSprites);
		}
		else if(currentAnimation == UNBURROW) {
			animation.swapFrames(unburrowSprites);
		}
		else if(currentAnimation == JUMPDESTRUCT) {
			animation.swapFrames(redLightNormalSprites);
		}
		else if(currentAnimation == KILLEDDESTRUCT) {
			animation.swapFrames(noLightNormalSprites);
		}
		else if(currentAnimation == TELEPORT) {
			animation.swapFrames(teleportSprites);
		}
	}
}









