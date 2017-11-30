package Support;

import java.util.ArrayList;

public class LevelEnemySpawnSupport {
	
	private ArrayList<LevelEnemySpawnOdds> leso = new ArrayList<LevelEnemySpawnOdds>();
	
	/*private int darkmistMin;
	private int darkmistMax;
	private int halfmoonMin;
	private int halfmoonMax;
	private int mineMin;
	private int mineMax;
	private int shieldMin;
	private int shieldMax;
	private int spikesMin;
	private int spikesMax;
	private int starMin;
	private int starMax;
	private int triangleMin;
	private int triangleMax;*/
	
	private int maxCapacity;
	private int currentCapacity;
	
	// per capacity point
	private long spawnDelayTime;
	
	private long lastSpawnTime;

	private int rollMin;
	private int rollMax;
	
	private long lastAddedCapacity;
	
	public LevelEnemySpawnSupport(int maxCapacity, int spawnDelayTime) {
		this.maxCapacity = maxCapacity;
		this.spawnDelayTime = spawnDelayTime;
		rollMin = 0;
		rollMax = 100000;
		populateList();
		lastAddedCapacity = 1;
		lastSpawnTime = System.nanoTime();
	}
	
	// gives default rough values to each enemys odds of being chosen
	// normally they are changed in a GameState to represent what one wants for that specific level
	private void populateList() {
		for(int i = 1; i <= Support.ENEMYTYPES; i++) {
			leso.add(new LevelEnemySpawnOdds(
				i,
				(int) ((double) rollMin + (double) rollMax / (double) Support.ENEMYTYPES * (double) (i - 1)),
				(int) ((double) rollMin + (double) rollMax / (double) Support.ENEMYTYPES * (double) i)
			));
		}
	}

	public void setEnemyMinMax(int enemy, int min, int max) {
		for(LevelEnemySpawnOdds x : leso) {
			if(x.getType() == enemy) {
		        x.setMin(min);
				x.setMax(max);
			}
		}
	}
	
	public int getRandomEnemyType() {
		int enemyType = Support.NOTYPE;
		int r = Support.randInt(rollMin, rollMax);
		
		for(LevelEnemySpawnOdds x : leso) {
			if(r >= x.getMin() && r <= x.getMax()) {
				enemyType = x.getType();
				lastSpawnTime = System.nanoTime();
				return enemyType;
			}
		}
		
		if(enemyType == Support.NOTYPE) System.out.println("Bad roll odds, " + r + " didn't find a match.");
		
		return enemyType;
	}
	
	public boolean shouldSpawnEnemy() {
		long elapsed = (System.nanoTime() - lastSpawnTime) / 1000000;
		
		if(elapsed > spawnDelayTime * lastAddedCapacity && currentCapacity < maxCapacity) {
			return true;
		}
		
		return false;
	}

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public int getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(int currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public long getSpawnDelayTime() {
		return spawnDelayTime;
	}

	public void setSpawnDelayTime(long spawnDelayTime) {
		this.spawnDelayTime = spawnDelayTime;
	}

	public long getLastSpawnTime() {
		return lastSpawnTime;
	}

	public void setLastSpawnTime(long lastSpawnTime) {
		this.lastSpawnTime = lastSpawnTime;
	}
	
	public long getLastAddedCapacity() {
		return lastAddedCapacity;
	}

	public void setLastAddedCapacity(long lastAddedCapacity) {
		this.lastAddedCapacity = lastAddedCapacity;
	}
}
