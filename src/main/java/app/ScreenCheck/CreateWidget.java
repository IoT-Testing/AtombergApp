package app.ScreenCheck;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CreateWidget - Handles creation of Atomberg Home Widget on Android launcher.
 *
 * <p>This version:
 * <ul>
 *   <li>Removes duplication</li>
 *   <li>Adds safe navigation</li>
 *   <li>Eliminates infinite loops</li>
 *   <li>Improves error visibility</li>
 * </ul>
 */
public class CreateWidget {
    private final AndroidDriver driver;

    // === Constants ===
    private static final int HOME_SCREEN_LONG_PRESS_X = 500;
    private static final int HOME_SCREEN_LONG_PRESS_Y = 1100;
    private static final int WIDGETS_TRAY_TAP_X = 540;
    private static final int WIDGETS_TRAY_TAP_Y = 2350;

    private static final By WIDGETS_MENU_ICON = By.xpath("(//android.widget.ImageView[@resource-id='com.android.launcher:id/item_icon'])[3]");
    private static final By ATOMBERG_WIDGET_ITEM = By.xpath("//android.widget.TextView[@content-desc='Atomberg Home widget']");
    private static final By DONE_BUTTON = By.xpath("//android.widget.Button[@resource-id='com.android.launcher:id/exit']");

    private static final int MAX_SCROLL_ATTEMPTS = 10;
    private static final long POST_ACTION_WAIT_MS = 1500;

    public CreateWidget(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Creates the Atomberg Home Widget on the device's home screen.
     */
    public void createWidget() {
        openWidgetsTray();
        selectAtombergWidget();
        confirmAndClose();
    }

    // === Internal Helpers ===

    /**
     * Opens the widgets tray from home screen.
     */
    private void openWidgetsTray() {
        System.out.println("Opening widgets tray...");
        ActionsUtil.Tap.withCoordinates(driver, WIDGETS_TRAY_TAP_X, WIDGETS_TRAY_TAP_Y);
        ActionsUtil.sleep(1000);

        ActionsUtil.longPress(driver, HOME_SCREEN_LONG_PRESS_X, HOME_SCREEN_LONG_PRESS_Y);
        ActionsUtil.sleep(1000);

        if (!clickElementIfExists(WIDGETS_MENU_ICON, "Widgets Menu")) {
            throw new RuntimeException("Failed to open widgets menu.");
        }
        ActionsUtil.sleep(1000);
    }

    /**
     * Scrolls and selects the Atomberg Home Widget.
     */
    private void selectAtombergWidget() {
        System.out.println("Searching for Atomberg Home widget...");

        if (clickElementIfExists(ATOMBERG_WIDGET_ITEM, "Atomberg Home Widget")) {
            return; // Found immediately
        }

        // Scroll up to find widget
        for (int i = 0; i < MAX_SCROLL_ATTEMPTS; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.sleep(800);

            if (clickElementIfExists(ATOMBERG_WIDGET_ITEM, "Atomberg Home Widget")) {
                return;
            }
        }

        throw new RuntimeException("Atomberg Home widget not found after " + MAX_SCROLL_ATTEMPTS + " scroll attempts.");
    }

    /**
     * Clicks the Done/Exit button to finalize widget placement.
     */
    private void confirmAndClose() {
        if (clickElementIfExists(DONE_BUTTON, "Done (Exit Widgets Tray)")) {
            ActionsUtil.sleep(POST_ACTION_WAIT_MS);
            System.out.println("Widget created successfully.");
        } else {
            System.err.println("Warning: Could not confirm widget creation — 'Done' button not found.");
        }
    }

    // === Utility Methods ===

    /**
     * Safely clicks element if present and displayed.
     *
     * @param locator Locator
     * @param label   Label for logs
     * @return true if clicked
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                System.out.println("Clicked: " + label);
                return true;
            } else {
                System.out.println(label + " found but not displayed.");
                return false;
            }
        } catch (NoSuchElementException e) {
            System.out.println(label + " not found.");
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking " + label + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Alternative method using list filtering (more robust).
     */
    public void createWidget2() {
        openWidgetsTray();

        List<WebElement> widgets = findLabeledElements().stream()
                .filter(el -> "Atomberg Home widget".equals(getAttribute(el, "content-desc")))
                .collect(Collectors.toList());

        if (!widgets.isEmpty()) {
            widgets.get(0).click();
            System.out.println("Selected Atomberg Home widget");
        } else {
            scrollAndFindWidget();
        }

        confirmAndClose();
    }

    /**
     * Scrolls and tries to find widget if not initially visible.
     */
    private void scrollAndFindWidget() {
        for (int i = 0; i < MAX_SCROLL_ATTEMPTS; i++) {
            ActionsUtil.Scroll.Up(driver);
            ActionsUtil.sleep(800);

            List<WebElement> candidates = findLabeledElements().stream()
                    .filter(el -> "Atomberg Home widget".equals(getAttribute(el, "content-desc")))
                    .collect(Collectors.toList());

            if (!candidates.isEmpty()) {
                candidates.get(0).click();
                return;
            }
        }
        throw new RuntimeException("Atomberg Home widget not found in launcher.");
    }

    /**
     * Gets all elements with non-null content-desc.
     */
    private List<WebElement> findLabeledElements() {
        return driver.findElements(AppiumBy.className("android.widget.TextView")).stream()
                .filter(el -> getAttribute(el, "content-desc") != null)
                .collect(Collectors.toList());
    }

    /**
     * Safely gets DOM attribute.
     */
    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return null;
        }
    }
}