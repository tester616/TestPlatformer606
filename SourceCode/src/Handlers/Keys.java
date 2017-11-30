package Handlers;

import java.awt.event.KeyEvent;
//import Support.Support;

// this class contains a boolean array of current and previous key states
// for the 10 keys that are used for this game.
// a key k is down when keyState[k] is true.

public class Keys {
	
	public static final int NUM_KEYS = 46;
	
	public static boolean keyState[] = new boolean[NUM_KEYS];
	public static boolean prevKeyState[] = new boolean[NUM_KEYS];
	
	public static int UP = 0;
	public static int LEFT = 1;
	public static int DOWN = 2;
	public static int RIGHT = 3;
	public static int W = 4;
	public static int E = 5;
	public static int R = 6;
	public static int T = 7;
	public static int F = 8;
	public static int ENTER = 9;
	public static int ESC = 10;
	public static int K = 11;
	public static int O = 12;
	public static int P = 13;
	public static int SPACE = 14;
	public static int DELETE = 15;
	public static int F1 = 16;

	public static int num1 = 17;
	public static int num2 = 18;
	public static int num3 = 19;
	public static int num4 = 20;
	public static int num5 = 21;
	public static int num6 = 22;
	public static int num7 = 23;
	public static int num8 = 24;
	public static int num9 = 25;
	public static int num0 = 26;
	public static int Q = 27;
	public static int Y = 28;
	public static int U = 29;
	public static int I = 30;
	public static int A = 31;
	public static int S = 32;
	public static int D = 33;
	public static int G = 34;
	public static int H = 35;
	public static int J = 36;
	public static int L = 37;
	public static int Z = 38;
	public static int X = 39;
	public static int C = 40;
	public static int V = 41;
	public static int B = 42;
	public static int N = 43;
	public static int M = 44;
	public static int BACKSPACE = 45;
	
	public static void keySet(int i, boolean b) {
		if(i == KeyEvent.VK_UP) keyState[UP] = b;
		else if(i == KeyEvent.VK_LEFT) keyState[LEFT] = b;
		else if(i == KeyEvent.VK_DOWN) keyState[DOWN] = b;
		else if(i == KeyEvent.VK_RIGHT) keyState[RIGHT] = b;
		else if(i == KeyEvent.VK_W) keyState[W] = b;
		else if(i == KeyEvent.VK_E) keyState[E] = b;
		else if(i == KeyEvent.VK_R) keyState[R] = b;
		else if(i == KeyEvent.VK_T) keyState[T] = b;
		else if(i == KeyEvent.VK_F) keyState[F] = b;
		else if(i == KeyEvent.VK_ENTER) keyState[ENTER] = b;
		else if(i == KeyEvent.VK_ESCAPE) keyState[ESC] = b;
		else if(i == KeyEvent.VK_K) keyState[K] = b;
		else if(i == KeyEvent.VK_O) keyState[O] = b;
		else if(i == KeyEvent.VK_P) keyState[P] = b;
		else if(i == KeyEvent.VK_SPACE) keyState[SPACE] = b;
		else if(i == KeyEvent.VK_DELETE) keyState[DELETE] = b;
		else if(i == KeyEvent.VK_F1) keyState[F1] = b;
		
		else if(i == KeyEvent.VK_1) keyState[num1] = b;
		else if(i == KeyEvent.VK_2) keyState[num2] = b;
		else if(i == KeyEvent.VK_3) keyState[num3] = b;
		else if(i == KeyEvent.VK_4) keyState[num4] = b;
		else if(i == KeyEvent.VK_5) keyState[num5] = b;
		else if(i == KeyEvent.VK_6) keyState[num6] = b;
		else if(i == KeyEvent.VK_7) keyState[num7] = b;
		else if(i == KeyEvent.VK_8) keyState[num8] = b;
		else if(i == KeyEvent.VK_9) keyState[num9] = b;
		else if(i == KeyEvent.VK_0) keyState[num0] = b;
		else if(i == KeyEvent.VK_Q) keyState[Q] = b;
		else if(i == KeyEvent.VK_Y) keyState[Y] = b;
		else if(i == KeyEvent.VK_U) keyState[U] = b;
		else if(i == KeyEvent.VK_I) keyState[I] = b;
		else if(i == KeyEvent.VK_A) keyState[A] = b;
		else if(i == KeyEvent.VK_S) keyState[S] = b;
		else if(i == KeyEvent.VK_D) keyState[D] = b;
		else if(i == KeyEvent.VK_G) keyState[G] = b;
		else if(i == KeyEvent.VK_H) keyState[H] = b;
		else if(i == KeyEvent.VK_J) keyState[J] = b;
		else if(i == KeyEvent.VK_L) keyState[L] = b;
		else if(i == KeyEvent.VK_Z) keyState[Z] = b;
		else if(i == KeyEvent.VK_X) keyState[X] = b;
		else if(i == KeyEvent.VK_C) keyState[C] = b;
		else if(i == KeyEvent.VK_V) keyState[V] = b;
		else if(i == KeyEvent.VK_B) keyState[B] = b;
		else if(i == KeyEvent.VK_N) keyState[N] = b;
		else if(i == KeyEvent.VK_M) keyState[M] = b;
		else if(i == KeyEvent.VK_BACK_SPACE) keyState[BACKSPACE] = b;
	}
	
	public static void update() {
		for(int i = 0; i < NUM_KEYS; i++) {
			prevKeyState[i] = keyState[i];
		}
	}
	
	public static boolean isPressed(int i) {
		return keyState[i] && !prevKeyState[i];
	}
	
	public static boolean anyKeyPress() {
		for(int i = 0; i < NUM_KEYS; i++) {
			if(keyState[i]) return true;
		}
		return false;
	}
	
}
