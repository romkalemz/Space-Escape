package space_escape;

import jig.Entity;
import jig.ResourceManager;
import jig.Shape;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

class PlayingState extends BasicGameState {
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		//load tiles based on level
		Game se = (Game)game;
		
		se.background = ResourceManager.getImage(Game.BG_STARS_RSC);
		// load initial routings for enemies
		se.map.updateEnemies(se);
		
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		Game se = (Game)game;
		
		// render background
		g.drawImage(se.background, 0, 0);
		// render UI
		se.UI.render(se, g);
		
		// render overlay
		if(se.overlayEnabled) {
			se.map.renderOverlay(g, se);
			for(int i = 0; i < se.enemies.size(); i++)
				se.enemies.get(i).renderPath(g);
		}
		
		se.map.render(g);
		se.player.render(g);
		// render each enemy
		for(int i = 0; i < se.enemies.size(); i++)
			se.enemies.get(i).render(g);
		// render each orb
		for( int i = 0; i < se.orbs.size(); i++)
			se.orbs.get(i).render(g);
		// render each bullet
		for(int i = 0; i < se.bullets.size(); i++)
			se.bullets.get(i).render(g);
		
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		Game se = (Game)game;
				
		// cheat codes and testing options
		userCodes(se, input);
		// player movement OR other controls input addressed
		playerMove(se, input);
		playerControls(se, input);
		// update the entities below (translations, animations, etc.)
		playerUpdate(se, delta);
		enemyUpdate(se, delta);
		orbUpdate(se, delta);
		bulletUpdate(se, delta);
		
