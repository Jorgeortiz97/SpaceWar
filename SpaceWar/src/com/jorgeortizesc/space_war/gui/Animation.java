package com.jorgeortizesc.space_war.gui;

import java.awt.Point;

import com.jorgeortizesc.space_war.domain.Enviroment;
import com.jorgeortizesc.space_war.domain.IPositionable;

import javafx.scene.canvas.GraphicsContext;

/***
 * Class for creating and drawing animations: asteroids destruction or collision
 * between asteroids and the spacecraft.
 */
public class Animation implements IPositionable {

	public enum TypeOfAnimation {
		EXPLOSSION, DESTRUCTION
	};

	private int x, y, w, h, imageIndex, type;
	private long timer;
	private final static int ANIMATION_TIME = 200;
	private Enviroment t;

	public Animation(Point collisionCenter, TypeOfAnimation typeOfAnimation, Enviroment t) {
		type = typeOfAnimation.ordinal();
		w = (int) ImageLoader.ANIMATION[type][0].getWidth();
		h = (int) ImageLoader.ANIMATION[type][0].getHeight();
		this.x = collisionCenter.x - w / 2;
		this.y = collisionCenter.y - h / 2;
		this.t = t;
		imageIndex = 0;
		timer = System.currentTimeMillis();
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

	@Override
	public void draw(GraphicsContext gc) {
		if (System.currentTimeMillis() - timer > ANIMATION_TIME) {
			timer = System.currentTimeMillis();
			imageIndex++;
		}
		if (imageIndex < ImageLoader.ANIMATION[type].length)
			gc.drawImage(ImageLoader.ANIMATION[type][imageIndex], getX(), getY());

	}

	public boolean isOut() {
		return imageIndex >= ImageLoader.ANIMATION[type].length;

	}

}
