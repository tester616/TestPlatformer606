package GameState;

import java.awt.*;

import Audio.JukeBox;
import Entity.Player;
import Handlers.Keys;
import Main.GamePanel;
import TileMap.Background;
import Support.Support;

public class MenuState extends GameState {
	
	private final int MENU = 0;
	private final int CONTROLS = 1;
	
	private int textMode;

	private Background bg;
	
	private int currentChoice = 0;
	private String[] options = {
		"Start",
		"Difficulty: ",
		"Controls",
		"Credits",
		"Size: ",
		"Quit"
	};
	
<<<<<<< HEAD
	private final String MOVEMENT1 = "\u2190 \u2192",
		MOVEMENT2 = "Move left or right.",
		JUMPING1 = "W E",
		JUMPING2 = "Perform a normal jump with W or a dash jump with E. Normal",
		JUMPING3 = "jumps gain increased height by holding down W. Dash jumps offer",
		JUMPING4 = "very little directional control, but can be used after normal jumps",
		JUMPING5 = "to help reach nearby ledges or jump from one wall to another.",
		ATTACKING1 = "R T",
		ATTACKING2 = "Attack enemies in front of you with R or around you with T.",
		SHIELDING1 = "\u2193",
		SHIELDING2 = "Activate a shield to become invulnerable for a very brief moment",
		SHIELDING3 = "followed by a short cooldown. Getting hit during this time",
		SHIELDING4 = "extends the invulnerability while allowing your next shield",
		SHIELDING5 = "to be activated just before it ends. Too early activation",
		SHIELDING6 = "doesn't work, so precise timing is the key.",
		REWINDING1 = "F",
		REWINDING2 = "Backtrack recent movement and changes to health, attack energy ",
=======
	// lazy old bad way of displaying used for these, which doesn't make use of automatic line management like for example the dialogs do
	private final String MOVEMENT1 = "\u2190 \u2192",
		MOVEMENT2 = "Move left or right.",
		JUMPING1 = "W E",
		JUMPING2 = "Jump with W or dash jump with E. Normal jumps only work from the",
		JUMPING3 = "ground and gain increased height by holding down W. Dash jumps",
		JUMPING4 = "offer little control, but can be used in midair. Touch the ground or",
		JUMPING5 = "a wall to use it again. Can be used to reach ledges or wall climb.",
		ATTACKING1 = "R T",
		ATTACKING2 = "Attack enemies in front of you with R or around you with T.",
		SHIELDING1 = "\u2193",
		SHIELDING2 = "Activate this just before getting hit to deflect it and become",
		SHIELDING3 = "invulnerable for ~0.5sec. Earliest reactivation can be done with",
		SHIELDING4 = "~0.1sec of invulnerability left. Can deflect all damage, but status",
		SHIELDING5 = "effects still apply. Empty activation or too early reactivation",
		SHIELDING6 = "leads to a short cooldown. Getting hang of the rhythm is key.",
		REWINDING1 = "F",
		REWINDING2 = "Backtrack recent movement and changes to health, attack energy",
>>>>>>> 2.03
		REWINDING3 = "and status effects while holding down the button.",
		REWINDING4 = "Costly, but very powerful if used wisely.";
	
	private Color titleColor;
	private Font titleFont;

	private Color selectionColor;
	private Color selectionActiveColor;
	private Font selectionFont;
	
	private Color controlsColor;
	private Font controlsFont;
	
	@SuppressWarnings("unused")
	private Player player;


	public MenuState(GameStateManager gsm) {
		super(gsm);
	}
	
	// has it's own override methods since it doesn't work in a very similar way to normal level states
	@Override
	public void init() {
		try {
			bg = new Background("/Backgrounds/menubg.gif", "/Backgrounds/menubg.gif", 1);
			bg.setVector(0.3, 0.0);
			
			titleColor = new Color(128, 0, 0);
			titleFont = new Font(
					"Century Gothic",
					Font.PLAIN,
					28
			);

			selectionColor = Color.RED;
			selectionActiveColor = Color.WHITE;
			selectionFont = new Font("Arial", Font.PLAIN, 12);
			
			controlsColor = Color.RED;
			controlsFont = new Font("Arial", Font.PLAIN, 10);
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		if(!JukeBox.isPlaying("menu")) {
			JukeBox.stopAllMusicTracks();
			JukeBox.adjustVolume("menu", -10);	//fail, can adjust until 0 volume, change later ???doesn't look like it does??? & needs to be outside if?
			JukeBox.loop("menu", 600, JukeBox.getFrames("menu") - 2200);
		}
		
		textMode = MENU;
		
		gsm.refreshGameStateTransferData();
	}

	@Override
	public void update() {
		bg.update();
		
		// check keys
		handleInput();
	}
	
	@Override
	public void draw(Graphics2D g) {
		FontMetrics metrics;
	    int textHalfWidth;
	    
	    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.NORMALOPACITY));
		
		// draw bg
		bg.draw(g);
		
