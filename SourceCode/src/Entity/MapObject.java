package Entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import Drawable.Drawable;
import Main.GamePanel;
import Support.Support;
import TileMap.Tile;
import TileMap.TileMap;

public abstract class MapObject extends Drawable {
	
	// good to have id and stuff...
	protected int id;
	
	// tile stuff
	protected TileMap tileMap;
	protected int tileSize;
	protected double xmap;
	protected double ymap;
	
	// position and vector
	protected double x;
	protected double y;
	protected double dx;	// d stands for direction, so in other words how fast it's going in x and y axis
	protected double dy;
	
	// dimensions
	protected int width;
	protected int height;
	
	// collision box
	protected int cwidth;
	protected int cheight;
	
	// collision
	protected int currRow;
	protected int currCol;
	protected double xdest;
	protected double ydest;
	protected double xtemp;
	protected double ytemp;
	protected boolean topLeft;
	protected boolean topRight;
	protected boolean bottomLeft;
	protected boolean bottomRight;
	
	// animation
	protected Animation animation;
	protected int currentAction;
	protected int previousAction;
	protected boolean facingRight;
	
	// movement
	protected boolean left;
	protected boolean right;
	protected boolean up;
	protected boolean down;
	protected boolean jumping;
	protected boolean falling;
	protected boolean touchingLeftWall;
	protected boolean touchingRightWall;
	
	// wall jump grace period
	protected long lastTouchedLeftWall;
	protected long lastTouchedRightWall;
	
	// movement attributes
	protected double moveSpeed;
	protected double maxSpeed;
	protected double stopSpeed;
	protected double fallSpeed;
	protected double maxFallSpeed;
	protected double jumpStart;
	protected double stopJumpSpeed;
	
	// speed modifier
	protected double speedMultiplier;
	
	// can affect hostile objects negatively & can be affected negatively by hostile objects
	protected boolean pacified;
	protected boolean invulnerable;
	
	// flinching protects against some negative actions like damage and knockback
	protected boolean flinching;
	protected long flinchTime;
	protected long flinchTimer;
	
	// constructor
	public MapObject(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
		flinchTime = 1000;
		id = Support.id;
		Support.id++;
	}
	
	public int getId() { return id; }
	
	public void setTileMap(TileMap tm) {
		tileMap = tm;
		tileSize = tm.getTileSize();
	}
	
	public boolean intersects(MapObject o) {
		Rectangle r1 = getRectangle();
		Rectangle r2 = o.getRectangle();
		return r1.intersects(r2);
	}
	
	// used to get direction for knockback interaction or other stuff
	public ArrayList<Double> getDirectionToMapObject(MapObject o) {
		double xDir = o.getx() - getx();
		double yDir = o.gety() - gety();
		return getDirectionProportions(xDir, yDir);
	}
	
	// used to get direction for knockback interaction or other stuff
	public ArrayList<Double> getDirectionToPoint(double xPoint, double yPoint) {
		double xDir = xPoint - getx();
		double yDir = yPoint - gety();
		return getDirectionProportions(xDir, yDir);
	}
	
	// give it speeds in x and y direction to get their proportions compared to each other, for example for every 1x you need 1,5y
	public ArrayList<Double> getDirectionProportions(double xDir, double yDir) {
		double xDirProportion = 0.0;
		double yDirProportion = 0.0;
		/*System.out.println("xDir & yDir before division");
		System.out.println(xDir + " " + yDir);
		System.out.println("individual values");
		System.out.println("tm " + xmap + " " + ymap);
		System.out.println("explosion values");
		System.out.println(o.gety() + " " + o.getHeight() + " " + o.getHeight() / 2);
		System.out.println("player values");
		System.out.println(gety() + " " + getHeight() + " " + getHeight() / 2);
		System.out.println("totals");
		System.out.println(((int) o.gety() + o.getHeight() / 2));
		System.out.println(((int) y + height / 2));*/
		if(xDir == 0 && yDir != 0) {
			xDirProportion = 0;
			yDirProportion = yDir / Math.abs(yDir);
		}
		else if(xDir != 0 && yDir == 0) {
			xDirProportion = xDir / Math.abs(xDir);
			yDirProportion = 0.0;
		}
		else if(xDir == 0 && yDir == 0) {
			xDirProportion = 0.0;
			yDirProportion = 0.0;
		}
		else if(Math.abs(xDir) >= Math.abs(yDir)) {
			xDirProportion = xDir / Math.abs(yDir);
			yDirProportion = yDir / Math.abs(yDir);
		}
		else {
			xDirProportion = xDir / Math.abs(xDir);
			yDirProportion = yDir / Math.abs(xDir);
		}
		//System.out.println("xDir & yDir after division");
		//System.out.println(xDir + " " + yDir);
		ArrayList<Double> direction = new ArrayList<Double>();
		direction.add(xDirProportion);
		direction.add(yDirProportion);
		return direction;
	}
	
