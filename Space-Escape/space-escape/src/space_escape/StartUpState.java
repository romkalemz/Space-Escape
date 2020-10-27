package space_escape;

import java.util.Iterator;

import jig.ResourceManager;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * This state is active prior to the Game starting. In this state, sound is
 * turned off, and the bounce counter shows '?'. The user can only interact with
 * the game by pressing the SPACE key which transitions to the Playing State.
 * Otherwise, all game objects are rendered and updated normally.
 * 
 * Transitions From (Initialization), GameOverState
 * 
 * Transitions To PlayingState
 */
class StartUpState extends BasicGameState {
	
	public int delay;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) {
		container.setSoundOn(false);
		Game se = (Game)game;
		
		se.clear();
		se.map.clear();
		se.UI.clear();
		se.enemies.clear();
		se.orbs.clear();
		se.bullets.clear();
		
		se.level += 1;
		if(se.level > 3)
			se.level = 0;
		System.out.println(se.level);
		if(se.level > 0 && se.level < 3)
			se.map.loadLevel(se, se.level);
	}


	@Override
	public void render(GameContainer container, StateBasedGame game,
			Graphics g) throws SlickException {
		Game se = (Game)game;
		if(se.level == 0)
			g.drawImage(ResourceManager.getImage(Game.STARTUP_BANNER_RSC), 0, 0);
		else if(se.level == 1)
			g.drawImage(ResourceManager.getImage(Game.LEVEL1_BANNER_RSC), 0, 0);
		else if(se.level == 2)
			g.drawImage(ResourceManager.getImage(Game.LEVEL2_BANNER_RSC), 0, 0);
		else if(se.level ==3)
			g.drawImage(ResourceManager.getImage(Game.WIN_BANNER_RSC), 0, 0);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game,
			int delta) throws SlickException {

		Input input = container.getInput();
		Game se = (Game)game;

		if (input.isKeyDown(Input.KEY_SPACE) && se.level > 0 && se.level < 3 && delay <= 0) {
			delay = 500;
			se.enterState(Game.PLAYINGSTATE);
		}
		else if(input.isKeyDown(Input.KEY_SPACE) && (se.level == 0 || se.level == 3) && delay <= 0) {
			delay = 500;
			se.enterState(Game.STARTUPSTATE);
		}
		
		delay -= delta;
	}

	@Override
	public int getID() {
		return Game.STARTUPSTATE;
	}
	
}