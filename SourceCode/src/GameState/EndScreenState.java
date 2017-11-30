package GameState;

import java.awt.*;
import java.util.ArrayList;

import Handlers.Keys;
import Main.GamePanel;
import Support.Support;

public class EndScreenState extends GameState {
	
	private ArrayList<String> textRows;
	
	private Color fontColor;
	private Font endScreenTitleFont;
	private Font endScreenTextFont;
	
	private String endScreenTitle;
	private String endScreenText;
<<<<<<< HEAD
=======
	private String endScreenTime;
>>>>>>> 2.03

	private int textMarginTop;
	private int textMarginLeft;
	private int textMarginRight;
	private int textMarginTextFromTitle;
	private int textMarginTextRow;
	
	private boolean textRowsCalculated;
	
	public EndScreenState(GameStateManager gsm) {
		super(gsm);
	}
	
	@Override
	protected void setLevelSpecificValues() {
		
	}
	
	@Override
	public void init() {
		textRows = new ArrayList<String>();
		
		fontColor = Color.RED;
		endScreenTitleFont = new Font("Century Gothic", Font.PLAIN, 28);
		//endScreenTextFont = new Font("Arial", Font.PLAIN, 12);
		endScreenTextFont = new Font("Arial", Font.PLAIN, 10);
		
		endScreenTitle = Support.endingTitle;
		endScreenText = Support.endingText;

		textMarginTop = 10;
		textMarginLeft = 10;
		textMarginRight = 10;
		textMarginTextFromTitle = 15;
		textMarginTextRow = 3;
		
		
	}
	
	@Override
	public void update() {
		handleInput();
		
		if(escapeButton) {
			changeGameState(GameStateManager.MENUSTATE, false);
		}
		
		if(enterButton) {
			changeGameState(GameStateManager.MENUSTATE, false);
		}
	}
	
	@Override
	public void draw(Graphics2D g) {
		Color origColor = g.getColor();
		Font origFont = g.getFont();
		FontMetrics metrics;
		
		metrics = g.getFontMetrics(endScreenTitleFont);
		int titleHeight = endScreenTitleFont.getSize();
	    int titleHalfWidth = (metrics.stringWidth(endScreenTitle)) / 2;
	    
	    metrics = g.getFontMetrics(endScreenTextFont);
	    int textHeight = endScreenTextFont.getSize();
	    if(!textRowsCalculated) {
	    	int rowSize = GamePanel.WIDTH - textMarginLeft - textMarginRight;
	    	textRows = Support.getSuitableTextRows(endScreenText, rowSize, metrics);
		    textRowsCalculated = true;
	    }
	    
		//metrics = g.getFontMetrics(waitingFont);
	    //int waitingWidth = metrics.stringWidth(waitingText);
	    //int waitingHeight = smallFont.getSize();

		// draw black box
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
		
		// draw title
		g.setColor(fontColor);
		g.setFont(endScreenTitleFont);
		g.drawString(endScreenTitle, GamePanel.WIDTH / 2 - titleHalfWidth, textMarginTop + titleHeight);
		
		// draw text
		g.setFont(endScreenTextFont);
		int levelBriefingDrawY;
		for(int i = 0; i < textRows.size(); i++) {
			levelBriefingDrawY = textMarginTop + titleHeight + textMarginTextFromTitle + textHeight + 
				textHeight * i + textMarginTextRow * i;
			g.drawString(textRows.get(i), textMarginLeft, levelBriefingDrawY);
		}
		
<<<<<<< HEAD
		// draw loading text
		//g.setFont(waitingFont);
		//g.drawString(waitingText, GamePanel.WIDTH - waitingWidth - 10, GamePanel.HEIGHT - waitingHeight / 2 - 10);
=======
		// draw playtime if on
		if(GamePanel.playtime.isDrawTime()) {
			endScreenTime = GamePanel.playtime.getcHours() + "h " + GamePanel.playtime.getcMinutes() + "min " + GamePanel.playtime.getcSeconds() + "." + GamePanel.playtime.getcMilliseconds() + "sec";
		    int timeHalfWidth = (metrics.stringWidth(endScreenTime)) / 2;
		    
			g.drawString(endScreenTime, GamePanel.WIDTH / 2 - timeHalfWidth, GamePanel.HEIGHT - textHeight / 2 - 20);
		}
>>>>>>> 2.03
		
		g.setColor(origColor);
		g.setFont(origFont);
	}
	
	@Override
	public void handleInput() {
		escapeButton = Keys.isPressed(Keys.ESC);
		enterButton = Keys.isPressed(Keys.ENTER);
	}
}









