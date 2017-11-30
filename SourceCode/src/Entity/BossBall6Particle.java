package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall6ParticleExtra;
import Handlers.Content;

public class BossBall6Particle extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	
	private BossBall6ParticleExtra ed;
	
	public BossBall6Particle(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.BossBall6Particle[0];
		
		spritesM = Content.BossBall6Particle[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
		
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
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
	}

	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
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
		ed = (BossBall6ParticleExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		lifeTime = ed.lifeTime;
	}
}









