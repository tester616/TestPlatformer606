package GameState;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
<<<<<<< HEAD
=======
import java.util.Random;
>>>>>>> 2.03

import Audio.JukeBox;
import Entity.Ally;
import Entity.Announcement;
import Entity.Collectible;
import Entity.Enemy;
import Entity.EntitySpawnData;
import Entity.HUD;
import Entity.Life;
import Entity.Player;
import Entity.Portal;
import Entity.Enemies.Darkmist;
import Entity.Enemies.Halfmoon;
import Entity.Enemies.Mine;
import Entity.Enemies.Sentry;
import Entity.Enemies.Shield;
import Entity.Enemies.Spikes;
import Entity.Enemies.Square;
import Entity.Enemies.Star;
import Entity.Enemies.Triangle;
import Entity.Dialog;
import Handlers.Keys;
import Main.GamePanel;
import Support.LevelEnemySpawnSupport;
import Support.Support;
import TileMap.Background;
import TileMap.TileMap;

public abstract class GameState {
	
	// describes if the game is currently for example loading or running normally
	protected int status;
	
	// possible status values
	protected final int LOADING = 0;
	protected final int WAITING_PAST_LOADING = 1;
	protected final int PLAYING = 2;
	
	protected String loadingText;
	protected String waitingText;

	protected int levelNumber;
	protected String levelName;
	protected String levelBriefing;
	
	protected boolean escapeButton;
	protected boolean enterButton;
	protected boolean deleteButton;
	protected boolean f1Button;
	
	private Color fontColor;
	private Font largeFont;
	private Font smallFont;
	private Font tinyFont;

	protected GameStateManager gsm;

	protected TileMap tileMap;
	protected Background bg;
	
	protected Player player;
	
	protected ArrayList<Enemy> enemies;
	protected ArrayList<Ally> allies;
	protected ArrayList<Portal> portals;
	protected ArrayList<Collectible> collectibles;
	protected ArrayList<Announcement> announcements;

	// player related entities waiting to be spawned
	protected ArrayList<EntitySpawnData> playerSpawnList;
	// enemy related entities waiting to be spawned
	protected ArrayList<EntitySpawnData> enemySpawnList;
	// enemies that didn't find a valid spawn point waiting for another search before joining enemySpawnList
	protected ArrayList<EntitySpawnData> enemyInvalidPointSpawnList;
	// collectible related entities waiting to be spawned
	protected ArrayList<EntitySpawnData> collectibleSpawnList;
	// collectibles that didn't find a valid spawn point waiting for another search before joining collectibleSpawnList
	protected ArrayList<EntitySpawnData> collectibleInvalidPointSpawnList;
	// announcements that pop up on screen and float with constant speed before disappearing
	protected ArrayList<EntitySpawnData> announcementSpawnList;
	
	protected LevelEnemySpawnSupport enemySpawnSupport;
	
	protected HUD hud;
	
	protected int CAPACITY;
	
	protected int CAPACITY_INTERVAL;
	
	protected int ODDS_DARKMIST;
	protected int ODDS_HALFMOON;
	protected int ODDS_MINE;
	protected int ODDS_SENTRY;
	protected int ODDS_SHIELD;
	protected int ODDS_SPIKES;
	protected int ODDS_SQUARE;
	protected int ODDS_STAR;
	protected int ODDS_TRIANGLE;
	
	protected int INITIAL_DARKMIST;
	protected int INITIAL_HALFMOON;
	protected int INITIAL_MINE;
	protected int INITIAL_SENTRY;
	protected int INITIAL_SHIELD;
	protected int INITIAL_SPIKES;
	protected int INITIAL_SQUARE;
	protected int INITIAL_STAR;
	protected int INITIAL_TRIANGLE;

	protected String tileset;
	protected String tilesetM;
	protected String map;
	protected String background;
	protected String backgroundM;
	
	protected int playerStartX;
	protected int playerStartY;

	protected String musicTrack;
	protected int musicDampen;
	protected int musicStartFrame;

	protected Point[] lifePoints;
	protected Point[] portalPoints;

	protected int portalContactGameState;
	protected boolean portalContactTransferPlayer;

	protected int escapeGameState;
	protected boolean escapeTransferPlayer;

	protected int respawnGameState;
	protected boolean respawnTransferPlayer;
	
	protected Dialog dialog;
	protected Font dialogTitleFont;
	protected Color dialogTitleColor;
	protected String dialogTitleText;
	protected Font dialogTextFont;
	protected Color dialogTextColor;
	protected String dialogDialogText;
	
	private int loadingScreenTextMarginTop;
	private int loadingScreenTextMarginLeft;
	private int loadingScreenTextMarginRight;
	// how far under the level title it begins
	private int loadingScreenTextMarginLevelBriefingFromName;
	private int loadingScreenTextMarginLevelBriefingRow;
	
	protected ArrayList<String> levelBriefingTextRows;
	
	protected boolean levelBriefingTextRowsCalculated;

