package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import TileMap.TileMap;
import EntityExtraData.BossGemPieceExtra;
import Handlers.Content;

public class BossGemPiece extends Enemy {

	private BufferedImage[] sprites;
	
	public BossGemPiece(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);

		moveSpeed = 0.3;
		maxSpeed = 3.0;
		fallSpeed = 0.10;
		maxFallSpeed = 4.0;
		
		width = 31;
		height = 31;
		cwidth = 5;
		cheight = 5;
		
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
	
	/*@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
	}*/
	
	@Override
	public void checkTileMapCollision() {
		
		double decimalCurrCol = x / tileSize;
		double decimalCurrRow = y / tileSize;
		
		if(decimalCurrCol <= 0) currCol = (int) decimalCurrCol - 1;
		else currCol = (int) decimalCurrCol;
		if(decimalCurrRow <= 0) currRow = (int) decimalCurrRow - 1;
		else currRow = (int) decimalCurrRow;
		
		xdest = x + dx;
		ydest = y + dy;
		
		xtemp = x;
		ytemp = y;
		
		touchingLeftWall = false;
		touchingRightWall = false;
		
		calculateCorners(x, ydest);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				dx = 0;
				
				if(cheight / 2 >= tileSize) {
					int hitRow = currRow - (cheight / 2) / tileSize;
					ytemp = hitRow * tileSize + cheight / 2;
				}
				else {
					ytemp = currRow * tileSize + cheight / 2;
				}
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				dx = 0;
				
				falling = false;
				
				if(cheight / 2 >= tileSize) {
					int hitRow = currRow + (cheight / 2) / tileSize;
					ytemp = (hitRow + 1) * tileSize - cheight / 2;
				}
				else {
					ytemp = (currRow + 1) * tileSize - cheight / 2;
				}
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				touchingLeftWall = true;
				if(cwidth / 2 >= tileSize) {
					int hitCol = currCol - (cwidth / 2) / tileSize;
					xtemp = hitCol * tileSize + cwidth / 2;
				}
				else {
					xtemp = currCol * tileSize + cwidth / 2;
				}
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				touchingRightWall = true;
				if(cwidth / 2 >= tileSize) {
					int hitCol = currCol + (cwidth / 2) / tileSize;
					xtemp = (hitCol + 1) * tileSize - cwidth / 2;
				}
				else {
					xtemp = (currCol + 1) * tileSize - cwidth / 2;
				}
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		BossGemPieceExtra ed = (BossGemPieceExtra) extraData;
		dx = ed.dx;
		dy = ed.dy;
		
		// perfect code.
		if(mode == 1) sprites = Content.BossGemPiece1[0];
		else if(mode == 2) sprites = Content.BossGemPiece2[0];
		else if(mode == 3) sprites = Content.BossGemPiece3[0];
		else if(mode == 4) sprites = Content.BossGemPiece4[0];
		else if(mode == 5) sprites = Content.BossGemPiece5[0];
		else if(mode == 6) sprites = Content.BossGemPiece6[0];
		else if(mode == 7) sprites = Content.BossGemPiece7[0];
		else if(mode == 8) sprites = Content.BossGemPiece8[0];
		else if(mode == 9) sprites = Content.BossGemPiece9[0];
		else if(mode == 10) sprites = Content.BossGemPiece10[0];
		else if(mode == 11) sprites = Content.BossGemPiece11[0];
		else if(mode == 12) sprites = Content.BossGemPiece12[0];
		else if(mode == 13) sprites = Content.BossGemPiece13[0];
		else if(mode == 14) sprites = Content.BossGemPiece14[0];
		else if(mode == 15) sprites = Content.BossGemPiece15[0];
		else if(mode == 16) sprites = Content.BossGemPiece16[0];
		else if(mode == 17) sprites = Content.BossGemPiece17[0];
		else if(mode == 18) sprites = Content.BossGemPiece18[0];
		else {
			sprites = Content.BossGemPiece1[0];
			System.out.println("Wrong mode for BossPiece " + mode);
		}
	}

	@Override
	public void setBufferedImages() {}
	
	@Override
	public void swapAnimationFrames() {}
}









