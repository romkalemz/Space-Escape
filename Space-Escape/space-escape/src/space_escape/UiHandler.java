package space_escape;

import java.util.ArrayList;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.Entity;
import jig.ResourceManager;
import jig.Shape;

public class UiHandler {

	//ArrayList<Shape> healthBar;		// an array of square shapes representing health
	ArrayList<Orb> currentOrbs;		// an array of current occupied orbs
	Entity form;					// the final transfomation after 3 collected orbs
	Animation rbg_anim;
	int score;
	
	public UiHandler() {
		//healthBar = new ArrayList<Shape>();
		currentOrbs = new ArrayList<Orb>();
		form = null;
		score = 0;
	}
	
	public void addOrb(Orb o) {
		
		// if there is an "empty" spot, place there first
		for (int i = 0; i < currentOrbs.size(); i++) {
			if(currentOrbs.get(i) == null) {
				currentOrbs.set(i, o);
				return;
			}
		}
		// check if maximum orb count is less than 3
		if(currentOrbs.size() < 3) 
			currentOrbs.add(o);
	}
	
	public Orb dropOrb(int index) {
		Orb o = currentOrbs.get(index);
		if(o != null)
			currentOrbs.set(index, null);
		return o;
	}
	
	public void setForm(Entity f) {

		if(f == null) {
			form = null;
			return;
		}
		form = f;
		rbg_anim = new Animation(ResourceManager.getSpriteSheet(
				Game.RBG_FORM_RSC, 250, 50), 0, 0, 0, 8, true, 50, true);
		rbg_anim.setLooping(true);
		form.addAnimation(rbg_anim);
	}

	public void clear() {
		score = 0;
		currentOrbs.clear();
	}

	public void render(Game se, Graphics g) {
		//background
		g.drawImage(ResourceManager.getImage(Game.BG_UI_RSC), 0, 640);
		
		// stats
		//g.drawLine(0, 640, 1200, 640);
		g.drawString("Movement Speed: " + (int)(se.player.moveSpeed *100), 25, 710);
		g.drawString("Attack Speed: " + (int)(se.player.rof / 100), 25, 735);
		g.drawString("Attack Damage: " + (int)(se.player.atkDmg), 25, 760);
		g.drawString("Score: "+score, 550, 10);
		
		// health 
		for(int x = 0; x < se.player.HP; x++) {
			Image image = ResourceManager.getImage(Game.HEALTH_RSC).getScaledCopy(60, 60);
			if(se.player.super_enabled)
				image.setImageColor(0.9f, 1.0f, 0.0f);
			g.drawImage(image, 1100 - (x * 60), 720);
		}
		// item boxes
		for(int x = 0; x < 3; x++) {
			Image image = ResourceManager.getImage(Game.ITEMSQR_RSC).getScaledCopy(50, 50);
			image.setFilter(Image.FILTER_NEAREST);
			g.drawImage(image, 475 + (x * 100), 665);
		}
		
		// empty final form display
		Image image = ResourceManager.getImage(Game.ITEMSQR_RSC).getScaledCopy(250, 50);
		g.drawImage(image, 475, 730);
		if(form != null)
			form.render(g);
		
		// orbs in the UI
		for (int i = 0; i < currentOrbs.size(); i++) {
			if(currentOrbs.get(i) != null) {
				currentOrbs.get(i).setPosition(500 +(i*100), 690);
				currentOrbs.get(i).render(g);
			}
		}
		
		
	}
	
}

