package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

// purely visual
public class ParryShield extends Ally {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	//private BufferedImage[] spritesM;

	private double playerX;
	private double playerY;
	
	private float currentOpacity;
	
	public ParryShield(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		maxSpeed = 0.0;
		fallSpeed = 0.0;
		maxFallSpeed = 0.0;
		
		width = 31;
		height = 31;
		cwidth = 15;
		cheight = 15;
		
		health = maxHealth = 1;
		damage = 0;
		
		facingRight = true;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		spritesC = Content.ParryShield[0];
		
		//spritesM = Content.ParryShieldM[1];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(14);
		
		currentOpacity = Support.TRANSPARENT;
	}
	
	@Override
	public void hit(int damage) {
		
	}
	
	private void getNextPosition() {
		x = playerX;
		y = playerY;
	}

	private void updateCurrentAnimation() {
		if(animation.hasPlayedOnce()) dead = true;
	}
	
	public void update() {
		
		updateCurrentAnimation();
		//checkLifeTime();
		
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
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			sprites = spritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			sprites = spritesC;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		animation.swapFrames(sprites);
	}
}









