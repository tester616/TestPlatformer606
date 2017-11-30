package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall1Extra;
import Handlers.Content;

public class BossBall1 extends Enemy {

	private BufferedImage[] flyingSprites;

	private BufferedImage[] flyingSpritesC;

	private BufferedImage[] flyingSpritesM;
	
	private int currentAnimation;
	
	private final int FLYING = 0;
	
	private long lifeTime;
	private long creationTime;
	
	private long particleInterval;
	private long lastParticleCreationTime;
	
	private boolean createParticle;
	
	public BossBall1(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 3.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 5;
		cheight = 5;
		
		health = maxHealth = 1;
<<<<<<< HEAD
		damage = 9;
		
		lifeTime = 80000;
=======
		damage = 11;
		
		lifeTime = 100000;
>>>>>>> 2.03
		creationTime = System.nanoTime();
		
		particleInterval = 30;
		lastParticleCreationTime = System.nanoTime();
		
		currentAnimation = FLYING;
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		flyingSpritesC = Content.BossBall1[0];
		
		flyingSpritesM = Content.BossBall1M[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(flyingSprites);
		animation.setDelay(55);
		
		setExtraData(mode, extraData);
	}

	private void getNextPosition() {
		if(currentAnimation == FLYING) {
			
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FLYING) {
			long lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
			
			if(lifeTimePassed > lifeTime) {
				dead = true;
			}
		}
	}
	
	private void checkParticleCreationTime() {
		long particleTimePassed = (System.nanoTime() - lastParticleCreationTime) / 1000000;
		
		if(particleTimePassed > particleInterval) {
			createParticle = true;
		}
	}
	
	private void checkOutOfBoundsCollision() {
		// x direction update		
		if(x - cwidth / 2 - 1 < 0) {
			right = true;
			left = false;
		}
		else if(x + cwidth / 2 + 1 > tileMap.getWidth()) {
			right = false;
			left = true;
		}
		// y direction update
		if(y - cheight / 2 - 1 < 0) {
			up = false;
			down = true;
		}
		else if(y + cheight / 2 + 1 > tileMap.getHeight()) {
			up = true;
			down = false;
		}
	}
	
	public void update() {
		checkParticleCreationTime();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		checkOutOfBoundsCollision();
		
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
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(dead) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall1Explosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
		}
		
		if(createParticle) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall1Particle(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createParticle = false;
			lastParticleCreationTime = System.nanoTime();
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			flyingSprites = flyingSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			flyingSprites = flyingSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == FLYING) {
			animation.swapFrames(flyingSprites);
		}
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		BossBall1Extra ed = (BossBall1Extra) extraData;
		dx = ed.dx;
		dy = ed.dy;
	}
}