	// knocks this object to the given direction with the given vector
	// xDir and yDir are reversed, so should be used directly with the results from getDirectionToMapObject() where you return direction values leading to the object that does the knockback
	public void knockback(double xDir, double yDir, double knockbackStrength, boolean continuous, boolean absolute) {
		// invulnerability gives immunity to knockback unless it's absolute and needed to set speed of an enemy towards the player for example
		// flinching can also give immunity if the knockback mode is not continuous
		if(!absolute) {
			if(invulnerable || (!continuous && flinching)) return;
		}
		
		double x = 0;
		// xDir is 0, no formula needed, all of knockbackStrength is in yDir
		if(xDir == 0 & yDir != 0) {
			if(yDir > 0) dy += knockbackStrength * (-1);
			else if(yDir < 0) dy += knockbackStrength;
		}
		// yDir is 0, no formula needed, all of knockbackStrength is in xDir
		else if(xDir != 0 & yDir == 0) {
			if(xDir > 0) dx += knockbackStrength *(-1);
			else if(xDir < 0) dx += knockbackStrength;
		}
		// xDir & yDir are both 0, objects are centered inside each other, direction is straight up
		else if(xDir == 0 && yDir == 0) {
			dy += knockbackStrength * (-1);
		}
		// normal cases
		else {
			// (xDir * x)^2 + (yDir * x)^2 = knockbackStrength^2 solved for x
			x = Support.pythGetXYMultiplier(xDir, yDir, knockbackStrength);
			
			dx += xDir * x * (-1);
			dy += yDir * x * (-1);
		}

		/*System.out.println(x);
		System.out.println(xDir * x);
		System.out.println(yDir * x);
		System.out.println("should be...");
		System.out.println(knockbackStrength);
		System.out.println("is...");
		System.out.println(Math.hypot(xDir * x, yDir * x));*/
	}
	
	public Rectangle getRectangle() {
		return new Rectangle(
			(int)x - cwidth / 2,
			(int)y - cheight / 2,
			cwidth,
			cheight
		);
	}
	
	public void calculateCorners(double x, double y) {
		
		// plenty of fixing needed for values under 0
		double decimalLeftTile = (x - cwidth / 2) / tileSize;
		double decimalRightTile = (x + cwidth / 2 - 1) / tileSize;
		if(decimalRightTile < 0) decimalRightTile = (x + cwidth / 2) / tileSize;
		double decimalTopTile = (y - cheight / 2) / tileSize;
		double decimalBottomTile = (y + cheight / 2 - 1) / tileSize;
		if(decimalBottomTile < 0) decimalBottomTile = (y + cheight / 2) / tileSize;
		
		int leftTile;
        int rightTile;
        int topTile;
        int bottomTile;
		
		if(decimalLeftTile <= 0) leftTile = (int) decimalLeftTile - 1;
		else leftTile = (int) decimalLeftTile;
		
		if(decimalRightTile <= 0) rightTile = (int) decimalRightTile - 1;
		else rightTile = (int) decimalRightTile;
		
		if(decimalTopTile <= 0) topTile = (int) decimalTopTile - 1;
		else topTile = (int) decimalTopTile;
		
		if(decimalBottomTile <= 0) bottomTile = (int) decimalBottomTile - 1;
		else bottomTile = (int) decimalBottomTile;
        
		//System.out.println("calccorn y:"+y);
        //System.out.println("bottile:"+bottomTile+" decimalver:"+((y + cheight / 2 - 1) / tileSize));
        
        /*if(
        		topTile < 0 ||
        		bottomTile >= tileMap.getNumRows() ||
                leftTile < 0 ||
                rightTile >= tileMap.getNumCols()
        ) {
        	System.out.println("all corners NO "+(topTile < 0)+" "+(bottomTile >= tileMap.getNumRows())+" "+(leftTile < 0)+" "+(rightTile >= tileMap.getNumCols()));
        	topLeft = topRight = bottomLeft = bottomRight = false;
        	//return;
        }*/
        
        int tl = tileMap.getType(topTile, leftTile, false);
        int tr = tileMap.getType(topTile, rightTile, false);
        int bl = tileMap.getType(bottomTile, leftTile, false);
        int br = tileMap.getType(bottomTile, rightTile, false);
        
        //System.out.println(bottomTile+" "+rightTile);
        
        topLeft = tl == Tile.BLOCKED;
        topRight = tr == Tile.BLOCKED;
        bottomLeft = bl == Tile.BLOCKED;
        bottomRight = br == Tile.BLOCKED;
        //System.out.println(topLeft+" "+topRight+" "+bottomLeft+" "+bottomRight);
        //System.out.println("TR"+tr+" topRight"+topRight+" rightTile"+rightTile);
	}
	
