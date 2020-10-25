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
	
	public float initSpeed;		//initial starting speed
	public float multSpeed;		//speed multiplier
	public float atkSpeed;		//the rate of fire from player
	public float atkDmg;		//the amount of hit points per bullet
	public float hp;			//health of the player
	public float pushback = 20;	//amount to push the player back once collided
	
	public ArrayList<Attachment> attachments;
	
	public Player(final float x, final float y, float initSp) {
		super(x, y);
		image = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC).getScaledCopy(40, 40);
		image.setRotation(180);
		addImageWithBoundingBox(image);
		
		velocity = new Vector(0, 0);
		initSpeed = initSp;
		multSpeed = atkSpeed = atkDmg = 1;
		hp = 3;
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

	public void update(final int delta) {
		translate(velocity.scale(delta));
	}

}
