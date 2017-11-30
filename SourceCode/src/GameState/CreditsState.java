package GameState;

import java.awt.*;
import java.util.ArrayList;

import Audio.JukeBox;
import Entity.*;
import Entity.Dialog;
import Main.GamePanel;
import Support.LevelEnemySpawnSupport;
import Support.Support;
import TileMap.*;

public class CreditsState extends GameState {
	
	private Color creditsColor;
	private Font creditsFont;
	
	public CreditsState(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/credits.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.CREDITSPLAYERX;
		playerStartY = GameStateManager.CREDITSPLAYERY;
		
		musicTrack = "menu";
		musicDampen = -10;
		musicStartFrame = 600;
		
		CAPACITY = 0;
		
		CAPACITY_INTERVAL = 3500;
		
		ODDS_DARKMIST = 1000;
		ODDS_HALFMOON = 5000;
		ODDS_MINE = 1000;
		ODDS_SENTRY = 15000;
		ODDS_SHIELD = 500;
		ODDS_SPIKES = 37000;
		ODDS_STAR = 500;
		ODDS_TRIANGLE = 40000;
		
		INITIAL_DARKMIST = 0;
		INITIAL_HALFMOON = 0;
		INITIAL_MINE = 0;
		INITIAL_SENTRY = 0;
		INITIAL_SHIELD = 0;
		INITIAL_SPIKES = 0;
		INITIAL_STAR = 0;
		INITIAL_TRIANGLE = 0;

		lifePoints = new Point[] {
			
		};
		portalPoints = new Point[] {
			new Point(GameStateManager.CREDITSPORTALX, GameStateManager.CREDITSPORTALY)
		};
		
		portalContactGameState = GameStateManager.MENUSTATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.CREDITSSTATE;
		respawnTransferPlayer = true;
	}
	
	/*@Override
	protected void setLevelSpecificValues() {
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level1.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL1PLAYERX;
		playerStartY = GameStateManager.LEVEL1PLAYERY;
		
		musicTrack = "level1";
		musicDampen = -20;
		musicStartFrame = 600;
		
		CAPACITY = 30;
		
		CAPACITY_INTERVAL = 3500;
		
		ODDS_DARKMIST = 1000;
		ODDS_HALFMOON = 10000;
		ODDS_MINE = 1000;
		ODDS_SHIELD = 500;
		ODDS_SPIKES = 37000;
		ODDS_STAR = 500;
		ODDS_TRIANGLE = 50000;
		
		INITIAL_DARKMIST = 0;
		INITIAL_HALFMOON = 0;
		INITIAL_MINE = 20;
		INITIAL_SHIELD = 0;
		INITIAL_SPIKES = 7;
		INITIAL_STAR = 0;
		INITIAL_TRIANGLE = 10;

		lifePoints = new Point[] {
			new Point(GameStateManager.LEVEL1LIFEX, GameStateManager.LEVEL1LIFEY)
		};
		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL1PORTALX, GameStateManager.LEVEL1PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL2STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
	}*/
	
