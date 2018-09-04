package com.jorgeortizesc.acc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jorgeortizesc.acc.AccEvent.TypeOfEvent;

/***
 * The class SerialReader provides methods to create and destroy a thread which
 * reads data from the serial connection and transmit it toward the listener.
 */
public class SerialReader extends Thread {
	private BufferedReader bufReader;
	private volatile boolean finish;
	private IAccListener listener;

	private final static char[] COMMANDS = { 'X', 'Y', 'Z', 'B' };

	private final static TypeOfEvent[] EVENTS = { TypeOfEvent.X, TypeOfEvent.Y, TypeOfEvent.Z, TypeOfEvent.BUTTON };

	/***
	 * Constructor for SerialReader.
	 *
	 * @param in
	 *            InputStream from an open SerialPort object.
	 * @param listener
	 *            Listener which will be notified with AccEvents.
	 */
	public SerialReader(InputStream in, IAccListener listener) {
		this.bufReader = new BufferedReader(new InputStreamReader(in));
		this.listener = listener;
	}

	/***
	 * Closes the reader and releases the resources.
	 */
	public void finish() {
		finish = true;
		if (bufReader != null)
			try {
				bufReader.close();
			} catch (IOException e) {
			}
	}

	private void sendEvent(AccEvent event) {
		listener.receiveAccEvent(event);
	}

	/***
	 * This thread sends the data to the listener object.
	 */
	public void run() {
		String line;
		try {
			while (finish == false) {
				line = bufReader.readLine();
				if (line != null) {
					char cmd = line.charAt(0);
					int i = 0;
					while (i < COMMANDS.length && cmd != COMMANDS[i])
						i++;
					if (i != COMMANDS.length) {
						double val = 0.0;
						try {
							val = Double.parseDouble(line.substring(1));
						} catch (NumberFormatException e) {
							continue;
						}
						sendEvent(new AccEvent(EVENTS[i], val));
					}
				}
			}
		} catch (IOException e) {
			finish();
		}
	}

}