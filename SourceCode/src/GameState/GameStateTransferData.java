package GameState;

import java.util.HashMap;

import Entity.Player;

public class GameStateTransferData {
	
	private Player player;
	
	private HashMap<Integer, Boolean> levelLifeCollectedStatus = new HashMap<Integer, Boolean>();
	
	/*public boolean level1LifeCollected;
	public boolean level2LifeCollected;
	public boolean level3LifeCollected;
	public boolean level4LifeCollected;
	public boolean level5LifeCollected;
	public boolean level6LifeCollected;*/
	
	// giving a null value doesn't affect values from before in GameStateManager
	public GameStateTransferData() {
		
	}
	
	public GameStateTransferData(Player player) {
		this.player = player;
	}

	public GameStateTransferData(int level, boolean levelLifeCollected) {
		levelLifeCollectedStatus.put(level, levelLifeCollected);
		//setLevelLifeCollected(level, levelLifeCollected);
	}
	
	public GameStateTransferData(Player player, int level, boolean levelLifeCollected) {
		this.player = player;
		levelLifeCollectedStatus.put(level, levelLifeCollected);
		//setLevelLifeCollected(level, levelLifeCollected);
	}
	
	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public HashMap<Integer, Boolean> getLevelLifeCollectedStatus() {
		return levelLifeCollectedStatus;
	}

	public void setLevelLifeCollectedStatus(HashMap<Integer, Boolean> levelLifeCollectedStatus) {
		this.levelLifeCollectedStatus = levelLifeCollectedStatus;
	}
	
	public void setLevelLifeCollected(int level, boolean levelLifeCollected) {
		levelLifeCollectedStatus.put(level, levelLifeCollected);
		/*if(level == 1) level1LifeCollected = levelLifeCollected;
		else if(level == 2) level2LifeCollected = levelLifeCollected;
		else if(level == 3) level3LifeCollected = levelLifeCollected;
		else if(level == 4) level4LifeCollected = levelLifeCollected;
		else if(level == 5) level5LifeCollected = levelLifeCollected;
		else if(level == 6) level6LifeCollected = levelLifeCollected;
		else System.out.println("Error in GameStateTransferData constructor, no such level exists.");*/
	}
}
