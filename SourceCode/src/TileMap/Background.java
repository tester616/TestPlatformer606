package TileMap;

import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;

import Drawable.Drawable;
import Main.GamePanel;
import Support.Support;

public class Background extends Drawable {

	private BufferedImage image;

	private BufferedImage imageC;

	private BufferedImage imageM;
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private double moveScale;
	
	public Background(String c, String m, double ms) {
		try {
			imageC = ImageIO.read(
				getClass().getResourceAsStream(c)
			);
			imageM = ImageIO.read(
					getClass().getResourceAsStream(m)
				);
			setBufferedImages();
			moveScale = ms;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setPosition(double x, double y) {
		this.x = (x * moveScale) % GamePanel.WIDTH;
		this.y = (y * moveScale) % GamePanel.HEIGHT;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void update() {
		x += dx;
		y += dy;
		if(x > GamePanel.WIDTH) x = 0;
		x += (dx * moveScale) % GamePanel.WIDTH;
		y += (dy * moveScale) % GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g) {
		
		g.drawImage(image, (int)x, (int)y, null);
		
		// since the background can scroll extra images need to be drawn if the first one ends
		if(x < 0) {
			g.drawImage(
					image,
					(int)x + GamePanel.WIDTH,
					(int)y,
					null
			);
		}
		if(x > 0) {
			g.drawImage(
					image,
					(int)x - GamePanel.WIDTH,
					(int)y,
					null
			);
		}
		
		if(y < 0) {
			g.drawImage(
					image,
					(int)x,
					(int)y + GamePanel.HEIGHT,
					null
			);
		}
		if(y > 0) {
			g.drawImage(
					image,
					(int)x,
					(int)y - GamePanel.HEIGHT,
					null
			);
		}
		
		if(x > 0 && y > 0) {
			g.drawImage(
					image,
					(int)x - GamePanel.WIDTH,
					(int)y - GamePanel.HEIGHT,
					null
			);
		}
		if(x > 0 && y < 0) {
			g.drawImage(
					image,
					(int)x - GamePanel.WIDTH,
					(int)y + GamePanel.HEIGHT,
					null
			);
		}
		if(x < 0 && y > 0) {
			g.drawImage(
					image,
					(int)x + GamePanel.WIDTH,
					(int)y - GamePanel.HEIGHT,
					null
			);
		}
		if(x < 0 && y < 0) {
			g.drawImage(
					image,
					(int)x + GamePanel.WIDTH,
					(int)y + GamePanel.HEIGHT,
					null
			);
		}
	}

	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			image = imageC;
		}
		else if(colorMode == Support.MONOCHROME) {
			image = imageM;
		}
	}
}





