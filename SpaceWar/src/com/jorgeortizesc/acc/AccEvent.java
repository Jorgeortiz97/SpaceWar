package com.jorgeortizesc.acc;

/***
 * The class AccEvent encapsulates the necessary information to represent an
 * accelerometer event that happened on the device.
 */
public class AccEvent {

	/***
	 * Types of event related with the accelerometer.
	 */
	public enum TypeOfEvent {
		/***
		 * X-Axis changed
		 */
		X,
		/***
		 * Y-Axis changed
		 */
		Y,
		/***
		 * Z-Axis changed
		 */
		Z,
		/***
		 * Button pressed
		 */
		BUTTON
	}

	private TypeOfEvent type;
	private double value;

	public AccEvent(TypeOfEvent event, double value) {
		this.type = event;
		this.value = value;
	}

	/***
	 * Returns the type of the event.
	 *
	 * @return Type of the event
	 */
	public TypeOfEvent getTypeOfEvent() {
		return type;
	}

	/***
	 * Returns the value associated with the event
	 *
	 * @return a double between [-1, 1]
	 */
	public double getValue() {
		return value;
	}

}
