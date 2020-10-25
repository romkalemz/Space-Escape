package space_escape;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import jig.ResourceManager;
import jig.Shape;

public class UiHandler {

	ArrayList<Shape> healthBar;		// an array of square shapes representing health
	ArrayList<Orb> currentOrbs;		// an array of current occupied orbs
	
	public UiHandler() {
		healthBar = new ArrayList<Shape>();
		currentOrbs = new ArrayList<Orb>();
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

	public void render(Game se, Graphics g) {
		g.drawLine(0, 640, 1200, 640);
		g.drawString("Movement Speed: " + se.player.initSpeed * se.player.multSpeed * 4, 25, 700);
		g.drawString("Attack Speed: " + se.player.atkSpeed, 25, 725);
		g.drawString("Attack Damage: " + se.player.atkDmg, 25, 750);
		for(int x = 0; x < se.player.hp; x++) {
			g.drawImage(ResourceManager.getImage(Game.HEALTH_RSC).getScaledCopy(60, 60), 1100 - (x * 60), 650);
		}
		for(int x = 0; x < 3; x++) {
			Image image = ResourceManager.getImage(Game.ITEMSQR_RSC);
			image.setFilter(Image.FILTER_NEAREST);
			g.drawImage(image, 425 + (x * 120), 675);
		}
		
		for (int i = 0; i < currentOrbs.size(); i++) {
			if(currentOrbs.get(i) != null) {
				currentOrbs.get(i).setPosition(465 +(i*120), 710);
				currentOrbs.get(i).render(g);
			}
		}
	}
	
}

