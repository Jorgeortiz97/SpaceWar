package com.jorgeortizesc.space_war.util;

import java.awt.Point;

import com.jorgeortizesc.space_war.domain.IPositionable;
import com.jorgeortizesc.space_war.gui.Window;

import javafx.scene.canvas.GraphicsContext;

/***
 * Class for controlling the interactions between objects
 */
public class Physics {

	private static IPositionable screen = new IPositionable() {

		@Override
		public int getY() {
			return 0;
		}

		@Override
		public int getX() {
			return 0;
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
		}
	};

	/***
	 * This method determines if there is a collision between two objects
	 *
	 * @param obj1
	 *            IPositionable obj (rectangle)
	 * @param obj2
	 *            IPositionable obj (rectangle)
	 * @return True if there is a collision; false, otherwise
	 */
	public static boolean col(IPositionable obj1, IPositionable obj2) {

		boolean a = obj1.getX() + obj1.getW() >= obj2.getX();
		boolean b = obj1.getX() <= obj2.getX() + obj2.getW();
		boolean c = obj1.getY() + obj1.getH() >= obj2.getY();
		boolean d = obj1.getY() <= obj2.getY() + obj2.getH();

		return a && b && c && d;

	}

	/***
	 * Returns the intersection between two rectangles.
	 *
	 * @param obj1
	 *            IPositionable obj (rectangle)
	 * @param obj2
	 *            IPositionable obj (rectangle)
	 * @return Point with the intersection between two rectangles
	 */
	public static Point getIntersection(IPositionable obj1, IPositionable obj2) {

		int x1 = Math.max(obj1.getX(), obj2.getX());
		int y1 = Math.max(obj1.getY(), obj2.getY());
		int x2 = Math.min(obj1.getX() + obj1.getW(), obj2.getX() + obj2.getW());
		int y2 = Math.min(obj1.getY() + obj1.getH(), obj2.getY() + obj2.getH());

		return new Point((x1 + x2) / 2, (y1 + y2) / 2);
	}

	/***
	 * This method determines if an object is inside the screen.
	 *
	 * @param obj1
	 *            IPositionable obj (rectangle)
	 * @return True if object is inside the screen; false, otherwise
	 */
	public static boolean isInScreen(IPositionable obj1) {
		return col(screen, obj1);
	}

}
