package app.Automations;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.*;
import java.time.Duration;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Automation - Manages automation creation and deletion: time-based, quick access.
 *
 * <p>This version avoids all assertions and focuses on robust execution,
 * graceful failure, and reusability as a utility class.
 */
public class Automation {
    private final AndroidDriver driver;

    // === Constants ===
    private static final By AUTOMATIONS_TAB = By.xpath("//android.view.View[@content-desc='Automations']");
    private static final By NEW_AUTOMATION_BUTTON = By.xpath("(//android.widget.ImageView)[3]"); // Fallback add button
    private static final By QUICK_ACCESS_SECTION = By.xpath("//android.widget.ImageView[@content-desc='Quick access']");
    private static final By TIME_OF_DAY_SECTION = By.xpath("//android.widget.ImageView[@content-desc='Time of day']");
    private static final By ADD_BUTTON = By.xpath("//android.widget.Button[@content-desc='Add']");
    private static final By OK_BUTTON = By.xpath("//android.widget.Button[@content-desc='OK']");
    private static final By DELETE_CONFIRM_BUTTON = By.xpath("//android.widget.Button[@content-desc='Yes']");
    private static final String SUCCESS_DELETE_MESSAGE = "Automation Removed Successfully";
    private static final String SUCCESS_ADD_MESSAGE = "Scheduled Automation Added Successfully";

    private final Random random = new Random();