	protected boolean lifeCollected;
	
	
	protected GameState(GameStateManager gsm) {
		this.gsm = gsm;
		init();
	}
	protected void init() {
		try {
			enemies = new ArrayList<Enemy>();
			allies = new ArrayList<Ally>();
			portals = new ArrayList<Portal>();
			collectibles = new ArrayList<Collectible>();
			announcements = new ArrayList<Announcement>();

			levelBriefingTextRows = new ArrayList<String>();
			
			setLevelSpecificValues();

			playerSpawnList = new ArrayList<EntitySpawnData>();
			enemySpawnList = new ArrayList<EntitySpawnData>();
			enemyInvalidPointSpawnList = new ArrayList<EntitySpawnData>();
			collectibleSpawnList = new ArrayList<EntitySpawnData>();
			collectibleInvalidPointSpawnList = new ArrayList<EntitySpawnData>();
			announcementSpawnList = new ArrayList<EntitySpawnData>();
			
			loadingText = "loading...";
			waitingText = "press enter to continue";
			
			fontColor = Color.RED;
			largeFont = new Font("Century Gothic", Font.PLAIN, 28);
			smallFont = new Font("Arial", Font.PLAIN, 12);
			tinyFont = new Font("Arial", Font.PLAIN, 10);
			
			tileMap = new TileMap(Support.TILESIZE);
			tileMap.loadTiles(tileset, tilesetM);
			tileMap.loadMap(map);
			tileMap.setTween(1);

			loadingScreenTextMarginTop = 10;
			loadingScreenTextMarginLeft = 10;
			loadingScreenTextMarginRight = 10;
			loadingScreenTextMarginLevelBriefingFromName = 15;
			loadingScreenTextMarginLevelBriefingRow = 3;
			
			levelBriefingTextRowsCalculated = false;
			
			bg = new Background(background, backgroundM, 0.1);
			
			dialog = new Dialog();
			
			if(gsm.getGameStateTransferData().getPlayer() == null) {
				player = new Player(tileMap, dialog);
			}
			else {
				player = gsm.getGameStateTransferData().getPlayer();
				player.setTileMap(tileMap);
			}
			player.setPosition(playerStartX, playerStartY);
			
			tileMap.setPosition(
					GamePanel.WIDTH / 2 - player.getx(),
					GamePanel.HEIGHT / 2 - player.gety()
			);

			hud = new HUD(player);

			JukeBox.stopAllMusicTracks();
			handleMusic();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// run once you start playing on the level
	private void startLevel() {
		enemySpawnSupport = new LevelEnemySpawnSupport(CAPACITY, CAPACITY_INTERVAL);
		
		// adds all initial enemies of the stage
		populateEnemySpawnList();
		populatePortals();
		populateCollectibles();
		
		updateEnemySpawnSupportCapacity();
		setEnemySpawnOdds();
	}
	
	// each level game state implements their own solution
	protected void setLevelSpecificValues() {
		
	}
	
	// each level game state can give additional implementation, like load further tracks
	protected void handleMusic() {
		if(JukeBox.containsClip(musicTrack)) {
			status = WAITING_PAST_LOADING;
		}
		else {
			status = LOADING;
			GamePanel.specificAudioToLoad.add(musicTrack);
			GamePanel.loadSpecificAudio = true;
			GamePanel.loading = true;
		}
	}
	
	protected void setMusic() {
		JukeBox.stopAllMusicTracks();
		//JukeBox.adjustVolume(musicTrack, musicDampen);
		//JukeBox.loop(musicTrack, musicStartFrame, musicStartFrame, JukeBox.getFrames(musicTrack) - 200);
		JukeBox.loopWithRecommendedVolume(musicTrack, musicStartFrame, musicStartFrame, JukeBox.getFrames(musicTrack) - 200);
	}
	
	protected void setEnemySpawnOdds() {
		try {
			enemySpawnSupport.setEnemyMinMax(
				Support.DARKMIST,
				0,
				ODDS_DARKMIST - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.HALFMOON,
				ODDS_DARKMIST,
				ODDS_DARKMIST + ODDS_HALFMOON - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.MINE,
				ODDS_DARKMIST + ODDS_HALFMOON,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.SENTRY,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.SHIELD,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.SPIKES,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.SQUARE,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES + ODDS_SQUARE - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.STAR,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES + ODDS_SQUARE,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES + ODDS_SQUARE + ODDS_STAR - 1
			);
			enemySpawnSupport.setEnemyMinMax(
				Support.TRIANGLE,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES + ODDS_SQUARE + ODDS_STAR,
				ODDS_DARKMIST + ODDS_HALFMOON + ODDS_MINE + ODDS_SENTRY + ODDS_SHIELD + ODDS_SPIKES + ODDS_SQUARE + ODDS_STAR + ODDS_TRIANGLE - 1
			);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void updateEnemySpawnSupportCapacity() {
		enemySpawnSupport.setCurrentCapacity(0);
		for(Enemy e : enemies) {
			enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + e.getCost());
		}
		if(enemySpawnSupport.getCurrentCapacity() < 0) enemySpawnSupport.setCurrentCapacity(0);
	}
	
	private void checkForNewEnemyToSpawn() {
<<<<<<< HEAD
		if(enemySpawnSupport.shouldSpawnEnemy()) {
=======
		if(enemySpawnSupport.shouldSpawnEnemy() && !Support.cheatStopEnemySpawning) {
>>>>>>> 2.03
			int type = enemySpawnSupport.getRandomEnemyType();
			if(type == Support.NOTYPE) return;
			
			if(type == Support.DARKMIST) {
				addEnemyToSpawnList(Support.DARKMIST, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_DARKMIST);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_DARKMIST);
			}
			else if(type == Support.HALFMOON) {
				addEnemyToSpawnList(Support.HALFMOON, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_HALFMOON);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_HALFMOON);
			}
			else if(type == Support.MINE) {
				addEnemyToSpawnList(Support.MINE, 1, false, false);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_MINE);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_MINE);
			}
			else if(type == Support.SENTRY) {
				addEnemyToSpawnList(Support.SENTRY, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_SENTRY);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_SENTRY);
			}
			else if(type == Support.SHIELD) {
				addEnemyToSpawnList(Support.SHIELD, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_SHIELD);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_SHIELD);
			}
			else if(type == Support.SPIKES) {
				addEnemyToSpawnList(Support.SPIKES, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_SPIKES);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_SPIKES);
			}
			else if(type == Support.SQUARE) {
				addEnemyToSpawnList(Support.SQUARE, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_SQUARE);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_SQUARE);
			}
			else if(type == Support.STAR) {
				addEnemyToSpawnList(Support.STAR, 1, false, true);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_STAR);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_STAR);
			}
			else if(type == Support.TRIANGLE) {
				addEnemyToSpawnList(Support.TRIANGLE, 1, false, false);
				enemySpawnSupport.setLastAddedCapacity(Support.COST_TRIANGLE);
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() + Support.COST_TRIANGLE);
			}
			else {
				System.out.println("Wrong type " + type + " in checkForNewEnemyToSpawn()");
			}
		}
	}
	
	protected void populateCollectibles() {
		Life l;
		for(int i = 0; i < lifePoints.length; i++) {
			l = new Life(tileMap);
			l.setPosition(lifePoints[i].x, lifePoints[i].y);
			collectibles.add(l);
		}
	}
	
	protected void populatePortals() {
		Portal p;
		for(int i = 0; i < portalPoints.length; i++) {
			p = new Portal(tileMap, true);
			p.setPosition(portalPoints[i].x, portalPoints[i].y);
			portals.add(p);
		}
	}
	
	protected void populateEnemySpawnList() {
		addEnemyToSpawnList(Support.DARKMIST, INITIAL_DARKMIST, false, true);
		addEnemyToSpawnList(Support.HALFMOON, INITIAL_HALFMOON, false, true);
		addEnemyToSpawnList(Support.MINE, INITIAL_MINE, false, false);
		addEnemyToSpawnList(Support.SENTRY, INITIAL_SENTRY, false, true);
		addEnemyToSpawnList(Support.SHIELD, INITIAL_SHIELD, false, true);
		addEnemyToSpawnList(Support.SPIKES, INITIAL_SPIKES, false, true);
		addEnemyToSpawnList(Support.SQUARE, INITIAL_SQUARE, false, true);
		addEnemyToSpawnList(Support.STAR, INITIAL_STAR, false, true);
		addEnemyToSpawnList(Support.TRIANGLE, INITIAL_TRIANGLE, false, false);
	}
	
	// add chosen amount of an enemy type to spawn list
	protected void addEnemyToSpawnList(int type, int amount, boolean canBeVisibleToPlayer, boolean canBeInMidair) {
		if(amount <= 0) return;
		
		for(int i = 0; i < amount; i++) {
			Enemy e = null;
			if(type == Support.DARKMIST) {
				e = new Darkmist(tileMap, 0, null);
			}
			else if(type == Support.HALFMOON) {
				e = new Halfmoon(tileMap, 0, null);
			}
			else if(type == Support.MINE) {
				e = new Mine(tileMap, 0, null);
			}
			else if(type == Support.SENTRY) {
				e = new Sentry(tileMap, 0, null);
			}
			else if(type == Support.SHIELD) {
				e = new Shield(tileMap, 0, null);
			}
			else if(type == Support.SPIKES) {
				e = new Spikes(tileMap, 0, null);
			}
			else if(type == Support.SQUARE) {
				e = new Square(tileMap, 0, null);
			}
			else if(type == Support.STAR) {
				e = new Star(tileMap, 0, null);
			}
			else if(type == Support.TRIANGLE) {
				e = new Triangle(tileMap, 0, null);
			}
			else {
				System.out.println("Error in addEnemyToSpawnList, type " + type + " doesn't exist.");
			}
			
			Point p = e.getSuitableRandomSpawnPoint(canBeVisibleToPlayer, canBeInMidair);
			EntitySpawnData esd = new EntitySpawnData(
				e,
				p.x,
				p.y
			);
			if(p.x == 0 && p.y == 0) {
				enemyInvalidPointSpawnList.add(esd);
				System.out.println("No suitable spawn point found for an enemy.");
			}
			else {
				enemySpawnList.add(esd);
			}
		}
	}
	
	protected void update() {
		if(player != null && tileMap != null && bg != null && enemies != null && portals != null) {
			if(escapeButton) {
				resetValues();
				changeGameState(escapeGameState, escapeTransferPlayer);
			}
			
			if(status == LOADING) {
				updateLoading();
			}
			else if(status == WAITING_PAST_LOADING) {
				updateWaiting();
			}
			else if(status == PLAYING) {
				updatePlaying();
			}
			else System.out.println("Wrong status (" + status + ") in GameState for " + levelName);
		}
	}
	
	// runs while loading
	private void updateLoading() {
		if(JukeBox.containsClip(musicTrack)) {
			status = WAITING_PAST_LOADING;
		}
	}
	
	// runs past load while waiting for players input
	private void updateWaiting() {
		handleWaitingInput();
		
		if(enterButton) {
			status = PLAYING;
			
			startLevel();
			
<<<<<<< HEAD

			
			setMusic();
=======
			setMusic();
			
			// start counting game time if an order for it is pending
			if(GamePanel.playtime.isStartTimeOnNextStageStart()) {
				GamePanel.playtime.startGameTime();
				GamePanel.playtime.setStartTimeOnNextStageStart(false);
			}
>>>>>>> 2.03
		}
		
		// if inputCheats is on you can toggle cheatInputMode
		if(f1Button && Support.inputCheats) {
			Support.cheatInputMode = !Support.cheatInputMode;
		}
	}
	
	// runs normally while game runs
	private void updatePlaying() {
		// check keys
		handleInput();
		
		// player related updates
		spawnPlayerEntities();
		addPlayerEntitiesToSpawnList();
		player.update();
		// check if hit by enemies, used to be own attacks too but that's been moved to objects of Ally
		player.checkAttack(enemies);
		
		// updates related to objects allied to the player, for example any attacks in midair created by it
		updateAllies();
		
		// update tilemap
		tileMap.setPosition(
			GamePanel.WIDTH / 2 - player.getx(),
			GamePanel.HEIGHT / 2 - player.gety()
		);
		
		// set background
		bg.setPosition(tileMap.getx(), tileMap.gety());
		
		// player.checkAttack was here before!!! put back if problems are encountered and related to it
		
		// enemy related updates
		// handles ArrayList enemyInvalidPointSpawnList
		searchForEnemySpawnPoints();
		// handles ArrayList enemySpawnList
		spawnEnemyEntities();
		// handles ArrayList enemies
		updateEnemies();
		
		// adds a new randomly rolled enemy once a certain amount of time has passed granted there is capacity left
		checkForNewEnemyToSpawn();
		
		// portal updates were here before!!!
		
		// collectible related updates, following same logic as enemy related updates
		searchForCollectibleSpawnPoints();
		spawnCollectibleEntities();
		updateCollectibles();
		
		// announcement related updates, pretty much same logic again, but extends MapObject directly
		spawnAnnouncements();
		updateAnnouncements();
		
		// stage end portal related updates
		updatePortals();
		// checks if the player has reached the end portal for a level
		if(player.intersectsWithPortals(portals)) {
			player.resetStatusEffects();
			player.resetSpeed();
			changeGameState(portalContactGameState, portalContactTransferPlayer);
		}
		
		// dialog related updates
		dialog.update();
		
		// concerning tile map and background, other objects are done in their respective loops
		checkForSurroundingsColorUpdate();
		
		// if quickCheats is on you can destroy all enemies with delete
		if(deleteButton && Support.quickCheats) {
			deleteEnemies(null);
		}
		
		// if inputCheats is on you can toggle cheatInputMode
		if(f1Button && Support.inputCheats) {
			Support.cheatInputMode = !Support.cheatInputMode;
		}
		
		handlePlayerDeath();
	}
	
	// deletes either all enemies or spares chosen enemies by their id
	protected void deleteEnemies(ArrayList<Integer> exceptions) {
		if(exceptions == null || exceptions.isEmpty()) {
			enemies.clear();
			enemySpawnList.clear();
			enemyInvalidPointSpawnList.clear();
		}
		else {
			boolean spareEnemy;
			for(int i = 0; i < enemies.size(); i++) {
				spareEnemy = false;
				for(int id : exceptions) {
					if(enemies.get(i).getId() == id) {
						spareEnemy = true;
					}
				}
				if(!spareEnemy) {
					enemies.remove(i);
					i--;
				}
			}
			
			for(int i = 0; i < enemySpawnList.size(); i++) {
				spareEnemy = false;
				Enemy e = (Enemy) enemySpawnList.get(i).getO();
				for(int id : exceptions) {
					if(e.getId() == id) {
						spareEnemy = true;
					}
				}
				if(!spareEnemy) {
					enemySpawnList.remove(i);
					i--;
				}
			}
			
			for(int i = 0; i < enemyInvalidPointSpawnList.size(); i++) {
				spareEnemy = false;
				Enemy e = (Enemy) enemyInvalidPointSpawnList.get(i).getO();
				for(int id : exceptions) {
					if(e.getId() == id) {
						spareEnemy = true;
					}
				}
				if(!spareEnemy) {
					enemyInvalidPointSpawnList.remove(i);
					i--;
				}
			}
		}
		
		updateEnemySpawnSupportCapacity();
	}
	
	private void spawnPlayerEntities() {
		// spawn player related entities if any to spawn
		for(int i = 0; i < playerSpawnList.size(); i++) {
			Ally a = (Ally) playerSpawnList.get(i).getO();
			a.setPosition(
					playerSpawnList.get(i).getX(),
					playerSpawnList.get(i).getY()
			);
			allies.add(a);
			playerSpawnList.remove(i);
			i--;
		}
	}
	
	private void addPlayerEntitiesToSpawnList() {
		// add player related entities to spawn list
		ArrayList<EntitySpawnData> playerEntities = player.getEntitiesToSpawn();
		if(playerEntities != null) {
			for(int i = 0; i < playerEntities.size(); i++) {
				if(playerEntities.get(i).getO().getClass().getSimpleName().equals("Announcement")) {
					// last minute bandaid fix for a bigger problem with code that's not worth fixing anymore
					announcementSpawnList.add(playerEntities.get(i));
				}
				else {
					playerSpawnList.add(playerEntities.get(i));
				}
			}
		}
	}
	
	private void updateAllies() {
		// update all player related objects
		for(int i = 0; i < allies.size(); i++) {
			Ally a = allies.get(i);
			
			a.setPlayerInformation(player.getx(), player.gety());
			a.update();
			
			// even dead allies can spawn entities before they are erased, so this check is done first for every ally
			ArrayList<EntitySpawnData> allyEntities = a.getEntitiesToSpawn();
			if(allyEntities != null) {
				for(int j = 0; j < allyEntities.size(); j++) {
					playerSpawnList.add(allyEntities.get(j));
				}
			}
			
			// ally died during the update, remove it
			if(a.isDead()) {
				allies.remove(i);
				i--;
			}
			// ally didn't die, continue normally
			else {
				a.checkAttack(enemies);
				
				if(player.shouldChangeColors()) {
					a.setColorMode(Support.surroundingsColorMode);
					a.setBufferedImages();
					a.swapAnimationFrames();
				}
			}
		}
	}
	
	private void searchForEnemySpawnPoints() {
		// search for new spawnpoints if bad ones exist
		for(int i = 0; i < enemyInvalidPointSpawnList.size(); i++) {
			Enemy e2 = (Enemy) enemyInvalidPointSpawnList.get(i).getO();
			Point p = e2.getSuitableRandomSpawnPoint(
				enemyInvalidPointSpawnList.get(i).getCanBeVisibleToPlayer(),
				enemyInvalidPointSpawnList.get(i).getCanBeInMidair()
			);
			// if point is found, move this EntitySpawnData over to the other list
			if(!(p.x == 0 && p.y == 0)) {
				EntitySpawnData esd = new EntitySpawnData(
					enemyInvalidPointSpawnList.get(i).getO(),
					p.x,
					p.y,
					enemyInvalidPointSpawnList.get(i).getCanBeVisibleToPlayer(),
					enemyInvalidPointSpawnList.get(i).getCanBeInMidair()
				);
				enemySpawnList.add(esd);
				enemyInvalidPointSpawnList.remove(i);
			}
		}
	}
	
	private void spawnEnemyEntities() {
		// spawn enemy related entities if any to spawn
		for(int i = 0; i < enemySpawnList.size(); i++) {
			Enemy e3 = null;
			try {
				e3 = (Enemy) enemySpawnList.get(i).getO();
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			e3.setPosition(
				enemySpawnList.get(i).getX(),
				enemySpawnList.get(i).getY()
			);
			enemies.add(e3);
			enemySpawnList.remove(i);
			i--;
		}
	}
	
	private void updateEnemies() {
		// update all enemies
		for(int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			
			e.setPlayerInformation(player.getx(), player.gety());
			e.update();
			
			// even dead enemies can spawn entities before they are erased, so this check is done first for every enemy
			ArrayList<EntitySpawnData> enemyEntities = e.getEntitiesToSpawn();
			if(enemyEntities != null) {
				for(int j = 0; j < enemyEntities.size(); j++) {
					if(enemyEntities.get(j).getX() == 0 && enemyEntities.get(j).getY() == 0) {
						enemyInvalidPointSpawnList.add(enemyEntities.get(j));
					}
					else {
						enemySpawnList.add(enemyEntities.get(j));
					}
				}
			}
			
			// enemy died during update, remove it
			if(e.isDead()) {
				enemySpawnSupport.setCurrentCapacity(enemySpawnSupport.getCurrentCapacity() - enemies.get(i).getCost());
				enemies.remove(i);
				i--;
			}
			// enemy didn't die, continue normally
			else {
				if(player.shouldChangeColors()) {
					e.setColorMode(Support.surroundingsColorMode);
					e.setBufferedImages();
					e.swapAnimationFrames();
				}
			}
		}
	}
	
	private void searchForCollectibleSpawnPoints() {
		// search for new spawnpoints if bad ones exist
		for(int i = 0; i < collectibleInvalidPointSpawnList.size(); i++) {
			Collectible c = (Collectible) collectibleInvalidPointSpawnList.get(i).getO();
			Point p = c.getSuitableRandomSpawnPoint(
					collectibleInvalidPointSpawnList.get(i).getCanBeVisibleToPlayer(),
					collectibleInvalidPointSpawnList.get(i).getCanBeInMidair()
			);
			// if point is found, move this EntitySpawnData over to the other list
			if(!(p.x == 0 && p.y == 0)) {
				EntitySpawnData esd = new EntitySpawnData(
					collectibleInvalidPointSpawnList.get(i).getO(),
					p.x,
					p.y,
					collectibleInvalidPointSpawnList.get(i).getCanBeVisibleToPlayer(),
					collectibleInvalidPointSpawnList.get(i).getCanBeInMidair()
				);
				collectibleSpawnList.add(esd);
				collectibleInvalidPointSpawnList.remove(i);
			}
		}
	}
	
	private void spawnCollectibleEntities() {
		// spawn collectible related entities if any to spawn
		for(int i = 0; i < collectibleSpawnList.size(); i++) {
			Collectible a = null;
			try {
				a = (Collectible) collectibleSpawnList.get(i).getO();
			}
			catch(Exception e) {
				e.printStackTrace();
			}

			a.setPosition(
					collectibleSpawnList.get(i).getX(),
					collectibleSpawnList.get(i).getY()
			);
			collectibles.add(a);
			collectibleSpawnList.remove(i);
			i--;
		}
	}
	
	private void updateCollectibles() {
		// update collectibles
		for(int i = 0; i < collectibles.size(); i++) {
			Collectible c = collectibles.get(i);
			
			ArrayList<EntitySpawnData> collectibleEntities = c.getEntitiesToSpawn();
			if(collectibleEntities != null) {
				for(int j = 0; j < collectibleEntities.size(); j++) {
					if(collectibleEntities.get(j).getX() == 0 && collectibleEntities.get(j).getY() == 0) {
						collectibleInvalidPointSpawnList.add(collectibleEntities.get(j));
					}
					else {
						collectibleSpawnList.add(collectibleEntities.get(j));
					}
				}
			}
			
			if(c.isDead()) {
				collectibles.remove(i);
				i--;
			}
			else {
				c.update();
				
				if(player.shouldChangeColors()) {
					c.setColorMode(Support.surroundingsColorMode);
					c.setBufferedImages();
					c.swapAnimationFrames();
				}
				
				// contact check with collectibles
				if(player.intersects(c)) {
					if(c.getClass().getSimpleName().equals("Life")) {
						player.setLives(player.getLives() + 1);
						String lifeCollectedAnnouncement = "+1 life (" + player.getLives() + " total)";
						announcementSpawnList.add(new EntitySpawnData(
							new Announcement(
								tileMap,
								lifeCollectedAnnouncement,
								0.0,
								-0.30,
								1800,
								null,
								null
							),
							c.getx(),
							c.gety() - 15
						));
						lifeCollected = true;
						JukeBox.playWithRecommendedVolume("life");
					}
					
					c.setDead(true);
				}
			}
		}
	}
	
	private void spawnAnnouncements() {
		for(int i = 0; i < announcementSpawnList.size(); i++) {
			Announcement a = null;
			try {
				a = (Announcement) announcementSpawnList.get(i).getO();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			
			a.setPosition(
				announcementSpawnList.get(i).getX(),
				announcementSpawnList.get(i).getY()
			);
			announcements.add(a);
			announcementSpawnList.remove(i);
			i--;
		}
	}
	
	private void updateAnnouncements() {
		for(int i = 0; i < announcements.size(); i++) {
			Announcement a = announcements.get(i);
			
			ArrayList<EntitySpawnData> announcementEntities = a.getEntitiesToSpawn();
			if(announcementEntities != null) {
				for(int j = 0; j < announcementEntities.size(); j++) {
					announcementSpawnList.add(announcementEntities.get(j));
				}
			}
			
			if(a.isDead()) {
				announcements.remove(i);
				i--;
			}
			else {
				a.update();
			}
		}
	}
	
	private void updatePortals() {
		// update portals
		for(int i = 0; i < portals.size(); i++) {
			Portal p = portals.get(i);
			if(p.shouldRemove()) {
				portals.remove(i);
				i--;
			}
			else {
				p.update();
				
				if(player.shouldChangeColors()) {
					p.setColorMode(Support.surroundingsColorMode);
					p.setBufferedImages();
					p.swapAnimationFrames();
				}
			}
		}
	}

	private void handlePlayerDeath() {
		if(player.isDead() && player.shouldMoveOnWithDeath()) {
			resetValues();
			if(player.shouldRespawn()) {
				changeGameState(respawnGameState, respawnTransferPlayer);
			}
			else {
				changeGameState(GameStateManager.MENUSTATE, false);
			}
		}
	}
	
	private void checkForSurroundingsColorUpdate() {
		// last changes done by rewinding are to the tilemap and the background
		// changes to enemies and other objects happen in their loops above
		if(player.shouldChangeColors()) {
			tileMap.setColorMode(Support.surroundingsColorMode);
			tileMap.setBufferedImages();
			bg.setColorMode(Support.surroundingsColorMode);
			bg.setBufferedImages();
		}
	}
	
	// reset values before changing game states
	private void resetValues() {
		player.resetDeathValues();
	}
	
	private void checkInputCheatCode(String code) {
		if(code.equals(Support.CHEAT_INVULNERABILITY)) {
			player.setInvulnerable(!player.getInvulnerable());
<<<<<<< HEAD
		}
		else if(code.equals(Support.CHEAT_INFINITEJUMP)) {
			Support.cheatInfiniteJump = !Support.cheatInfiniteJump;
		}
		else if(code.equals(Support.CHEAT_INFINITEDASHJUMP)) {
			Support.cheatInfiniteDashJump = !Support.cheatInfiniteDashJump;
		}
		else if(code.equals(Support.CHEAT_KILLALLENEMIES)) {
			deleteEnemies(null);
		}
		else if(code.equals(Support.CHEAT_MANYLIVES)) {
			player.setLives(9);
		}
		else if(code.equals(Support.CHEAT_REGENHEALTH)) {
			Support.cheatHealthRegen = !Support.cheatHealthRegen;
		}
		else if(code.equals(Support.CHEAT_REGENATTACKENERGY)) {
			Support.cheatAttackRegen = !Support.cheatAttackRegen;
		}
		else if(code.equals(Support.CHEAT_REGENREWINDENERGY)) {
			Support.cheatRewindRegen = !Support.cheatRewindRegen;
		}
		else if(code.equals(Support.CHEAT_ATTACKRANGE)) {
			Support.cheatAttackRange = !Support.cheatAttackRange;
		}
		else if(code.equals(Support.CHEAT_ATTACKDAMAGE)) {
			Support.cheatAttackDamage = !Support.cheatAttackDamage;
		}
		else if(code.equals(Support.CHEAT_SHOWHITBOXES)) {
			Support.cheatShowHitboxes = !Support.cheatShowHitboxes;
		}
		else if(code.equals(Support.CHEAT_SHOWFPS)) {
			Support.cheatShowFps = !Support.cheatShowFps;
		}
		else if(code.equals(Support.CHEAT_SUICIDE)) {
			player.kill();
		}
		else if(code.equals(Support.CHEAT_LEVEL_1)) {
			changeGameState(1, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_1)) {
			changeGameState(1, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_1)) {
			changeGameState(1, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_2)) {
			changeGameState(2, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_3)) {
			changeGameState(3, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_4)) {
			changeGameState(4, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_5)) {
			changeGameState(5, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_6)) {
			changeGameState(6, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_7)) {
			changeGameState(7, true);
		}
=======
			String cheatAnnouncement = "Invulnerability: " + player.getInvulnerable();
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_INFINITEJUMP)) {
			Support.cheatInfiniteJump = !Support.cheatInfiniteJump;
			String cheatAnnouncement = "Infinite jump: " + Support.cheatInfiniteJump;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_INFINITEDASHJUMP)) {
			Support.cheatInfiniteDashJump = !Support.cheatInfiniteDashJump;
			String cheatAnnouncement = "Infinite dash jump: " + Support.cheatInfiniteDashJump;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_INFINITEPARRY)) {
			Support.cheatInfiniteParry = !Support.cheatInfiniteParry;
			String cheatAnnouncement = "Infinite parries: " + Support.cheatInfiniteParry;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_KILLALLENEMIES)) {
			deleteEnemies(null);
			String cheatAnnouncement = "";
			Random rand = new Random();
			int roll = rand.nextInt(3);
			if(roll == 0) cheatAnnouncement = "Wall was hit.";
			else if(roll == 1) cheatAnnouncement = "Unfeeling and merciless.";
			else if(roll == 2) cheatAnnouncement = "Is your next cheat going to involve 9 lives?";
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_STOPENEMYSPAWNING)) {
			Support.cheatStopEnemySpawning = !Support.cheatStopEnemySpawning;
			String cheatAnnouncement = "Stop enemy spawning: " + Support.cheatStopEnemySpawning;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_MANYLIVES)) {
			player.setLives(9);
			String cheatAnnouncement = "";
			Random rand = new Random();
			int roll = rand.nextInt(3);
			if(roll == 0) cheatAnnouncement = "You feel strangely feline.";
			else if(roll == 1) cheatAnnouncement = "But you still can't land on your feet.";
			else if(roll == 2) cheatAnnouncement = "You felt your reincarnations crawling on your back.";
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_REGENHEALTH)) {
			Support.cheatHealthRegen = !Support.cheatHealthRegen;
			String cheatAnnouncement = "Regenerating health: " + Support.cheatHealthRegen;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_REGENATTACKENERGY)) {
			Support.cheatAttackRegen = !Support.cheatAttackRegen;
			String cheatAnnouncement = "Attack energy fast regeneration: " + Support.cheatAttackRegen;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_REGENREWINDENERGY)) {
			Support.cheatRewindRegen = !Support.cheatRewindRegen;
			String cheatAnnouncement = "Rewind energy fast regeneration: " + Support.cheatRewindRegen;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_ATTACKRANGE)) {
			Support.cheatAttackRange = !Support.cheatAttackRange;
			String cheatAnnouncement = "Large attacks: " + Support.cheatAttackRange;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_ATTACKDAMAGE)) {
			Support.cheatAttackDamage = !Support.cheatAttackDamage;
			String cheatAnnouncement = "Strong attacks: " + Support.cheatAttackDamage;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_SHOWHITBOXES)) {
			Support.cheatShowHitboxes = !Support.cheatShowHitboxes;
			String cheatAnnouncement = "Show hitboxes: " + Support.cheatShowHitboxes;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_SHOWFPS)) {
			Support.cheatShowFps = !Support.cheatShowFps;
			String cheatAnnouncement = "Show fps: " + Support.cheatShowFps;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_SUICIDE)) {
			player.kill();
			String cheatAnnouncement = "Family status: Honored";
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_BOSSRUSH)) {
			Support.cheatBossRush = !Support.cheatBossRush;
			String cheatAnnouncement = "Boss rush spawn point: " + Support.cheatBossRush;
			addDefaultAnnouncement(cheatAnnouncement);
		}
		else if(code.equals(Support.CHEAT_LEVEL_1)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(1, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_2)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(2, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_3)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(3, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_4)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(4, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_5)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(5, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_6)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(6, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_7)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(7, true);
		}
		else if(code.equals(Support.CHEAT_LEVEL_EASTEREGG)) {
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
			changeGameState(8, true);
		}
	}
	
	private void addDefaultAnnouncement(String cheatAnnouncement) {
		announcementSpawnList.add(new EntitySpawnData(
			new Announcement(
				tileMap,
				cheatAnnouncement,
				0.0,
				-0.30,
				3500,
				null,
				null
			),
			player.getx(),
			player.gety() - 15
		));
>>>>>>> 2.03
	}
	
	protected void draw(Graphics2D g) {
		if(g != null && bg != null && tileMap != null && player != null && hud != null) {
			updateDrawOpacity(g);
			
			if(status == LOADING) {
				drawLoading(g);
			}
			else if(status == WAITING_PAST_LOADING) {
				drawWaiting(g);
			}
			else if(status == PLAYING) {
				drawPlaying(g);
			}
			else System.out.println("Wrong status (" + status + ") in GameState for " + levelName);
		}
	}
	
	private void updateDrawOpacity(Graphics2D g) {
		if(player.getRewindingTerms()) Support.surroundingsOpacity = Support.REWINDINGOPACITY;
		else if(player.isDizzy()) Support.surroundingsOpacity = Support.DIZZYOPACITY;
		else Support.surroundingsOpacity = Support.NORMALOPACITY;
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}
	
	private void drawLoading(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.NORMALOPACITY));
		Color origColor = g.getColor();
		Font origFont = g.getFont();
		FontMetrics metrics;
		Font levelNameFont = largeFont;
		Font levelBriefingFont = tinyFont;
		Font loadingFont = smallFont;
		
		metrics = g.getFontMetrics(levelNameFont);
		int levelNameHeight = levelNameFont.getSize();
	    int levelNameHalfWidth = (metrics.stringWidth(levelName)) / 2;
	    
	    metrics = g.getFontMetrics(levelBriefingFont);
	    int levelBriefingHeight = levelBriefingFont.getSize();
	    if(!levelBriefingTextRowsCalculated) {
	    	int rowSize = GamePanel.WIDTH - loadingScreenTextMarginLeft - loadingScreenTextMarginRight;
		    levelBriefingTextRows = Support.getSuitableTextRows(levelBriefing, rowSize, metrics);
		    levelBriefingTextRowsCalculated = true;
	    }
	    
		metrics = g.getFontMetrics(loadingFont);
	    int loadingWidth = metrics.stringWidth(loadingText);
	    int loadingHeight = smallFont.getSize();
	    
	    // draw black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw level name
		g.setColor(Color.RED);
		g.setFont(levelNameFont);
		g.drawString(levelName, GamePanel.WIDTH / 2 - levelNameHalfWidth, loadingScreenTextMarginTop + levelNameHeight);
		
		// draw level briefing
		g.setFont(levelBriefingFont);
		int levelBriefingDrawY;
		for(int i = 0; i < levelBriefingTextRows.size(); i++) {
			levelBriefingDrawY = loadingScreenTextMarginTop + levelNameHeight + loadingScreenTextMarginLevelBriefingFromName + levelBriefingHeight + 
				levelBriefingHeight * i + loadingScreenTextMarginLevelBriefingRow * i;
			g.drawString(levelBriefingTextRows.get(i), loadingScreenTextMarginLeft, levelBriefingDrawY);
		}
		
		// draw loading text
		g.setFont(loadingFont);
		g.drawString(loadingText, GamePanel.WIDTH - loadingWidth - 10, GamePanel.HEIGHT - loadingHeight / 2 - 10);
		
		// draw currentCheatCode if input mode is on
		if(Support.cheatInputMode) {
			g.drawString(Support.currentCheatCode, loadingScreenTextMarginLeft, GamePanel.HEIGHT - loadingHeight / 2 - 10);
		}
		
		g.setColor(origColor);
		g.setFont(origFont);
	}
	
	private void drawWaiting(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.NORMALOPACITY));
		Color origColor = g.getColor();
		Font origFont = g.getFont();
		FontMetrics metrics;
		Font levelNameFont = largeFont;
		Font levelBriefingFont = tinyFont;
		Font waitingFont = smallFont;
		
		metrics = g.getFontMetrics(levelNameFont);
		int levelNameHeight = levelNameFont.getSize();
	    int levelNameHalfWidth = (metrics.stringWidth(levelName)) / 2;
	    
	    metrics = g.getFontMetrics(levelBriefingFont);
	    int levelBriefingHeight = levelBriefingFont.getSize();
	    if(!levelBriefingTextRowsCalculated) {
	    	int rowSize = GamePanel.WIDTH - loadingScreenTextMarginLeft - loadingScreenTextMarginRight;
		    levelBriefingTextRows = Support.getSuitableTextRows(levelBriefing, rowSize, metrics);
		    levelBriefingTextRowsCalculated = true;
	    }
	    
		metrics = g.getFontMetrics(waitingFont);
	    int waitingWidth = metrics.stringWidth(waitingText);
	    int waitingHeight = smallFont.getSize();
	    
	    // draw black background
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw level name
		g.setColor(Color.RED);
		g.setFont(levelNameFont);
		g.drawString(levelName, GamePanel.WIDTH / 2 - levelNameHalfWidth, loadingScreenTextMarginTop + levelNameHeight);
		
		// draw level briefing
		g.setFont(levelBriefingFont);
		int levelBriefingDrawY;
		for(int i = 0; i < levelBriefingTextRows.size(); i++) {
			levelBriefingDrawY = loadingScreenTextMarginTop + levelNameHeight + loadingScreenTextMarginLevelBriefingFromName + levelBriefingHeight + 
				levelBriefingHeight * i + loadingScreenTextMarginLevelBriefingRow * i;
			g.drawString(levelBriefingTextRows.get(i), loadingScreenTextMarginLeft, levelBriefingDrawY);
		}
		
		// draw loading text
		g.setFont(waitingFont);
		g.drawString(waitingText, GamePanel.WIDTH - waitingWidth - 10, GamePanel.HEIGHT - waitingHeight / 2 - 10);
		
		// draw currentCheatCode if input mode is on
		if(Support.cheatInputMode) {
			g.drawString(Support.currentCheatCode, loadingScreenTextMarginLeft, GamePanel.HEIGHT - waitingHeight / 2 - 10);
		}
		
