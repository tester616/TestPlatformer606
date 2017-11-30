package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;

import TileMap.TileMap;
import Audio.JukeBox;
import EntityExtraData.AttackWeakExtra;
import Handlers.Content;

public class PlayerAttackWeak extends Ally {

	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	private double scale;
	private double minScale;
	private double maxScale;
	private int originalWidth;
	private int originalHeight;
	private int originalCwidth;
	private int originalCheight;

	private long lifeTimePassed;

	private double fadeStartDecimal;
	private float currentOpacity;
	private float originalOpacity;
	
	public PlayerAttackWeak(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 8;
		cheight = 21;
		
		originalWidth = width;
		originalHeight = height;
		originalCwidth = cwidth;
		originalCheight = cheight;

		minScale = 1.0;
		maxScale = 3.0;
		if(Support.cheatAttackRange) maxScale *= 3;
		scale = minScale;
		
		health = maxHealth = 1;
		damage = 1;
		if(Support.cheatAttackDamage) damage *= 3;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.AttackWeak[0];
		
		spritesM = Content.AttackWeak[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
		lifeTime = 170;
		creationTime = System.nanoTime();
		
		fadeStartDecimal = 0.9;
		originalOpacity = (float) 1.0;
		
		setExtraData(mode, extraData);
		
		JukeBox.playWithRecommendedVolume("playerweakattack");
	}
	
	private void getNextPosition() {
		x += dx;
		y += dy;
	}

	private void setLifeTimePassed() {
		lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
	}
	
	private void checkLifeTime() {
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}

	public void setCurrentOpacity(float currentOpacity) {
		this.currentOpacity = currentOpacity;
	}

	private void setScaledValues() {
		scale = minScale + (maxScale - minScale) * ((double) lifeTimePassed / (double) lifeTime);
		width = (int) (originalWidth * scale);
		height = (int) (originalHeight * scale);
		cwidth = (int) (originalCwidth * scale);
		cheight = (int) (originalCheight * scale);
	}
	
	// currentOpacity
	private void setFadeableValues() {
		double lifeTimePassedOfTotalInDecimal = (double) lifeTimePassed / (double) lifeTime;
		double lifeTimeLeftOfTotalDecimal = 1.0 - lifeTimePassedOfTotalInDecimal;
		if(lifeTimeLeftOfTotalDecimal <= fadeStartDecimal) {
			double lifeTimePassedAtFadeStartDecimal = 1.0 - fadeStartDecimal;

			long lifeTimeLastPart = (long) (lifeTime * fadeStartDecimal);
			long lifeTimePassedOfLastPart = (long) (lifeTimePassed - (lifeTime * lifeTimePassedAtFadeStartDecimal));
			
			double lifeTimePassedOfLastPartInDecimal = (double) lifeTimePassedOfLastPart / (double) lifeTimeLastPart;
			float fadedOpacity = (float) (originalOpacity - (originalOpacity * lifeTimePassedOfLastPartInDecimal));
			if(fadedOpacity < 0.0) fadedOpacity = (float) 0.0;

			setCurrentOpacity(fadedOpacity);
		}
		else {
			setCurrentOpacity(originalOpacity);
		}
	}
	
	public void update() {
		setLifeTimePassed();
		
		setFadeableValues();
		
		checkLifeTime();
		
		setScaledValues();
		
		// update position
		getNextPosition();
		//checkTileMapCollision();
		//setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}
	
	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		float drawOpacity = currentOpacity * Support.surroundingsOpacity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawOpacity));
		
		super.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		AttackWeakExtra ed = (AttackWeakExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		facingRight = ed.facingRight;
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
	public void checkTileMapCollision() {
/*
		
		double decimalCurrCol = x / tileSize;
		double decimalCurrRow = y / tileSize;
		
		// (int) conversion drops decimals, making both -0.99 and 0.99 to 0, so the -1 row correction must happen while the values have decimals
		if(decimalCurrCol <= 0) currCol = (int) decimalCurrCol - 1;
		else currCol = (int) decimalCurrCol;
		if(decimalCurrRow <= 0) currRow = (int) decimalCurrRow - 1;
		else currRow = (int) decimalCurrRow;
		
		xdest = x + dx;
		ydest = y + dy;
		//System.out.println("xtemp at beginning "+xtemp);
		xtemp = x;
		//System.out.println("xtemp at beginning 2 (took x value) "+xtemp);
		ytemp = y;
		
		// wall touch resets
		touchingLeftWall = false;
		touchingRightWall = false;
		
		calculateCorners(x, ydest);
		//System.out.println(topLeft+" "+topRight+" "+bottomLeft+" "+bottomRight);
		//System.out.println("omfg "+currRow+" "+currCol);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
				//System.out.println("top collision");
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				//System.out.println(y+" "+dy+" "+(y + dy));
				dy = 0;
				falling = false;
				ytemp = (currRow + 1) * tileSize - cheight / 2;
				//System.out.println("bottom collision");
				//System.out.println(y+" "+ytemp);
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
				touchingLeftWall = true;
				//System.out.println("left collision");
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
			}
			else {
				xtemp += dx;
			}
		}*/
	}
}









