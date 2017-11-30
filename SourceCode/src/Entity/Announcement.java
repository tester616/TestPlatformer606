package Entity;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

import TileMap.TileMap;

public class Announcement extends MapObject {
	
	private final int DISPLAY = 0;
	
	private int currentAnimation;
	
	private String content;
	
	private long lifeTime;
	private long creationTime;
	
	private Font font = new Font("Arial", Font.PLAIN, 10);
	private Color fontColor = Color.RED;
	
	private FontMetrics metrics;
	
	private boolean dead;
	
	public Announcement(TileMap tm, String content, double dx, double dy, long lifeTime, Font font, Color fontColor) {
		
		super(tm);
		
		this.content = content;
		this.dx = dx;
		this.dy = dy;
		this.lifeTime = lifeTime;
		if(font != null) this.font = font;
		if(fontColor != null) this.fontColor = fontColor;
		creationTime = System.nanoTime();
	}
	
	public Announcement(TileMap tm, int content, double dx, double dy, long lifeTime, Font font, Color fontColor) {
		
		super(tm);
		
		this.content = "" + content;
		this.dx = dx;
		this.dy = dy;
		this.lifeTime = lifeTime;
		if(font != null) this.font = font;
		if(fontColor != null) this.fontColor = fontColor;
		creationTime = System.nanoTime();
	}
	
	public Announcement(TileMap tm, double content, double dx, double dy, long lifeTime, Font font, Color fontColor) {
		
		super(tm);
		
		this.content = "" + content;
		this.dx = dx;
		this.dy = dy;
		this.lifeTime = lifeTime;
		if(font != null) this.font = font;
		if(fontColor != null) this.fontColor = fontColor;
		creationTime = System.nanoTime();
	}
	
	public Announcement(TileMap tm, long content, double dx, double dy, long lifeTime, Font font, Color fontColor) {
		
		super(tm);
		
		this.content = "" + content;
		this.dx = dx;
		this.dy = dy;
		this.lifeTime = lifeTime;
		if(font != null) this.font = font;
		if(fontColor != null) this.fontColor = fontColor;
		creationTime = System.nanoTime();
	}

	public boolean isDead() { return dead; }
	
	public void updateCurrentAnimation() {
		if(currentAnimation == DISPLAY) {
			long passedTime = (System.nanoTime() - creationTime) / 1000000;
			
			if(passedTime >= lifeTime) {
				dead = true;
			}
		}
	}
	
	public void update() {
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		
		updateCurrentAnimation();
	}

	public void draw(Graphics2D g) {
		setMapPosition();
		
		Color origColor = g.getColor();
		Font origFont = g.getFont();
		
		metrics = g.getFontMetrics(font);
	    int contentHalfWidth = (metrics.stringWidth(content)) / 2;
		int contentHalfHeight = font.getSize() / 2;
	    
		g.setColor(fontColor);
		g.setFont(font);
		g.drawString(
			content, 
			(int) (x + xmap - contentHalfWidth),
			(int) (y + ymap - contentHalfHeight)
		);
	    
		g.setColor(origColor);
		g.setFont(origFont);
	}
	
	@Override
	public void checkTileMapCollision() {
		xtemp = x;
		ytemp = y;
		
		ytemp += dy;
		xtemp += dx;
	}
}









