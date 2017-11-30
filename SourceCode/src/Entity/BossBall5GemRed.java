package Entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import EntityExtraData.BossBall5Extra;
import Handlers.Content;

public class BossBall5GemRed extends Enemy {

	private BufferedImage[] sprites;

	private BufferedImage[] spritesC;

	private BufferedImage[] spritesM;
	
	private int mode;
	
	public final int ZERO = 0;
	public final int FIFTEEN = 1;
	public final int THIRTY = 2;
	public final int FORTYFIVE = 3;
	public final int SIXTY = 4;
	public final int SEVENTYFIVE = 5;
	public final int NINETY = 6;
	public final int ONEHUNDRED_AND_FIVE = 7;
	public final int ONEHUNDRED_AND_TWENTY = 8;
	public final int ONEHUNDRED_AND_THIRTYFIVE = 9;
	public final int ONEHUNDRED_AND_FIFTY = 10;
	public final int ONEHUNDRED_AND_SIXTYFIVE = 11;
	public final int ONEHUNDRED_AND_EIGHTY = 12;
	public final int ONEHUNDRED_AND_NINETYFIVE = 13;
	public final int TWOHUNDRED_AND_TEN = 14;
	public final int TWOHUNDRED_AND_TWENTYFIVE = 15;
	public final int TWOHUNDRED_AND_FORTY = 16;
	public final int TWOHUNDRED_AND_FIFTYFIVE = 17;
	public final int TWOHUNDRED_AND_SEVENTY = 18;
	public final int TWOHUNDRED_AND_EIGHTYFIVE = 19;
	public final int THREEHUNDRED = 20;
	public final int THREEHUNDRED_AND_FIFTEEN = 21;
	public final int THREEHUNDRED_AND_THIRTY = 22;
	public final int THREEHUNDRED_AND_FORTYFIVE = 23;
	
	private double totalSpeed;
	
	BossBall5Extra ed;

	private int currentAnimation;

	private final int IDLE = 0;
	private final int SPAWNING = 1;
	private final int INVISIBLE = 2;
	private final int STARTFLYING = 3;
	private final int FLY = 4;

	private long invisibleStart;
	private long invisibleTime;
	
	private long idleTime;
	private long idleStart;
	
	private float currentOpacity;

	private boolean spawnFlash;

	
	public BossBall5GemRed(TileMap tm, int mode, Object extraData) {
		
		super(tm, mode, extraData);
		
		setExtraData(mode, extraData);
		
		width = 31;
		height = 31;
		cwidth = 1;
		cheight = 1;
		
		health = maxHealth = 1;
		damage = 9;
		
		totalSpeed = ed.totalSpeed;
		
		currentAnimation = SPAWNING;
		
		currentOpacity = Support.INVISIBLEOPACITY;
		
		idleTime = ed.idleDuration;
		idleStart = System.nanoTime();
		
		invisibleTime = 300;
		invisibleStart = System.nanoTime();
		
		pacified = false;
		invulnerable = false;
		
		updateColorMode();
		
		int spriteRow = mode;
		if(mode > 11) spriteRow -= 12;
		
		spritesC = Content.BossBall5GemRed[spriteRow];
		
		spritesM = Content.BossBall5GemRedM[spriteRow];
		
		setBufferedImages();
		
		animation = new Animation();
		animation.setFrames(sprites);
		animation.setDelay(-1);
	}
	
	// gives dx and dy according to mode which describes direction of the sprite
	// 0 reverses direction, 1 does nothing and 2 randoms one of the two
	// unreversed directions always point towards the middle of a circle all modes would form, making knockback return reverse values pushing them away
	// in short 0 gives inwards suction relative to the circle, 1 gives outwards push
	private void setSpeed() {
		double radiansAngle;
		double tanValue;
		double xDir;
		double yDir;
		double xSpeedInward;
		double ySpeedInward;
		
		// first mode (0) is 0 degrees, second mode (1) is 15 and so on
		radiansAngle = Math.toRadians(mode * 15);
		
		tanValue = Math.tan(radiansAngle);
		
		if(Double.isNaN(tanValue)) {
			xDir = 0.0;
			yDir = 1.0;
		}
		else {
			xDir = 1.0;
			yDir = tanValue;
		}
		
		ArrayList<Double> direction = getDirectionProportions(xDir, yDir);
		xSpeedInward = direction.get(0);
		ySpeedInward = direction.get(1);
		
		// attack 3 doesn't always go in a set pattern inwards, but has random spawns instead
		if(ed.attackNumber != 3) {

		}
		// points speed directions inwards relative to the circle the gems form
		if(mode < NINETY || mode > TWOHUNDRED_AND_SEVENTY) xSpeedInward = Math.abs(xSpeedInward) * (-1);
		else xSpeedInward = Math.abs(xSpeedInward);
		if((mode > ZERO && mode < ONEHUNDRED_AND_EIGHTY)) ySpeedInward = Math.abs(ySpeedInward) * (-1);
		else ySpeedInward = Math.abs(ySpeedInward);
		
		if(ed.reverse == 2) ed.reverse = Support.randInt(0, 1);
		
		if(ed.reverse == 0) {
			knockback(xSpeedInward * (-1), ySpeedInward * (-1), totalSpeed, true, true);
		}
		else if(ed.reverse == 1) {
			knockback(xSpeedInward, ySpeedInward, totalSpeed, true, true);
		}
		else System.out.println("Wrong reverse (" + ed.reverse + ") value in setSpeed of BossBall5GemRed.");
	}
	
