package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Audio.JukeBox;
import Handlers.Content;
import Support.Support;
import TileMap.TileMap;

public class Explosion extends Enemy {
	
	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	public Explosion(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		 
		width = 31;
		height = 31;
		
		invulnerable = true;
		pacified = true;
		
		spritesC = Content.EnemyExplosion[0];
		
		spritesM = Content.EnemyExplosionM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(70);
		
		JukeBox.playWithRecommendedVolume("enemydeath");
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




















