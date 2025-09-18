package app.ScreenCheck;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.stream.Collectors;

import static app.resources.Locators.HomeLocators.ANALYTICS_TAB;
import static app.resources.Locators.HomeLocators.MORE_TAB;

/**
 * ScreenCheck - Verifies presence and navigates to main app screens: Home, Analytics, More.
 */
public class ScreenCheck {
    private final AndroidDriver atomberg;

    public ScreenCheck(AndroidDriver atomberg) {
        this.atomberg = atomberg;
    }

    /**
     * Navigates to the More tab. If already selected, does nothing.
     */
    public void moreTab() {
        ensureOnScreen(MORE_TAB, "More");
        rateUsPopup();
    }

    /**
     * Navigates to the Home screen by clicking the Home tab (detected via "Hi" text).
     * Fails if no home tab is found.
     */
    public void homeScreen() {
        rateUsPopup();

        List<WebElement> candidateTabs = findVisibleTabs();
        WebElement homeTab = findHomeTab(candidateTabs);

        if (homeTab == null) {
            throw new RuntimeException("Home tab not found. Expected element with content-desc starting with 'Hi'");
        }

        clickIfNotSelected(homeTab, "Home");
        rateUsPopup();
    }

    /**
     * Navigates to the Analytics tab. If already selected, does nothing.
     */
    public void analytics() {
        ensureOnScreen(ANALYTICS_TAB, "Analytics");

        rateUsPopup();
    }

    // === Internal Helpers ===

    /**
     * Ensures a tab is visible and selected. Clicks only if unselected.
     *
     * @param xpath Locator string
     * @param label Tab name for logging
     */
    private void ensureOnScreen(By xpath, String label) {
        By locator = xpath;
        WebElement tab = waitForElement(locator, 10);

        System.out.println(label + " tab selected: " + tab.isSelected());

        if (!tab.isSelected()) {
            tab.click();
            System.out.println(label + " tab clicked.");
        }
    }

    /**
     * Finds all visible ImageView elements used as bottom tabs.
     *
     * @return List of tab candidates
     */
    private List<WebElement> findVisibleTabs() {
        List<WebElement> allImages = atomberg.findElements(By.className("android.widget.ImageView"));
        return allImages.stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());
    }

    /**
     * Finds the Home tab among visible tabs (identified by "Hi" prefix).
     *
     * @param tabs List of tab elements
     * @return Found Home tab or null
     */
    private WebElement findHomeTab(List<WebElement> tabs) {
        return tabs.stream()
                .filter(el -> {
                    String desc = el.getDomAttribute("content-desc");
                    return desc != null && desc.startsWith("Hi");
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * Clicks element only if not already selected.
     *
     * @param element Element to click
     * @param label   Label for logs
     */
    private void clickIfNotSelected(WebElement element, String label) {
        if (!element.isSelected()) {
            element.click();
            System.out.println(label + " tab clicked.");
        } else {
            System.out.println(label + " tab already selected.");
        }
    }

    /**
     * Dismisses "Rate Us" popup if present.
     */
    public void rateUsPopup() {
        try {
            List<WebElement> buttons = atomberg.findElements(By.className("android.widget.Button"));
            List<WebElement> cancelButtons = buttons.stream()
                    .filter(btn -> "Cancel".equals(btn.getDomAttribute("content-desc")))
                    .collect(Collectors.toList());

            for (WebElement cancelButton : cancelButtons) {
                if (cancelButton.isDisplayed() && cancelButton.isEnabled()) {
                    cancelButton.click();
                    System.out.println("Rate Us popup canceled.");
                    break; // Only one should appear
                }
            }
        } catch (Exception e) {
            System.err.println("Error while dismissing Rate Us popup: " + e.getMessage());
            // Don't fail test — popup is optional
        }
    }

    // === Utility Methods ===

    /**
     * Waits up to N seconds for element to be present.
     *
     * @param locator    Element locator
     * @param timeoutSec Timeout in seconds
     * @return WebElement if found
     */
    private WebElement waitForElement(By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                return atomberg.findElement(locator);
            } catch (NoSuchElementException ignored) {
                app.util.ActionsUtil.sleep(500);
            }
        }
        throw new RuntimeException("Element not found after " + timeoutSec + " seconds: " + locator);
    }
}