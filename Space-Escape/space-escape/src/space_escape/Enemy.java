package space_escape;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Enemy extends Entity {
	
	public Image image;
	private ArrayList<Vector> followPath;
	private float speed;
	private Vector velocity;
	
	public Enemy(final float x, final float y, String type) {
		super(x, y);
		
		followPath = null;
		speed = 0.8f;
		velocity = new Vector(0.0f, 0.0f);
		
		if(type == "alien") {
			image = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC).getScaledCopy(0.5f);
			image.setImageColor(0.5f, 0.5f, 0.5f);
			addImageWithBoundingBox(image);
		}
	}
	
	public void traversePath(ArrayList<Vector> path) {
		followPath = path;
		//velocity = followPath.get(0);
		
	}
	
	public void renderPath(Graphics g) {
		for(int i = 0; i < followPath.size()-1; i++) {
			Vector curPoint = followPath.get(i);
			Vector nextPoint = followPath.get(i+1);
			g.drawLine(curPoint.getX(), curPoint.getY(), nextPoint.getX(), nextPoint.getY());
		}
	}
	
	public void update(final int delta) {
		translate(velocity.scale(delta * speed));
	}

}
