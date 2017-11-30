package Support;

import java.awt.FontMetrics;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import Handlers.DialogConversation;

public class Support {
	
	// menu screen name
	public static final String GAMENAME = "Platformer 606";
	
	// opacity constants
	public static final float REWINDINGOPACITY = (float) 0.15;
	public static final float TRANSPARENT = (float) 0.5;
	public static final float DIZZYOPACITY = (float) 0.07;
	public static final float NORMALOPACITY = (float) 1.0;
	public static final float INVISIBLEOPACITY = (float) 0.0;
	
	// color mode constants
	public static final int COLORED = 0;
	public static final int MONOCHROME = 1;
	
	// size of tiles
	public static final int TILESIZE = 30;
	
	// difficulty identifiers and constants
	public static final int DIFFICULTY_CASUAL = 0;
	public static final int DIFFICULTY_EASY = 1;
	public static final int DIFFICULTY_NORMAL = 2;
	public static final int DIFFICULTY_HARD = 3;
	public static final int DIFFICULTY_SUICIDE = 4;
	public static final double MULTIPLIER_DAMAGE_CASUAL = 0.25;
	public static final double MULTIPLIER_DAMAGE_EASY = 0.5;
	public static final double MULTIPLIER_DAMAGE_NORMAL = 1.0;
	public static final double MULTIPLIER_DAMAGE_HARD = 2.0;
	public static final double MULTIPLIER_DAMAGE_SUICIDE = 4.0;
	public static final double MULTIPLIER_SPAWNRATE_CASUAL = 1.8;
	public static final double MULTIPLIER_SPAWNRATE_EASY = 1.2;
	public static final double MULTIPLIER_SPAWNRATE_NORMAL = 1.0;
	public static final double MULTIPLIER_SPAWNRATE_HARD = 0.8;
	public static final double MULTIPLIER_SPAWNRATE_SUICIDE = 0.6;
	public static final double MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_CASUAL = 1.5;
	public static final double MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_EASY = 1.2;
	public static final double MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_NORMAL = 1.0;
	public static final double MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_HARD = 0.95;
	public static final double MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_SUICIDE = 0.9;
	public static final int EXTRALIVES_CASUAL = 2;
	public static final int EXTRALIVES_EASY = 1;
	public static final int EXTRALIVES_NORMAL = 0;
	public static final int EXTRALIVES_HARD = 0;
	public static final int EXTRALIVES_SUICIDE = 0;

	public static int difficulty = DIFFICULTY_NORMAL;
	public static double difficultyDamageMultiplier = MULTIPLIER_DAMAGE_NORMAL;
	public static double difficultySpawnrateMultiplier = MULTIPLIER_SPAWNRATE_NORMAL;
	public static double difficultyBossBottomPortalExtraCooldownMultiplier = MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_NORMAL;
	public static int difficultyExtraLives = EXTRALIVES_NORMAL;
	
	// given to pretty much anything, increased after each assignment by 1
	public static int id = 1;
	
	// draw opacity values
	public static float surroundingsOpacity = (float) 1.0;
	public static float playerOpacity = (float) 1.0;
	
	// current color mode
	public static int surroundingsColorMode = COLORED;
	
