package Entity.Enemies;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Entity.*;
import Handlers.Content;

public class Halfmoon extends Enemy {

	private BufferedImage[] chaseSprites;

	private BufferedImage[] chaseSpritesC;

	private BufferedImage[] chaseSpritesM;
	
	private double playerX;
	private double playerY;
	
	private double totalSpeedVector;
	
	private double homingStrength;
	
	private double shadowX;
	private double shadowY;
	
	private int currentAnimation;

	private float swapLineOpacity;

	private long animationStart;
	private long animationTime;

	private int chaseTimeMin;
	private int chaseTimeMax;

	private int swapChargeTimeMin;
	private int swapChargeTimeMax;

	private float swapChargeOpacityMin;
	private float swapChargeOpacityMax;
	
	private static final int CHASE = 0;
	private static final int SWAPCHARGE = 1;
	private static final int SWAP = 2;
	
	public Halfmoon(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 1.5;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		width = 31;
		height = 31;
		cwidth = 13;
		cheight = 13;
		
		health = maxHealth = 1;
		damage = 10;
		
		cost = Support.COST_HALFMOON;
		
		chaseTimeMin = 5000;
		chaseTimeMax = 30000;
		
		swapChargeTimeMin = 1600;
		swapChargeTimeMax = 1600;

		swapChargeOpacityMin = (float) 0.0;
		swapChargeOpacityMax = (float) 0.35;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		chaseSpritesC = Content.Halfmoon[0];
		
		chaseSpritesM = Content.HalfmoonM[0];
		
		//chaseSpritesM = Content.convertToGrayScale(chaseSpritesC);
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(chaseSprites);
		animation.setDelay(35);
		
		currentAnimation = CHASE;
		animationTime = Long.valueOf(Support.randInt(
				chaseTimeMin,
				chaseTimeMax
		));
		animationStart = System.nanoTime();
		
		speedMultiplier = 1.0;
		homingStrength = -0.9;
		
		shadowX = x;
		shadowY = y;
	}
	
	private void getNextPosition() {
		if(currentAnimation == CHASE || currentAnimation == SWAPCHARGE) {
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
		else if(currentAnimation == SWAP) {
			dx = 0;
			dy = 0;
			x = shadowX;
			y = shadowY;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == CHASE) {
			long passedTime = (System.nanoTime() - animationStart) / 1000000;
			
			if(passedTime >= animationTime) {
				currentAnimation = SWAPCHARGE;
				animationTime = Long.valueOf(Support.randInt(
						swapChargeTimeMin,
						swapChargeTimeMax
				));
				animationStart = System.nanoTime();
				swapLineOpacity = swapChargeOpacityMin;
			}
		}
		else if(currentAnimation == SWAPCHARGE) {
			long passedTime = (System.nanoTime() - animationStart) / 1000000;
			
			swapLineOpacity = (float) (swapChargeOpacityMin + (swapChargeOpacityMax - swapChargeOpacityMin) * ((double) passedTime / (double) animationTime));
			
			if(passedTime >= animationTime) {
				if(isInSwappablePosition()) {
					currentAnimation = SWAP;
					swapLineOpacity = Support.NORMALOPACITY;
				}
				else {
					currentAnimation = CHASE;
					animationTime = Long.valueOf(Support.randInt(
							chaseTimeMin,
							chaseTimeMax
					));
					animationStart = System.nanoTime();
					swapLineOpacity = Support.NORMALOPACITY;
				}
			}
		}
		else if(currentAnimation == SWAP) {
			currentAnimation = CHASE;
			animationTime = Long.valueOf(Support.randInt(
					chaseTimeMin,
					chaseTimeMax
			));
			animationStart = System.nanoTime();
		}
	}
	
	private boolean isInSwappablePosition() {
		// check shadow is within map bounds
		if(
			shadowX - cwidth / 2 - 1 > 0 &&
			shadowX + cwidth / 2 + 1 < tileMap.getWidth() &&
			shadowY - cheight / 2 - 1 > 0 &&
			shadowY + cheight / 2 + 1 < tileMap.getHeight()
		) {
			// check shadow is not inside a wall
			calculateCorners(shadowX, shadowY);
			if(!(topLeft || topRight || bottomLeft || bottomRight)) {
				return true;
			}
		}
		return false;
	}

	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		setShadowPosition();
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > 400) {
				flinching = false;
			}
		}
		
		animation.update();
		
		updateCurrentAnimation();
	}

	private void setShadowPosition() {
		double distanceX = playerX - x;
		double distanceY = playerY - y;
		shadowX = playerX + distanceX;
		shadowY = playerY + distanceY;
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		// draw halfmoon
		super.draw(g);
		
		// draw shadow
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(shadowX + xmap - width / 2),
				(int)(shadowY + ymap - height / 2),
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(shadowX + xmap - width / 2 + width),
				(int)(shadowY + ymap - height / 2),
				-width,
				height,
				null
			);
		}
		
		// draw swap line if needed
		if(currentAnimation == SWAPCHARGE || currentAnimation == SWAP) {
			// prepare opacity and color of g to draw swap line
			swapLineOpacity *= Support.surroundingsOpacity;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, swapLineOpacity));
			Color origColor = g.getColor();
			if(currentAnimation == SWAPCHARGE) {
				g.setColor(Color.GRAY);
			}
			else if(currentAnimation == SWAP) {
				g.setColor(Color.WHITE);
			}
			
			// draw swap line
			g.drawLine((int) (x + xmap), (int) (y + ymap), (int) (shadowX + xmap), (int) (shadowY + ymap));
			
			// reset original g values
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
			g.setColor(origColor);
		}
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
		
		touchingLeftWall = false;
		touchingRightWall = false;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy *= -0.6;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy *= -0.6;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx *= -0.6;
				xtemp = currCol * tileSize + cwidth / 2;
				touchingLeftWall = true;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx *= -0.6;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
				touchingRightWall = true;
			}
			else {
				xtemp += dx;
			}
		}
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






