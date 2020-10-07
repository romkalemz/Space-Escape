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
	//private int countdown;
	public float initSpeed;		//initial starting speed
	public float multSpeed;		//speed multiplier
	public float atkSpeed;		//the rate of fire from player
	public float atkDmg;		//the amount of hit points per bullet

	public Player(final float x, final float y, float initSp) {
		super(x, y);
		addImageWithBoundingBox(ResourceManager
				.getImage(Game.PLAYER_ORIGIN_RSC));
		velocity = new Vector(0, 0);
		initSpeed = initSp;
		multSpeed = atkSpeed = atkDmg = 1;
		
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
		Image img = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC);
		img.setRotation(dir);
		addImageWithBoundingBox(img);
	}
	
	public void checkBounds(int screenw, int screenh) {
		if(this.getCoarseGrainedMinX()<0) {
			this.setPosition(25, this.getY());
		}else if(this.getCoarseGrainedMaxX()>screenw){
			this.setPosition(screenw-25, this.getY());
		}
		
		if(this.getCoarseGrainedMinY()<0) {
			this.setPosition(this.getX(), 25);
		}else if(this.getCoarseGrainedMaxY()>screenh){
			this.setPosition(this.getX(), screenh-25);
		}
		
	}

	/**
	 * Bounce the ball off a surface. This simple implementation, combined
	 * with the test used when calling this method can cause "issues" in
	 * some situations. Can you see where/when? If so, it should be easy to
	 * fix!
	 * 
	 * @param surfaceTangent
	 */
//	public void bounce(float surfaceTangent) {
//		velocity = velocity.bounce(surfaceTangent);
//	}

	/**
	 * Update the Ball based on how much time has passed...
	 * 
	 * @param delta
	 *            the number of milliseconds since the last update
	 */
	public void update(final int delta) {
		translate(velocity.scale(delta));
	}

}
