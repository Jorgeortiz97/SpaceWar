package com.jorgeortizesc.space_war.domain;

import com.jorgeortizesc.space_war.gui.ImageLoader;
import com.jorgeortizesc.space_war.gui.Window;

import javafx.scene.canvas.GraphicsContext;

public class Enviroment implements IPositionable {

	private int x = 0, y = 0;

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
		return Window.WIDTH;
	}

	@Override
	public int getH() {
		return Window.HEIGHT;
	}

	@Override
	public void draw(GraphicsContext gc) {

		int restX = x % 800;
		if (restX < 0)
			restX += 800;

		int restY = y % 600;
		if (restY < 0)
			restY += 600;

		gc.drawImage(ImageLoader.BACKGROUND, restX, restY);
		gc.drawImage(ImageLoader.BACKGROUND, restX - 800, restY);
		gc.drawImage(ImageLoader.BACKGROUND, restX, restY - 600);
		gc.drawImage(ImageLoader.BACKGROUND, restX - 800, restY - 600);

	}

	public void moveX(int amount) {
		x -= amount;
	}

	public void moveY(int amount) {
		y -= amount;
	}

	public void setY(int value) {
		y = value;
	}

}
