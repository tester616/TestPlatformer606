package EntityExtraData;

import java.util.ArrayList;

public class BossTurretSpawnerExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	
	public ArrayList<BossTurretListExtra> attackList;
	public long spawnInterval;
	public int spawnBurstAmount;
	public long initialIdleTime;
	
	public BossTurretSpawnerExtra(ArrayList<BossTurretListExtra> attackList, long spawnInterval, int spawnBurstAmount, long initialIdleTime) {
		this.attackList = new ArrayList<BossTurretListExtra>(attackList);
		this.spawnInterval = spawnInterval;
		this.spawnBurstAmount = spawnBurstAmount;
		this.initialIdleTime = initialIdleTime;
	}
}
