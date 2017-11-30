package EntityExtraData;

public class BossBall7ParticleExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double dx;
	public double dy;
	public double accelerationX;
	public double accelerationY;
	public long lifeTime;
	
	public BossBall7ParticleExtra(double dx, double dy, double accelerationX, double accelerationY, long lifeTime) {
		this.dx = dx;
		this.dy = dy;
		this.accelerationX = accelerationX;
		this.accelerationY = accelerationY;
		this.lifeTime = lifeTime;
	}
}
