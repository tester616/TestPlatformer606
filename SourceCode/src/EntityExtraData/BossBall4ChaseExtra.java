package EntityExtraData;

public class BossBall4ChaseExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double initialTravelX;
	public double initialTravelY;
	
	public BossBall4ChaseExtra(double initialTravelX, double initialTravelY) {
		this.initialTravelX = initialTravelX;
		this.initialTravelY = initialTravelY;
	}
}
