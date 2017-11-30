package GameState;

import java.awt.*;

import Support.Support;

public class Level4State extends GameState {
	
	public Level4State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 4;
		levelName = "The Crossing";
		levelBriefing = "Some sight this is to behold. Two small islands separated by a giant looming chasm with only a few tiny pillars scattered across. " +
			"Crossing it using the pillars looks very dangerous, but luckily there are plenty of platforms floating above, just waiting to be " +
			"boarded. Stakes are about as high as their altitude, but even if you fall off you might be saved by one of the pillars... maybe.";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level4.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL4PLAYERX;
		playerStartY = GameStateManager.LEVEL4PLAYERY;
		
		musicTrack = "level4";
		musicDampen = -20;
		musicStartFrame = 0;
		
		CAPACITY = 50;
		
		CAPACITY_INTERVAL = (int) (4500 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 16000;
		ODDS_HALFMOON = 10000;
		ODDS_MINE = 28000;
		ODDS_SENTRY = 8000;
		ODDS_SHIELD = 9000;
		ODDS_SPIKES = 14000;
		ODDS_SQUARE = 4000;
		ODDS_STAR = 10000;
		ODDS_TRIANGLE = 1000;
		
		INITIAL_DARKMIST = 2;
		INITIAL_HALFMOON = 1;
		INITIAL_MINE = 5;
		INITIAL_SENTRY = 1;
		INITIAL_SHIELD = 0;
		INITIAL_SPIKES = 6;
		INITIAL_SQUARE = 0;
		INITIAL_STAR = 1;
		INITIAL_TRIANGLE = 1;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL4LIFEX, GameStateManager.LEVEL4LIFEY)
			};
		}
		
		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL4PORTALX, GameStateManager.LEVEL4PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL5STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL4STATE;
		respawnTransferPlayer = true;
	}
}












