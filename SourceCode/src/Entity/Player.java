package Entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import Support.Support;

import javax.imageio.ImageIO;

import Audio.JukeBox;
import Entity.Enemies.Mine;
import EntityExtraData.AttackWeakExtra;
import EntityExtraData.PlayerCoreExtra;
import TileMap.*;

public class Player extends MapObject {
	
	// player stuff
	private double health;
	private double maxHealth;
	private int lives;
	private boolean dead;
	
	// dashing
	private boolean dashing;
	private boolean dashJumping;
	private boolean firstDashBeforeLanding;
	private double xDashJumpStart;
	private double yDashJumpStart;
	private double xDashStopSpeed;
	private long wallContactGracePeriod;
	
	// status effects
	private boolean dizzy;
	private long dizzyStart;
	private int dizzyDuration;
	private int dizzyDurationLeft;
	private boolean confused;
	private long confusedStart;
	private int confusedDuration;
	private int confusedDurationLeft;
	private boolean sealed;
	private long sealedStart;
	private int sealedDuration;
	private int sealedDurationLeft;
	private boolean grabbed;
	
	
	// animations
	private ArrayList<BufferedImage[]> sprites;
	private final int[] numFrames = {	// amount of sprites in a row from first down
		11, 11, 11, 11
	};
	
	// animation actions
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int FALLING = 3;
	@SuppressWarnings("unused")
	private static final int GRABBED = 4;
	
	// default speed values
	private static final double DEFAULTSTOPSPEED = 0.4;
	private static final double DEFAULTFALLSPEED = 0.15;
	private static final double DEFAULTSTOPJUMPSPEED = 0.3;
	private static final double DEFAULTXDASHSTOPSPEED = 0.10;
	
	// health stuff
	private double healthRegeneration;
	private double healthCheatRegeneration;
	
	// attack stuff
	private int attackEnergy;
	private int maxAttackEnergy;
	private int weakAttackEnergyCost;
	private int strongAttackEnergyCost;
	private int attackRegeneration;
	private int attackCheatRegeneration;
	
	// parry stuff
	// parrying is parry button pressed state, which in itself doesn't mean parry invulnerability is active
	private boolean parrying;
	// activeParrying gives the kind of invulnerability that comes from parrying and is active based on multiple things
	private boolean activeParrying;
	private boolean initialParry;
	private boolean hasFailedParryChain;
	private long parryPreDuration;
	private long parryPreStartTime;
	private long parryActiveDuration;
	private long parryActiveStartTime;
	private long parryChainWindow;
	private long parryCooldown;
	private long parryCurrentCooldown;
	
	// time rewind stuff
	private ArrayList<PlayerPastData> playerPastData = new ArrayList<PlayerPastData>();
	private long pastDataSaveTime;
	private boolean rewindButton;
	private boolean rewindButtonInitialPress;
	private boolean rewindStartPause;
	private boolean rewindActiveTime;
	private boolean rewindEndPause;
	private long rewindStartPauseTime;
	private long rewindStartPauseTotalTime;
	private long rewindEndPauseTime;
	
	private long rewindEndPauseTotalTime;
	private int rewindEnergy;
	private int maxRewindEnergy;
	private int rewindInitialCost;
	private int rewindUpkeepCost;
	private int rewindRegeneration;
	private int rewindCheatRegeneration;
	private long ppdCurrentTime;
	
	// time rewind color change stuff
	private boolean changeColors;
	private boolean rewindingTerms;
	
	// some stuff related to players death and the resulting draw texts
	private boolean deathTextCountdownActivated;
	private boolean deathLifeSubtractionCountdownActivated;
	private boolean deathRespawnCountdownActivated;
	private boolean drawDeathText;
	private boolean moveOnWithDeath;
	private boolean respawn;
	private long deathTextCountdownActivationTime;
	private long deathTextCountdownWaitTime;
	private long deathLifeSubtractionCountdownActivationTime;
	private long deathLifeSubtractionCountdownWaitTime;
	private long deathRespawnCountdownActivationTime;
	private long deathRespawnCountdownWaitTime;
	private boolean spawnParryShield;
	private boolean spawnParryShieldImpact;
	private boolean strongAttacking;
	private boolean spawnStrongAttack;
	private boolean weakAttacking;
	private boolean spawnWeakAttack;
	private boolean spawnPlayerFraction;
	private boolean spawnPlayerCore;
	
	// a few additional button press booleans
	private boolean togglingInvulnerability;
	private boolean togglingInfiniteJump;
	private boolean togglingHealthRegenerationHaste;
	private boolean togglingAttackRegenerationHaste;
	private boolean togglingRewindRegenerationHaste;
	private boolean suiciding;
	
	// false ignores player game inputs such as moving or attacking
	private boolean freeToAct;
	
	private boolean showDialog;
	private boolean updateDialogConversation;
	private int updateDialogConversationID;
	
	// parry cooldown announcement
	private boolean spawnParryCooldownAnnouncement;
	private double parryCooldownAnnouncement;
	private double parryCooldownAnnouncementDx;
	private double parryCooldownAnnouncementDy;
	private long parryCooldownAnnouncementLifeTime;
	private int parryCooldownAnnouncementX;
	private int parryCooldownAnnouncementY;
	
	private boolean spawnWeakAttackEnergyAnnouncement;
	private String weakAttackEnergyAnnouncement;
	private double weakAttackEnergyAnnouncementDx;
	private double weakAttackEnergyAnnouncementDy;
	private long weakAttackEnergyAnnouncementLifeTime;
	private int weakAttackEnergyAnnouncementX;
	private int weakAttackEnergyAnnouncementY;
	
	private boolean spawnStrongAttackEnergyAnnouncement;
	private String strongAttackEnergyAnnouncement;
	private double strongAttackEnergyAnnouncementDx;
	private double strongAttackEnergyAnnouncementDy;
	private long strongAttackEnergyAnnouncementLifeTime;
	private int strongAttackEnergyAnnouncementX;
	private int strongAttackEnergyAnnouncementY;
	
	private boolean spawnRewindEnergyAnnouncement;
	private String rewindEnergyAnnouncement;
	private double rewindEnergyAnnouncementDx;
	private double rewindEnergyAnnouncementDy;
	private long rewindEnergyAnnouncementLifeTime;
	private int rewindEnergyAnnouncementX;
	private int rewindEnergyAnnouncementY;
	
	
	// a list where collected items from the map are stored and activated from
	//private ArrayList<Collectible> collectibles;
	
