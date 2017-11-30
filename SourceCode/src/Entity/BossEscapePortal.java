package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class BossEscapePortal extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;

	private static final int FADEIN = 0;
	private static final int IDLE = 1;
	private static final int FADEOUT = 2;

	private int currentAnimation;

	private float currentOpacity;

	private long animationStart;
	
	private long fadeInTime;
	private float fadeInStartOpacity;
	private float fadeInEndOpacity;
	
	private long idleTime;

	private long fadeOutTime;
	private float fadeOutStartOpacity;
	private float fadeOutEndOpacity;

	
	public BossEscapePortal(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		width = 31;
		height = 31;
		cwidth = 14;
		cheight = 14;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.Portal[0];
		
		spritesM = Content.PortalM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(40);
		
		fadeInTime = 700;
		fadeInStartOpacity = Support.INVISIBLEOPACITY;
		fadeInEndOpacity = Support.NORMALOPACITY;
		
		idleTime = 3000;
		
		fadeOutTime = 700;
		fadeOutStartOpacity = Support.NORMALOPACITY;
		fadeOutEndOpacity = Support.INVISIBLEOPACITY;
		
		currentOpacity = fadeInStartOpacity;
		
		animationStart = System.nanoTime();
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FADEIN) {
			long passedTime = (System.nanoTime() - animationStart) / 1000000;
			
			currentOpacity = Support.getTimeDependantFloat(
				fadeInStartOpacity,
				fadeInEndOpacity,
				fadeInTime,
				passedTime
			);
			
			if(passedTime >= fadeInTime) {
				currentAnimation = IDLE;
				animationStart = System.nanoTime();
			}
		}
		else if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - animationStart) / 1000000;
			
			if(passedTime >= idleTime) {
				currentAnimation = FADEOUT;
				animationStart = System.nanoTime();
			}
		}
		else if(currentAnimation == FADEOUT) {
			long passedTime = (System.nanoTime() - animationStart) / 1000000;
			
			currentOpacity = Support.getTimeDependantFloat(
				fadeOutStartOpacity,
				fadeOutEndOpacity,
				fadeInTime,
				passedTime
			);
			
			if(passedTime >= fadeOutTime) {
				dead = true;
			}
		}
	}
	
	public void update() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
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
}









