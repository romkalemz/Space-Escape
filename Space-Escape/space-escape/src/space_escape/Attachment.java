package space_escape;

import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Attachment extends Entity {
	
	private Image image;
	private String type;
	
	
	public Attachment(final float x, final float y, String t) {
		super(x *40+20, y *40+20);
		type = t;
		
		if(type == "blue") {
			image = ResourceManager.getImage(Game.ATTACH_BLUE_RSC);
		}
		if(type == "red") {
			
		}
		if(type == "green") {
			
		}
		
		addImageWithBoundingBox(image);
		
	}
	
}
