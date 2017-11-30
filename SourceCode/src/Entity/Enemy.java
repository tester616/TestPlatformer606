package Entity;

import TileMap.TileMap;

public class Enemy extends MapObject {

		protected int health;
		protected int maxHealth;
		protected boolean dead;
		protected int damage;
		protected int cost;
		protected int mode;
		protected Object extraData;
		
		public static final int MODE_DEFAULT = 0;
		
		public Enemy(TileMap tm, int mode, Object extraData) {
			super(tm);
			this.mode = mode;
			this.extraData = extraData;
		}
		
		public boolean isDead() { return dead; }
		
		public int getDamage() { return damage; }
		
		public void hit(int damage) {
			if(dead || flinching) return;
			health -= damage;
			if(health < 0) health = 0;
			if(health == 0) dead = true;
			flinching = true;
			flinchTimer = System.nanoTime();
		}
		
		public void update() {
			
		}
		
		public void setPlayerInformation(double playerX, double playerY) {
			
		}
		
		public int getCost() {
			return cost;
		}
}
