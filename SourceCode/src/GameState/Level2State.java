package GameState;

import java.awt.*;

import Support.Support;

public class Level2State extends GameState {
	
	public Level2State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 2;
		levelName = "The crossroad";
		levelBriefing = "Looks like the road up ahead splits into three. Which one holds the least amount of obstacles, I wonder? Make your decision, but don't take too long, as I have a creeping feeling it was created for that very purpose.";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level2.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL2PLAYERX;
		playerStartY = GameStateManager.LEVEL2PLAYERY;
		
		musicTrack = "level2";
		musicDampen = -10;
		musicStartFrame = 0;
		
		CAPACITY = 80;
		
		CAPACITY_INTERVAL = (int) (4200 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 1500;
		ODDS_HALFMOON = 10000;
		ODDS_MINE = 13000;
		ODDS_SENTRY = 4000;
		ODDS_SHIELD = 12000;
		ODDS_SPIKES = 25000;
		ODDS_SQUARE = 9000;
		ODDS_STAR = 500;
		ODDS_TRIANGLE = 25000;
		
		INITIAL_DARKMIST = 0;
		INITIAL_HALFMOON = 2;
		INITIAL_MINE = 3;
		INITIAL_SENTRY = 0;
		INITIAL_SHIELD = 1;
		INITIAL_SPIKES = 7;
		INITIAL_SQUARE = 0;
		INITIAL_STAR = 0;
		INITIAL_TRIANGLE = 7;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL2LIFEX, GameStateManager.LEVEL2LIFEY)
			};
		}
		
		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL2PORTALX, GameStateManager.LEVEL2PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL3STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL2STATE;
		respawnTransferPlayer = true;
	}
}












