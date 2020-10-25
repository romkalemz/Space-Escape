package space_escape;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Orb extends Entity {
	
	private String type;
	private Animation anim;
	
	
	public Orb(final float x, final float y, String t) {
		super(x *40+20, y *40+20);
		type = t;
		Animation a = null;
		
		if(type == "blue") {
			a = new Animation(ResourceManager.getSpriteSheet(
				Game.ORB_BLUE_RSC, 20, 20), 0, 0, 3, 0, false, 50, true);
		}
		else if(type == "red") {
			a = new Animation(ResourceManager.getSpriteSheet(
				Game.ORB_RED_RSC, 20, 20), 0, 0, 3, 0, false, 50, true);
			
		}
		else if(type == "green") {
			a = new Animation(ResourceManager.getSpriteSheet(
					Game.ORB_GREEN_RSC, 20, 20), 0, 0, 3, 0, false, 50, true);
		}
		
		a.setLooping(true);
		addAnimation(a);
	}
	
}