<<<<<<< HEAD
=======
		// draw currentCheatCode if input mode is on
		if(GamePanel.playtime.isDrawTime()) {
			g.drawString(
				GamePanel.playtime.getcHours() + ":" + GamePanel.playtime.getcMinutes() + ":" + GamePanel.playtime.getcSeconds() + "." + GamePanel.playtime.getcMilliseconds(),
				10,
				GamePanel.HEIGHT - waitingHeight / 2 - 10
			);
		}
		
>>>>>>> 2.03
		g.setColor(origColor);
		g.setFont(origFont);
	}
	
	private void drawPlaying(Graphics2D g) {
		// draw bg
		bg.draw(g);
		
		// tileMap blends too well in with the background so it's left unblurried during rewinding
		if(player.getRewindingTerms()) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.NORMALOPACITY));
		}
		
		// draw tilemap
		tileMap.draw(g);
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.playerOpacity));
		
		// draw player
		player.draw(g);

		// draw player related objects
		for(int i = 0; i < allies.size(); i++) {
			allies.get(i).draw(g);
			if(Support.cheatShowHitboxes) {
				allies.get(i).drawHitbox(g);
				allies.get(i).drawPosition(g);
			}
		}
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
		
		if(Support.cheatShowHitboxes) {
			player.drawHitbox(g);
			player.drawPosition(g);
		}
		
		// draw enemies
		for(int i = 0; i < enemies.size(); i++) {
			enemies.get(i).draw(g);
			if(Support.cheatShowHitboxes) {
				enemies.get(i).drawHitbox(g);
				enemies.get(i).drawPosition(g);
			}
		}
		
		// draw portals
		for(int i = 0; i < portals.size(); i++) {
			portals.get(i).draw(g);
		}
		
		// draw collectibles
		for(int i = 0; i < collectibles.size(); i++) {
			collectibles.get(i).draw(g);
		}
		
		// draw announcements
		for(int i = 0; i < announcements.size(); i++) {
			announcements.get(i).draw(g);
		}
		
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.playerOpacity));
		
		// draw hud
		hud.draw(g);
		
		// draw dialog if one is active
		dialog.draw(g);
		
		if(player.isDead() && player.shouldDrawDeathText()) {
			FontMetrics metrics;
		    int textHalfWidth;
		    
			g.setColor(fontColor);
			g.setFont(largeFont);
		    
		    // set metrics with correct font size
			metrics = g.getFontMetrics(largeFont);
			
			String livesLeft = "Lives left: " + player.getLives();
			
			// calculate string length with said metrics
		    textHalfWidth = (metrics.stringWidth(livesLeft)) / 2;
		    g.drawString(livesLeft, (GamePanel.WIDTH / 2) - textHalfWidth, (GamePanel.HEIGHT / 2) - 20);	
		}

		// draw currentFps and/or maxFps if wanted
		if(Support.cheatShowFps) {
			g.setColor(fontColor);
			g.setFont(smallFont);
			
			g.drawString("" + GamePanel.currentFps, GamePanel.WIDTH - 34, 12);
			g.drawString("" + GamePanel.maxFps, GamePanel.WIDTH - 34, 24);
		}

		// draw currentCheatCode if input mode is on
		if(Support.cheatInputMode) {
			g.setColor(fontColor);
			g.setFont(smallFont);
			
			g.drawString(Support.CHEATHELP_INFORMATION_1, loadingScreenTextMarginLeft, GamePanel.HEIGHT - 70);
			g.drawString(Support.CHEATHELP_INFORMATION_2, loadingScreenTextMarginLeft, GamePanel.HEIGHT - 50);
			g.drawString(Support.CHEATHELP_INFORMATION_3, loadingScreenTextMarginLeft, GamePanel.HEIGHT - 30);
			g.drawString(Support.currentCheatCode, loadingScreenTextMarginLeft, GamePanel.HEIGHT - 10);
		}
	}
	
	protected void changeGameState(int gameState, boolean transferPlayer) {
		try {
			GameStateTransferData gstd = new GameStateTransferData();
			if(transferPlayer) gstd.setPlayer(player);
			if(lifeCollected) gstd.setLevelLifeCollected(levelNumber, true);
<<<<<<< HEAD
=======
			if(GamePanel.playtime.isStopTimeOnNextGameStateChange()) {
				GamePanel.playtime.stopGameTime();
				GamePanel.playtime.setDrawTime(true);
				GamePanel.playtime.setStopTimeOnNextGameStateChange(false);
			}
			if(GamePanel.playtime.isResetFullOnNextGameStateChange()) {
				GamePanel.playtime.resetFull();
				GamePanel.playtime.setResetFullOnNextGameStateChange(false);
			}
>>>>>>> 2.03
			gsm.transferGameStateData(gstd);
			gsm.setState(gameState);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleInput() {
		if(!Support.cheatInputMode) {
			escapeButton = Keys.isPressed(Keys.ESC);
			dialog.setNextLine(Keys.isPressed(Keys.ENTER) || Keys.keyState[Keys.SPACE]);
			if(player.isFreeToAct()) {
				player.setUp(Keys.keyState[Keys.UP]);
				player.setLeft(Keys.keyState[Keys.LEFT]);
				player.setParrying(Keys.isPressed(Keys.DOWN));
				player.setRight(Keys.keyState[Keys.RIGHT]);
				player.setJumping(Keys.keyState[Keys.W]);
				player.setDashing(Keys.isPressed(Keys.E));
				player.setWeakAttacking(Keys.isPressed(Keys.R));
				player.setStrongAttacking(Keys.isPressed(Keys.T));
				player.setRewinding(Keys.keyState[Keys.F]);
				player.setRewindingInitialPress(Keys.isPressed(Keys.F));
				player.setSuiciding(Keys.isPressed(Keys.K));
			}
			// cheats
			player.setTogglingInvulnerability(Keys.isPressed(Keys.O));
			player.setTogglingInfiniteJump(Keys.isPressed(Keys.P));
			deleteButton = Keys.isPressed(Keys.DELETE);
			f1Button = Keys.isPressed(Keys.F1);
		}
		else {
			handleCheatInput();
		}
		f1Button = Keys.isPressed(Keys.F1);
	}
	
	public void handleWaitingInput() {
		escapeButton = Keys.isPressed(Keys.ESC);
		enterButton = Keys.isPressed(Keys.ENTER);
<<<<<<< HEAD
		/*if(!Support.cheatInputMode) {
			f1Button = Keys.isPressed(Keys.F1);
		}
		else {
			handleCheatInput();
		}*/
=======
>>>>>>> 2.03
	}
	
	// all hail javas elegant way to listen to a line from the keyboard without having focus like the console
	private void handleCheatInput() {
		String character = "";
		
		// add character
		if(Keys.isPressed(Keys.num1)) character = "1";
		if(Keys.isPressed(Keys.num2)) character = "2";
		if(Keys.isPressed(Keys.num3)) character = "3";
		if(Keys.isPressed(Keys.num4)) character = "4";
		if(Keys.isPressed(Keys.num5)) character = "5";
		if(Keys.isPressed(Keys.num6)) character = "6";
		if(Keys.isPressed(Keys.num7)) character = "7";
		if(Keys.isPressed(Keys.num8)) character = "8";
		if(Keys.isPressed(Keys.num9)) character = "9";
		if(Keys.isPressed(Keys.num0)) character = "0";
		if(Keys.isPressed(Keys.Q)) character = "q";
		if(Keys.isPressed(Keys.W)) character = "w";
		if(Keys.isPressed(Keys.E)) character = "e";
		if(Keys.isPressed(Keys.R)) character = "r";
		if(Keys.isPressed(Keys.T)) character = "t";
		if(Keys.isPressed(Keys.Y)) character = "y";
		if(Keys.isPressed(Keys.U)) character = "u";
		if(Keys.isPressed(Keys.I)) character = "i";
		if(Keys.isPressed(Keys.O)) character = "o";
		if(Keys.isPressed(Keys.P)) character = "p";
		if(Keys.isPressed(Keys.A)) character = "a";
		if(Keys.isPressed(Keys.S)) character = "s";
		if(Keys.isPressed(Keys.D)) character = "d";
		if(Keys.isPressed(Keys.F)) character = "f";
		if(Keys.isPressed(Keys.G)) character = "g";
		if(Keys.isPressed(Keys.H)) character = "h";
		if(Keys.isPressed(Keys.J)) character = "j";
		if(Keys.isPressed(Keys.K)) character = "k";
		if(Keys.isPressed(Keys.L)) character = "l";
		if(Keys.isPressed(Keys.Z)) character = "z";
		if(Keys.isPressed(Keys.X)) character = "x";
		if(Keys.isPressed(Keys.C)) character = "c";
		if(Keys.isPressed(Keys.V)) character = "v";
		if(Keys.isPressed(Keys.B)) character = "b";
		if(Keys.isPressed(Keys.N)) character = "n";
		if(Keys.isPressed(Keys.M)) character = "m";
		
		Support.currentCheatCode += character;
		
		// remove last character
		if(Keys.isPressed(Keys.BACKSPACE)) {
			if (Support.currentCheatCode != null && Support.currentCheatCode.length() > 0) {
				Support.currentCheatCode = Support.currentCheatCode.substring(0, Support.currentCheatCode.length() - 1);
		    }
		}
		
		// enter cheat
		if(Keys.isPressed(Keys.ENTER)) {
			Support.cheatInputMode = false;
			String finalCheatCode = Support.currentCheatCode;
			Support.currentCheatCode = "";
			checkInputCheatCode(finalCheatCode);
		}
	}
}












