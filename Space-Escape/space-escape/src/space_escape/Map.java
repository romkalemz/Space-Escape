package space_escape;

import java.util.ArrayList;
import java.util.PriorityQueue;

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
		
		// overlay stuff
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
	
	// find shortest path between two entities
	// returns an array of points needed to follow
	public ArrayList<Vector> dijkstraPath(Entity enemy, Entity player) {
		int a = 1;
		
		// STEP 1, populate graph with costs of tiles from enemy to player
		// place initial tile and neighbors into queue and adjust costs
		Tile startTile = getTile(enemy);				// grab first tile
		startTile.cost = 0;								// set cost of travel to 0
		// unexplored tiles are placed in the queue
		PriorityQueue<Tile> Q = new PriorityQueue<Tile>();
		Q.add(startTile);								// add to the queue/stack
		// if tile is unexplored, visit it and adjust costs
		while(!Q.isEmpty()) {
			Tile curTile = Q.poll();						// pop the head of the stack
			curTile.neighbors = findNeighbors(curTile);		// finds all valid tiles around it
			// adjust and compare values of neighboring tiles, add to Q
			for (int i = 0; i < curTile.neighbors.size(); i++) {
				Tile neighbor = curTile.neighbors.get(i);
				// check if you can walk on the neighbor tile AND adjust costs
				// first check if this is a neighboring corner tile
				if(!neighbor.isCorner()) {
					if(!neighbor.isSolid() && neighbor.cost > curTile.cost + 1) {
						neighbor.cost = curTile.cost + 1;	// reduce cost of neighbor
															// since its closer than before
						neighbor.prev = curTile;			// keep track of the prev
															// so you can backtrack
						Q.add(neighbor);					// add to Queue and repeat
					}
				}
				else if(neighbor.isCorner()) {
					if(!neighbor.isSolid() && neighbor.cost > curTile.cost + 1.5) {
						neighbor.cost = curTile.cost + 1.5f;	// reduce cost of neighbor
																// since its closer than before
						neighbor.prev = curTile;				// keep track of the prev
																// so you can backtrack
						Q.add(neighbor);						// add to Queue and repeat
					}
				}
				
			}
		}
		// STEP 2, trace back from player to enemy with previous tiles
		Tile endTile = getTile(player);
		Tile previousTile = endTile;
		ArrayList<Tile> backPath = new ArrayList<Tile>();
		a = 1;
		// follow the previous tiles until you reach the enemy (null)
		while(previousTile != null) {
			backPath.add(previousTile);
			previousTile = previousTile.prev;
		}
		// STEP 3, return a reversed array of points from step 2
		ArrayList<Vector> shortestPath = new ArrayList<Vector>();
		// add points from the backPath to the shortest path in reverse
		// for the enemy to follow the shortest path towards the player
		for (int i = backPath.size()-1; i >= 0; i--)
			shortestPath.add(backPath.get(i).getPosition());
		
		return shortestPath;
	}
	
	private ArrayList<Tile> findNeighbors(Tile t) {
		Vector pos = getTilePosition(t);
		int x = (int)pos.getX();
		int y = (int)pos.getY();
		
		ArrayList<Tile> n = new ArrayList<Tile>();
		// side tiles
		if(x > 0)							
			n.add(tiles[x-1][y]);	
		if(x < number_of_tilesX-1)	
			n.add(tiles[x+1][y]);	
		if(y > 0)							
			n.add(tiles[x][y-1]);
		if(y < number_of_tilesY-1)			
			n.add(tiles[x][y+1]);
		
		// corner tiles
		if(x > 0 && y > 0) {
			tiles[x-1][y-1].corner = true;
			n.add(tiles[x-1][y-1]);
		}	
		if(x > 0 && y < number_of_tilesY-1)	{
			tiles[x-1][y+1].corner = true;
			n.add(tiles[x-1][y+1]);	
		}	
		if(x < number_of_tilesX-1 && y > 0)	{
			tiles[x+1][y-1].corner = true;
			n.add(tiles[x+1][y-1]);
		}	
		if(x < number_of_tilesX-1 && y < number_of_tilesY-1) {
			tiles[x+1][y+1].corner = true;
			n.add(tiles[x+1][y+1]);
		}
			
		return n;
	}

	// returns position of entity in pixel scale
	public Vector getPosition(Entity entity) {
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(entity.collides(overlay[x][y]) != null)
					return overlay[x][y].getPosition();
			}
		}
		return new Vector(0, 0);
	}
	// returns position of the entity in tile scale
	public Vector getTilePosition(Entity entity) {
		int x = (int) entity.getX() / tileSize;
		int y = (int) entity.getY() / tileSize;
		return new Vector(x, y);
	}
	// returns the tile based on the tile scale arguments
	public Tile getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= number_of_tilesX || y >= number_of_tilesY)
			return null;
		return tiles[x][y]; 
	}
	// returns the tile based on the entity argument
	public Tile getTile(Entity entity) {
		Vector tempPos = getTilePosition(entity);
		return getTile((int)tempPos.getX(), (int)tempPos.getY());
	}

	
	public void renderOverlay(Graphics g, Game game) {
		Vector tilePlace = getPosition(game.player);
		playerTile.setPosition(tilePlace);
		tilePlace = getPosition(game.alien);
		enemyTile.setPosition(tilePlace);
		
		playerTile.render(g);
		enemyTile.render(g);
		// render the rest of the transparent overlay tiles
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

	public void updateEnemies(Game game) {
		Game g = (Game)game;
		// find shortest path from enemies to player
		ArrayList<Vector> shortestPath = dijkstraPath(g.alien, g.player);
		g.alien.traversePath(shortestPath);
		
	}
	
}
