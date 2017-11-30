package EntityExtraData;

public class BossBall6Extra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double dx;
	public double dy;
	public double homingStrength;
	
	public BossBall6Extra(double dx, double dy, double homingStrength) {
		this.dx = dx;
		this.dy = dy;
		this.homingStrength = homingStrength;
	}
}
