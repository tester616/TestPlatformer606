package Main;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import Audio.JukeBox;
import GameState.GameStateManager;
import Handlers.Keys;
<<<<<<< HEAD
=======
import Support.Playtime;
>>>>>>> 2.03

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Runnable, KeyListener {

	// dimensions
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static int SCALE = 2;
	
	public static boolean SHOULDCHANGEJPANELSIZE;
	
	// game thread
	private Thread gameThread;
	private boolean running;
	public static int TARGETFPS = 60;
	public static int currentFps;
	public static int maxFps;
	//public static boolean showFps = true;
	//private long targetTime = 1000 / TARGETFPS;
	private long totalWaitTime = 1000 / TARGETFPS * 1000000;
	
	// loader thread
	private Thread loaderThread;
	
	// loading booleans & stuff
	// false to stop all loading
	public static boolean loading;
	// used when top priority is to load one piece or many specific pieces of audio
	public static boolean loadSpecificAudio;
	public static ArrayList<String> specificAudioToLoad = new ArrayList<String>();
	// loads unloaded assets freely while true and nothing of higher priority interferes
	public static boolean freeLoadingMoment;
	
	// image
	private BufferedImage image;
	private Graphics2D g;
	
	// game state manager
	private GameStateManager gsm;
	
<<<<<<< HEAD
=======
	// game time tracker
	public static Playtime playtime;
>>>>>>> 2.03
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(
				WIDTH * SCALE,
				HEIGHT * SCALE
		));
		setFocusable(true);
		requestFocus();
	}
	
	public void updateSize() {
		setPreferredSize(new Dimension(
				WIDTH * SCALE,
				HEIGHT * SCALE
		));
		setFocusable(true);
		requestFocus();
		
		Game.window.pack();
		
		image = new BufferedImage(
				WIDTH,
				HEIGHT,
				BufferedImage.TYPE_INT_RGB
		);
		g = (Graphics2D) image.getGraphics();
		
<<<<<<< HEAD
=======
		Game.window.setLocationRelativeTo(null);
		
>>>>>>> 2.03
		SHOULDCHANGEJPANELSIZE = false;
	}
	
	public void addNotify() {
		super.addNotify();
		
		JukeBox.init();
		
		loading = true;
		freeLoadingMoment = true;
		
		if(gameThread == null) {
			gameThread = new Thread(this);
			addKeyListener(this);
			gameThread.setPriority(Thread.MAX_PRIORITY);
			gameThread.start();
		}
		
		if(loaderThread == null) {
			loaderThread = new Thread(new Runnable() {
				public void run() {
			    	while(true) {
			    		loadAssets();
				    	try {
							Thread.sleep(500);
						}
				    	catch (InterruptedException e) {
							e.printStackTrace();
						}
			    	 }
			     }
			});
			loaderThread.setPriority(Thread.NORM_PRIORITY);
			loaderThread.start();
		}
	}

	private void loadAssets() {
		if(!loading) return;
		
		loadAudio();
	}
	
	private void loadAudio() {
		if(loadSpecificAudio) {
			for(int a = 0; a < specificAudioToLoad.size(); a++) {
				JukeBox.loadSpecificAudio(specificAudioToLoad.get(a));
				specificAudioToLoad.remove(a);
				a--;
			}
			loadSpecificAudio = false;
		}
		else {
			while(freeLoadingMoment && !JukeBox.isAllAudioLoaded()) {
				JukeBox.loadNext();
			}
		}
	}

	private void init() {
		// width and height makes up the total amount of pixels the image is composed of that's drawn to g on the jpanel
		image = new BufferedImage(
				WIDTH,
				HEIGHT,
				BufferedImage.TYPE_INT_RGB
		);
		g = (Graphics2D) image.getGraphics();
		
		running = true;
		
		gsm = new GameStateManager();
		
<<<<<<< HEAD
=======
		playtime = new Playtime();
>>>>>>> 2.03
	}
	
	public void run() {
		while(true) {
			
			if(JukeBox.containsClip("menu")) {
				
				freeLoadingMoment = false;
				
				init();
				
				long start;
				long elapsed;
				long waitTimeLeft;
				long currentTime;
				long waitUntil;
				long frameTotalTime;
				
				// game loop
				while(running) {
					start = System.nanoTime();
					
					update();
					draw();
					drawToScreen();
					
					elapsed = System.nanoTime() - start;
					
					// use this to see how long each frame roughly takes to execute
					//System.out.println(elapsed);
					
					waitTimeLeft = totalWaitTime - elapsed;
					currentTime = System.nanoTime();
					waitUntil = currentTime + waitTimeLeft;
					
					while(currentTime < waitUntil) {
						currentTime = System.nanoTime();
					}
					
					frameTotalTime = System.nanoTime() - start;
					maxFps = (int) (1000000000 / elapsed);
					currentFps = (int) (1000 / (frameTotalTime / 1000000));
				}
			}
			else {
				try {
					Thread.sleep(500);
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void update() {
		if(SHOULDCHANGEJPANELSIZE) updateSize();
		gsm.update();
		Keys.update();
	}
<<<<<<< HEAD
	private void draw() {
		gsm.draw(g);
	}
=======
	
	private void draw() {
		gsm.draw(g);
	}
	
>>>>>>> 2.03
	private void drawToScreen() {
		Graphics g2 = getGraphics();
		// width and height are responsible for how big the drawing area is on the jpanel
		g2.drawImage(image,
				0,
				0,
				WIDTH * SCALE,
				HEIGHT * SCALE,
				null
		);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {}
	public void keyPressed(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), true);
	}
	public void keyReleased(KeyEvent key) {
		Keys.keySet(key.getKeyCode(), false);
	}
	
}








