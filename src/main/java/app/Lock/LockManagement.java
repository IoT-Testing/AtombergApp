package app.Lock;

import app.SmartDevice;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import java.util.List;
import java.util.stream.Collectors;
import static app.resources.Locators.Android.DeviceScreens.LockLocators.*;
import static app.resources.Messages.*;
import static app.util.AppUtil.navigateToAddScreen;

//TODO :
/**
 *  1. Link Unlink 20 cycles in account mode
 *  2. 20 Power on-off cycles, while OTA(Account)
 *  3. 20 Power on-off cycles, while OTA(Guest)
 *  4. 20 Bluetooth on-off cycles, while OTA(Account)
 *  5. 20 Bluetooth on-off cycles, while OTA(Guest)
 */

/**
 * LockManagement - End-to-end automation for Atomberg Smart Lock setup and control.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Eliminate duplication</li>
 *   <li>Improve error visibility</li>
 *   <li>Centralize locators</li>
 *   <li>Break down monolithic methods</li>
 *   <li>Follow clean coding principles</li>
 * </ul>
 */
public class LockManagement implements SmartDevice {
    private final AndroidDriver atomberg;

    // === Locator Constants ===


    // Messages


    public LockManagement(AndroidDriver driver) {
        this.atomberg = driver;
    }

    /**
     * Discovers and adds a new smart lock.
     */
    public void addition() {
        navigateToAddScreen(atomberg);
        System.out.println("Searching for available devices...");
        ActionsUtil.sleep(15000); // Allow scan

        if (isElementPresent(SMART_LOCK_INDICATOR)) {
            clickWhenReady(atomberg, CONNECT_BUTTON);
            System.out.println("Connection initiated.");
        } else {
            System.out.println("No smart lock detected.");
        }
    }

//    private void manageLockDevice(){
//        LockManagement lock = new LockManagement(atomberg);
//        lock.addition();
//    }

    public void deletion(){}


    /**
     * Completes PIN setup for newly added lock.
     */
    public void additionProcess() {
        // Enter random 6-digit PIN
        for (int i = 1; i <= 6; i++) {
            By pinField = By.xpath("(//android.widget.EditText)[" + i + "]");
            WebElement field = waitForElement(pinField, 10);
            field.sendKeys(String.valueOf((int) (Math.random() * 9)));
        }

        clickWhenReady(atomberg, By.xpath("//android.widget.Button[@content-desc='Save']"));
        ActionsUtil.sleep(5000);

        if (isElementPresent(SUCCESS_MESSAGE)) {
            System.out.println("Lock Added Successfully");
            ActionsUtil.sleep(1500);
        } else {
            System.err.println("Lock addition failed: Success message not shown.");
        }
        ActionsUtil.sleep(3000);
    }

    /**
     * Checks if any locks are online and controls them.
     */
    public void checkLock() {
        clickWhenReady(atomberg, LOCK_TAB);
        List<WebElement> locks = atomberg.findElements(By.className("android.widget.Button"));

        List<WebElement> validLocks = locks.stream()
                .filter(el -> {
                    el.findElement(By.tagName("ImageView"));
                    return true;
                }).collect(Collectors.toList());

        if (validLocks.isEmpty()) {
            System.out.println("No Lock Available");
            return;
        }

        System.out.println(validLocks.size() + " Lock(s) Available");

        for (WebElement lock : validLocks) {
            lock.click();
            control();
            atomberg.navigate().back();
        }
    }

    /**
     * Controls an individual lock: unlock, view history, access keys, settings.
     */
    public void control() {
        ActionsUtil.sleep(7500);
        clickWhenReady(atomberg, UNLOCK_HANDLE);
        System.out.println("Unlocking...");

        ActionsUtil.sleep(3000);

        if (isElementPresent(By.xpath("//android.view.View[@content-desc='Unlocked']"))) {
            System.out.println("Successfully unlocked");
            history();
            ActionsUtil.sleep(5000);
            atomberg.navigate().back();

            AccessKeys();
            lockSettings();
            atomberg.navigate().back();
        } else if (isElementPresent(By.xpath("//android.view.View[@content-desc='Could not unlock']"))) {
            System.out.println("Lock not available or Bluetooth off");
        }
    }

    /**
     * Opens lock history.
     */
    private void history() {
        if (isElementPresent(HISTORY_BUTTON)) {
            clickWhenReady(atomberg, HISTORY_BUTTON);
        }
    }

    /**
     * Navigates into Access Keys section and inspects types.
     */
    private void AccessKeys() {
        clickWhenReady(atomberg, ACCESS_KEYS_BUTTON);
        KeyType();
    }