	public void updateCurrentAnimation() {
		if(currentAnimation == IDLE) {
			long passedTime = (System.nanoTime() - idleStart) / 1000000;
			
			if(passedTime >= idleTime) {
				currentAnimation = STARTFLYING;
				setSpeed();
			}
		}
		else if(currentAnimation == SPAWNING) {
			spawnFlash = true;
			currentAnimation = INVISIBLE;
		}
		else if(currentAnimation == INVISIBLE) {
			long passedTime = (System.nanoTime() - invisibleStart) / 1000000;
			
			if(passedTime >= invisibleTime) {
				currentOpacity = Support.NORMALOPACITY;
				currentAnimation = IDLE;
			}
		}
		else if(currentAnimation == STARTFLYING) {
			currentAnimation = FLY;
		}
		/*else if(currentAnimation == FLY) {
			// nothing needed here
		}*/
	}
	
	public void update() {
		// update position
		checkTileMapCollision();
		//checkOutOfBounds();
		setPosition(xtemp, ytemp);
		
		// update animation
		animation.update();
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		float drawOpacity = currentOpacity * Support.surroundingsOpacity;
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawOpacity));
		
		super.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}

	/*private void checkOutOfBounds() {
		// just simple checks with minimum math and processing time
		if(x < -50) dead = true;
		if(x > tileMap.getWidth() + 50) dead = true;
		if(y < -50) dead = true;
		if(y > tileMap.getHeight() + 50) dead = true;
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
		
		calculateCorners(x, ydest);
		
		if(dy < 0) {
			if(topLeft || topRight) {
				dead = true;
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dead = true;
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dead = true;
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dead = true;
			}
			else {
				xtemp += dx;
			}
		}
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(spawnFlash) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall5Flash(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);

			spawnFlash = false;
		}
		
		return esdList;
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
	
	@Override
	public void setExtraData(int mode, Object extraData) {
		this.mode = mode;
		ed = (BossBall5Extra) extraData;
	}
}





/* OLD setSpeed preserved in case it's needed	
	// gives dx and dy according to mode which describes direction of the sprite
	// 0 reverses direction, 1 does nothing and 2 randoms one of the two
	// depending on mode, reversed 0 directions go 100% right or varyingly down while 1 directions go 100% left or varyingly up
	private void setSpeed(int reverse) {
		double radiansAngle = 0;
		double tanValue;
		double xDir;
		double yDir;
		
		if(mode == ZERO) radiansAngle = Math.toRadians(0);
		else if(mode == FIFTEEN) radiansAngle = Math.toRadians(15);
		else if(mode == THIRTY) radiansAngle = Math.toRadians(30);
		else if(mode == FOURTYFIVE) radiansAngle = Math.toRadians(45);
		else if(mode == SIXTY) radiansAngle = Math.toRadians(60);
		else if(mode == SEVENTYFIVE) radiansAngle = Math.toRadians(75);
		else if(mode == NINETY) radiansAngle = Math.toRadians(90);
		else if(mode == ONEHUNDREDANDFIVE) radiansAngle = Math.toRadians(105);
		else if(mode == ONEHUNDREDANDTWENTY) radiansAngle = Math.toRadians(120);
		else if(mode == ONEHUNDREDANDTHIRTYFIVE) radiansAngle = Math.toRadians(135);
		else if(mode == ONEHUNDREDANDFIFTY) radiansAngle = Math.toRadians(150);
		else if(mode == ONEHUNDREDANDSIXTYFIVE) radiansAngle = Math.toRadians(165);
		else System.out.println("Error in setExtraData of BossBall5RedGem, mode (" + mode + ") doesn't exist.");
		
		tanValue = Math.tan(radiansAngle);
		
		if(Double.isNaN(tanValue)) {
			xDir = 0.0;
			yDir = 1.0;
		}
		else {
			xDir = 1.0;
			yDir = tanValue;
		}
		
		ArrayList<Double> direction = getDirectionProportions(xDir, yDir);
		
		if(reverse == 2) reverse = Support.randInt(0, 1);
		
		if(reverse == 0) {
			knockback(direction.get(0) * (-1), direction.get(1) * (-1), totalSpeed, true, true);
		}
		else if(reverse == 1) {
			knockback(direction.get(0), direction.get(1), totalSpeed, true, true);
		}
		else System.out.println("Wrong reverse (" + reverse + ") value in setSpeed of BossBall5RedGem.");
	}
 */



