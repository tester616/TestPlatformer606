package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Handlers.Content;

public class BossExplosion extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	public BossExplosion(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		width = 131;
		height = 131;
		cwidth = 95;
		cheight = 95;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.MineExplosion[0];
		
		spritesM = Content.MineExplosionM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(30);
		
		JukeBox.playWithRecommendedVolume("bossdeath");
	}
	
	@Override
	public void hit(int damage) {
		
	}

	private void updateCurrentAnimation() {
		if(animation.hasPlayedOnce()) {
			dead = true;
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








