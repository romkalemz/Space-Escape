package space_escape;

import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Tile extends Entity {
	
	private boolean isSolid;
	
	public Tile(float x, float y, float sx, float sy, boolean solid, String rsc) {
		super(x, y);
		isSolid = solid;
		Image img = ResourceManager.getImage(rsc).getScaledCopy((int)sx, (int)sy);
		//img.setFilter(Image.FILTER_LINEAR);
		addImageWithBoundingBox(img);
	}
}
