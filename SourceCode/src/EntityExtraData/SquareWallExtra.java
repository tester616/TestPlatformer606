package EntityExtraData;

public class SquareWallExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public int startX;
	public int startY;
	public int endX;
	public int endY;
	
	public SquareWallExtra(int startX, int startY, int endX, int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
}
