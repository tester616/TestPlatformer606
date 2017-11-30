package Support;

public class LevelEnemySpawnOdds {
	
	private int type;
	private int min;
	private int max;
	
	public LevelEnemySpawnOdds(int type, int min, int max) {
		this.type = type;
		this.min = min;
		this.max = max;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
}
