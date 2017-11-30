package GameState;

import java.awt.Graphics2D;

public class GameStateManager {
	
	private GameState[] gameStates;
	private int currentState;
	
<<<<<<< HEAD
	// player data that carries from one state to another
	//private Player player;
	
	// data that can carry over when switching game states
	private GameStateTransferData gstd;
	
	public static final int NUMGAMESTATES = 10,
		NUMLEVELS = 7,
=======
	// data that can carry over when switching game states
	private GameStateTransferData gstd;
	
	public static final int NUMGAMESTATES = 11,
		NUMLEVELS = 8,
>>>>>>> 2.03
		MENUSTATE = 0,
		LEVEL1STATE = 1,
		LEVEL2STATE = 2,
		LEVEL3STATE = 3,
		LEVEL4STATE = 4,
		LEVEL5STATE = 5,
		LEVEL6STATE = 6,
		LEVEL7STATE = 7,
<<<<<<< HEAD
		ENDSCREENSTATE = 8,
		CREDITSSTATE = 9,
=======
		LEVELEASTEREGGSTATE = 8,
		ENDSCREENSTATE = 9,
		CREDITSSTATE = 10,
>>>>>>> 2.03
		
		// spawn points for player, portals and other stuff
		LEVEL1PLAYERX = 30, LEVEL1PLAYERY = 470,
		LEVEL2PLAYERX = 60, LEVEL2PLAYERY = 120,
		LEVEL3PLAYERX = 15, LEVEL3PLAYERY = 495,
		LEVEL4PLAYERX = 27, LEVEL4PLAYERY = 840,
		LEVEL5PLAYERX = 885, LEVEL5PLAYERY = 340,
		LEVEL6PLAYERX = 1685, LEVEL6PLAYERY = 80,
<<<<<<< HEAD
		LEVEL7PLAYERX = 1515, LEVEL7PLAYERY = 67, //LEVEL7PLAYERX = 620 for fast testing,
=======
		LEVEL7PLAYERX = 1515, LEVEL7PLAYERY = 67,
		LEVEL7PLAYERXBOSSRUSH = 120, LEVEL7PLAYERYBOSSRUSH = 190,
		LEVELEASTEREGGPLAYERX = 1455, LEVELEASTEREGGPLAYERY = 45,
>>>>>>> 2.03
		
		LEVEL1PORTALX = 1765, LEVEL1PORTALY = 85,
		LEVEL2PORTALX = 1745, LEVEL2PORTALY = 845,
		LEVEL3PORTALX = 1780, LEVEL3PORTALY = 855,
		LEVEL4PORTALX = 1755, LEVEL4PORTALY = 850,
		LEVEL5PORTALX = 885, LEVEL5PORTALY = 270,
		LEVEL6PORTALX = 1245, LEVEL6PORTALY = 380,
<<<<<<< HEAD
=======
		LEVELEASTEREGGPORTALX = 1065, LEVELEASTEREGGPORTALY = 195,
>>>>>>> 2.03
		
		LEVEL7BOSSX = 385, LEVEL7BOSSY = 520,
		
		LEVEL1LIFEX = 1665, LEVEL1LIFEY = 620,
		LEVEL2LIFEX = 405, LEVEL2LIFEY = 45,
		LEVEL3LIFEX = 970, LEVEL3LIFEY = 855,
		LEVEL4LIFEX = 1070, LEVEL4LIFEY = 630,
		LEVEL5LIFEX = 1340, LEVEL5LIFEY = 550,
		LEVEL6LIFEX = 885, LEVEL6LIFEY = 735,
		LEVEL7LIFEX = 1125, LEVEL7LIFEY = 180,
<<<<<<< HEAD
=======
		LEVELEASTEREGGLIFEX = -1000, LEVELEASTEREGGLIFEY = -1000,
>>>>>>> 2.03
		
		CREDITSPLAYERX = 250, CREDITSPLAYERY = 820,
		CREDITSPORTALX = 137, CREDITSPORTALY = 510,
		
		CREDITS_PROGRAMMING_X = 250, CREDITS_PROGRAMMING_Y = 700,
		CREDITS_PROGRAMMING_MAIN_X = 250, CREDITS_PROGRAMMING_MAIN_Y = 730,
		CREDITS_PROGRAMMING_MAIN2_X = 250, CREDITS_PROGRAMMING_MAIN2_Y = 745,
		CREDITS_PROGRAMMING_MAIN3_X = 250, CREDITS_PROGRAMMING_MAIN3_Y = 760,
		
		CREDITS_PROGRAMMING_SUPPORT_X = 400, CREDITS_PROGRAMMING_SUPPORT_Y = 730,
		CREDITS_PROGRAMMING_SUPPORT2_X = 400, CREDITS_PROGRAMMING_SUPPORT2_Y = 745,
		CREDITS_PROGRAMMING_SUPPORT3_X = 400, CREDITS_PROGRAMMING_SUPPORT3_Y = 760,
		
