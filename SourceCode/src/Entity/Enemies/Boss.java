package Entity.Enemies;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Support.Support;
import TileMap.TileMap;
import Audio.JukeBox;
import Entity.*;
import EntityExtraData.BossBall1Extra;
import EntityExtraData.BossBall3Extra;
import EntityExtraData.BossBall4ChaseExtra;
import EntityExtraData.BossBall4SplitExtra;
import EntityExtraData.BossBall6Extra;
import EntityExtraData.BossBall7Extra;
import EntityExtraData.BossFinalAttackListExtra;
import EntityExtraData.BossFinalAttackSpawnerExtra;
import EntityExtraData.BossGemPieceExtra;
import EntityExtraData.BossTurretListExtra;
import EntityExtraData.BossTurretSpawnerExtra;
import Handlers.Content;
import Handlers.DialogConversation;

public class Boss extends Enemy {
	// attacks only spawn if true
	@SuppressWarnings("unused")
	private boolean aggressive;
	
	// copy of GameState dialog, needed for certain phase advancement
	private Dialog dialog;
	
	private BufferedImage[] frameSprites;
	private BufferedImage[] gemGreenSprites;
	private BufferedImage[] gemYellowSprites;
	private BufferedImage[] gemRedSprites;
	private BufferedImage[] gemPaleSprites;
	private BufferedImage[] gemGreenMiddleSprites;
	private BufferedImage[] gemYellowMiddleSprites;
	private BufferedImage[] gemRedMiddleSprites;
	private BufferedImage[] gemPaleMiddleSprites;
	private BufferedImage[] shieldSprites;
	private BufferedImage[] portalTopSprites;
	private BufferedImage[] portalBottomSprites;
	private BufferedImage[] portalLeftSprites;
	private BufferedImage[] portalRightSprites;
	private BufferedImage[] invisibleSprites;
	
	private BufferedImage[] frameSpritesC;
	private BufferedImage[] gemGreenSpritesC;
	private BufferedImage[] gemYellowSpritesC;
	private BufferedImage[] gemRedSpritesC;
	private BufferedImage[] gemPaleSpritesC;
	private BufferedImage[] gemGreenMiddleSpritesC;
	private BufferedImage[] gemYellowMiddleSpritesC;
	private BufferedImage[] gemRedMiddleSpritesC;
	private BufferedImage[] gemPaleMiddleSpritesC;
	private BufferedImage[] shieldSpritesC;
	private BufferedImage[] portalTopSpritesC;
	private BufferedImage[] portalBottomSpritesC;
	private BufferedImage[] portalLeftSpritesC;
	private BufferedImage[] portalRightSpritesC;
	private BufferedImage[] invisibleSpritesC;
	
	private BufferedImage[] frameSpritesM;
	private BufferedImage[] gemGreenSpritesM;
	private BufferedImage[] gemYellowSpritesM;
	private BufferedImage[] gemRedSpritesM;
	private BufferedImage[] gemPaleSpritesM;
	private BufferedImage[] gemGreenMiddleSpritesM;
	private BufferedImage[] gemYellowMiddleSpritesM;
	private BufferedImage[] gemRedMiddleSpritesM;
	private BufferedImage[] gemPaleMiddleSpritesM;
	private BufferedImage[] shieldSpritesM;
	private BufferedImage[] portalTopSpritesM;
	private BufferedImage[] portalBottomSpritesM;
	private BufferedImage[] portalLeftSpritesM;
	private BufferedImage[] portalRightSpritesM;
	private BufferedImage[] invisibleSpritesM;
	
	private Animation frameAnimation;
	private Animation gemAnimation;
	private Animation gemMiddleAnimation;
	private Animation shieldAnimation;
	private Animation portalTopAnimation;
	private Animation portalBottomAnimation;
	private Animation portalLeftAnimation;
	private Animation portalRightAnimation;
	
	private int preFightDialogTriggerWidth;
	private int preFightDialogTriggerHeight;
	
	// fight states
	//-------------------------------------------------------------------------------------------------------------------------------------
	private int currentAnimation;
	
	// prefight
	private final int PREFIGHT_DIALOGIDLE = -3;
	private final int PREFIGHT_APPEAR = -2;
	@SuppressWarnings("unused")
	private final int PREFIGHT_WAITFORSTART = -1;
	
	// normal fight
	private final int FIGHT_IDLE = 0;
	private final int FIGHT_TURRET = 1;
	private final int FIGHT_TOPPORTAL = 2;
	private final int FIGHT_BOTTOMPORTAL = 3;
	private final int FIGHT_LEFTRIGHTPORTAL = 4;
	
	// final attack
	private final int FINALATTACK_IDLE = 50;
	private final int FINALATTACK_SHIELD = 51;
	private final int FINALATTACK_WAVE_1 = 52;
	private final int FINALATTACK_WAVE_2 = 53;
	private final int FINALATTACK_WAVE_3 = 54;
	private final int FINALATTACK_WAVE_4 = 55;
	private final int FINALATTACK_WAVE_5 = 56;
	private final int FINALATTACK_WAVE_6 = 57;
	private final int FINALATTACK_WAVE_7 = 58;
	private final int FINALATTACK_WAVE_8 = 59;
	private final int FINALATTACK_WAVE_9 = 60;
	private final int FINALATTACK_WAVE_10 = 61;
	private final int FINALATTACK_WAVE_11 = 62;
	private final int FINALATTACK_WAVE_12 = 63;
	private final int FINALATTACK_WAVE_13 = 64;
	private final int FINALATTACK_WAVE_14 = 65;
	private final int FINALATTACK_WAVE_15 = 66;
	private final int FINALATTACK_WAVE_16 = 67;
	private final int FINALATTACK_DESTRUCTION_1 = 68;
	private final int FINALATTACK_DESTRUCTION_2 = 69;
	
	// deathbed
	private final int DEATHBED_IDLE = 70;
	private final int DEATHBED_WAITTRIGGER_1 = 71;
	private final int DEATHBED_WAITTRIGGER_2 = 72;
	private final int DEATHBED_WAITTRIGGER_3 = 73;
	private final int DEATHBED_WAITTRIGGER_4 = 74;
	private final int DEATHBED_WAITTRIGGER_5 = 75;
	private final int DEATHBED_WAITTRIGGER_6 = 76;
	private final int DEATHBED_WAITTRIGGER_7 = 77;
	private final int DEATHBED_WAITTRIGGER_8 = 78;
	private final int DEATHBED_WAITTRIGGER_9 = 79;
	
	// killed
	private final int KILLED_WAITTRIGGER_1 = 80;
	private final int KILLED_WAITTRIGGER_2 = 81;
	private final int KILLED_WAITTRIGGER_3 = 82;
	private final int KILLED_WAITTRIGGER_4 = 83;
	
	
	// old, only used for testing
	private final int BALL1 = 101;
	private final int BALL2 = 102;
	private final int BALL3 = 103;
	private final int BALL4CHASE = 104;
	private final int BALL4SPLIT = 105;
	private final int BALL6 = 106;
	private final int BALL7 = 107;
	private final int WALL = 108;
	private final int FINALATTACK1 = 109;
	private final int FINALATTACK2 = 110;
	private final int FINALATTACK3 = 111;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// data related to boss phases
	//-------------------------------------------------------------------------------------------------------------------------------------
	private int currentPhase;
	
	@SuppressWarnings("unused")
	private final int PHASE_PREFIGHT_DIALOG = 0;
	@SuppressWarnings("unused")
	private final int PHASE_PREFIGHT_PHASEIN = 1;
	private final int PHASE_FIGHT_GREENGEM = 2;
	private final int PHASE_FIGHT_YELLOWGEM = 3;
	private final int PHASE_FIGHT_REDGEM = 4;
	private final int PHASE_FIGHT_DESPERATION = 5;
	private final int PHASE_POSTFIGHT_DEATHBED = 6;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	
	private int currentGemMode;
	
	private final int GEMMODE_GREEN = 0;
	private final int GEMMODE_YELLOW = 1;
	private final int GEMMODE_RED = 2;
	private final int GEMMODE_PALE = 3;
	
	// personal shield values
	//-------------------------------------------------------------------------------------------------------------------------------------
	// is defensive shield around the gem up or not
	private boolean shielded;
	
	// shield health, breaks when dropped to 0, resets to max value after cooldown
	private int shieldHealth;
	private int shieldMaxHealth;
	
	// how long it takes for a new shield to be created once it's destroyed, -1 never restores it
	private long shieldRestoreTime;
	// moment when shield broke
	private long shieldBreakTime;

	private final long SHIELD_RESTORETIME_NOFIGHT = -1;
	private final long SHIELD_RESTORETIME_GREENGEM = 7600;
	private final long SHIELD_RESTORETIME_YELLOWGEM = 6100;
	private final long SHIELD_RESTORETIME_REDGEM = 4600;
	
	// shield opacity
	private float currentShieldOpacity;
	
	// shield size and hitbox
	private int shieldWidth;
	private int shieldHeight;
	private int shieldCWidth;
	private int shieldCHeight;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// master draw boolean, draw all or nothing
	private boolean drawBoss;
	
	// frame draw variables
	private float currentFrameOpacity;
	private float fadeInFrameStartOpacity;
	private float fadeInFrameEndOpacity;
	private float fadeInTotalOpacityChange;
	private boolean drawFrame;
	private boolean frameIsDoneFadingIn;
	private long fadeInFrameTime;
	private long fadeInFrameStart;
	
	// frame size and hitbox
	private int frameWidth;
	private int frameHeight;
	private int frameCWidth;
	private int frameCHeight;
	
	// gem size and hitbox
	private int gemWidth;
	private int gemHeight;
	private int gemCWidth;
	private int gemCHeight;
	
	// portal size and hitbox
	private int portalWidth;
	private int portalHeight;
	@SuppressWarnings("unused")
	private int portalCWidth;
	@SuppressWarnings("unused")
	private int portalCHeight;
	
	// values currently used as hitbox for attacks
	private int attackCWidth;
	private int attackCHeight;
	
	// middle small part of gem that blinks
	private int gemMiddleWidth;
	private int gemMiddleHeight;
	
	// animation speed for middle gem dot
	private int gemMiddleAnimationTimeGreen;
	private int gemMiddleAnimationTimeYellow;
	private int gemMiddleAnimationTimeRed;
	private int gemMiddleAnimationTimePale;
	
	// determines if a portal should be drawn/used to spawn enemies
	private boolean portalTopActive;
	private boolean portalBottomActive;
	private boolean portalLeftActive;
	private boolean portalRightActive;
	
	// current position of player
	private double playerX;
	private double playerY;
	
	// various spawn positions relative to middle gem
	//-------------------------------------------------------------------------------------------------------------------------------------
	// portal draw positions relative to middle of boss frame
	private int portalTopPosX = 0;
	private int portalTopPosY = -62;
	private int portalBottomPosX = 0;
	private int portalBottomPosY = 62;
	private int portalLeftPosX = -62;
	private int portalLeftPosY = 0;
	private int portalRightPosX = 62;
	private int portalRightPosY = 0;
	
	// turret object spawn positions relative to middle of boss frame
	private int turretNWSpawnPosX = -27;
	private int turretNWSpawnPosY = -27;
	private int turretNESpawnPosX = 27;
	private int turretNESpawnPosY = -27;
	private int turretSWSpawnPosX = -27;
	private int turretSWSpawnPosY = 27;
	private int turretSESpawnPosX = 27;
	private int turretSESpawnPosY = 27;
	
	// wall attacks can be spawned either high or low and require different dodging depending on which one is chosen
	// these are related to the left and right portal positions from the variables above
	private int wallTopSpawnPosY = -45;
	private int wallBottomSpawnPosY = 50;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	private int currentWallSpawnPosY;
	
	private final int SPAWNMODE_WALL_TOP = 0;
	private final int SPAWNMODE_WALL_BOTTOM = 1;
	private final int SPAWNMODE_WALL_RANDOM = 1;
	
	// weapon identifiers used for example while getting next idle cycle time
	private final int WEAPON_ALL = 0;
	private final int WEAPON_TURRET = 1;
	private final int WEAPON_TOPPORTAL = 2;
	private final int WEAPON_BOTTOMPORTAL = 3;
	private final int WEAPON_LEFTRIGHTPORTAL = 4;
	
	// old all around idle time, not used anymore
	private long idleTime;
	private long idleStart;
	
	// values determining how frequently different attacks are spawned and when the countdown for it started
	private long turretIdleTime;
	private long turretIdleStart;
	private long topPortalIdleTime;
	private long topPortalIdleStart;
	private long bottomPortalIdleTime;
	private long bottomPortalIdleStart;
	private long leftRightPortalIdleTime;
	private long leftRightPortalIdleStart;
	
	// some values for various attacks
	private double dxBall1;
	private double dyBall1;
	private double dxBall4Chase;
	private double dyBall4Chase;
	private double dxBall4Split;
	private double dyBall4Split;
	
	// warning times are used to determine how long it takes from the moment a weapon starts a warning animation to when the attack spawns
	private long warningTime;
	private long turretWarningStart;
	private long topPortalWarningStart;
	private long leftRightPortalWarningStart;
	private long bottomPortalWarningStart;
	
	// true to run code checking if a warning time is up, spawning the related attack when that happens
	// kept as false whenever there's no ongoing warning for the attack in question
	private boolean turretAttackPending;
	private boolean topPortalAttackPending;
	private boolean leftRightPortalAttackPending;
	private boolean bottomPortalAttackPending;
	
	// booleans used to spawn things
	//-------------------------------------------------------------------------------------------------------------------------------------
	// booleans triggering warning effects for the weapons that can spawn attacks
	private boolean createTurretWarning;
	private boolean createTopPortalWarning;
	private boolean createBottomPortalWarning;
	private boolean createLeftRightPortalWarning;
	
	// booleans triggering various turret attacks
	private boolean createTurretFire1;
	private boolean createTurretFire2;
	private boolean createTurretFire3;
	
	// booleans triggering attacks the top portal uses
	private boolean createBall1;
	private boolean createBall2;
	private boolean createBall3;
	private boolean createBall4Chase;
	private boolean createBall4Split;
	private boolean createBall6;
	private boolean createBall7;
	
	// booleans triggering attacks the side portals use
	private boolean createWall;
	
	// booleans triggering attacks the bottom portal
	private boolean createEnemyDarkmist;
	private boolean createEnemyHalfmoon;
	private boolean createEnemyMine;
	private boolean createEnemySentry;
	private boolean createEnemyShield;
	private boolean createEnemySpikes;
	private boolean createEnemySquare;
	private boolean createEnemyStar;
	private boolean createEnemyTriangle;
	
	// booleans triggering attacks that are part of the final attack
	private boolean createShield;
	private boolean createFinalAttack1;
	private boolean createFinalAttack2;
	private boolean createFinalAttack3;
	private boolean createFinalAttack4;
	
	// created after the fight is over if boss is shown mercy
	private boolean createEscapePortal;
	//-------------------------------------------------------------------------------------------------------------------------------------

	// final attack related values
	//-------------------------------------------------------------------------------------------------------------------------------------
	private final int FINALATTACK_GEMCOLOR_RED = 0;
	private final int FINALATTACK_GEMCOLOR_GREEN = 1;
	private final int FINALATTACK_GEMCOLOR_RANDOM = 2;
	private final long FINALATTACK_IDLETIME_DEFAULT = 2000;
	private final double FINALATTACK_ATTACKSPEED_DEFAULT = 2.0;
	private final int FINALATTACK_GEMAMOUNT_DEFAULT = 24;
	private final double FINALATTACK_RADIUS_DEFAULT = 75.0;
	private final int FINALATTACK_STARTDEGREEMODE_DEFAULT = 0;
	private final int FINALATTACK_SPAWNINTERVAL_DEFAULT = 70;
	
	// final attack wave values
	// change these before spawning a wave to control various things about it
	private int finalAttackGemColor;
	private long finalAttackIdleTime;
	private double finalAttackSpeed;
	private int finalAttackGemAmount;
	private double finalAttackRadius;
	private int finalAttackStartDegreeMode;
	private long finalAttackSpawnInterval;
	
	// varying radius attack values
	private double finalAttackRadiusDifferenceInitial;
	private double finalAttackRadiusDifferenceIncrease;
	private int finalAttackRadiusAmountOfChanges;
	private boolean outwardStartDirection;
	
