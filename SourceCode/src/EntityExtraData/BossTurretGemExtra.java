package EntityExtraData;

public class BossTurretGemExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double dx;
	public double dy;
	
	public BossTurretGemExtra(double dx, double dy) {
		this.dx = dx;
		this.dy = dy;
	}
}
