package space_escape;

import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Bullet extends Entity {
	
	public Image image;
	private float speed;
	public float force;		// size of bullet where size = 1 is tile size / 2
	public int damage;			// amount of hit points it damages
	private int rof;			// rate of fire of the bullet (1 = 1 bullet/sec)
	private String type;		// type of bullet
	public Vector velocity;	// direction the bullet is travelling 
	public boolean isFromEnemy;
	
	public void setSpeed(float sp) 		{ speed = sp; }
	//public void setSize(double s) 		{ size = s; }
	public void setDamage(int d)		{ damage = d; }
	public void setType(String t)		{ type = t; }
	public void setROF(int r)			{ rof = r; }
	
	public void setVelocity(Vector v) { 
		velocity = v;
	}
	
	public Vector getDirection() {
		Vector v = velocity;
		Vector normal = new Vector(1, 1);
		return normal.setRotation(v.getRotation());
	}
	
	public void setDirection(Entity e, Vector v) {
		setPosition(e.getPosition());
		setVelocity(v);
		image.setRotation((int) v.getRotation() + 90);
	}
	
	public Bullet(final float x, final float y) {
		super(x, y);
		velocity = new Vector(0, 0);
		speed = 0.25f;
		damage = 1;
		force = 1;
		image = ResourceManager.getImage(Game.BULLET_REGULAR_RSC).getScaledCopy(5, 10);
		addImageWithBoundingBox(image);
	}
	
	public boolean isCollided(Map m, int sw, int sh) {
		return (checkBounds(sw, sh) || checkTileCollision(m));
	}
	
	public boolean checkBounds(int screenw, int screenh) {
		if(this.getCoarseGrainedMinX()<0 || this.getCoarseGrainedMaxX()>screenw-10)
			return true;
		
		if(this.getCoarseGrainedMinY()<0 || this.getCoarseGrainedMaxY()>screenh-10)
			return true;

		return false;
	}
	
	public boolean checkTileCollision(Map map) {
		
		// CHECKING OUTER SIDES OF PLAYERS' TILES METHOD
		int sideX = (int) Math.floor(getX() / map.tileSize);
		int sideY = (int) Math.floor(getY() / map.tileSize);
		Tile t;
		// checking W side
		if(sideX + 1 < map.number_of_tilesX) {
			t = map.getTile(sideX +1, sideY);
			if(t.isSolid() && collides(t) != null) {
				return true;
			}
		}
		// checking N side
		if(sideY + 1 < map.number_of_tilesY) {
			t = map.getTile(sideX, sideY + 1);
			if(t.isSolid() && collides(t) != null) {
				return true;
			}
		}
		// checking E side
		if(sideX - 1 > 0) {
			t = map.getTile(sideX - 1, sideY);
			if(t.isSolid() && collides(t) != null) {
				return true;
			}
		}
		// checking S side
		if(sideY - 1 > 0) {
			t = map.getTile(sideX, sideY - 1);
			if(t.isSolid() && collides(t) != null) {
				return true;
			}
		}
		return false;
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta*speed));
	}

}
