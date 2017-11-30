package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.SquareWallExtra;
import Handlers.Content;

public class SquareWall extends Enemy {
	
	// not really needed for anything
	private BufferedImage[] sprites;
	
	private BufferedImage[] spritesC;
	
	private BufferedImage[] spritesM;
	
	public static final int MODE_X = 0;
	public static final int MODE_Y = 1;
	
	// in some cases a little extra own knockback can be necessary, keep this value low
	private double knockbackStrength;
	// 1 for no velocity loss, 0 for superglue
	private double ricochetStrength;
	
	private int currentAnimation;
	
	private final int IDLE = 0;
	
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	private long idleTime;
	private long idleStart;
	
	public SquareWall(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = Math.abs(startX - endX);
		if(width == 0) width = 1;
		height = Math.abs(startY - endY);
		if(height == 0) height = 1;
		cwidth = width;
		cheight = height;
		
		health = maxHealth = 3;
		damage = 0;
		
		pacified = false;
		invulnerable = false;
		
		knockbackStrength = 0.9;
		ricochetStrength = 0.7;
		
		idleTime = 5000;
		idleStart = System.nanoTime();
		
		updateColorMode();
		
		spritesC = Content.Invisible[0];
		
		spritesM = Content.Invisible[0];
		
		setBufferedImages();
		
		currentAnimation = IDLE;
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}

	public int getMode() {
		return mode;
	}

	public double getRicochetStrength() {
		return ricochetStrength;
	}

	public double getKnockbackStrength() {
		return knockbackStrength;
	}
	
	private void getNextPosition() {
		if(currentAnimation == IDLE) {
			
		}
	}

	private void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - idleStart) / 1000000;
			
			if(passedTime >= idleTime) {
				dead = true;
			}
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
		
		//super.draw(g);
		
		Color origColor = g.getColor();
		g.setColor(Color.WHITE);
		
		g.drawLine(
			(int) (startX + xmap),
			(int) (startY + ymap),
			(int) (endX + xmap),
			(int) (endY + ymap)
		);
		
		// reset original color
		g.setColor(origColor);
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		return esdList;
	}
	
	@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
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
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		SquareWallExtra ed = (SquareWallExtra) extraData;
		startX = ed.startX;
		startY = ed.startY;
		endX = ed.endX;
		endY = ed.endY;
	}
}










