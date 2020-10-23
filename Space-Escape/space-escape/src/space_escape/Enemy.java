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
	private ArrayList<Vector> followPath;
	private int followPoint;
	private float speed;
	private Vector velocity;
	
	public Enemy(final float x, final float y, String type) {
		super(x, y);
		
		followPath = null;
		followPoint = 0;
		speed = 0.1f;
		velocity = new Vector(0.0f, 0.0f);
		
		if(type == "alien") {
			image = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC).getScaledCopy(0.5f);
			image.setImageColor(0.5f, 0.5f, 0.5f);
			addImageWithBoundingBox(image);
		}
	}
	
	public void setPath(ArrayList<Vector> path) {
		followPath = path;
		if (followPath.size() > 1)
			followPoint = 1;
		else
			followPoint = 0;
	}
	
	public void followPath() {
		Vector vel = new Vector(0, 0);
		Vector des = followPath.get(followPoint);
		Vector dif = new Vector(des.getX()-getX(), des.getY()-getY());
		
		if (dif.length() <= 3) {
			followPoint++;
			if(followPoint >= followPath.size())
				followPoint = 0;
			setPosition(des.getX(), des.getY());
		}
		else
			vel = new Vector(dif.getX() / dif.length(), dif.getY() / dif.length());
		
		velocity = vel;
	}
	
	public void renderPath(Graphics g) {
		for(int i = 0; i < followPath.size()-1; i++) {
			Vector curPoint = followPath.get(i);
			Vector nextPoint = followPath.get(i+1);
			g.drawGradientLine(curPoint.getX(), curPoint.getY(), new Color(255, 0, 0),
							   nextPoint.getX(), nextPoint.getY(), new Color(0, 255, 0));
		}
	}
	
	public void update(final int delta) {
		followPath();
		translate(velocity.scale(delta * speed));
	}

}