	@Override
	public void init() {
		
		setLevelSpecificValues();
		
		enemies = new ArrayList<Enemy>();
		allies = new ArrayList<Ally>();
		portals = new ArrayList<Portal>();
		collectibles = new ArrayList<Collectible>();
		announcements = new ArrayList<Announcement>();
		
		playerSpawnList = new ArrayList<EntitySpawnData>();
		enemySpawnList = new ArrayList<EntitySpawnData>();
		enemyInvalidPointSpawnList = new ArrayList<EntitySpawnData>();
		collectibleSpawnList = new ArrayList<EntitySpawnData>();
		collectibleInvalidPointSpawnList = new ArrayList<EntitySpawnData>();
		announcementSpawnList = new ArrayList<EntitySpawnData>();
		
		enemySpawnSupport = new LevelEnemySpawnSupport(CAPACITY, CAPACITY_INTERVAL);
		
		tileMap = new TileMap(Support.TILESIZE);
		tileMap.loadTiles(tileset, tilesetM);
		tileMap.loadMap(map);
		tileMap.setTween(1);
		
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

		// adds all initial enemies of the stage
		populateEnemySpawnList();
		populatePortals();
		populateCollectibles();
		
		updateEnemySpawnSupportCapacity();
		setEnemySpawnOdds();
		
		// part of original init code below
		try {
			creditsColor = Color.RED;
			creditsFont = new Font(
					"Arial",
					Font.PLAIN,
					12
			);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!JukeBox.isPlaying(musicTrack)) {
			JukeBox.adjustVolume(musicTrack, musicDampen);
			JukeBox.loop(musicTrack, 600, JukeBox.getFrames(musicTrack) - 2200);
		}
		
		status = PLAYING;
	}
	
	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		
		if(g != null && bg != null && tileMap != null && player != null) {
			// draw credits
			g.setColor(creditsColor);
			g.setFont(creditsFont);
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.NORMALOPACITY));
			
			g.drawString(GameStateManager.CREDITS_PROGRAMMING, GameStateManager.CREDITS_PROGRAMMING_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_PROGRAMMING_MAIN, GameStateManager.CREDITS_PROGRAMMING_MAIN_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_MAIN_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_PROGRAMMING_MAIN2, GameStateManager.CREDITS_PROGRAMMING_MAIN2_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_MAIN2_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_PROGRAMMING_MAIN3, GameStateManager.CREDITS_PROGRAMMING_MAIN3_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_MAIN3_Y + tileMap.gety());

			g.drawString(GameStateManager.CREDITS_PROGRAMMING_SUPPORT, GameStateManager.CREDITS_PROGRAMMING_SUPPORT_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_SUPPORT_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_PROGRAMMING_SUPPORT2, GameStateManager.CREDITS_PROGRAMMING_SUPPORT2_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_SUPPORT2_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_PROGRAMMING_SUPPORT3, GameStateManager.CREDITS_PROGRAMMING_SUPPORT3_X + tileMap.getx(), GameStateManager.CREDITS_PROGRAMMING_SUPPORT3_Y + tileMap.gety());

			g.drawString(GameStateManager.CREDITS_GRAPHICS, GameStateManager.CREDITS_GRAPHICS_X + tileMap.getx(), GameStateManager.CREDITS_GRAPHICS_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_GRAPHICS_EVERYTHING, GameStateManager.CREDITS_GRAPHICS_EVERYTHING_X + tileMap.getx(), GameStateManager.CREDITS_GRAPHICS_EVERYTHING_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_GRAPHICS_EVERYTHING2, GameStateManager.CREDITS_GRAPHICS_EVERYTHING2_X + tileMap.getx(), GameStateManager.CREDITS_GRAPHICS_EVERYTHING2_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_GRAPHICS_EVERYTHING3, GameStateManager.CREDITS_GRAPHICS_EVERYTHING3_X + tileMap.getx(), GameStateManager.CREDITS_GRAPHICS_EVERYTHING3_Y + tileMap.gety());

			g.drawString(GameStateManager.CREDITS_AUDIO, GameStateManager.CREDITS_AUDIO_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_AUDIO_MUSIC, GameStateManager.CREDITS_AUDIO_MUSIC_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_MUSIC_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_AUDIO_MUSIC2, GameStateManager.CREDITS_AUDIO_MUSIC2_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_MUSIC2_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_AUDIO_MUSIC3, GameStateManager.CREDITS_AUDIO_MUSIC3_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_MUSIC3_Y + tileMap.gety());

			g.drawString(GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS, GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS2, GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS2_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS2_Y + tileMap.gety());
			g.drawString(GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS3, GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS3_X + tileMap.getx(), GameStateManager.CREDITS_AUDIO_SOUNDEFFECTS3_Y + tileMap.gety());
		}
	}
}









