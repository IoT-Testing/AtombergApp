package app.ScreenCheck;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;

import static app.resources.Locators.Android.HomeLocators.*;

/**
 * ScreenCheck – verifies presence on and navigates to main app screens:
 * Home, Analytics, and More tab.
 *
 * <p>Designed as a thin helper: each method ensures the correct screen is
 * active and dismisses any transient popups (Rate Us, Alexa) that may block
 * subsequent actions.</p>
 */
public class ScreenCheck {

    private final AndroidDriver atomberg;

    public ScreenCheck(AndroidDriver atomberg) {
        this.atomberg = atomberg;
    }

    // ── Tab navigation ────────────────────────────────────────────────────────

    /**
     * Navigates to the More tab. If the tab is already selected, does nothing.
     */
    public void moreTab() {
        ensureOnScreen(MORE_TAB, "More");
        rateUsPopup();
    }

    /**
     * Navigates to the Home (Devices) screen.
     * Dismisses Rate Us popup if present.
     */
    public void homeScreen() {
        rateUsPopup();
        ensureOnScreen(DEVICES, "Home");
    }

    /**
     * Navigates to the Analytics tab.
     * Dismisses Rate Us popup if present.
     */
    public void analytics() {
        ensureOnScreen(ANALYTICS_TAB, "Analytics");
        rateUsPopup();
    }

    // ── Popup dismissal ───────────────────────────────────────────────────────

    /**
     * Dismisses a "Rate Us" popup if one is currently displayed.
     * Failures are swallowed — the popup is optional and must not fail tests.
     */
    public void rateUsPopup() {
        try {
            List<WebElement> buttons = atomberg.findElements(AppiumBy.className("android.widget.Button"));
            List<WebElement> cancelButtons = buttons.stream()
                    .filter(btn -> "Cancel".equals(btn.getDomAttribute("content-desc")))
                    .toList();

            for (WebElement cancelButton : cancelButtons) {
                if (cancelButton.isDisplayed() && cancelButton.isEnabled()) {
                    cancelButton.click();
                    System.out.println("Rate Us popup dismissed.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error while dismissing Rate Us popup: " + e.getMessage());
        }
    }

    // ── Internal helpers ──────────────────────────────────────────────────────

    /**
     * Ensures a tab is visible and selected. Clicks only if not already selected.
     *
     * @param locator  locator for the tab element
     * @param label    human-readable tab name used in log messages
     */
    private void ensureOnScreen(By locator, String label) {
        WebElement tab = waitForElement(locator);
        System.out.println(label + " tab selected: " + tab.isSelected());
        if (!tab.isSelected()) {
            tab.click();
            System.out.println(label + " tab clicked.");
        }
    }

    /**
     * Polls for an element until the timeout elapses, returning it if found.
     *
     * @param locator element locator
     * @return the {@link WebElement} once visible
     * @throws RuntimeException if the element is not found within the timeout
     */
    private WebElement waitForElement(By locator) {
        long deadline = System.currentTimeMillis() + (long) 10 * 1_000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                return atomberg.findElement(locator);
            } catch (NoSuchElementException ignored) {
                app.util.ActionsUtil.sleep(500);
            }
        }
        throw new RuntimeException(
                "Element not found after " + (long) 10 + "s: " + locator);
    }

    @SuppressWarnings("unused")
    private WebElement findOptionalElement(By locator) {
        try {
            return atomberg.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}
