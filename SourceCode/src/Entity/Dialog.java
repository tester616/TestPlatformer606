package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Handlers.Content;
import Handlers.DialogConversation;
import Handlers.DialogText;
import Main.GamePanel;
import Support.Support;

public class Dialog {
	
	private BufferedImage[] dialogBoxImage;
	private BufferedImage[] currentDialogIcon;
	
	private BufferedImage[] iconPlayer;
	private BufferedImage[] iconBossGreenGem;
	private BufferedImage[] iconBossYellowGem;
	private BufferedImage[] iconBossRedGem;
	private BufferedImage[] iconBossPaleGem;
	private BufferedImage[] iconUnknown;
	
	private Color origColor;
	
	private Font titleFont;
	private Color titleColor;
	private String titleContent;
	
	private Font textFont;
	private Color textColor;
	private String textContent;
	
	private DialogConversation dc;
	
	private int currentLine;
	private boolean nextLineButton;
	
	private ArrayList<String> dialogTextRows;

	// how many pixels till box area begins from each direction
	private int marginTop;
	private int marginBottom;
	private int marginLeft;
	private int marginRight;
	
	// how many pixels of the box area that cannot be written on
	private int boxTextMarginTop;
	private int boxTextMarginBottom;
	private int boxTextMarginLeft;
	private int boxTextMarginRight;
	
	private int availableTextAreaX;
	@SuppressWarnings("unused")
	private int availableTextAreaY;
	
	private int titleContentExtraMargin;
	private int titleContentHeight;
	
	private int textContentExtraMargin;
	// height reserved for each line of text
	private int textContentHeight;
	
	private boolean active;
	private boolean allowManualProgress;
	private boolean allowAutomaticProgress;

	private long dialogLineAppearedTime;
	
	private long dialogLineAutoContinueTime;

	private String enterButtonContent;
	private String spaceButtonContent;
	
	private Font helpButtonFont;
	private Color helpButtonColor;

	private int helpButtonMargin;

	private boolean showDialog;
	
	private boolean newLineUpdateNeeded;
	
	// x from 0
	private int dialogIconXMargin;
	// y from top of dialog box
	private int dialogIconYMargin;

	
	public Dialog() {
		dialogTextRows = new ArrayList<String>();
		
		dialogBoxImage = Content.DialogBox[0];
		iconPlayer = Content.DialogIcon[0];
		iconBossGreenGem = Content.DialogIcon[1];
		iconBossYellowGem = Content.DialogIcon[2];
		iconBossRedGem = Content.DialogIcon[3];
		iconBossPaleGem = Content.DialogIcon[4];
		iconUnknown = Content.DialogIcon[5];
		
		marginTop = GamePanel.HEIGHT - dialogBoxImage[0].getHeight() + 3;
		marginBottom = 3;
		marginLeft = 3;
		marginRight = 3;
		
		boxTextMarginTop = 3;
		boxTextMarginBottom = 3;
		boxTextMarginLeft = 3;
		boxTextMarginRight = 3;
		
		availableTextAreaX = GamePanel.WIDTH - marginLeft - marginRight - boxTextMarginLeft - boxTextMarginRight;
		availableTextAreaY = GamePanel.HEIGHT - marginTop - marginBottom - boxTextMarginTop - boxTextMarginBottom;
		
		titleContentExtraMargin = 7;
		textContentExtraMargin = 7;
		
		dialogLineAutoContinueTime = 4500;
		
		active = true;
		allowManualProgress = true;
		allowAutomaticProgress = true;
		
		dialogIconXMargin = 10;
		// height of any icon works, since they are all same size
		dialogIconYMargin = -iconPlayer[0].getHeight();
		
		enterButtonContent = "Enter: Next";
		spaceButtonContent = "Space: Skip";
		
		helpButtonFont = new Font("Arial", Font.PLAIN, 10);
		helpButtonColor = Color.RED;
		
		setHelpButtonMargin();
	}
	
	public void startNewDialogById(int dialogId, int speakerBossTitle) {
		setDialogConversation(new DialogConversation(dialogId, speakerBossTitle));
		startDialog();
		setActive(true);
		setShowDialog(true);
		newLineUpdateNeeded = true;
	}

	public void setNextLine(boolean b) { nextLineButton = b; }
	
