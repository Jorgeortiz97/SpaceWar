package com.jorgeortizesc.space_war.domain;

import javafx.scene.canvas.GraphicsContext;

/***
 * This class represents a rectangle which can be drawn.
 */
public interface IPositionable {

	public int getX();

	public int getY();

	public int getW();

	public int getH();

	void draw(GraphicsContext gc);

}
