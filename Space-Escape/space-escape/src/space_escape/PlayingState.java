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


/**
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	
	private int angled_pos_delay, orb_pickup_delay;
	private int player_shoot_cooldown, enemy_shoot_cooldown;
	private boolean overlayEnabled = false;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		//load tiles based on level
		Game se = (Game)game;
		
		//if(se.level == 1) {
			se.map.loadLevel(se, 1);
			se.background = ResourceManager.getImage(Game.BG_STARS_RSC);
		//}
		
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
		if(overlayEnabled) {
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
		
		playerMove(se, input);
		playerControls(se, input);

		// remove bullets that have collided with something
		for(int i = 0; i < se.bullets.size(); i++) {
				se.bullets.get(i).update(delta);
				if(se.bullets.get(i).isCollided(se.map, se.ScreenWidth, se.ScreenHeight))
					se.bullets.remove(i);
		}

		

		playerUpdate(se, delta);
		enemyUpdate(se, delta);
		orbUpdate(se, delta);
		
	}
	
	
	private void playerMove(Game se, Input input) {
		se.player.setVelocity(new Vector(0, 0));
		// player movement
		if (input.isKeyDown(Input.KEY_W)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(0f, -se.player.initSpeed * se.player.multSpeed)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(0f, +se.player.initSpeed * se.player.multSpeed)));
		}
		if (input.isKeyDown(Input.KEY_A)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(-se.player.initSpeed * se.player.multSpeed, 0f)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(+se.player.initSpeed * se.player.multSpeed, 0f)));
		}
	
		// player direction / aim
		// wait for a slight cooldown to allow slower response times to angled facing position
		if (angled_pos_delay <= 0) {
			if (input.isKeyDown(Input.KEY_UP)) {
				se.player.setRotation(180);
				if(player_shoot_cooldown <= 0) {
					addBullets(se, se.player, new Vector(0, -1));
			player_shoot_cooldown = 100;
				}
			}
			
			if (input.isKeyDown(Input.KEY_RIGHT)) {
				se.player.setRotation(270);
				if(player_shoot_cooldown <= 0) {
					addBullets(se, se.player, new Vector(1, 0));
					player_shoot_cooldown = 100;
				}
			}
			if (input.isKeyDown(Input.KEY_DOWN)) {
				se.player.setRotation(0);
				if(player_shoot_cooldown <= 0) {
					addBullets(se, se.player, new Vector(0, 1));
					player_shoot_cooldown = 100;
				}
			}
			if (input.isKeyDown(Input.KEY_LEFT)) {
				se.player.setRotation(90);
				if(player_shoot_cooldown <= 0) {
					addBullets(se, se.player, new Vector(-1, 0));
					player_shoot_cooldown = 100;
				}
			}
		}
		
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_RIGHT)) {
			se.player.setRotation(225);
			angled_pos_delay = 50;
			if(player_shoot_cooldown <= 0) {
				addBullets(se, se.player, new Vector(1, -1));
				player_shoot_cooldown = 100;
			}
			
		}
		if (input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_DOWN)) {
			se.player.setRotation(315);
			angled_pos_delay = 50;
			if(player_shoot_cooldown <= 0) {
				addBullets(se, se.player, new Vector(1, 1));
				player_shoot_cooldown = 100;
			}
		}
		if (input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_LEFT)) {
			se.player.setRotation(45);
			angled_pos_delay = 50;
			if(player_shoot_cooldown <= 0) {
				addBullets(se, se.player, new Vector(-1, 1));
				player_shoot_cooldown = 100;
			}
		}
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_LEFT)) {
			se.player.setRotation(135);
			angled_pos_delay = 50;
			if(player_shoot_cooldown <= 0) {
				addBullets(se, se.player, new Vector(-1, -1));
				player_shoot_cooldown = 100;
			}
		}
	}

	private void userCodes(Game g, Input input) {
		// enable / disable overlay mapping
		if(input.isKeyPressed(Input.KEY_O)) {
			if(!overlayEnabled)
				overlayEnabled = true;
			else 
				overlayEnabled = false;
		}
		// game over state
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			g.enterState(Game.GAMEOVERSTATE);
		}
	}
		
	private void playerControls(Game g, Input input) {
		// drop the orb in the corresponding inputted key
		if(input.isKeyPressed(Input.KEY_1) && g.UI.currentOrbs.get(0) != null) {
			Orb droppedOrb = g.UI.dropOrb(0);
			addOrb(g, droppedOrb);
			orb_pickup_delay = 400;
		}
		if(input.isKeyPressed(Input.KEY_2) && g.UI.currentOrbs.get(1) != null) {
			Orb droppedOrb = g.UI.dropOrb(1);
			addOrb(g, droppedOrb);
			orb_pickup_delay = 400;
		}
		if(input.isKeyPressed(Input.KEY_3) && g.UI.currentOrbs.get(2) != null) {
			Orb droppedOrb = g.UI.dropOrb(2);
			addOrb(g, droppedOrb);
			orb_pickup_delay = 400;
		}
		
		
	}
	
	private void playerUpdate(Game g, int delta) {
		g.player.update(delta);	
		g.player.checkBounds(g.ScreenWidth, g.ScreenHeight);
		g.player.checkCollision(g.map);
		angled_pos_delay -= delta;
		player_shoot_cooldown -= delta;
	}
	
	
	private void enemyUpdate(Game g, int delta) {
		// update enemies paths
		g.map.updateEnemies(g);
		// for each enemy, check collisions and update their location
		for(int i = 0; i < g.enemies.size(); i++) {
			g.enemies.get(i).checkCollision(g.map);
			g.enemies.get(i).update(delta);
		}
		// check if the UFO's have a direct line of sight to shoot bullets
		for(int i = 0; i < g.enemies.size(); i++) {
			if(g.enemies.get(i).type == "ufo" && g.enemies.get(i).path.size() <= 5) {
				if(enemy_shoot_cooldown <= 0) {
					addBullets(g, g.enemies.get(i), null);
					enemy_shoot_cooldown = 100;
				}
			}
		}
		enemy_shoot_cooldown -= delta;
	}
	
	private void orbUpdate(Game g, int delta) {
		// remove orbs that the player collided with
		for(int i = 0; i < g.orbs.size(); i++) {
			Orb orb = g.orbs.get(i);
			if (g.player.collides(orb) != null) {
				if(orb_pickup_delay <= 0) {
					g.UI.addOrb(g.orbs.get(i));
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
		
		
		orb_pickup_delay -= delta;
	}

	
	private void addOrb(Game g, Orb o) {
		Vector playerTile = g.map.getTile(g.player).getPosition();
		o.setPosition(playerTile);
		g.orbs.add(o);
	}
	
	private void addBullets(Game g, Entity e, Vector v) {
		Projectile b = new Projectile(e.getX(), e.getY());
		if (v == null) {
			// find the direction for the bullets to travel to
			// check if the entity is an enemy
			Vector playerPos = g.player.getPosition();
			if (playerPos != e.getPosition()) {
				double dir = playerPos.subtract(e.getPosition()).getRotation();
				v = new Vector(1, 1).setRotation(dir);
				b.setSpeed(0.2f);
			}
		}
		b.setDirection(e, v);
		g.bullets.add(b);
	}
	
	@Override
	public int getID() {
		return Game.PLAYINGSTATE;
	}
	
}