    public Automation(AndroidDriver driver) {
        this.driver = driver;
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(6));

    }

    /**
     * Creates a time-based automation.
     */
    public void TimeOfDay() {
        navigateToAutomations();
        openNewAutomationFlow();
        createScheduledAutomation();
    }

    /**
     * Creates a quick access automation.
     */
    public void QuickAccess() {
        ActionsUtil.sleep(2000);
        navigateToAutomations();

        if (!clickElementIfExists(QUICK_ACCESS_SECTION, "Quick Access")) {
            System.err.println("Quick Access section not found.");
            return;
        }

        if (!openNewAutomationFlow()) return;

        // Enter name
        WebElement nameInput = waitForElement(By.xpath("//android.widget.EditText"), 10);
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());
        nameInput.sendKeys("QA" + timestamp);

        selectRandomFan();
        selectRandomAction();
        clickElementIfExists(ADD_BUTTON, "Add");

        if (isSuccessMessageDisplayed("Quick Access Automation Added Successfully")) {
            logpoint("Quick Access automation created successfully.");
        } else {
            System.err.println("Failed to confirm Quick Access creation.");
        }
    }

    /**
     * Navigates to Automations tab.
     */
    private void navigateToAutomations() {
        WebElement automationsTab = driver.findElement(AUTOMATIONS_TAB);
        if (automationsTab != null && automationsTab.isDisplayed()) {
            automationsTab.click();
            logpoint("Navigated to Automations");
        } else {
            System.err.println("Automations tab not available.");
        }
    }

    /**
     * Opens the 'New Automation' dialog.
     *
     * @return true if opened successfully
     */
    private boolean openNewAutomationFlow() {
        try {
            // Try specific new automation button first
            By newAutoLocator = By.xpath("//android.view.View[@content-desc='Schedule actions']/android.widget.ImageView[1]");
            if (clickElementIfExists(newAutoLocator, "New Automation (Specific)")) {
                return true;
            }

            // Fallback: generic add button
            return clickElementIfExists(NEW_AUTOMATION_BUTTON, "New Automation (Fallback)");
        } catch (Exception e) {
            System.err.println("Error opening new automation: " + e.getMessage());
            return false;
        }
    }

    /**
     * Fills out a scheduled automation form.
     */
    private void createScheduledAutomation() {
        String timestamp = new SimpleDateFormat("HHmmss").format(new Date());

        WebElement nameInput = waitForElement(By.xpath("//android.widget.EditText"), 10);
        nameInput.sendKeys("Automation" + timestamp);

        selectRandomFan();

        // Select time
        clickElementIfExists(By.xpath("//android.view.View[@content-desc='Select time']"), "Select Time");
        adjustSeekBar();
        ActionsUtil.sleep(250);
        adjustSeekBar();
        ActionsUtil.sleep(1000);
        clickElementIfExists(By.xpath("//android.widget.Button[@content-desc='Ok']"), "OK (Time Picker)");

        selectRandomAction();

        ActionsUtil.Scroll.Up(driver);
        clickElementIfExists(By.xpath("//android.widget.Switch"), "Enable Switch");
        clickElementIfExists(ADD_BUTTON, "Add");

        if (isSuccessMessageDisplayed(SUCCESS_ADD_MESSAGE)) {
            logpoint("New automation created successfully.");
        } else {
            System.err.println("Failed to confirm automation creation.");
        }
    }

    /**
     * Selects one or more fans randomly.
     */
    private void selectRandomFan() {
        clickElementIfExists(By.xpath("//android.view.View[@content-desc='Select fans']"), "Select Fans");

        List<WebElement> checkboxes = driver.findElements(AppiumBy.className("android.widget.CheckBox"));
        if (checkboxes.isEmpty()) {
            System.err.println("No fan checkboxes found.");
            return;
        }

        int count = random.nextInt(checkboxes.size()) + 1; // At least one
        for (int i = 0; i < count && i < checkboxes.size(); i++) {
            try {
                checkboxes.get(i).click();
            } catch (Exception e) {
                System.err.println("Failed to click checkbox: " + e.getMessage());
            }
        }

        clickElementIfExists(OK_BUTTON, "OK (Fan Selection)");
    }

    /**
     * Adjusts the time picker seek bar randomly.
     */
    private void adjustSeekBar() {
        Dimension size = driver.manage().window().getSize();
        int startY = (int) (size.getHeight() * 0.635);
        int startX = (int) (size.getWidth() * 0.21);
        int endX = (int) (size.getWidth() * (0.22 + random.nextDouble() * 0.78));

        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(finger, 1)
                .addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY))
                .addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()))
                .addAction(new Pause(finger, Duration.ofMillis(150)))
                .addAction(finger.createPointerMove(Duration.ofMillis(250), PointerInput.Origin.viewport(), endX, startY))
                .addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Collections.singletonList(sequence));
        logpoint("Adjusted seek bar");
    }

    /**
     * Selects a random action from list.
     */
    private void selectRandomAction() {
        clickElementIfExists(By.xpath("//android.widget.Button[@content-desc='Power ON']"), "Power ON");
        List<WebElement> actions = driver.findElements(AppiumBy.className("android.view.View"));
        if (!actions.isEmpty()) {
            int index = random.nextInt(actions.size());
            try {
                actions.get(index).click();
            } catch (Exception e) {
                System.err.println("Failed to select random action: " + e.getMessage());
            }
        }
    }

    /**
     * Checks if success message is displayed.
     */
    private boolean isSuccessMessageDisplayed(String message) {
        try {
            By locator = By.xpath("//android.view.View[@content-desc='" + message + "']");
            return isElementPresent(locator);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Deletes all Time-of-Day automations.
     */
    public void deleteTimeOfDay() {
        navigateToAutomations();
        clickElementIfExists(TIME_OF_DAY_SECTION, "Time of Day");

        while (true) {
            List<WebElement> automations = getAutomationListStartingWith("Automation");
            if (automations.isEmpty()) break;

            automations.get(0).click();
            confirmDelete();
            ActionsUtil.sleep(1000);
        }
        logpoint("All Time-of-Day automations deleted.");
    }

    /**
     * Deletes all Quick Access automations.
     */
    public void deleteQuickAccess() {
        navigateToAutomations();
        clickElementIfExists(QUICK_ACCESS_SECTION, "Quick Access");

        while (true) {
            List<WebElement> quickAccessItems = getAutomationListStartingWith("QA");
            if (quickAccessItems.isEmpty()) break;

            quickAccessItems.get(0).click();
            confirmDelete();
            ActionsUtil.sleep(1000);
        }
        logpoint("All Quick Access automations deleted.");
    }

    /**
     * Gets list of automation items starting with given prefix.
     */
    private List<WebElement> getAutomationListStartingWith(String prefix) {
        return driver.findElements(AppiumBy.className("android.widget.ImageView")).stream()
                .filter(el -> {
                    String desc = getAttribute(el, "content-desc");
                    return desc != null && desc.startsWith(prefix);
                })
                .collect(Collectors.toList());
    }

    /**
     * Confirms deletion of an automation.
     */
    private void confirmDelete() {
        clickElementIfExists(By.xpath("(//android.widget.Button)[1]"), "Delete Button");
        clickElementIfExists(DELETE_CONFIRM_BUTTON, "Yes (Confirm Delete)");

        if (isSuccessMessageDisplayed(SUCCESS_DELETE_MESSAGE)) {
            logpoint("Automation deleted successfully.");
        } else {
            System.err.println("Deletion may have failed.");
        }
    }

    // === Utility Methods ===

    /**
     * Safely clicks element if present and displayed.
     */
    private boolean clickElementIfExists(By locator, String label) {
        try {
            WebElement el = driver.findElement(locator);
            if (el.isDisplayed()) {
                el.click();
                logpoint("Clicked: " + label);
                return true;
            } else {
                logpoint(label + " found but not displayed.");
                return false;
            }
        } catch (NoSuchElementException e) {
            logpoint(label + " not found.");
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking " + label + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Waits for element to be present.
     */
    private WebElement waitForElement(By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                return driver.findElement(locator);
            } catch (NoSuchElementException ignored) {
                ActionsUtil.sleep(500);
            }
        }
        return null;
    }

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
     * Gets attribute safely.
     */
    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return null;
        }
    }
}
