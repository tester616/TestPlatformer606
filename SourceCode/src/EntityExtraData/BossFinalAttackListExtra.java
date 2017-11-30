package EntityExtraData;

public class BossFinalAttackListExtra {
	
	// used in arraylists by BossFinalAttackSpawnerExtra to include some data for each fired projectile for that particular attack
	// color 0 is red, 1 is green, 2 (or lack of data) is random
	public int color;
	public long duration;
	public double totalSpeed;
	public double radius;
	
	public BossFinalAttackListExtra(int color, long duration, double totalSpeed, double radius) {
		this.color = color;
		this.duration = duration;
		this.totalSpeed = totalSpeed;
		this.radius = radius;
	}
}
