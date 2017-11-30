package GameState;

import java.awt.*;

import Support.Support;

public class Level6State extends GameState {
	
	public Level6State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 6;
		levelName = "The labyrinth";
		levelBriefing = "What's this? A labyrinth of all things? Give me a break. Apparently you'll have to play another one of his little games. " +
			"But what is he hoping to gain from adding a few long, thin corridors together? Doesn't he know about the simple rule of following a wall to " +
			"get out of most labyrinths? What could you possibly get fooled by now?";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level6.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL6PLAYERX;
		playerStartY = GameStateManager.LEVEL6PLAYERY;
		
		musicTrack = "level6";
		musicDampen = -20;
		musicStartFrame = 0;
		
		CAPACITY = 200;
		
		CAPACITY_INTERVAL = (int) (3500 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 8000;
		ODDS_HALFMOON = 14000;
		ODDS_MINE = 24000;
		ODDS_SENTRY = 3000;
		ODDS_SHIELD = 9000;
		ODDS_SPIKES = 24000;
		ODDS_SQUARE = 6000;
		ODDS_STAR = 9000;
		ODDS_TRIANGLE = 14000;
		
		INITIAL_DARKMIST = 1;
		INITIAL_HALFMOON = 1;
		INITIAL_MINE = 7;
		INITIAL_SENTRY = 1;
		INITIAL_SHIELD = 1;
		INITIAL_SPIKES = 5;
		INITIAL_SQUARE = 2;
		INITIAL_STAR = 2;
		INITIAL_TRIANGLE = 5;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL6LIFEX, GameStateManager.LEVEL6LIFEY)
			};
		}
		
		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL6PORTALX, GameStateManager.LEVEL6PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL7STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL6STATE;
		respawnTransferPlayer = true;
	}
}