	// cheats related stuff
	// cheats that toggle with buttons like o & p, mainly for testing purposes
	public static boolean quickCheats = false;
	// cheats you have to input a code for after pressing F1
	public static boolean inputCheats = true;
	public static boolean cheatInputMode = false;
	public static String currentCheatCode = "";
	public static final String CHEAT_INVULNERABILITY = "dargonknikket";
<<<<<<< HEAD
	public static final String CHEAT_INFINITEJUMP = "hoppapojke";
	public static final String CHEAT_INFINITEDASHJUMP = "mobilemidas";
	public static final String CHEAT_KILLALLENEMIES = "smugglypuff";
	public static final String CHEAT_MANYLIVES = "nekomimi";
	public static final String CHEAT_REGENHEALTH = "modernmilitaryshooter";
	public static final String CHEAT_REGENATTACKENERGY = "atatata";
	public static final String CHEAT_REGENREWINDENERGY = "chronostheexplorer";
	public static final String CHEAT_ATTACKRANGE = "brarange";
	public static final String CHEAT_ATTACKDAMAGE = "bradps";
	public static final String CHEAT_SHOWHITBOXES = "dendarangen";
	public static final String CHEAT_SHOWFPS = "butmuhcinematicexperience";
	public static final String CHEAT_SUICIDE = "honorabrusoduko";
	public static final String CHEAT_LEVEL_1 = "hanteleporterar1";
	public static final String CHEAT_LEVEL_2 = "hanteleporterar2";
	public static final String CHEAT_LEVEL_3 = "hanteleporterar3";
	public static final String CHEAT_LEVEL_4 = "hanteleporterar4";
	public static final String CHEAT_LEVEL_5 = "hanteleporterar5";
	public static final String CHEAT_LEVEL_6 = "hanteleporterar6";
	public static final String CHEAT_LEVEL_7 = "hanteleporterar7";
=======
	public static final String CHEAT_INFINITEJUMP = "gravityop";
	public static final String CHEAT_INFINITEDASHJUMP = "mobilemidas";
	public static final String CHEAT_INFINITEPARRY = "parryking";
	public static final String CHEAT_KILLALLENEMIES = "thewall";
	public static final String CHEAT_STOPENEMYSPAWNING = "wandererofthegreatmists";
	public static final String CHEAT_MANYLIVES = "nekomimi";
	public static final String CHEAT_REGENHEALTH = "modernmilitaryshooter";
	public static final String CHEAT_REGENATTACKENERGY = "happyboi";
	public static final String CHEAT_REGENREWINDENERGY = "chronostheexplorer";
	public static final String CHEAT_ATTACKRANGE = "meleehero";
	public static final String CHEAT_ATTACKDAMAGE = "oneshockball";
	public static final String CHEAT_SHOWHITBOXES = "notonmyscreen";
	public static final String CHEAT_SHOWFPS = "butmuhcinematicexperience";
	public static final String CHEAT_SUICIDE = "nani";
	public static final String CHEAT_BOSSRUSH = "1v1finaldestination";
	public static final String CHEAT_LEVEL_1 = "episode1";
	public static final String CHEAT_LEVEL_2 = "episode2";
	public static final String CHEAT_LEVEL_3 = "episode24";
	public static final String CHEAT_LEVEL_4 = "episode4";
	public static final String CHEAT_LEVEL_5 = "episode5";
	public static final String CHEAT_LEVEL_6 = "episode6";
	public static final String CHEAT_LEVEL_7 = "episode7";
	public static final String CHEAT_LEVEL_EASTEREGG = "episode3";
>>>>>>> 2.03
	public static final String CHEATHELP_INFORMATION_1 = "Cheat input mode on";
	public static final String CHEATHELP_INFORMATION_2 = "F1 to close";
	public static final String CHEATHELP_INFORMATION_3 = "Enter to submit";
	// cheats status
	// invulnerability is taken care of with normal invulnerable boolean in Player
	//public static boolean cheatInvulnerability;
	public static boolean cheatInfiniteJump;
	public static boolean cheatInfiniteDashJump;
<<<<<<< HEAD
=======
	public static boolean cheatInfiniteParry;
	public static boolean cheatStopEnemySpawning;
>>>>>>> 2.03
	public static boolean cheatHealthRegen;
	public static boolean cheatAttackRegen;
	public static boolean cheatRewindRegen;
	public static boolean cheatAttackRange;
	public static boolean cheatAttackDamage;
	public static boolean cheatShowHitboxes;
	public static boolean cheatShowFps;
<<<<<<< HEAD
=======
	public static boolean cheatBossRush;
>>>>>>> 2.03
	
	// enemy identifiers
	public static final int NOTYPE = 0;
	public static final int DARKMIST = 1;
	public static final int HALFMOON = 2;
	public static final int MINE = 3;
	public static final int SENTRY = 4;
	public static final int SHIELD = 5;
	public static final int SPIKES = 6;
	public static final int SQUARE = 7;
	public static final int STAR = 8;
	public static final int TRIANGLE = 9;
	
	public static final int ENEMYTYPES = 9;
	
