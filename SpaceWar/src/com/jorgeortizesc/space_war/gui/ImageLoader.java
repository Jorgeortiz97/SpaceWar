package com.jorgeortizesc.space_war.gui;

import javafx.scene.image.Image;

/***
 * Class for loading images in memory
 */
public class ImageLoader {

	public static Image[][] ANIMATION;
	public static Image[] SPACECRAFT;
	public static Image SNIPER_SIGHT;
	public static Image[] ASTEROIDS;
	public static Image BACKGROUND;

	private final static int ANIMATION_SZ = 2, EXPLOSSIONS_SZ = 3, DESTRUCTION_SZ = 6, SPACECRAFT_SZ = 3,
			ASTEROIDS_SZ = 4;

	public static void init() {

		ANIMATION = new Image[ANIMATION_SZ][];
		ANIMATION[0] = new Image[EXPLOSSIONS_SZ];
		for (int i = 0; i < EXPLOSSIONS_SZ; i++)
			ANIMATION[0][i] = new Image("file:img/exp" + i + ".png");

		ANIMATION[1] = new Image[DESTRUCTION_SZ];
		for (int i = 0; i < DESTRUCTION_SZ; i++)
			ANIMATION[1][i] = new Image("file:img/dis" + i + ".png");

		SPACECRAFT = new Image[SPACECRAFT_SZ];
		for (int i = 0; i < SPACECRAFT_SZ; i++)
			SPACECRAFT[i] = new Image("file:img/spacecraft" + i + ".png");

		SNIPER_SIGHT = new Image("file:img/sniper-sight.png");

		ASTEROIDS = new Image[ASTEROIDS_SZ];
		for (int i = 0; i < ASTEROIDS_SZ; i++)
			ASTEROIDS[i] = new Image("file:img/asteroid" + i + ".png");

		BACKGROUND = new Image("file:img/background.png");
	}

}