		if(se.UI.score >= 30) 
			se.enterState(Game.STARTUPSTATE);
		
	}

	private void userCodes(Game g, Input input) {
		// enable / disable overlay mapping
		if(input.isKeyPressed(Input.KEY_O)) {
			if(!g.overlayEnabled)
				g.overlayEnabled = true;
			else 
				g.overlayEnabled = false;
		}
		// god mode enable / disable
		if(input.isKeyPressed(Input.KEY_G)) {
			if(!g.superEnabled) {
				g.superEnabled = true;
				g.player.setSuperStats(true);
			}
			else {
				g.superEnabled = false;
				g.player.setSuperStats(false);
			}
		}
		
		// next level
		if (input.isKeyDown(Input.KEY_ESCAPE))
			g.UI.score = 30;
	}
	
	private void playerMove(Game se, Input input) {
		se.player.setVelocity(new Vector(0, 0));
		// player movement
		if (input.isKeyDown(Input.KEY_W)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(0, -1)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(0, 1)));
		}
		if (input.isKeyDown(Input.KEY_A)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(-1, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(1, 0)));
		}
	
		// player direction / aim
		// wait for a slight cooldown to allow slower response times to angled facing position
		if (se.angled_pos_delay <= 0) {
			if (input.isKeyDown(Input.KEY_UP)) {
				se.player.setRotation(180);
				
				if(se.player_shoot_cooldown <= 0)
					addBullets(se, se.player, new Vector(0, -1));
			}
			
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				se.player.setRotation(270);
				
				if(se.player_shoot_cooldown <= 0)
					addBullets(se, se.player, new Vector(1, 0));
			}
			if (input.isKeyDown(Input.KEY_DOWN)) {
				se.player.setRotation(0);
				
				if(se.player_shoot_cooldown <= 0)
					addBullets(se, se.player, new Vector(0, 1));

			}
			if (input.isKeyDown(Input.KEY_LEFT)) {
				se.player.setRotation(90);
				
				if(se.player_shoot_cooldown <= 0)
					addBullets(se, se.player, new Vector(-1, 0));
			}
		}
		
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_RIGHT)) {
			se.player.setRotation(225);
			se.angled_pos_delay = 50;
			
			if(se.player_shoot_cooldown <= 0)
				addBullets(se, se.player, new Vector(1, -1));
		}
		if (input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_DOWN)) {
			se.player.setRotation(315);
			se.angled_pos_delay = 50;
			
			if(se.player_shoot_cooldown <= 0)
				addBullets(se, se.player, new Vector(1, 1));
		}
		if (input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_LEFT)) {
			se.player.setRotation(45);
			se.angled_pos_delay = 50;
			
			if(se.player_shoot_cooldown <= 0)
				addBullets(se, se.player, new Vector(-1, 1));
		}
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_LEFT)) {
			se.player.setRotation(135);
			se.angled_pos_delay = 50;
			
			if(se.player_shoot_cooldown <= 0)
				addBullets(se, se.player, new Vector(-1, -1));
		}
	}
	
	private void playerControls(Game g, Input input) {
		// drop the orb in the corresponding inputted key
		if(input.isKeyPressed(Input.KEY_1) && g.UI.currentOrbs.get(0) != null) {
			Orb droppedOrb = g.UI.dropOrb(0);
			addOrb(g, droppedOrb);
			g.player.removeStats(droppedOrb);
			g.orb_pickup_delay = 400;
		}
		if(input.isKeyPressed(Input.KEY_2) && g.UI.currentOrbs.get(1) != null) {
			Orb droppedOrb = g.UI.dropOrb(1);
			addOrb(g, droppedOrb);
			g.player.removeStats(droppedOrb);
			g.orb_pickup_delay = 400;
		}
		if(input.isKeyPressed(Input.KEY_3) && g.UI.currentOrbs.get(2) != null) {
			Orb droppedOrb = g.UI.dropOrb(2);
			addOrb(g, droppedOrb);
			g.player.removeStats(droppedOrb);
			g.orb_pickup_delay = 400;
		}
		
		
	}
	
	private void playerUpdate(Game g, int delta) {
		g.player.update(delta);	
		g.player.checkBounds(g.ScreenWidth, g.ScreenHeight);
		g.player.checkCollision(g.map);
		if(g.player.HP <= 0)
			g.enterState(Game.GAMEOVERSTATE);
		g.angled_pos_delay -= delta;
		g.player_shoot_cooldown -= delta;
	}
	
	private void enemyUpdate(Game g, int delta) {
		// spawn enemies
		if(g.spawn_cooldown <= 0) {
			g.spawn_cooldown = 8500;
			Enemy e;
			for(int i = 0; i < g.map.spawnTiles.size(); i++) {
				Vector spawnPos = g.map.getTilePosition(g.map.spawnTiles.get(i));
				if(spawnPos != g.map.getTilePosition(g.player)) {
					// place enemy at that tile (find a random type of enemy)
					double rand = Math.random();
					if(rand <= 0.33) {
						// spawn alien
						e = new Enemy(spawnPos.getX(), spawnPos.getY(), "alien");
					}
					else if(rand > 0.33 && rand <= 0.66) {
						// spawn ufo
						e = new Enemy(spawnPos.getX(), spawnPos.getY(), "ufo");
					}
					else if(rand > 0.66) {
						// spawn robot
						e = new Enemy(spawnPos.getX(), spawnPos.getY(), "robot");
					} else
						e = null;
					g.enemies.add(e);
				}
				
			}
		}
		// for each enemy, check collisions and update their location
		for(int i = 0; i < g.enemies.size(); i++) {
			g.enemies.get(i).checkCollision(g.map);
			if(g.enemies.get(i).collides(g.player) != null && g.touchdamage_cooldown <= 0 && !g.superEnabled) {
				g.player.HP -= 1;
				g.touchdamage_cooldown = 400;
			}
		}
		// update enemies paths
		g.map.updateEnemies(g);

		// check if the UFO's have a direct line of sight to shoot bullets
		for(int i = 0; i < g.enemies.size(); i++) {
			if(g.enemies.get(i).KO <= 0) {
				if(g.enemies.get(i).type == "ufo" && g.enemies.get(i).path.size() <= 5) {
					if(g.enemies.get(i).shoot_cooldown <= 0) {
						g.enemies.get(i).shoot_cooldown = 700;
						addBullets(g, g.enemies.get(i), null);
					}
				}
				g.enemies.get(i).traversePath();
			}
			g.enemies.get(i).update(delta);
		}
		
		for(int i = 0; i < g.enemies.size(); i++) {
			g.enemies.get(i).shoot_cooldown -= delta;
			g.enemies.get(i).KO -= delta;
		}
		g.touchdamage_cooldown -= delta;
		g.enemy_shoot_cooldown -= delta;
		g.spawn_cooldown -= delta;
	}
	
	private void orbUpdate(Game g, int delta) {
		// remove orbs that the player collided with
		for(int i = 0; i < g.orbs.size(); i++) {
			Orb orb = g.orbs.get(i);
			
			if (g.player.collides(orb) != null) {
				
				if(g.orb_pickup_delay <= 0) {
					if(g.player.orbCount < 3) {
						g.UI.addOrb(g.orbs.get(i));
						g.player.setStats(g.orbs.get(i));
						
					} else if(g.player.orbCount == 3) {
						// start collecting points for the score
						g.UI.score += 1;
					}
					g.orbs.remove(i);
				}
			}
		}
		// check if the player has 3 orbs already if so, go into final form
		if(g.UI.currentOrbs.size() == 3) {
			int reds = 0, blues = 0, greens = 0;
			
			for(int i = 0; i < g.UI.currentOrbs.size(); i++) {
				Orb orb = g.UI.currentOrbs.get(i);
				if(orb != null) {
					
					if(orb.type == "red")
						reds++;
					if(orb.type == "green")
						greens++;
					if(orb.type == "blue")
						blues++;
				}
			}
			if(reds == 1 && blues == 1 && greens == 1) {
				
				if(g.UI.form == null) {
					
					Entity rbg = new Entity(600, 755);
					g.UI.setForm(rbg);
				}
			}
			// if there is not at least 3 orbs, then remove form
			else if(reds + blues + greens != 3)
				g.UI.setForm(null);
		}
		
		
		g.orb_pickup_delay -= delta;
	}

	private void bulletUpdate(Game g, int delta) {
		// remove bullets that have collided with something
		for(int i = 0; i < g.bullets.size(); i++) {
			Bullet bullet = g.bullets.get(i);
			// check if collided with entity
			// with enemy
			for(int j = 0; j < g.enemies.size(); j++) {
				Enemy enemy = g.enemies.get(j);
				
				if(enemy.collides(bullet) != null && !bullet.isFromEnemy) {
					enemy.KO = 75;
					enemy.velocity = enemy.velocity.add(bullet.velocity.scale(1.5f));
					enemy.hp -= bullet.damage;
					
					if(enemy.hp <= 0) {
						Orb potentialOrb = enemy.dropOrb(g.map);
						
						if(potentialOrb != null)
							g.orbs.add(potentialOrb);

						g.UI.score += 1;
						g.enemies.remove(enemy);
					}
					g.bullets.remove(bullet);
				}
			}
			if(g.player.collides(bullet) != null && bullet.isFromEnemy) {
				if(!g.superEnabled)
					g.player.HP -= bullet.damage;
				if(g.player.HP <= 0)
					g.enterState(Game.GAMEOVERSTATE);
				g.bullets.remove(bullet);
			}
			// remove the bullet
			else if(bullet.isCollided(g.map, g.ScreenWidth, g.ScreenHeight))
				g.bullets.remove(bullet);
			
			bullet.update(delta);
		}
	}
	
	private void addOrb(Game g, Orb o) {
		Vector playerTile = g.map.getTile(g.player).getPosition();
		o.setPosition(playerTile);
		g.orbs.add(o);
	}
	
	private void addBullets(Game g, Entity e, Vector v) {
		Bullet b = new Bullet(e.getX(), e.getY());
		if (v == null) {
			b.isFromEnemy = true;
			// find the direction for the bullets to travel to
			// check if the entity is an enemy
			Vector playerPos = g.player.getPosition();
			if (playerPos != e.getPosition()) {
				double dir = playerPos.subtract(e.getPosition()).getRotation();
				v = new Vector(1, 1).setRotation(dir);
				b.setSpeed(0.2f);
			}
		} else {
			// bullet is from the player, adjust speed and damage
			b.setDamage(g.player.atkDmg);
			b.setSpeed(g.player.bulletSpeed);
			
			g.player_shoot_cooldown = g.player.rof;
		}
		b.setDirection(e, v);
		g.bullets.add(b);
	}
	
	@Override
	public int getID() {
		return Game.PLAYINGSTATE;
	}
	
}