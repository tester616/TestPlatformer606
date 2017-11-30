package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossTurretGemExtra;
import Handlers.Content;

public class BossTurretGemRed extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;

	private int animDelayMin;
	private int animDelayMax;

	private long lifeTime;
	private long creationTime;
	
	public BossTurretGemRed(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 46;
		height = 46;
		cwidth = 5;
		cheight = 5;
		
		health = maxHealth = 1;
		damage = 9;
		
		animDelayMin = 30;
		animDelayMax = 60;
		
		lifeTime = 15000;
		creationTime = System.nanoTime();
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.SentryGemRed[0];
		
		spritesM = Content.SentryGemRedM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(Support.randInt(animDelayMin, animDelayMax));
	}
	
	private void checkLifeTime() {
		long lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
		
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}
	
	public void update() {
		
		// update position
		checkLifeTime();
		checkTileMapCollision();
		checkOutOfBounds();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}

	private void checkOutOfBounds() {
		// just simple checks with minimum math and processing time
		if(x < -50) dead = true;
		if(x > tileMap.getWidth() + 50) dead = true;
		if(y < -50) dead = true;
		if(y > tileMap.getHeight() + 50) dead = true;
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
		
		calculateCorners(x, ydest);
		
		if(dy < 0) {
			if(topLeft || topRight) {
				dy *= -1;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy *= -1;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx *= -1;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx *= -1;
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
		BossTurretGemExtra ed = (BossTurretGemExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
	}
}