	public DialogConversation getDialogConversation() { return dc; }
	public void setDialogConversation(DialogConversation dc) { this.dc = dc; }
	
	private void setDialogIcon(int dialogIcon) {
		if(dialogIcon == DialogConversation.ICON_PLAYER) {
			currentDialogIcon = iconPlayer;
		}
		else if(dialogIcon == DialogConversation.ICON_BOSS_GREEN) {
			currentDialogIcon = iconBossGreenGem;
		}
		else if(dialogIcon == DialogConversation.ICON_BOSS_YELLOW) {
			currentDialogIcon = iconBossYellowGem;
		}
		else if(dialogIcon == DialogConversation.ICON_BOSS_RED) {
			currentDialogIcon = iconBossRedGem;
		}
		else if(dialogIcon == DialogConversation.ICON_BOSS_PALE) {
			currentDialogIcon = iconBossPaleGem;
		}
		else if(dialogIcon == DialogConversation.ICON_UNKNOWN) {
			currentDialogIcon = iconUnknown;
		}
	}
	
	private void loadCurrentLine() {
		if(isDialogOver()) {
			System.out.println("Can't load current line (" + currentLine + ") since dc has no such line.");
			return;
		}
		setDialogValues(dc.conversationContent.get(currentLine));
	}
	
	private void setDialogValues(DialogText dt) {
		if(dt == null) {
			System.out.println("Can't set new dialog text values since dt is null.");
			return;
		}
		
		titleFont = dt.titleFont;
		titleColor = dt.titleColor;
		titleContent = dt.titleContent;
		titleContentHeight = titleFont.getSize() + titleContentExtraMargin;
		
		textFont = dt.textFont;
		textColor = dt.textColor;
		textContent = dt.textContent;
		textContentHeight = textFont.getSize() + textContentExtraMargin;
		
		setDialogIcon(dt.icon);
	}
	
