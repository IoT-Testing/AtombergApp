package app.Fan;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * FanModels - Handles selection of fan models and their available colors.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Eliminate duplication via shared helpers</li>
 *   <li>Externalize color options and locators</li>
 *   <li>Add proper error handling</li>
 *   <li>Fix misleading logs</li>
 *   <li>Improve reliability with safe interaction patterns</li>
 * </ul>
 */
public class FanModels {
    private final AndroidDriver driver;

    // === Swipe Constants ===
    private static final double SWIPE_START_X_RATIO = 0.9;
    private static final double SWIPE_Y_RATIO = 0.67;
    private static final int POST_SWIPE_WAIT_MS = 500;

    public FanModels(AndroidDriver driver) {
        this.driver = driver;
    }

    /**
     * Selects the appropriate color variant based on visible options.
     * Used after selecting Six LED series.
     */
    public void SixLEDColorSelect() {
        if (isElementPresent("//android.widget.ImageView[@content-desc='Golden Oakwood']")) {
            RenesaPlus();
        } else if (isElementPresent("//android.widget.ImageView[@content-desc='Earth Brown']")) {
            StudioPlus();
        } else {
            Renesa();
        }
    }

    // === Model-Specific Color Selection Methods ===

    /**
     * Selects a random color for Renesa model (5 variants, some require swipe).
     */
    public void Renesa() {
        String[] colors = {"Brown and Black", "White and Black"};
        By[] xpaths = {
                By.xpath("//android.widget.ImageView[@content-desc='Brown and Black']"),
                By.xpath("//android.widget.ImageView[@content-desc='White and Black']")
        };

        String[] scrolledColors = {"Midnight Black", "Pebble Grey", "Misty Teal"};
        By[] scrolledXpaths = {
                By.xpath("//android.widget.ImageView[@content-desc='Midnight Black']"),
                By.xpath("//android.widget.ImageView[@content-desc='Pebble Grey']"),
                By.xpath("//android.widget.ImageView[@content-desc='Misty Teal']")
        };

        selectRandomColorWithScroll(colors, xpaths, scrolledColors, scrolledXpaths, 2);
    }

    /**
     * Selects a random color for Renesa+ model (4 variants, last two require swipe).
     */
    public void RenesaPlus() {
        String[] colors = {"Pearl White", "Golden Oakwood"};
        By[] xpaths = {
                By.xpath("//android.widget.ImageView[@content-desc='Pearl White']"),
                By.xpath("//android.widget.ImageView[@content-desc='Golden Oakwood']")
        };

        String[] scrolledColors = {"Earth Brown", "Natural Oakwood"};
        By[] scrolledXpaths = {
                By.xpath("//android.widget.ImageView[@content-desc='Earth Brown']"),
                By.xpath("//android.widget.ImageView[@content-desc='Natural Oakwood']")
        };

        selectRandomColorWithScroll(colors, xpaths, scrolledColors, scrolledXpaths, 2);
    }

    /**
     * Selects a random color for Studio+ model (2 variants).
     */
    public void StudioPlus() {
        By[] options = {
                By.xpath("//android.widget.ImageView[@content-desc='Marble White']"),
                By.xpath("//android.widget.ImageView[@content-desc='Earth Brown']")
        };
        selectRandomColor(options, "Studio+");
    }

    /**
     * Selects a random color for Aris model (2 variants).
     */
    public void Aris() {
        By[] options = {
                By.xpath("//android.widget.ImageView[@content-desc='Marble White']"),
                By.xpath("//android.widget.ImageView[@content-desc='Dark Teakwood']")
        };
        selectRandomColor(options, "Aris");
    }

    /**
     * Selects a random color for Jaguar model (3 variants, one requires swipe).
     */
    public void Jaguar() {
        String[] colors = {"Marble White", "Regent Gray"};
        By[] xpaths = {
                By.xpath("//android.widget.ImageView[@content-desc='Marble White']"),
                By.xpath("//android.widget.ImageView[@content-desc='Regent Gray']")
        };

        String[] scrolledColors = {"Matte Black"};
        By[] scrolledXpaths = {
                By.xpath("//android.widget.ImageView[@content-desc='Matte Black']")
        };

        selectRandomColorWithScroll(colors, xpaths, scrolledColors, scrolledXpaths, 2);
    }

    /**
     * Selects a random color for Erica model (2 variants).
     */
    public void Erica() {
        By[] options = {
                By.xpath("//android.widget.ImageView[@content-desc='Snow White']"),
                By.xpath("//android.widget.ImageView[@content-desc='Umber Brown']")
        };
        selectRandomColor(options, "Erica");
    }

    // === Internal Helpers ===

    /**
     * Clicks a random element from the given array of locators.
     *
     * @param locators Array of By strategies
     * @param label    Model name for logging
     */
    private void selectRandomColor(By[] locators, String label) {
        By selectedLocator = getRandomLocator(locators);
        clickElement(selectedLocator, getElementText(selectedLocator));
    }

    /**
     * Selects a random color, optionally using swipe to reveal more options.
     *
     * @param initialColors     First N non-scrolled colors
     * @param initialLocators   Locators for initial colors
     * @param scrolledColors    Colors available after swipe
     * @param scrolledLocators  Locators for scrolled colors
     * @param initialCount      Number of non-scrolled items before needing swipe
     */
    private void selectRandomColorWithScroll(
            String[] initialColors,
            By[] initialLocators,
            String[] scrolledColors,
            By[] scrolledLocators,
            int initialCount) {

        // Combine all possible choices
        String[] allColors = combineArrays(initialColors, scrolledColors);
        By[] allLocators = combineArrays(initialLocators, scrolledLocators);

        // Pick random index
        int choiceIndex = new java.util.Random().nextInt(allColors.length);

        // If choice is beyond initial count, swipe first
        if (choiceIndex >= initialCount) {
            ActionsUtil.Swipe.Right(driver, SWIPE_START_X_RATIO, SWIPE_Y_RATIO);
            ActionsUtil.sleep(POST_SWIPE_WAIT_MS);
        }

        String colorName = allColors[choiceIndex];
        System.out.println(colorName + " selected.");
        clickElement(allLocators[choiceIndex], colorName);
    }

    /**
     * Safely checks if an element exists by XPath.
     *
     * @param xpath Locator string
     * @return true if element is present
     */
    private boolean isElementPresent(String xpath) {
        try {
            return driver.findElement(By.xpath(xpath)).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Clicks element with logging.
     *
     * @param locator By strategy
     * @param label   Action label
     */
    private void clickElement(By locator, String label) {
        try {
            driver.findElement(locator).click();
            System.out.println(label + " clicked.");
        } catch (Exception e) {
            System.err.println("Failed to click '" + label + "': " + e.getMessage());
            throw new RuntimeException("Interaction failed: " + label, e);
        }
    }

    /**
     * Gets text from element's content-desc attribute.
     *
     * @param locator Element locator
     * @return content-desc value or empty string
     */
    private String getElementText(By locator) {
        try {
            return driver.findElement(locator).getDomAttribute("content-desc");
        } catch (NoSuchElementException e) {
            return "Unknown";
        }
    }

    /**
     * Returns a random locator from the array.
     *
     * @param locators Array of locators
     * @return Randomly chosen locator
     */
    private By getRandomLocator(By[] locators) {
        return locators[new java.util.Random().nextInt(locators.length)];
    }

    /**
     * Combines two arrays into one.
     *
     * @param a First array
     * @param b Second array
     * @return Combined array
     */
    private <T> T[] combineArrays(T[] a, T[] b) {
        T[] result = java.util.Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}