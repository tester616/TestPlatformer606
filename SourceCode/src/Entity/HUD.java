package Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import Main.GamePanel;
import Support.Support;

public class HUD {
	
	private Player player;
	
	private BufferedImage barsImage;
	private BufferedImage dizzyImage;
	private BufferedImage confusedImage;
	private BufferedImage sealedImage;
	private BufferedImage clockImage;
	private Font font;
	private Color origColor;
	
	private final int BARS_IMAGE_X = 0,
		BARS_IMAGE_Y = 10,
		BARS_TEXT_HEALTH_X = 20,
		BARS_TEXT_HEALTH_Y = 25,
		BARS_TEXT_ATTACKENERGY_X = 20,
		BARS_TEXT_ATTACKENERGY_Y = 46,
		BARS_TEXT_REWINDENERGY_X = 20,
		BARS_TEXT_REWINDENERGY_Y = 67,
		
		STATUSEFFECT_DIZZY_ICON_X = 80,
		STATUSEFFECT_DIZZY_ICON_Y = 10,
		STATUSEFFECT_DIZZY_TIME_X = 91,
		STATUSEFFECT_DIZZY_TIME_Y = 53,
		
		STATUSEFFECT_CONFUSED_ICON_X = 116,
		STATUSEFFECT_CONFUSED_ICON_Y = 10,
		STATUSEFFECT_CONFUSED_TIME_X = 127,
		STATUSEFFECT_CONFUSED_TIME_Y = 53,
		
		STATUSEFFECT_SEALED_ICON_X = 152,
		STATUSEFFECT_SEALED_ICON_Y = 10,
		STATUSEFFECT_SEALED_TIME_X = 163,
		STATUSEFFECT_SEALED_TIME_Y = 53,
		
		INTERSECTION_MARGIN_EXTRA = 15;
		
	private int playerIntersectionExtraY;
	
	private double clockMinuteRewindSpeed;
	private double clockHourRewindSpeed;
	
	private double clockFullCircleLength;
	
	private double clockMinuteArmLength;
	private double clockMinuteRadianPosition;
	private double clockMinuteXPosition;
	private double clockMinuteYPosition;
	
	private double clockHourArmLength;
	private double clockHourRadianPosition;
	private double clockHourXPosition;
	private double clockHourYPosition;
	
	private boolean initialRewindFrame;
	
