package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

public class LifeShine extends Collectible {

	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	private int currentAnimation;
	
	private final int FLASHING = 0;
	
	public LifeShine(TileMap tm) {
		
		super(tm);
		
		width = 31;
		height = 31;
		//cwidth = 9;
		//cheight = 9;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.LifeShine[0];
		
		spritesM = Content.LifeShineM[0];
		
		setBufferedImages();
		
		currentAnimation = FLASHING;
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(80);
	}
	
	private void getNextPosition() {
		if(currentAnimation == FLASHING) {
			// nothing needed
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == FLASHING && animation.hasPlayedOnce()) {
			dead = true;
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
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
		if(currentAnimation == FLASHING) {
			animation.swapFrames(sprites);
		}
	}
}