    /**
     * Inspects all types of remote keys (OTP, Timed Pin).
     */
    private void KeyType() {
        List<WebElement> keyButtons = atomberg.findElements(By.className("android.widget.Button"));
        int total = keyButtons.size();

        for (int i = 0; i < total; i++) {
            List<WebElement> currentKeys = atomberg.findElements(By.className("android.widget.Button"));
            WebElement key = currentKeys.get(i);

            String desc = key.getDomAttribute("content-desc");
            boolean isOTP = desc != null && desc.startsWith("Remote OTP");
            boolean isPin = desc != null && desc.startsWith("Remote Timed Pin");

            key.click();

            if (isOTP) {
                ActionsUtil.sleep(5000);
                List<WebElement> otpList = getVisibleLabeledElements();
                otpList.remove(otpList.size() - 1); // Remove last (non-data) element
                printContentDesc(otpList, "Remote OTP");
            }

            if (isPin) {
                ActionsUtil.sleep(5000);
                List<WebElement> pinList = getVisibleLabeledElements();
                for (WebElement e : pinList) {
                    String content = e.getDomAttribute("content-desc");
                    if (content != null && content.startsWith("End")) {
                        e.click();
                        periodicTimedPin();
                        break;
                    }
                }
            }

            atomberg.navigate().back();

            // Reopen Access Keys unless it's the last item
            if (i < total - 1) {
                clickWhenReady(atomberg, ACCESS_KEYS_BUTTON);
            }
        }
    }

    /**
     * Navigates to lock settings and manages preferences.
     */
    private void lockSettings() {
        clickWhenReady(atomberg, SETTINGS_BUTTON);
        ActionsUtil.sleep(1000);

        clickWhenReady(atomberg, USERS_BUTTON);
        ActionsUtil.sleep(1000);
        atomberg.navigate().back();

        preferences();
    }

    /**
     * Toggles various preference switches: silent mode, passage mode, etc.
     */
    private void preferences() {
        if (!isElementPresent(PREFERENCES_SECTION)) {
            System.out.println("Preferences section not found.");
            return;
        }

        List<WebElement> switches = atomberg.findElements(By.className("android.widget.Switch"));
        if (switches.size() < 5) {
            System.err.println("Expected at least 5 switches in Preferences.");
            return;
        }

        // Toggle Silent Mode
        toggleSwitchAndConfirm(switches.get(SILENT_MODE_SWITCH_INDEX), "Silent mode", this::silentMode);

        // Toggle Passage Mode
        boolean wasEnabled = "true".equals(switches.get(PASSAGE_MODE_SWITCH_INDEX).getDomAttribute("checked"));
        switches.get(PASSAGE_MODE_SWITCH_INDEX).click();
        ActionsUtil.SSleep(2);
        passageMode(wasEnabled);

        // Toggle Fingerprint
        switches.get(FINGERPRINT_SWITCH_INDEX).click();
        System.out.println("Fingerprint toggled");
        fingerprint();

        // Toggle Card
        switches.get(CARD_SWITCH_INDEX).click();
        System.out.println("Card toggled");
        CardEnable();

        // Toggle All Pins
        WebElement pinsSwitch = switches.get(PINS_SWITCH_INDEX);
        System.out.println(pinsSwitch.getDomAttribute("checked").equals("true") ?
                "Disable all pins" : "Enable all pins");
        pinsSwitch.click();
    }

    /**
     * Handles confirmation dialog for silent mode.
     */
    private void silentMode() {
        if (isElementPresent(By.xpath("//android.view.View[@content-desc='Do you want to enable silent mode?']")) ||
                isElementPresent(By.xpath("//android.view.View[@content-desc='Do you want to disable silent mode?']"))) {
            clickWhenReady(atomberg, YES_BUTTON);
        }
        ActionsUtil.SSleep(5);
    }

    /**
     * Handles passage mode activation/deactivation with confirmations.
     *
     * @param wasEnabled true if already enabled (so now disabling)
     */
    private void passageMode(boolean wasEnabled) {
        if (wasEnabled) {
            // Already enabled → now disabling → no prompts
            return;
        }

        // Enabling passage mode → show prompts
        if (isElementPresent(PASSAGE_MODE_PROMPT_1)) {
            clickWhenReady(atomberg, YES_BUTTON);
        }
        if (isElementPresent(PASSAGE_MODE_PROMPT_2)) {
            clickWhenReady(atomberg, YES_BUTTON);
        }
        if (isElementPresent(PASSAGE_MODE_SUCCESS)) {
            System.out.println("Passage Mode Enabled Successfully");
        }
    }

