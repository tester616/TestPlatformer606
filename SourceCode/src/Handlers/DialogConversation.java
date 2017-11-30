package Handlers;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import Support.Support;

public class DialogConversation {
	
	private int conversationID;
	
	public static final int CONVERSATION_BOSS_PREFIGHT_NORMAL = 0;
	public static final int CONVERSATION_BOSS_PREFIGHT_CASUAL = 1;
	public static final int CONVERSATION_BOSS_PREFIGHT_SUICIDE = 2;
	public static final int CONVERSATION_BOSS_MIDFIGHT_FINALATTACKSTART = 3;
	public static final int CONVERSATION_BOSS_MIDFIGHT_FINALATTACKCAUGHT = 4;
	public static final int CONVERSATION_BOSS_MIDFIGHT_FINALATTACKGROW = 5;
	public static final int CONVERSATION_BOSS_MIDFIGHT_YELLOWGEMSTART = 6;
	public static final int CONVERSATION_BOSS_MIDFIGHT_REDGEMSTART = 7;
	public static final int CONVERSATION_BOSS_POSTFIGHT_FINALATTACKDESTRUCTION = 8;
	public static final int CONVERSATION_BOSS_POSTFIGHT_DEATHBED_1 = 9;
	public static final int CONVERSATION_BOSS_POSTFIGHT_DEATHBED_2 = 10;
	public static final int CONVERSATION_BOSS_POSTFIGHT_DEATHBED_3 = 11;
	public static final int CONVERSATION_BOSS_POSTFIGHT_DEATHBED_4 = 12;
	public static final int CONVERSATION_BOSS_POSTFIGHT_ESCAPED_1 = 13;
	public static final int CONVERSATION_BOSS_POSTFIGHT_ESCAPED_2 = 14;
	public static final int CONVERSATION_BOSS_POSTFIGHT_KILLED_1 = 15;
	public static final int CONVERSATION_BOSS_POSTFIGHT_KILLED_2 = 16;

	public static final int SPEAKER_PLAYER = 0;
	public static final int SPEAKER_BOSS_UNKNOWN = 1;
	public static final int SPEAKER_BOSS_KNOWN = 2;
	public static final int SPEAKER_UNKNOWN = 3;
	
	private int currentBossSpeaker;
	
	public static final int ICON_PLAYER = 0;
	public static final int ICON_BOSS_GREEN = 1;
	public static final int ICON_BOSS_YELLOW = 2;
	public static final int ICON_BOSS_RED = 3;
	public static final int ICON_BOSS_PALE = 4;
	public static final int ICON_UNKNOWN = 5;
	
	public static final int SPECIALTAG_NONE = 0;
	public static final int SPECIALTAG_BOSSFADEIN = 1;
	public static final int SPECIALTAG_GAMEFADEOUT = 2;
	
	public ArrayList<DialogText> conversationContent;

	private String titleContentBossUnknown;
	private String titleContentBossKnown;
	private String titleContentPlayer;
	private String titleContentUnknown;
	private Font titleFontBoss;
	private Font titleFontPlayer;
	private Font titleFontUnknown;
	private Color titleColorBoss;
	private Color titleColorPlayer;
	private Color titleColorUnknown;
	
	private Color textColorBoss;
	private Color textColorPlayer;
	private Color textColorUnknown;
	private Font textFontBoss;
	private Font textFontPlayer;
	private Font textFontUnknown;
	
	
	public DialogConversation(int conversationID, int bossSpeakerTitle) {
		conversationContent = new ArrayList<DialogText>();
		
		this.conversationID = conversationID;

		titleContentBossUnknown = "???";
		titleContentBossKnown = "Lazahil";
		titleContentPlayer = "SeekBot 2.4 #606";
		titleContentUnknown = "???";
		titleFontBoss = new Font("Arial", Font.PLAIN, 12);
		titleFontPlayer = new Font("Arial", Font.PLAIN, 12);
		titleFontUnknown = new Font("Arial", Font.PLAIN, 12);
		titleColorBoss = Color.RED;
		titleColorPlayer = Color.RED;
		titleColorUnknown = Color.RED;
		
		textFontBoss = new Font("Arial", Font.PLAIN, 10);
		textFontPlayer = new Font("Arial", Font.PLAIN, 10);
		textFontUnknown = new Font("Arial", Font.PLAIN, 10);
		textColorBoss = Color.BLACK;
		textColorPlayer = Color.BLACK;
		textColorUnknown = Color.BLACK;
		
		currentBossSpeaker = bossSpeakerTitle;
		
		populateConversation();
	}
	
