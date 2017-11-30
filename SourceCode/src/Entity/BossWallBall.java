package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class BossWallBall extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	
	private int maxRandomSpeed;
	
	/*private int drawXPositionChange;
	private int drawYPositionChange;
	private boolean drawMirrored;
	private boolean drawUpsideDown;*/
	
	public BossWallBall(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		// 1k is 1.0
		maxRandomSpeed = 900;
		
		setSpeed();
		
		updateColorMode();
		
		spritesC = Content.BossWallBall[0];
		
		spritesM = Content.BossWallBallM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(50);
		
		lifeTime = 2000;
		creationTime = System.nanoTime();
	}
	
	private void setSpeed() {
		dx = Support.getDoubleWithXExtraDecimals(Support.randInt(0, maxRandomSpeed * 2), 3) - Support.getDoubleWithXExtraDecimals(maxRandomSpeed, 3);
		dy = Support.getDoubleWithXExtraDecimals(Support.randInt(0, maxRandomSpeed * 2), 3) - Support.getDoubleWithXExtraDecimals(maxRandomSpeed, 3);
	}

	@Override
	public void hit(int damage) {
		
	}
	
	private void getNextPosition() {
		
	}
	
	private void checkLifeTime() {
		
		long lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
		
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}
	
	public void update() {
		checkLifeTime();
		
		//setRandomDrawValues();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	/*private void setRandomDrawValues() {
		drawXPositionChange = Support.randInt(0, 6) - 3;
		drawYPositionChange = Support.randInt(0, 6) - 3;
		if(Support.randInt(0, 1) == 1) drawMirrored = true;
		else drawMirrored = false;
		if(Support.randInt(0, 1) == 1) drawUpsideDown = true;
		else drawUpsideDown = false;
	}*/

	public void draw(Graphics2D g) {
		/*int xStart;
		int yStart;
		int xWidth;
		int yHeight;
		
		if(!drawMirrored) {
			xStart = (int)(x + xmap - width / 2 + drawXPositionChange);
			xWidth = width;
		}
		else {
			xStart = (int)(x + xmap - width / 2 + width + drawXPositionChange);
			xWidth = -width;
		}
		if(!drawUpsideDown) {
			yStart = (int)(y + ymap - height / 2 + drawYPositionChange);
			yHeight = width;
		}
		else {
			yStart = (int)(y + ymap - height / 2 + height + drawYPositionChange);
			yHeight = -height;
		}*/
		
		setMapPosition();
		
		super.draw(g);
		
		/*g.drawImage(
			animation.getImage(),
			xStart,
			yStart,
			xWidth,
			yHeight,
			null
		);*/
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
	}
}









