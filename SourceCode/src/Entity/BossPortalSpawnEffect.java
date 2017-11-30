package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class BossPortalSpawnEffect extends Enemy {

	public static final int PORTAL1 = 1;
	public static final int PORTAL2 = 2;
	public static final int PORTAL3 = 3;
	public static final int TURRET = 4;

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	
	private int drawXPositionChange;
	private int drawYPositionChange;
	private boolean drawMirrored;
	private boolean drawUpsideDown;
	
	public BossPortalSpawnEffect(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 61;
		height = 61;
		cwidth = 45;
		cheight = 45;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		if(this.mode == PORTAL1) {
			spritesC = Content.BossPortal1[1];
			
			spritesM = Content.BossPortal1M[1];
		}
		else if(this.mode == PORTAL2) {
			spritesC = Content.BossPortal2[1];
			
			spritesM = Content.BossPortal2M[1];
		}
		else if(this.mode == PORTAL3) {
			spritesC = Content.BossPortal3[1];
			
			spritesM = Content.BossPortal3M[1];
		}
		else if(this.mode == TURRET) {
			spritesC = Content.Portal[1];
			
			spritesM = Content.PortalM[1];
			
			width = 31;
			height = 31;
			cwidth = 15;
			cheight = 15;
		}
		else {
			spritesC = Content.BossPortal1[1];
			
			spritesM = Content.BossPortal1M[1];
			
			System.out.println("Wrong mode (" + this.mode + ") in BossPortalSpawnEffect, picking default color.");
		}
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(30);
		
		lifeTime = 2000;
		creationTime = System.nanoTime();
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
		
		setRandomDrawValues();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	private void setRandomDrawValues() {
		drawXPositionChange = Support.randInt(0, 6) - 3;
		drawYPositionChange = Support.randInt(0, 6) - 3;
		if(Support.randInt(0, 1) == 1) drawMirrored = true;
		else drawMirrored = false;
		if(Support.randInt(0, 1) == 1) drawUpsideDown = true;
		else drawUpsideDown = false;
	}

	public void draw(Graphics2D g) {
		int xStart;
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
		}
		
		setMapPosition();
		
		g.drawImage(
			animation.getImage(),
			xStart,
			yStart,
			xWidth,
			yHeight,
			null
		);
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