		if(textMode == MENU) {
			g.setColor(titleColor);
			g.setFont(titleFont);
			
		    // set metrics with correct font size
			metrics = g.getFontMetrics(titleFont);
			
			// calculate string length with said metrics
		    textHalfWidth = (metrics.stringWidth(Support.GAMENAME)) / 2;
		    
		    // draw title
			g.drawString(Support.GAMENAME, (GamePanel.WIDTH / 2) - textHalfWidth, 70);
			
			// same thing with menu options
			g.setFont(selectionFont);
			metrics = g.getFontMetrics(selectionFont);
			for(int i = 0; i < options.length; i++) {
				if(i == currentChoice) {
					g.setColor(selectionActiveColor);
				}
				else {
					g.setColor(selectionColor);
				}
				
				// adds to the difficulty row
				if(i == 1) {
					String fullRow = "";
					if(Support.difficulty == 0) {
						fullRow = options[i] + "Casual (enemies do 0,25x damage)";
					}
					else if(Support.difficulty == 1) {
						fullRow = options[i] + "Easy (enemies do 0,5x damage)";
					}
					else if(Support.difficulty == 2) {
						fullRow = options[i] + "Normal (enemies do 1x damage)";
					}
					else if(Support.difficulty == 3) {
						fullRow = options[i] + "Hard (enemies do 2x damage)";
					}
					else if(Support.difficulty == 4) {
						fullRow = options[i] + "Suicide (enemies do 4x damage)";
					}
					textHalfWidth = (metrics.stringWidth(fullRow)) / 2;
					g.drawString(fullRow, (GamePanel.WIDTH / 2) - textHalfWidth, 140 + i * 15);
				}
				else if(i == 4) {
					String fullRow = "";
					if(GamePanel.SCALE == 1) {
						fullRow = options[i] + "Small (1x pixel size)";
					}
					else if(GamePanel.SCALE == 2) {
						fullRow = options[i] + "Normal (2x pixel size)";
					}
					else if(GamePanel.SCALE == 3) {
						fullRow = options[i] + "Large (3x pixel size)";
					}
					else if(GamePanel.SCALE == 4) {
						fullRow = options[i] + "Giant (4x pixel size)";
					}
					else if(GamePanel.SCALE == 5) {
						fullRow = options[i] + "American (5x pixel size)";
					}
					textHalfWidth = (metrics.stringWidth(fullRow)) / 2;
					g.drawString(fullRow, (GamePanel.WIDTH / 2) - textHalfWidth, 140 + i * 15);
				}
				// normally writes text
				else {
					textHalfWidth = (metrics.stringWidth(options[i])) / 2;
					g.drawString(options[i], (GamePanel.WIDTH / 2) - textHalfWidth, 140 + i * 15);
				}
			}
		}
		else if(textMode == CONTROLS) {
			g.setColor(controlsColor);
			g.setFont(controlsFont);
			
			g.drawString(MOVEMENT1, 5, 10);
			g.drawString(MOVEMENT2, 5, 20);
			g.drawString(JUMPING1, 5, 40);
			g.drawString(JUMPING2, 5, 50);
			g.drawString(JUMPING3, 5, 60);
			g.drawString(JUMPING4, 5, 70);
			g.drawString(JUMPING5, 5, 80);
			g.drawString(ATTACKING1, 5, 100);
			g.drawString(ATTACKING2, 5, 110);
			g.drawString(SHIELDING1, 5, 130);
			g.drawString(SHIELDING2, 5, 140);
			g.drawString(SHIELDING3, 5, 150);
			g.drawString(SHIELDING4, 5, 160);
			g.drawString(SHIELDING5, 5, 170);
			g.drawString(SHIELDING6, 5, 180);
			g.drawString(REWINDING1, 5, 200);
			g.drawString(REWINDING2, 5, 210);
			g.drawString(REWINDING3, 5, 220);
			g.drawString(REWINDING4, 5, 230);
		}
		else {
			g.setColor(titleColor);
			g.setFont(titleFont);
			
		    // set metrics with correct font size
			metrics = g.getFontMetrics(titleFont);
			
			String text = "Invalid textMode. " + textMode;
			
			// calculate string length with said metrics
		    textHalfWidth = (metrics.stringWidth(text)) / 2;
		    
		    // draw title
			g.drawString(text, (GamePanel.WIDTH / 2) - textHalfWidth, 70);
		}
	}
	
	private void select() {
		if(currentChoice == 0) {
			// start
<<<<<<< HEAD
=======
			GamePanel.playtime.setResetFullOnNextGameStateChange(true);
>>>>>>> 2.03
			changeGameState(GameStateManager.LEVEL1STATE, false);
		}
		if(currentChoice == 1) {
			// change difficulty
			Support.difficulty++;
			if(Support.difficulty > 4) Support.difficulty = 0;
			Support.setDifficultyValues();
		}
		if(currentChoice == 2) {
			// controls
			textMode = CONTROLS;
		}
		if(currentChoice == 3) {
			// credits
			changeGameState(GameStateManager.CREDITSSTATE, false);
		}
		if(currentChoice == 4) {
			// change size of JPanel
			GamePanel.SCALE++;
			if(GamePanel.SCALE > 5) GamePanel.SCALE = 1;
			GamePanel.SHOULDCHANGEJPANELSIZE = true; 
		}
		if(currentChoice == 5) {
			// quit
			System.exit(0);
		}
	}

	@Override
	public void handleInput() {
		if(textMode == MENU) {
			if(Keys.isPressed(Keys.ENTER)) {
				JukeBox.playWithRecommendedVolume("menuselect");
				select();
			}
			if(Keys.isPressed(Keys.UP)) {
				JukeBox.playWithRecommendedVolume("menuscroll");
				currentChoice--;
				if(currentChoice == -1) {
					currentChoice = options.length -1;
				}
			}
			if(Keys.isPressed(Keys.DOWN)) {
				JukeBox.playWithRecommendedVolume("menuscroll");
				currentChoice++;
				if(currentChoice == options.length) {
					currentChoice = 0;
				}
			}
		}
		else if(textMode == CONTROLS) {
			if(Keys.isPressed(Keys.ENTER)) textMode = MENU;
			if(Keys.isPressed(Keys.ESC)) textMode = MENU;
		}
		else {
			if(Keys.isPressed(Keys.ESC)) textMode = MENU;
		}
	}
}








