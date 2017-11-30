package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Handlers.Content;

public class MineExplosion extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// total vector of knockback
	private double knockbackForce;
	
	public MineExplosion(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 131;
		height = 131;
		cwidth = 95;
		cheight = 95;
		
		health = maxHealth = 1;
		damage = 25;
		
		knockbackForce = 9.0;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.MineExplosion[0];
		
		spritesM = Content.MineExplosionM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(30);
		
		JukeBox.playWithRecommendedVolume("mineexplosion");
	}
	
	@Override
	public void hit(int damage) {
		
	}
	
	private void getNextPosition() {
		
	}
	
	public double getKnockbackForce() {
		return knockbackForce;
	}

	private void updateCurrentAnimation() {
		if(animation.hasPlayedOnce()) {
			dead = true;
		}
	}
	
	public void update() {
		
		updateCurrentAnimation();
		
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








