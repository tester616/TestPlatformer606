package Entity;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall7Extra;
import EntityExtraData.BossBall7ParticleExtra;
import Handlers.Content;
import Main.GamePanel;

public class BossBall7 extends Enemy {

	private BufferedImage[] chaseSprites;
	private BufferedImage[] explosionSprites;

	private BufferedImage[] chaseSpritesC;
	private BufferedImage[] explosionSpritesC;

	private BufferedImage[] chaseSpritesM;
	private BufferedImage[] explosionSpritesM;
	
	// homing effects need information about the player's location
	private double playerX;
	private double playerY;
	
	// x and y speeds combined in length
	//private double totalSpeedVector;
	
	// how accurately it chases the player, -1 is no homing, 0 is a neutral homing and 1 is perfect homing, values under -1 and over 1 are treated as 0
	private double homingStrength;
	
	private int currentAnimation;
	
	private final int CHASE = 0;
	private final int EXPLOSION = 1;

	public static final long PATH_TRAVELTIME_DEFAULT = 1000;

	private float pathOpacity;
	
	private long pathTravelTime;
	private int pathTravelCurrentFrame;
	private int pathTravelTotalFrames;
	
	private long explosionTime;
	private int explosionCurrentFrame;
	private int explosionTotalFrames;

	private long createParticleTime;
	private int createParticleCurrentFrame;
	private int createParticleTotalFrames;
	
	private int particleSpeedMax;
	private long particleLifeTime;
	private int particleAccelerationMax;
	
	private double xPathStart;
	private double yPathStart;
	private double xPathEnd;
	private double yPathEnd;
	
	private double xPathPos;
	private double yPathPos;
	
	private double dxPath;
	private double dyPath;
	
	// before death, initial spawns another BossBall7 targeted at the players position with secondary mode that doesn't spawn anymore
	public static final int MODE_INITIAL = 0;
	public static final int MODE_SECONDARY = 1;
	
	private boolean createSecondaryBall;
	private boolean createExplosion;
	private boolean createParticle;
	
	
	public BossBall7(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		//maxSpeed = 6.2;
		
		width = 31;
		height = 31;
		cwidth = 9;
		cheight = 9;
		
		health = maxHealth = 1;
		damage = 8;
		
		pacified = true;
		invulnerable = true;
		
		updateColorMode();
		
		chaseSpritesC = Content.BossBall7[0];
		explosionSpritesC = Content.Invisible[0];
		
		chaseSpritesM = Content.BossBall7M[0];
		explosionSpritesM = Content.Invisible[0];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(chaseSprites);
		animation.setDelay(45);
		
		currentAnimation = CHASE;
		
		homingStrength = -0.9885;
		speedMultiplier = 1.0;
		
		xPathPos = xPathStart;
		yPathPos = yPathStart;
		
		pathOpacity = Support.TRANSPARENT;
		
		pathTravelTotalFrames = (int) (((double) pathTravelTime / 1000) * GamePanel.TARGETFPS);
		pathTravelCurrentFrame = 0;
		
		explosionTime = 290;
		explosionTotalFrames = (int) (((double) explosionTime / 1000) * GamePanel.TARGETFPS);
		
		createParticleTime = 30;
		createParticleTotalFrames = (int) (((double) createParticleTime / 1000) * GamePanel.TARGETFPS);
		
		particleSpeedMax = 200;
		particleAccelerationMax = 40;
		particleLifeTime = 850;
		
		setPathSpeed();
	}
	
	private void setPathSpeed() {
		double xPathTotal = xPathEnd - xPathStart;
		double yPathTotal = yPathEnd - yPathStart;
		
		dxPath = xPathTotal / (double) pathTravelTotalFrames;
		dyPath = yPathTotal / (double) pathTravelTotalFrames;
	}
	
	// for example 1000 & 3 give something from -1.0 to 1.0
	private double getRandomAcceleration(int accelerationMax, int decimals) {
		return Support.getDoubleWithXExtraDecimals(
			Support.randInt(
				-accelerationMax,
				accelerationMax
			),
			decimals
		);
	}