	public void setTitleFont(Font titleFont) { this.titleFont = titleFont; }
	public void setTitleColor(Color titleColor) { this.titleColor = titleColor; }
	public void setTitleContent(String titleContent) { this.titleContent = titleContent; }

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
		textContentHeight = textFont.getSize() + textContentExtraMargin;
	}
	public void setTextColor(Color textColor) { this.textColor = textColor; }
	public void setTextContent(String textContent) { this.textContent = textContent; }
	
	public void setCurrentLine(int currentLine) { this.currentLine = currentLine; }

	private void showNextLine() { currentLine++; }

	private void resetAutoContinue() { dialogLineAppearedTime = System.nanoTime(); }

	private void resetLine() { currentLine = 0; }

	public boolean getActive() { return active; }
	public void setActive(boolean active) { this.active = active; }
	
	public boolean allowManualProgress() { return allowManualProgress; }
	public void setAllowManualProgress(boolean allowManualProgress) { this.allowManualProgress = allowManualProgress; }

	public boolean allowAutomaticProgress() { return allowAutomaticProgress; }
	public void setAllowAutomaticProgress(boolean allowAutomaticProgress) { this.allowAutomaticProgress = allowAutomaticProgress; }
	
	public long getDialogLineAutoContinueTime() { return dialogLineAutoContinueTime; }
	public void setDialogLineAutoContinueTime(long dialogLineAutoContinueTime) { this.dialogLineAutoContinueTime = dialogLineAutoContinueTime; }

	public boolean shouldShowDialog() { return showDialog; }
	public void setShowDialog(boolean showDialog) { this.showDialog = showDialog; }
	
	public int getCurrentLine() { return currentLine; }
	
	// calculates margin for text giving information about what button presses do
	// call after determining the font before trying to display them
	public void setHelpButtonMargin() {
		Graphics2D g = (Graphics2D) new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB).getGraphics();
		FontMetrics metrics = g.getFontMetrics(helpButtonFont);
		int enterTextLength = metrics.stringWidth(enterButtonContent);
		int spaceTextLength = metrics.stringWidth(spaceButtonContent);
		if(enterTextLength > spaceTextLength) helpButtonMargin = enterTextLength;
		else helpButtonMargin = spaceTextLength;
	}
	
	public boolean isDialogOver() {
		if(dc == null) return true;
		return currentLine >= dc.conversationContent.size();
	}

	public void startDialog() {
		resetLine();
		loadCurrentLine();
		resetAutoContinue();
		updateDialogStatus();
	}

	private void updateDialogStatus() {
		if(isDialogOver()) {
			setActive(false);
		}
	}
	
	// call this when all variables are known to calculate suitable rows of text to draw
	public void fillDialogTextRows(Graphics2D g) {
		// FontMetrics can return length of a String
		FontMetrics metrics = g.getFontMetrics(textFont);
		String tempText = "";
		ArrayList<String> words = new ArrayList<String>(Support.getWords(textContent));
		
		dialogTextRows.clear();
		
		for(int a = 0; a < words.size(); a++) {
			if(availableTextAreaX >= metrics.stringWidth(tempText) + metrics.stringWidth(words.get(a))) {
				if(tempText.length() == 0) {
					tempText = tempText + words.get(a) + " ";
				}
				else {
					tempText = tempText.concat(words.get(a) + " ");
				}
			}
			else {
				dialogTextRows.add(tempText);
				tempText = "";
				a--;
			}
		}
		
		if(tempText.length() > 0) dialogTextRows.add(tempText);
		
		newLineUpdateNeeded = false;
	}
	
	private void progressToNextLine() {
		showNextLine();
		loadCurrentLine();
		resetAutoContinue();
		updateDialogStatus();
		if(!isDialogOver()) newLineUpdateNeeded = true;
	}

	public void update() {
		/*if(player.shouldUpdateDialogConversation()) {
			setDialogConversation(new DialogConversation(player.getUpdateDialogConversationID()));
			startDialog();
			setActive(true);
			player.setUpdateDialogConversation(false);
		}
		else */
		if(active) {
			if(dc == null) return;
			
			long passedTime = (System.nanoTime() - dialogLineAppearedTime) / 1000000;
			
			if(passedTime >= dialogLineAutoContinueTime && allowAutomaticProgress) {
				progressToNextLine();
			}

			if(nextLineButton && allowManualProgress) {
				progressToNextLine();
			}
		}
	}

	public void draw(Graphics2D g) {
		if(shouldShowDialog() && !isDialogOver()) {
			if(newLineUpdateNeeded) fillDialogTextRows(g);
			
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.TRANSPARENT));
			
			// textbox
			g.drawImage(
				dialogBoxImage[0],
				0,
				GamePanel.HEIGHT - dialogBoxImage[0].getHeight(),
				GamePanel.WIDTH,
				dialogBoxImage[0].getHeight(),
				null
			);
			
			// icon if any
			if(currentDialogIcon != null) {
				g.drawImage(
					currentDialogIcon[0],
					dialogIconXMargin,
					GamePanel.HEIGHT - dialogBoxImage[0].getHeight() + dialogIconYMargin,
					currentDialogIcon[0].getWidth(),
					currentDialogIcon[0].getHeight(),
					null
				);
			}

			origColor = g.getColor();
			
			g.setFont(helpButtonFont);
			g.setColor(helpButtonColor);
			// enter: next etc.
			if(allowManualProgress) {
				g.drawString(
					enterButtonContent,
					GamePanel.WIDTH - (marginRight + boxTextMarginRight + helpButtonMargin),
					marginTop - (boxTextMarginTop + textContentHeight + textContentHeight / 2)
				);
				g.drawString(
					spaceButtonContent,
					GamePanel.WIDTH - (marginRight + boxTextMarginRight + helpButtonMargin),
					marginTop - (boxTextMarginTop + textContentHeight / 2)
				);
			}
			
			g.setFont(titleFont);
			g.setColor(titleColor);
			// title
			g.drawString(
				titleContent,
				marginLeft + boxTextMarginLeft,
				marginTop + boxTextMarginTop + titleContentHeight / 2
			);
			
			g.setFont(textFont);
			g.setColor(textColor);
			// text
			int rowsDrawn = 0;
			for(String s : dialogTextRows) {
				g.drawString(
					s,
					marginLeft + boxTextMarginLeft,
					marginTop + boxTextMarginTop + titleContentHeight / 2 + textContentHeight + rowsDrawn * textContentHeight / 2
				);
				rowsDrawn++;
			}
			
			g.setColor(origColor);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
		}
	}
}