	// spawn position for everything related to boss position
	// shield spawns at this point and waves are centered around it
	private int finalAttackPosX;
	private int finalAttackPosY;
	
	// next wave in turn
	private int nextWave;
	
	// wait times after each wave
	private long finalAttackCurrentWaveWaitTime;
	private long finalAttackLastWaveStart;
	
	// from being grabbed by the forcefield to the first wave spawning
	private final long FINALATTACK_WAVEPAUSE_INITIALGRAB = 9000;
	
	// 3 circles going outward
	//----------------------------------------------------------
	private final long FINALATTACK_WAVEPAUSE_1 = 900;
	private final long FINALATTACK_WAVEPAUSE_2 = 900;
	private final long FINALATTACK_WAVEPAUSE_3 = 6000;
	//----------------------------------------------------------
	
	// 1 whole outer spiral & 2 inner half spirals
	//----------------------------------------------------------
	private final long FINALATTACK_WAVEPAUSE_4 = 0;
	private final long FINALATTACK_WAVEPAUSE_5 = FINALATTACK_SPAWNINTERVAL_DEFAULT * 12 + 700;
	private final long FINALATTACK_WAVEPAUSE_6 = 7500;
	//----------------------------------------------------------
	
	// slow inner ring & fast outer ring
	//----------------------------------------------------------
	private final long FINALATTACK_WAVEPAUSE_7 = 500;
	private final long FINALATTACK_WAVEPAUSE_8 = 5500;
	//----------------------------------------------------------
	
	// fast activating double spirals (red & green)
	//----------------------------------------------------------
	private final long FINALATTACK_WAVEPAUSE_9 = 0;
	private final long FINALATTACK_WAVEPAUSE_10 = 10500;
	//----------------------------------------------------------
	
	// varying radius spiral followed by 4 explosions during second lap
	//----------------------------------------------------------
	private final long FINALATTACK_WAVEPAUSE_11 = 9500;
	private final long FINALATTACK_WAVEPAUSE_12 = 1100;
	private final long FINALATTACK_WAVEPAUSE_13 = 1100;
	private final long FINALATTACK_WAVEPAUSE_14 = 1100;
	private final long FINALATTACK_WAVEPAUSE_15 = 11200;
	//----------------------------------------------------------
	
	// final spam
	//----------------------------------------------------------
	private final long FINALATTACK_WAVEPAUSE_16 = 11500;
	//----------------------------------------------------------
	
	// self destruction
	//----------------------------------------------------------
	private final long FINALATTACK_DESTRUCTIONPAUSE_1 = 2000;
	private final long FINALATTACK_DESTRUCTIONPAUSE_2 = 6000;
	//----------------------------------------------------------
	
	// deathbed
	//----------------------------------------------------------
	private final long DEATHBED_WAITTRIGGERPAUSE_1 = 20000;
	private final long DEATHBED_WAITTRIGGERPAUSE_2 = 20000;
	private final long DEATHBED_WAITTRIGGERPAUSE_3 = 13000;
	private final long DEATHBED_WAITTRIGGERPAUSE_4 = 700;
	private final long DEATHBED_WAITTRIGGERPAUSE_5 = 1050;
	private final long DEATHBED_WAITTRIGGERPAUSE_6 = 4500;
	private final long DEATHBED_WAITTRIGGERPAUSE_7 = 13000;
	@SuppressWarnings("unused")
	private final long DEATHBED_WAITTRIGGERPAUSE_8 = 5000 * 8;
	private final long DEATHBED_WAITTRIGGERPAUSE_9 = 3000;
	//----------------------------------------------------------
	// killed
	//----------------------------------------------------------
	private final long KILLED_WAITTRIGGERPAUSE_1 = 1000;
	private final long KILLED_WAITTRIGGERPAUSE_2 = 7000;
	private final long KILLED_WAITTRIGGERPAUSE_3 = 5000 * 5;
	//----------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// weapon idle time related values
	//-------------------------------------------------------------------------------------------------------------------------------------
	private long turretIdleMin;
	private long turretIdleMax;
	
	private long topPortalIdleMin;
	private long topPortalIdleMax;
	
	private long bottomPortalIdleMin;
	private long bottomPortalIdleMax;
	
	private long leftRightPortalIdleMin;
	private long leftRightPortalIdleMax;
	
	private final long IDLE_TURRET_MIN_GREENGEM = 25000;
	private final long IDLE_TURRET_MAX_GREENGEM = 35000;
	private final long IDLE_TURRET_MIN_YELLOWGEM = 17000;
	private final long IDLE_TURRET_MAX_YELLOWGEM = 19000;
	private final long IDLE_TURRET_MIN_REDGEM = 17000;
	private final long IDLE_TURRET_MAX_REDGEM = 19000;
	private final long IDLE_TURRET_MIN_DESPERATION = 999999;
	private final long IDLE_TURRET_MAX_DESPERATION = 999999;
	
	private final long IDLE_TOPPORTAL_MIN_GREENGEM = 5000;
	private final long IDLE_TOPPORTAL_MAX_GREENGEM = 7000;
	private final long IDLE_TOPPORTAL_MIN_YELLOWGEM = 18000;
	private final long IDLE_TOPPORTAL_MAX_YELLOWGEM = 23000;
	private final long IDLE_TOPPORTAL_MIN_REDGEM = 8000;
	private final long IDLE_TOPPORTAL_MAX_REDGEM = 12000;
	private final long IDLE_TOPPORTAL_MIN_DESPERATION = 999999;
	private final long IDLE_TOPPORTAL_MAX_DESPERATION = 999999;
	
<<<<<<< HEAD
	private final long IDLE_LEFTRIGHTPORTAL_MIN_GREENGEM = 50000;
	private final long IDLE_LEFTRIGHTPORTAL_MAX_GREENGEM = 60000;
=======
	private final long IDLE_LEFTRIGHTPORTAL_MIN_GREENGEM = 45000;
	private final long IDLE_LEFTRIGHTPORTAL_MAX_GREENGEM = 55000;
>>>>>>> 2.03
	private final long IDLE_LEFTRIGHTPORTAL_MIN_YELLOWGEM = 9000;
	private final long IDLE_LEFTRIGHTPORTAL_MAX_YELLOWGEM = 14000;
	private final long IDLE_LEFTRIGHTPORTAL_MIN_REDGEM = 13000;
	private final long IDLE_LEFTRIGHTPORTAL_MAX_REDGEM = 18000;
	private final long IDLE_LEFTRIGHTPORTAL_MIN_DESPERATION = 999999;
	private final long IDLE_LEFTRIGHTPORTAL_MAX_DESPERATION = 999999;
	
<<<<<<< HEAD
	private final long IDLE_BOTTOMPORTAL_MIN_GREENGEM = 17000;
	private final long IDLE_BOTTOMPORTAL_MAX_GREENGEM = 20000;
=======
	private final long IDLE_BOTTOMPORTAL_MIN_GREENGEM = 15000;
	private final long IDLE_BOTTOMPORTAL_MAX_GREENGEM = 18000;
>>>>>>> 2.03
	private final long IDLE_BOTTOMPORTAL_MIN_YELLOWGEM = 8000;
	private final long IDLE_BOTTOMPORTAL_MAX_YELLOWGEM = 12000;
	private final long IDLE_BOTTOMPORTAL_MIN_REDGEM = 15000;
	private final long IDLE_BOTTOMPORTAL_MAX_REDGEM = 18000;
	private final long IDLE_BOTTOMPORTAL_MIN_DESPERATION = 999999;
	private final long IDLE_BOTTOMPORTAL_MAX_DESPERATION = 999999;
	
	// bottom portal enemy extra spawntime values (affected by a difficulty multiplier in the final value)
	// every time an enemy is spawned by the bottom portal extra cooldown time is added to compensate for tougher spawns
	private static final long IDLE_BOTTOMPORTAL_EXTRA_DARKMIST = 3500;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_HALFMOON = 2000;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_MINE = 0;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_SENTRY = 6000;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_SHIELD = 8000;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_SPIKES = 1500;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_SQUARE = 7500;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_STAR = 9000;
	private static final long IDLE_BOTTOMPORTAL_EXTRA_TRIANGLE = 4000;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// turret spawner values, turret 1 is northeast and from there it rotates clockwise, turret without number stands for all 4
	//-------------------------------------------------------------------------------------------------------------------------------------
	// base values to select from
	private final int TURRET_ATTACK1 = 1;
	private final int TURRET_ATTACK2 = 2;
	private final int TURRET_ATTACK3 = 3;
	
	@SuppressWarnings("unused")
	private final int TURRET_COLOR_RED = 0;
	@SuppressWarnings("unused")
	private final int TURRET_COLOR_GREEN = 1;
	private final int TURRET_COLOR_RANDOM = 2;
	
	private final double TURRET1_DEGREES_BASE = 90.0 * 3 + 45.0;
	private final double TURRET2_DEGREES_BASE = 90.0 * 0 + 45.0;
	private final double TURRET3_DEGREES_BASE = 90.0 * 1 + 45.0;
	private final double TURRET4_DEGREES_BASE = 90.0 * 2 + 45.0;
	private final double TURRET_ATTACK1_DEGREES_VARIATION = 10.0;
	private final double TURRET_ATTACK2_DEGREES_VARIATION = 25.0;
	private final double TURRET_ATTACK2_DEGREES_VARIATION_EXTRA_MAX = 10.0;
	private final double TURRET_ATTACK3_DEGREES_VARIATION = 30.0;
	
	private final int TURRET_ATTACK1_TOTALSPAWNAMOUNT = 10;
	private final double TURRET_ATTACK1_SPEED_MIN = 1.7;
	private final double TURRET_ATTACK1_SPEED_MAX = 2.3;
	private final double TURRET1_ATTACK1_DEGREES_MIN = TURRET1_DEGREES_BASE - TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET1_ATTACK1_DEGREES_MAX = TURRET1_DEGREES_BASE + TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET2_ATTACK1_DEGREES_MIN = TURRET2_DEGREES_BASE - TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET2_ATTACK1_DEGREES_MAX = TURRET2_DEGREES_BASE + TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET3_ATTACK1_DEGREES_MIN = TURRET3_DEGREES_BASE - TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET3_ATTACK1_DEGREES_MAX = TURRET3_DEGREES_BASE + TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET4_ATTACK1_DEGREES_MIN = TURRET4_DEGREES_BASE - TURRET_ATTACK1_DEGREES_VARIATION;
	private final double TURRET4_ATTACK1_DEGREES_MAX = TURRET4_DEGREES_BASE + TURRET_ATTACK1_DEGREES_VARIATION;
	private final int TURRET_ATTACK1_SPAWNINTERVAL = 160;
	private final int TURRET_ATTACK1_SPAWNBURSTAMOUNT = 1;
	private final int TURRET_ATTACK1_INITIALIDLE = 0;
	
	// bonuses are all added to the base value when applied
	private final int TURRET_ATTACK1_TOTALSPAWNAMOUNT_BONUS_REDGEM = (int) ((double) TURRET_ATTACK1_TOTALSPAWNAMOUNT * 0.5);
	private final double TURRET_ATTACK1_SPEED_MIN_BONUS_REDGEM = 0.2;
	private final double TURRET_ATTACK1_SPEED_MAX_BONUS_REDGEM = 0.2;
	
	private final int TURRET_ATTACK2_TOTALSPAWNAMOUNT = 5;
	private final double TURRET_ATTACK2_SPEED_MIN = 1.7;
	private final double TURRET_ATTACK2_SPEED_MAX = 2.3;
	private final double TURRET1_ATTACK2_DEGREES_MIN = TURRET1_DEGREES_BASE - TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET1_ATTACK2_DEGREES_MAX = TURRET1_DEGREES_BASE + TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET2_ATTACK2_DEGREES_MIN = TURRET2_DEGREES_BASE - TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET2_ATTACK2_DEGREES_MAX = TURRET2_DEGREES_BASE + TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET3_ATTACK2_DEGREES_MIN = TURRET3_DEGREES_BASE - TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET3_ATTACK2_DEGREES_MAX = TURRET3_DEGREES_BASE + TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET4_ATTACK2_DEGREES_MIN = TURRET4_DEGREES_BASE - TURRET_ATTACK2_DEGREES_VARIATION;
	private final double TURRET4_ATTACK2_DEGREES_MAX = TURRET4_DEGREES_BASE + TURRET_ATTACK2_DEGREES_VARIATION;
	private final int TURRET_ATTACK2_SPAWNINTERVAL = 160;
	private final int TURRET_ATTACK2_SPAWNBURSTAMOUNT = 1;
	private final int TURRET1_ATTACK2_INITIALIDLE = TURRET_ATTACK2_SPAWNINTERVAL * TURRET_ATTACK2_TOTALSPAWNAMOUNT * 0;
	private final int TURRET2_ATTACK2_INITIALIDLE = TURRET_ATTACK2_SPAWNINTERVAL * TURRET_ATTACK2_TOTALSPAWNAMOUNT * 1 + TURRET_ATTACK2_SPAWNINTERVAL;
	private final int TURRET3_ATTACK2_INITIALIDLE = TURRET_ATTACK2_SPAWNINTERVAL * TURRET_ATTACK2_TOTALSPAWNAMOUNT * 2 + TURRET_ATTACK2_SPAWNINTERVAL;
	private final int TURRET4_ATTACK2_INITIALIDLE = TURRET_ATTACK2_SPAWNINTERVAL * TURRET_ATTACK2_TOTALSPAWNAMOUNT * 3 + TURRET_ATTACK2_SPAWNINTERVAL;
	
	private final int TURRET_ATTACK2_TOTALSPAWNAMOUNT_BONUS_REDGEM = (int) ((double) TURRET_ATTACK2_TOTALSPAWNAMOUNT * 0.5);
	private final double TURRET_ATTACK2_SPEED_MIN_BONUS_REDGEM = 0.2;
	private final double TURRET_ATTACK2_SPEED_MAX_BONUS_REDGEM = 0.2;
	
	private final int TURRET_ATTACK3_TOTALSPAWNAMOUNT = 4;
	private final double TURRET_ATTACK3_SPEED_MIN = 1.6;
	private final double TURRET_ATTACK3_SPEED_MAX = 2.4;
	private final double TURRET1_ATTACK3_DEGREES_MIN = TURRET1_DEGREES_BASE - TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET1_ATTACK3_DEGREES_MAX = TURRET1_DEGREES_BASE + TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET2_ATTACK3_DEGREES_MIN = TURRET2_DEGREES_BASE - TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET2_ATTACK3_DEGREES_MAX = TURRET2_DEGREES_BASE + TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET3_ATTACK3_DEGREES_MIN = TURRET3_DEGREES_BASE - TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET3_ATTACK3_DEGREES_MAX = TURRET3_DEGREES_BASE + TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET4_ATTACK3_DEGREES_MIN = TURRET4_DEGREES_BASE - TURRET_ATTACK3_DEGREES_VARIATION;
	private final double TURRET4_ATTACK3_DEGREES_MAX = TURRET4_DEGREES_BASE + TURRET_ATTACK3_DEGREES_VARIATION;
	private final int TURRET_ATTACK3_SPAWNINTERVAL = 1600;
	private final int TURRET_ATTACK3_SPAWNBURSTAMOUNT = 4;
	private final int TURRET_ATTACK3_INITIALIDLE = 0;
	
	private final int TURRET_ATTACK3_TOTALSPAWNAMOUNT_BONUS_REDGEM = (int) ((double) TURRET_ATTACK3_TOTALSPAWNAMOUNT * 1.0);
	private final double TURRET_ATTACK3_SPEED_MIN_BONUS_REDGEM = 0.1;
	private final double TURRET_ATTACK3_SPEED_MAX_BONUS_REDGEM = 0.3;

	private int turretCurrentTotalSpawnAmount;
	private int turretCurrentColor;
	private double turretCurrentTotalSpeedMin;
	private double turretCurrentTotalSpeedMax;
	private double turret1CurrentDegreesMin;
	private double turret1CurrentDegreesMax;
	private double turret2CurrentDegreesMin;
	private double turret2CurrentDegreesMax;
	private double turret3CurrentDegreesMin;
	private double turret3CurrentDegreesMax;
	private double turret4CurrentDegreesMin;
	private double turret4CurrentDegreesMax;
	private long turretCurrentSpawnInterval;
	private int turretCurrentSpawnBurstAmount;
	private int turretCurrentInitialIdleTime;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// ball related spawn values
	//-------------------------------------------------------------------------------------------------------------------------------------
	// ball 7
	private final int BALL7_TOP_X = 0;
	private final int BALL7_TOP_Y = -80;
	private final int BALL7_BOTTOM_X = 0;
	private final int BALL7_BOTTOM_Y = 80;
	private final int BALL7_LEFT_X = -80;
	private final int BALL7_LEFT_Y = 0;
	private final int BALL7_RIGHT_X = 80;
	private final int BALL7_RIGHT_Y = 0;
	
