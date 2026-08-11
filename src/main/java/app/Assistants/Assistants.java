package app.Assistants;

import io.appium.java_client.android.AndroidDriver;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;

/**
 * Assistants - Orchestrates connection and disconnection of voice assistants.
 *   Amazon Alexa
 *   Google Home
 */
public class Assistants {
    private final AndroidDriver driver;

    // Optional: Use SLF4J for structured logging (recommended)
    // If not using SLF4J, plain System.out is fine
    private static final boolean USE_SLF4J = false;
    private void logInfo(String msg) {
        if (USE_SLF4J) {
             Logger logger = LoggerFactory.getLogger(Assistants.class);
             logger.info(()->msg);
        } else {
            logpoint("[Assistants] " + msg);
        }
    }

    public Assistants(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Tests full lifecycle of Alexa integration: connect â†’ disconnect.
     */
    public void testAlexaIntegration() {
        logInfo("Testing Alexa integration...");
        try {
            Alexa alexa = new Alexa(driver);
            alexa.Connect();
            logInfo("Alexa connected successfully.");

            alexa.Disconnect();
            logInfo("Alexa disconnected successfully.");
        } catch (Exception e) {
            System.err.println("Failed during Alexa test: " + e.getMessage());
        }
    }

    /**
     * Tests full lifecycle of Google Home integration: connect â†’ disconnect.
     */
    public void testGoogleHomeIntegration() {
        logInfo("Testing Google Home integration...");
        try {
            GoogleHome googleHome = new GoogleHome(driver);
            googleHome.Connect();
            logInfo("Google Home connected successfully.");

            googleHome.Disconnect();
            logInfo("Google Home disconnected successfully.");
        } catch (Exception e) {
            System.err.println("Failed during Google Home test: " + e.getMessage());
        }
    }

    /**
     * Runs all assistant integrations sequentially.
     */
    public void runAllAssistantTests() {
        logInfo("Starting all assistant integration tests...");

        testAlexaIntegration();
        testGoogleHomeIntegration();

        logInfo("All assistant tests completed.");
    }
}
