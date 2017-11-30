package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall3Extra;
import Handlers.Content;

public class BossBall3 extends Enemy {
	
	private BufferedImage[] flyingSprites;
	private BufferedImage[] lyingSprites;
	
	private BufferedImage[] flyingSpritesC;
	private BufferedImage[] lyingSpritesC;
	
	private BufferedImage[] flyingSpritesM;
	private BufferedImage[] lyingSpritesM;
	
	// modes
	private final int MODE_BIG = 0;
	private final int MODE_SMALL = 1;
	
	// spawn size values
	private final int SIZE_BIG = 31;
	private final int CSIZE_BIG = 13;
	private final int SIZE_SMALL = 11;
	private final int CSIZE_SMALL = 5;
	
	private final int CWIDTH_LYING = 23;

	private final int FLYING_BIG = 0;
	private final int LANDING_BIG = 1;
	private final int LYING_BIG = 2;
	private final int FLYING_SMALL = 3;
	
	private int currentAnimation;
	
	private long lyingTime;
	private long lyingStart;
	
	private float currentOpacity;

	private boolean createSmallBall3;
	
	private BossBall3Extra ed;
	
	private int smallBounceMaxX;
	private int smallBounceMinY;
	private int smallBounceMaxY;
	
	public BossBall3(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		maxSpeed = 10.0;
		//stopSpeed = 0.05;
		fallSpeed = 0.2;
		maxFallSpeed = 4.0;
		
		//width etc. is set in setExtraData
		// so is currentAnimation
		// and pacified & invulnerable
		
		health = maxHealth = 1;
		damage = 0;
		
		lyingTime = 7000;
		
		currentOpacity = 0.8f;
		
		// 150 = 1.5 and so on
		smallBounceMaxX = 230;
		smallBounceMinY = 200;
		smallBounceMaxY = 580;
		
		updateColorMode();
		
		flyingSpritesC = Content.BossBall3[0];
		lyingSpritesC = Content.BossBall3[1];
		
		flyingSpritesM = Content.BossBall3M[0];
		lyingSpritesM = Content.BossBall3M[1];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(flyingSprites);
		animation.setDelay(40);
		
		falling = true;
	}

	private void setSize(int mode) {
		// main big ball
		if(mode == MODE_BIG) {
			width = SIZE_BIG;
			height = SIZE_BIG;
			cwidth = CSIZE_BIG;
			cheight = CSIZE_BIG;
		}
		// small ground split ball
		else if(mode == MODE_SMALL) {
			width = SIZE_SMALL;
			height = SIZE_SMALL;
			cwidth = CSIZE_SMALL;
			cheight = CSIZE_SMALL;
		}
		else {
			System.out.println("Wrong mode (" + this.mode + ") in setSize (BossBall3), defaulting to " + MODE_BIG + " (big)");
			width = SIZE_BIG;
			height = SIZE_BIG;
			cwidth = CSIZE_BIG;
			cheight = CSIZE_BIG;
		}
	}
	
	private void setCurrentAnimation(int mode) {
		if(mode == MODE_BIG) {
			currentAnimation = FLYING_BIG;
		}
		else if(mode == MODE_SMALL) {
			currentAnimation = FLYING_SMALL;
		}
		else {
			System.out.println("Wrong mode (" + this.mode + ") in setCurrentAnimation (BossBall3), defaulting to " + MODE_BIG + " (big)");
			currentAnimation = FLYING_BIG;
		}
	}

	private void setObjectInteraction(int mode) {
		if(mode == MODE_BIG) {
			pacified = false;
			invulnerable = false;
		}
		else if(mode == MODE_SMALL) {
			pacified = true;
			invulnerable = false;
		}
		else {
			System.out.println("Wrong mode (" + this.mode + ") in setCurrentAnimation (BossBall3), defaulting to " + MODE_BIG + " (big)");
			pacified = false;
			invulnerable = false;
		}
	}

	private void getNextPosition() {
		if(currentAnimation == FLYING_BIG) {
			if(falling) {
				dy += fallSpeed;
				if(dy > maxFallSpeed) dy = maxFallSpeed;
			}
		}
		else if(currentAnimation == LANDING_BIG) {
			dx = 0;
			dy = 0;
		}
		else if(currentAnimation == FLYING_SMALL) {
			if(falling) {
				dy += fallSpeed;
				if(dy > maxFallSpeed) dy = maxFallSpeed;
			}
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FLYING_BIG) {
			if(falling == false) {
				currentAnimation = LANDING_BIG;
			}
		}
		else if(currentAnimation == LANDING_BIG) {
			currentAnimation = LYING_BIG;
			animation.setFrames(lyingSprites);
			lyingStart = System.nanoTime();
			invulnerable = true;
			createSmallBall3 = true;
			cwidth = CWIDTH_LYING;
		}
		else if(currentAnimation == LYING_BIG) {
			long passedTime = (System.nanoTime() - lyingStart) / 1000000;
			
			if(passedTime >= lyingTime) {
				dead = true;
			}
		}
		else if(currentAnimation == FLYING_SMALL) {
			if(falling == false) {
				dead = true;
			}
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
				dy = 0;
				ytemp = currRow * tileSize + cheight / 2;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
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
				dx = 0;
				xtemp = currCol * tileSize + cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				xtemp = (currCol + 1) * tileSize - cwidth / 2;
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(createSmallBall3) {
			for (int amount = Support.randInt(1, 3); amount > 0; amount--) {
				double bounceX = Support.getDoubleWithXExtraDecimals(
					Support.randInt(
						smallBounceMaxX * (-1),
						smallBounceMaxX
					),
					2
				);
				double bounceY = Support.getDoubleWithXExtraDecimals(
					Support.randInt(
						smallBounceMinY,
						smallBounceMaxY
					),
					2
				) * (-1);
				BossBall3Extra ed = new BossBall3Extra(bounceX, bounceY);
				EntitySpawnData esd = new EntitySpawnData(
					new BossBall3(tileMap, MODE_SMALL, ed),
					getx(),
					gety()
				);
				esdList.add(esd);
			}
			
			createSmallBall3 = false;
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			flyingSprites = flyingSpritesC;
			lyingSprites = lyingSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			flyingSprites = flyingSpritesM;
			lyingSprites = lyingSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == FLYING_BIG) {
			animation.swapFrames(flyingSprites);
		}
		else if(currentAnimation == LYING_BIG) {
			animation.swapFrames(lyingSprites);
		}
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		ed = (BossBall3Extra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		
		setSize(this.mode);
		
		setCurrentAnimation(this.mode);
		
		setObjectInteraction(this.mode);
	}
}









