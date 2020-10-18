package space_escape;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

public class Map {

	private int number_of_tilesX;
	private int number_of_tilesY;
	private int level;
	private Tile[][] grid;
	
	public Map(int lvl, int sx, int sy) {
		number_of_tilesX = sx;
		number_of_tilesY = sy;
		level = lvl;
		grid = new Tile[sx][sy];
	}
	
	public void loadLevel(int lvl) {
		if (lvl == 1) {
			
			for(int x = 0; x < number_of_tilesX; x++) {
				for(int y = 0; y < number_of_tilesY; y++) {
					Tile starTile = new Tile(20+40*x, 20+40*y, 40, 40, false, Game.TILE_OVERLAY_RSC);
					grid[x][y] = starTile;
				}
			}
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) {
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(grid[x][y] != null)
					grid[x][y].render(g);
			}
		}
	}
	
}