	private void getNextPosition() {
		if(currentAnimation == CHASE) {
			// updates path position
			xPathPos += dxPath;
			yPathPos += dyPath;
			
			// updates ball chasing the path position
			double xDifference = xPathPos - x;
			double yDifference = yPathPos - y;
			
			if(homingStrength > 0.0 && homingStrength <= 1.0) {
				dx = dx * (1 - Math.abs(homingStrength));
				dy = dy * (1 - Math.abs(homingStrength));
			}
			
			if(homingStrength < 0.0 && homingStrength >= -1.0) {
				xDifference = xDifference * (1 - Math.abs(homingStrength));
				yDifference = yDifference * (1 - Math.abs(homingStrength));
			}
			
			dx = (dx + xDifference) * speedMultiplier;
			dy = (dy + yDifference) * speedMultiplier;
			
			/*totalSpeedVector = Math.hypot(dx, dy);
			if(totalSpeedVector > maxSpeed) {
				double totalSpeedInDecimal = totalSpeedVector / maxSpeed;
				 dx = dx / totalSpeedInDecimal;
				 dy = dy / totalSpeedInDecimal;
			}*/
		}
		else if(currentAnimation == EXPLOSION) {
			dxPath = 0.0;
			dyPath = 0.0;
		}
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == CHASE) {
			createParticleCurrentFrame++;
			if(createParticleCurrentFrame > createParticleTotalFrames) {
				createParticle = true;
				createParticleCurrentFrame = 0;
			}
			
			pathTravelCurrentFrame++;
			if(pathTravelCurrentFrame > pathTravelTotalFrames) {
				currentAnimation = EXPLOSION;
				explosionCurrentFrame = 0;
				animation.setFrames(explosionSprites);
				animation.setDelay(-1);
				createExplosion = true;
			}
		}
		else if(currentAnimation == EXPLOSION) {
			explosionCurrentFrame++;
			if(explosionCurrentFrame > explosionTotalFrames) {
				if(mode == MODE_INITIAL) createSecondaryBall = true;
				dead = true;
			}
		}
	}
	
	public void update() {
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
		
		// draw path line
		float drawPathOpacity = pathOpacity * Support.surroundingsOpacity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawPathOpacity));
		Color origColor = g.getColor();
		g.setColor(Color.WHITE);
		
		// draw swap line
		g.drawLine(
			(int) (xPathStart + xmap),
			(int) (yPathStart + ymap),
			(int) (xPathEnd + xmap),
			(int) (yPathEnd + ymap)
		);
		
		// reset original g values
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
		g.setColor(origColor);
		
		// draw ball
		super.draw(g);
		/*if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				width,
				height,
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}*/
	}
	
	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(createSecondaryBall) {
			BossBall7Extra ed = new BossBall7Extra(
				xPathEnd,
				yPathEnd,
				playerX,
				playerY,
				pathTravelTime
			);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall7(tileMap, MODE_SECONDARY, ed),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createSecondaryBall = false;
		}
		
		if(createParticle) {
			BossBall7ParticleExtra ed = new BossBall7ParticleExtra(
				Support.getDoubleWithXExtraDecimals(Support.randInt(-particleSpeedMax, particleSpeedMax), 3),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-particleSpeedMax, particleSpeedMax), 3),
				getRandomAcceleration(particleAccelerationMax, 3),
				getRandomAcceleration(particleAccelerationMax, 3),
				particleLifeTime
			);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall7Particle(tileMap, MODE_SECONDARY, ed),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createParticle = false;
		}
		
		if(createExplosion) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall7Explosion(tileMap, MODE_SECONDARY, null),
				(int) xPathEnd,
				(int) yPathEnd
			);
			esdList.add(esd);
			
			createExplosion = false;
		}
		
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
			chaseSprites = chaseSpritesC;
			explosionSprites = explosionSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			chaseSprites = chaseSpritesM;
			explosionSprites = explosionSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		if(currentAnimation == CHASE) {
			animation.swapFrames(chaseSprites);
		}
		else if(currentAnimation == EXPLOSION) {
			animation.swapFrames(explosionSprites);
		}
	}
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		BossBall7Extra ed = (BossBall7Extra) extraData;
		xPathStart = ed.xPathStart;
		yPathStart = ed.yPathStart;
		xPathEnd = ed.xPathEnd;
		yPathEnd = ed.yPathEnd;
		pathTravelTime = ed.pathTravelTime;
	}
}









