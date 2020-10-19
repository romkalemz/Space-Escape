package space_escape;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Tile extends Entity {
	
	private boolean isSolid;
	float sizeX, sizeY;
	Image image;

	// basic constructor with location, size, if its a solid, and the texture
	public Tile(float x, float y, float sx, float sy, boolean solid, String texture) {
		super(x, y);
		sizeX = sx;
		sizeY = sy;
		isSolid = solid;
		if(texture != "null") {
			image = ResourceManager.getImage(texture).getScaledCopy((int)sx, (int)sy);
			addImageWithBoundingBox(image);
		}
	}
	
	// constructor allowing for color change
	public Tile(float x, float y, float sx, float sy, boolean solid, String texture, Color c) {
		super(x, y);
		sizeX = sx;
		sizeY = sy;
		isSolid = solid;
		image = ResourceManager.getImage(texture).getScaledCopy((int)sx, (int)sy);
		image.setImageColor(c.r, c.g, c.b);
		addImageWithBoundingBox(image);
	}
	
	// change the color of the already constructed Tile
	public void changeImage(String rsc, Color c) {
		if(rsc == "null") {
			this.removeImage(image);
			return;
		}
		Image newImage = ResourceManager.getImage(rsc).getScaledCopy((int)sizeX, (int)sizeY);
		if(c != null)
			newImage.setImageColor(c.r, c.g, c.b);
		addImageWithBoundingBox(newImage);
	}
	
}
