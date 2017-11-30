package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import EntityExtraData.BossBall6ParticleExtra;
import Handlers.Content;
<<<<<<< HEAD
=======
import Main.GamePanel;
>>>>>>> 2.03

public class BossShield extends Enemy {

	private BufferedImage[] shieldSprites;
	private BufferedImage[] flashSprites;

	private BufferedImage[] shieldSpritesC;
	private BufferedImage[] flashSpritesC;

	private BufferedImage[] shieldSpritesM;
	private BufferedImage[] flashSpritesM;

	private final int IDLE = 0;
	private final int GROW = 1;
	private final int FLASH = 2;
	
	private int currentAnimation;
	
	//private Dialog dialog;
	
	private long lifeTime;
	private long creationTime;
	
	private double knockbackForce;

	private long lifeTimePassed;

	private float currentOpacity;
	private float originalOpacity;
	
	// suction effect with negative values, so min is actually bigger than max
	private double knockbackForceMin;
	private double knockbackForceMax;
	
	private long knockbackForceGrowTime;
	
	// destruction values
	private double destructionGrowMaxScale;
	private long destructionGrowTime;
	private long destructionGrowStart;
	
	// flash values
	private double flashFadeoutStartDecimal;
	private long flashTime;
	private long flashStart;
	
	private int originalWidth;
	private int originalHeight;

	private boolean destructing;
	
