package app.WaterPurifier;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;

import java.util.*;
import java.util.stream.Collectors;

import static app.util.AppUtil.waitForElement;

/**
 * ROManagement - Manages Water Purifier (RO) devices: add, control, health check.
 *
 * <p>This version avoids all assertions and focuses on robust execution,
 * graceful failure, and reusability as a utility class.
 */
public class ROManagement {
    private final AndroidDriver atomberg;
    private final ScreenCheck screenCheck;

    // === Locator Constants ===
    private static final By ADD_BUTTON = By.xpath("(//android.widget.ImageView)[3]"); // Fallback add button
    private static final By DEVICE_SEARCH_TEXT = By.xpath("//android.view.View[@content-desc=\"Atomberg Smart Water Purifier\"]");
    private static final By PAIRING_IN_PROGRESS = By.xpath("//android.view.View[@content-desc=\"Pairing with device\"]");
    private static final By SELECT_COLOR_HEADER = By.xpath("//android.view.View[@content-desc=\"Select your device color\"]");
    private static final By NEXT_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Next\"]");
    private static final By ADD_FIRST_DEVICE_ICON = By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]");
    private static final By WATER_PURIFIER_BADGE = By.xpath("//android.widget.ImageView[@content-desc=\"Water Purifier\"]");
    private static final By BUY_NOW_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Buy Now!\"]");
    private static final By CHECK_HEALTH_BUTTON = By.xpath("//android.view.View[@content-desc=\"Check Health\"]");
    private static final By MODE_BUTTON = By.xpath("//android.widget.ImageView[@content-desc=\"Mode\"]");
    private static final By CONTENT_DESC_VIEW = By.className("android.view.View");

    public ROManagement(AndroidDriver driver) {
        this.atomberg = driver;
        this.screenCheck = new ScreenCheck(driver);
    }

    /**
     * Adds a new RO device via scan/add flow.
     */
    public void addRO() {
        System.out.println("Searching for available RO devices...");

        if (!clickAddButton()) {
            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1850);
            ActionsUtil.sleep(1000);
        }

        ActionsUtil.sleep(15000); // Allow discovery

        WebElement roDevice = waitForElement(atomberg, DEVICE_SEARCH_TEXT, 10);
        if (roDevice != null) {
            ActionsUtil.Tap.connectButton(atomberg, roDevice);
        } else {
            System.err.println("No \"Atomberg Smart Water Purifier\" found during scan.");
            return;
        }

        // Confirm pairing started
        if (isElementPresent(PAIRING_IN_PROGRESS)) {
            System.out.println("Pairing with RO device...");
        } else {
            System.err.println("Pairing did not start. Expected \"Pairing with device\" message.");
        }
    }

    /**
     * Completes post-addition setup process.
     */
    public void ROAdditionProcess() {
        ActionsUtil.SSleep(3);

        if (!isElementPresent(SELECT_COLOR_HEADER)) {
            System.err.println("Device color selection screen not displayed.");
            return;
        }

        clickElementIfExists(NEXT_BUTTON, "Next");
        AppUtil.additionProcess(atomberg);
        ActionsUtil.SSleep(5);
    }

    /**
     * Checks if any RO is added and online; adds one if none exist.
     */
    public void checkRO() {
        screenCheck.homeScreen();

        if (isAddFirstDeviceVisible()) {
            addRO();
            ROAdditionProcess();
            screenCheck.homeScreen(); // Return to verify
        } else {
            checkROOnline();
        }
    }

    // === Internal Helpers ===

    /**
     * Clicks the Add button if present.
     */
    private boolean clickAddButton() {
        try {
            WebElement addButton = atomberg.findElement(ADD_BUTTON);
            addButton.click();
            ActionsUtil.sleep(1000);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if "Add your first smart device" is visible.
     */
    private boolean isAddFirstDeviceVisible() {
        try {
            return atomberg.findElement(ADD_FIRST_DEVICE_ICON).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Verifies RO is online and interacts with it.
     */
    private void checkROOnline() {
        if (!clickElementIfExists(WATER_PURIFIER_BADGE, "Water Purifier")) {
            System.out.println("No RO Online or badge not found.");
            return;
        }

        ActionsUtil.sleep(3000);

        if (isElementPresent(BUY_NOW_BUTTON)) {
            System.out.println("RO requires purchase action.");
            return;
        }

        List<WebElement> purifiers = getLabeledElements().stream()
                .filter(el -> Objects.requireNonNull(el.getDomAttribute("content-desc")).endsWith("Living Room"))
                .collect(Collectors.toList());

        if (purifiers.isEmpty()) {
            System.out.println("No RO devices online.");
            return;
        }

        System.out.println("RO Available: " + purifiers.size());
        for (WebElement purifier : purifiers) {
            String name = purifier.getDomAttribute("content-desc");
            System.out.println("Interacting with: " + name);
            purifier.click();
            ROControl();
        }
    }

    /**
     * Controls RO: health check, mode switching.
     */
    private void ROControl() {
        if (!clickElementIfExists(CHECK_HEALTH_BUTTON, "Check Health")) return;

        ActionsUtil.SSleep(1);
        inspectHealth();
        ActionsUtil.SSleep(1);
        changeModes();
        atomberg.navigate().back();
    }

    /**
     * Inspects health metrics.
     */
    private void inspectHealth() {
        List<WebElement> elements = getLabeledElements();
        if (elements.size() > 2) {
            elements.remove(0);
            elements.remove(elements.size() - 1); // Remove header/footer
        }

        System.out.println("=== RO Health Metrics ===");
        for (WebElement el : elements) {
            System.out.println("- " + el.getDomAttribute("content-desc"));
        }
        atomberg.navigate().back();
    }

    /**
     * Cycles through available modes.
     */
    private void changeModes() {
        if (!clickElementIfExists(MODE_BUTTON, "Mode")) return;

        List<WebElement> modeElements = getLabeledElements();
        modeElements.remove(modeElements.size() - 1); // Remove last (footer)

        Random random = new Random();

        for (int i = 0; i < modeElements.size(); i++) {
            WebElement modeEl = modeElements.get(i);
            String content = modeEl.getDomAttribute("content-desc");
            System.out.println("Setting RO mode: " + content);
            modeEl.click();

            if ("Custom Taste Preference".equals(content)) {
                selectRandomTastePreference(random);
            }

            if (i < modeElements.size() - 1) {
                if (!clickElementIfExists(MODE_BUTTON, "Mode (reopen)")) break;
                modeElements = getLabeledElements();
                modeElements.remove(modeElements.size() - 1);
            }
        }
    }

    /**
     * Selects a random option under \"Custom Taste Preference\".
     */
    private void selectRandomTastePreference(Random random) {
        List<WebElement> options = getLabeledElements();
        options.remove(options.size() - 1); // Remove footer

        if (!options.isEmpty()) {
            WebElement selected = options.get(random.nextInt(options.size()));
            System.out.println(selected.getDomAttribute("content-desc") + " Selected");
            selected.click();
        }
    }

    // === Utility Methods ===

    /**
     * Safely checks if element is present.
     */
    private boolean isElementPresent(By locator) {
        try {
            return atomberg.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Safely clicks element if present and displayed.
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = atomberg.findElement(locator);
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
     * Gets all elements with non-null content-desc.
     */
    private List<WebElement> getLabeledElements() {
        return atomberg.findElements(CONTENT_DESC_VIEW).stream()
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