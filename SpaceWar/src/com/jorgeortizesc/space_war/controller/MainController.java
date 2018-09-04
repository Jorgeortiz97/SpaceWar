package com.jorgeortizesc.space_war.controller;

import java.util.Optional;

import com.jorgeortizesc.acc.AccController;
import com.jorgeortizesc.space_war.gui.ImageLoader;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextInputDialog;

public class MainController {
	private static MainController instance = null;
	private AccController a;

	private MainController() {
		ImageLoader.init();

	}

	public static MainController getInstance() {
		if (instance == null)
			instance = new MainController();
		return instance;
	}

	public String showInput(String title, String message) {
		TextInputDialog dialog = new TextInputDialog("COM1");
		dialog.setTitle(title);
		dialog.setHeaderText(null);
		dialog.setContentText(message);

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent())
			return result.get();
		return null;
	}

	private void showAlert(String title, String message, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public void showMessage(String title, String msg) {
		showAlert(msg, title, AlertType.INFORMATION);
	}

	public void showError(String title, String msg) {
		showAlert(msg, title, AlertType.ERROR);
	}

	public void exit() {
		if (a != null)
			a.disconnect();
		System.exit(0);
	}

	// ACCELEROMETER
	public void setAccelerometerInstance(AccController a) {
		this.a = a;
	}

}
