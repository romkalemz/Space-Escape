package space_escape;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import jig.Entity;
import jig.Vector;

public class Map {

	public static int tileSize = 40;
	private int number_of_tilesX = 30;
	private int number_of_tilesY = 16;
	private Tile playerTile, enemyTile;
	private Tile[][] tiles;
	private Tile[][] overlay;

	
	public Map() {

		tiles = new Tile[number_of_tilesX][number_of_tilesY];
		
		//overlay stuff
		overlay = new Tile[number_of_tilesX][number_of_tilesY];
		// the tile highlighted in GREEN for overlay, multiple colors added for less transparency
		playerTile = new Tile(0, 0, 1, 1, false, Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		
		enemyTile = new Tile(0, 0, 1, 1, false, Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		

		
		// overlay setup
		for(int x = 0; x < number_of_tilesX; x++)
			for(int y = 0; y < number_of_tilesY; y++)
				overlay[x][y] = new Tile(x, y, 1, 1, false, Game.TILE_OVERLAY_RSC);
	}
	
	public void loadLevel(int lvl) {
		// place empty tiles through the whole map first
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				Tile empty = new Tile(x, y, 1, 1, false, "null");
				tiles[x][y] = empty;
			}
		}
		
		if (lvl == 1) {
			Tile astroidTile = new Tile(3, 2, 3, 3, true, Game.TILE_ASTROID1_RSC);
			for(int x = astroidTile.getTileX(); x < astroidTile.getSizeX() + astroidTile.getTileX(); x++) {
				for(int y = astroidTile.getTileY(); y < astroidTile.getSizeY() + astroidTile.getTileY(); y++ ) {
					tiles[x][y] = astroidTile;
				}
			}

		}
	}
	
	public Vector getPosition(Entity entity) {
		
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(entity.collides(overlay[x][y]) != null)
					return overlay[x][y].getPosition();
			}
		}
		return new Vector(0, 0);
	}
	
	public Vector getTilePosition(Entity entity) {
		int x = (int) entity.getX() / tileSize;
		int y = (int) entity.getY() / tileSize;
		return new Vector(x, y);
	}
	
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= number_of_tilesX || y >= number_of_tilesY)
			return null;
		return tiles[x][y]; 
	}

	
	public void renderOverlay(Graphics g, Game game) {
		
		Vector tilePlace = getPosition(game.player);
		playerTile.setPosition(tilePlace);
		tilePlace = getPosition(game.alien);
		enemyTile.setPosition(tilePlace);
		
		playerTile.render(g);
		enemyTile.render(g);
		//render the rest of the transparent overlay tiles
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				overlay[x][y].render(g);
				if(tiles[x][y].isSolid()) {
					Tile solidTile = new Tile(x-1, y-1, 1, 1, false, Game.TILE_OVERLAY_RSC, new Color(0, 0, 255));
					solidTile.render(g);
				}
			}
		}
	}
	

	public void render(Graphics g) {
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(tiles[x][y] != null)
					tiles[x][y].render(g);
			}
		}
	}
	
}
