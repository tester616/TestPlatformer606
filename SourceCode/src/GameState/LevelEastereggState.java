package GameState;

import java.awt.*;

import Support.Support;

public class LevelEastereggState extends GameState {
	
	public LevelEastereggState(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 8;
		levelName = "Episode 3";
		levelBriefing = "Bye bye episode 3.";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/leveleasteregg.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVELEASTEREGGPLAYERX;
		playerStartY = GameStateManager.LEVELEASTEREGGPLAYERY;
		
		musicTrack = "leveleasteregg";
		musicDampen = -13;
		musicStartFrame = 0;
		
		CAPACITY = 222;
		
		CAPACITY_INTERVAL = (int) (3333 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 13000;
		ODDS_HALFMOON = 13000;
		ODDS_MINE = 10000;
		ODDS_SENTRY = 11000;
		ODDS_SHIELD = 6000;
		ODDS_SPIKES = 16000;
		ODDS_SQUARE = 9000;
		ODDS_STAR = 8000;
		ODDS_TRIANGLE = 14000;
		
		INITIAL_DARKMIST = 2;
		INITIAL_HALFMOON = 2;
		INITIAL_MINE = 16;
		INITIAL_SENTRY = 2;
		INITIAL_SHIELD = 1;
		INITIAL_SPIKES = 13;
		INITIAL_SQUARE = 4;
		INITIAL_STAR = 2;
		INITIAL_TRIANGLE = 10;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVELEASTEREGGLIFEX, GameStateManager.LEVELEASTEREGGLIFEY)
			};
		}

		portalPoints = new Point[] {
			new Point(GameStateManager.LEVELEASTEREGGPORTALX, GameStateManager.LEVELEASTEREGGPORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL3STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVELEASTEREGGSTATE;
		respawnTransferPlayer = true;
	}
}