	public Player(TileMap tm, Dialog dialog) {
		
		super(tm);
		
		//collectibles = new ArrayList<Collectible>();
		
		// for reading in sprites
		width = 31;
		height = 31;
		
		// "real" width & height used for determining collision, which c stands for
		cwidth = 22;
		cheight = 22;

		moveSpeed = 0.3;
		maxSpeed = 1.6;
		stopSpeed = DEFAULTSTOPSPEED;
		fallSpeed = DEFAULTFALLSPEED;
		maxFallSpeed = 4.0;
		jumpStart = -5.4;
		stopJumpSpeed = DEFAULTSTOPJUMPSPEED;

		facingRight = true;
		
		lives = 3 + Support.difficultyExtraLives;
		health = maxHealth = 100;
		dead = false;
		
		xDashJumpStart = 6.3;
		yDashJumpStart = -6.5;
		xDashStopSpeed = DEFAULTXDASHSTOPSPEED;
		wallContactGracePeriod = 100;
		
		speedMultiplier = 1.0;
		
		pacified = false;
		invulnerable = false;
		
		dizzy = false;
		confused = false;
		sealed = false;
		dizzyDuration = 7000;
		confusedDuration = 7000;
		sealedDuration = 7000;
		
		healthRegeneration = 0.0;
		healthCheatRegeneration = 0.03;
		
		attackEnergy = maxAttackEnergy = 100000;
		weakAttackEnergyCost = 25000;
		strongAttackEnergyCost = 100000;
		attackRegeneration = 260;
		attackCheatRegeneration = 2200;
		
		parryPreDuration = 155;
		parryChainWindow = 110;
		parryActiveDuration = 485;
		parryCooldown = 1700;
		
		pastDataSaveTime = 3000;
		rewindStartPauseTotalTime = 1400;
		rewindEndPauseTotalTime = 1400;
		rewindEnergy = maxRewindEnergy = 100000;
		rewindInitialCost = 20000;
		rewindUpkeepCost = 440;
		rewindRegeneration = 15;
		rewindCheatRegeneration = 1200;
		
		deathTextCountdownActivated = false;
		deathLifeSubtractionCountdownActivated = false;
		deathRespawnCountdownActivated = false;
		drawDeathText = false;
		moveOnWithDeath = false;
		respawn = true;
		deathTextCountdownWaitTime = 2500;
		deathLifeSubtractionCountdownWaitTime = 1500;
		deathRespawnCountdownWaitTime = 2000;
		
		freeToAct = true;
		
		parryCooldownAnnouncementDx = 0.0;
		parryCooldownAnnouncementDy = -1.15;
		parryCooldownAnnouncementLifeTime = 500;
		parryCooldownAnnouncementX = 0;
		parryCooldownAnnouncementY = -15;
		
		weakAttackEnergyAnnouncementDx = 0.0;
		weakAttackEnergyAnnouncementDy = -1.15;
		weakAttackEnergyAnnouncementLifeTime = 500;
		weakAttackEnergyAnnouncementX = 0;
		weakAttackEnergyAnnouncementY = -15;
		
		strongAttackEnergyAnnouncementDx = 0.0;
		strongAttackEnergyAnnouncementDy = -1.15;
		strongAttackEnergyAnnouncementLifeTime = 500;
		strongAttackEnergyAnnouncementX = 0;
		strongAttackEnergyAnnouncementY = -15;
		
		rewindEnergyAnnouncementDx = 0.0;
		rewindEnergyAnnouncementDy = -1.15;
		rewindEnergyAnnouncementLifeTime = 500;
		rewindEnergyAnnouncementX = 0;
		rewindEnergyAnnouncementY = -15;
		
		// load sprites
		try {
			
			BufferedImage spritesheet = ImageIO.read(
				getClass().getResourceAsStream(
					"/Sprites/Player/player.gif"
				)
			);
			
			sprites = new ArrayList<BufferedImage[]>();
			for(int i = 0; i < 4; i++) {
				BufferedImage[] bi = new BufferedImage[numFrames[i]];
				for(int j = 0; j < numFrames[i]; j++) {
					
					if(i != 6) {
						bi[j] = spritesheet.getSubimage(
								j * width,
								i * height,
								width,
								height
						);
					}
					else {
						bi[j] = spritesheet.getSubimage(
								j * width * 2,
								i * height,
								width * 2,
								height
						);
					}
				}
				
				sprites.add(bi);
				
			}
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		animation = new Animation();
		currentAction = IDLE;
		animation.setFrames(sprites.get(IDLE));
		animation.setDelay(-1);
	}
	
	public double getHealth() { return health; }
	public double getMaxHealth() { return maxHealth; }
	public int getLives() { return lives; }
	public int getAttackEnergy() { return attackEnergy; }
	public int getMaxAttackEnergy() { return maxAttackEnergy; }
	public int getRewindEnergy() { return rewindEnergy; }
	public int getMaxRewindEnergy() { return maxRewindEnergy; }
	public int getDizzyDurationLeft() { return dizzyDurationLeft; }
	public int getConfusedDurationLeft() { return confusedDurationLeft; }
	public int getSealedDurationLeft() { return sealedDurationLeft; }
	public boolean isDead() { return dead; }
	public boolean isDizzy() { return dizzy; }
	public boolean isConfused() { return confused; }
	public boolean isSealed() { return sealed; }
	public boolean isRewinding() { return rewindButton; }
	public boolean isRewindStartPause() { return rewindStartPause; }
	public boolean isRewindActiveTime() { return rewindActiveTime; }
	public boolean isRewindEndPause() { return rewindEndPause; }
	public boolean shouldChangeColors() { return changeColors; }
	public boolean getRewindingTerms() { return rewindingTerms; }
	public boolean shouldDrawDeathText() { return drawDeathText; }
	public boolean shouldMoveOnWithDeath() { return moveOnWithDeath; }
	public boolean shouldRespawn() { return respawn; }
	public boolean isFreeToAct() { return freeToAct; }
	public boolean shouldShowDialog() { return showDialog; }
	public boolean shouldUpdateDialogConversation() { return updateDialogConversation; }
	public int getUpdateDialogConversationID() { return updateDialogConversationID; }
	public long getRewindEndPauseTime() { return rewindEndPauseTime; }
	public boolean getInvulnerable() { return invulnerable; }
	
	public void setDead(boolean dead) { this.dead = dead; }
	public void setWeakAttacking(boolean b) { weakAttacking = b; }
	public void setStrongAttacking(boolean b) { strongAttacking = b; }
	public void setDashing(boolean b) { dashing = b; }
	public void setRewinding(boolean b) { rewindButton = b; }
	public void setRewindingInitialPress(boolean b) { rewindButtonInitialPress = b; }
	public void setParrying(boolean b) { parrying = b; }
	public void setSuiciding(boolean b) { suiciding = b; }
	public void setTogglingInvulnerability(boolean b) { togglingInvulnerability = b; }
	public void setTogglingInfiniteJump(boolean b) { togglingInfiniteJump = b; }
	public void setTogglingAttackRegenerationHaste(boolean b) { togglingAttackRegenerationHaste = b; }
	public void setLives(int lives) { this.lives = lives; }
	public void setGrabbed(boolean b) { grabbed = b; }
	public void setShowDialog(boolean b) { showDialog = b; }
	public void setUpdateDialogConversation(boolean b) { updateDialogConversation = b; }
	public void setInvulnerable(boolean b) { invulnerable = b; }
	
	// player's own attacks are are checked in ally objects
	public void checkAttack(ArrayList<Enemy> enemies) {
		
		// starts off as 1.0, but can be modified by contact with certain enemies
		speedMultiplier = 1.0;
		
		// reset values that may have been modified by speedMultiplier
		//setDefaultSpeedValues();
		
		// loop through enemies
		for(int i = 0; i < enemies.size(); i++) {
			
			Enemy e = enemies.get(i);
			
			// check enemy collision
<<<<<<< HEAD
			if(intersects(e) && !invulnerable && !e.pacified && !rewindingTerms) {
=======
			// there must be a collision, the enemy must be active, the player must not be rewinding AND the player must either not be invulnerable or the enemy must be BossShield
			if(
				intersects(e) && !e.pacified && !rewindingTerms &&
				(!invulnerable || e.getClass().getSimpleName().equals("BossShield"))
			) {
				// lazy code, a better solution would move all of these to a common method inside the Enemy class with different implementations for each enemy.
>>>>>>> 2.03
				if(e.getClass().getSimpleName().equals("Stardust")) {
					speedMultiplier *= 0.98;
				}
				else if(e.getClass().getSimpleName().equals("Darkmist")) {
					hit(e.getDamage(), 0, 0);
					setDizzy();
				}
				else if(e.getClass().getSimpleName().equals("Square")) {
					hit(e.getDamage(), 0, 0);
					setConfused();
				}
				else if(e.getClass().getSimpleName().equals("SquareWall")) {
					SquareWall sw = (SquareWall) e;
					//hit(sw.getDamage(), 0, 0);
					setConfused();
					// impact reaction on velocity below
					// determines if reaction happens in x or y axis
					if(sw.getMode() == SquareWall.MODE_X) {
						// determines on which side the player is, in other words should the ricochet be + or -
						if(y > sw.gety()) {
							if(dy <= 0) {
								dy *= -sw.getRicochetStrength();
							}
							if(Math.abs(dy) < sw.getKnockbackStrength()) dy += sw.getKnockbackStrength();
						}
						else if(y < sw.gety()) {
							if(dy >= 0) {
								dy *= -sw.getRicochetStrength();
							}
							if(Math.abs(dy) < sw.getKnockbackStrength()) dy -= sw.getKnockbackStrength();
						}
						else {
							if(Math.abs(dy) < sw.getKnockbackStrength()) {
								if(Support.randInt(0, 1) == 1) dy += sw.getKnockbackStrength();
								else dy -= sw.getKnockbackStrength();
							}
						}
					}
					else if(sw.getMode() == SquareWall.MODE_Y) {
						if(x > sw.getx()) {
							if(dx <= 0) {
								dx *= -sw.getRicochetStrength();
								facingRight = !facingRight;
							}
							if(Math.abs(dx) < sw.getKnockbackStrength()) dx += sw.getKnockbackStrength();
						}
						else if(x < sw.getx()) {
							if(dx >= 0) {
								dx *= -sw.getRicochetStrength();
								facingRight = !facingRight;
							}
							if(Math.abs(dx) < sw.getKnockbackStrength()) dx -= sw.getKnockbackStrength();
						}
						else {
							if(Math.abs(dx) < sw.getKnockbackStrength()) {
								if(Support.randInt(0, 1) == 1) dx += sw.getKnockbackStrength();
								else dx -= sw.getKnockbackStrength();
							}
						}
					}
				}
				else if(e.getClass().getSimpleName().equals("Mine")) {
					((Mine) e).setTriggered(true);
				}
				else if(e.getClass().getSimpleName().equals("MineExplosion")) {
					ArrayList<Double> direction = getDirectionToMapObject(e);
					knockback(
						direction.get(0),
						direction.get(1),
						((MineExplosion) e).getKnockbackForce(),
						false,
						false
					);
					hit(e.getDamage(), 0, 0);
				}
				else if(e.getClass().getSimpleName().equals("ShieldRepel")) {
					ArrayList<Double> direction = getDirectionToMapObject(e);
					knockback(
						direction.get(0),
						direction.get(1),
						((ShieldRepel) e).getKnockbackForce(),
						false,
						false
					);
					hit(e.getDamage(), 0, 0);
				}
				else if(e.getClass().getSimpleName().equals("BallRepelAttack")) {
					ArrayList<Double> direction = getDirectionToMapObject(e);
					knockback(
						direction.get(0),
						direction.get(1),
						((BallRepelAttack) e).getKnockbackForce(),
						true,
						false
					);
				}
				else if(e.getClass().getSimpleName().equals("BallRepelShield")) {
					ArrayList<Double> direction = getDirectionToMapObject(e);
					knockback(
						direction.get(0),
						direction.get(1),
						((BallRepelShield) e).getKnockbackForce(),
						true,
						false
					);
				}
				else if(e.getClass().getSimpleName().equals("SentryGemGreen")) {
					hit(
						e.getDamage(),
						((SentryGemGreen) e).getAttackEnergyDamage(),
						((SentryGemGreen) e).getRewindEnergyDamage()
					);
				}
				else if(e.getClass().getSimpleName().equals("BossBall5GemGreen")) {
					hit(
						e.getDamage(),
						((BossBall5GemGreen) e).getAttackEnergyDamage(),
						((BossBall5GemGreen) e).getRewindEnergyDamage()
					);
				}
				else if(e.getClass().getSimpleName().equals("BossTurretGemGreen")) {
					hit(
						e.getDamage(),
						((BossTurretGemGreen) e).getAttackEnergyDamage(),
						((BossTurretGemGreen) e).getRewindEnergyDamage()
					);
				}
				else if(e.getClass().getSimpleName().equals("BossBall1Particle")) {
					ArrayList<Double> direction = getDirectionToMapObject(e);
					knockback(
						direction.get(0),
						direction.get(1),
						((BossBall1Particle) e).getKnockbackForce(),
						true,
						false
					);
				}
				else if(e.getClass().getSimpleName().equals("BossBall2")) {
					hit(e.getDamage(), 0, 0);
					setDizzy();
					setConfused();
				}
				else if(e.getClass().getSimpleName().equals("BossBall3")) {
					speedMultiplier *= 0.88;
					attackEnergy -= attackRegeneration * 1.5;
					if(attackEnergy < 0) attackEnergy = 0;
				}
				else if(e.getClass().getSimpleName().equals("BossShield")) {
					ArrayList<Double> direction = getDirectionToMapObject(e);
					knockback(
						direction.get(0),
						direction.get(1),
						((BossShield) e).getKnockbackForce(),
						true,
<<<<<<< HEAD
						false
=======
						true
>>>>>>> 2.03
					);
					
					if(e.getx() == getx() && e.gety() == gety()) {
						grabbed = true;
					}
				}
				else if(e.getClass().getSimpleName().equals("BossWall")) {
					hit(e.getDamage(), 0, 0);
					setSealed();
				}
				else {
					hit(e.getDamage(), 0, 0);
				}
			}
		}
	}

	private void setDizzy() {
		if(!dizzy) {
			JukeBox.playWithRecommendedVolume("dizzy");
			dizzy = true;
		}
		dizzyStart = System.nanoTime();
	}

	private void setConfused() {
		if(!confused) {
			JukeBox.playWithRecommendedVolume("confused");
			confused = true;
		}
		confusedStart = System.nanoTime();
	}

	private void setSealed() {
		if(!sealed) {
			JukeBox.playWithRecommendedVolume("sealed");
			sealed = true;
		}
		sealedStart = System.nanoTime();
	}

	public boolean intersectsWithPortals(ArrayList<Portal> portals) {
		
		// loop through portals
		for(int i = 0; i < portals.size(); i++) {
			Portal p = portals.get(i);
			
			// check portal collision
			if(intersects(p)) {
				return true;
			}
		}
		
		return false;
	}
	
	public void hit(double healthDamage, int attackEnergyDamage, int rewindEnergyDamage) {
<<<<<<< HEAD
		// parrying can be done even when flinching to make the timing a bit less awkward
=======
		// parrying can be done even when flinching to make the timing less awkward
>>>>>>> 2.03
		if(activeParrying) {
			if(initialParry) {
				hitInitialParry();
			}
			return;
		}
		
		if(flinching) return;
		
		health -= healthDamage * Support.difficultyDamageMultiplier;
		if(health < 0) health = 0;
		if((int) health == 0) {
			kill();
		}
		
		attackEnergy -= attackEnergyDamage * Support.difficultyDamageMultiplier;
		if(attackEnergy < 0) attackEnergy = 0;
		
		rewindEnergy -= rewindEnergyDamage * Support.difficultyDamageMultiplier;
		if(rewindEnergy < 0) rewindEnergy = 0;
		
		flinching = true;
		flinchTimer = System.nanoTime();
		
		JukeBox.playWithRecommendedVolume("playerhit");
	}
	
	private void checkFallingDeath() {
		if(y > tileMap.getHeight() - height / 2 -1 + 300) {
			kill();
		}
	}
	
	public void kill() {
		if(dead) return;
		
		dead = true;
		health = 0;
		spawnPlayerFraction = true;
		spawnPlayerCore = true;
	}
	
	// POSSIBLE RARE ERROR
	private void getNextPosition() {
		// active parrying cancels movement
		if(activeParrying && !initialParry) {
			dx = 0;
			dy = 0;
		}
		// so does being grabbed
		else if(grabbed) {
			dx = 0;
			dy = 0;
		}
		else {
<<<<<<< HEAD
			//System.out.println("P next pos 1");
			// dashjumping, disables free x movement and ends once dx has slowed down to abs of maxSpeed
			if(dashJumping) {

				//System.out.println("P next pos 1.1");
				// slows down dx gradually while dashjumping if it's over abs of maxSpeed, ends the dashjump otherwise
				if(Math.abs(dx) > maxSpeed) {
					//System.out.println("P next pos 1.2");
					// speed after slowdown
					dx = Math.abs(dx) - xDashStopSpeed;
					//System.out.println("P next pos 1.3");
				}
				else {
					//System.out.println("P next pos 1.4");
					dashJumping = false;
				}

				//System.out.println("P next pos 1.5");
=======
			// dashjumping, disables free x movement and ends once dx has slowed down to abs of maxSpeed
			if(dashJumping) {
				
				// slows down dx gradually while dashjumping if it's over abs of maxSpeed, ends the dashjump otherwise
				if(Math.abs(dx) > maxSpeed) {
					// speed after slowdown
					dx = Math.abs(dx) - xDashStopSpeed;
				}
				else {
					dashJumping = false;
				}
				
>>>>>>> 2.03
				// speed direction reversed if facing left
				if(!facingRight) dx = Math.abs(dx) * (-1);
			}
			// if not dashjumping, do default x movement check
			else {
<<<<<<< HEAD
				//System.out.println("P next pos 1.6");
				// x movement
				if(left) {
					//System.out.println("P next pos 1.7");
=======
				// x movement
				if(left) {
>>>>>>> 2.03
					dx -= moveSpeed;
					if(dx < -maxSpeed) {
						dx = -maxSpeed;
					}
				}
				else if(right) {
<<<<<<< HEAD
					//System.out.println("P next pos 1.8");
=======
>>>>>>> 2.03
					dx += moveSpeed;
					if(dx > maxSpeed) {
						dx = maxSpeed;
					}
				}
				else {
<<<<<<< HEAD
					//System.out.println("P next pos 1.9");
=======
>>>>>>> 2.03
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
				}
			}
<<<<<<< HEAD
			//System.out.println("P next pos 2");
			
			// jumping
			if(jumping && !falling || jumping && Support.cheatInfiniteJump) {
				//System.out.println("P next pos 3");
				JukeBox.playWithRecommendedVolume("playerjump");
				//System.out.println("P next pos 4");
				dy = jumpStart;
				//System.out.println("P next pos 5");
				falling = true;
				//System.out.println("P next pos 6");
			}

			//System.out.println("P next pos 7");
			long timePassedFromLastLeftWallContact = (System.nanoTime() - lastTouchedLeftWall) / 1000000;
			//System.out.println("P next pos 8");
			long timePassedFromLastRightWallContact = (System.nanoTime() - lastTouchedRightWall) / 1000000;
			//System.out.println("P next pos 9");
=======
			
			// jumping
			if(jumping && !falling || jumping && Support.cheatInfiniteJump) {
				JukeBox.playWithRecommendedVolume("playerjump");
				dy = jumpStart;
				falling = true;
			}
			
			long timePassedFromLastLeftWallContact = (System.nanoTime() - lastTouchedLeftWall) / 1000000;
			long timePassedFromLastRightWallContact = (System.nanoTime() - lastTouchedRightWall) / 1000000;
>>>>>>> 2.03
			
			// dashing activates if the button is pressed and player hasn't dashed since last landing or if player has been in contact with a wall recently
			if(
				(
					dashing &&
					(
						firstDashBeforeLanding ||
						touchingLeftWall ||
						touchingRightWall ||
						timePassedFromLastLeftWallContact < wallContactGracePeriod ||
						timePassedFromLastRightWallContact < wallContactGracePeriod
					)
				) ||
				(dashing && Support.cheatInfiniteDashJump)
			) {
<<<<<<< HEAD
				//System.out.println("P next pos 9.1");
				JukeBox.playWithRecommendedVolume("playerdashjump");
				//System.out.println("P next pos 9.2");
=======
				JukeBox.playWithRecommendedVolume("playerdashjump");
>>>>>>> 2.03
				if(
						(facingRight && !(touchingRightWall || timePassedFromLastRightWallContact < wallContactGracePeriod)) ||
						(touchingLeftWall || timePassedFromLastLeftWallContact < wallContactGracePeriod)
				) {
<<<<<<< HEAD
					//System.out.println("P next pos 22");
					dx = xDashJumpStart;
					//System.out.println("P next pos 23");
					facingRight = true;
					//System.out.println("P next pos 24");
				}
				else {
					//System.out.println("P next pos 25");
					dx = -xDashJumpStart;
					//System.out.println("P next pos 26");
					facingRight = false;
					//System.out.println("P next pos 27");
				}
				//System.out.println("P next pos 28");
				dy = yDashJumpStart;
				//System.out.println("P next pos 29");
				firstDashBeforeLanding = false;
				//System.out.println("P next pos 33");
				dashJumping = true;
				//System.out.println("P next pos 34");
				falling = true;
				//System.out.println("P next pos 35");
				lastTouchedLeftWall = 0;
				//System.out.println("P next pos 36");
				lastTouchedRightWall = 0;
				//System.out.println("P next pos 37");
			}

			//System.out.println("P next pos 43");
			// falling
			if(falling) {
				//System.out.println("P next pos 44");
				dy += fallSpeed;

				//System.out.println("P next pos 45");
				////System.out.println("falling with dy of "+dy);
				
				if(dy > 0) {
					//System.out.println("P next pos 46");
					jumping = false;
					////System.out.println("dy > 0 so it's going down and jumping = "+jumping);
				}
				//System.out.println("P next pos 47");
=======
					dx = xDashJumpStart;
					facingRight = true;
				}
				else {
					dx = -xDashJumpStart;
					facingRight = false;
				}
				dy = yDashJumpStart;
				firstDashBeforeLanding = false;
				dashJumping = true;
				falling = true;
				lastTouchedLeftWall = 0;
				lastTouchedRightWall = 0;
			}
			
			// falling
			if(falling) {
				dy += fallSpeed;
				
				if(dy > 0) {
					jumping = false;
				}
				
>>>>>>> 2.03
				if(
					(dy < 0 && !jumping) ||
					(dy < 0 && dashJumping) ||
					(dy < 0 && (timePassedFromLastLeftWallContact < wallContactGracePeriod || timePassedFromLastRightWallContact < wallContactGracePeriod) && jumping && firstDashBeforeLanding == false) ||
					dy < jumpStart
				) {
<<<<<<< HEAD
					//System.out.println("P next pos 48");
					dy += stopJumpSpeed;
					////System.out.println("dy < 0 and !jumping while firstDash now is"+firstDashBeforeLanding+" and dashJumping now is "+dashJumping);
				}
				//System.out.println("P next pos 49");

				if(dy > maxFallSpeed) dy = maxFallSpeed;
				//System.out.println("P next pos 55");
			}
			// resets certain booleans if not falling
			else {
				//System.out.println("P next pos 56");
				firstDashBeforeLanding = true;
				//System.out.println("P next pos 57");
				dashJumping = false;
			}

			//System.out.println("P next pos 58");
			// applies speedMultiplier to the final values
			dx *= speedMultiplier;
			dy *= speedMultiplier;
			//System.out.println("P next pos 59");
=======
					dy += stopJumpSpeed;
				}

				if(dy > maxFallSpeed) dy = maxFallSpeed;
			}
			// resets certain booleans if not falling
			else {
				firstDashBeforeLanding = true;
				dashJumping = false;
			}
			
			// applies speedMultiplier to the final values
			dx *= speedMultiplier;
			dy *= speedMultiplier;
>>>>>>> 2.03
		}
	}
	
	private void updateCurrentAnimation() {
		// set animation
		int lastFrame;
		if(dy > 0) {
			if(currentAction != FALLING) {
				currentAction = FALLING;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(40);
				animation.setFrame(lastFrame);
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(40);
				animation.setFrame(lastFrame);
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				animation.setFrame(lastFrame);
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(-1);
				animation.setFrame(lastFrame);
			}
		}
	}

	public void update() {
		// some cheats can be toggled freely if cheatToggling is on
		handleQuickCheats();
		
		// player just died and either respawned or game overed
		// in case of respawn certain values need resetting
		if(moveOnWithDeath) resetDeathValues();
		
		// update player normally only if alive
		if(!isDead()) {
			if(rewindButtonInitialPress && rewindEnergy < rewindInitialCost) {
				int energyCurrent = (int) (rewindEnergy / 1000);
				int energyNeeded = (int) (rewindInitialCost / 1000);
				rewindEnergyAnnouncement = energyCurrent + "/" + energyNeeded;
				spawnRewindEnergyAnnouncement = true;
			}
			
			// player is treated as in the middle of rewinding if...
			// 1) the button is currently pressed, wasn't already pressed last frame and enough energy to cover the initial cost OR
			// 2) one of the rewind stages is active, which will automatically end as needed
			rewindingTerms = (rewindButton && rewindEnergy >= rewindInitialCost) ||
					rewindStartPause ||
					rewindActiveTime ||
					rewindEndPause;
			
			// going back in time
			if(rewindingTerms) {
				updateRewinding();
			}
			// normal update
			else {
				updateNormal();
			}
		}
		// player is dead
		else {
			updateDead();
		}
	}

	private void updateRewinding() {
		// after the first time this runs
		if(rewindStartPause || rewindActiveTime || rewindEndPause) {
			rewindPlayer();
		}
		// first time starts the pause period and performs other changes related to it like monochrome colors
		else {
			performRewindInitialChanges();
		}
	}
	
	// changes done on first rewinding frame
	private void performRewindInitialChanges() {
		rewindStartPause = true;
		rewindActiveTime = false;
		rewindEndPause = false;
		rewindStartPauseTime = System.nanoTime();
		rewindEnergy -= rewindInitialCost;
		changeColors = true;
		Support.surroundingsColorMode = Support.MONOCHROME;
		JukeBox.playWithRecommendedVolume("rewindstart");
	}
	
	// things that happen during the pause at start of rewinding
	private void performRewindStartPauseChanges() {
		long elapsed = (System.nanoTime() - rewindStartPauseTime) / 1000000;
		if(!rewindButton || elapsed > rewindStartPauseTotalTime) {
			rewindStartPause = false;
			rewindActiveTime = true;
			rewindEndPause = false;
			JukeBox.playWithRecommendedVolume("rewind");
		}
	}
	
	// things that happen each frame while actively rewinding
	private void performRewindActiveTimeChanges() {
		rewindEnergy -= rewindUpkeepCost;
		loadPlayerPastData();
		discardPlayerPastData();
		if(!rewindButton || rewindEnergy < rewindUpkeepCost) {
			rewindStartPause = false;
			rewindActiveTime = false;
			rewindEndPause = true;
			rewindEndPauseTime = System.nanoTime();
			JukeBox.stop("rewind");
			JukeBox.playWithRecommendedVolume("rewindstart");
		}
	}
	
	// changes done when active rewinding enters the last pause stage
	private void performRewindEndPauseChanges() {
		long elapsed = (System.nanoTime() - rewindEndPauseTime) / 1000000;
		if(elapsed > rewindEndPauseTotalTime) {
			rewindStartPause = false;
			rewindActiveTime = false;
			rewindEndPause = false;
			changeColors = true;
			Support.surroundingsColorMode = Support.COLORED;
			loadTimeDependentPlayerPastData();
			flinching = true;
			flinchTimer = System.nanoTime();
		}
	}

	private void rewindPlayer() {
		// after gray color update this becomes false so they don't keep updating in vain
		if(changeColors) changeColors = false;
		
		if(rewindStartPause) {
			performRewindStartPauseChanges();
		}
		else if(rewindActiveTime) {
			performRewindActiveTimeChanges();
		}
		else if(rewindEndPause) {
			performRewindEndPauseChanges();
		}
		else {
			System.out.println("Error in rewindPlayer, no matching rewind state found, defaulting to starting pause.");
			performRewindInitialChanges();
		}
	}
	
	private void updateNormal() {
		// returns changeColors to false after the line below makes it true and the next frame is handled
		// needs to be before
		if(changeColors) changeColors = false;

		regenerateHealth();
		regenerateAttack();
		regenerateRewind();
		
		// spawn all kinds of player attacks if needed
		handleAttacks();
		
		// start/stop activeParrying if needed
		handleParrying();
		
		checkFallingDeath();
		
		updateStatusEffects();
		
<<<<<<< HEAD
		//System.out.println("UPDATE BEFORE getNextPosition()");
		
		// update position
		getNextPosition(); //not in here?
		//System.out.println("UPDATE AFTER getNextPosition()");
		checkTileMapCollision();
		//System.out.println("UPDATE AFTER checkTileMapCollision()");
		setPosition(xtemp, ytemp);
		//System.out.println("UPDATE AFTER setPosition()");
=======
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
>>>>>>> 2.03
		
		// check done flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > flinchTime) {
				flinching = false;
			}
		}
		
<<<<<<< HEAD
		// NOTE: these have reversed order in enemy classes, may need to study closer if there are unwanted effects
=======
>>>>>>> 2.03
		animation.update();
		updateCurrentAnimation();
		
