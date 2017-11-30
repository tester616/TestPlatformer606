package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import TileMap.TileMap;
import EntityExtraData.PlayerPieceExtra;
import Handlers.Content;

public class PlayerPiece extends Ally {

	private BufferedImage[] sprites;
	
	public PlayerPiece(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);

		moveSpeed = 0.3;
		maxSpeed = 3.0;
		fallSpeed = 0.10;
		maxFallSpeed = 4.0;
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		falling = true;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = true;
		invulnerable = true;
		
		setExtraData(mode, extraData);
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	@Override
	public void hit(int damage) {
		
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
		
		if(falling) {
			dy += fallSpeed;
			if(dy > maxFallSpeed) dy = maxFallSpeed;
		}
	}

	public void update() {
		
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
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		PlayerPieceExtra ed = (PlayerPieceExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		
		// perfect code.
		if(mode == 1) sprites = Content.PlayerPiece1[0];
		else if(mode == 2) sprites = Content.PlayerPiece2[0];
		else if(mode == 3) sprites = Content.PlayerPiece3[0];
		else if(mode == 4) sprites = Content.PlayerPiece4[0];
		else if(mode == 5) sprites = Content.PlayerPiece5[0];
		else if(mode == 6) sprites = Content.PlayerPiece6[0];
		else if(mode == 7) sprites = Content.PlayerPiece7[0];
		else if(mode == 8) sprites = Content.PlayerPiece8[0];
		else if(mode == 9) sprites = Content.PlayerPiece9[0];
		else if(mode == 10) sprites = Content.PlayerPiece10[0];
		else if(mode == 11) sprites = Content.PlayerPiece11[0];
		else if(mode == 12) sprites = Content.PlayerPiece12[0];
		else if(mode == 13) sprites = Content.PlayerPiece13[0];
		else if(mode == 14) sprites = Content.PlayerPiece14[0];
		else if(mode == 15) sprites = Content.PlayerPiece15[0];
		else if(mode == 16) sprites = Content.PlayerPiece16[0];
		else if(mode == 17) sprites = Content.PlayerPiece17[0];
		else if(mode == 18) sprites = Content.PlayerPiece18[0];
		else if(mode == 19) sprites = Content.PlayerPiece19[0];
		else if(mode == 20) sprites = Content.PlayerPiece20[0];
		else if(mode == 21) sprites = Content.PlayerPiece21[0];
		else if(mode == 22) sprites = Content.PlayerPiece22[0];
		else if(mode == 23) sprites = Content.PlayerPiece23[0];
		else if(mode == 24) sprites = Content.PlayerPiece24[0];
		else if(mode == 25) sprites = Content.PlayerPiece25[0];
		else if(mode == 26) sprites = Content.PlayerPiece26[0];
		else if(mode == 27) sprites = Content.PlayerPiece27[0];
		else if(mode == 28) sprites = Content.PlayerPiece28[0];
		else if(mode == 29) sprites = Content.PlayerPiece29[0];
		else if(mode == 30) sprites = Content.PlayerPiece30[0];
		else if(mode == 31) sprites = Content.PlayerPiece31[0];
		else {
			sprites = Content.PlayerPiece1[0];
			System.out.println("Wrong mode for PlayerPiece " + mode);
		}
	}

	@Override
	public void setBufferedImages() {}
	
	@Override
	public void swapAnimationFrames() {}
}









