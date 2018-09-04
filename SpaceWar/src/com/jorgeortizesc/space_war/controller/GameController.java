package com.jorgeortizesc.space_war.controller;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;

import com.jorgeortizesc.acc.AccController;
import com.jorgeortizesc.acc.AccEvent;
import com.jorgeortizesc.acc.AccEvent.TypeOfEvent;
import com.jorgeortizesc.acc.IAccListener;
import com.jorgeortizesc.space_war.domain.Asteroid;
import com.jorgeortizesc.space_war.domain.Enviroment;
import com.jorgeortizesc.space_war.domain.Spacecraft;
import com.jorgeortizesc.space_war.domain.Spacecraft.Course;
import com.jorgeortizesc.space_war.domain.Spacecraft.YAxisIntensity;
import com.jorgeortizesc.space_war.domain.Weapon;
import com.jorgeortizesc.space_war.gui.Animation;
import com.jorgeortizesc.space_war.gui.Animation.TypeOfAnimation;
import com.jorgeortizesc.space_war.gui.Window;
import com.jorgeortizesc.space_war.util.Physics;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/***
 * GameController provides methods for controlling the game: physics, drawing
 * and receiving accelerometer events.
 */
public class GameController implements IAccListener {

	private Enviroment t = new Enviroment();

	// Game objects:
	private Spacecraft spacecraft = new Spacecraft(t);
	private LinkedList<Weapon> weapons = new LinkedList<>();
	private LinkedList<Asteroid> asteroids = new LinkedList<>();
	private LinkedList<Animation> animations = new LinkedList<>();

	// Asteroids generation:
	private Random rnd = new Random();
	private long spawnTimer = System.currentTimeMillis();
	private static int spawnTime = 2000;

	// Game stat:
	private int lastScore = -1;

	// Game flags:
	private boolean gameStarted = false, finishDetection = false;

	public GameController() {
	}

	/***
	 * Initialize the accelerometer controller.
	 */
	public void init() {
		AccController accelerometer = new AccController();
		String port = null;
		MainController mc = MainController.getInstance();

		try {
			do {
				port = mc.showInput("Choose the COM port", "Please, enter the name of the COM port:");
			} while (port == null);

			accelerometer.connect(port, this);
		} catch (Exception e) {
			mc.showError("Device not detected", "KL25Z was not detected in the port " + port);
			mc.exit();
		}

		mc.setAccelerometerInstance(accelerometer);
	}

	// Start a new game
	private void startGame() {
		t.setY(0);
		gameStarted = true;
	}

	// Finish a game in progress
	private void finishGame() {
		spacecraft.setShield(100);
		lastScore = t.getY() / 1000;
		t.setY(0);
		asteroids.clear();
		weapons.clear();
		animations.clear();
		finishDetection = false;
		gameStarted = false;
	}

	/***
	 * Method for managing objects and the relationships between them.
	 */
	public synchronized void physics() {

		// Remove objects that are out of the screen or destroyed
		asteroids.removeIf(a -> a.isOut());
		weapons.removeIf(w -> w.isOut());
		animations.removeIf(a -> a.isOut());

		// Generate asteroids only if game is started
		if (gameStarted) {
			if (t.getY() > 1000 && t.getY() < 4000)
				spawnTime = 1600;
			else if (t.getY() > 4000 && t.getY() < 6000)
				spawnTime = 1200;
			else if (t.getY() > 6000 && t.getY() < 8000)
				spawnTime = 800;
			else if (t.getY() > 8000 && t.getY() < 12000)
				spawnTime = 500;
			else if (t.getY() > 12000)
				spawnTime = 300;

			if (System.currentTimeMillis() - spawnTimer > spawnTime) {
				Asteroid asteroid = new Asteroid(t);
				asteroid.setPosition(-t.getX() + rnd.nextInt(Window.WIDTH) - asteroid.getW(),
						-t.getY() - asteroid.getH());

				asteroids.add(asteroid);
				spawnTimer = System.currentTimeMillis();
			}
		}

		// Check collisions between the spacecraft and asteroids
		asteroids.stream().filter(a -> !a.isCollisioned() && Physics.col(spacecraft, a)).forEach(a -> {
			if (spacecraft.collision(a.collision()))
				finishDetection = true;
			Point collisionCenter = Physics.getIntersection(spacecraft, a);
			collisionCenter.x -= t.getX();
			collisionCenter.y -= t.getY();
			animations.add(new Animation(collisionCenter, TypeOfAnimation.EXPLOSSION, t));
		});

		// Finish the game if the spacecraft is destroyed
		if (finishDetection) {
			finishGame();
			return;
		}

		// Check collisions between bullets and asteroids
		weapons.forEach(w -> {
			w.move();
			asteroids.forEach(a -> {
				if (!w.isCollisioned() && Physics.col(a, w)) {

					Point collisionCenter = Physics.getIntersection(w, a);
					collisionCenter.x -= t.getX();
					collisionCenter.y -= t.getY();
					animations.add(new Animation(collisionCenter, TypeOfAnimation.EXPLOSSION, t));
					w.collision();
					if (a.collisionWithWeapon()) {
						Point epicenter = new Point(a.getX() + a.getW() / 2 - t.getX(),
								a.getY() + a.getH() / 2 - t.getY());
						animations.add(new Animation(epicenter, TypeOfAnimation.DESTRUCTION, t));
					}

				}
			});
		});
	}

	/***
	 * Method for drawing the objects.
	 *
	 * @param gc
	 *            GraphicContext where objects will be drawn
	 */
	public void paint(GraphicsContext gc) {

		// Draw background
		t.draw(gc);

		// Draw terrain elements
		asteroids.forEach(a -> a.draw(gc));

		// Draw bullets
		for (Weapon w : weapons)
			w.draw(gc);

		// Draw animation
		for (Animation a : animations)
			a.draw(gc);

		// Draw spacecraft
		spacecraft.draw(gc);

		// Draw stats
		gc.setFill(Color.GREEN);
		if (gameStarted)
			gc.fillText("Score: " + t.getY() / 1000, Window.WIDTH - 160, Window.HEIGHT - 30);
		else {
			if (lastScore != -1)
				gc.fillText("Last score: " + lastScore, Window.WIDTH - 220, Window.HEIGHT - 30);

			gc.setFill(Color.YELLOW);
			if (lastScore != -1)
				gc.fillText("GAME OVER", Window.WIDTH / 2 - 50, 300);
			gc.fillText("Press the board's button to start the game", Window.WIDTH / 2 - 150, 330);
		}

	}

	private final static double X_THRESHOLD = 0.3;
	private final static double Y_THRESHOLD = 0.3;

	@Override
	public synchronized void receiveAccEvent(AccEvent event) {
		TypeOfEvent type = event.getTypeOfEvent();
		switch (type) {
		case BUTTON:
			if (!gameStarted)
				startGame();
			else if (spacecraft.shoot())
				weapons.add(new Weapon(spacecraft.getWX() - t.getX(), spacecraft.getWY() - t.getY(),
						spacecraft.getRadAng(), t));
			break;
		case X:
			if (event.getValue() < -X_THRESHOLD)
				spacecraft.moveSight(Course.LEFT);
			else if (event.getValue() > X_THRESHOLD)
				spacecraft.moveSight(Course.RIGHT);
			break;
		case Y:
			if (event.getValue() < -Y_THRESHOLD)
				spacecraft.moveYAxis(YAxisIntensity.FAST);
			else if (event.getValue() > Y_THRESHOLD)
				spacecraft.moveYAxis(YAxisIntensity.SLOW);
			break;
		default:
			break;
		}
	}

}
