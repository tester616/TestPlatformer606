package EntityExtraData;

public class BossBall7Extra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double xPathStart;
	public double yPathStart;
	public double xPathEnd;
	public double yPathEnd;
	public long pathTravelTime;
	
	public BossBall7Extra(double xPathStart, double yPathStart, double xPathEnd, double yPathEnd, long pathTravelTime) {
		this.xPathStart = xPathStart;
		this.yPathStart = yPathStart;
		this.xPathEnd = xPathEnd;
		this.yPathEnd = yPathEnd;
		this.pathTravelTime = pathTravelTime;
	}
}
