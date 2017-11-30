package Support;

public class Playtime {
	
	// pending actions
	private boolean startTimeOnNextStageStart;
	private boolean stopTimeOnNextGameStateChange;
	private boolean drawTimeOnNextGameStateChange;
	private boolean resetFullOnNextGameStateChange;
	
	// states
	private boolean drawTime;

	// whole values to be displayed
	private int cHours;
	private int cMinutes;
	private int cSeconds;
	private int cMilliseconds;

	// in nanoseconds
	private long gameStartTime;
	private long gameEndTime;
	private long gameTotalTime;
	
	public Playtime() {
		startTimeOnNextStageStart = true;
		stopTimeOnNextGameStateChange = false;
		drawTimeOnNextGameStateChange = false;
		
		drawTime = false;
		
		cHours = 0;
		cMinutes = 0;
		cSeconds = 0;
		cMilliseconds = 0;
		
		gameStartTime = 0;
		gameEndTime = 0;
		gameTotalTime = 0;
	}
	
	public void startGameTime() {
		gameStartTime = System.nanoTime();
	}
	
	public void stopGameTime() {
		gameEndTime = System.nanoTime();
	}
	
	// takes difference of gameStart & gameEnd and converts it to hours, minutes, seconds & milliseconds
	public void setCurrentTimeValues() {
		double gameTotalTimeMilliseconds;
		// for example 1.24
		double gameTimeComponentHours;
		// this would then be 0.24
		double gameTimeComponentHoursRest;
		// and this 0.24 * 60 = 14.4
		double gameTimeComponentMinutes;
		// and this 0.4 etc.
		double gameTimeComponentMinutesRest;
		double gameTimeComponentSeconds;
		double gameTimeComponentSecondsRest;
		double gameTimeComponentMilliseconds;
		
		gameTotalTime = gameEndTime - gameStartTime;

		gameTotalTimeMilliseconds = (double) gameTotalTime / 1000000;
		
		gameTimeComponentHours = gameTotalTimeMilliseconds / (1000 * 60 * 60);
		gameTimeComponentHoursRest = gameTimeComponentHours - Math.floor(gameTimeComponentHours);
		
		gameTimeComponentMinutes = gameTimeComponentHoursRest * 60;
		gameTimeComponentMinutesRest = gameTimeComponentMinutes - Math.floor(gameTimeComponentMinutes);
		
		gameTimeComponentSeconds = gameTimeComponentMinutesRest * 60;
		gameTimeComponentSecondsRest = gameTimeComponentSeconds - Math.floor(gameTimeComponentSeconds);
		
		gameTimeComponentMilliseconds = gameTimeComponentSecondsRest * 1000;
		
		if(gameTimeComponentHours >= 24) {
			cHours = 23;
			cMinutes = 59;
			cSeconds = 59;
			cMilliseconds = 999;
		}
		else {
			// the components contain decimals, so those are removed first
			cHours = (int) Math.floor(gameTimeComponentHours);
			cMinutes = (int) Math.floor(gameTimeComponentMinutes);
			cSeconds = (int) Math.floor(gameTimeComponentSeconds);
			cMilliseconds = (int) Math.floor(gameTimeComponentMilliseconds);
		}
	}
	
	public void resetPending() {
		startTimeOnNextStageStart = true;
		stopTimeOnNextGameStateChange = false;
		drawTimeOnNextGameStateChange = false;
	}
	
	public void resetStates() {
		drawTime = false;
	}
	
	public void resetTime() {
		cHours = 0;
		cMinutes = 0;
		cSeconds = 0;
		cMilliseconds = 0;
		
		gameStartTime = 0;
		gameEndTime = 0;
		gameTotalTime = 0;
	}
	
	public void resetFull() {
		resetPending();
		resetStates();
		resetTime();
	}

	public boolean isStartTimeOnNextStageStart() {
		return startTimeOnNextStageStart;
	}

	public void setStartTimeOnNextStageStart(boolean startTimeOnNextStageStart) {
		this.startTimeOnNextStageStart = startTimeOnNextStageStart;
	}

	public boolean isStopTimeOnNextGameStateChange() {
		return stopTimeOnNextGameStateChange;
	}

	public void setStopTimeOnNextGameStateChange(boolean stopTimeOnNextGameStateChange) {
		this.stopTimeOnNextGameStateChange = stopTimeOnNextGameStateChange;
	}

	public boolean isDrawTimeOnNextGameStateChange() {
		return drawTimeOnNextGameStateChange;
	}

	public void setDrawTimeOnNextGameStateChange(boolean drawTimeOnNextGameStateChange) {
		this.drawTimeOnNextGameStateChange = drawTimeOnNextGameStateChange;
	}
	
	public boolean isResetFullOnNextGameStateChange() {
		return resetFullOnNextGameStateChange;
	}

	public void setResetFullOnNextGameStateChange(boolean resetFullOnNextGameStateChange) {
		this.resetFullOnNextGameStateChange = resetFullOnNextGameStateChange;
	}
	
	public boolean isDrawTime() {
		return drawTime;
	}

	public void setDrawTime(boolean drawTime) {
		this.drawTime = drawTime;
	}
	
	public int getcHours() {
		return cHours;
	}

	public void setcHours(int cHours) {
		this.cHours = cHours;
	}

	public int getcMinutes() {
		return cMinutes;
	}

	public void setcMinutes(int cMinutes) {
		this.cMinutes = cMinutes;
	}

	public int getcSeconds() {
		return cSeconds;
	}

	public void setcSeconds(int cSeconds) {
		this.cSeconds = cSeconds;
	}

	public int getcMilliseconds() {
		return cMilliseconds;
	}

	public void setcMilliseconds(int cMilliseconds) {
		this.cMilliseconds = cMilliseconds;
	}

	public long getGameStartTime() {
		return gameStartTime;
	}

	public void setGameStartTime(long gameStartTime) {
		this.gameStartTime = gameStartTime;
	}

	public long getGameEndTime() {
		return gameEndTime;
	}

	public void setGameEndTime(long gameEndTime) {
		this.gameEndTime = gameEndTime;
	}
	
	public long getGameTotalTime() {
		return gameTotalTime;
	}

	public void setGameTotalTime(long gameTotalTime) {
		this.gameTotalTime = gameTotalTime;
	}
}
