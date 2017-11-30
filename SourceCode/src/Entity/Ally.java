package Entity;

import java.util.ArrayList;

import TileMap.TileMap;

public class Ally extends MapObject {

		protected int health;
		protected int maxHealth;
		protected boolean dead;
		protected int damage;
		protected int cost;
		protected boolean flinching;
		protected long flinchTimer;
		protected int mode;
		protected Object extraData;
		
		public static final int MODE_DEFAULT = 0;
		
		public Ally(TileMap tm, int mode, Object extraData) {
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
		
		public void checkAttack(ArrayList<Enemy> enemies) {
			// loop through enemies
			for(int i = 0; i < enemies.size(); i++) {
				
				Enemy e = enemies.get(i);
				
				// attack enemy if conditions are met
				if(intersects(e) && !pacified && !e.invulnerable) {
					e.hit(damage);
				}
			}
		}
		
		public void update() {
			
		}
		
		public void setPlayerInformation(double playerX, double playerY) {
			
		}
		
		protected void setExtraData() {
			
		}
}