	public void checkTileMapCollision() {
		
		double decimalCurrCol = x / tileSize;
		double decimalCurrRow = y / tileSize;
		
		// (int) conversion drops decimals, making both -0.99 and 0.99 to 0, so the -1 row correction must happen while the values have decimals
		if(decimalCurrCol <= 0) currCol = (int) decimalCurrCol - 1;
		else currCol = (int) decimalCurrCol;
		if(decimalCurrRow <= 0) currRow = (int) decimalCurrRow - 1;
		else currRow = (int) decimalCurrRow;
		
		xdest = x + dx;
		ydest = y + dy;
		//System.out.println("xtemp at beginning "+xtemp);
		xtemp = x;
		//System.out.println("xtemp at beginning 2 (took x value) "+xtemp);
		ytemp = y;
		
		// wall touch resets
		touchingLeftWall = false;
		touchingRightWall = false;
		
		calculateCorners(x, ydest);
		//System.out.println(topLeft+" "+topRight+" "+bottomLeft+" "+bottomRight);
		//System.out.println("omfg "+currRow+" "+currCol);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				// for big objects
				if(cheight / 2 >= tileSize) {
					int hitRow = currRow - (cheight / 2) / tileSize;
					ytemp = hitRow * tileSize + cheight / 2;
				}
				// normal cases
				else {
					ytemp = currRow * tileSize + cheight / 2;
				}
				//System.out.println("top collision");
			}
			else {
				ytemp += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				//System.out.println(y+" "+dy+" "+(y + dy));
				dy = 0;
				falling = false;
				// for big objects
				if(cheight / 2 >= tileSize) {
					int hitRow = currRow + (cheight / 2) / tileSize;
					ytemp = (hitRow + 1) * tileSize - cheight / 2;
				}
				// normal cases
				else {
					ytemp = (currRow + 1) * tileSize - cheight / 2;
				}
				//System.out.println("bottom collision");
				//System.out.println(y+" "+ytemp);
			}
			else {
				ytemp += dy;
			}
		}
		
		calculateCorners(xdest, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				touchingLeftWall = true;
				// for big objects
				if(cwidth / 2 >= tileSize) {
					int hitCol = currCol - (cwidth / 2) / tileSize;
					xtemp = hitCol * tileSize + cwidth / 2;
				}
				// normal cases
				else {
					xtemp = currCol * tileSize + cwidth / 2;
				}
				//System.out.println("left collision");
			}
			else {
				xtemp += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				touchingRightWall = true;
				// for big objects
				if(cwidth / 2 >= tileSize) {
					int hitCol = currCol + (cwidth / 2) / tileSize;
					xtemp = (hitCol + 1) * tileSize - cwidth / 2;
				}
				// normal cases
				else {
					xtemp = (currCol + 1) * tileSize - cwidth / 2;
				}
				
				//System.out.println("formula currCol "+currCol+" tileSize "+tileSize+" and cwidth "+cwidth);
				
				//System.out.println("right top n bottom "+topRight+" "+bottomRight+" and xtemp "+xtemp);
				//System.out.println("right collision");
			}
			else {
				xtemp += dx;
			}
		}
		
		if(!falling) {
			calculateCorners(x, ydest + 1);
			if(!bottomLeft && !bottomRight) {
				falling = true;
			}
		}
	}
	
	public int getx() { return (int)x; }
	public int gety() { return (int)y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	public int getCWidth() { return cwidth; }
	public int getCHeight() { return cheight; }
	

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public void setVector(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void setMapPosition() {
		xmap = tileMap.getx();
		ymap = tileMap.gety();
	}
	
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	public void setJumping(boolean b) { jumping = b; }
	
	public boolean notOnScreen() {
		return x + xmap + width < 0 ||
			x + xmap - width > GamePanel.WIDTH ||
			y + ymap + height < 0 ||
			y + ymap - height > GamePanel.HEIGHT;
	}
	
	public void draw(Graphics2D g) {
		if(facingRight) {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2),
				(int)(y + ymap - height / 2),
				width,
				height,
				null
			);
		}
		else {
			g.drawImage(
				animation.getImage(),
				(int)(x + xmap - width / 2 + width),
				(int)(y + ymap - height / 2),
				-width,
				height,
				null
			);
		}
	}
	
	public void drawHitbox(Graphics2D g) {
		g.setColor(Color.red);
		g.drawRect(
			(int)(x + xmap - cwidth / 2),
			(int)(y + ymap - cheight / 2),
			cwidth,
			cheight
		);
	}
	
	public void drawPosition(Graphics2D g) {
		g.setColor(Color.red);
		g.drawRect(
			(int)(x + xmap - cwidth / 2),
			(int)(y + ymap - cheight / 2),
			cwidth,
			cheight
		);
		g.drawOval(
			(int) (x + xmap),
			(int) (y + ymap),
			0,
			0
		);
	}
	
	public Point getSuitableRandomSpawnPoint(boolean canBeVisibleToPlayer, boolean canBeInMidair) {
		Point validPoint = new Point();
		boolean validPointFound = false;
		int mapTotalWidth = tileMap.getWidth();
		int mapTotalHeight = tileMap.getHeight();
		int eventualLoopBreaker = 0;
		
		//System.out.println("---------------------------------spawn point------------------------------");
		//System.out.println("tileMap position " + tileMap.getx() + " " + tileMap.gety());
		
		while(!validPointFound) {
			Point p;
			
			// runs only if no valid spawn point has been found for a while and returns the point 0, 0
			if(eventualLoopBreaker >= 30) {
				p = new Point(0, 0);
				validPoint = p;
				break;
			}
			
			// gets the point based on visibility for player
			if(canBeVisibleToPlayer) {
				// get random spawn point with a small offset from the very corners of the map
				p = new Point(
					Support.randInt(
						0 + cwidth * 2,
						mapTotalWidth - cwidth * 2
					),
					Support.randInt(
						0 + cheight * 2,
						mapTotalHeight - cheight * 2
					)
				);
			}
			else {
				// get random spawn point with a small offset from the very corners of the map and - the size of the screen since those are bad values and added back later if triggered
				p = new Point(
					Support.randInt(
						0 + cwidth * 2,
						mapTotalWidth - cwidth * 2 - GamePanel.WIDTH
					),
					Support.randInt(
						0 + cheight * 2,
						mapTotalHeight - cheight * 2 - GamePanel.HEIGHT
					)
				);
				
				// if a point is given inside the player's field of view, it's conflicting values are changed to appear outside
				// values over the player's field of view also need to be increased to keep the balance and populate the last bit of map
				if(p.x >= -tileMap.getx() - width / 2 - 1) {
					// values just slightly inside the beginning of the field of view are given an additional small push so they don't appear slightly on the opposide end after the first change
					if(
						p.x >= -tileMap.getx() - width / 2 - 1 &&
						p.x <= -tileMap.getx() + width / 2 + 1
					) {
						p.x += width + 1;
					}
					
					p.x += GamePanel.WIDTH;
				}
				if(p.y >= -tileMap.gety() - height / 2 - 1) {
					// values just slightly inside the beginning of the field of view are given an additional small push so they don't appear slightly on the opposide end after the first change
					if(
						p.y >= -tileMap.gety() - height / 2 - 1 &&
						p.y <= -tileMap.gety() + height / 2 + 1
					) {
						p.y += height + 1;
					}
					
					p.y += GamePanel.HEIGHT;
				}
			}
			
			// lastly the random point needs to be tested to not put the mapobject inside a wall
			calculateCorners(p.x, p.y);
			// the point is accepted if none of the four corners are inside walls
			if(!(topLeft || topRight || bottomLeft || bottomRight) && canBeInMidair) {
				validPoint = p;
				validPointFound = true;
			}
			
			// for finding a point on the ground
			if(!canBeInMidair) {
				int yPosition = p.y;
				int positiveMultiplier = 1;
				int negativeMultiplier = -1;
				int bottomTileRowNumber;
				boolean positiveMultiplierTurn = true;
				boolean upOutOfBounds = false;
				boolean downOutOfBounds = false;
				// finding next row of tiles going down that blocks the object, breaks when yPosition out of bounds
				while(!upOutOfBounds || !downOutOfBounds) {
					// a multiplier runs once for positive and once for negative before increasing with 1 until both are out of bounds without a valid point found
					if(positiveMultiplierTurn && !downOutOfBounds) {
						// reset yPos
						yPosition = p.y;
						// add multiplier effect
						yPosition += (tileMap.getTileSize() * positiveMultiplier);
						
						bottomTileRowNumber = (int) ((yPosition + cheight / 2 - 1) / tileSize);
						// check if bottom tile is the first tile, continue if not (this should always be over 0 since it's going from 0 to 1 in the worst case)
						if(bottomTileRowNumber > 0) {
							// check if bottom tile is out of bounds
							if(bottomTileRowNumber < tileMap.getNumRows()) {
								// not below tileMap, continuing
								
								calculateCorners(p.x, yPosition);
								// potential row has been found below
								if(bottomLeft && bottomRight) {
									// check to see if the row above the found one is blocked
									calculateCorners(p.x, yPosition - tileMap.getTileSize());
									if(!bottomLeft && !bottomRight) {
										// success, spawn point found
										
										// new y point just above the bottom tile
										p.y = (bottomTileRowNumber * tileMap.getTileSize()) - height / 2 - 1;
										
										// one final check to see the position isn't inside a wall,
										// since there is a rare bug which shouldn't occur going in + direction, but just to be safe
										calculateCorners(p.x, p.y);
										// the point is accepted if none of the four corners are inside walls
										if(!(topLeft || topRight || bottomLeft || bottomRight) && canBeInMidair) {
											// the new y point might be inside players vision, so a check is done if needed
											if(canBeVisibleToPlayer) {
												validPoint = p;
												validPointFound = true;
												break;
											}
											else {
												if(
													!(
														(
															(p.y >= -tileMap.gety() - height / 2 - 1) &&
															(p.y <= -tileMap.gety() + GamePanel.HEIGHT + height / 2 + 1)
														) &&
														(
															(p.x >= -tileMap.getx() - width / 2 - 1) &&
															(p.x <= -tileMap.getx() + GamePanel.WIDTH + width / 2 + 1)
														)
													)
												) {
													validPoint = p;
													validPointFound = true;
													break;
												}
											}
										}
									}
								}
							}
							else {
								// below tileMap, locking positive part of loop
								
								downOutOfBounds = true;
							}
						}
						
						positiveMultiplierTurn = false;
						positiveMultiplier++;
					}
					else if(!positiveMultiplierTurn && !upOutOfBounds) {
						// reset yPos
						yPosition = p.y;
						// add multiplier effect
						yPosition += (tileMap.getTileSize() * negativeMultiplier);
						
						bottomTileRowNumber = (int) ((yPosition + cheight / 2 - 1) / tileSize);
						// check if bottom tile is out of bounds, continue if not (this should always pass since it's going up)
						if(bottomTileRowNumber < tileMap.getNumRows()) {
							// make sure bottom tile isn't the first one
							if(bottomTileRowNumber > 0) {
								// not first tile, continuing
								
								calculateCorners(p.x, yPosition);
								// potential row has been found above
								if(bottomLeft && bottomRight) {
									// check to see if the row above the found one is blocked
									calculateCorners(p.x, yPosition - tileMap.getTileSize());
									if(!bottomLeft && !bottomRight) {
										// success, spawn point found
										
										// new y point just above the bottom tile
										p.y = (bottomTileRowNumber * tileMap.getTileSize()) - height / 2 - 1;
										
										// one final check to see the position isn't inside a wall, since there is a rare bug
										calculateCorners(p.x, p.y);
										// the point is accepted if none of the four corners are inside walls
										if(!(topLeft || topRight || bottomLeft || bottomRight)) {
											// the new y point might be inside players vision, so a check is done if needed
											if(canBeVisibleToPlayer) {
												validPoint = p;
												validPointFound = true;
												break;
											}
											else {
												if(
													!(
														(
															(p.y >= -tileMap.gety() - height / 2 - 1) &&
															(p.y <= -tileMap.gety() + GamePanel.HEIGHT + height / 2 + 1)
														) &&
														(
															(p.x >= -tileMap.getx() - width / 2 - 1) &&
															(p.x <= -tileMap.getx() + GamePanel.WIDTH + width / 2 + 1)
														)
													)
												) {
													validPoint = p;
													validPointFound = true;
													break;
												}
											}
										}
									}
								}
							}
							else {
								// below tileMap, locking positive part of loop
								
								upOutOfBounds = true;
							}
						}
						
						positiveMultiplierTurn = true;
						negativeMultiplier--;
					}
					else {
						break;
					}
				}
			}
			
			eventualLoopBreaker++;
		}
		
		return validPoint;
	}

	public void setAnimationImages() {
		
	}
	
	public void setExtraData(int mode, Object extraData) {
		
	}
	
	public ArrayList<EntitySpawnData> getEntitiesToSpawn() {
		return null;
	}
}



