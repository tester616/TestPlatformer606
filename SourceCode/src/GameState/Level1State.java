package GameState;

import java.awt.*;

import Support.Support;

public class Level1State extends GameState {
	
	public Level1State(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
		levelNumber = 1;
		levelName = "The entrance";
		levelBriefing = "Arrival, finally. The signal is strong enough to assume this is the entrance to his little hideout. And what an entrance it is. " +
			"Looks like his delusions of grandeur are as strong as ever... Target is to be destroyed on sight without negotiation. You're also free to " +
			"let any other possible nuisances share his fate. Quite simple, really. Wouldn't you agree?";
		
		tileset = "/Tilesets/spacetileset.gif";
		tilesetM = "/Tilesets/spacetilesetm.gif";
		
		map = "/Maps/level1.map";
		
		background = "/Backgrounds/spacebg.gif";
		backgroundM = "/Backgrounds/spacebgm.gif";
		
		playerStartX = GameStateManager.LEVEL1PLAYERX;
		playerStartY = GameStateManager.LEVEL1PLAYERY;
		
		musicTrack = "level1";
		musicDampen = -20;
		musicStartFrame = 0;
		
		CAPACITY = 50;
		
		CAPACITY_INTERVAL = (int) (4500 * Support.difficultySpawnrateMultiplier);
		
		ODDS_DARKMIST = 1000;
		ODDS_HALFMOON = 10000;
		ODDS_MINE = 1000;
		ODDS_SENTRY = 3500;
		ODDS_SHIELD = 200;
		ODDS_SPIKES = 36000;
		ODDS_SQUARE = 200;
		ODDS_STAR = 100;
		ODDS_TRIANGLE = 48000;
		
		INITIAL_DARKMIST = 0;
		INITIAL_HALFMOON = 0;
		INITIAL_MINE = 1;
		INITIAL_SENTRY = 0;
		INITIAL_SHIELD = 0;
		INITIAL_SPIKES = 7;
		INITIAL_SQUARE = 0;
		INITIAL_STAR = 0;
		INITIAL_TRIANGLE = 10;
		
		lifeCollected = gsm.getGameStateTransferData().getLevelLifeCollectedStatus().get(levelNumber);
		if(lifeCollected) {
			lifePoints = new Point[] {
					
			};
		}
		else {
			lifePoints = new Point[] {
				new Point(GameStateManager.LEVEL1LIFEX, GameStateManager.LEVEL1LIFEY)
			};
		}

		portalPoints = new Point[] {
			new Point(GameStateManager.LEVEL1PORTALX, GameStateManager.LEVEL1PORTALY)
		};
		
		portalContactGameState = GameStateManager.LEVEL2STATE;
		portalContactTransferPlayer = true;
		
		escapeGameState = GameStateManager.MENUSTATE;
		escapeTransferPlayer = false;
		
		respawnGameState = GameStateManager.LEVEL1STATE;
		respawnTransferPlayer = true;
		
		dialogTitleFont = new Font("Arial", Font.PLAIN, 12);
		dialogTitleColor = Color.RED;
		dialogTitleText = "Boss";
		
		dialogTextFont = new Font("Arial", Font.PLAIN, 10);
		dialogTextColor = Color.BLACK;
		dialogDialogText = "Some incredibly fucking long ass string where the boss talks shit about your choice of difficulty (especially for casuals). and mmmmmmmmmmmmmmmmmmmm m m mmmmmmmmmmmmmmmmmmmmmmm mmmmmmnn asdasdwaoli  ao fijaidfjemw awo japofrje p9ut 4iw73w kejh euh ekjdfgndkru h5u";
	}
}
