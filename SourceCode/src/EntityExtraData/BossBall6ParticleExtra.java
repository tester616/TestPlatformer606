package EntityExtraData;

public class BossBall6ParticleExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double dx;
	public double dy;
	public long lifeTime;
	
	public BossBall6ParticleExtra(double dx, double dy, long lifeTime) {
		this.dx = dx;
		this.dy = dy;
		this.lifeTime = lifeTime;
	}
}
