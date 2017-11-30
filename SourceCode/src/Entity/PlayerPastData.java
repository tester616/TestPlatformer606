package Entity;


public class PlayerPastData {
	
	// MapObject data
	public int id;

	public double xmap;
	public double ymap;
	
	public double x;
	public double y;
	public double dx;
	public double dy;
	
	public int currentAction;
	
	public boolean left;
	public boolean right;
	public boolean jumping;
	public boolean falling;
	public boolean touchingLeftWall;
	public boolean touchingRightWall;
	
	public long lastTouchedLeftWall;
	public long lastTouchedRightWall;
	
	public boolean facingRight;
	
	// Player data
	public double health;
	public int attackEnergy;
	
	public boolean dashing;
	public boolean dashJumping;
	public boolean firstDashBeforeLanding;
	
	public boolean dizzy;
	public long dizzyStart;
	
	public boolean confused;
	public long confusedStart;
	
	public boolean sealed;
	public long sealedStart;
	
	// additional stuff
	public long currentTime;
	public int currentFrame;
	public long currentDelay;
	
	/*public PlayerPastData(
		 int id,
		 double xmap,
		 double ymap,
		 double x,
		 double y,
		 double dx,
		 double dy,
		 int currentAction,
		 boolean left,
		 boolean right,
		 boolean jumping,
		 boolean falling,
		 boolean touchingLeftWall,
		 boolean touchingRightWall,
		 long lastTouchedLeftWall,
		 long lastTouchedRightWall,
		 boolean facingRight,
		 int health,
		 boolean dashing,
		 boolean dashJumping,
		 boolean firstDashBeforeLanding,
		 boolean dizzy,
		 long dizzyStart,
		 int dizzyDuration,
		 long currentTime,
		 int currentFrame
	) {
		
	}*/
}