	public HUD(Player p) {
		player = p;
		try {
			barsImage = ImageIO.read(
					getClass().getResourceAsStream(
						"/HUD/hud.gif"
					)
			);
			font = new Font("arial", Font.PLAIN, 14);
			
			dizzyImage = ImageIO.read(
					getClass().getResourceAsStream(
						"/HUD/hudicondizzy.gif"
					)
			);
			
			confusedImage = ImageIO.read(
					getClass().getResourceAsStream(
						"/HUD/hudiconconfused.gif"
					)
			);
			
			sealedImage = ImageIO.read(
					getClass().getResourceAsStream(
						"/HUD/hudiconsealed.gif"
					)
			);
			
			clockImage = ImageIO.read(
					getClass().getResourceAsStream(
						"/HUD/clockframe.gif"
					)
			);
			
			clockMinuteRewindSpeed = -0.02;
			clockHourRewindSpeed = clockMinuteRewindSpeed / 12;
			clockFullCircleLength = 2 * Math.PI;
			clockMinuteArmLength = 75.0;
			clockHourArmLength = 30.0;
			
			playerIntersectionExtraY = GamePanel.HEIGHT - barsImage.getHeight() - BARS_IMAGE_Y * 2;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkPlayerIntersection(Rectangle r1) {
		Rectangle r2 = player.getRectangle();
		return r1.intersects(r2);
	}
	
	private boolean checkHudIntersectionWithPlayer() {
		boolean barsIntersection;
		boolean dizzyIntersection;
		boolean confusedIntersection;
		boolean sealedIntersection;
		
		barsIntersection = checkPlayerIntersection(new Rectangle(
			BARS_IMAGE_X,
			BARS_IMAGE_Y,
			barsImage.getWidth(),
			barsImage.getHeight()
		));
		
		if(player.isDizzy()) {
			dizzyIntersection = checkPlayerIntersection(new Rectangle(
				STATUSEFFECT_DIZZY_ICON_X - INTERSECTION_MARGIN_EXTRA,
				STATUSEFFECT_DIZZY_ICON_Y - INTERSECTION_MARGIN_EXTRA,
				dizzyImage.getWidth() + INTERSECTION_MARGIN_EXTRA * 2,
				dizzyImage.getHeight() + INTERSECTION_MARGIN_EXTRA * 2
			));
		}
		else dizzyIntersection = false;
		
		if(player.isConfused()) {
			confusedIntersection = checkPlayerIntersection(new Rectangle(
				STATUSEFFECT_CONFUSED_ICON_X - INTERSECTION_MARGIN_EXTRA,
				STATUSEFFECT_CONFUSED_ICON_Y - INTERSECTION_MARGIN_EXTRA,
				confusedImage.getWidth() + INTERSECTION_MARGIN_EXTRA * 2,
				confusedImage.getHeight() + INTERSECTION_MARGIN_EXTRA * 2
			));
		}
		else confusedIntersection = false;
		
		if(player.isSealed()) {
			sealedIntersection = checkPlayerIntersection(new Rectangle(
				STATUSEFFECT_SEALED_ICON_X - INTERSECTION_MARGIN_EXTRA,
				STATUSEFFECT_SEALED_ICON_Y - INTERSECTION_MARGIN_EXTRA,
				sealedImage.getWidth() + INTERSECTION_MARGIN_EXTRA * 2,
				sealedImage.getHeight() + INTERSECTION_MARGIN_EXTRA * 2
			));
		}
		else sealedIntersection = false;
		
		return barsIntersection || dizzyIntersection || confusedIntersection || sealedIntersection;
	}
	
	// call once when rewinding begins, giving initial random positions for the clocks hands
	private void setRewindClockInitialRadianValues() {
		clockMinuteRadianPosition = Support.getDoubleWithXExtraDecimals(Support.randInt(0, 628), 2);
		clockHourRadianPosition = clockFullCircleLength / 12 * Support.randInt(0, 11);
		clockHourRadianPosition += clockMinuteRadianPosition / 12;
		initialRewindFrame = false;
	}
	
	// called every frame while rewinding, giving new positions in radians for the clocks hands
	private void updateRewindClockRadianValues() {
		clockMinuteRadianPosition += clockMinuteRewindSpeed;
		if(clockMinuteRadianPosition > clockFullCircleLength) clockMinuteRadianPosition -= clockFullCircleLength;
		clockHourRadianPosition += clockHourRewindSpeed;
		if(clockHourRadianPosition > clockFullCircleLength) clockHourRadianPosition -= clockFullCircleLength;
	}
	
	// called every frame while rewinding, giving new positions as x & y coordinates for the clocks hands
	private void updateRewindClockCoordinates() {
		// radian positions include fix for 0 being at 3 o'clock by subtracting 1/4 of a lap
		ArrayList<Double> minuteCoordinates = Support.getCircleXYPos(clockMinuteArmLength, clockMinuteRadianPosition - clockFullCircleLength / 4);
		clockMinuteXPosition = minuteCoordinates.get(0);
		clockMinuteYPosition = minuteCoordinates.get(1);
		
		ArrayList<Double> hourCoordinates = Support.getCircleXYPos(clockHourArmLength, clockHourRadianPosition - clockFullCircleLength / 4);
		clockHourXPosition = hourCoordinates.get(0);
		clockHourYPosition = hourCoordinates.get(1);
	}
	
	public void draw(Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.TRANSPARENT));
		
		g.setFont(font);
		origColor = g.getColor();
		
		int extraY = 0;
		if(checkHudIntersectionWithPlayer()) extraY = playerIntersectionExtraY;
		
		drawPlayerBars(g, extraY);
		
		drawStatusIcons(g, extraY);
		
		drawRewindParts(g);
		
		g.setColor(origColor);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.surroundingsOpacity));
	}

	private void drawPlayerBars(Graphics2D g, int extraY) {
		// the ?proper? way
		// unfortunately it takes longer to execute and is not as simple logically
		/*barsY =  GamePanel.HEIGHT - BARS_IMAGE_Y - barsImage.getHeight();
		healthY =  GamePanel.HEIGHT - BARS_TEXT_HEALTH_Y - font.getSize();
		attackEnergyY =  GamePanel.HEIGHT - BARS_TEXT_ATTACKENERGY_Y - font.getSize();
		rewindEnergyY =  GamePanel.HEIGHT - BARS_TEXT_REWINDENERGY_Y - font.getSize();*/
		
		g.drawImage(
			barsImage,
			BARS_IMAGE_X,
			BARS_IMAGE_Y + extraY,
			null
		);
		
		g.setColor(getTextColor(player.getHealth(), player.getMaxHealth()));
		g.drawString(
			(int) player.getHealth() + "/" + (int) player.getMaxHealth(),
			BARS_TEXT_HEALTH_X,
			BARS_TEXT_HEALTH_Y + extraY
		);
		
		g.setColor(getTextColor(player.getAttackEnergy(), player.getMaxAttackEnergy()));
		g.drawString(
			(int) player.getAttackEnergy() / 1000 + "/" + (int) player.getMaxAttackEnergy() / 1000,
			BARS_TEXT_ATTACKENERGY_X,
			BARS_TEXT_ATTACKENERGY_Y + extraY
		);

		g.setColor(getTextColor(player.getRewindEnergy(), player.getMaxRewindEnergy()));
		g.drawString(
			player.getRewindEnergy() / 1000 + "/" + player.getMaxRewindEnergy() / 1000,
			BARS_TEXT_REWINDENERGY_X,
			BARS_TEXT_REWINDENERGY_Y + extraY
		);
	}

	private void drawStatusIcons(Graphics2D g, int extraY) {
		if(player.isDizzy()) {
			g.drawImage(dizzyImage,
				STATUSEFFECT_DIZZY_ICON_X,
				STATUSEFFECT_DIZZY_ICON_Y + extraY,
				null
			);
			g.setColor(Color.WHITE);
			g.drawString(
				"" + player.getDizzyDurationLeft(),
				STATUSEFFECT_DIZZY_TIME_X,
				STATUSEFFECT_DIZZY_TIME_Y + extraY
			);
		}
		
		if(player.isConfused()) {
			g.drawImage(
				confusedImage,
				STATUSEFFECT_CONFUSED_ICON_X,
				STATUSEFFECT_CONFUSED_ICON_Y + extraY,
				null
			);
			g.setColor(Color.WHITE);
			g.drawString(
				"" + player.getConfusedDurationLeft(),
				STATUSEFFECT_CONFUSED_TIME_X,
				STATUSEFFECT_CONFUSED_TIME_Y + extraY
			);
		}
		
		if(player.isSealed()) {
			g.drawImage(
				sealedImage,
				STATUSEFFECT_SEALED_ICON_X,
				STATUSEFFECT_SEALED_ICON_Y + extraY,
				null
			);
			g.setColor(Color.WHITE);
			g.drawString(
				"" + player.getSealedDurationLeft(),
				STATUSEFFECT_SEALED_TIME_X,
				STATUSEFFECT_SEALED_TIME_Y + extraY
			);
		}
	}
	
	private void drawRewindParts(Graphics2D g) {
		if(player.getRewindingTerms()) {
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, Support.NORMALOPACITY));
			
			if(initialRewindFrame) {
				setRewindClockInitialRadianValues();
				updateRewindClockRadianValues();
				updateRewindClockCoordinates();
			}
			
			// moves the clock only when the player is also moving back in time
			if(player.isRewindActiveTime()) {
				updateRewindClockRadianValues();
				updateRewindClockCoordinates();
			}
			
			if(player.isRewindEndPause()) {
				long elapsed = (System.nanoTime() - player.getRewindEndPauseTime()) / 1000000;
				if(elapsed / 100 % 2 == 0) {
					drawClock(g);
				}
			}
			else drawClock(g);
		}
		else initialRewindFrame = true;
	}

	private void drawClock(Graphics2D g) {
		g.drawImage(
			clockImage,
			GamePanel.WIDTH / 2 - clockImage.getWidth() / 2,
			GamePanel.HEIGHT / 2 - clockImage.getWidth() / 2,
			null
		);
		g.setColor(Color.RED);
		g.drawLine(
			GamePanel.WIDTH / 2,
			GamePanel.HEIGHT / 2,
			GamePanel.WIDTH / 2 + (int) clockHourXPosition,
			GamePanel.HEIGHT / 2 + (int) clockHourYPosition
		);
		g.drawLine(
			GamePanel.WIDTH / 2,
			GamePanel.HEIGHT / 2,
			GamePanel.WIDTH / 2 + (int) clockMinuteXPosition,
			GamePanel.HEIGHT / 2 + (int) clockMinuteYPosition
		);
	}

	private Color getTextColor(double currentValue, double maxValue) {
		//int R = 255;
		//int R = (int) (0.0 + 255.0 * ((double) currentValue / (double) maxValue));
		//int G = (int) (0.0 + 255.0 * ((double) currentValue / (double) maxValue));
		//int B = (int) (0.0 + 255.0 * ((double) currentValue / (double) maxValue));
		int R = (int) (255.0 - 255.0 * (currentValue / maxValue));
		int G = 0;
		int B = 0;
		//int B = 255;
		return new Color(R, G, B);
	}
}