		CREDITS_GRAPHICS_X = 750, CREDITS_GRAPHICS_Y = 700,
		CREDITS_GRAPHICS_EVERYTHING_X = 750, CREDITS_GRAPHICS_EVERYTHING_Y = 730,
		CREDITS_GRAPHICS_EVERYTHING2_X = 750, CREDITS_GRAPHICS_EVERYTHING2_Y = 745,
		CREDITS_GRAPHICS_EVERYTHING3_X = 750, CREDITS_GRAPHICS_EVERYTHING3_Y = 760,
		
		CREDITS_AUDIO_X = 1000, CREDITS_AUDIO_Y = 700,
		CREDITS_AUDIO_MUSIC_X = 1000, CREDITS_AUDIO_MUSIC_Y = 730,
		CREDITS_AUDIO_MUSIC2_X = 1000, CREDITS_AUDIO_MUSIC2_Y = 745,
		CREDITS_AUDIO_MUSIC3_X = 1000, CREDITS_AUDIO_MUSIC3_Y = 760,
		
		CREDITS_AUDIO_SOUNDEFFECTS_X = 1340, CREDITS_AUDIO_SOUNDEFFECTS_Y = 730,
		CREDITS_AUDIO_SOUNDEFFECTS2_X = 1340, CREDITS_AUDIO_SOUNDEFFECTS2_Y = 745,
		CREDITS_AUDIO_SOUNDEFFECTS3_X = 1340, CREDITS_AUDIO_SOUNDEFFECTS3_Y = 760;
	
	public static final String CREDITS_PROGRAMMING = "Programming",
		CREDITS_PROGRAMMING_MAIN = "Programmer",
		CREDITS_PROGRAMMING_MAIN2 = "tester606",
		CREDITS_PROGRAMMING_MAIN3 = "Whoever that might be.",
		
		CREDITS_PROGRAMMING_SUPPORT = "Online tutorial",
		CREDITS_PROGRAMMING_SUPPORT2 = "ForeignGuyMike (www.youtube.com/user/foreignguymike)",
		CREDITS_PROGRAMMING_SUPPORT3 = "For his Dragon Tale tutorial and the map editor it includes.",
				
		CREDITS_GRAPHICS  = "Graphics",
		CREDITS_GRAPHICS_EVERYTHING = "Everything",
		CREDITS_GRAPHICS_EVERYTHING2 = "tester606",
		CREDITS_GRAPHICS_EVERYTHING3 = "By using GIMP (https://www.gimp.org).",
		
		CREDITS_AUDIO  = "Audio",
		CREDITS_AUDIO_MUSIC = "Music",
		CREDITS_AUDIO_MUSIC2 = "Jamie Nord (www.youtube.com/user/JamieNordMusic)",
		CREDITS_AUDIO_MUSIC3 = "A lifesaver in the barren lands of royalty free game music.",
		
		CREDITS_AUDIO_SOUNDEFFECTS = "Sound effects",
		CREDITS_AUDIO_SOUNDEFFECTS2 = "http://www.bfxr.net",
		CREDITS_AUDIO_SOUNDEFFECTS3 = "A neat tool to create game sound effects with.";
	
	
	public GameStateManager() {
		gameStates = new GameState[NUMGAMESTATES];
		
		currentState = MENUSTATE;
		loadState(currentState);
	}
	
	public void loadState(int state) {
		if(state == MENUSTATE)
			gameStates[state] = new MenuState(this);
		if(state == LEVEL1STATE)
			gameStates[state] = new Level1State(this);
		if(state == LEVEL2STATE)
			gameStates[state] = new Level2State(this);
		if(state == LEVEL3STATE)
			gameStates[state] = new Level3State(this);
		if(state == LEVEL4STATE)
			gameStates[state] = new Level4State(this);
		if(state == LEVEL5STATE)
			gameStates[state] = new Level5State(this);
		if(state == LEVEL6STATE)
			gameStates[state] = new Level6State(this);
		if(state == LEVEL7STATE)
			gameStates[state] = new Level7State(this);
<<<<<<< HEAD
=======
		if(state == LEVELEASTEREGGSTATE)
			gameStates[state] = new LevelEastereggState(this);
>>>>>>> 2.03
		if(state == ENDSCREENSTATE)
			gameStates[state] = new EndScreenState(this);
		if(state == CREDITSSTATE)
			gameStates[state] = new CreditsState(this);
	}
	
	public void unloadState(int state) {
		gameStates[state] = null;
	}

	public void setState(int state) {
		unloadState(currentState);
		currentState = state;
		loadState(currentState);
	}

	public void transferGameStateData(GameStateTransferData gstd) {
		this.gstd.setPlayer(gstd.getPlayer());
		for(int level = 1; level <= NUMLEVELS; level++) {
			if(gstd.getLevelLifeCollectedStatus().containsKey(level)) {
				this.gstd.getLevelLifeCollectedStatus().put(level, gstd.getLevelLifeCollectedStatus().get(level));
			}
		}
	}

	public GameStateTransferData getGameStateTransferData() {
		return gstd;
	}
	
	// call this always before starting a new game
	public void refreshGameStateTransferData() {
		gstd = new GameStateTransferData();
		for(int level = 1; level <= NUMLEVELS; level++) gstd.setLevelLifeCollected(level, false);
	}
	
	public void update() {
		try {
			gameStates[currentState].update();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g) {
		try {
			gameStates[currentState].draw(g);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
