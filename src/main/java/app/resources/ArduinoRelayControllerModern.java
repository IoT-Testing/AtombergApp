package app.resources;

import com.fazecast.jSerialComm.*;

/**
 * Arduino LED Controller using jSerialComm Library
 * 
 * Modern alternative to RXTX library for serial communication
 * 
 * Requirements:
 * - jSerialComm library (add to classpath)
 *   Maven: com.fazecast:jserialcomm:2.10.4
 * 
 * @author Arduino Serial Communication Example
 * @version 1.0
 */
public class ArduinoRelayControllerModern {
    public SerialPort serialPort;
    private static final int BAUD_RATE = 9600;
    private static final int TIMEOUT_MS = 1000;

    /**
     * List all available serial ports
     */
    public static void listPorts() {
        logpoint("\nAvailable Serial Ports:");
        SerialPort[] ports = SerialPort.getCommPorts();

        if (ports.length == 0) {
            logpoint("No serial ports found!");
        } else {
            for (int i = 0; i < ports.length; i++) {
                System.out.printf("%d. %s - %s\n", 
                    i + 1, 
                    ports[i].getSystemPortName(), 
                    ports[i].getDescriptivePortName());
            }
        }
    }

    /**
     * Connect to Arduino on specified port
     */
    public boolean connect(String portName) {
        try {
            serialPort = SerialPort.getCommPort(portName);
            serialPort.setBaudRate(BAUD_RATE);
            serialPort.setComPortTimeouts(
                SerialPort.TIMEOUT_WRITE_BLOCKING | SerialPort.TIMEOUT_READ_SEMI_BLOCKING,
                TIMEOUT_MS,
                TIMEOUT_MS
            );

            if (serialPort.openPort()) {
                logpoint("Connected to Arduino on " + portName);

                // Wait for Arduino to initialize
                Thread.sleep(2000);

                return true;
            } else {
                System.err.println("Failed to open port: " + portName);
                return false;
            }

        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Auto-detect and connect to Arduino
     */
    public boolean autoConnect() {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            String portName = port.getSystemPortName();
            String description = port.getDescriptivePortName().toLowerCase();
            // Try to find Arduino-like ports
            if(description.contains("com18")){
                if (description.contains("arduino") ||
                        description.contains("ch340") ||
                        description.contains("usb") ||
                        portName.startsWith("COM") ||
                        portName.contains("ttyUSB") ||
                        portName.contains("ttyACM")) {

                    logpoint("Trying to connect to: " + portName);

                    if (connect(portName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Send JSON command to control LED
     */
    public void sendLEDCommand(boolean ledState) {
        if (serialPort == null || !serialPort.isOpen()) {
            System.err.println("Error: Serial port not open");
            return;
        }
        logpoint(serialPort.isOpen());
        try {
            String jsonCommand = "{\"power_toggle\":" + ledState + "}\n";
            byte[] buffer = jsonCommand.getBytes();

            int bytesWritten = serialPort.writeBytes(buffer, buffer.length);

            if (bytesWritten > 0) {
                logpoint("Sent: " + jsonCommand.trim());
            } else {
                System.err.println("Failed to send command");
            }

        } catch (Exception e) {
            System.err.println("Send error: " + e.getMessage());
        }
    }

    /**
     * Read response from Arduino (optional)
     */
    public void readResponse() {
        if (serialPort == null || !serialPort.isOpen()) {
            return;
        }

        try {
            byte[] buffer = new byte[1024];
            int numRead = serialPort.readBytes(buffer, buffer.length);

            if (numRead > 0) {
                String response = new String(buffer, 0, numRead);
                logpoint("Arduino: " + response.trim());
            }

        } catch (Exception e) {
            // Ignore read errors
        }
    }

    /**
     * Disconnect from Arduino
     */
    public void disconnect() {
        if(serialPort != null){
            serialPort.removeDataListener();
        }
        if (serialPort != null && serialPort.isOpen()) {
            serialPort.closePort();
            logpoint("Disconnected from Arduino");
        }
    }


    /**
     * Main method - Interactive control
     */
    public void relayController(String[] args) {
        ArduinoRelayControllerModern controller = new ArduinoRelayControllerModern();
        // List available ports
        listPorts();
        // Try to connect
        boolean connected = false;

        if (args.length > 0) {
            // Use port from command line argument
            connected = controller.connect(args[0]);
        } else {
            // Try auto-connect
            logpoint("Attempting auto-connect...");
            connected = controller.autoConnect();
            if(!connected) {
                controller.disconnect();
                connected = controller.connect("COM18");
            }
        }

        if (!connected) {
            System.err.println("Failed to connect to Arduino");
            System.err.println("Usage: java ArduinoLEDControllerModern [PORT_NAME]");
            System.err.println("Example: java ArduinoLEDControllerModern COM3");
            return;
        }
        // Interactive control loop
        controller.sendLEDCommand(true);
        controller.disconnect();
        logpoint("\nProgram terminated.");
    }
}

