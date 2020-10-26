package space_escape;

import java.util.ArrayList;

import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

/**
 * The Ball class is an Entity that has a velocity (since it's moving). When
 * the Ball bounces off a surface, it temporarily displays a image with
 * cracks for a nice visual effect.
 * 
 */
 class Player extends Entity {

	private Vector velocity;
	public Image image;
	
	public float initSpeed;		// initial starting speed
	public int rof;				// the rate of fire from player
	public float bulletSpeed;	// the speed of bullet travel
	public int atkDmg;			// the amount of hit points per bullet
	public float moveSpeed;		// the movement speed of player
	public float HP;			// health of the player
	public float totalHP;		// the max hp the player can have
	public boolean regen_enabled;
	public int regen_cooldown;
	public float pushback = 20;	// amount to push the player back once collided
	
	public int orbCount = 0;
	
	public Player(final float x, final float y, float initSp) {
		super(x, y);
		image = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC).getScaledCopy(40, 40);
		image.setRotation(180);
		addImageWithBoundingBox(image);
		
		velocity = new Vector(0, 0);
		//initSpeed = initSp;
		rof = 450;
		atkDmg = 1;
		totalHP = HP = 5;
		moveSpeed = 0.21f;
		bulletSpeed = 0.3f;
		regen_enabled = false;
		//attachments = new ArrayList<Orb>();
	}
	
	
	public void setStats(Orb orb) {
	
		if(orb.type == "red") {
			if(atkDmg == 3)
				atkDmg += 2;
			else 
				atkDmg += 1;
		}
		if(orb.type == "blue") {
			moveSpeed += 0.05;
			if(rof == 150) {
				rof -= 50;
			} else
				rof -= 150;
		}
		if(orb.type == "green") {
			//totalHPincrease = true;
			HP += 1;
			totalHP++;
			regen_enabled = true;
			regen_cooldown = 2000;
		}
		orbCount++;
	}
	
	public void removeStats(Orb orb) {
		if(orb.type == "red") {
			if(atkDmg == 5) {
				atkDmg -= 2;
			} else
				atkDmg -= 1;
		}
		if(orb.type == "blue") {
			moveSpeed -= 0.05;
			rof += 150;
		}
		if(orb.type == "green") {
			if(HP > 1) {
				totalHP--;
				HP -= 1;	
			}
			else
				totalHP--;
			regen_enabled = false;
		}
		orbCount--;
	}
	

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}
	
	public void setRotation(int dir) {
		image.setRotation(dir);
		addImageWithBoundingBox(image);
	}
	
	public void checkBounds(int screenw, int screenh) {
		if(this.getCoarseGrainedMinX()<0) {
			this.setPosition(pushback, this.getY());
		}else if(this.getCoarseGrainedMaxX()>screenw){
			this.setPosition(screenw-pushback, this.getY());
		}
		
		if(this.getCoarseGrainedMinY()<0) {
			this.setPosition(this.getX(), pushback);
		}else if(this.getCoarseGrainedMaxY()>screenh){
			this.setPosition(this.getX(), screenh-pushback);
		}
		
	}
	
	public void checkCollision(Map map) {
		
		// CHECKING OUTER SIDES OF PLAYERS' TILES METHOD
		int sideX = (int) Math.floor(getX() / map.tileSize);
		int sideY = (int) Math.floor(getY() / map.tileSize);
		Tile t;
		// checking W side
		if(sideX + 1 < map.number_of_tilesX) {
			t = map.getTile(sideX +1, sideY);
			if(t.isSolid() && collides(t) != null) {
				setX(t.getCoarseGrainedMinX() - pushback);
			}
		}
		// checking N side
		if(sideY + 1 < map.number_of_tilesY) {
			t = map.getTile(sideX, sideY + 1);
			if(t.isSolid() && collides(t) != null) {
				setY(t.getCoarseGrainedMinY() - pushback);
			}
		}
		// checking E side
		if(sideX - 1 > 0) {
			t = map.getTile(sideX - 1, sideY);
			if(t.isSolid() && collides(t) != null) {
				setX(t.getCoarseGrainedMaxX() + pushback);
			}
		}
		// checking S side
		if(sideY - 1 > 0) {
			t = map.getTile(sideX, sideY - 1);
			if(t.isSolid() && collides(t) != null) {
				setY(t.getCoarseGrainedMaxY() + pushback);
			}
		}	
	}
	
	public boolean checkOrbCollision(Orb o) {
		if(collides(o) != null) {
			return true;
		} else
			return false;
	}

	public void update(final int delta) {
		// update health if you have regen
		if(regen_enabled && regen_cooldown <= 0) {
			if(HP < totalHP) {
				HP++;
				if(totalHP == 6)
					regen_cooldown = 4000;
				else if(totalHP == 7)
					regen_cooldown = 3000;
				else if(totalHP == 8)
					regen_cooldown = 1000;
			}
		}
		regen_cooldown -= delta;
		translate(velocity.scale(delta * moveSpeed));
	}

}
