package com.jorgeortizesc.space_war.domain;

import java.util.Random;

import com.jorgeortizesc.space_war.gui.ImageLoader;
import com.jorgeortizesc.space_war.gui.Window;
import com.jorgeortizesc.space_war.util.Physics;

import javafx.scene.canvas.GraphicsContext;

public class Asteroid implements IPositionable {

	private Random rnd = new Random();
	private int x, y, w, h, img = rnd.nextInt(4);

	private final static int[] LIFES = { 2, 2, 5, 5 };
	private int life;

	private Enviroment t;

	private boolean collisioned = false;

	public Asteroid(Enviroment t) {
		this.t = t;
		this.w = (int) ImageLoader.ASTEROIDS[img].getWidth();
		this.h = (int) ImageLoader.ASTEROIDS[img].getHeight();
		life = LIFES[img];
	}

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getX() {
		return x + t.getX();
	}

	@Override
	public int getY() {
		return y + t.getY();
	}

	@Override
	public int getW() {
		return w;
	}

	@Override
	public int getH() {
		return h;
	}

	public boolean isOut() {
		return life <= 0 || getY() > Window.HEIGHT;
	}

	public boolean isCollisioned() {
		return collisioned;
	}

	public int collision() {
		collisioned = true;
		return life;
	}

	@Override
	public void draw(GraphicsContext gc) {
		if (Physics.isInScreen(this))
			gc.drawImage(ImageLoader.ASTEROIDS[img], getX(), getY());

	}

	public boolean collisionWithWeapon() {
		life--;
		return (life <= 0);

	}

}
