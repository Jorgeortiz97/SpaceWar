package com.jorgeortizesc.space_war.domain;

import com.jorgeortizesc.space_war.gui.ImageLoader;
import com.jorgeortizesc.space_war.gui.Window;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Spacecraft implements IPositionable {

	public static enum YAxisIntensity {
		SLOW, NORMAL, FAST
	};

	private Course course;

	public static enum Course {
		STRAIGHT, RIGHT, LEFT
	}

	private final static int RUDDER_SENSISTIVITY = 3;

	private final static int[] SPEED = { 2, 4, 8 };

	private YAxisIntensity yIntensity = YAxisIntensity.NORMAL;
	private long accelerationTimer = 0;

	private int imageIndex = 0;

	private int snpX, snpY;

	private int x, y, w, h, cx, cy;
	private double ang = 90;

	private int shield = 100;
	private Enviroment t;

	private final static int RECHARGE_TIME = 300;
	private long rechargeTimer = 0;

	public Spacecraft(Enviroment t) {

		w = (int) ImageLoader.SPACECRAFT[0].getWidth();
		h = (int) ImageLoader.SPACECRAFT[0].getHeight();
		x = (Window.WIDTH - w) / 2;
		y = Window.HEIGHT - h - 20;

		cx = x + w / 2;
		cy = y + h / 2;

		snpX = (int) (cx - ImageLoader.SNIPER_SIGHT.getWidth() / 2);
		snpY = (int) (cy - ImageLoader.SNIPER_SIGHT.getHeight() / 2);
		this.t = t;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getW() {
		return w;
	}

	@Override
	public int getH() {
		return h;
	}

	public int getCX() {
		return cx;
	}

	public int getCY() {
		return cy;
	}

	public int getWX() {
		double radAng = Math.toRadians(ang);
		double cos = Math.cos(radAng);
		return (int) (cx - cos * h / 2);
	}

	public int getWY() {
		double radAng = Math.toRadians(ang);
		double sin = Math.sin(radAng);
		return (int) (cy - sin * h / 2);
	}

	public double getRadAng() {
		return Math.toRadians(ang);
	}

	public void moveYAxis(YAxisIntensity intensity) {
		yIntensity = intensity;
		accelerationTimer = System.currentTimeMillis();
	}

	public void moveSight(Course course) {
		this.course = course;
	}

	public boolean shoot() {
		if (System.currentTimeMillis() - rechargeTimer > RECHARGE_TIME) {
			rechargeTimer = System.currentTimeMillis();
			return true;
		}
		return false;
	}

	private void turnLeft() {
		ang -= RUDDER_SENSISTIVITY;
		if (ang < 30)
			ang = 30;
	}

	private void turnRight() {
		ang += RUDDER_SENSISTIVITY;
		if (ang > 150)
			ang = 150;
	}

	@Override
	public void draw(GraphicsContext gc) {

		if (course == Course.LEFT)
			turnLeft();
		else if (course == Course.RIGHT)
			turnRight();
		else {
			if (ang < 90)
				turnRight();
			else if (ang > 90)
				turnLeft();

		}

		course = Course.STRAIGHT;

		double radAng = Math.toRadians(ang);
		double cos = Math.cos(radAng);
		double sin = Math.sin(radAng);

		if (yIntensity != YAxisIntensity.NORMAL && System.currentTimeMillis() - accelerationTimer > 200)
			yIntensity = YAxisIntensity.NORMAL;

		imageIndex = yIntensity.ordinal();
		t.moveY(-SPEED[imageIndex]);
		t.moveX((int) (-SPEED[imageIndex] * cos));
		// double ang = 180 - Math.toDegrees(Math.atan2(snpY - cy, snpX - cx));

		gc.save();
		gc.translate(cx, cy);
		gc.rotate(ang + 270);
		gc.drawImage(ImageLoader.SPACECRAFT[imageIndex], -w / 2, -h / 2);
		gc.restore();

		gc.drawImage(ImageLoader.SNIPER_SIGHT, snpX - cos * 300, snpY - sin * 300);

		gc.setFill(Color.GREEN);

		gc.fillText("Shield: " + shield + "%", 20, Window.HEIGHT - 30);

	}

	public boolean collision(int amount) {
		shield -= amount;
		return shield <= 0;
	}

	public void setShield(int value) {
		shield = value;
	}

}