	private int ball7TopPosX;
	private int ball7TopPosY;
	private int ball7BottomPosX;
	private int ball7BottomPosY;
	private int ball7LeftPosX;
	private int ball7LeftPosY;
	private int ball7RightPosX;
	private int ball7RightPosY;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// top portal spawn odds during different phases
	// ball 5 is second mode of ball 4
	// roll starts from 1 and ends in GREENGEM_BALL7_MAX
	// GREENGEM_BALL1_MAX needs to be smallest value going up all the way to GREENGEM_BALL7_MAX as highest value
	//-------------------------------------------------------------------------------------------------------------------------------------
	private final int SPAWNODDS_BALL1_MAX_GREENGEM = 50;
	private final int SPAWNODDS_BALL2_MAX_GREENGEM = SPAWNODDS_BALL1_MAX_GREENGEM + 1;
	private final int SPAWNODDS_BALL3_MAX_GREENGEM = SPAWNODDS_BALL2_MAX_GREENGEM +  1;
	private final int SPAWNODDS_BALL4_MAX_GREENGEM = SPAWNODDS_BALL3_MAX_GREENGEM +  1;
	private final int SPAWNODDS_BALL5_MAX_GREENGEM = SPAWNODDS_BALL4_MAX_GREENGEM +  1;
	private final int SPAWNODDS_BALL6_MAX_GREENGEM = SPAWNODDS_BALL5_MAX_GREENGEM +  1;
	private final int SPAWNODDS_BALL7_MAX_GREENGEM = SPAWNODDS_BALL6_MAX_GREENGEM +  1;

	private final int SPAWNODDS_BALL1_MAX_YELLOWGEM = 1;
	private final int SPAWNODDS_BALL2_MAX_YELLOWGEM = SPAWNODDS_BALL1_MAX_YELLOWGEM + 2;
	private final int SPAWNODDS_BALL3_MAX_YELLOWGEM = SPAWNODDS_BALL2_MAX_YELLOWGEM + 2;
	private final int SPAWNODDS_BALL4_MAX_YELLOWGEM = SPAWNODDS_BALL3_MAX_YELLOWGEM + 2;
	private final int SPAWNODDS_BALL5_MAX_YELLOWGEM = SPAWNODDS_BALL4_MAX_YELLOWGEM + 2;
	private final int SPAWNODDS_BALL6_MAX_YELLOWGEM = SPAWNODDS_BALL5_MAX_YELLOWGEM + 2;
	private final int SPAWNODDS_BALL7_MAX_YELLOWGEM = SPAWNODDS_BALL6_MAX_YELLOWGEM + 2;
	
	private final int SPAWNODDS_BALL1_MAX_REDGEM = 1;
	private final int SPAWNODDS_BALL2_MAX_REDGEM = SPAWNODDS_BALL1_MAX_REDGEM + 1;
	private final int SPAWNODDS_BALL3_MAX_REDGEM = SPAWNODDS_BALL2_MAX_REDGEM + 1;
	private final int SPAWNODDS_BALL4_MAX_REDGEM = SPAWNODDS_BALL3_MAX_REDGEM + 1;
	private final int SPAWNODDS_BALL5_MAX_REDGEM = SPAWNODDS_BALL4_MAX_REDGEM + 1;
	private final int SPAWNODDS_BALL6_MAX_REDGEM = SPAWNODDS_BALL5_MAX_REDGEM + 1;
	private final int SPAWNODDS_BALL7_MAX_REDGEM = SPAWNODDS_BALL6_MAX_REDGEM + 1;
		
	private int spawnoddsBall1Max;
	private int spawnoddsBall2Max;
	private int spawnoddsBall3Max;
	private int spawnoddsBall4Max;
	private int spawnoddsBall5Max;
	private int spawnoddsBall6Max;
	private int spawnoddsBall7Max;
	//-------------------------------------------------------------------------------------------------------------------------------------
	
	// prefight dialog variables
	//-------------------------------------------------------------------------------------------------------------------------------------
	private int currentPrefightDialogProgress;

	@SuppressWarnings("unused")
	private final int DIALOGPROGRESS_PREFIGHT_NOTSTARTED = 0;
	@SuppressWarnings("unused")
	private final int DIALOGPROGRESS_PREFIGHT_INITIAL = 1;
	private final int DIALOGPROGRESS_PREFIGHT_BOSSAPPEARANCE = 2;
	private final int DIALOGPROGRESS_PREFIGHT_FINISHED = 3;
	//-------------------------------------------------------------------------------------------------------------------------------------

	private boolean finalAttackIsFinished;

	private boolean playerIsCaughtInFinalAttackGrab;

	private boolean fightStarted;

	private boolean prepareForFinalAttack;
	
	private int finalAttackShieldId;
	
	private boolean destructFinalAttack;
	
	private boolean fadeInEndScreen;

	private int bossEscapePortalX;
	private int bossEscapePortalY;
	
	private boolean permanentFall;
	
	private boolean killable;
	
	private boolean createBossExplosion;
	private boolean createBossGemPieces;

	private boolean fadeInAllowed;
	
	// extra idle time so rolling high cost enemies isn't that unfair
	private long bottomPortalLastEnemySpawnedExtra;
	
	
	
	public Boss(TileMap tm, int mode, Object extraData, Dialog dialog) {
		
		super(tm, mode, extraData);
		
		this.dialog = dialog;
		
		drawBoss = true;
		
		moveSpeed = 1.0;
		maxSpeed = 1.0;
		fallSpeed = 0.2;
		maxFallSpeed = 10.0;
		
		// frame values
		frameWidth = 217;
		frameHeight = 217;
		frameCWidth = 180;
		frameCHeight = 180;
		
		// personal shield values
		shieldWidth = 71;
		shieldHeight = 71;
		shieldCWidth = 64;
		shieldCHeight = 64;
		
		// middle gem values
		gemWidth = 31;
		gemHeight = 31;
		gemCWidth = 14;
		gemCHeight = 14;
		
		// surrounding four portal values
		portalWidth = 31;
		portalHeight = 31;
		portalCWidth = 14;
		portalCHeight = 14;
		
		// normal width etc. are used for all normal calculations except for getting intersection with other map objects
		width = frameWidth;
		height = frameHeight;
		cwidth = frameCWidth;
		cheight = frameCHeight;
		
		// how close player has to come for pre fight dialog to trigger
		preFightDialogTriggerWidth = 110;
		preFightDialogTriggerHeight = 150;
		
		// middle gem middle shining part values
		gemMiddleWidth = 31;
		gemMiddleHeight = 31;
		
		gemMiddleAnimationTimeGreen = 60;
		gemMiddleAnimationTimeYellow = 50;
		gemMiddleAnimationTimeRed = 40;
		gemMiddleAnimationTimePale = 220;
		
		fadeInFrameTime = 3000;
		
		currentGemMode = GEMMODE_GREEN;
		
		flinchTime = 1500;
		
		health = maxHealth = 15;
		shieldHealth = shieldMaxHealth = 3;
		damage = 9;

		aggressive = true;
		shielded = false;
		
		shieldRestoreTime = SHIELD_RESTORETIME_NOFIGHT;
		
		idleTime = 8000;
		idleStart = System.nanoTime();
		turretIdleStart = System.nanoTime();
		topPortalIdleStart = System.nanoTime();
		bottomPortalIdleStart = System.nanoTime();
		leftRightPortalIdleStart = System.nanoTime();
		setWeaponIdleTime(WEAPON_ALL);
		
		warningTime = 2000;
		
		finalAttackGemColor = FINALATTACK_GEMCOLOR_RANDOM;
		finalAttackIdleTime = FINALATTACK_IDLETIME_DEFAULT;
		finalAttackSpeed = FINALATTACK_ATTACKSPEED_DEFAULT;
		finalAttackGemAmount = FINALATTACK_GEMAMOUNT_DEFAULT;
		finalAttackRadius = FINALATTACK_RADIUS_DEFAULT;
		finalAttackStartDegreeMode = FINALATTACK_STARTDEGREEMODE_DEFAULT;
		finalAttackSpawnInterval = FINALATTACK_SPAWNINTERVAL_DEFAULT;
		
		finalAttackPosX = 0;
		finalAttackPosY = -150;
		
		ball7TopPosX = BALL7_TOP_X;
		ball7TopPosY = BALL7_TOP_Y;
		ball7BottomPosX = BALL7_BOTTOM_X;
		ball7BottomPosY = BALL7_BOTTOM_Y;
		ball7LeftPosX = BALL7_LEFT_X;
		ball7LeftPosY = BALL7_LEFT_Y;
		ball7RightPosX = BALL7_RIGHT_X;
		ball7RightPosY = BALL7_RIGHT_Y;
		
		bossEscapePortalX = 0;
		bossEscapePortalY = -70;
		
		prepareTurretAttackValues(TURRET_ATTACK1);
		
		pacified = true;
		invulnerable = true;
		
		currentAnimation = PREFIGHT_DIALOGIDLE;
		
		updatePhase();
		
		updateColorMode();
		
		frameSpritesC = Content.BossFrame[0];
		gemGreenSpritesC = Content.BossGem[0];
		gemYellowSpritesC = Content.BossGem[1];
		gemRedSpritesC = Content.BossGem[2];
		gemPaleSpritesC = Content.BossGem[3];
		gemGreenMiddleSpritesC = Content.BossGemMiddle[0];
		gemYellowMiddleSpritesC = Content.BossGemMiddle[1];
		gemRedMiddleSpritesC = Content.BossGemMiddle[2];
		gemPaleMiddleSpritesC = Content.BossGemMiddle[2];
		shieldSpritesC = Content.BossShield[0];
		portalTopSpritesC = Content.BossPortal1[0];
		portalLeftSpritesC = Content.BossPortal2[0];
		portalRightSpritesC = Content.BossPortal2[0];
		portalBottomSpritesC = Content.BossPortal3[0];
		invisibleSpritesC = Content.Invisible[0];
		
		frameSpritesM = Content.BossFrameM[0];
		gemGreenSpritesM = Content.BossGemM[0];
		gemYellowSpritesM = Content.BossGemM[1];
		gemRedSpritesM = Content.BossGemM[2];
		gemPaleSpritesM = Content.BossGemM[3];
		gemGreenMiddleSpritesM = Content.BossGemMiddleM[0];
		gemYellowMiddleSpritesM = Content.BossGemMiddleM[1];
		gemRedMiddleSpritesM = Content.BossGemMiddleM[2];
		gemPaleMiddleSpritesM = Content.BossGemMiddleM[2];
		shieldSpritesC = Content.BossShieldM[0];
		portalTopSpritesM = Content.BossPortal1M[0];
		portalLeftSpritesM = Content.BossPortal2M[0];
		portalRightSpritesM = Content.BossPortal2M[0];
		portalBottomSpritesM = Content.BossPortal3M[0];
		invisibleSpritesM = Content.Invisible[0];
		
		setBufferedImages();
		
		frameAnimation = new Animation();
		frameAnimation.setFrames(frameSprites);
		frameAnimation.setDelay(-1);
		
		gemAnimation = new Animation();
		gemAnimation.setFrames(gemGreenSprites);
		gemAnimation.setDelay(40);
		
		gemMiddleAnimation = new Animation();
		gemMiddleAnimation.setFrames(gemGreenMiddleSprites);
		gemMiddleAnimation.setDelay(gemMiddleAnimationTimeGreen);
		
		shieldAnimation = new Animation();
		shieldAnimation.setFrames(shieldSprites);
		shieldAnimation.setDelay(-1);
		
		portalTopAnimation = new Animation();
		portalTopAnimation.setFrames(portalTopSprites);
		portalTopAnimation.setDelay(40);
		
		portalBottomAnimation = new Animation();
		portalBottomAnimation.setFrames(portalBottomSprites);
		portalBottomAnimation.setDelay(40);
		
		portalLeftAnimation = new Animation();
		portalLeftAnimation.setFrames(portalLeftSprites);
		portalLeftAnimation.setDelay(40);
		
		portalRightAnimation = new Animation();
		portalRightAnimation.setFrames(portalRightSprites);
		portalRightAnimation.setDelay(40);
		
		facingRight = true;
		
		setShieldOpacity();
		
		updateAttackHitbox();
	}

	// updates attack hitbox depending on if shield is up or not
	private void updateAttackHitbox() {
		if(shielded) {
			attackCWidth = shieldCWidth;
			attackCHeight = shieldCHeight;
		}
		else {
			attackCWidth = gemCWidth;
			attackCHeight = gemCHeight;
		}
	}
	
	// called when phase of the fight changes
	private void updatePortalMinMaxIdleTimes() {
		if(currentPhase == PHASE_FIGHT_GREENGEM) {
			turretIdleMin = IDLE_TURRET_MIN_GREENGEM;
			turretIdleMax = IDLE_TURRET_MAX_GREENGEM;
			topPortalIdleMin = IDLE_TOPPORTAL_MIN_GREENGEM;
			topPortalIdleMax = IDLE_TOPPORTAL_MAX_GREENGEM;
			bottomPortalIdleMin = IDLE_BOTTOMPORTAL_MIN_GREENGEM;
			bottomPortalIdleMax = IDLE_BOTTOMPORTAL_MAX_GREENGEM;
			leftRightPortalIdleMin = IDLE_LEFTRIGHTPORTAL_MIN_GREENGEM;
			leftRightPortalIdleMax = IDLE_LEFTRIGHTPORTAL_MAX_GREENGEM;
		}
		else if(currentPhase == PHASE_FIGHT_YELLOWGEM) {
			turretIdleMin = IDLE_TURRET_MIN_YELLOWGEM;
			turretIdleMax = IDLE_TURRET_MAX_YELLOWGEM;
			topPortalIdleMin = IDLE_TOPPORTAL_MIN_YELLOWGEM;
			topPortalIdleMax = IDLE_TOPPORTAL_MAX_YELLOWGEM;
			bottomPortalIdleMin = IDLE_BOTTOMPORTAL_MIN_YELLOWGEM;
			bottomPortalIdleMax = IDLE_BOTTOMPORTAL_MAX_YELLOWGEM;
			leftRightPortalIdleMin = IDLE_LEFTRIGHTPORTAL_MIN_YELLOWGEM;
			leftRightPortalIdleMax = IDLE_LEFTRIGHTPORTAL_MAX_YELLOWGEM;
		}
		else if(currentPhase == PHASE_FIGHT_REDGEM) {
			turretIdleMin = IDLE_TURRET_MIN_REDGEM;
			turretIdleMax = IDLE_TURRET_MAX_REDGEM;
			topPortalIdleMin = IDLE_TOPPORTAL_MIN_REDGEM;
			topPortalIdleMax = IDLE_TOPPORTAL_MAX_REDGEM;
			bottomPortalIdleMin = IDLE_BOTTOMPORTAL_MIN_REDGEM;
			bottomPortalIdleMax = IDLE_BOTTOMPORTAL_MAX_REDGEM;
			leftRightPortalIdleMin = IDLE_LEFTRIGHTPORTAL_MIN_REDGEM;
			leftRightPortalIdleMax = IDLE_LEFTRIGHTPORTAL_MAX_REDGEM;
		}
		else if(currentPhase == PHASE_FIGHT_DESPERATION) {
			turretIdleMin = IDLE_TURRET_MIN_DESPERATION;
			turretIdleMax = IDLE_TURRET_MAX_DESPERATION;
			topPortalIdleMin = IDLE_TOPPORTAL_MIN_DESPERATION;
			topPortalIdleMax = IDLE_TOPPORTAL_MAX_DESPERATION;
			bottomPortalIdleMin = IDLE_BOTTOMPORTAL_MIN_DESPERATION;
			bottomPortalIdleMax = IDLE_BOTTOMPORTAL_MAX_DESPERATION;
			leftRightPortalIdleMin = IDLE_LEFTRIGHTPORTAL_MIN_DESPERATION;
			leftRightPortalIdleMax = IDLE_LEFTRIGHTPORTAL_MAX_DESPERATION;
		}
	}
	
