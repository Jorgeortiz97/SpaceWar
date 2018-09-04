package com.jorgeortizesc.space_war.domain;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Weapon implements IPositionable {

	private int x, y, w, h;
	private double radAng;
	private Enviroment t;

	private final static int SPEED = 10;
	private long expirationTimer;
	private final static int EXPIRATION_TIME = 2000;
	private boolean collisioned = false;

	public Weapon(int x, int y, double radAng, Enviroment t) {
		this.x = x;
		this.y = y;
		this.radAng = radAng;
		w = 10;
		h = 10;
		this.t = t;
		expirationTimer = System.currentTimeMillis();
	}

	@Override
	public int getX() {
		return t.getX() + x;
	}

	@Override
	public int getY() {
		return t.getY() + y;
	}

	@Override
	public int getW() {
		return w;
	}

	@Override
	public int getH() {
		return h;
	}

	public void move() {
		x -= Math.cos(radAng) * SPEED;
		y -= Math.sin(radAng) * SPEED;
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setFill(Color.RED);
		gc.fillOval(getX(), getY(), w, h);

	}

	public boolean isCollisioned() {
		return collisioned;
	}

	public void collision() {
		collisioned = true;
	}

	public boolean isOut() {
		return (collisioned || System.currentTimeMillis() - expirationTimer > EXPIRATION_TIME);
	}

}
