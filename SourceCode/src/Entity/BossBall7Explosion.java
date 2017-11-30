package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Handlers.Content;

public class BossBall7Explosion extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	// total vector of knockback
	private double knockbackForce;

	private float currentOpacity;
	
	public BossBall7Explosion(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		width = 131;
		height = 131;
		cwidth = 111;
		cheight = 111;
		
		health = maxHealth = 1;
		damage = 13;
		
		currentOpacity = 0.9f;
		
		knockbackForce = 9.0;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.BossBall7Explosion[0];
		
		spritesM = Content.BossBall7ExplosionM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(35);
		
		JukeBox.playWithRecommendedVolume("bossdarkballexplosion");
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
		if(animation.hasPlayedOnce()) dead = true;
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