	// enemy costs
	public static final int COST_DARKMIST = 3;
	public static final int COST_HALFMOON = 2;
	public static final int COST_MINE = 1;
	public static final int COST_SENTRY = 3;
	public static final int COST_SHIELD = 4;
	public static final int COST_SPIKES = 1;
	public static final int COST_SQUARE = 4;
	public static final int COST_STAR = 5;
	public static final int COST_TRIANGLE = 1;
	
	// dialog name boss goes by, starts as ???
	public static int bossSpeakerStatus = DialogConversation.SPEAKER_BOSS_UNKNOWN;
	
	public static final String ENDING_TITLE_KILL = "Bloodthirsty ending";
	public static final String ENDING_TITLE_SPARE = "Merciful ending";
	public static final String ENDING_TEXT_KILL = "He deserved it. His hideout is quite impressive though. Seems like a waste to just destroy or ignore it. With " +
		"some tinkering, maybe it would serve as a good course for a brand new sport aimed at prisoners heading for execution?";
<<<<<<< HEAD
	public static final String ENDING_TEXT_SPARE = "Who dared program compassion into it? Prepare every single one of our finest intelligence experts for " +
		"investigation! All culprits are wanted alive if possible. Our original manhunt is hereby halted until further notice.";
=======
	public static final String ENDING_TEXT_SPARE = "Who dared program compassion into it? I want intelligence unit X6 gathered for an immediate meeting NOW! Contacting our colonies "
			+ "on D12 is also a top priority! Critical elements are to remain on standby until further notice!";
>>>>>>> 2.03

	public static String endingTitle = "";
	public static String endingText = "";

	public static int randInt(int min, int max) {
	    Random rand = new Random();
	    return (rand.nextInt((max - min) + 1) + min);
	}

	public static double getDoubleWithXExtraDecimals(int xInt, int decimalAmount) {
		double xDouble = (double) xInt;
		double divisor = Math.pow(10.0, (double) decimalAmount);
		double xDoubleDecimal = xDouble / divisor;
	    return xDoubleDecimal;
	}

	public static double getDoubleWithXExtraDecimals(double xDouble, int decimalAmount) {
		double divisor = Math.pow(10.0, (double) decimalAmount);
		double xDoubleDecimal = xDouble / divisor;
	    return xDoubleDecimal;
	}

	public static double pythGetXYMultiplier(double xDir, double yDir, double knockbackStrength) {
		// (xDir * x)^2 + (yDir * x)^2 = knockbackStrength^2 solved for x
		return knockbackStrength / (Math.sqrt((Math.pow(Math.abs(xDir), 2) + Math.pow(Math.abs(yDir), 2))));
	}

	public static double getMovementDirection(double dx, double dy) {
		if(dx == 0) {
			System.out.println("Error in getMovementDirection, division with 0");
			return 999999.606;
		}
		return dy / dx;
	}
	
	public static ArrayList<String> getSuitableTextRows(String textContent, int rowSize, FontMetrics metrics) {
		String tempText = "";
		ArrayList<String> words = new ArrayList<String>(Support.getWords(textContent));
		ArrayList<String> textRows = new ArrayList<String>();
		
		for(int a = 0; a < words.size(); a++) {
			if(rowSize >= metrics.stringWidth(tempText) + metrics.stringWidth(words.get(a))) {
				if(tempText.length() == 0) {
					tempText = tempText + words.get(a) + " ";
				}
				else {
					tempText = tempText.concat(words.get(a) + " ");
				}
			}
			else {
				textRows.add(tempText);
				tempText = "";
				a--;
			}
		}
		
		if(tempText.length() > 0) textRows.add(tempText);
		
		return textRows;
	}

	public static ArrayList<String> getWords(String s) {
		String textLeft = s;
		ArrayList<String> words = new ArrayList<String>();
		String currentWord = "";
		char currentChar;
		
		for(int textPosition = 0; textPosition < textLeft.length(); textPosition++) {
			currentChar = textLeft.charAt(textPosition);
			// char is not space, continue word
			if(currentChar != ' ') {
				if(currentWord.length() == 0) {
					currentWord = currentWord + currentChar;
				}
				else {
					currentWord = currentWord.concat("" + currentChar);
				}
			}
			// char is space, word ends
			else {
				words.add(currentWord);
				currentWord = "";
				// textLeft is everything starting one position after the space char was found !!!CHECK IF IT ERRORS WITH NOTHING LEFT OUT OF BOUNDS!!!
				textLeft = textLeft.substring(textPosition + 1);
				// reset to -1, which will be 0 after ++
				textPosition = -1;
			}
			
		}
		
		if(currentWord.length() > 0) words.add(currentWord);
		
		return words;
	}
	
