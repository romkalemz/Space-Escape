package space_escape;

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
	//private int countdown;
	public float initSpeed;		//initial starting speed
	public float multSpeed;		//speed multiplier
	public float atkSpeed;		//the rate of fire from player
	public float atkDmg;		//the amount of hit points per bullet
	public float hp;			//health of the player

	public Player(final float x, final float y, float initSp) {
		super(x, y);
		image = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC).getScaledCopy(0.5f);
		addImageWithBoundingBox(image);
		
		velocity = new Vector(0, 0);
		initSpeed = initSp;
		multSpeed = atkSpeed = atkDmg = 1;
		hp = 3;
		
		//countdown = 0;
	}

	public void setVelocity(final Vector v) {
		velocity = v;
	}

	public Vector getVelocity() {
		return velocity;
	}
	
	public void setRotation(int dir) {
		//countdown = 500;
		image.setRotation(dir);
		addImageWithBoundingBox(image);
	}
	
	public void checkBounds(int screenw, int screenh) {
		if(this.getCoarseGrainedMinX()<0) {
			this.setPosition(15, this.getY());
		}else if(this.getCoarseGrainedMaxX()>screenw){
			this.setPosition(screenw-15, this.getY());
		}
		
		if(this.getCoarseGrainedMinY()<0) {
			this.setPosition(this.getX(), 15);
		}else if(this.getCoarseGrainedMaxY()>screenh){
			this.setPosition(this.getX(), screenh-15);
		}
		
	}
	
	public void checkCollision(Map map) {
		
		float wiggle_room = initSpeed * 20;
		//use the players current location to check surroundings
		Vector playerPos = map.getTilePosition(this);
		//grab all the tiles around the player
		Tile w 		= map.getTile((int) playerPos.getX() - 1, (int) playerPos.getY());
		Tile n 		= map.getTile((int) playerPos.getX(), (int) playerPos.getY() - 1);
		Tile e 		= map.getTile((int) playerPos.getX() + 1, (int) playerPos.getY());
		Tile s 		= map.getTile((int) playerPos.getX(), (int) playerPos.getY() + 1);
		Tile nw 	= map.getTile((int) playerPos.getX() - 1, (int) playerPos.getY() - 1);
		Tile ne 	= map.getTile((int) playerPos.getX() + 1, (int) playerPos.getY() - 1);
		Tile se 	= map.getTile((int) playerPos.getX() + 1, (int) playerPos.getY() + 1);
		Tile sw 	= map.getTile((int) playerPos.getX() - 1, (int) playerPos.getY() + 1);
		
		//check collision between the tiles and the player
		if(w != null) {
			if(w.isSolid() && collides(w) != null)
				this.setX(w.getCoarseGrainedMaxX() + 20);
		}
		if(e != null) {
			if(e.isSolid() && collides(e) != null)
				this.setX(e.getCoarseGrainedMinX() - 20);
		}
		if(n != null) {
			if(n.isSolid() && collides(n) != null)
				this.setX(n.getCoarseGrainedMaxY() + 20);
		}
		if(s != null) {
			if(s.isSolid() && collides(s) != null)
				this.setX(s.getCoarseGrainedMinY() - 20);
		}
		//checking corner tiles
		if(nw != null && nw.isSolid() && collides(nw) != null) {
			if(nw.getCoarseGrainedMaxY() - getCoarseGrainedMinY() > wiggle_room)
				this.setX(nw.getCoarseGrainedMaxX() + 20);
			if(nw.getCoarseGrainedMaxX() - getCoarseGrainedMinX() > wiggle_room)
				this.setX(nw.getCoarseGrainedMaxY() + 20);
		}
		if(se != null && se.isSolid() && collides(se) != null) {
			if(getCoarseGrainedMaxY() - se.getCoarseGrainedMinY() > wiggle_room)
				this.setX(se.getCoarseGrainedMinX() - 20);
			if(getCoarseGrainedMaxX() - se.getCoarseGrainedMinX() > wiggle_room)
				this.setX(se.getCoarseGrainedMinY() - 20);
		}
		if(sw != null && sw.isSolid() && collides(sw) != null) {
			if(getCoarseGrainedMaxY() - sw.getCoarseGrainedMinY() > wiggle_room)
				this.setX(sw.getCoarseGrainedMaxX() + 20);
			if(sw.getCoarseGrainedMaxX() - getCoarseGrainedMinX() > wiggle_room)
				this.setX(sw.getCoarseGrainedMinY() - 20);
		}
		if(ne != null && ne.isSolid() && collides(ne) != null) {
			if(ne.getCoarseGrainedMaxY() - getCoarseGrainedMinY() > wiggle_room)
				this.setX(ne.getCoarseGrainedMinX() - 20);
			if(getCoarseGrainedMaxX() - ne.getCoarseGrainedMinX() > wiggle_room)
				this.setX(ne.getCoarseGrainedMaxY() + 20);
		}
			
	}

	public void update(final int delta) {
		translate(velocity.scale(delta));
	}

}
