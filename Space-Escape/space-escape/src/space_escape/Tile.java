package space_escape;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;

public class Tile extends Entity implements Comparable<Tile> {
	
	// Tile elements
	private boolean solid;
	private int x, y;
	private int sizeX, sizeY;
	Image image;
	
	// Dijkstra's elements
	public float cost;
	public Tile prev;
	public ArrayList<Tile> neighbors;
	public boolean corner;
	public boolean visited;
	
	public boolean isSolid() 	{ return solid; }
	public boolean isCorner() 	{ return corner; }
	
	public int getTileX() { return x; }
	public int getTileY() { return y; }
	
	public float getSizeX() { return sizeX; }
	public float getSizeY() { return sizeY; }

	// basic constructor with location, size, if its a solid, and the texture
	public Tile(int x, int y, int sx, int sy, boolean sol, String texture) {
		super(x *40+20, y *40+20);
		this.x = x;
		this.y = y;
		sizeX = sx;
		sizeY = sy;
		solid = sol;
		corner = false;
		visited = false;
		cost = (float) Double.POSITIVE_INFINITY;
		prev = null;
		if(texture != "null") {
			image = ResourceManager.getImage(texture).getScaledCopy(40*sx, 40*sy);
			addImageWithBoundingBox(image);
		}
	}
	
	// constructor allowing for color change
	public Tile(int x, int y, int sx, int sy, boolean sol, String texture, Color c) {
		super(x *40+20, y *40+20);
		this.x = x;
		this.y = y;
		sizeX = sx;
		sizeY = sy;
		solid = sol;
		image = ResourceManager.getImage(texture).getScaledCopy(40*sx, 40*sy);
		image.setImageColor(c.r, c.g, c.b);
		addImageWithBoundingBox(image);
	}
	
	// change the color of the already constructed Tile
	public void changeImage(String rsc, Color c) {
		if(rsc == "null") {
			this.removeImage(image);
			return;
		}
		Image newImage = ResourceManager.getImage(rsc).getScaledCopy(40*sizeX, 40*sizeY);
		if(c != null)
			newImage.setImageColor(c.r, c.g, c.b);
		addImageWithBoundingBox(newImage);
	}

	@Override
	public int compareTo(Tile o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