    /**
     * Validates fingerprint toggle results.
     */
    private void fingerprint() {
        if (isElementPresent(FP_DISABLED_MSG)) {
            System.out.println("All Fingerprints Disabled Successfully!\nEnabling them again");
            List<WebElement> switches = atomberg.findElements(By.className("android.widget.Switch"));
            if (switches.size() > FINGERPRINT_SWITCH_INDEX) {
                switches.get(FINGERPRINT_SWITCH_INDEX).click();
            }
        } else if (isElementPresent(FP_ENABLED_MSG)) {
            System.out.println("All Fingerprints Enabled Successfully");
        }
    }

    /**
     * Handles card access toggle and status.
     */
    private void CardEnable() {
        if (isElementPresent(CARD_NOT_AVAILABLE)) {
            System.out.println("No Cards present for this Lock. Please add one");
        } else if (isElementPresent(By.xpath("//android.view.View[@content-desc='All Cards Disabled Successfully!']"))) {
            System.out.println("Cards Disabled, Enabling it ...");
            List<WebElement> switches = atomberg.findElements(By.className("android.widget.Switch"));
            if (switches.size() > CARD_SWITCH_INDEX) {
                switches.get(CARD_SWITCH_INDEX).click();
            }
        } else if (isElementPresent(By.xpath("//android.view.View[@content-desc='All Cards Enabled Successfully!']"))) {
            System.out.println("Cards Enabled");
        }
    }

    /**
     * Configures periodic timed PIN duration using seekbar scroll.
     */
    private void periodicTimedPin() {
        List<WebElement> seekBars = atomberg.findElements(By.className("android.widget.SeekBar"));
        List<WebElement> labeledSeekBars = seekBars.stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());

        if (labeledSeekBars.size() >= 2) {
            ActionsUtil.Scroll.element(atomberg, labeledSeekBars.get(1));
        }

        ActionsUtil.SSleep(2);
        ActionsUtil.Tap.element(atomberg, findRequiredElement(DONE_BUTTON));
        ActionsUtil.SSleep(1);
        WebElement update = findRequiredElement(UPDATE_BUTTON);
        assert update.isDisplayed();
        update.click();

        pin(); // View PIN list
    }

    /**
     * Displays configured PINs.
     */
    private void pin() {
        ActionsUtil.sleep(5000);
        List<WebElement> views = getVisibleLabeledElements();
        views.remove(views.size() - 1); // Remove footer/button
        printContentDesc(views, "PIN");
    }

    // === Utility Methods ===



    /**
     * Safely checks if element is present.
     *
     * @param locator Element locator
     * @return true if displayed
     */
    private boolean isElementPresent(By locator) {
        try {
            return atomberg.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Waits up to N seconds for element to be clickable.
     *
     * @param driver   Driver instance
     * @param locator  Element locator
     * @param timeoutSec Timeout in seconds
     */
    private void clickWhenReady(AndroidDriver driver, By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                driver.findElement(locator).click();
                return;
            } catch (Exception ignored) {
                ActionsUtil.sleep(500);
            }
        }
        throw new RuntimeException("Element not clickable after " + timeoutSec + "s: " + locator);
    }

    /**
     * Shortcut with default 10s timeout.
     */
    private void clickWhenReady(AndroidDriver driver, By locator) {
        clickWhenReady(driver, locator, 10);
    }

    /**
     * Waits and returns required element.
     *
     * @param locator Locator
     * @return Found element
     */
    private WebElement findRequiredElement(By locator) {
        return waitForElement(locator, 10);
    }

    /**
     * Waits for element presence.
     *
     * @param locator    Locator
     * @param timeoutSec Timeout
     * @return WebElement
     */
    private WebElement waitForElement(By locator, long timeoutSec) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < timeoutSec * 1000) {
            try {
                return atomberg.findElement(locator);
            } catch (NoSuchElementException ignored) {
                ActionsUtil.sleep(500);
            }
        }
        throw new RuntimeException("Element not found: " + locator);
    }

    /**
     * Gets all visible elements with non-null content-desc.
     *
     * @return List of labeled elements
     */
    private List<WebElement> getVisibleLabeledElements() {
        return atomberg.findElements(By.className("android.view.View")).stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());
    }

    /**
     * Prints content-desc of all elements with label.
     * @param elements List of web elements
     * @param type     Type name for logs
     */
    private void printContentDesc(List<WebElement> elements, String type) {
        for (WebElement el : elements) {
            String desc = el.getDomAttribute("content-desc");
            if (desc != null) System.out.println(type + ": " + desc);
        }
    }

    /**
     * Toggles a switch and runs post-toggle action.
     *
     * @param switchEl  Switch element
     * @param label     Label for log
     * @param action    Action to run after toggle
     */
    private void toggleSwitchAndConfirm(WebElement switchEl, String label, Runnable action) {
        switchEl.click();
        System.out.println(label);
        ActionsUtil.sleep(1000);
        action.run();
    }
}