package Entity;

import java.awt.image.BufferedImage;



public class Animation {

	private BufferedImage[] frames;
	private int currentFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playedOnce;
	
	public Animation() {
		playedOnce = false;
	}
	
	public void setFrames(BufferedImage[] frames) {
		this.frames = frames;
		currentFrame = 0;
		startTime = System.nanoTime();
		playedOnce = false;
	}
	
	// doesn't reset currentFrame or anything else and is used to switch to and from monochrome images
	public void swapFrames(BufferedImage[] frames) {
		this.frames = frames;
	}
	
	public void setDelay(long d) { delay = d; }
	public void setFrame(int i) { currentFrame = i; }
	
	public void update() {
		if(delay == -1) return;
		if(delay == 0) {
			delay = 1;
			System.out.println("Animation delay can't be 0, changing to 1.");
		}
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		
		while(elapsed >= delay) {
			elapsed -= delay;
			startTime = startTime + delay * 1000000;
			currentFrame++;
			if(currentFrame == frames.length) {
				currentFrame = 0;
				playedOnce = true;
			}
		}
		
		/*long elapsed = (System.nanoTime() - startTime) / 1000000;
		if(elapsed > delay) {
			currentFrame++;
			startTime = System.nanoTime();
		}
		if(currentFrame == frames.length) {
			currentFrame = 0;
			playedOnce = true;
		}*/
	}
	
	public int getFrame() { return currentFrame; }
	public long getDelay() { return delay; }
	public BufferedImage getImage() { return frames[currentFrame]; }
	public boolean hasPlayedOnce() { return playedOnce; }
}









