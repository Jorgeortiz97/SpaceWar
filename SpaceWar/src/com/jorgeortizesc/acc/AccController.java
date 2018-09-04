package com.jorgeortizesc.acc;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/***
 * The class AccController provides a method for establishing a serial
 * connection with a KL25Z device and receiving data from its accelerometer
 */
public class AccController {

	private SerialReader serialReaderThread;
	private SerialPort serialPort;

	private final static int BAUD_RATE = 9600;

	/***
	 * Establishes a connection in the indicated serial port.
	 *
	 * @param portName
	 *            Name of the port name. Example: "COM3"
	 * @param listener
	 *            Object which will be notified with AccEvents
	 * @throws IOException
	 *             if port doesn't exist, is being used or doesn't support
	 *             specified operations
	 * @throws IllegalArgumentException
	 *             if specified port is not a valid serial port
	 */
	public void connect(String portName, IAccListener listener) throws IOException, IllegalArgumentException {
		CommPortIdentifier portIdentifier;
		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e) {
			throw new IOException("Port " + portName + " doesn't exist");
		}

		boolean used = false;
		CommPort commPort = null;

		if (portIdentifier.isCurrentlyOwned())
			used = true;
		else {
			try {
				commPort = portIdentifier.open(this.getClass().getName(), 2000);
			} catch (PortInUseException e) {
				used = true;
			}
		}

		if (used)
			throw new IOException("Port " + portName + " is being used");

		if (commPort instanceof SerialPort) {
			serialPort = (SerialPort) commPort;
			try {
				serialPort.setSerialPortParams(BAUD_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);
			} catch (UnsupportedCommOperationException e) {
				throw new IOException("Port " + portName + " doesn't support specified operations");
			}

			InputStream in = serialPort.getInputStream();

			serialReaderThread = new SerialReader(in, listener);
			serialReaderThread.start();
		} else
			throw new IllegalArgumentException();
	}

	/***
	 * Closes this object and releases any system resources associated with it.
	 */
	public void disconnect() {
		serialReaderThread.finish();
		serialPort.close();
	}

}