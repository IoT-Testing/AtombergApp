package app.Login;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Guest - Handles guest mode entry via "Continue without login".
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Add safe element detection</li>
 *   <li>Improve error visibility</li>
 *   <li>Add logging</li>
 *   <li>Follow clean coding standards</li>
 * </ul>
 */
public class Guest {

    // === Locator Constants ===
    private static final By CONTINUE_WITHOUT_LOGIN_BUTTON = By.xpath(
            "//android.view.View[@content-desc='Continue without login']"
    );
    private static final By YES_CONFIRMATION_BUTTON = By.xpath(
            "//android.widget.Button[@content-desc='Yes']"
    );

    /**
     * Enters guest mode by skipping login and confirming dialog.
     *
     * @param driver AndroidDriver instance
     */
    public static void Mode(AndroidDriver driver) {
        logpoint("Entering guest mode...");

        if (clickElementWithWait(driver, CONTINUE_WITHOUT_LOGIN_BUTTON)) {
            System.err.println("Failed to click 'Continue without login'. Aborting guest mode.");
            return;
        }

        if (clickElementWithWait(driver, YES_CONFIRMATION_BUTTON)) {
            System.err.println("Failed to confirm guest mode with 'Yes'.");
            return;
        }

        logpoint("Successfully entered guest mode.");
    }

    // === Utility Methods ===

    /**
     * Waits up to N seconds and clicks element when available.
     *
     * @param driver  Driver instance
     * @param locator Element locator
     * @return true if clicked successfully
     */
    private static boolean clickElementWithWait(AndroidDriver driver, By locator) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < (long) 10 * 1000) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed() && isClickable(element)) {
                    element.click();
                    logpoint("Clicked: " + locatorToString(locator));
                    return false;
                }
            } catch (NoSuchElementException ignored) {
                // Try again after delay
            } catch (Exception e) {
                System.err.println("Error interacting with " + locatorToString(locator) + ": " + e.getMessage());
                return true;
            }
            app.util.ActionsUtil.sleep(500); // Wait before retry
        }
        logpoint("Timed out waiting for: " + locatorToString(locator));
        return true;
    }

    /**
     * Checks if element is clickable based on DOM attribute.
     *
     * @param element WebElement
     * @return true if clickable
     */
    private static boolean isClickable(WebElement element) {
        String clickable = element.getDomAttribute("clickable");
        return "true".equals(clickable);
    }

    /**
     * Converts By locator to readable string for logs.
     *
     * @param by Locator
     * @return Readable description
     */
    private static String locatorToString(By by) {
        return by.toString().split("-> ")[1]; // Simplify output
    }
}
