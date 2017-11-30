package GameState;

import java.awt.*;
import java.util.ArrayList;

import Audio.JukeBox;
import Entity.BossShield;
import Entity.Enemy;
import Entity.Enemies.Boss;
import Handlers.DialogConversation;
import Main.GamePanel;
import Support.Support;

public class Level7State extends GameState {

	private int bossId;
	private int bossShieldId;
	private boolean bossSpawned;
	private boolean bossPreFightDialogStarted;
	private boolean bossfightStarted;
	private boolean bossfightFinalAttackPreparationsDone;
	private boolean bossFinalAttackDestructionOngoing;

	private float endScreenOpacity;
	
	private long endScreenFadeInTime;
	private long endScreenFadeInStart;
	private boolean endScreenFadeInHasStarted;

	public Level7State(GameStateManager gsm) {
		super(gsm);
		
		endScreenOpacity = Support.INVISIBLEOPACITY;
		
		endScreenFadeInTime = 3000;
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 7;
		levelName = "The confrontation";
		levelBriefing = "Looks like you're finally approaching the end of that sad little labyrinth. The signal has also grown very strong by now, which " +
			"means your target is likely hiding somewhere nearby. I say it's high time to throw the games aside and release some of that pent-up " +
			"frustration back at its father. Proceed with extreme caution while I go grab some snacks.";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level7.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
<<<<<<< HEAD
		playerStartX = GameStateManager.LEVEL7PLAYERX;
		playerStartY = GameStateManager.LEVEL7PLAYERY;
=======
		if(Support.cheatBossRush) {
			playerStartX = GameStateManager.LEVEL7PLAYERXBOSSRUSH;
			playerStartY = GameStateManager.LEVEL7PLAYERYBOSSRUSH;
		}
		else {
			playerStartX = GameStateManager.LEVEL7PLAYERX;
			playerStartY = GameStateManager.LEVEL7PLAYERY;
		}
>>>>>>> 2.03
		
		musicTrack = "level7";
		musicDampen = -15;
		musicStartFrame = 0;
		
		CAPACITY = 50;
		
		CAPACITY_INTERVAL = (int) (6500 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 12000;
		ODDS_HALFMOON = 13000;
		ODDS_MINE = 12000;
		ODDS_SENTRY = 9000;
		ODDS_SHIELD = 10000;
		ODDS_SPIKES = 11000;
		ODDS_SQUARE = 9000;
		ODDS_STAR = 11000;
		ODDS_TRIANGLE = 13000;
		
		INITIAL_DARKMIST = 1;
		INITIAL_HALFMOON = 1;
		INITIAL_MINE = 2;
		INITIAL_SENTRY = 1;
		INITIAL_SHIELD = 1;
		INITIAL_SPIKES = 2;
		INITIAL_SQUARE = 1;
		INITIAL_STAR = 1;
		INITIAL_TRIANGLE = 2;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL7LIFEX, GameStateManager.LEVEL7LIFEY)
			};
		}
		
		portalPoints = new Point[] {
			
		};
		
		portalContactGameState = GameStateManager.MENUSTATE;
		portalContactTransferPlayer = false;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL7STATE;
		respawnTransferPlayer = true;
		
		bossSpawned = false;
	}

	private void checkForBossSpawn() {
		if(bossSpawned) return;
		
		if(
			(player.getx() > 80 && player.getx() < 160) &&
			(player.gety() > 220 && player.gety() < 230)
		) {
			prepareBossFight();
			spawnBoss();
		}
	}
	
	// checks for pre fight dialog start and fight start
	private void checkForBossFightStart() {
		if(bossfightStarted || !bossSpawned) return;
		
		for(Enemy e : enemies) {
			if(e.getId() == bossId) {
				Boss b = (Boss) e;
				if(b.isInFightActivationRange(player) && !bossPreFightDialogStarted) startBossPreFightDialog(); 
				if(b.hasFightStarted()) startBossFight();
			}
		}
	}

	private void checkForBossFightFinalAttackPreparation() {
		if(!bossSpawned || !bossfightStarted || bossfightFinalAttackPreparationsDone) return;
		
		if(getBossById().shouldPrepareForFinalAttack()) prepareBossFinalAttack();
	}
	
	private void checkForBossFightFinalAttackDestruction() {
		if(!bossSpawned || !bossfightStarted || !bossfightFinalAttackPreparationsDone || bossFinalAttackDestructionOngoing) return;
		
		if(getBossById().shouldDestructFinalAttack()) {
			getBossShieldById().setDestructing(true);
			bossFinalAttackDestructionOngoing = true;
		}
	}

	private Boss getBossById() {
		for(Enemy e : enemies) {
			if(e.getId() == bossId) {
				return (Boss) e;
			}
		}
		
		return null;
	}

	private BossShield getBossShieldById() {
		for(Enemy e : enemies) {
			if(e.getId() == bossShieldId) {
				return (BossShield) e;
			}
		}
		
		return null;
	}

	private void prepareBossFight() {
		if(bossSpawned) return;
		
		deleteEnemies(null);
		CAPACITY = 0;
		enemySpawnSupport.setMaxCapacity(CAPACITY);
	}
	
	private void spawnBoss() {
		if(bossSpawned) return;
		
		Boss boss = new Boss(tileMap, 0, null, dialog);
		bossId = boss.getId();
		boss.setPosition(GameStateManager.LEVEL7BOSSX, GameStateManager.LEVEL7BOSSY);
		enemies.add(boss);
		bossSpawned = true;
	}

	private void startBossPreFightDialog() {
		if(Support.difficulty == Support.DIFFICULTY_CASUAL) dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_PREFIGHT_CASUAL, Support.bossSpeakerStatus); 
		else if(Support.difficulty == Support.DIFFICULTY_SUICIDE) dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_PREFIGHT_SUICIDE, Support.bossSpeakerStatus);
		else dialog.startNewDialogById(DialogConversation.CONVERSATION_BOSS_PREFIGHT_NORMAL, Support.bossSpeakerStatus); 
		dialog.setAllowAutomaticProgress(false);
		dialog.setAllowManualProgress(true);
		bossPreFightDialogStarted = true;
	}
	
	private void startBossFight() {
		JukeBox.stopAllMusicTracks();
		JukeBox.adjustVolume("boss", -15);
		JukeBox.loop("boss", 600, JukeBox.getFrames("boss") - 2200);
		bossfightStarted = true;
	}
	
	private void prepareBossFinalAttack() {
		ArrayList<Integer> exceptions = new ArrayList<Integer>();
		exceptions.add(this.bossId);
		for(Enemy e : enemies) {
			if(e.getId() == bossId) {
				Boss b = (Boss) e;
				bossShieldId = b.getFinalAttackShieldId();
				exceptions.add(bossShieldId);
			}
		}
		deleteEnemies(exceptions);
		bossfightFinalAttackPreparationsDone = true;
	}

	private void checkForBossFightOngoingFinalAttack() {
		if(!bossSpawned || !bossfightStarted || !bossfightFinalAttackPreparationsDone) return;
		
		boolean finalAttackIsOngoing = false;
		
		for(int i = 0; i < enemies.size(); i++) {
			if(enemies.get(i).getId() == bossShieldId) {
				enemies.add(enemies.remove(i));
				BossShield bs = (BossShield) enemies.get(enemies.size() - 1);
				if(bs.shouldDeleteEnemies()) {
					ArrayList<Integer> exceptions = new ArrayList<Integer>();
					exceptions.add(bossId);
					exceptions.add(bossShieldId);
					deleteEnemies(exceptions);
					bs.setDeleteEnemies(false);
				}
				finalAttackIsOngoing = true;
			}
		}
		
		// runs once final attack bossShield isn't found anymore and releases the player
		if(!finalAttackIsOngoing) player.setGrabbed(false);
	}

	private void checkForEndScreenFadeInStart() {
		if(!bossSpawned || endScreenFadeInHasStarted) return;
		
		Boss b = getBossById();
		
		if(b.shouldFadeInEndScreen()) {
			endScreenFadeInStart = System.nanoTime();
			endScreenFadeInHasStarted = true;
		}
	}
	
	// sets the current opacity
	private void checkForEndScreenFadeInUpdate() {
		if(!endScreenFadeInHasStarted) return;
		
		long endScreenFadeInTimePassed = (System.nanoTime() - endScreenFadeInStart) / 1000000;
		double endScreenFadeInTimePassedOfTotal = (double) endScreenFadeInTimePassed /  (double) endScreenFadeInTime;
		endScreenOpacity = (float) endScreenFadeInTimePassedOfTotal * Support.NORMALOPACITY;
		if(endScreenOpacity >= Support.NORMALOPACITY) {
			endScreenOpacity = Support.NORMALOPACITY;
<<<<<<< HEAD
=======
			GamePanel.playtime.setStopTimeOnNextGameStateChange(true);
>>>>>>> 2.03
			changeGameState(GameStateManager.ENDSCREENSTATE, false);
		}
	}
	
	@Override
	protected void handleMusic() {
		if(JukeBox.containsClip(musicTrack) && JukeBox.containsClip("boss")) {
			status = WAITING_PAST_LOADING;
		}
		else {
			status = LOADING;
			GamePanel.specificAudioToLoad.add(musicTrack);
			GamePanel.specificAudioToLoad.add("boss");
			GamePanel.loadSpecificAudio = true;
			GamePanel.loading = true;
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		if(player != null && tileMap != null && bg != null && enemies != null && portals != null) {
			try {
				checkForBossSpawn();
				
				checkForBossFightStart();
				
				checkForBossFightFinalAttackPreparation();
				
				checkForBossFightFinalAttackDestruction();
				
				checkForBossFightOngoingFinalAttack();
				
				checkForEndScreenFadeInStart();
				
				checkForEndScreenFadeInUpdate();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, endScreenOpacity));
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}
}


