	// called when phase of the fight changes
	protected void updateTopPortalSpawnOdds() {
		if(currentPhase == PHASE_FIGHT_GREENGEM) {
			spawnoddsBall1Max = SPAWNODDS_BALL1_MAX_GREENGEM;
			spawnoddsBall2Max = SPAWNODDS_BALL2_MAX_GREENGEM;
			spawnoddsBall3Max = SPAWNODDS_BALL3_MAX_GREENGEM;
			spawnoddsBall4Max = SPAWNODDS_BALL4_MAX_GREENGEM;
			spawnoddsBall5Max = SPAWNODDS_BALL5_MAX_GREENGEM;
			spawnoddsBall6Max = SPAWNODDS_BALL6_MAX_GREENGEM;
			spawnoddsBall7Max = SPAWNODDS_BALL7_MAX_GREENGEM;
		}
		else if(currentPhase == PHASE_FIGHT_YELLOWGEM) {
			spawnoddsBall1Max = SPAWNODDS_BALL1_MAX_YELLOWGEM;
			spawnoddsBall2Max = SPAWNODDS_BALL2_MAX_YELLOWGEM;
			spawnoddsBall3Max = SPAWNODDS_BALL3_MAX_YELLOWGEM;
			spawnoddsBall4Max = SPAWNODDS_BALL4_MAX_YELLOWGEM;
			spawnoddsBall5Max = SPAWNODDS_BALL5_MAX_YELLOWGEM;
			spawnoddsBall6Max = SPAWNODDS_BALL6_MAX_YELLOWGEM;
			spawnoddsBall7Max = SPAWNODDS_BALL7_MAX_YELLOWGEM;
		}
		else if(currentPhase == PHASE_FIGHT_REDGEM) {
			spawnoddsBall1Max = SPAWNODDS_BALL1_MAX_REDGEM;
			spawnoddsBall2Max = SPAWNODDS_BALL2_MAX_REDGEM;
			spawnoddsBall3Max = SPAWNODDS_BALL3_MAX_REDGEM;
			spawnoddsBall4Max = SPAWNODDS_BALL4_MAX_REDGEM;
			spawnoddsBall5Max = SPAWNODDS_BALL5_MAX_REDGEM;
			spawnoddsBall6Max = SPAWNODDS_BALL6_MAX_REDGEM;
			spawnoddsBall7Max = SPAWNODDS_BALL7_MAX_REDGEM;
		}
	}
	
	// set new cooldown time between current min-max values for a weapon or all at once
	private void setWeaponIdleTime(int weapon) {
		if(weapon == WEAPON_ALL) {
			turretIdleTime = (long) Support.randInt((int) turretIdleMin, (int) turretIdleMax);
			topPortalIdleTime = (long) Support.randInt((int) topPortalIdleMin, (int) topPortalIdleMax);
			bottomPortalIdleTime = (long) Support.randInt((int) bottomPortalIdleMin, (int) bottomPortalIdleMax);
			leftRightPortalIdleTime = (long) Support.randInt((int) leftRightPortalIdleMin, (int) leftRightPortalIdleMax);
		}
		else if(weapon == WEAPON_TURRET) {
			turretIdleTime = (long) Support.randInt((int) turretIdleMin, (int) turretIdleMax);
		}
		else if(weapon == WEAPON_TOPPORTAL) {
			topPortalIdleTime = (long) Support.randInt((int) topPortalIdleMin, (int) topPortalIdleMax);
		}
		else if(weapon == WEAPON_BOTTOMPORTAL) {
			bottomPortalIdleTime = (long) Support.randInt((int) bottomPortalIdleMin, (int) bottomPortalIdleMax);
			bottomPortalIdleTime += bottomPortalLastEnemySpawnedExtra;
		}
		else if(weapon == WEAPON_LEFTRIGHTPORTAL) {
			leftRightPortalIdleTime = (long) Support.randInt((int) leftRightPortalIdleMin, (int) leftRightPortalIdleMax);
		}
		else System.out.println("Error in Boss setWeaponIdleTime, weapon " + weapon + " doesn't exist.");
	}
	
	private void updatePhase() {
		/*if(currentPhase == PHASE_PREFIGHT_DIALOG) {
			if(currentPrefightDialogProgress == DIALOGPROGRESS_PREFIGHT_INITIAL) {
				currentPhase = PHASE_PREFIGHT_PHASEIN;
			}
		}
		else if(currentPhase == PHASE_PREFIGHT_PHASEIN) {
			if(frameIsDoneFadingIn && currentPrefightDialogProgress == DIALOGPROGRESS_PREFIGHT_FINISHED) {
			}
		}*/
		if(currentPhase == PHASE_FIGHT_GREENGEM) {
			if(health <= 10) {
				setPhase(PHASE_FIGHT_YELLOWGEM);
			}
		}
		else if(currentPhase == PHASE_FIGHT_YELLOWGEM) {
			if(health <= 5) {
				setPhase(PHASE_FIGHT_REDGEM);
			}
		}
		else if(currentPhase == PHASE_FIGHT_REDGEM) {
			if(health <= 0) {
				setPhase(PHASE_FIGHT_DESPERATION);
			}
		}
		else if(currentPhase == PHASE_FIGHT_DESPERATION) {
			if(finalAttackIsFinished) {
				setPhase(PHASE_POSTFIGHT_DEATHBED);
			}
		}
	}
	
	private void setPhase(int phase) {
		if(phase == PHASE_FIGHT_GREENGEM) {
			currentPhase = PHASE_FIGHT_GREENGEM;
			currentGemMode = GEMMODE_GREEN;
			swapAnimationFrames();
			updatePortalMinMaxIdleTimes();
			updateTopPortalSpawnOdds();
			shieldRestoreTime = SHIELD_RESTORETIME_GREENGEM;
			restoreShield();
			setWeaponIdleTime(WEAPON_ALL);
		}
		else if(phase == PHASE_FIGHT_YELLOWGEM) {
			currentPhase = PHASE_FIGHT_YELLOWGEM;
			currentGemMode = GEMMODE_YELLOW;
			swapAnimationFrames();
			updatePortalMinMaxIdleTimes();
			updateTopPortalSpawnOdds();
			shieldRestoreTime = SHIELD_RESTORETIME_YELLOWGEM;
			restoreShield();
			setWeaponIdleTime(WEAPON_ALL);
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_MIDFIGHT_YELLOWGEMSTART, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(2500);
		}
		else if(phase == PHASE_FIGHT_REDGEM) {
			currentPhase = PHASE_FIGHT_REDGEM;
			currentGemMode = GEMMODE_RED;
			swapAnimationFrames();
			updatePortalMinMaxIdleTimes();
			updateTopPortalSpawnOdds();
			shieldRestoreTime = SHIELD_RESTORETIME_REDGEM;
			restoreShield();
			setWeaponIdleTime(WEAPON_ALL);
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_MIDFIGHT_REDGEMSTART, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(2500);
		}
		else if(phase == PHASE_FIGHT_DESPERATION) {
			portalTopActive = false;
			portalBottomActive = false;
			portalLeftActive = false;
			portalRightActive = false;
			currentPhase = PHASE_FIGHT_DESPERATION;
			swapAnimationFrames();
			updatePortalMinMaxIdleTimes();
			shieldRestoreTime = SHIELD_RESTORETIME_NOFIGHT;
			breakShield();
			setWeaponIdleTime(WEAPON_ALL);
			turretAttackPending = false;
			topPortalAttackPending = false;
			bottomPortalAttackPending = false;
			leftRightPortalAttackPending = false;
			finalAttackIsFinished = false;
			currentAnimation = FINALATTACK_SHIELD;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_MIDFIGHT_FINALATTACKSTART, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(1500);
			// for GameState communication
			prepareForFinalAttack = true;
		}
		else if(phase == PHASE_POSTFIGHT_DEATHBED) {
			currentPhase = PHASE_POSTFIGHT_DEATHBED;
			currentGemMode = GEMMODE_PALE;
			swapAnimationFrames();
			width = gemWidth;
			height = gemHeight;
			cwidth = gemCWidth;
			cheight = gemCHeight;
			drawFrame = false;
			portalTopActive = false;
			portalBottomActive = false;
			portalLeftActive = false;
			portalRightActive = false;
			killable = true;
			health = 1;
		}
	}

	private void getNextPosition() {
		// falling
		if(falling) {
			dy += fallSpeed;
		}
		// band aid solutions are fun sometimes
		else if(permanentFall) {
			dy += fallSpeed;
		}
	}
	
