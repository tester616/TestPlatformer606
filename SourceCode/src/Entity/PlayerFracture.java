package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import EntityExtraData.PlayerPieceExtra;
import Handlers.Content;

public class PlayerFracture extends Ally {

	private BufferedImage[] shutdownIdleSprites;
	private BufferedImage[] fractureSprites;
	private BufferedImage[] fracturedSprites;

	private static final int SHUTDOWNIDLE = 0;
	private static final int FRACTURE = 1;
	private static final int FRACTUREIDLE = 2;

	private int currentAnimation;

	private long shutdownIdleStart;
	private long shutdownIdleTime;

	private long fractureIdleStart;
	private long fractureIdleTime;
	private boolean spawnPlayerPieces;
	
	public PlayerFracture(TileMap tm, int mode, Object extraData) {
		
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
		
		pacified = true;
		invulnerable = true;
		
		shutdownIdleSprites = Content.PlayerFracture[0];
		fractureSprites = Content.PlayerFracture[1];
		fracturedSprites = Content.PlayerFracture[2];
		
		animation = new Animation();
		animation.setFrames(shutdownIdleSprites);
		animation.setDelay(-1);
		
		shutdownIdleTime = 1500;
		fractureIdleTime = 600;
		
		shutdownIdleStart = System.nanoTime();
	}
	
	@Override
	public void hit(int damage) {
		
	}
	
	private void getNextPosition() {
		dx = 0;
		dy = 0;
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == SHUTDOWNIDLE) {
			long passedTime = (System.nanoTime() - shutdownIdleStart) / 1000000;
			
			if(passedTime >= shutdownIdleTime) {
				currentAnimation = FRACTURE;
				animation.setFrames(fractureSprites);
				animation.setDelay(70);
				JukeBox.playWithRecommendedVolume("playerdeathcrack");
			}
		}
		else if(currentAnimation == FRACTURE && animation.hasPlayedOnce()) {
			currentAnimation = FRACTUREIDLE;
			animation.setFrames(fracturedSprites);
			animation.setDelay(-1);
			fractureIdleStart = System.nanoTime();
			JukeBox.playWithRecommendedVolume("playerdeathcrack");
		}
		else if(currentAnimation == FRACTUREIDLE) {
			long passedTime = (System.nanoTime() - fractureIdleStart) / 1000000;
			
			if(passedTime >= fractureIdleTime) {
				dead = true;
				spawnPlayerPieces = true;
				JukeBox.playWithRecommendedVolume("playerdeath");
			}
		}
	}
	
	public void update() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		
		setMapPosition();
		
		super.draw(g);
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(spawnPlayerPieces) {
			EntitySpawnData esd;
			double dxEd = 0;
			double dyEd = 0;
			for(int pieceNumber = 1; pieceNumber <= 31; pieceNumber++) {
				dxEd = Support.getDoubleWithXExtraDecimals(Support.randInt(-500, 500), 2);
				dyEd = Support.getDoubleWithXExtraDecimals(Support.randInt(-700, 500), 2);
				PlayerPieceExtra ed = new PlayerPieceExtra(dxEd, dyEd);
				esd = new EntitySpawnData(
					new PlayerPiece(tileMap, pieceNumber, ed),
					getx(),
					gety()
				);
				esdList.add(esd);
			}
			
			spawnPlayerPieces = false;
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {}
	
	@Override
	public void swapAnimationFrames() {}
}