		if(!dashJumping) {
			if(right) facingRight = true;
			if(left) facingRight = false;
		}
		
		savePlayerPastData();
		updatePlayerPastData();
<<<<<<< HEAD
		//System.out.println("UPDATE AFTER updatePlayerPastData()");
=======
>>>>>>> 2.03
	}

	private void updateDead() {
		dx = 0;
		dy = 0;
		
		if(!deathTextCountdownActivated && !deathLifeSubtractionCountdownActivated && !deathRespawnCountdownActivated) {
			activateDeathTextCountdown();
			deathTextCountdownActivated = true;
		}
		else if(deathTextCountdownActivated && !deathLifeSubtractionCountdownActivated && !deathRespawnCountdownActivated) {
			long elapsed = (System.nanoTime() - deathTextCountdownActivationTime) / 1000000;
			
			if(elapsed > deathTextCountdownWaitTime) {
				drawDeathText = true;
				
				activateDeathLifeSubtractionCountdown();
				deathLifeSubtractionCountdownActivated = true;
			}
		}
		else if(deathTextCountdownActivated && deathLifeSubtractionCountdownActivated && !deathRespawnCountdownActivated) {
			long elapsed = (System.nanoTime() - deathLifeSubtractionCountdownActivationTime) / 1000000;
			
			if(elapsed > deathLifeSubtractionCountdownWaitTime) {
				lives--;
				
				activateDeathRespawnCountdown();
				deathRespawnCountdownActivated = true;
			}
		}
		else if(deathTextCountdownActivated && deathLifeSubtractionCountdownActivated && deathRespawnCountdownActivated) {
			long elapsed = (System.nanoTime() - deathRespawnCountdownActivationTime) / 1000000;
			
			if(elapsed > deathRespawnCountdownWaitTime) {
				moveOnWithDeath = true;
				if(lives > 0) respawn = true;
				else respawn = false;
			}
		}
	}

	// quick cheats can only be toggled when quickCheats is on
	private void handleQuickCheats() {
		if(!Support.quickCheats) return;
		if(togglingInvulnerability) invulnerable = !invulnerable;
		if(togglingInfiniteJump) Support.cheatInfiniteJump = !Support.cheatInfiniteJump;
		if(togglingHealthRegenerationHaste) Support.cheatHealthRegen = !Support.cheatHealthRegen;
		if(togglingAttackRegenerationHaste) Support.cheatAttackRegen = !Support.cheatAttackRegen;
		if(togglingRewindRegenerationHaste) Support.cheatRewindRegen = !Support.cheatRewindRegen;
		if(suiciding) kill();
	}

	private void handleAttacks() {
		if(weakAttacking) {
			if(attackEnergy >= weakAttackEnergyCost) {
				spawnWeakAttack = true;
				attackEnergy -= weakAttackEnergyCost;
			}
			else {
				int energyCurrent = (int) (attackEnergy / 1000);
				int energyNeeded = (int) (weakAttackEnergyCost / 1000);
				weakAttackEnergyAnnouncement = energyCurrent + "/" + energyNeeded;
				spawnWeakAttackEnergyAnnouncement = true;
			}
		}
		
		if(strongAttacking) {
			if(attackEnergy >= strongAttackEnergyCost) {
				spawnStrongAttack = true;
				attackEnergy -= strongAttackEnergyCost;
			}
			else {
				int energyCurrent = (int) (attackEnergy / 1000);
				int energyNeeded = (int) (strongAttackEnergyCost / 1000);
				strongAttackEnergyAnnouncement = energyCurrent + "/" + energyNeeded;
				spawnStrongAttackEnergyAnnouncement = true;
			}
		}
	}

	private void handleParrying() {
		checkActiveParryingStart();
		checkActiveParryingStop();
	}
	
	// when not activeParrying, check if it's time to start
	private void checkActiveParryingStart() {
<<<<<<< HEAD
=======
		//parrying here simply refers to the parrying key being pressed, has nothing to do with actually being in a parrying ready state or invulnerable from parrying
>>>>>>> 2.03
		if(parrying) {
			long elapsed = (System.nanoTime() - parryPreStartTime) / 1000000;
			
			if(activeParrying && !hasFailedParryChain) {
				// if parrying, it needs to happen during parryChainWindow
<<<<<<< HEAD
				if(elapsed >= parryActiveDuration - parryChainWindow) {
=======
				if(elapsed >= parryActiveDuration - parryChainWindow || Support.cheatInfiniteParry) {
>>>>>>> 2.03
					activateParrying();
				}
				else {
					hasFailedParryChain = true;
					parryCurrentCooldown = parryCooldown;
				}
			}
			else {
				// if not parrying, it needs to happen after parryCooldown is done
<<<<<<< HEAD
				if(elapsed >= parryCurrentCooldown) {
=======
				if(elapsed >= parryCurrentCooldown || Support.cheatInfiniteParry) {
>>>>>>> 2.03
					activateParrying();
				}
				else {
					// cooldown in rough format of 2 digits less --> 3456 becomes 34
					int parryCooldownLeftRoughInt = ((int) parryCurrentCooldown - (int) elapsed) / 100;
					parryCooldownAnnouncement = (double) parryCooldownLeftRoughInt / 10.0;
					spawnParryCooldownAnnouncement = true;
				}
			}
		}
	}
	
	// when activeParrying, check if it's time to stop
	private void checkActiveParryingStop() {
		if(activeParrying) {
			//parrying resets after differing times depending on if it catches an enemy hit or not
			if(initialParry) {
				long elapsed = (System.nanoTime() - parryPreStartTime) / 1000000;
				
				if(elapsed > parryPreDuration) {
					deactivateParrying();
				}
			}
			else {
				long elapsed = (System.nanoTime() - parryActiveStartTime) / 1000000;
				
				if(elapsed > parryActiveDuration) {
					deactivateParrying();
				}
			}
		}
	}

	private void activateParrying() {
		activeParrying = true;
		initialParry = true;
		parryPreStartTime = System.nanoTime();
		parryCurrentCooldown = parryCooldown;
		spawnParryShield = true;
		hasFailedParryChain = false;
	}

	private void deactivateParrying() {
		activeParrying = false;
	}
	
	private void hitInitialParry() {
		initialParry = false;
		parryActiveStartTime = System.nanoTime();
		JukeBox.playWithRecommendedVolume("playerparry");
		spawnParryShieldImpact = true;
		parryCurrentCooldown = 0;
	}

	private void activateDeathTextCountdown() {
		deathTextCountdownActivationTime = System.nanoTime();
	}

	private void activateDeathLifeSubtractionCountdown() {
		deathLifeSubtractionCountdownActivationTime = System.nanoTime();
	}

	private void activateDeathRespawnCountdown() {
		deathRespawnCountdownActivationTime = System.nanoTime();
	}

	public void resetDeathValues() {
		deathTextCountdownActivated = false;
		deathLifeSubtractionCountdownActivated = false;
		deathRespawnCountdownActivated = false;
		drawDeathText = false;
		moveOnWithDeath = false;
		health = maxHealth;
		attackEnergy = maxAttackEnergy;
		rewindEnergy = maxRewindEnergy;
		dead = false;
		dizzy = false;
		confused = false;
		sealed = false;
		grabbed = false;
		showDialog = false;
	}

	public void resetStatusEffects() {
		dizzy = false;
		confused = false;
		sealed = false;
		grabbed = false;
	}

	public void resetSpeed() {
		dx = 0.0;
		dy = 0.0;
	}

	private void regenerateHealth() {
		if(sealed) return;
		health += healthRegeneration;
		if(Support.cheatHealthRegen) health += healthCheatRegeneration;
		if(health > maxHealth) health = maxHealth;
	}

	private void regenerateAttack() {
		if(sealed) return;
		attackEnergy += attackRegeneration;
		if(Support.cheatAttackRegen) attackEnergy += attackCheatRegeneration;
		if(attackEnergy > maxAttackEnergy) attackEnergy = maxAttackEnergy;
	}

	private void regenerateRewind() {
		if(sealed) return;
		rewindEnergy += rewindRegeneration;
		if(Support.cheatRewindRegen) rewindEnergy += rewindCheatRegeneration;
		if(rewindEnergy > maxRewindEnergy) rewindEnergy = maxRewindEnergy;
	}

	private void savePlayerPastData() {
		PlayerPastData ppd = new PlayerPastData();
		ppd.id = Support.id;
		Support.id++;
		ppd.xmap = xmap;
		ppd.ymap = ymap;
		ppd.x = x;
		ppd.y = y;
		ppd.dx = dx;
		ppd.dy = dy;
		ppd.currentAction = currentAction;
		ppd.left = left;
		ppd.right = right;
		ppd.jumping = jumping;
		ppd.falling = falling;
		ppd.touchingLeftWall = touchingLeftWall;
		ppd.touchingRightWall = touchingRightWall;
		ppd.lastTouchedLeftWall = lastTouchedLeftWall;
		ppd.lastTouchedRightWall = lastTouchedRightWall;
		ppd.facingRight = facingRight;
		ppd.health = health;
		ppd.attackEnergy = attackEnergy;
		ppd.dashing = dashing;
		ppd.dashJumping = dashJumping;
		ppd.firstDashBeforeLanding = firstDashBeforeLanding;
		ppd.dizzy = dizzy;
		ppd.dizzyStart = dizzyStart;
		ppd.confused = confused;
		ppd.confusedStart = confusedStart;
		ppd.sealed = sealed;
		ppd.sealedStart = sealedStart;
		ppd.currentTime = System.nanoTime();
		ppd.currentFrame = animation.getFrame();
		ppd.currentDelay = animation.getDelay();
		playerPastData.add(ppd);
	}
	
	// discards too old data
	private void updatePlayerPastData() {
		if(playerPastData.size() >= 2) {
			while(true) {
				long oldestMoment = playerPastData.get(0).currentTime / 1000000;
				long latestMoment = playerPastData.get(playerPastData.size() - 1).currentTime / 1000000;
				if(latestMoment - oldestMoment > pastDataSaveTime) {
					playerPastData.remove(0);
				}
				else {
					break;
				}
			}
		}
	}

	// latest is loaded, returns load success state
	private boolean loadPlayerPastData() {
		if(!playerPastData.isEmpty()) {
			PlayerPastData ppd = playerPastData.get(playerPastData.size() - 1);
			xmap = ppd.xmap;
			ymap = ppd.ymap;
			x = ppd.x;
			y = ppd.y;
			dx = ppd.dx;
			dy = ppd.dy;
			currentAction = ppd.currentAction;
			left = ppd.left;
			right = ppd.right;
			jumping = ppd.jumping;
			falling = ppd.falling;
			touchingLeftWall = ppd.touchingLeftWall;
			touchingRightWall = ppd.touchingRightWall;
			lastTouchedLeftWall = System.nanoTime() - (ppd.currentTime - ppd.lastTouchedLeftWall);
			lastTouchedRightWall = System.nanoTime() - (ppd.currentTime - ppd.lastTouchedRightWall);
			facingRight = ppd.facingRight;
			health = ppd.health;
			attackEnergy = ppd.attackEnergy;
			dashing = ppd.dashing;
			dashJumping = ppd.dashJumping;
			firstDashBeforeLanding = ppd.firstDashBeforeLanding;
			dizzy = ppd.dizzy;
			dizzyStart = ppd.dizzyStart;
			confused = ppd.confused;
			confusedStart = ppd.confusedStart;
			sealed = ppd.sealed;
			sealedStart = ppd.sealedStart;
			ppdCurrentTime = ppd.currentTime;
			animation.setFrame(ppd.currentFrame);
			animation.setDelay(ppd.currentDelay);
			
			return true;
		}
		
		return false;
	}
	
	// latest is discarded, used after a load when it's not needed anymore
	private void discardPlayerPastData() {
		if(!playerPastData.isEmpty()) {
			playerPastData.remove(playerPastData.size() - 1);
		}
	}
	
	// anything that uses System.nanoTime() etc. is loaded at the same time when player is able to move again as time moves on and needs to be precise
	// this doesn't take data from a PlayerPastData object, but instead straight from the Player object that were loaded in during last frame of rewindActiveTime
	private void loadTimeDependentPlayerPastData() {
		// since time moves forward you have to do some math to get the new start time for status effects so they have the correct amount of duration left
		// in other words statusStart = System.nanoTime() - timePassedOfStatusStart
		long dizzyTimePassed = ppdCurrentTime - dizzyStart;
		dizzyStart = System.nanoTime() - dizzyTimePassed;
		long confusedTimePassed = ppdCurrentTime - confusedStart;
		confusedStart = System.nanoTime() - confusedTimePassed;
		long sealedTimePassed = ppdCurrentTime - sealedStart;
		sealedStart = System.nanoTime() - sealedTimePassed;
	}

	private void updateStatusEffects() {
		updateDizzyStatus();
		updateConfusedStatus();
		updateSealedStatus();
	}

	private void updateDizzyStatus() {
		if(dizzy) {
			long dizzyTimePassed = (System.nanoTime() - dizzyStart) / 1000000;
			dizzyDurationLeft = (int) ((dizzyDuration - dizzyTimePassed) / 1000 + 1);
			if(dizzyTimePassed > dizzyDuration) dizzy = false;
		}
	}

	private void updateConfusedStatus() {
		if(confused) {
			long confusedTimePassed = (System.nanoTime() - confusedStart) / 1000000;
			confusedDurationLeft = (int) ((confusedDuration - confusedTimePassed) / 1000 + 1);
			if(confusedTimePassed > confusedDuration) confused = false;
		}
	}

	private void updateSealedStatus() {
		if(sealed) {
			long sealedTimePassed = (System.nanoTime() - sealedStart) / 1000000;
			sealedDurationLeft = (int) ((sealedDuration - sealedTimePassed) / 1000 + 1);
			if(sealedTimePassed > sealedDuration) sealed = false;
		}
	}
	
	public void draw(Graphics2D g) {
		if(dead) return;
		
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed / 100 % 2 == 0) {
				return;
			}
		}
		
		setMapPosition();
		
		super.draw(g);
		
	}
	
	@Override
	public void setLeft(boolean b) {
		if(confused) right = b;
		else left = b;
	}
	
	@Override
	public void setRight(boolean b) {
		if(confused) left = b;
		else right = b;
	}
	
	@Override
	public void checkTileMapCollision() {
		super.checkTileMapCollision();
		
		// check if object is next to a wall while not moving in x direction
		if(dx == 0) {
			calculateCorners(x - 1, y);
			if(topLeft || bottomLeft) {
				touchingLeftWall = true;
				lastTouchedLeftWall = System.nanoTime();
			}
			calculateCorners(x + 1, y);
			if(topRight || bottomRight) {
				touchingRightWall = true;
				lastTouchedRightWall = System.nanoTime();
			}
		}
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		// explosion cant be enemy in ally list
		if(spawnPlayerFraction) {
			EntitySpawnData esd = new EntitySpawnData(
				new PlayerFracture(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			spawnPlayerFraction = false;
		}
		
		if(spawnPlayerCore) {
			PlayerCoreExtra ed = new PlayerCoreExtra(0, -1.4);
			EntitySpawnData esd = new EntitySpawnData(
				new PlayerCore(tileMap, 0, ed),
				getx(),
				gety() - width / 2
			);
			esdList.add(esd);
			spawnPlayerCore = false;
		}
		
		if(spawnParryShield) {
			EntitySpawnData esd = new EntitySpawnData(
				new ParryShield(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			spawnParryShield = false;
		}
		
		if(spawnParryShieldImpact) {
			EntitySpawnData esd = new EntitySpawnData(
				new ParryShieldImapct(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			spawnParryShieldImpact = false;
		}
		
		if(spawnStrongAttack) {
			/*double dxEd = 0;
			double dyEd = 0;
			dxEd += dx;
			dyEd += dy;
			AttackStrongExtra ed = new AttackStrongExtra(dxEd, dyEd);*/
			EntitySpawnData esd = new EntitySpawnData(
				new PlayerAttackStrong(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			spawnStrongAttack = false;
		}
		
		if(spawnWeakAttack) {
			double dxEd = 0;
			double dyEd = 0;
			if(facingRight) dxEd = 3.0;
			else dxEd = -3.0;
			dxEd += dx;
			AttackWeakExtra ed = new AttackWeakExtra(dxEd, dyEd, facingRight);
			EntitySpawnData esd = new EntitySpawnData(
				new PlayerAttackWeak(tileMap, 0, ed),
				getx(),
				gety()
			);
			esdList.add(esd);
			spawnWeakAttack = false;
		}
		
		if(spawnParryCooldownAnnouncement) {
			EntitySpawnData esd = new EntitySpawnData(
				new Announcement(
					tileMap,
					parryCooldownAnnouncement,
					parryCooldownAnnouncementDx,
					parryCooldownAnnouncementDy,
					parryCooldownAnnouncementLifeTime,
					null,
					null
				),
				getx() + parryCooldownAnnouncementX,
				gety() + parryCooldownAnnouncementY
			);
			esdList.add(esd);
			
			spawnParryCooldownAnnouncement = false;
		}
		
		if(spawnWeakAttackEnergyAnnouncement) {
			EntitySpawnData esd = new EntitySpawnData(
				new Announcement(
					tileMap,
					weakAttackEnergyAnnouncement,
					weakAttackEnergyAnnouncementDx,
					weakAttackEnergyAnnouncementDy,
					weakAttackEnergyAnnouncementLifeTime,
					null,
					null
				),
				getx() + weakAttackEnergyAnnouncementX,
				gety() + weakAttackEnergyAnnouncementY
			);
			esdList.add(esd);
			
			spawnWeakAttackEnergyAnnouncement = false;
		}
		
		if(spawnStrongAttackEnergyAnnouncement) {
			EntitySpawnData esd = new EntitySpawnData(
				new Announcement(
					tileMap,
					strongAttackEnergyAnnouncement,
					strongAttackEnergyAnnouncementDx,
					strongAttackEnergyAnnouncementDy,
					strongAttackEnergyAnnouncementLifeTime,
					null,
					null
				),
				getx() + strongAttackEnergyAnnouncementX,
				gety() + strongAttackEnergyAnnouncementY
			);
			esdList.add(esd);
			
			spawnStrongAttackEnergyAnnouncement = false;
		}
		
		if(spawnRewindEnergyAnnouncement) {
			EntitySpawnData esd = new EntitySpawnData(
				new Announcement(
					tileMap,
					rewindEnergyAnnouncement,
					rewindEnergyAnnouncementDx,
					rewindEnergyAnnouncementDy,
					rewindEnergyAnnouncementLifeTime,
					null,
					null
				),
				getx() + rewindEnergyAnnouncementX,
				gety() + rewindEnergyAnnouncementY
			);
			esdList.add(esd);
			
			spawnRewindEnergyAnnouncement = false;
		}
		
		return esdList;
	}
}




/*
 * 

	private void updateCurrentAnimation() {
		// set animation
		int lastFrame;
		if(dy > 0) {
			if(currentAction != FALLING) {
				currentAction = FALLING;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(FALLING));
				animation.setDelay(40);
				animation.setFrame(lastFrame);
				width = 30;
			}
		}
		else if(dy < 0) {
			if(currentAction != JUMPING) {
				currentAction = JUMPING;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(JUMPING));
				animation.setDelay(40);
				animation.setFrame(lastFrame);
				width = 30;
			}
		}
		else if(left || right) {
			if(currentAction != WALKING) {
				currentAction = WALKING;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(WALKING));
				animation.setDelay(40);
				animation.setFrame(lastFrame);
				width = 30;
			}
		}
		else {
			if(currentAction != IDLE) {
				currentAction = IDLE;
				lastFrame = animation.getFrame();
				animation.setFrames(sprites.get(IDLE));
				animation.setDelay(-1);
				animation.setFrame(lastFrame);
				width = 30;
			}
		}
	}
 */






