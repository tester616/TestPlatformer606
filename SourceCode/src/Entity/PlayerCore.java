package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.PlayerCoreExtra;
import Handlers.Content;

public class PlayerCore extends Ally {

	private BufferedImage[] flyingSprites;
	private BufferedImage[] disappearingSprites;

	private BufferedImage[] flyingSpritesC;
	private BufferedImage[] disappearingSpritesC;

	private BufferedImage[] flyingSpritesM;
	private BufferedImage[] disappearingSpritesM;
	
	private final int FLYING = 0;
	private final int DISAPPEARING = 1;
	
	private int currentAnimation;
	
	private long flyingStart;
	private long flyingTime;
	
	private PlayerCoreExtra ed;
	
	public PlayerCore(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		stopSpeed = 0.03;
		
		width = 31;
		height = 31;
		cwidth = 3;
		cheight = 3;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		currentAnimation = FLYING;
		
		updateColorMode();
		
		flyingSpritesC = Content.PlayerCore[0];
		disappearingSpritesC = Content.PlayerCore[1];
		
		flyingSpritesM = Content.PlayerCore[0];
		disappearingSpritesM = Content.PlayerCore[1];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(flyingSprites);
		animation.setDelay(35);
		
		flyingTime = 900;
		flyingStart = System.nanoTime();
	}
	
	private void getNextPosition() {
		if(dx > 0) {
			dx -= stopSpeed;
			if(dx < 0) {
				dx = 0;
			}
		}
		else if(dx < 0) {
			dx += stopSpeed;
			if(dx > 0) {
				dx = 0;
			}
		}
		if(dy > 0) {
			dy -= stopSpeed;
			if(dy < 0) {
				dy = 0;
			}
		}
		else if(dy < 0) {
			dy += stopSpeed;
			if(dy > 0) {
				dy = 0;
			}
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FLYING) {
			long passedTime = (System.nanoTime() - flyingStart) / 1000000;
			
			if(passedTime >= flyingTime) {
				currentAnimation = DISAPPEARING;
				animation.setFrames(disappearingSprites);
				animation.setDelay(50);
				dx = 0.0;
				dy = 0.0;
			}
		}
		else if(currentAnimation == DISAPPEARING && animation.hasPlayedOnce()) {
			dead = true;
		}
	}
	
	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
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
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			flyingSprites = flyingSpritesC;
			disappearingSprites = disappearingSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			flyingSprites = flyingSpritesM;
			disappearingSprites = disappearingSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == FLYING) {
			animation.swapFrames(flyingSprites);
		}
		else if(currentAnimation == DISAPPEARING) {
			animation.swapFrames(disappearingSprites);
		}
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		ed = (PlayerCoreExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
	}
}









