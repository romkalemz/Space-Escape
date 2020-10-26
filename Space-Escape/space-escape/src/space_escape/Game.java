package space_escape;

import java.util.ArrayList;

import jig.Entity;
import jig.ResourceManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;


public class Game extends StateBasedGame {
	
	public static final int STARTUPSTATE = 0;
	public static final int PLAYINGSTATE = 1;
	public static final int GAMEOVERSTATE = 2;
	
	public static final String PLAYER_ORIGIN_RSC = "space_escape/resource/player-origin.png";
	public static final String GAMEOVER_BANNER_RSC = "space_escape/resource/gameover.png";
	public static final String STARTUP_BANNER_RSC = "space_escape/resource/PressSpace.png";
	public static final String HEALTH_RSC = "space_escape/resource/heart.png";
	public static final String ITEMSQR_RSC = "space_escape/resource/itemSqr.png";
	public static final String TILE_OVERLAY_RSC = "space_escape/resource/overlayTile.png";
	public static final String BG_STARS_RSC = "space_escape/resource/starsBG.png";
	public static final String TILE_ASTROID1_RSC = "space_escape/resource/asteroid1.png";
	public static final String ENEMY_ALIEN_RSC = "space_escape/resource/alien.png";
	public static final String ENEMY_UFO_RSC = "space_escape/resource/ufo.png";
	public static final String ENEMY_ROBOT_RSC = "space_escape/resource/robot.png";
	public static final String BULLET_REGULAR_RSC = "space_escape/resource/bullet.png";
	public static final String ORB_BLUE_RSC = "space_escape/resource/blue_orb_anim.png";
	public static final String ORB_RED_RSC = "space_escape/resource/red_orb_anim.png";
	public static final String ORB_GREEN_RSC = "space_escape/resource/green_orb_anim.png";
	public static final String BOUND_RSC = "space_escape/resource/bound.png";
	public static final String RBG_FORM_RSC = "space_escape/resource/rbg_form_anim.png";
	public static final String BG_UI_RSC = "space_escape/resource/UIbg.png";
	
	public final int ScreenWidth;
	public final int ScreenHeight;

	
	Map map;
	UiHandler UI;
	Player player;
	ArrayList<Enemy> enemies;
	ArrayList<Orb> orbs;
	ArrayList<Bullet> bullets;
	public int level;
	public Image background;


	public Game(String title, int width, int height) {
		super(title);
		ScreenHeight = height;
		ScreenWidth = width;
		level = 1;

		Entity.setCoarseGrainedCollisionBoundary(Entity.AABB);
				
	}


	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new StartUpState());
		addState(new GameOverState());
		addState(new PlayingState());
		
		ResourceManager.loadImage(PLAYER_ORIGIN_RSC);
		ResourceManager.loadImage(GAMEOVER_BANNER_RSC);
		ResourceManager.loadImage(STARTUP_BANNER_RSC);
		ResourceManager.loadImage(HEALTH_RSC);
		ResourceManager.loadImage(ITEMSQR_RSC);
		ResourceManager.loadImage(TILE_OVERLAY_RSC);
		ResourceManager.loadImage(BG_STARS_RSC);
		ResourceManager.loadImage(TILE_ASTROID1_RSC);
		ResourceManager.loadImage(ENEMY_ALIEN_RSC);
		ResourceManager.loadImage(ENEMY_UFO_RSC);
		ResourceManager.loadImage(ENEMY_ROBOT_RSC);
		ResourceManager.loadImage(BULLET_REGULAR_RSC);
		ResourceManager.loadImage(ORB_RED_RSC);
		ResourceManager.loadImage(ORB_BLUE_RSC);
		ResourceManager.loadImage(ORB_GREEN_RSC);
		ResourceManager.loadImage(BOUND_RSC);
		ResourceManager.loadImage(RBG_FORM_RSC);
		ResourceManager.loadImage(BG_UI_RSC);
		
		map = new Map();
		UI = new UiHandler();
		player = new Player(0, 0, .25f);
		enemies = new ArrayList<Enemy>();
		orbs = new ArrayList<Orb>();
		bullets = new ArrayList<Bullet>();
		
		
	}
	
	public static void main(String[] args) {
		AppGameContainer app;
		try {
			app = new AppGameContainer(new Game("Space Escape", 1200, 640));
			app.setDisplayMode(1200, 800, false);
			app.setVSync(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

	
}
