package space_escape;

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
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}

	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(true);
	}
	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		Game se = (Game)game;
		
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
			se.player.setVelocity(se.player.getVelocity().add(new Vector(0f, -.2f)));
		}
		if (input.isKeyDown(Input.KEY_S)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(0f, +.2f)));
		}
		if (input.isKeyDown(Input.KEY_A)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(-.2f, 0)));
		}
		if (input.isKeyDown(Input.KEY_D)) {
			se.player.setVelocity(se.player.getVelocity().add(new Vector(+.2f, 0f)));
		}
		//player direction / aim
		if (input.isKeyDown(Input.KEY_UP))
			se.player.setRotation(0);
		if (input.isKeyDown(Input.KEY_RIGHT))
			se.player.setRotation(90);
		if (input.isKeyDown(Input.KEY_DOWN))
			se.player.setRotation(180);
		if (input.isKeyDown(Input.KEY_LEFT))
			se.player.setRotation(270);
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_RIGHT))
			se.player.setRotation(45);
		if (input.isKeyDown(Input.KEY_RIGHT) && input.isKeyDown(Input.KEY_DOWN))
			se.player.setRotation(135);
		if (input.isKeyDown(Input.KEY_DOWN) && input.isKeyDown(Input.KEY_LEFT))
			se.player.setRotation(225);
		if (input.isKeyDown(Input.KEY_UP) && input.isKeyDown(Input.KEY_LEFT))
			se.player.setRotation(315);
		
		if (se.player.getCoarseGrainedMaxX() > se.ScreenWidth
				|| se.player.getCoarseGrainedMinX() < 0) {
			se.player.bounce(90);
		} else if (se.player.getCoarseGrainedMaxY() > se.ScreenHeight
				|| se.player.getCoarseGrainedMinY() < 0) {
			se.player.bounce(0);
		}
		
		

		se.player.update(delta);


		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			game.enterState(Game.GAMEOVERSTATE);
		}
	}

	@Override
	public int getID() {
		return Game.PLAYINGSTATE;
	}
	
}