	public void updateCurrentAnimation() {
		//if(!aggressive) return;
		
		// for each ended warning time the weapon in question spawns an attack
		checkWarningTimes();
		
		// prefight states start
		// phase switches handle changing currentAnimation from here down to FIGHT_IDLE
		if(currentAnimation == PREFIGHT_DIALOGIDLE) {
			if(currentPrefightDialogProgress == DIALOGPROGRESS_PREFIGHT_BOSSAPPEARANCE) {
				currentAnimation = PREFIGHT_APPEAR;
				fadeInFrameStartOpacity = currentFrameOpacity;
				fadeInFrameEndOpacity = Support.NORMALOPACITY;
				fadeInTotalOpacityChange = fadeInFrameEndOpacity - fadeInFrameStartOpacity;
				fadeInFrameStart = System.nanoTime();
				frameIsDoneFadingIn = false;
				drawFrame = true;
				JukeBox.playWithRecommendedVolume("bossappear");
			}
			else if(dialog != null && dialog.getDialogConversation() != null) {
				if(
					dialog.getDialogConversation().conversationContent.get(dialog.getCurrentLine()).specialTag == 
					DialogConversation.SPECIALTAG_BOSSFADEIN
				) {
					currentPrefightDialogProgress = DIALOGPROGRESS_PREFIGHT_BOSSAPPEARANCE;
				}
			}
		}
		else if(currentAnimation == PREFIGHT_APPEAR) {
			if(frameIsDoneFadingIn && currentPrefightDialogProgress == DIALOGPROGRESS_PREFIGHT_FINISHED) {
				startFight();
			}
			
			if(!frameIsDoneFadingIn) {
				// update fade in opacity every frame until it's done and returns true
				frameIsDoneFadingIn = setFadeInFrameOpacity();
			}
			
			if(dialog.isDialogOver()) {
				currentPrefightDialogProgress = DIALOGPROGRESS_PREFIGHT_FINISHED;
			}
		}
		//prefight states end
		
		// normal fight states start
		else if(currentAnimation == FIGHT_IDLE) {
			long currentTime = System.nanoTime();
			long passedIdleTime = (currentTime - idleStart) / 1000000;
			long passedTurretTime = (currentTime - turretIdleStart) / 1000000;
			long passedTopPortalTime = (currentTime - topPortalIdleStart) / 1000000;
			long passedBottomPortalTime = (currentTime - bottomPortalIdleStart) / 1000000;
			long passedLeftRightPortalTime = (currentTime - leftRightPortalIdleStart) / 1000000;
			
			if(passedIdleTime >= idleTime) {
				//currentAnimation = BALL7;
			}
			if(passedTurretTime >= turretIdleTime && !turretAttackPending) {
				currentAnimation = FIGHT_TURRET;
			}
			if(passedTopPortalTime >= topPortalIdleTime && !topPortalAttackPending) {
				currentAnimation = FIGHT_TOPPORTAL;
			}
			if(passedBottomPortalTime >= bottomPortalIdleTime && !bottomPortalAttackPending) {
				currentAnimation = FIGHT_BOTTOMPORTAL;
			}
			if(passedLeftRightPortalTime >= leftRightPortalIdleTime && !leftRightPortalAttackPending) {
				currentAnimation = FIGHT_LEFTRIGHTPORTAL;
			}
		}
		else if(currentAnimation == FIGHT_TURRET) {
			currentAnimation = FIGHT_IDLE;
			turretWarningStart = System.nanoTime();
			createTurretWarning = true;
			turretAttackPending = true;
		}
		else if(currentAnimation == FIGHT_TOPPORTAL) {
			currentAnimation = FIGHT_IDLE;
			topPortalWarningStart = System.nanoTime();
			createTopPortalWarning = true;
			topPortalAttackPending = true;
		}
		else if(currentAnimation == FIGHT_BOTTOMPORTAL) {
			currentAnimation = FIGHT_IDLE;
			bottomPortalWarningStart = System.nanoTime();
			createBottomPortalWarning = true;
			bottomPortalAttackPending = true;
		}
		else if(currentAnimation == FIGHT_LEFTRIGHTPORTAL) {
			currentAnimation = FIGHT_IDLE;
			leftRightPortalWarningStart = System.nanoTime();
			createLeftRightPortalWarning = true;
			leftRightPortalAttackPending = true;
		}
		// normal fight states end
		
		// final attack states start
		else if(currentAnimation == FINALATTACK_IDLE) {
			if(playerIsCaughtInFinalAttackGrab) {
				long passedIdleTime = (System.nanoTime() - finalAttackLastWaveStart) / 1000000;
				
				if(passedIdleTime >= finalAttackCurrentWaveWaitTime) {
					currentAnimation = nextWave;
				}
			}
			else if(
				(int) playerX == getx() + finalAttackPosX && 
				(int) playerY == gety() + finalAttackPosY
			){
				playerIsCaughtInFinalAttackGrab = true;
				finalAttackLastWaveStart = System.nanoTime();
				finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_INITIALGRAB;
				nextWave = FINALATTACK_WAVE_1;
				dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_MIDFIGHT_FINALATTACKCAUGHT, Support.bossSpeakerStatus);
				dialog.setAllowManualProgress(false);
				dialog.setAllowAutomaticProgress(true);
				dialog.setDialogLineAutoContinueTime(1750);
			}
		}
		else if(currentAnimation == FINALATTACK_SHIELD) {
			currentAnimation = FINALATTACK_IDLE;
			idleStart = System.nanoTime();
			createShield = true;
			playerIsCaughtInFinalAttackGrab = false;
		}
		// ring, red
		else if(currentAnimation == FINALATTACK_WAVE_1) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_1;
			nextWave = FINALATTACK_WAVE_2;//TODO CHANGE TO TEST
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RED,
				FINALATTACK_IDLETIME_DEFAULT,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// ring, green
		else if(currentAnimation == FINALATTACK_WAVE_2) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_2;
			nextWave = FINALATTACK_WAVE_3;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_GREEN,
				FINALATTACK_IDLETIME_DEFAULT + 750,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 25.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// ring, mixed
		else if(currentAnimation == FINALATTACK_WAVE_3) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_3;
			nextWave = FINALATTACK_WAVE_4;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RANDOM,
				FINALATTACK_IDLETIME_DEFAULT + 1500,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 50.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// spiral, random, full lap, outer
		else if(currentAnimation == FINALATTACK_WAVE_4) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_4;
			nextWave = FINALATTACK_WAVE_5;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RANDOM,
				FINALATTACK_IDLETIME_DEFAULT + 3400,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 50.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT + 12,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack2 = true;
		}
		// spiral, red, half lap, first half, inner
		else if(currentAnimation == FINALATTACK_WAVE_5) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_5;
			nextWave = FINALATTACK_WAVE_6;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RED,
				FINALATTACK_IDLETIME_DEFAULT + 400,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				FINALATTACK_GEMAMOUNT_DEFAULT / 2,
				FINALATTACK_RADIUS_DEFAULT + 25.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack2 = true;
		}
		// spiral, green, half lap, later half, inner
		else if(currentAnimation == FINALATTACK_WAVE_6) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_6;
			nextWave = FINALATTACK_WAVE_7;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_GREEN,
				FINALATTACK_IDLETIME_DEFAULT + 400,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				FINALATTACK_GEMAMOUNT_DEFAULT / 2,
				FINALATTACK_RADIUS_DEFAULT + 25.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT + 12,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack2 = true;
		}
		// ring, red, close, slow
		else if(currentAnimation == FINALATTACK_WAVE_7) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_7;
			nextWave = FINALATTACK_WAVE_8;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RED,
				FINALATTACK_IDLETIME_DEFAULT,
				FINALATTACK_ATTACKSPEED_DEFAULT - 0.9,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 30.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// ring, green, far, fast
		else if(currentAnimation == FINALATTACK_WAVE_8) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_8;
			nextWave = FINALATTACK_WAVE_9;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_GREEN,
				FINALATTACK_IDLETIME_DEFAULT - 300,
				FINALATTACK_ATTACKSPEED_DEFAULT + 1.95,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 53.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// spiral, red, fast activation, used together with green
		else if(currentAnimation == FINALATTACK_WAVE_9) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_9;
			nextWave = FINALATTACK_WAVE_10;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RED,
				FINALATTACK_IDLETIME_DEFAULT - 1100,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				(int)(FINALATTACK_GEMAMOUNT_DEFAULT * 2.5),
				FINALATTACK_RADIUS_DEFAULT + 35.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT + 6,
				FINALATTACK_SPAWNINTERVAL_DEFAULT + 70
			);
			createFinalAttack2 = true;
		}
		// spiral, green, fast activation, used together with red
		else if(currentAnimation == FINALATTACK_WAVE_10) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_10;
			nextWave = FINALATTACK_WAVE_11;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_GREEN,
				FINALATTACK_IDLETIME_DEFAULT - 1100,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				(int)(FINALATTACK_GEMAMOUNT_DEFAULT * 2.5),
				FINALATTACK_RADIUS_DEFAULT + 35.0,
				FINALATTACK_STARTDEGREEMODE_DEFAULT + 18,
				FINALATTACK_SPAWNINTERVAL_DEFAULT + 70
			);
			createFinalAttack2 = true;
		}
		// spiral with varying radius, random
		else if(currentAnimation == FINALATTACK_WAVE_11) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_11;
			nextWave = FINALATTACK_WAVE_12;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RANDOM,
				FINALATTACK_IDLETIME_DEFAULT,
				FINALATTACK_ATTACKSPEED_DEFAULT + 0.4,
				FINALATTACK_GEMAMOUNT_DEFAULT * 2,
				FINALATTACK_RADIUS_DEFAULT,
				FINALATTACK_STARTDEGREEMODE_DEFAULT + 6,
				FINALATTACK_SPAWNINTERVAL_DEFAULT + 290
			);
			setFinalAttackVaryingRadiusValues(
				8.0,
				5.0,
				7,
				true
			);
			createFinalAttack4 = true;
		}
		// ring, red
		else if(currentAnimation == FINALATTACK_WAVE_12) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_12;
			nextWave = FINALATTACK_WAVE_13;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RED,
				FINALATTACK_IDLETIME_DEFAULT - 750,
				FINALATTACK_ATTACKSPEED_DEFAULT - 0.4,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 25,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// ring, green
		else if(currentAnimation == FINALATTACK_WAVE_13) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_13;
			nextWave = FINALATTACK_WAVE_14;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_GREEN,
				FINALATTACK_IDLETIME_DEFAULT - 750,
				FINALATTACK_ATTACKSPEED_DEFAULT - 0.4,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 40,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// ring, red
		else if(currentAnimation == FINALATTACK_WAVE_14) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_14;
			nextWave = FINALATTACK_WAVE_15;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RED,
				FINALATTACK_IDLETIME_DEFAULT - 750,
				FINALATTACK_ATTACKSPEED_DEFAULT - 0.4,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 25,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// ring, green
		else if(currentAnimation == FINALATTACK_WAVE_15) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_15;
			nextWave = FINALATTACK_WAVE_16;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_GREEN,
				FINALATTACK_IDLETIME_DEFAULT - 750,
				FINALATTACK_ATTACKSPEED_DEFAULT - 0.4,
				FINALATTACK_GEMAMOUNT_DEFAULT,
				FINALATTACK_RADIUS_DEFAULT + 40,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack1 = true;
		}
		// final spam
		else if(currentAnimation == FINALATTACK_WAVE_16) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_WAVEPAUSE_16;
			nextWave = FINALATTACK_DESTRUCTION_1;
			setFinalAttackWaveValues(
				FINALATTACK_GEMCOLOR_RANDOM,
				FINALATTACK_IDLETIME_DEFAULT + 2850,
				FINALATTACK_ATTACKSPEED_DEFAULT,
				1650,
				FINALATTACK_RADIUS_DEFAULT,
				FINALATTACK_STARTDEGREEMODE_DEFAULT,
				FINALATTACK_SPAWNINTERVAL_DEFAULT
			);
			createFinalAttack3 = true;
		}
		// self destruction first trigger
		else if(currentAnimation == FINALATTACK_DESTRUCTION_1) {
			currentAnimation = FINALATTACK_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_DESTRUCTIONPAUSE_1;
			nextWave = FINALATTACK_DESTRUCTION_2;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_MIDFIGHT_FINALATTACKGROW, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(2000);
			destructFinalAttack = true;
		}
		// self destruction second trigger
		else if(currentAnimation == FINALATTACK_DESTRUCTION_2) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = FINALATTACK_DESTRUCTIONPAUSE_2;
			nextWave = DEATHBED_WAITTRIGGER_1;
			finalAttackIsFinished = true;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_FINALATTACKDESTRUCTION, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(1600);
		}
		// final attack states end
		
		// post death states start, continues to use old names...
		else if(currentAnimation == DEATHBED_IDLE) {
			long passedIdleTime = (System.nanoTime() - finalAttackLastWaveStart) / 1000000;
			
			if(passedIdleTime >= finalAttackCurrentWaveWaitTime) {
				currentAnimation = nextWave;
			}
		}
		// waiting triggers conversation and eventually an escape for the boss if not finished off
		else if(currentAnimation == DEATHBED_WAITTRIGGER_1) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_1;
			nextWave = DEATHBED_WAITTRIGGER_2;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_DEATHBED_1, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(3000);
		}
		else if(currentAnimation == DEATHBED_WAITTRIGGER_2) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_2;
			nextWave = DEATHBED_WAITTRIGGER_3;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_DEATHBED_2, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(2100);
		}
		else if(currentAnimation == DEATHBED_WAITTRIGGER_3) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_3;
			nextWave = DEATHBED_WAITTRIGGER_4;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_DEATHBED_3, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(2100);
		}
		else if(currentAnimation == DEATHBED_WAITTRIGGER_4) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_4;
			nextWave = DEATHBED_WAITTRIGGER_5;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_DEATHBED_4, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(2300);
			createEscapePortal = true;
		}
		else if(currentAnimation == DEATHBED_WAITTRIGGER_5) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_5;
			nextWave = DEATHBED_WAITTRIGGER_6;
			permanentFall = true;
			dx = 0.0;
			dy = 0.0;
			fallSpeed = -0.03;
		}
		else if(currentAnimation == DEATHBED_WAITTRIGGER_6) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_6;
			nextWave = DEATHBED_WAITTRIGGER_7;
			gemAnimation.setFrames(invisibleSprites);
			gemAnimation.setDelay(-1);
			gemMiddleAnimation.setFrames(invisibleSprites);
			gemMiddleAnimation.setDelay(-1);
			permanentFall = false;
			dx = 0.0;
			dy = 0.0;
			fallSpeed = 0.2;
			invulnerable = true;
			Support.endingTitle = Support.ENDING_TITLE_SPARE;
			Support.endingText = Support.ENDING_TEXT_SPARE;
		}
		else if(currentAnimation == DEATHBED_WAITTRIGGER_7) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_7;
			nextWave = DEATHBED_WAITTRIGGER_8;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_ESCAPED_1, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(3000);
		}
		// old autorun version
		/*else if(currentAnimation == DEATHBED_WAITTRIGGER_8) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_8;
			nextWave = DEATHBED_WAITTRIGGER_9;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_ESCAPED_2, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(5000);
		}*/
		// new manual version
		else if(currentAnimation == DEATHBED_WAITTRIGGER_8) {
			if(dialog.isDialogOver()) {
				if(fadeInAllowed) currentAnimation = DEATHBED_WAITTRIGGER_9;
				else {
					dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_ESCAPED_2, Support.bossSpeakerStatus);
					dialog.setAllowManualProgress(true);
					dialog.setAllowAutomaticProgress(false);
				}
			}
			else if(dialog != null && dialog.getDialogConversation() != null) {
				if(
					dialog.getDialogConversation().conversationContent.get(dialog.getCurrentLine()).specialTag == 
					DialogConversation.SPECIALTAG_GAMEFADEOUT
				) {
					fadeInAllowed = true;
				}
			}
		}
		// game fades out and ends if not killed until this point
		else if(currentAnimation == DEATHBED_WAITTRIGGER_9) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = DEATHBED_WAITTRIGGERPAUSE_9;
			fadeInEndScreen = true;
		}
		// killed ending starts here
		else if(currentAnimation == KILLED_WAITTRIGGER_1) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = KILLED_WAITTRIGGERPAUSE_1;
			nextWave = KILLED_WAITTRIGGER_2;
			permanentFall = false;
			fallSpeed = 0.2;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_KILLED_1, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(1000);
			Support.endingTitle = Support.ENDING_TITLE_KILL;
			Support.endingText = Support.ENDING_TEXT_KILL;
			invulnerable = true;
			JukeBox.playWithRecommendedVolume("bosspredeath");
		}
		else if(currentAnimation == KILLED_WAITTRIGGER_2) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = KILLED_WAITTRIGGERPAUSE_2;
			nextWave = KILLED_WAITTRIGGER_3;
			drawBoss = false;
			createBossGemPieces = true;
			createBossExplosion = true;
			JukeBox.stop("bosspredeath");
		}
		// old autorun version
		/*else if(currentAnimation == KILLED_WAITTRIGGER_3) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = KILLED_WAITTRIGGERPAUSE_3;
			nextWave = KILLED_WAITTRIGGER_4;
			dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_KILLED_2, Support.bossSpeakerStatus);
			dialog.setAllowManualProgress(false);
			dialog.setAllowAutomaticProgress(true);
			dialog.setDialogLineAutoContinueTime(5000);
		}*/
		// new manual version
		else if(currentAnimation == KILLED_WAITTRIGGER_3) {
			if(dialog.isDialogOver()) {
				if(fadeInAllowed) currentAnimation = KILLED_WAITTRIGGER_4;
				else {
					dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_POSTFIGHT_KILLED_2, Support.bossSpeakerStatus);
					dialog.setAllowManualProgress(true);
					dialog.setAllowAutomaticProgress(false);
				}
			}
			else if(dialog != null && dialog.getDialogConversation() != null) {
				if(
					dialog.getDialogConversation().conversationContent.get(dialog.getCurrentLine()).specialTag == 
					DialogConversation.SPECIALTAG_GAMEFADEOUT
				) {
					fadeInAllowed = true;
				}
			}
		}
		else if(currentAnimation == KILLED_WAITTRIGGER_4) {
			currentAnimation = DEATHBED_IDLE;
			finalAttackLastWaveStart = System.nanoTime();
			finalAttackCurrentWaveWaitTime = KILLED_WAITTRIGGERPAUSE_3;
			nextWave = KILLED_WAITTRIGGER_4;
			fadeInEndScreen = true;
		}
		// post death states end
		
		// old final attack spawners, could use?
		else if(currentAnimation == FINALATTACK1) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createFinalAttack1 = true;
		}
		else if(currentAnimation == FINALATTACK2) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createFinalAttack2 = true;
			finalAttackGemColor = FINALATTACK_GEMCOLOR_RED;
			finalAttackIdleTime = 950;
			finalAttackSpeed = 2.2;
			finalAttackGemAmount = 24 * 3;
		}
		else if(currentAnimation == FINALATTACK3) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createFinalAttack3 = true;
			finalAttackGemColor = FINALATTACK_GEMCOLOR_RANDOM;
			finalAttackIdleTime = 4500;
			finalAttackSpeed = 2.0;
			finalAttackGemAmount = 1500;
		}
		
		// old spawners below this point, don't use
		else if(currentAnimation == BALL1) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall1 = true;
		}
		else if(currentAnimation == BALL2) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall2 = true;
		}
		else if(currentAnimation == BALL3) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall3 = true;
		}
		else if(currentAnimation == BALL4CHASE) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall4Chase = true;
		}
		else if(currentAnimation == BALL4SPLIT) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall4Split = true;
		}
		else if(currentAnimation == BALL6) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall6 = true;
		}
		else if(currentAnimation == BALL7) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createBall7 = true;
		}
		else if(currentAnimation == WALL) {
			currentAnimation = FIGHT_IDLE;
			idleStart = System.nanoTime();
			createWall = true;
		}
	}
	
	private void startFight() {
		idleStart = System.nanoTime();
		turretIdleStart = System.nanoTime();
		topPortalIdleStart = System.nanoTime();
		bottomPortalIdleStart = System.nanoTime();
		leftRightPortalIdleStart = System.nanoTime();
		setPhase(PHASE_FIGHT_GREENGEM);
		currentAnimation = FIGHT_IDLE;
		portalTopActive = true;
		portalBottomActive = true;
		portalLeftActive = true;
		portalRightActive = true;
		invulnerable = false;
		// used to tell the GameState in question (Level7State) the fight has begun so it can respond
		fightStarted = true;
	}
	
	// boolean returns for communication with GameState
	public int getFinalAttackShieldId() { return finalAttackShieldId; }
	public boolean hasFightStarted() { return fightStarted; }
	public boolean shouldPrepareForFinalAttack() { return prepareForFinalAttack; }
	public boolean shouldDestructFinalAttack() { return destructFinalAttack; }
	public boolean shouldFadeInEndScreen() { return fadeInEndScreen; }

	// returns if done fading or not
	private boolean setFadeInFrameOpacity() {
		long elapsedFadeInTime = (System.nanoTime() - fadeInFrameStart) / 1000000;
		double fadeTimePassedOfTotalInDecimal = (double) elapsedFadeInTime / (double) fadeInFrameTime;
		if(fadeTimePassedOfTotalInDecimal >= 1) {
			currentFrameOpacity = fadeInFrameEndOpacity;
			return true;
		}
		else currentFrameOpacity = 
			(float) ((double) fadeInFrameStartOpacity + fadeTimePassedOfTotalInDecimal * (double) fadeInTotalOpacityChange);
		return false;
	}

	private void setFinalAttackWaveValues(
		int finalAttackGemColor,
		long finalAttackIdleTime,
		double finalAttackSpeed,
		int finalAttackGemAmount,
		double finalAttackRadius,
		int finalAttackStartDegreeMode,
		long finalAttackSpawnInterval
	) {
		this.finalAttackGemColor = finalAttackGemColor;
		this.finalAttackIdleTime = finalAttackIdleTime;
		this.finalAttackSpeed = finalAttackSpeed;
		this.finalAttackGemAmount = finalAttackGemAmount;
		this.finalAttackRadius = finalAttackRadius;
		this.finalAttackStartDegreeMode = finalAttackStartDegreeMode;
		this.finalAttackSpawnInterval = finalAttackSpawnInterval;
	}
	
	private void setFinalAttackVaryingRadiusValues(
		double finalAttackRadiusDifferenceInitial,
		double finalAttackRadiusDifferenceIncrease,
		int finalAttackRadiusAmountOfChanges,
		boolean outwardStartDirection
	) {
		this.finalAttackRadiusDifferenceInitial = finalAttackRadiusDifferenceInitial;
		this.finalAttackRadiusDifferenceIncrease = finalAttackRadiusDifferenceIncrease;
		this.finalAttackRadiusAmountOfChanges = finalAttackRadiusAmountOfChanges;
		this.outwardStartDirection = outwardStartDirection;
	}

	private void checkWarningTimes() {
		if(turretAttackPending) {
			long passedTurretWarningTime = (System.nanoTime() - turretWarningStart) / 1000000;
			
			if(passedTurretWarningTime >= warningTime) {
				createRandomTurretObject();
				setWeaponIdleTime(WEAPON_TURRET);
				turretIdleStart = System.nanoTime();
				turretAttackPending = false;
			}
		}
		
		if(topPortalAttackPending) {
			long passedTopPortalWarningTime = (System.nanoTime() - topPortalWarningStart) / 1000000;
			
			if(passedTopPortalWarningTime >= warningTime) {
				createRandomTopPortalObject();
				setWeaponIdleTime(WEAPON_TOPPORTAL);
				topPortalIdleStart = System.nanoTime();
				topPortalAttackPending = false;
			}
		}
		
		if(bottomPortalAttackPending) {
			long passedTopPortalWarningTime = (System.nanoTime() - bottomPortalWarningStart) / 1000000;
			
			if(passedTopPortalWarningTime >= warningTime) {
				createRandomBottomPortalObject();
				setWeaponIdleTime(WEAPON_BOTTOMPORTAL);
				bottomPortalIdleStart = System.nanoTime();
				bottomPortalAttackPending = false;
			}
		}
		
		if(leftRightPortalAttackPending) {
			long passedTopPortalWarningTime = (System.nanoTime() - leftRightPortalWarningStart) / 1000000;
			
			if(passedTopPortalWarningTime >= warningTime) {
				createWall = true;
				leftRightPortalIdleStart = System.nanoTime();
				setWeaponIdleTime(WEAPON_LEFTRIGHTPORTAL);
				setWallSpawnMode(SPAWNMODE_WALL_RANDOM);
				leftRightPortalAttackPending = false;
			}
		}
	}

	private void setWallSpawnMode(int spawnmode) {
		if(spawnmode == SPAWNMODE_WALL_RANDOM) {
			if(Support.randInt(0, 1) == 0) spawnmode = SPAWNMODE_WALL_TOP;
			else spawnmode = SPAWNMODE_WALL_BOTTOM;
		}
		
		if(spawnmode == SPAWNMODE_WALL_TOP) currentWallSpawnPosY = wallTopSpawnPosY;
		else if(spawnmode == SPAWNMODE_WALL_BOTTOM) currentWallSpawnPosY = wallBottomSpawnPosY;
		else {
			currentWallSpawnPosY = wallTopSpawnPosY;
			System.out.println("Error in Boss setWallSpawnMode, spawnmode (" + spawnmode + ") doesn't exist.");
		}
	}

	private void createRandomTurretObject() {
		int roll = Support.randInt(0, 2);
		if(roll == 0) {
			prepareTurretAttackValues(TURRET_ATTACK1);
			if(currentPhase == PHASE_FIGHT_REDGEM) setTurretPhaseModifier(PHASE_FIGHT_REDGEM, TURRET_ATTACK1);
			createTurretFire1 = true;
		}
		else if(roll == 1) {
			prepareTurretAttackValues(TURRET_ATTACK2);
			if(currentPhase == PHASE_FIGHT_REDGEM) setTurretPhaseModifier(PHASE_FIGHT_REDGEM, TURRET_ATTACK2);
			createTurretFire2 = true;
		}
		else if(roll == 2) {
			prepareTurretAttackValues(TURRET_ATTACK3);
			if(currentPhase == PHASE_FIGHT_REDGEM) setTurretPhaseModifier(PHASE_FIGHT_REDGEM, TURRET_ATTACK3);
			createTurretFire3 = true;
		}
		else {
			System.out.println("Error in Boss createRandomTurretObject, roll (" + roll + ") was out of bounds.");
		}
	}

	// also gives default color as random, update that manually after each call if you need something else
	private void prepareTurretAttackValues(int attack) {
		if(attack == TURRET_ATTACK1) {
			turretCurrentTotalSpawnAmount = TURRET_ATTACK1_TOTALSPAWNAMOUNT;
			turretCurrentColor = TURRET_COLOR_RANDOM;
			turretCurrentTotalSpeedMin = TURRET_ATTACK1_SPEED_MIN;
			turretCurrentTotalSpeedMax = TURRET_ATTACK1_SPEED_MAX;
			turret1CurrentDegreesMin = TURRET1_ATTACK1_DEGREES_MIN;
			turret1CurrentDegreesMax = TURRET1_ATTACK1_DEGREES_MAX;
			turret2CurrentDegreesMin = TURRET2_ATTACK1_DEGREES_MIN;
			turret2CurrentDegreesMax = TURRET2_ATTACK1_DEGREES_MAX;
			turret3CurrentDegreesMin = TURRET3_ATTACK1_DEGREES_MIN;
			turret3CurrentDegreesMax = TURRET3_ATTACK1_DEGREES_MAX;
			turret4CurrentDegreesMin = TURRET4_ATTACK1_DEGREES_MIN;
			turret4CurrentDegreesMax = TURRET4_ATTACK1_DEGREES_MAX;
			turretCurrentSpawnInterval = TURRET_ATTACK1_SPAWNINTERVAL;
			turretCurrentSpawnBurstAmount = TURRET_ATTACK1_SPAWNBURSTAMOUNT;
			turretCurrentInitialIdleTime = TURRET_ATTACK1_INITIALIDLE;
		}
		else if(attack == TURRET_ATTACK2) {
			turretCurrentTotalSpawnAmount = TURRET_ATTACK2_TOTALSPAWNAMOUNT;
			turretCurrentColor = TURRET_COLOR_RANDOM;
			turretCurrentTotalSpeedMin = TURRET_ATTACK2_SPEED_MIN;
			turretCurrentTotalSpeedMax = TURRET_ATTACK2_SPEED_MAX;
			
			double extraDegreeVariation = Support.getDoubleWithXExtraDecimals(
				Support.randInt(
					0,
					(int) (TURRET_ATTACK2_DEGREES_VARIATION_EXTRA_MAX * 100)
				),
				2
			);
			turret1CurrentDegreesMin = TURRET1_ATTACK2_DEGREES_MIN - extraDegreeVariation;
			turret1CurrentDegreesMax = TURRET1_ATTACK2_DEGREES_MAX + extraDegreeVariation;
			turret2CurrentDegreesMin = TURRET2_ATTACK2_DEGREES_MIN - extraDegreeVariation;
			turret2CurrentDegreesMax = TURRET2_ATTACK2_DEGREES_MAX + extraDegreeVariation;
			turret3CurrentDegreesMin = TURRET3_ATTACK2_DEGREES_MIN - extraDegreeVariation;
			turret3CurrentDegreesMax = TURRET3_ATTACK2_DEGREES_MAX + extraDegreeVariation;
			turret4CurrentDegreesMin = TURRET4_ATTACK2_DEGREES_MIN - extraDegreeVariation;
			turret4CurrentDegreesMax = TURRET4_ATTACK2_DEGREES_MAX + extraDegreeVariation;
			turretCurrentSpawnInterval = TURRET_ATTACK2_SPAWNINTERVAL;
			turretCurrentSpawnBurstAmount = TURRET_ATTACK2_SPAWNBURSTAMOUNT;
			// only good for first turret, update later as needed
			turretCurrentInitialIdleTime = TURRET1_ATTACK2_INITIALIDLE;
		}
		else if(attack == TURRET_ATTACK3) {
			turretCurrentTotalSpawnAmount = TURRET_ATTACK3_TOTALSPAWNAMOUNT;
			turretCurrentColor = TURRET_COLOR_RANDOM;
			turretCurrentTotalSpeedMin = TURRET_ATTACK3_SPEED_MIN;
			turretCurrentTotalSpeedMax = TURRET_ATTACK3_SPEED_MAX;
			turret1CurrentDegreesMin = TURRET1_ATTACK3_DEGREES_MIN;
			turret1CurrentDegreesMax = TURRET1_ATTACK3_DEGREES_MAX;
			turret2CurrentDegreesMin = TURRET2_ATTACK3_DEGREES_MIN;
			turret2CurrentDegreesMax = TURRET2_ATTACK3_DEGREES_MAX;
			turret3CurrentDegreesMin = TURRET3_ATTACK3_DEGREES_MIN;
			turret3CurrentDegreesMax = TURRET3_ATTACK3_DEGREES_MAX;
			turret4CurrentDegreesMin = TURRET4_ATTACK3_DEGREES_MIN;
			turret4CurrentDegreesMax = TURRET4_ATTACK3_DEGREES_MAX;
			turretCurrentSpawnInterval = TURRET_ATTACK3_SPAWNINTERVAL;
			turretCurrentSpawnBurstAmount = TURRET_ATTACK3_SPAWNBURSTAMOUNT;
			// only good for first turret, update later as needed
			turretCurrentInitialIdleTime = TURRET_ATTACK3_INITIALIDLE;
		}
		else System.out.println("Error in Boss prepareTurretAttackValues, attack " + attack + "matches no value.");
	}
	
	// red gem phase additionally modifies some values to make the attacks harder
	private void setTurretPhaseModifier(int phase, int attack) {
		if(phase == PHASE_FIGHT_REDGEM) {
			if(attack == TURRET_ATTACK1) {
				turretCurrentTotalSpawnAmount += TURRET_ATTACK1_TOTALSPAWNAMOUNT_BONUS_REDGEM;
				turretCurrentTotalSpeedMin += TURRET_ATTACK1_SPEED_MIN_BONUS_REDGEM;
				turretCurrentTotalSpeedMax += TURRET_ATTACK1_SPEED_MAX_BONUS_REDGEM;
			}
			else if(attack == TURRET_ATTACK2) {
				turretCurrentTotalSpawnAmount += TURRET_ATTACK2_TOTALSPAWNAMOUNT_BONUS_REDGEM;
				turretCurrentTotalSpeedMin += TURRET_ATTACK2_SPEED_MIN_BONUS_REDGEM;
				turretCurrentTotalSpeedMax += TURRET_ATTACK2_SPEED_MAX_BONUS_REDGEM;
			}
			else if(attack == TURRET_ATTACK3) {
				turretCurrentTotalSpawnAmount += TURRET_ATTACK3_TOTALSPAWNAMOUNT_BONUS_REDGEM;
				turretCurrentTotalSpeedMin += TURRET_ATTACK3_SPEED_MIN_BONUS_REDGEM;
				turretCurrentTotalSpeedMax += TURRET_ATTACK3_SPEED_MAX_BONUS_REDGEM;
			}
		}
	}

	private void createRandomTopPortalObject() {
		int ballType = Support.randInt(1, spawnoddsBall7Max);
		
		if(spawnoddsBall1Max > 0 && ballType <= spawnoddsBall1Max) createBall1 = true;
		else if(ballType > spawnoddsBall1Max && ballType <= spawnoddsBall2Max) createBall2 = true;
		else if(ballType > spawnoddsBall2Max && ballType <= spawnoddsBall3Max) createBall3 = true;
		else if(ballType > spawnoddsBall3Max && ballType <= spawnoddsBall4Max) createBall4Split = true;
		else if(ballType > spawnoddsBall4Max && ballType <= spawnoddsBall5Max) createBall4Chase = true;
		else if(ballType > spawnoddsBall5Max && ballType <= spawnoddsBall6Max) createBall6 = true;
		else if(ballType > spawnoddsBall6Max && ballType <= spawnoddsBall7Max) createBall7 = true;
		else System.out.println("Error in Boss createRandomTopPortalObject, ballType (" + ballType + ") doesn't exist in any accepted roll interval.");
	}

	private void createRandomBottomPortalObject() {
		int enemyType = Support.randInt(1, Support.ENEMYTYPES);
		
		if(enemyType == Support.DARKMIST) {
			createEnemyDarkmist = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_DARKMIST * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.HALFMOON) {
			createEnemyHalfmoon = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_HALFMOON * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.MINE) {
			createEnemyMine = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_MINE * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.SENTRY) {
			createEnemySentry = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_SENTRY * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.SHIELD) {
			createEnemyShield = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_SHIELD * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.SPIKES) {
			createEnemySpikes = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_SPIKES * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.SQUARE) {
			createEnemySquare = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_SQUARE * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.STAR) {
			createEnemyStar = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_STAR * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else if(enemyType == Support.TRIANGLE) {
			createEnemyTriangle = true;
			bottomPortalLastEnemySpawnedExtra = (long) ((double) IDLE_BOTTOMPORTAL_EXTRA_TRIANGLE * Support.difficultyBossBottomPortalExtraCooldownMultiplier);
		}
		else System.out.println("Error in Boss createRandomBottomPortalObject, enemyType (" + enemyType + ") doesn't exist.");
	}
	
	private void breakShield() {
		if(shielded) {
			shielded = false;
			JukeBox.playWithRecommendedVolume("bossshieldbreak");
			shieldBreakTime = System.nanoTime();
		}
		updateAttackHitbox();
	}
	
	private void restoreShield() {
		if(!shielded) {
			shielded = true;
			JukeBox.playWithRecommendedVolume("bossshieldrestore");
		}
		shieldHealth = shieldMaxHealth;
		setShieldOpacity();
		updateAttackHitbox();
	}
	
	// call this whenever shield health value changes
	private void setShieldOpacity() {
		if(shieldHealth >= 3) currentShieldOpacity = Support.TRANSPARENT;
		else if(shieldHealth == 2) currentShieldOpacity = 0.33f;
		else if(shieldHealth == 1) currentShieldOpacity = 0.16f;
		else if(shieldHealth <= 0) currentShieldOpacity = Support.INVISIBLEOPACITY;
	}

	public boolean isInFightActivationRange(MapObject o) {
		Rectangle r1 = new Rectangle(
			(int)x - preFightDialogTriggerWidth / 2,
			(int)y - preFightDialogTriggerHeight / 2,
			preFightDialogTriggerWidth,
			preFightDialogTriggerHeight
		);
		Rectangle r2 = o.getRectangle();
		
		return r1.intersects(r2);
	}

	public void update() {
		// update position
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		// check shield restore
		if(!shielded && shieldRestoreTime != -1) {
			long elapsed = (System.nanoTime() - shieldBreakTime) / 1000000;
			
			if(elapsed > shieldRestoreTime) restoreShield();
		}
		
		// check flinching
		if(flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if(elapsed > flinchTime) {
				flinching = false;
			}
		}
		
		// update animations
		frameAnimation.update();
		gemAnimation.update();
		gemMiddleAnimation.update();
		portalTopAnimation.update();
		portalBottomAnimation.update();
		portalLeftAnimation.update();
		portalRightAnimation.update();
		
		updatePhase();
		updateCurrentAnimation();
	}
	
	public void draw(Graphics2D g) {
		setMapPosition();
		
		if(!drawBoss) return;
		
		if(facingRight) {
			if(drawFrame) {
				float frameDrawOpacity = currentFrameOpacity * Support.surroundingsOpacity;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, frameDrawOpacity));
				g.drawImage(
						frameAnimation.getImage(),
						(int)(x + xmap - frameWidth / 2),
						(int)(y + ymap - frameHeight / 2),
						frameWidth,
						frameHeight,
						null
					);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
			}
			if(flinching && !shielded) {
				long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
				if(elapsed / 100 % 2 == 0) {
					g.drawImage(
						gemAnimation.getImage(),
						(int)(x + xmap - gemWidth / 2),
						(int)(y + ymap - gemHeight / 2),
						gemWidth,
						gemHeight,
						null
					);
					g.drawImage(
						gemMiddleAnimation.getImage(),
						(int)(x + xmap - gemMiddleWidth / 2),
						(int)(y + ymap - gemMiddleHeight / 2),
						gemMiddleWidth,
						gemMiddleHeight,
						null
					);
				}
			}
			else {
				g.drawImage(
					gemAnimation.getImage(),
					(int)(x + xmap - gemWidth / 2),
					(int)(y + ymap - gemHeight / 2),
					gemWidth,
					gemHeight,
					null
				);
				g.drawImage(
					gemMiddleAnimation.getImage(),
					(int)(x + xmap - gemMiddleWidth / 2),
					(int)(y + ymap - gemMiddleHeight / 2),
					gemMiddleWidth,
					gemMiddleHeight,
					null
				);
			}
			if(portalTopActive) {
				g.drawImage(
					portalTopAnimation.getImage(),
					(int)(x + xmap + portalTopPosX - portalWidth / 2),
					(int)(y + ymap + portalTopPosY - portalHeight / 2),
					portalWidth,
					portalHeight,
					null
				);
			}
			if(portalBottomActive) {
				g.drawImage(
					portalBottomAnimation.getImage(),
					(int)(x + xmap + portalBottomPosX - portalWidth / 2),
					(int)(y + ymap + portalBottomPosY - portalHeight / 2),
					portalWidth,
					portalHeight,
					null
				);

			}
			if(portalLeftActive) {
				g.drawImage(
					portalLeftAnimation.getImage(),
					(int)(x + xmap + portalLeftPosX - portalWidth / 2),
					(int)(y + ymap + portalLeftPosY - portalHeight / 2),
					portalWidth,
					portalHeight,
					null
				);
			}
			if(portalRightActive) {
				g.drawImage(
					portalRightAnimation.getImage(),
					(int)(x + xmap + portalRightPosX - portalWidth / 2),
					(int)(y + ymap + portalRightPosY - portalHeight / 2),
					portalWidth,
					portalHeight,
					null
				);
			}
			if(shielded) {
				float shieldDrawOpacity = currentShieldOpacity * Support.surroundingsOpacity;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shieldDrawOpacity));
				if(flinching) {
					long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
					if(elapsed / 100 % 2 == 0) {
						g.drawImage(
							shieldAnimation.getImage(),
							(int)(x + xmap - shieldWidth / 2),
							(int)(y + ymap - shieldHeight / 2),
							shieldWidth,
							shieldHeight,
							null
						);
					}
				}
				else {
					g.drawImage(
						shieldAnimation.getImage(),
						(int)(x + xmap - shieldWidth / 2),
						(int)(y + ymap - shieldHeight / 2),
						shieldWidth,
						shieldHeight,
						null
					);
				}
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
			}
		}
		else {
			if(drawFrame) {
				float frameDrawOpacity = currentFrameOpacity * Support.surroundingsOpacity;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, frameDrawOpacity));
				g.drawImage(
					frameAnimation.getImage(),
					(int)(x + xmap - frameWidth / 2 + frameWidth),
					(int)(y + ymap - frameHeight / 2),
					-frameWidth,
					frameHeight,
					null
				);
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
			}
			g.drawImage(
				gemAnimation.getImage(),
				(int)(x + xmap - gemWidth / 2 + gemWidth),
				(int)(y + ymap - gemHeight / 2),
				-gemWidth,
				gemHeight,
				null
			);
			g.drawImage(
				gemMiddleAnimation.getImage(),
				(int)(x + xmap - gemMiddleWidth / 2 + gemMiddleWidth),
				(int)(y + ymap - gemMiddleHeight / 2),
				-gemMiddleWidth,
				gemMiddleHeight,
				null
			);
			if(portalTopActive) {
				g.drawImage(
					portalTopAnimation.getImage(),
					(int)(x + xmap + portalTopPosX - portalWidth / 2 + portalWidth),
					(int)(y + ymap + portalTopPosY - portalHeight / 2),
					-portalWidth,
					portalHeight,
					null
				);
			}
			if(portalBottomActive) {
				g.drawImage(
					portalBottomAnimation.getImage(),
					(int)(x + xmap + portalBottomPosX - portalWidth / 2 + portalWidth),
					(int)(y + ymap + portalBottomPosY - portalHeight / 2),
					-portalWidth,
					portalHeight,
					null
				);

			}
			if(portalLeftActive) {
				g.drawImage(
					portalLeftAnimation.getImage(),
					(int)(x + xmap + portalLeftPosX - portalWidth / 2 + portalWidth),
					(int)(y + ymap + portalLeftPosY - portalHeight / 2),
					-portalWidth,
					portalHeight,
					null
				);
			}
			if(portalRightActive) {
				g.drawImage(
					portalRightAnimation.getImage(),
					(int)(x + xmap + portalRightPosX - portalWidth / 2 + portalWidth),
					(int)(y + ymap + portalRightPosY - portalHeight / 2),
					-portalWidth,
					portalHeight,
					null
				);
			}
			if(shielded) {
				float shieldDrawOpacity = currentShieldOpacity * Support.surroundingsOpacity;
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, shieldDrawOpacity));
				if(flinching) {
					long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
					if(elapsed / 100 % 2 == 0) {
						g.drawImage(
							shieldAnimation.getImage(),
							(int)(x + xmap - shieldWidth / 2 + shieldWidth),
							(int)(y + ymap - shieldHeight / 2),
							-shieldWidth,
							shieldHeight,
							null
						);
					}
				}
				else {
					g.drawImage(
						shieldAnimation.getImage(),
						(int)(x + xmap - shieldWidth / 2 + shieldWidth),
						(int)(y + ymap - shieldHeight / 2),
						-shieldWidth,
						shieldHeight,
						null
					);
				}
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
			}
		}
	}

	private EntitySpawnData getFinalAttackEntitySpawnData(int attackNumber) {
		EntitySpawnData esd = null;
		
		// gem ring
		if(attackNumber == 1) {
			ArrayList<BossFinalAttackListExtra> finalAttackExtraList = new ArrayList<BossFinalAttackListExtra>();
			for(int i = 0; i < 24; i++) {
				BossFinalAttackListExtra extra = new BossFinalAttackListExtra(
					finalAttackGemColor,
					finalAttackIdleTime,
					finalAttackSpeed,
					finalAttackRadius
				);
				finalAttackExtraList.add(extra);
			}
			BossFinalAttackSpawnerExtra ed = new BossFinalAttackSpawnerExtra(
				finalAttackExtraList,
				finalAttackStartDegreeMode,
				finalAttackSpawnInterval
			);
			esd = new EntitySpawnData(
				new BossFinalAttackSpawner(tileMap, attackNumber, ed),
				getx() + finalAttackPosX,
				gety() + finalAttackPosY
			);
		}
		// continuous gem spiral
		else if(attackNumber == 2) {
			ArrayList<BossFinalAttackListExtra> finalAttackExtraList = new ArrayList<BossFinalAttackListExtra>();
			for(int i = 0; i < finalAttackGemAmount; i++) {
				BossFinalAttackListExtra extra = new BossFinalAttackListExtra(
					finalAttackGemColor,
					finalAttackIdleTime,
					finalAttackSpeed,
					finalAttackRadius
				);
				finalAttackExtraList.add(extra);
			}
			BossFinalAttackSpawnerExtra ed = new BossFinalAttackSpawnerExtra(
				finalAttackExtraList,
				finalAttackStartDegreeMode,
				finalAttackSpawnInterval
			);
			esd = new EntitySpawnData(
				new BossFinalAttackSpawner(tileMap, attackNumber, ed),
				getx() + finalAttackPosX,
				gety() + finalAttackPosY
			);
		}
		// random spawns all around with increasing speed
		else if(attackNumber == 3) {
			ArrayList<BossFinalAttackListExtra> finalAttackExtraList = new ArrayList<BossFinalAttackListExtra>();
			for(int i = 0; i < finalAttackGemAmount; i++) {
				BossFinalAttackListExtra extra = new BossFinalAttackListExtra(
					finalAttackGemColor,
					finalAttackIdleTime,
					finalAttackSpeed,
					finalAttackRadius
				);
				finalAttackExtraList.add(extra);
			}
			BossFinalAttackSpawnerExtra ed = new BossFinalAttackSpawnerExtra(
				finalAttackExtraList,
				finalAttackStartDegreeMode,
				finalAttackSpawnInterval
			);
			esd = new EntitySpawnData(
				new BossFinalAttackSpawner(tileMap, attackNumber, ed),
				getx() + finalAttackPosX,
				gety() + finalAttackPosY
			);
		}
		// continuous gem spiral with varying radius
		else if(attackNumber == 4) {
			// initial values
			// last lap radius
			double lastRadius = finalAttackRadius;
			// radius to be used for this lap
			double currentRadius = finalAttackRadius;
			// extra to the radius that increases over the laps with finalAttackRadiusDifferenceIncrase until it resets when direction is swapped
			double radiusIncreaseExtra = 0.0;
			// total amount of additional radius for a run of the loop
			double additionalRadiusTotal = 0.0;
			// is the chain going outward or inward
			boolean outwardDirection = outwardStartDirection;
			// lap number going in a certain direction, resets to 2 since the last gem for one direction also makes up the first one when going in reverse
			int lap = 1;
			
			ArrayList<BossFinalAttackListExtra> finalAttackExtraList = new ArrayList<BossFinalAttackListExtra>();
			for(int i = 0; i < finalAttackGemAmount; i++) {
				// total additional radius is the base value + all accumulated extra increase
				additionalRadiusTotal = finalAttackRadiusDifferenceInitial + radiusIncreaseExtra;
				// + or - is used depending on if direction is outward or inward
				if(outwardDirection) currentRadius = lastRadius + additionalRadiusTotal;
				else currentRadius = lastRadius - additionalRadiusTotal;
				
				// normal creation of extra data for the attacking gem
				BossFinalAttackListExtra extra = new BossFinalAttackListExtra(
					finalAttackGemColor,
					finalAttackIdleTime,
					finalAttackSpeed,
					currentRadius
				);
				finalAttackExtraList.add(extra);
				
				// preparations for next lap
				if(lap >= finalAttackRadiusAmountOfChanges) {
					outwardDirection = !outwardDirection;
					lap = 2;
					radiusIncreaseExtra = finalAttackRadiusDifferenceIncrease;
				}
				else {
					lap++;
					radiusIncreaseExtra += finalAttackRadiusDifferenceIncrease;
				}
				lastRadius = currentRadius;
			}
			BossFinalAttackSpawnerExtra ed = new BossFinalAttackSpawnerExtra(
				finalAttackExtraList,
				finalAttackStartDegreeMode,
				finalAttackSpawnInterval
			);
			esd = new EntitySpawnData(
				new BossFinalAttackSpawner(tileMap, 2, ed),
				getx() + finalAttackPosX,
				gety() + finalAttackPosY
			);
		}
		
		return esd;
	}
	
	// change this implementation if needed for anything other than hitbox detection for attacks
	@Override
	public Rectangle getRectangle() {
		return new Rectangle(
			(int)x - attackCWidth / 2,
			(int)y - attackCHeight / 2,
			attackCWidth,
			attackCHeight
		);
	}
	
	@Override
	public void hit(int damage) {
		if(dead || flinching) return;
		if(shielded) {
			shieldHealth -= damage;
			if(shieldHealth < 0) shieldHealth = 0;
			if(shieldHealth == 0) breakShield();
			flinching = true;
			flinchTimer = System.nanoTime();
			setShieldOpacity();
		}
		else {
			health -= damage;
			if(health < 0) health = 0;
			flinching = true;
			flinchTimer = System.nanoTime();
			if(health == 0 && killable) currentAnimation = KILLED_WAITTRIGGER_1;
		}
	}

	@Override
	public void setPlayerInformation(double playerX, double playerY) {
		this.playerX = playerX;
		this.playerY = playerY;
	}
	
	@Override
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		ArrayList<EntitySpawnData> esdList = new ArrayList<EntitySpawnData>();
		
		if(dead) {
			EntitySpawnData esd = new EntitySpawnData(
				new Explosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
		}
		
		if(createTurretWarning) {
			EntitySpawnData esd;
			
			esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.TURRET, null),
				getx() + turretNWSpawnPosX,
				gety() + turretNWSpawnPosY
			);
			esdList.add(esd);
			
			esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.TURRET, null),
				getx() + turretNESpawnPosX,
				gety() + turretNESpawnPosY
			);
			esdList.add(esd);
			
			esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.TURRET, null),
				getx() + turretSWSpawnPosX,
				gety() + turretSWSpawnPosY
			);
			esdList.add(esd);
			
			esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.TURRET, null),
				getx() + turretSESpawnPosX,
				gety() + turretSESpawnPosY
			);
			esdList.add(esd);
			
			createTurretWarning = false;
		}
		
		if(createTopPortalWarning) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.PORTAL1, null),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			createTopPortalWarning = false;
		}
		
		if(createBottomPortalWarning) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.PORTAL3, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createBottomPortalWarning = false;
		}
		
		if(createLeftRightPortalWarning) {
			EntitySpawnData esd;
			
			esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.PORTAL2, null),
				getx() + portalLeftPosX,
				gety() + portalLeftPosY
			);
			esdList.add(esd);

			esd = new EntitySpawnData(
				new BossPortalSpawnEffect(tileMap, BossPortalSpawnEffect.PORTAL2, null),
				getx() + portalRightPosX,
				gety() + portalRightPosY
			);
			esdList.add(esd);
			
			createLeftRightPortalWarning = false;
		}
		
		// long repeater line with a slight initial degree variation
		if(createTurretFire1) {
			EntitySpawnData esd = null;
			BossTurretSpawnerExtra ed = null;
			ArrayList<BossTurretListExtra> btle = null;
			int turretTotalSpeedMin = (int) (turretCurrentTotalSpeedMin * 100);
			int turretTotalSpeedMax = (int) (turretCurrentTotalSpeedMax * 100);
			double turretTotalSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(turretTotalSpeedMin, turretTotalSpeedMax), 2);
			
			
			// goes clockwise, starting from 1.5 position
			// turret 1
			//------------------------------------------------------------------------------------------------------------------------
			int turret1DegreesMin = (int) (turret1CurrentDegreesMin * 100);
			int turret1DegreesMax = (int) (turret1CurrentDegreesMax * 100);
			double turret1Degrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret1DegreesMin, turret1DegreesMax), 2);
			btle = new ArrayList<BossTurretListExtra>();
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turret1Degrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretNESpawnPosX,
				gety() + turretNESpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 2
			//------------------------------------------------------------------------------------------------------------------------
			int turret2DegreesMin = (int) (turret2CurrentDegreesMin * 100);
			int turret2DegreesMax = (int) (turret2CurrentDegreesMax * 100);
			double turret2Degrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret2DegreesMin, turret2DegreesMax), 2);
			btle = new ArrayList<BossTurretListExtra>();
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turret2Degrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretSESpawnPosX,
				gety() + turretSESpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 3
			//------------------------------------------------------------------------------------------------------------------------
			int turret3DegreesMin = (int) (turret3CurrentDegreesMin * 100);
			int turret3DegreesMax = (int) (turret3CurrentDegreesMax * 100);
			double turret3Degrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret3DegreesMin, turret3DegreesMax), 2);
			btle = new ArrayList<BossTurretListExtra>();
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turret3Degrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretSWSpawnPosX,
				gety() + turretSWSpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 4
			//------------------------------------------------------------------------------------------------------------------------
			int turret4DegreesMin = (int) (turret4CurrentDegreesMin * 100);
			int turret4DegreesMax = (int) (turret4CurrentDegreesMax * 100);
			double turret4Degrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret4DegreesMin, turret4DegreesMax), 2);
			btle = new ArrayList<BossTurretListExtra>();
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turret4Degrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretNWSpawnPosX,
				gety() + turretNWSpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------
			
			createTurretFire1 = false;
		}
		
		// repeater with spread going clockwise
		if(createTurretFire2) {
			EntitySpawnData esd = null;
			BossTurretSpawnerExtra ed = null;
			ArrayList<BossTurretListExtra> btle = null;
			int turretTotalSpeedMin = (int) (turretCurrentTotalSpeedMin * 100);
			int turretTotalSpeedMax = (int) (turretCurrentTotalSpeedMax * 100);
			double turretTotalSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(turretTotalSpeedMin, turretTotalSpeedMax), 2);
			double degreeDifference;
			if(turret1CurrentDegreesMax < turret1CurrentDegreesMin) degreeDifference = (turret1CurrentDegreesMax + 360.0) - turret1CurrentDegreesMin;
			else degreeDifference = turret1CurrentDegreesMax - turret1CurrentDegreesMin;
			double degreeJump = degreeDifference / (double) (turretCurrentTotalSpawnAmount - 1.0);
			double turretDegrees;
			
			// goes clockwise, starting from 1.5 position
			// turret 1
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			turretCurrentInitialIdleTime = TURRET1_ATTACK2_INITIALIDLE;
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretDegrees = turret1CurrentDegreesMin + degreeJump * (double) i;
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretNESpawnPosX,
				gety() + turretNESpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 2
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			turretCurrentInitialIdleTime = TURRET2_ATTACK2_INITIALIDLE;
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretDegrees = turret2CurrentDegreesMin + degreeJump * (double) i;
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretSESpawnPosX,
				gety() + turretSESpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 3
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			turretCurrentInitialIdleTime = TURRET3_ATTACK2_INITIALIDLE;
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretDegrees = turret3CurrentDegreesMin + degreeJump * (double) i;
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretSWSpawnPosX,
				gety() + turretSWSpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 4
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			turretCurrentInitialIdleTime = TURRET4_ATTACK2_INITIALIDLE;
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretDegrees = turret4CurrentDegreesMin + degreeJump * (double) i;
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretNWSpawnPosX,
				gety() + turretNWSpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------
			
			createTurretFire2 = false;
		}
		
		// shotgun
		if(createTurretFire3) {
			EntitySpawnData esd = null;
			BossTurretSpawnerExtra ed = null;
			ArrayList<BossTurretListExtra> btle = null;
			int turretTotalSpeedMin = (int) (turretCurrentTotalSpeedMin * 100);
			int turretTotalSpeedMax = (int) (turretCurrentTotalSpeedMax * 100);
			double turretTotalSpeed;
			double turretDegrees;
			
			// goes clockwise, starting from 1.5 position
			// turret 1
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			int turret1DegreesMin = (int) (turret1CurrentDegreesMin * 100);
			int turret1DegreesMax = (int) (turret1CurrentDegreesMax * 100);
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretTotalSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(turretTotalSpeedMin, turretTotalSpeedMax), 2);
				turretDegrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret1DegreesMin, turret1DegreesMax), 2);
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretNESpawnPosX,
				gety() + turretNESpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 2
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			int turret2DegreesMin = (int) (turret2CurrentDegreesMin * 100);
			int turret2DegreesMax = (int) (turret2CurrentDegreesMax * 100);
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretTotalSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(turretTotalSpeedMin, turretTotalSpeedMax), 2);
				turretDegrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret2DegreesMin, turret2DegreesMax), 2);
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretSESpawnPosX,
				gety() + turretSESpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 3
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			int turret3DegreesMin = (int) (turret3CurrentDegreesMin * 100);
			int turret3DegreesMax = (int) (turret3CurrentDegreesMax * 100);
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretTotalSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(turretTotalSpeedMin, turretTotalSpeedMax), 2);
				turretDegrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret3DegreesMin, turret3DegreesMax), 2);
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretSWSpawnPosX,
				gety() + turretSWSpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------

			// turret 4
			//------------------------------------------------------------------------------------------------------------------------
			btle = new ArrayList<BossTurretListExtra>();
			int turret4DegreesMin = (int) (turret4CurrentDegreesMin * 100);
			int turret4DegreesMax = (int) (turret4CurrentDegreesMax * 100);
			for(int i = 0; i < turretCurrentTotalSpawnAmount; i++) {
				turretTotalSpeed = Support.getDoubleWithXExtraDecimals(Support.randInt(turretTotalSpeedMin, turretTotalSpeedMax), 2);
				turretDegrees = Support.getDoubleWithXExtraDecimals(Support.randInt(turret4DegreesMin, turret4DegreesMax), 2);
				btle.add(new BossTurretListExtra(
					turretCurrentColor,
					turretTotalSpeed,
					turretDegrees
				));
			}
			ed = new BossTurretSpawnerExtra(
				btle,
				turretCurrentSpawnInterval,
				turretCurrentSpawnBurstAmount,
				turretCurrentInitialIdleTime
			);
			esd = new EntitySpawnData(
				new BossTurretSpawner(tileMap, 1, ed),
				getx() + turretNWSpawnPosX,
				gety() + turretNWSpawnPosY
			);
			esdList.add(esd);
			//------------------------------------------------------------------------------------------------------------------------
			
			createTurretFire3 = false;
		}
		
		if(createBall1) {
			dxBall1 = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 200) + 100.0, 2);
			if(Support.randInt(0, 1) == 0) dxBall1 *= -1;
			dyBall1 = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 200) + 100.0, 2);
			if(Support.randInt(0, 1) == 0) dyBall1 *= -1;
			BossBall1Extra ed = new BossBall1Extra(dxBall1, dyBall1);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall1(tileMap, 0, ed),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			createBall1 = false;
		}
		
		if(createBall2) {
			/*dxBall1 = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 200) + 100.0, 2);
			if(Support.randInt(0, 1) == 0) dxBall1 *= -1;
			dyBall1 = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 200) + 100.0, 2);
			if(Support.randInt(0, 1) == 0) dyBall1 *= -1;
			BossBall1Extra ed = new BossBall1Extra(dxBall1, dyBall1);*/
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall2(tileMap, 0, null),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			createBall2 = false;
		}
		
		/*if(createBall3) {
			EntitySpawnData esd = new EntitySpawnData(
					new Square(tileMap, 0, null),
					getx(),
					gety()
				);
				esdList.add(esd);
			
			createBall3 = false;
		}*/
		
		if(createBall3) {
			double spawnBaseX = 0.07;
			double multiplier = 1.8;
			double pow = 0;
			for(int amount = 6; amount > 0; amount--) {
				BossBall3Extra ed = new BossBall3Extra(
					spawnBaseX * Math.pow(multiplier, pow),
					-9
				);
				EntitySpawnData esd = new EntitySpawnData(
					new BossBall3(tileMap, 0, ed),
					getx() + portalTopPosX,
					gety() + portalTopPosY
				);
				esdList.add(esd);
				
				ed = new BossBall3Extra(
					(spawnBaseX * Math.pow(multiplier, pow)) * (-1),
					-9
				);
				esd = new EntitySpawnData(
					new BossBall3(tileMap, 0, ed),
					getx() + portalTopPosX,
					gety() + portalTopPosY
				);
				esdList.add(esd);
				
				pow++;
			}
			
			createBall3 = false;
		}
		
		if(createBall4Chase) {
			dxBall4Chase = 0;
			dyBall4Chase = -3;
			BossBall4ChaseExtra ed = new BossBall4ChaseExtra(dxBall4Chase, dyBall4Chase);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall4Chase(tileMap, 0, ed),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			createBall4Chase = false;
		}
		
		if(createBall4Split) {
			dxBall4Split = 0;
			dyBall4Split = -3;
			BossBall4SplitExtra ed = new BossBall4SplitExtra(dxBall4Split, dyBall4Split, 8, null);
			EntitySpawnData esd = new EntitySpawnData(
				new BossBall4Split(tileMap, 0, ed),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			createBall4Split = false;
		}
		
		// ring of red gems
		if(createFinalAttack1) {
			esdList.add(getFinalAttackEntitySpawnData(1));
			createFinalAttack1 = false;
		}
		
		// continuous spiral of gems
		if(createFinalAttack2) {
			esdList.add(getFinalAttackEntitySpawnData(2));
			createFinalAttack2 = false;
		}
		
		// random spawns all around with increasing speed
		if(createFinalAttack3) {
			esdList.add(getFinalAttackEntitySpawnData(3));
			createFinalAttack3 = false;
		}
		
		// continuous spiral of gems with varying radius
		if(createFinalAttack4) {
			esdList.add(getFinalAttackEntitySpawnData(4));
			createFinalAttack4 = false;
		}
		
		if(createBall6) {
			BossBall6Extra ed = null;
			EntitySpawnData esd;
			ed = new BossBall6Extra(
				Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 2),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 2),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-9965, -9895), 4)
			);
			esd = new EntitySpawnData(
				new BossBall6(tileMap, 0, ed),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			ed = new BossBall6Extra(
				Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 2),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 2),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-9965, -9895), 4)
			);
			esd = new EntitySpawnData(
				new BossBall6(tileMap, 0, ed),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			ed = new BossBall6Extra(
				Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 2),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-600, 600), 2),
				Support.getDoubleWithXExtraDecimals(Support.randInt(-9965, -9895), 4)
			);
			esd = new EntitySpawnData(
				new BossBall6(tileMap, 0, ed),
				getx() + portalTopPosX,
				gety() + portalTopPosY
			);
			esdList.add(esd);
			
			createBall6 = false;
		}
		
		// continuous spiral of gems with varying radius
		if(createBall7) {
			BossBall7Extra ed = null;
			EntitySpawnData esd;
			ed = new BossBall7Extra(
				(double) (getx() + portalTopPosX),
				(double) (gety() + portalTopPosY),
				playerX + ball7TopPosX,
				playerY + ball7TopPosY,
				BossBall7.PATH_TRAVELTIME_DEFAULT
			);
			esd = new EntitySpawnData(
				new BossBall7(tileMap, BossBall7.MODE_INITIAL, ed),
				getx() + portalTopPosX + Support.randInt(-15, 15),
				gety() + portalTopPosY + Support.randInt(-15, 15)
			);
			esdList.add(esd);

			ed = new BossBall7Extra(
				(double) (getx() + portalTopPosX),
				(double) (gety() + portalTopPosY),
				playerX + ball7BottomPosX,
				playerY + ball7BottomPosY,
				BossBall7.PATH_TRAVELTIME_DEFAULT
			);
			esd = new EntitySpawnData(
				new BossBall7(tileMap, BossBall7.MODE_INITIAL, ed),
				getx() + portalTopPosX + Support.randInt(-15, 15),
				gety() + portalTopPosY + Support.randInt(-15, 15)
			);
			esdList.add(esd);

			ed = new BossBall7Extra(
				(double) (getx() + portalTopPosX),
				(double) (gety() + portalTopPosY),
				playerX + ball7LeftPosX,
				playerY + ball7LeftPosY,
				BossBall7.PATH_TRAVELTIME_DEFAULT
			);
			esd = new EntitySpawnData(
				new BossBall7(tileMap, BossBall7.MODE_INITIAL, ed),
				getx() + portalTopPosX + Support.randInt(-15, 15),
				gety() + portalTopPosY + Support.randInt(-15, 15)
			);
			esdList.add(esd);

			ed = new BossBall7Extra(
				(double) (getx() + portalTopPosX),
				(double) (gety() + portalTopPosY),
				playerX + ball7RightPosX,
				playerY + ball7RightPosY,
				BossBall7.PATH_TRAVELTIME_DEFAULT
			);
			esd = new EntitySpawnData(
				new BossBall7(tileMap, BossBall7.MODE_INITIAL, ed),
				getx() + portalTopPosX + Support.randInt(-15, 15),
				gety() + portalTopPosY + Support.randInt(-15, 15)
			);
			esdList.add(esd);
			
			createBall7 = false;
		}
		
		if(createShield) {
			//BossShieldExtra ed = new BossShieldExtra(dialog);
			BossShield bs = new BossShield(tileMap, 0, null);
			finalAttackShieldId = bs.getId();
			EntitySpawnData esd = new EntitySpawnData(
				bs,
				getx() + finalAttackPosX,
				gety() + finalAttackPosY
			);
			esdList.add(esd);
			
			createShield = false;
		}
		
		if(createWall) {
			EntitySpawnData esd;
			
			esd = new EntitySpawnData(
				new BossWall(tileMap, BossWall.MODE_LEFT, null),
				getx() + portalLeftPosX,
				gety() + portalLeftPosY + currentWallSpawnPosY
			);
			esdList.add(esd);

			esd = new EntitySpawnData(
				new BossWall(tileMap, BossWall.MODE_RIGHT, null),
				getx() + portalRightPosX,
				gety() + portalRightPosY + currentWallSpawnPosY
			);
			esdList.add(esd);
			
			createWall = false;
		}
		
		if(createEnemyDarkmist) {
			EntitySpawnData esd = new EntitySpawnData(
				new Darkmist(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemyDarkmist = false;
		}
		
		if(createEnemyHalfmoon) {
			EntitySpawnData esd = new EntitySpawnData(
				new Halfmoon(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemyHalfmoon = false;
		}
		
		if(createEnemyMine) {
			EntitySpawnData esd = new EntitySpawnData(
				new Mine(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemyMine = false;
		}
		
		if(createEnemySentry) {
			EntitySpawnData esd = new EntitySpawnData(
				new Sentry(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemySentry = false;
		}
		
		if(createEnemyShield) {
			EntitySpawnData esd = new EntitySpawnData(
				new Shield(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemyShield = false;
		}
		
		if(createEnemySpikes) {
			EntitySpawnData esd = new EntitySpawnData(
				new Spikes(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemySpikes = false;
		}
		
		if(createEnemySquare) {
			EntitySpawnData esd = new EntitySpawnData(
				new Square(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemySquare = false;
		}
		
		if(createEnemyStar) {
			EntitySpawnData esd = new EntitySpawnData(
				new Star(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemyStar = false;
		}
		
		if(createEnemyTriangle) {
			EntitySpawnData esd = new EntitySpawnData(
				new Triangle(tileMap, 0, null),
				getx() + portalBottomPosX,
				gety() + portalBottomPosY
			);
			esdList.add(esd);
			
			createEnemyTriangle = false;
		}
		
		if(createEscapePortal) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossEscapePortal(tileMap, 0, null),
				getx() + bossEscapePortalX,
				gety() + bossEscapePortalY
			);
			esdList.add(esd);
			
			createEscapePortal = false;
		}
		
		if(createBossGemPieces) {
			EntitySpawnData esd;
			double dxEd = 0;
			double dyEd = 0;
			for(int pieceNumber = 1; pieceNumber <= 18; pieceNumber++) {
				dxEd = Support.getDoubleWithXExtraDecimals(Support.randInt(-500, 500), 2);
				dyEd = Support.getDoubleWithXExtraDecimals(Support.randInt(-700, 400), 2);
				BossGemPieceExtra ed = new BossGemPieceExtra(dxEd, dyEd);
				esd = new EntitySpawnData(
					new BossGemPiece(tileMap, pieceNumber, ed),
					getx(),
					gety()
				);
				esdList.add(esd);
			}
			
			createBossGemPieces = false;
		}
		
		if(createBossExplosion) {
			EntitySpawnData esd = new EntitySpawnData(
				new BossExplosion(tileMap, 0, null),
				getx(),
				gety()
			);
			esdList.add(esd);
			
			createBossExplosion = false;
		}
		
		return esdList;
	}

	@Override
	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			frameSprites = frameSpritesC;
			gemGreenSprites = gemGreenSpritesC;
			gemYellowSprites = gemYellowSpritesC;
			gemRedSprites = gemRedSpritesC;
			gemPaleSprites = gemPaleSpritesC;
			gemGreenMiddleSprites = gemGreenMiddleSpritesC;
			gemYellowMiddleSprites = gemYellowMiddleSpritesC;
			gemRedMiddleSprites = gemRedMiddleSpritesC;
			gemPaleMiddleSprites = gemPaleMiddleSpritesC;
			shieldSprites = shieldSpritesC;
			portalTopSprites = portalTopSpritesC;
			portalBottomSprites = portalBottomSpritesC;
			portalLeftSprites = portalLeftSpritesC;
			portalRightSprites = portalRightSpritesC;
			invisibleSprites = invisibleSpritesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			frameSprites = frameSpritesM;
			gemGreenSprites = gemGreenSpritesM;
			gemYellowSprites = gemYellowSpritesM;
			gemRedSprites = gemRedSpritesM;
			gemPaleSprites = gemPaleSpritesM;
			gemGreenMiddleSprites = gemGreenMiddleSpritesM;
			gemYellowMiddleSprites = gemYellowMiddleSpritesM;
			gemRedMiddleSprites = gemRedMiddleSpritesM;
			gemPaleMiddleSprites = gemPaleMiddleSpritesM;
			shieldSprites = shieldSpritesM;
			portalTopSprites = portalTopSpritesM;
			portalBottomSprites = portalBottomSpritesM;
			portalLeftSprites = portalLeftSpritesM;
			portalRightSprites = portalRightSpritesM;
			invisibleSprites = invisibleSpritesM;
		}
	}
	
	@Override
	public void swapAnimationFrames() {
		frameAnimation.swapFrames(frameSprites);
		
		if(currentGemMode == GEMMODE_GREEN) {
			gemAnimation.swapFrames(gemGreenSprites);
			gemMiddleAnimation.swapFrames(gemGreenMiddleSprites);
			gemMiddleAnimation.setDelay(gemMiddleAnimationTimeGreen);
		}
		else if(currentGemMode == GEMMODE_YELLOW) {
			gemAnimation.swapFrames(gemYellowSprites);
			gemMiddleAnimation.swapFrames(gemYellowMiddleSprites);
			gemMiddleAnimation.setDelay(gemMiddleAnimationTimeYellow);
		}
		else if(currentGemMode == GEMMODE_RED) {
			gemAnimation.swapFrames(gemRedSprites);
			gemMiddleAnimation.swapFrames(gemRedMiddleSprites);
			gemMiddleAnimation.setDelay(gemMiddleAnimationTimeRed);
		}
		else if(currentGemMode == GEMMODE_PALE) {
			gemAnimation.swapFrames(gemPaleSprites);
			gemMiddleAnimation.swapFrames(gemPaleMiddleSprites);
			gemMiddleAnimation.setDelay(gemMiddleAnimationTimePale);
		}
		else System.out.println("Error in Boss swapAnimationFrames(), currentGemMode has bad value of " + currentGemMode);
		
		portalTopAnimation.swapFrames(portalTopSprites);
		portalBottomAnimation.swapFrames(portalBottomSprites);
		portalLeftAnimation.swapFrames(portalLeftSprites);
		portalRightAnimation.swapFrames(portalRightSprites);
	}
}









