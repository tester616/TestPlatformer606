package GameState;

import java.awt.*;

import Support.Support;

public class Level3State extends GameState {
	
	public Level3State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 3;
		levelName = "The jailbreak";
		levelBriefing = "You've arrived in some kind of a tall building. Scans indicate the threat enemies around here pose is quite manageable, but " +
				"also that the building itself is a dead end. This couldn't possibly be end of the road already? There has to be something else. Take a " +
				"look around, but be careful with some of the challenging jumps.";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level3.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL3PLAYERX;
		playerStartY = GameStateManager.LEVEL3PLAYERY;
		
		musicTrack = "level3";
		musicDampen = -10;
		musicStartFrame = 0;
		
		CAPACITY = 50;
		
		CAPACITY_INTERVAL = (int) (4700 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 7000;
		ODDS_HALFMOON = 10000;
		ODDS_MINE = 30000;
		ODDS_SENTRY = 0;
		ODDS_SHIELD = 0;
		ODDS_SPIKES = 20000;
		ODDS_SQUARE = 0;
		ODDS_STAR = 3000;
		ODDS_TRIANGLE = 30000;
		
		INITIAL_DARKMIST = 1;
		INITIAL_HALFMOON = 1;
		INITIAL_MINE = 5;
		INITIAL_SENTRY = 1;
		INITIAL_SHIELD = 0;
		INITIAL_SPIKES = 7;
		INITIAL_SQUARE = 0;
		INITIAL_STAR = 0;
		INITIAL_TRIANGLE = 6;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL3LIFEX, GameStateManager.LEVEL3LIFEY)
			};
		}

		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL3PORTALX, GameStateManager.LEVEL3PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL4STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL3STATE;
		respawnTransferPlayer = true;
	}
}
