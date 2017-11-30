package Entity;

public class EntitySpawnData {
	
	private Object o;
	private int x;
	private int y;
	private boolean canBeVisibleToPlayer;
	private boolean canBeInMidair;

	public EntitySpawnData(Object o, int x, int y) {
		this.o = o;
		this.x = x;
		this.y = y;
		canBeVisibleToPlayer = true;
		canBeInMidair = true;
	}

	public EntitySpawnData(Object o, int x, int y, boolean canBeVisibleToPlayer, boolean canBeInMidair) {
		this.o = o;
		this.x = x;
		this.y = y;
		this.canBeVisibleToPlayer = canBeVisibleToPlayer;
		this.canBeInMidair = canBeInMidair;
	}

	public Object getO() {
		return o;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public boolean getCanBeVisibleToPlayer() {
		return canBeVisibleToPlayer;
	}

	public boolean getCanBeInMidair() {
		return canBeInMidair;
	}
}
