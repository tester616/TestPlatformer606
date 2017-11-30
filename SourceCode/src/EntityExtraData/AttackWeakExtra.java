package EntityExtraData;

public class AttackWeakExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double dx;
	public double dy;
	public boolean facingRight;
	
	public AttackWeakExtra(double dx, double dy, boolean facingRight) {
		this.dx = dx;
		this.dy = dy;
		this.facingRight = facingRight;
	}
}
