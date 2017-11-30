package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

//import Support.Support;
import TileMap.TileMap;
import Handlers.Content;

// VERY mimimalistic approach in attempt to optimize the final attacks heavy load
public class BossBall5Flash extends Enemy {

	private BufferedImage[] sprites;
	
	//private BufferedImage[] spritesC;
	
	//private BufferedImage[] spritesM;
	
	//private int currentAnimation;
	
	//private final int FLASHING = 0;
	
	public BossBall5Flash(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		width = 31;
		height = 31;
		
		pacified = true;
		invulnerable = true;
		
		//updateColorMode();
		
		sprites = Content.BossBall5Flash[0];
		
		//spritesC = Content.BossBall5Flash[0];
		
		//spritesM = Content.BossBall5FlashM[1];
		
		//setBufferedImages();
		
		//currentAnimation = FLASHING;
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(25);
	}
	
	/*public void updateCurrentAnimation() {
		if(currentAnimation == FLASHING && animation.hasPlayedOnce()) {
			dead = true;
		}
	}*/
	
	public void update() {
		//checkTileMapCollision();
		//setPosition(xtemp, ytemp);
		
		animation.update();
		
		//updateCurrentAnimation();
		if(animation.hasPlayedOnce()) {
			dead = true;
		}
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		super.draw(g);
	}
	
	/*@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
	}*/

	/*@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			sprites = spritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			sprites = spritesM;
		}
	}*/
	
	/*@Override
	public void swapAnimationFrames() {
		if(currentAnimation == FLASHING) {
			animation.swapFrames(sprites);
		}
	}*/
}