	public static ArrayList<Double> getCircleXYPos(double r, int degrees) {
		ArrayList<Double> coordinates = new ArrayList<Double>();
		double x = r * Math.cos(Math.toRadians(degrees));
		double y = r * Math.sin(Math.toRadians(degrees));
		
		coordinates.add(x);
		coordinates.add(y);
		
		return coordinates;
	}
	
	public static ArrayList<Double> getCircleXYPos(double r, double radians) {
		ArrayList<Double> coordinates = new ArrayList<Double>();
		double x = r * Math.cos(radians);
		double y = r * Math.sin(radians);
		
		coordinates.add(x);
		coordinates.add(y);
		
		return coordinates;
	}
	
	public static float getTimeDependantFloat(
		float startValue,
		float endValue,
		long transitionTotalTime,
		long transitionPassedTime
	) {
		double transitionPassedTimeDecimal = (double) transitionPassedTime / (double) transitionTotalTime;
		if(transitionPassedTimeDecimal > 1.0) transitionPassedTimeDecimal = 1.0;
		return (float) ((double) startValue + ((double) endValue - (double) startValue) * transitionPassedTimeDecimal);
	}
	
	public static double getTimeDependantDouble(
		double startValue,
		double endValue,
		long transitionTotalTime,
		long transitionPassedTime
	) {
		double transitionPassedTimeDecimal = transitionPassedTime / transitionTotalTime;
		if(transitionPassedTimeDecimal > 1.0) transitionPassedTimeDecimal = 1.0;
		return startValue + (endValue - startValue) * transitionPassedTimeDecimal;
	}
	
	public static void setDifficultyValues() {
		if(difficulty == DIFFICULTY_CASUAL) {
			difficultyDamageMultiplier = MULTIPLIER_DAMAGE_CASUAL;
			difficultySpawnrateMultiplier = MULTIPLIER_SPAWNRATE_CASUAL;
			difficultyBossBottomPortalExtraCooldownMultiplier = MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_CASUAL;
			difficultyExtraLives = EXTRALIVES_CASUAL;
		}
		if(difficulty == DIFFICULTY_EASY) {
			difficultyDamageMultiplier = MULTIPLIER_DAMAGE_EASY;
			difficultySpawnrateMultiplier = MULTIPLIER_SPAWNRATE_EASY;
			difficultyBossBottomPortalExtraCooldownMultiplier = MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_EASY;
			difficultyExtraLives = EXTRALIVES_EASY;
		}
		if(difficulty == DIFFICULTY_NORMAL) {
			difficultyDamageMultiplier = MULTIPLIER_DAMAGE_NORMAL;
			difficultySpawnrateMultiplier = MULTIPLIER_SPAWNRATE_NORMAL;
			difficultyBossBottomPortalExtraCooldownMultiplier = MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_NORMAL;
			difficultyExtraLives = EXTRALIVES_NORMAL;
		}
		if(difficulty == DIFFICULTY_HARD) {
			difficultyDamageMultiplier = MULTIPLIER_DAMAGE_HARD;
			difficultySpawnrateMultiplier = MULTIPLIER_SPAWNRATE_HARD;
			difficultyBossBottomPortalExtraCooldownMultiplier = MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_HARD;
			difficultyExtraLives = EXTRALIVES_HARD;
		}
		if(difficulty == DIFFICULTY_SUICIDE) {
			difficultyDamageMultiplier = MULTIPLIER_DAMAGE_SUICIDE;
			difficultySpawnrateMultiplier = MULTIPLIER_SPAWNRATE_SUICIDE;
			difficultyBossBottomPortalExtraCooldownMultiplier = MULTIPLIER_BOSS_BOTTOMPORTALEXTRACOOLDOWN_SUICIDE;
			difficultyExtraLives = EXTRALIVES_SUICIDE;
		}
	}
	
	public URL getStringURL(String s) {
		return getClass().getResource(s);
	}
}










