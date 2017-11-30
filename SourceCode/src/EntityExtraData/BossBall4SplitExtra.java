package EntityExtraData;

import Entity.Animation;

public class BossBall4SplitExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public double dx;
	public double dy;
	public int splitsLeft;
	public Animation animation;
	
	public BossBall4SplitExtra(double dx, double dy, int splitsLeft, Animation animation) {
		this.dx = dx;
		this.dy = dy;
		this.splitsLeft = splitsLeft;
		this.animation = animation;
	}
}
