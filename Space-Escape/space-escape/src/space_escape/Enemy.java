package space_escape;

import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Enemy extends Entity {
	
	public Image image;
	
	public Enemy(final float x, final float y, String type) {
		super(x, y);
		if(type == "type1") {
			image = ResourceManager.getImage(Game.PLAYER_ORIGIN_RSC).getScaledCopy(0.5f);
			image.setImageColor(0.5f, 0.5f, 0.5f);
			addImageWithBoundingBox(image);
		}
	}

}
