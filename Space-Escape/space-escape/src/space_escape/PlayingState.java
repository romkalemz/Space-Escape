package space_escape;

import jig.ResourceManager;
import jig.Vector;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


/**
 * This state is active when the Game is being played. In this state, sound is
 * turned on, the bounce counter begins at 0 and increases until 10 at which
 * point a transition to the Game Over state is initiated. The user can also
 * control the ball using the WAS & D keys.
 * 
 * Transitions From StartUpState
 * 
 * Transitions To GameOverState
 */
class PlayingState extends BasicGameState {
	
	private int angled_pos_cooldown;
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		//load tiles based on level
		Game se = (Game)game;
		//if(se.level == 1) {
			se.map.loadLevel(1);
			se.background = ResourceManager.getImage(Game.BG_STARS_RSC);
		//}
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		Game se = (Game)game;
		
		//background render
		g.drawImage(se.background, 0, 0);
		// UI rendering
		g.drawLine(0, 640, 1200, 640);
		g.drawString("Movement Speed: " + se.player.initSpeed * se.player.multSpeed * 4, 25, 700);
		g.drawString("Attack Speed: " + se.player.atkSpeed, 25, 725);
		g.drawString("Attack Damage: " + se.player.atkDmg, 25, 750);
		for(int x = 0; x < se.player.hp; x++) {
			g.drawImage(ResourceManager.getImage(Game.HEALTH_RSC).getScaledCopy(60, 60), 1100 - (x * 60), 650);
		}
		for(int x = 0; x < 3; x++) {
			g.drawImage(ResourceManager.getImage(Game.ITEMSQR_RSC), 425 + (x * 120), 675);
		}
		
		// render everything else
		se.map.render(container, game, g);
		se.player.render(g);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		Game se = (Game)game;
		
		se.player.setVelocity(new Vector(0, 0));
		
		//player movement
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
		angled_pos_cooldown -= delta;
		//player direction / aim
		//wait for a slight cooldown to allow slower response times to angled facing position
		if (angled_pos_cooldown <= 0) {
			if (input.isKeyDown(Input.KEY_UP))
				se.player.setRotation(0);
			if (input.isKeyDown(Input.KEY_RIGHT))
				se.player.setRotation(90);
			if (input.isKeyDown(Input.KEY_DOWN))
				se.player.setRotation(180);
			if (input.isKeyDown(Input.KEY_LEFT))
				se.player.setRotation(270);
		}
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_RIGHT)) {
			se.player.setRotation(45);
			angled_pos_cooldown = 50;
		}
		if (input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_DOWN)) {
			se.player.setRotation(135);
			angled_pos_cooldown = 50;
		}
		if (input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_LEFT)) {
			se.player.setRotation(225);
			angled_pos_cooldown = 50;
		}
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_LEFT)) {
			se.player.setRotation(315);
			angled_pos_cooldown = 50;
		}
			
		
		
		se.player.update(delta);
		//player bounds
		se.player.checkBounds(se.ScreenWidth, se.ScreenHeight);
		

		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			game.enterState(Game.GAMEOVERSTATE);
		}
	}

	@Override
	public int getID() {
		return Game.PLAYINGSTATE;
	}
	
}