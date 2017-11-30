package TileMap;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import Drawable.Drawable;
import Main.GamePanel;
import Support.Support;

public class TileMap extends Drawable {

	// position
	// both 0 at top left corner, from where they both go to - side
	private double x;
	private double y;
	
	// bounds
	private int xmin;
	private int ymin;
	private int xmax;
	private int ymax;
	
	private double tween;
	
	// map
	private int[][] map;
	private int tileSize;
	private int numRows;
	private int numCols;
	private int width;
	private int height;
	
	// tileset
	private BufferedImage tileset;
	private int numTilesAcross;
	private Tile[][] tiles;
	
	private Tile[][] tilesC;
	
	private Tile[][] tilesM;
	
	// drawing
	private int rowOffset;
	private int colOffset;
	private int numRowsToDraw;
	private int numColsToDraw;
	
	public TileMap(int tileSize) {
		this.tileSize = tileSize;
		numRowsToDraw = GamePanel.HEIGHT / tileSize + 2;
		numColsToDraw = GamePanel.WIDTH / tileSize + 2;
		tween = 0.07;
	}
	
	public void loadTiles(String c, String m) {
		
		try {
			BufferedImage subimage;
			
			tileset = ImageIO.read(
				getClass().getResourceAsStream(c)
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			tilesC = new Tile[2][numTilesAcross];
			
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
					col * tileSize,
					0,
					tileSize,
					tileSize
				);
				tilesC[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(
						col * tileSize,
						tileSize,
						tileSize,
						tileSize
					);
				tilesC[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
			

			tileset = ImageIO.read(
					getClass().getResourceAsStream(m)
			);
			numTilesAcross = tileset.getWidth() / tileSize;
			tilesM = new Tile[2][numTilesAcross];
			
			for(int col = 0; col < numTilesAcross; col++) {
				subimage = tileset.getSubimage(
					col * tileSize,
					0,
					tileSize,
					tileSize
				);
				tilesM[0][col] = new Tile(subimage, Tile.NORMAL);
				subimage = tileset.getSubimage(
						col * tileSize,
						tileSize,
						tileSize,
						tileSize
					);
				tilesM[1][col] = new Tile(subimage, Tile.BLOCKED);
			}
			
			setBufferedImages();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap(String s) {
		
		try {
			
			InputStream in = getClass().getResourceAsStream(s);
			BufferedReader br = new BufferedReader(
				new InputStreamReader(in)
			);
			numCols = Integer.parseInt(br.readLine());
			numRows = Integer.parseInt(br.readLine());
			map = new int[numRows][numCols];
			width = numCols * tileSize;
			height = numRows * tileSize;
			
			xmin = GamePanel.WIDTH - width;
			xmax = 0;
			ymin = GamePanel.HEIGHT - height;
			ymax = 0;
			
			String delims = "\\s+";
			for(int row = 0; row < numRows; row++) {
				String line = br.readLine();
				String[] tokens = line.split(delims);
				for(int col = 0; col < numCols; col++) {
					map[row][col] = Integer.parseInt(tokens[col]);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public int getTileSize() { return tileSize; }
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	public int getType(int row, int col, boolean b) {
		// if either row or col is out of bounds (outside the visible tilemap), returns 0 as it can't be blocked
		if(
			row < 0 ||
			row >= getNumRows() ||
			col < 0 ||
			col >= getNumCols()
		) return 0;
		
		//System.out.println("GO1 "+row+" "+col);
		int rc = map[row][col];
		//System.out.println("GO2 "+row+" "+col+" "+rc);
		int r = rc / numTilesAcross;
		int c = rc % numTilesAcross;
		if(b) System.out.println(row+" "+col+" "+rc+" "+r+" "+c+" "+numTilesAcross);
		return tiles[r][c].getType();
	}
	
	public void setTween(double d) { tween = d; }
	
	public void setPosition(double x, double y) {
		
		this.x += (x - this.x) * tween;
		this.y += (y - this.y) * tween;
		
		fixBounds();
		
		colOffset = (int)-this.x / tileSize;
		rowOffset = (int)-this.y / tileSize;
	}
	
	private void fixBounds() {
		if(x < xmin) x = xmin;
		if(y < ymin) y = ymin;
		if(x > xmax) x = xmax;
		if(y > ymax) y = ymax;
	}
	
	public void draw(Graphics2D g) {
		
		for(int row = rowOffset; row < rowOffset + numRowsToDraw; row++) {
			
			if(row >= numRows) break;
			
			for(int col = colOffset; col < colOffset + numColsToDraw; col++) {
				
				if(col >= numCols) break;
				
				if(map[row][col] == 0) continue;
				
				int rc = map[row][col];
				int r = rc / numTilesAcross;
				int c = rc % numTilesAcross;
				
				g.drawImage(
					tiles[r][c].getImage(),
					(int)x + col * tileSize,
					(int)y + row * tileSize,
					null
				);
				
				//drawRowAndCol(g, x, y, row, col, r, c);
				

			}
		}
	}
	
	public void drawRowAndCol(Graphics2D g, double x, double y, int row, int col, int r, int c) {
		String stringR = Integer.toString(row);
		String stringC = Integer.toString(col);
		String stringR2 = "r:"+stringR;
		String stringC2 = "c:"+stringC;
		g.drawString(
			stringR2,
			(int)x + col * tileSize,
			(int)y + row * tileSize + 10
		);
		g.drawString(
				stringC2,
				(int)x + col * tileSize,
				(int)y + row * tileSize + 10 + 10
			);
	}
	
	public int getNumRows() { return numRows; }
	public int getNumCols() { return numCols; }

	public void setBufferedImages() {
		super.setBufferedImages();
		if(colorMode == Support.COLORED) {
			tiles = tilesC;
		}
		else if(colorMode == Support.MONOCHROME) {
			tiles = tilesM;
		}
	}
}






