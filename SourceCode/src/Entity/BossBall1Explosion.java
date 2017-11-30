package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Handlers.Content;
import Support.Support;
import TileMap.TileMap;

public class BossBall1Explosion extends Enemy {
	
	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	public BossBall1Explosion(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		 
		width = 31;
		height = 31;
		
		cwidth = 5;
		cheight = 5;
		
		invulnerable = true;
		pacified = true;
		
		spritesC = Content.BossBall1[2];
		
		spritesM = Content.BossBall1M[2];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
	}
	
	public void update() {
		animation.update();
		if(animation.hasPlayedOnce()) {
			dead = true;
		}
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
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




















