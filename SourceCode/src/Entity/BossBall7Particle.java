package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall7ParticleExtra;
import Handlers.Content;

public class BossBall7Particle extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private long lifeTime;
	private long creationTime;
	
	private BossBall7ParticleExtra ed;
	
	private double accelerationX;
	private double accelerationY;
	
	public BossBall7Particle(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		maxSpeed = 0.9;
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = false;
		
		updateColorMode();
		
		spritesC = Content.BossBall7Particle[0];
		
		spritesM = Content.BossBall7ParticleM[0];
		
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

	private void updateSpeed() {
		dx += accelerationX;
		if(Math.abs(dx) > maxSpeed) dx = maxSpeed;
		dy += accelerationY;
		if(Math.abs(dy) > maxSpeed) dy = maxSpeed;
	}
	
	public void update() {
		
		checkLifeTime();
		
		updateSpeed();
		
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
		ed = (BossBall7ParticleExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		accelerationX = ed.accelerationX;
		accelerationY = ed.accelerationY;
		lifeTime = ed.lifeTime;
	}
}









