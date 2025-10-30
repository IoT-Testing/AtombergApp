package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.util.List;
import java.util.stream.Collectors;

import static app.resources.Locators.MoreTabLocators.*;

/**
 * Play - Handles video tutorial playback in the 'Play' section.
 *
 * <p>This version avoids assertions and focuses on robust execution,
 * graceful failure, and reusability as a utility class.
 */
public class Play {
    private final AndroidDriver driver;

    // === Locator Constants ===

    // Swipe constants (adjust based on screen size if needed)
    private static final double SWIPE_START_X_RATIO = 0.80;
    private static final double SWIPE_Y_RATIO = 0.65;

    public Play(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Plays all major tutorial videos in sequence.
     */
    public void videos() {
        helpCheck();

        playVideo(APP_TOUR, "App Tour");
        playVideo(CONNECT_ALEXA, "Connect Alexa");
        playVideo(CONNECT_GOOGLE, "Connect Google");

        // Smart Locks section (may require horizontal swipe)
        if (!playVideoIfPresent(SMART_LOCKS_INSTALLATION, "Smart Locks Installation")) {
            ActionsUtil.Swipe.Left(driver, SWIPE_START_X_RATIO, SWIPE_Y_RATIO);
            playVideo(SMART_LOCKS_INSTALLATION, "Smart Locks Installation");
        }

        // Navigate back to start of lock videos
        ActionsUtil.Swipe.Left(driver, SWIPE_START_X_RATIO, SWIPE_Y_RATIO);
        ActionsUtil.sleep(2000);

        playVideo(SMART_LOCKS_FEATURES, "Smart Locks Features");
        playVideo(SMART_LOCKS_APP_SETUP, "Smart Locks App Setup");
    }

    // === Internal Helpers ===

    /**
     * Attempts to play a video by clicking its thumbnail.
     *
     * @param locator Locator of video
     * @param label   Label for logging
     * @return true if played successfully
     */
    private boolean playVideo(By locator, String label) {
        if (!clickElementIfExists(locator, label)) return false;

        AppUtil.captureScreenshot(driver);
        System.out.println(label + " Video Opened");

        // Simulate watching: go back twice (handles overlay/back buttons)
        videoTryCatch();
        videoTryCatch();

        ActionsUtil.sleep(5000); // Allow playback
        return true;
    }

    /**
     * Plays video only if already visible (no swipe needed).
     */
    private boolean playVideoIfPresent(By locator, String label) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                return playVideo(locator, label);
            }
        } catch (NoSuchElementException e) {
            return false;
        }
        return false;
    }

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
                System.out.println("Tap on " + label);
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
     * Navigates back until 'Video tutorials' is visible.
     * Limits attempts to prevent infinite loop.
     */
    private void videoTryCatch() {
        int attempts = 0;
        while (!isElementPresent(VIDEO_TUTORIALS_LINK) && attempts < 10) {
            System.out.println("Navigating back... attempt " + (++attempts));
            driver.navigate().back();
            ActionsUtil.sleep(1000);
        }

        if (!isElementPresent(VIDEO_TUTORIALS_LINK)) {
            System.err.println("Failed to return to 'Video tutorials' after 10 back presses.");
        }
    }

    /**
     * Ensures we are in the Help/Play section before playing videos.
     */
    private void helpCheck() {
        if (isElementPresent(VIDEO_TUTORIALS_LINK)) {
            return; // Already in correct section
        }

        List<WebElement> elements = getLabeledElements();
        for (WebElement el : elements) {
            String desc = getElementText(el);
            if (SELECT_AND_LINK_DEVICE.toString().contains(desc) ||
                    OPTIONS_HEADER.toString().contains(desc) ||
                    "Help".equals(desc)) {

                navigateToHelpSection();
                break;
            }
        }
    }

    /**
     * Clicks Help button after scrolling up.
     */
    private void navigateToHelpSection() {
        ActionsUtil.Scroll.Up(driver);
        clickElementIfExists(HELP_BUTTON, "Help");
    }

    // === Utility Methods ===

    /**
     * Safely checks if element is present.
     */
    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Gets all visible elements with non-null content-desc.
     */
    private List<WebElement> getLabeledElements() {
        return driver.findElements(By.className("android.view.View")).stream()
                .filter(el -> getElementText(el) != null)
                .collect(Collectors.toList());
    }

    /**
     * Gets text from element's content-desc attribute.
     */
    private String getElementText(WebElement el) {
        try {
            return el.getDomAttribute("content-desc");
        } catch (Exception e) {
            return null;
        }
    }
}