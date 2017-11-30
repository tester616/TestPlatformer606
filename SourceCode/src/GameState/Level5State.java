package GameState;

import java.awt.*;

import Support.Support;

public class Level5State extends GameState {
	
	public Level5State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 5;
		levelName = "The tease";
		levelBriefing = "I was really hoping we wouldn't run into any of his pitiful mischievous pranks, but this area seems to suggest otherwise. On a " +
			"separate note, the hideout has already proved to be worryingly large, which means he's probably stronger than any of us anticipated. Don't " +
			"underestimate him, tread carefully and try not to make a fool of yourself. At least the goal isn't exactly hidden this time.";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level5.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL5PLAYERX;
		playerStartY = GameStateManager.LEVEL5PLAYERY;
		
		musicTrack = "level5";
		musicDampen = -10;
		musicStartFrame = 500000;
		
		CAPACITY = 70;
		
		CAPACITY_INTERVAL = (int) (4500 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 13000;
		ODDS_HALFMOON = 13000;
		ODDS_MINE = 14000;
		ODDS_SENTRY = 7000;
		ODDS_SHIELD = 10000;
		ODDS_SPIKES = 13000;
		ODDS_SQUARE = 7000;
		ODDS_STAR = 10000;
		ODDS_TRIANGLE = 13000;
		
		INITIAL_DARKMIST = 1;
		INITIAL_HALFMOON = 2;
		INITIAL_MINE = 6;
		INITIAL_SENTRY = 0;
		INITIAL_SHIELD = 1;
		INITIAL_SPIKES = 5;
		INITIAL_SQUARE = 1;
		INITIAL_STAR = 0;
		INITIAL_TRIANGLE = 6;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL5LIFEX, GameStateManager.LEVEL5LIFEY)
			};
		}
		
		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL5PORTALX, GameStateManager.LEVEL5PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL6STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL5STATE;
		respawnTransferPlayer = true;
	}
}










