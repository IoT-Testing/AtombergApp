package app;

import app.Login.Email;
import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.appmanagement.ApplicationState;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import static app.resources.AppInfo.*;
import static app.resources.Credentials.*;
import static app.resources.Endpoints.*;

/**
 * AppInitializer â€“ initialises the AndroidDriver and handles app launch.
 */
public class AppInitializer {

    public AndroidDriver atomberg;
    public AppiumDriverLocalService service;

    public AndroidDriver getDriver() { return atomberg; }

    /**
     * Constructs an AppInitializer that immediately wraps an existing driver.
     *
     * <p>Equivalent to calling the no-arg constructor followed by
     * {@link #setDriver(AndroidDriver)}. Use this when the driver is created
     * externally (e.g. by {@code BaseTest.setup()}) and you need a concise
     * one-liner to construct the initializer.</p>
     *
     * @param driver a non-null, already-connected {@link AndroidDriver}
     */
    public AppInitializer(AndroidDriver driver) {
        setDriver(driver);
    }

    /**
     * Injects an externally-created {@link AndroidDriver} into this initializer.
     *
     * <p>Called by test classes that manage the driver lifecycle themselves (via
     * {@code BaseTest.setup()}) so that helper methods such as
     * {@link #checkMainScreen()} use the same driver instance rather than
     * creating a second one.</p>
     *
     * @param driver a non-null, already-connected {@link AndroidDriver}
     * @throws IllegalArgumentException if {@code driver} is {@code null}
     */
    public void setDriver(AndroidDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException(
                    "setDriver: driver must not be null. " +
                            "Ensure BaseTest.setup() completed successfully before calling setDriver().");
        }
        atomberg = driver;
        // Apply a consistent implicit-wait so all element lookups through this
        // initializer respect the same timeout as the rest of the test suite.
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        logpoint("AppInitializer: driver set â€” session id: "
                + atomberg.getSessionId());
    }

    /**
     * Stops the locally-managed Appium service (if one was started via
     * {@link #initializeDriver()}) and quits the active driver session.
     *
     * <p>Safe to call even when the service was never started or the driver
     * was injected externally â€” both cases are handled gracefully.</p>
     */
    public void stopServer() {
        if (atomberg != null) {
            try {
                atomberg.quit();
                logpoint("AppInitializer: driver session closed.");
            } catch (Exception e) {
                System.err.println("AppInitializer: error closing driver session â€” " + e.getMessage());
            } finally {
                atomberg = null;
            }
        }

        if (service != null && service.isRunning()) {
            try {
                service.stop();
                logpoint("AppInitializer: Appium service stopped.");
            } catch (Exception e) {
                System.err.println("AppInitializer: error stopping Appium service â€” " + e.getMessage());
            } finally {
                service = null;
            }
        } else {
            logpoint("AppInitializer: no locally-managed Appium service to stop " +
                    "(service was either never started or is already stopped).");
        }
    }

    // â”€â”€ Driver setup â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Opens the app with the standard package + activity.
     */
    public void openApp() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage(ATOMBERG_HOME);
        options.setAppActivity(ATOMBERG_ACTIVITY);
        try {
            URL url = new URL(APPIUM_URL);
            atomberg = new AndroidDriver(url, options);
            atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed Appium URL: " + APPIUM_URL, e);
        }
    }

    /**
     * Initialises driver with a URL supplied by a running service instance.
     */
    public void initializeDriver() {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setCapability("platformName", "Android");
        options.setCapability("platformVersion", "15");
        URL url = service.getUrl();
        logpoint("Appium URL: " + url);
        atomberg = new AndroidDriver(url, options);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    // â”€â”€ Login / screen check â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Checks whether the app is on the login screen.
     * If so, logs in with default credentials.
     * Returns false (the home-screen check is handled by the caller).
     */
    public boolean checkMainScreen() throws Exception {
        ApplicationState state = atomberg.queryAppState(ATOMBERG_HOME);
        if (state != ApplicationState.RUNNING_IN_FOREGROUND) {
            atomberg.activateApp(ATOMBERG_HOME);
        }
        ActionsUtil.SSleep(10);
        WebElement loginIndicator = findOptional(
                By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
                        " with Atomberg\"]"));
        if (loginIndicator != null) {
            logpoint("Login screen detected â€“ logging in.");
            new Email(atomberg).email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
            ActionsUtil.SSleep(3);

            // FIX H6: email() does NOT call handlePostLoginFlow(), so OS permission dialogs
            // and the Alexa popup are never dismissed after auto-login via checkMainScreen().
            // Call PermissionUtil.allow() here to clear any permission dialogs before tests run.
            PermissionUtil.allow(atomberg);

            // Confirm we reached the home screen after auto-login
            AppUtil.confirmOnHomeScreen(atomberg);
        } else {
            logpoint("Already logged in.");
        }
        return loginIndicator != null;
    }

    // â”€â”€ Internal â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    private WebElement findOptional(By locator) {
        try {
            return atomberg.findElement(locator);
        } catch (Exception e) {
            return null;
        }
    }
}
