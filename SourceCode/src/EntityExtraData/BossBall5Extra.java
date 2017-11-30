package EntityExtraData;

public class BossBall5Extra {
	
	// don't give in x & y positions as extra data, since they change later due to their EntitySpawnData values, everything else should be fair game
	public int reverse;
	public long idleDuration;
	public double totalSpeed;
	public int attackNumber;
	
	public BossBall5Extra(int reverse, long idleDuration, double totalSpeed, int attackNumber) {
		this.reverse = reverse;
		this.idleDuration = idleDuration;
		this.totalSpeed = totalSpeed;
		this.attackNumber = attackNumber;
	}
}
