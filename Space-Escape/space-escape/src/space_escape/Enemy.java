package space_escape;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Enemy extends Entity {
	
	public Image image;
	public String type;
	public ArrayList<Vector> path;
	private int followPoint;
	private float speed;
	public Vector velocity;
	public Vector pushback;
	public int hp;
	public int KO;			// knocked out cooldown (unable to traverse path if hit)
	
	
	public Enemy(final float x, final float y, String type) {
		super(x *40+20, y *40+20);
		
		this.type = type;
		path = new ArrayList<Vector>();
		followPoint = 0;
		velocity = new Vector(0, 0);
		KO = 0;
		
		if(type == "alien") {
			hp = 5;
			speed = 0.15f;
			pushback = new Vector(15, 15);
			image = ResourceManager.getImage(Game.ENEMY_ALIEN_RSC).getScaledCopy((int)pushback.getX() *2, (int)pushback.getY() *2);
			addImageWithBoundingBox(image);
		}
		else if(type == "ufo") {
			hp = 5;
			speed = 0.12f;
			pushback = new Vector(20, 15);
			image = ResourceManager.getImage(Game.ENEMY_UFO_RSC).getScaledCopy((int)pushback.getX() *2, (int)pushback.getY() *2);
			addImageWithBoundingBox(image);
		}
		else if(type == "robot") {
			hp = 10;
			speed = 0.1f;
			pushback = new Vector(20, 20);
			image = ResourceManager.getImage(Game.ENEMY_ROBOT_RSC).getScaledCopy((int)pushback.getX() *2, (int)pushback.getY() *2);
			addImageWithBoundingBox(image);
		}
	}
	
	public Orb dropOrb() {
		return null;
	}
	
	public void setPath(Tile current) {
		path.clear();
		while(current != null) {
			Vector loc = current.getPosition();
			path.add(loc);
			current = current.prev;
		}
	}
	
	public void traversePath() {
		Vector vel = new Vector(0, 0);
		if(path.isEmpty())
			return;
		if(path.size() == 1)
			followPoint = 0;
		Vector des = path.get(followPoint);
		Vector dif = new Vector(des.getX()-getX(), des.getY()-getY());
		
		if (dif.length() <= 3) {
			followPoint++;
			if(followPoint >= path.size())
				followPoint = 0;
			setPosition(des.getX(), des.getY());
		}
		else
			vel = new Vector(dif.getX() / dif.length(), dif.getY() / dif.length());
		
		velocity = vel;
	}

	
	public void renderPath(Graphics g) {
		for(int i = 0; i < path.size()-1; i++) {
			Vector first = path.get(i);
			Vector next = path.get(i+1);
			g.drawGradientLine(first.getX(), first.getY(), new Color(255, 0, 0),
							   next.getX(), next.getY(), new Color(0, 255, 0));
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
				setX(t.getCoarseGrainedMinX() - pushback.getX());
			}
		}
		// checking N side
		if(sideY + 1 < map.number_of_tilesY) {
			t = map.getTile(sideX, sideY + 1);
			if(t.isSolid() && collides(t) != null) {
				setY(t.getCoarseGrainedMinY() - pushback.getY());
			}
		}
		// checking E side
		if(sideX - 1 > 0) {
			t = map.getTile(sideX - 1, sideY);
			if(t.isSolid() && collides(t) != null) {
				setX(t.getCoarseGrainedMaxX() + pushback.getX());
			}
		}
		// checking S side
		if(sideY - 1 > 0) {
			t = map.getTile(sideX, sideY - 1);
			if(t.isSolid() && collides(t) != null) {
				setY(t.getCoarseGrainedMaxY() + pushback.getY());
			}
		}	
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta * speed));
	}






}