	private void populateConversation() {
		if(conversationID == CONVERSATION_BOSS_PREFIGHT_NORMAL) {
			populatePrefightCommonConversation();
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_NORMAL_1, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_BOSSFADEIN);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_NORMAL_2, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_PREFIGHT_CASUAL) {
			populatePrefightCommonConversation();
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_CASUAL_1, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_CASUAL_2, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_CASUAL_3, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_CASUAL_4, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_CASUAL_5, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_CASUAL_6, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_BOSSFADEIN);
		}
		else if(conversationID == CONVERSATION_BOSS_PREFIGHT_SUICIDE) {
			populatePrefightCommonConversation();
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_SUICIDE_1, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_SUICIDE_2, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_SUICIDE_3, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_SUICIDE_4, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
			Support.bossSpeakerStatus = SPEAKER_BOSS_KNOWN;
<<<<<<< HEAD
=======
			currentBossSpeaker = Support.bossSpeakerStatus;
>>>>>>> 2.03
			createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_SUICIDE_5, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_BOSSFADEIN);
		}
		else if(conversationID == CONVERSATION_BOSS_MIDFIGHT_YELLOWGEMSTART) {
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_YELLOWGEM_START, currentBossSpeaker, ICON_BOSS_YELLOW, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_MIDFIGHT_REDGEMSTART) {
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_REDGEM_START, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_MIDFIGHT_FINALATTACKSTART) {
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_1, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_2, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_3, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_MIDFIGHT_FINALATTACKCAUGHT) {
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_4, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_5, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_6, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_7, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_8, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_9, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_MIDFIGHT_FINALATTACKGROW) {
			createConversationLine(DialogLineDatabase.BOSS_MIDFIGHT_FINALATTACK_10, currentBossSpeaker, ICON_BOSS_RED, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_FINALATTACKDESTRUCTION) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_1, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_DEATHBED_1) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_2, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_3, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_4, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_5, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_DEATHBED_2) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_6, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_7, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_8, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_9, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_10, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_DEATHBED_3) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_11, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_12, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_DEATHBED_4) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_13, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_ESCAPED_1) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_14, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_15, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_ESCAPED_2) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_16, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_17, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_18, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_19, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_20, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_21, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_22, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_23, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_GAMEFADEOUT);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_KILLED_1) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_24, currentBossSpeaker, ICON_BOSS_PALE, SPECIALTAG_NONE);
		}
		else if(conversationID == CONVERSATION_BOSS_POSTFIGHT_KILLED_2) {
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_25, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_26, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_27, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_28, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_NONE);
			createConversationLine(DialogLineDatabase.BOSS_POSTFIGHT_29, SPEAKER_UNKNOWN, ICON_UNKNOWN, SPECIALTAG_GAMEFADEOUT);
		}
	}
	
	private void populatePrefightCommonConversation() {
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_1, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.PLAYER_PREFIGHT_1, SPEAKER_PLAYER, ICON_PLAYER, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_2, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_3, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_4, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_5, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_6, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.PLAYER_PREFIGHT_2, SPEAKER_PLAYER, ICON_PLAYER, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_7, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_8, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_9, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_10, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_11, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.PLAYER_PREFIGHT_3, SPEAKER_PLAYER, ICON_PLAYER, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_12, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
		createConversationLine(DialogLineDatabase.BOSS_PREFIGHT_13, currentBossSpeaker, ICON_BOSS_GREEN, SPECIALTAG_NONE);
	}
	
	private void createConversationLine(String line, int speaker, int icon, int specialTag) {
		if(speaker == SPEAKER_PLAYER) {
			conversationContent.add(new DialogText(
				titleFontPlayer,
				titleColorPlayer,
				titleContentPlayer,
				textFontPlayer,
				textColorPlayer,
				line,
				icon,
				specialTag
			));
		}
		else if(speaker == SPEAKER_BOSS_UNKNOWN) {
			conversationContent.add(new DialogText(
				titleFontBoss,
				titleColorBoss,
				titleContentBossUnknown,
				textFontBoss,
				textColorBoss,
				line,
				icon,
				specialTag
			));
		}
		else if(speaker == SPEAKER_BOSS_KNOWN) {
			conversationContent.add(new DialogText(
				titleFontBoss,
				titleColorBoss,
				titleContentBossKnown,
				textFontBoss,
				textColorBoss,
				line,
				icon,
				specialTag
			));
		}
		else if(speaker == SPEAKER_UNKNOWN) {
			conversationContent.add(new DialogText(
				titleFontUnknown,
				titleColorUnknown,
				titleContentUnknown,
				textFontUnknown,
				textColorUnknown,
				line,
				icon,
				specialTag
			));
		}
		else {
			System.out.println("Error in createConversationLine, speaker (" + speaker + ") matched no id.");
		}
	}
}
