package EntityExtraData;

import java.util.ArrayList;

public class BossFinalAttackSpawnerExtra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	
	// consist of data like color mode for each gem
	public ArrayList<BossFinalAttackListExtra> attackList;
	// 0-23 --> 0-345 degrees with increases of 15
	public int startDegreeMode;
	public long spawnInterval;
	
	public BossFinalAttackSpawnerExtra(ArrayList<BossFinalAttackListExtra> attackList, int startDegreeMode, long spawnInterval) {
		this.attackList = new ArrayList<BossFinalAttackListExtra>(attackList);
		this.startDegreeMode = startDegreeMode;
		this.spawnInterval = spawnInterval;
	}
}
