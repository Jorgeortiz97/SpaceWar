package com.jorgeortizesc.space_war.gui;

import com.jorgeortizesc.space_war.controller.GameController;
import com.jorgeortizesc.space_war.controller.MainController;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Window extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public final static int WIDTH = 800;
	public final static int HEIGHT = 600;

	// GUI Objects
	private GraphicsContext gc;

	// Controller
	private GameController gameController;

	public void start(Stage theStage) {

		theStage.setTitle("SpaceWar");

		Group root = new Group();
		Scene theScene = new Scene(root);
		theStage.setScene(theScene);

		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		root.getChildren().add(canvas);

		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.CYAN);

		MainController.getInstance();
		gameController = new GameController();

		// Timer for physics adn graphics
		new AnimationTimer() {
			private long startTime = System.currentTimeMillis();

			public void handle(long currentNanoTime) {

				gameController.physics();
				// Refresh Graphics 60 FPS
				if (System.currentTimeMillis() - startTime > 16) {
					gameController.paint(gc);
					startTime = System.currentTimeMillis();
				}
			}
		}.start();

		gc.setFont(new Font(18));

		theStage.setOnCloseRequest(e -> MainController.getInstance().exit());
		theStage.setHeight(HEIGHT);
		theStage.setWidth(WIDTH);
		theStage.setResizable(false);
		theStage.show();

		gameController.init();
	}
}
