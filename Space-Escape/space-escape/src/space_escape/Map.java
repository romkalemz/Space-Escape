package space_escape;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import jig.Entity;
import jig.ResourceManager;
import jig.Vector;

public class Map {

	private int number_of_tilesX = 30;
	private int number_of_tilesY = 16;
	private Tile playerTile, enemyTile;
	private Tile[][] tiles, overlay;
	public int[] playerLocation; 
	
	public Map() {

		tiles = new Tile[number_of_tilesX][number_of_tilesY];
		
		//overlay stuff
		overlay = new Tile[number_of_tilesX][number_of_tilesY];
		// the tile highlighted in GREEN for overlay, multiple colors added for less transparency
		playerTile = new Tile(0, 0, 40, 40, false, Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		
		enemyTile = new Tile(0, 0, 40, 40, false, Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));

		
		// overlay setup
		for(int x = 0; x < number_of_tilesX; x++)
			for(int y = 0; y < number_of_tilesY; y++)
				overlay[x][y] = new Tile(20+40*x, 20+40*y, 40, 40, false, Game.TILE_OVERLAY_RSC);
	}
	
	public void loadLevel(int lvl) {
		// place empty tiles through the whole map first
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				Tile empty = new Tile(20+40*x, 20+40*y, 40, 40, false, "null");
				tiles[x][y] = empty;
			}
		}
		
		if (lvl == 1) {
			Tile astroidTile = new Tile(20 + 400, 20 + 400, 120, 120, true, Game.TILE_ASTROID1_RSC);
			tiles[10][10] = astroidTile;
		}
	}
	
	private Vector findTileLocation(Entity entity) {
		
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(entity.collides(overlay[x][y]) != null)
					return overlay[x][y].getPosition();
			}
		}
		return new Vector(0, 0);
	}

	
	public void renderOverlay(Graphics g, Game game) {
		
		Vector tilePlace = findTileLocation(game.player);
		playerTile.setPosition(tilePlace);
		tilePlace = findTileLocation(game.alien);
		enemyTile.setPosition(tilePlace);
		
		playerTile.render(g);
		enemyTile.render(g);
		//render the rest of the transparent overlay tiles
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(overlay[x][y] != null)
					overlay[x][y].render(g);
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
