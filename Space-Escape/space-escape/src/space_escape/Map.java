package space_escape;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import jig.Entity;
import jig.Vector;

public class Map {

	public static int tileSize = 40;
	public int number_of_tilesX = 30;
	public int number_of_tilesY = 16;
	private Tile playerTile, enemyTile;
	private Tile[][] tiles;
	private Tile[][] overlay;
	public ArrayList<Tile> spawnTiles;

	
	public Map() {

		tiles = new Tile[number_of_tilesX][number_of_tilesY];
		
		// overlay stuff
		overlay = new Tile[number_of_tilesX][number_of_tilesY];
		// the tile highlighted in GREEN for overlay, multiple colors added for less transparency
		playerTile = new Tile(0, 0, 1, 1, false, Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		playerTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(0, 255, 0));
		
		enemyTile = new Tile(0, 0, 1, 1, false, Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		enemyTile.changeImage(Game.TILE_OVERLAY_RSC, new Color(255, 0, 0));
		// empty map
		// place empty tiles through the whole map first
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				Tile empty = new Tile(x, y, 1, 1);
				tiles[x][y] = empty;
			}
		}
		// overlay setup
		for(int x = 0; x < number_of_tilesX; x++)
			for(int y = 0; y < number_of_tilesY; y++)
				overlay[x][y] = new Tile(x, y, 1, 1, false, Game.TILE_OVERLAY_RSC);
	}
	
	public ArrayList<Tile> loadLevel(Game g, int lvl) {
		spawnTiles = new ArrayList<Tile>();
		if (lvl == 1) {
			
			int[][] mapLayout = 
				{{2,0,0,0,0,0,1,0,0,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0,0,0,0,0,0,2},
				 {0,1,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0,0,0,1,1,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0,0,0},
				 {0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0},
				 {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,9,0,1,0,0,0,0,0,1,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
				 {0,0,0,0,1,0,0,0,0,0,1,1,1,0,0,0,1,1,1,0,0,1,0,0,0,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
				 {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0},
				 {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,0,0,0,0},
				 {0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0},
				 {0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0},
				 {0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0},
				 {2,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,2}};
			
			
			for(int x = 0; x < number_of_tilesX; x++) {
				for(int y = 0; y < number_of_tilesY; y++) {
					if(mapLayout[y][x] == 1) {	// wall tile
						tiles[x][y] = new Tile(x, y, 1, 1, true, Game.TILE_ASTROID1_RSC);
					}
					if(mapLayout[y][x] == 2) {	//enemy tile
						spawnTiles.add(tiles[x][y]);	
					}
//					if(mapLayout[y][x] == 3) {
//						Enemy e = new Enemy(x, y, "ufo");
//						g.enemies.add(e);
//					}
//					if(mapLayout[y][x] == 4) {
//						Enemy e = new Enemy(x, y, "robot");
//						g.enemies.add(e);
//					}
//					if(mapLayout[y][x] == 5) {
//						Orb a = new Orb(x, y, "red");
//						g.orbs.add(a);
//					}
//					if(mapLayout[y][x] == 6) {
//						Orb a = new Orb(x, y, "blue");
//						g.orbs.add(a);
//					}
//					if(mapLayout[y][x] == 7) {
//						Orb a = new Orb(x, y, "green");
//						g.orbs.add(a);
//					}
					if(mapLayout[y][x] == 9) {
						g.player.setPosition(x *40+20, y*40+20);
					}
				}
			}
		}
		return spawnTiles;
	}
	
	// find shortest path between two entities
	// returns an array of points needed to follow
	public ArrayList<Vector> dijkstraPath(Entity enemy, Entity player) { 
		// STEP 1, populate graph with costs of tiles from player to enemies
		// place initial tile and neighbors into queue and adjust costs
		Tile startTile = getTile(player);				// grab first tile
		startTile.cost = 0;								// set cost of travel to 0
		// unexplored tiles are placed in the queue
		PriorityQueue<Tile> Q = new PriorityQueue<Tile>();
		Q.add(startTile);								// add to the queue/stack
		// if tile is unexplored, visit it and adjust costs
		while(!Q.isEmpty()) {
			Tile curTile = Q.poll();						// pop the head of the stack
			curTile.visited = true;
			curTile.neighbors = findNeighbors(curTile);		// finds all valid tiles around it
			// adjust and compare values of neighboring tiles, add to Q
			for (int i = 0; i < curTile.neighbors.size(); i++) {
				Tile neighbor = curTile.neighbors.get(i);
				// check if you can walk on the neighbor tile AND adjust costs
				// first check if this is a neighboring corner tile
				double travelDistance = travelCost(curTile, neighbor);
				if(!neighbor.isSolid() && neighbor.cost > curTile.cost + travelDistance) {
					neighbor.cost = (float) (curTile.cost + travelDistance);	// reduce cost of neighbor
																	// since its closer than before
					neighbor.prev = curTile;			// keep track of the prev
														// so you can backtrack
				}

				if(!neighbor.visited)
					Q.add(neighbor);
			}
		}
//		// STEP 2, trace back from player to enemy with previous tiles
//		Tile endTile = getTile(enemy);
//		Tile previousTile = endTile;
//		ArrayList<Tile> backPath = new ArrayList<Tile>();
//		// follow the previous tiles until you reach the enemy (null)
//		while(previousTile != null) {
//			backPath.add(previousTile);
//			System.out.println(backPath.size());
//			previousTile = previousTile.prev;
//		}
//		// STEP 3, return a reversed array of points from step 2
//		ArrayList<Vector> shortestPath = new ArrayList<Vector>();
//		// add points from the backPath to the shortest path in reverse
//		// for the enemy to follow the shortest path towards the player
//		for (int i = backPath.size()-1; i >= 0; i--)
//			shortestPath.add(backPath.get(i).getPosition());
//		
//		return shortestPath;
		return null;
	}
	
	private ArrayList<Tile> findNeighbors(Tile t) {
		Vector pos = getTilePosition(t);
		int x = (int)pos.getX();
		int y = (int)pos.getY();
		
		ArrayList<Tile> n = new ArrayList<Tile>();
		// SW
		if(x > 0 && y < number_of_tilesY-1)	{
			tiles[x-1][y+1].corner = true;
			n.add(tiles[x-1][y+1]);	
		}
		// W
		if(x > 0)							
			n.add(tiles[x-1][y]);
		// NW
		if(x > 0 && y > 0) {
			tiles[x-1][y-1].corner = true;
			n.add(tiles[x-1][y-1]);
		}
		// N
		if(y > 0)							
			n.add(tiles[x][y-1]);
		// NE
		if(x < number_of_tilesX-1 && y > 0)	{
			tiles[x+1][y-1].corner = true;
			n.add(tiles[x+1][y-1]);
		}	
		// E
		if(x < number_of_tilesX-1)	
			n.add(tiles[x+1][y]);	
		// SE
		if(x < number_of_tilesX-1 && y < number_of_tilesY-1) {
			tiles[x+1][y+1].corner = true;
			n.add(tiles[x+1][y+1]);
		}
		// S
		if(y < number_of_tilesY-1)			
			n.add(tiles[x][y+1]);
				
		return n;
	}
	
	private double travelCost(Tile t1, Tile t2) {
		
		double lx = Math.abs(t1.getX() - t2.getX());
		double ly = Math.abs(t1.getY() - t2.getY());
		double distance;
		if (lx == 0)			// vertical distance travel
			distance = ly;
		else if (ly == 0)		//horizontal distance travel
			distance = lx;
		else					//diagonal distance travel
			distance = Math.hypot(lx, ly);
		return distance / 40;	//converting to tile distance
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
		// for cost printing
		// setting decimal places (limit 2)
		// DecimalFormat df = new DecimalFormat("0.0");
		// --------------------------------------------
//		Vector tilePlace = getPosition(game.player);
//		playerTile.setPosition(tilePlace);
//		playerTile.render(g);
//		
//		for(int i = 0; i < game.enemies.size(); i++) {
//			tilePlace = getPosition(game.enemies.get(i));
//			enemyTile.setPosition(tilePlace);
//			enemyTile.render(g);
//		}
		
		
		// render the rest of the transparent overlay tiles
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				overlay[x][y].render(g);
				if(tiles[x][y].isSolid()) {
					Tile solidTile = new Tile(x, y, 1, 1, false, Game.TILE_OVERLAY_RSC, new Color(0, 0, 255));
					solidTile.render(g);
				}
				// draw lines of costs of tiles around player
				if(tiles[x][y].prev != null)
					g.drawGradientLine(tiles[x][y].prev.getX(), tiles[x][y].prev.getY(), 255, 255, 255, .2f,
							tiles[x][y].getX(), tiles[x][y].getY(), 255, 255, 255, 0.2f);
				// print the cost of tiles around enemies
				//if(tiles[x][y].cost < 3)
				//	g.drawString(""+df.format(tiles[x][y].cost), tiles[x][y].getPosition().getX() -15, tiles[x][y].getPosition().getY() - 10);
			}
		}
	}
	
	public void render(Graphics g) {
		for(int x = 0; x < number_of_tilesX; x++) {
			for(int y = 0; y < number_of_tilesY; y++) {
				if(tiles[x][y] != null) {
					tiles[x][y].render(g);
				}
			}
		}
	}

	public void updateEnemies(Game game) {
		Game g = (Game)game;
		// reset dijkstra elements for each tile
		for (int x = 0; x < number_of_tilesX; x++)
			for (int y = 0; y < number_of_tilesY; y++)
				tiles[x][y].resetDijkstraElements();
		
		// find shortest path from enemies to player
		for(int i = 0; i < g.enemies.size(); i++) {
			ArrayList<Vector> shortestPath = dijkstraPath(g.enemies.get(i), g.player);
			//g.enemies.get(i).setPath(shortestPath); 
			 g.enemies.get(i).setPath(getTile(g.enemies.get(i)));
		}
			
		
	}
	
}
