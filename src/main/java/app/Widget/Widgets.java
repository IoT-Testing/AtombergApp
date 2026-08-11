package app.Widget;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import static app.util.AppUtil.*;

/**
 * Widgets - Handles interaction with Atomberg Home Widget on Android launcher.
 *
 * <p>This version fixes critical bugs in sleep logic, null handling,
 * and improves reliability and maintainability.
 */
public class Widgets {

    // === Locator Constants ===
    private static final By WIDGET_CONTAINER = AppiumBy.className("android.widget.RelativeLayout");
    private static final By BUTTON_SPEED_UP = By.id("com.atomberg.app:id/bt_up");
    private static final By BUTTON_SPEED_DOWN = By.id("com.atomberg.app:id/bt_down");
    private static final By BUTTON_PREV_FAN = By.id("com.atomberg.app:id/bt_prev");
    private static final By BUTTON_NEXT_FAN = By.id("com.atomberg.app:id/bt_next");
    private static final By BUTTON_POWER = By.id("com.atomberg.app:id/bt_power");

    /**
     * Interacts with the Atomberg Home Widget.
     *
     * @param atomberg Driver instance
     */
    public static void Home(AndroidDriver atomberg) {
        if (!ensureWidgetVisible(atomberg)) {
            System.err.println("Widget not found after swiping. Cannot interact.");
            return;
        }

        try {
            WebElement spdBtUp = atomberg.findElement(BUTTON_SPEED_UP);
            WebElement spdBtDown = atomberg.findElement(BUTTON_SPEED_DOWN);
            WebElement prevFan = atomberg.findElement(BUTTON_PREV_FAN);
            WebElement nextFan = atomberg.findElement(BUTTON_NEXT_FAN);
            WebElement powerBt = atomberg.findElement(BUTTON_POWER);

            // Simulate interactions
            logpoint("Interacting with widget...");

            prevFan.click();
            sleep(1000);

            spdBtUp.click();
            sleep(1000);

            powerBt.click();
            sleep(1000);

            spdBtDown.click();
            sleep(1000);

            nextFan.click();
            logpoint("Widget interaction completed.");

        } catch (NoSuchElementException e) {
            System.err.println("One or more widget buttons not found: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during widget interaction: " + e.getMessage());
        }
    }

    /**
     * Ensures the widget is visible by swiping right if needed.
     *
     * @param driver Driver instance
     * @return true if widget is present
     */
    private static boolean ensureWidgetVisible(AndroidDriver driver) {
        if (isElementPresent(driver, WIDGET_CONTAINER)) {
            return true;
        }

        logpoint("Widget not found. Swiping right...");
        for (int i = 0; i < 5; i++) {
            ActionsUtil.Swipe.Right(driver, 0.50, 0.80);
            ActionsUtil.sleep(800);

            if (isElementPresent(driver, WIDGET_CONTAINER)) {
                logpoint("Widget found after swipe #" + (i + 1));
                return true;
            }
        }

        return false;
    }

    /**
     * Safely checks if element is present.
     */


    /**
     * Pauses execution for given milliseconds.
     *
     * @param millis Duration to sleep
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Sleep interrupted: " + e.getMessage());
        }
    }
}