	private boolean deleteEnemies;
	private boolean spawnParticle;
	
	
	public BossShield(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		//setExtraData(mode, extraData);
		
		originalWidth = 131;
		originalHeight = 131;
		
		width = originalWidth;
		height = originalHeight;
		cwidth = 700;
		cheight = 700;
		
		knockbackForceMin = 0.0;
		knockbackForceMax = -1.0;
		
		knockbackForce = knockbackForceMin;
		
		knockbackForceGrowTime = 10000;
		
		health = maxHealth = 1;
		damage = 0;
		
		pacified = false;
		invulnerable = true;
		
		updateColorMode();
		
		shieldSpritesC = Content.BossShield[0];
		flashSpritesC = Content.BossShieldFlash[0];
		
		shieldSpritesM = Content.BossShieldM[0];
		flashSpritesM = Content.BossShieldFlashM[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(shieldSprites);
		animation.setDelay(-1);
		
		originalOpacity = Support.TRANSPARENT;
		currentOpacity = originalOpacity;
		
		lifeTime = 100000;
		creationTime = System.nanoTime();
		
		destructionGrowMaxScale = 3.0;
		destructionGrowTime = 2000;
		
		flashFadeoutStartDecimal = 0.45;
		flashTime = 3900;
		
		currentAnimation = IDLE;
	}
	
	public boolean shouldDeleteEnemies() {
		return deleteEnemies;
	}

	public void setDeleteEnemies(boolean deleteEnemies) {
		this.deleteEnemies = deleteEnemies;
	}

	public double getKnockbackForce() {
		return knockbackForce;
	}
	
	public void setKnockbackForce(double knockbackForce) {
		this.knockbackForce = knockbackForce;
	}

	public void setCurrentOpacity(float currentOpacity) {
		this.currentOpacity = currentOpacity;
	}
	
	private void getNextPosition() {
		dx = 0;
		dy = 0;
	}

	@SuppressWarnings("unused")
	private void setLifeTimePassed() {
		lifeTimePassed = (System.nanoTime() - creationTime) / 1000000;
	}

	public void setDestructing(boolean destructing) { this.destructing = destructing; }
	
	@SuppressWarnings("unused")
	private void checkLifeTime() {
		if(lifeTimePassed > lifeTime) {
			dead = true;
		}
	}

	private void updateKnockbackForce() {
		long elapsedGrowTime = (System.nanoTime() - creationTime) / 1000000;
		double elapsedGrowTimeDecimal = (double) elapsedGrowTime / (double) knockbackForceGrowTime;
		if(elapsedGrowTimeDecimal > 1.0) elapsedGrowTimeDecimal = 1.0;
		knockbackForce = knockbackForceMin + ((knockbackForceMax - knockbackForceMin) * elapsedGrowTimeDecimal);
	}

	private void updateGrowScaledWidthAndHeight(long elapsedGrowTime) {
		double elapsedGrowTimeDecimal = (double) elapsedGrowTime / (double) destructionGrowTime;
		if(elapsedGrowTimeDecimal > 1.0) elapsedGrowTimeDecimal = 1.0;
		double currentScale = 1.0 + (destructionGrowMaxScale - 1.0) * elapsedGrowTimeDecimal;
		width = (int) ((double) originalWidth * currentScale);
		height = (int) ((double) originalHeight * currentScale);
	}
	
	private void updateFlashOpacityValue(long elapsedFlashTime) {
		double flashTimePassedOfTotalInDecimal = (double) elapsedFlashTime / (double) flashTime;
		double flashTimeLeftOfTotalDecimal = 1.0 - flashTimePassedOfTotalInDecimal;
		if(flashTimeLeftOfTotalDecimal <= flashFadeoutStartDecimal) {
			double lifeTimePassedAtFadeStartDecimal = 1.0 - flashFadeoutStartDecimal;
			
			long flashTimeLastPart = (long) (flashTime * flashFadeoutStartDecimal);
			long flashTimePassedOfLastPart = (long) (elapsedFlashTime - (flashTime * lifeTimePassedAtFadeStartDecimal));
			
			double lifeTimePassedOfLastPartInDecimal = (double) flashTimePassedOfLastPart / (double) flashTimeLastPart;
			float fadedOpacity = (float) (Support.NORMALOPACITY - (Support.NORMALOPACITY * lifeTimePassedOfLastPartInDecimal));
			if(fadedOpacity < 0.0) fadedOpacity = (float) 0.0;
			
			setCurrentOpacity(fadedOpacity);
		}
		else {
			setCurrentOpacity(Support.NORMALOPACITY);
			spawnParticle = true;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			if(destructing) {
				currentAnimation = GROW;
				destructionGrowStart = System.nanoTime();
				JukeBox.playWithRecommendedVolume("bossshieldgrow");
			}
		}
		else if(currentAnimation == GROW) {
			long passedTime = (System.nanoTime() - destructionGrowStart) / 1000000;
			
			updateGrowScaledWidthAndHeight(passedTime);
			
			if(passedTime >= destructionGrowTime) {
<<<<<<< HEAD
=======
				GamePanel.playtime.stopGameTime();
				GamePanel.playtime.setCurrentTimeValues();
>>>>>>> 2.03
				currentAnimation = FLASH;
				animation.setFrames(flashSprites);
				animation.setDelay(-1);
				setCurrentOpacity(Support.NORMALOPACITY);
				JukeBox.stop("boss");
				JukeBox.stop("bossshieldgrow");
				JukeBox.playWithRecommendedVolume("bossshieldflash");
				flashStart = System.nanoTime();
				deleteEnemies = true;
			}
		}
		else if(currentAnimation == FLASH) {
			long passedTime = (System.nanoTime() - flashStart) / 1000000;
			
			updateFlashOpacityValue(passedTime);
			
			if(passedTime >= flashTime) {
				dead = true;
			}
		}
	}
	
	public void update() {
		
		//setLifeTimePassed();
		
		updateKnockbackForce();
		
		//checkLifeTime();
		
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();

		float drawOpacity;
		if(currentAnimation == FLASH) {
			drawOpacity = currentOpacity;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawOpacity));
		}
		else {
			drawOpacity = currentOpacity * Support.surroundingsOpacity;
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawOpacity));
		}
		
		super.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(spawnParticle) {
			long particleLifeTime = Support.randInt(2500, 4500);
			int x = Support.randInt(-300, 300);
			int y = Support.randInt(-300, 300);
			double dx = Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 3);
			double dy = Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 3);
			BossBall6ParticleExtra ed = new BossBall6ParticleExtra(dx, dy, particleLifeTime);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall6Particle(tileMap, 0, ed),
				getx() + x,
				gety() + y
			);
			esdList.add(esd);
			
			spawnParticle = false;
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			shieldSprites = shieldSpritesC;
			flashSprites = flashSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			shieldSprites = shieldSpritesM;
			flashSprites = flashSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == IDLE) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == GROW) {
			animation.swapFrames(shieldSprites);
		}
		else if(currentAnimation == FLASH) {
			animation.swapFrames(flashSprites);
		}
	}
	
	/*@Override
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		
		BossShieldExtra ed = (BossShieldExtra) extraData;
		
		dialog = ed.dialog;
	}*/
}









