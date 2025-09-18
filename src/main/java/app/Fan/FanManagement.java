package app.Fan;

import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.AppUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * FanManagement - Manages fan-related operations in the Atomberg app.
 *
 * Refactored to improve code clarity, reduce duplication, enhance error handling,
 * and follow test automation best practices (e.g., explicit waits, POM-like structure).
 */
public class FanManagement {
    private final AndroidDriver atomberg;
    private final WebDriverWait wait; // For explicit waits

    public FanManagement(AndroidDriver driver) {
        this.atomberg = driver;
        // Initialize WebDriverWait with a 10-second timeout
        this.wait = new WebDriverWait(atomberg, Duration.ofSeconds(10));
    }

    /**
     * Adds a new fan by navigating to the add screen and attempting connection.
     * Retries scanning up to 10 times if no device is found.
     */
    public void addFan() {
        navigateToAddScreen();

        System.out.println("Searching for available devices...");

        for (int attempt = 0; attempt < 10; attempt++) {
            sleep(15); // Wait for scan results

            WebElement fanElement = findOptionalElement(FAN_DISCOVERY_XPATH);
            if (fanElement != null) {
                System.out.println("Atomberg Smart Fan detected.");
                List<WebElement> connectButtons = atomberg.findElements(CONNECT_BUTTON);

                for (int i = 1; i <= connectButtons.size(); i++) {
                    WebElement connectButton = atomberg.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
                    System.out.println("Clicking Connect button at index: " + i);
                    connectButton.click();

                    // Check for common error modals after clicking Connect
                    if (handleConnectionErrors()) {
                        continue; // Retry next connect option
                    } else {
                        System.out.println("Successfully connected or proceeding...");
                        break; // Exit loop on success
                    }
                }
                break; // Exit outer loop when fan is found and processed
            } else {
                handleNoDeviceFound();
            }
        }
    }

    // --- Helper Methods for addFan ---

    /**
     * Navigates to the 'Add Device' screen using either UI element or fallback tap.
     */
    private void navigateToAddScreen() {
        try {
            WebElement addButton = wait.until(ExpectedConditions.presenceOfElementLocated(ADD_BUTTON_XPATH));
            addButton.click();
        } catch (TimeoutException e) {
            System.out.println("Add button not found via XPath, using coordinate fallback.");
            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1850); // Fallback tap
        }
        sleep(3);
    }

    /**
     * Handles known error states post-connection attempt.
     *
     * @return true if an error was handled (retry needed), false if successful
     */
    private boolean handleConnectionErrors() {
        try {
            if (isElementPresent(CONNECTING_TO_LOCK_MODAL)) {
                atomberg.navigate().back();
                System.out.println("Back: Connecting to Lock modal appeared.");
                return true;
            }
            if (isElementPresent(COULD_NOT_ADD_LOCK)) {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                System.out.println("Cancel clicked: Could not add lock.");
                sleep(1);
                return true;
            }
            if (isElementPresent(DEVICE_ALREADY_PAIRED)) {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                System.out.println("Cancel clicked: Device already paired.");
                sleep(1);
                return true;
            }
            if (isElementPresent(COULD_NOT_REACH_DEVICE)) {
                System.out.println("Out of reach.");
                atomberg.navigate().back();
                atomberg.navigate().back();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error checking connection status: " + e.getMessage());
        }
        return false; // No errors found — assume success
    }

    /**
     * Handles case when no device is found during scan.
     */
    private void handleNoDeviceFound() {
        try {
            // If "Connect" exists twice, likely means some device is visible
            List<WebElement> connects = atomberg.findElements(CONNECT_BUTTON);
            if (connects.size() >= 2) {
                atomberg.findElement(By.xpath("//android.widget.Button")).click(); // Close dialog?
            } else {
                WebElement tryAgain = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
                tryAgain.click();
                System.out.println("Trying again...");
            }
        } catch (NoSuchElementException e) {
            System.out.println("Neither 'Connect' nor 'Try Again' found: " + e.getMessage());
        }
    }

    // --- End of addFan helpers ---

    /**
     * Completes the addition process: selects model, clicks Next, chooses room.
     */
    public void additionProcess() {
        Select.Fan(atomberg);
        WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(NEXT_BUTTON));
        System.out.println("Next");
        nextBtn.click();
        AppUtil.captureScreenshot(atomberg);
        AppUtil.additionProcess(atomberg); // Select room
    }

    /**
     * Performs basic fan control actions in sequence: Speeds 1–5 → Boost → Power.
     */
    public void fanControl() {
        performBasicFanActions();
    }

    /**
     * Executes random fan commands for a given number of iterations.
     *
     * @param iteration Number of random commands to execute
     */
    public void randomFanCommands(int iteration) {
        Random random = new Random();
        setImplicitWait(Duration.ZERO); // Disable implicit wait during rapid commands

        for (int i = 0; i < iteration; i++) {
            int command = random.nextInt(7);
            switch (command) {
                case 0 -> clickAndWait(SPEED_1, "Speed1");
                case 1 -> clickAndWait(SPEED_2, "Speed2");
                case 2 -> clickAndWait(SPEED_3, "Speed3");
                case 3 -> clickAndWait(SPEED_4, "Speed4");
                case 4 -> clickAndWait(SPEED_5, "Speed5");
                case 5 -> clickAndWait(BOOST_BUTTON, "Boost");
                case 6 -> clickAndWait(POWER_BUTTON, "Power");
            }
        }

        setImplicitWait(Duration.ofSeconds(5)); // Restore default
    }

    /**
     * Repeats full command cycle (1→5, Boost, Power) multiple times.
     *
     * @param iteration Number of full cycles
     */
    public void repeatCommands(int iteration) {
        setImplicitWait(Duration.ZERO);
        for (int i = 0; i < iteration; i++) {
            clickElement(SPEED_1); clickElement(SPEED_2); clickElement(SPEED_3);
            clickElement(SPEED_4); clickElement(SPEED_5); clickElement(BOOST_BUTTON);
            clickElement(POWER_BUTTON);
        }
        setImplicitWait(Duration.ofSeconds(5));
    }

    /**
     * Checks if any fans are online and controls them.
     * Scrolls if more than 4 fans exist.
     */
    public void checkFanOnline() {
        try {
            WebElement fansTab = wait.until(ExpectedConditions.elementToBeClickable(FANS_TAB));
            fansTab.click();
            sleep(3);

            WebElement buyNow = findOptionalElement(BUY_NOW_BUTTON);
            if (buyNow != null) {
                System.out.println("No fans added yet.");
                return;
            }

            List<WebElement> fanButtons = getVisibleFanList();
            System.out.println("Fans detected: " + fanButtons.size());

            if (fanButtons.isEmpty()) {
                System.out.println("No fan online.");
                return;
            }

            // Control first set of visible fans
            controlMultipleFans(fanButtons);

            // Handle scrolling if there might be more fans
            if (fanButtons.size() >= 4) {
                scrollAndProcessRemainingFans(fanButtons);
            }

        } catch (Exception e) {
            System.out.println("Error during fan online check: " + e.getMessage());
        } finally {
            sleep(1);
        }
    }

    /**
     * Gets current list of fan buttons excluding non-fan elements.
     *
     * @return List of valid fan web elements
     */
    private List<WebElement> getVisibleFanList() {
        List<WebElement> allButtons = atomberg.findElements(By.className("android.widget.Button"));
        return allButtons.stream()
                .map(el -> {
                    String desc = el.getDomAttribute("content-desc");
                    return desc != null && !desc.startsWith("Analytics") && !desc.equals("null") ? el : null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * Controls a list of fans by opening each and sending commands.
     *
     * @param fans List of fan elements to control
     */
    private void controlMultipleFans(List<WebElement> fans) {
        for (WebElement fan : fans) {
            String name = fan.getDomAttribute("content-desc");
            System.out.println("Controlling fan: " + name);
            fan.click();
            fanControl();
            atomberg.navigate().back();
        }
    }

    /**
     * Scrolls up to find additional fans beyond initial view.
     *
     * @param initialFans Previously visible fans
     */
    private void scrollAndProcessRemainingFans(List<WebElement> initialFans) {
        String lastFanName = initialFans.get(initialFans.size() - 1).getDomAttribute("content-desc");

        ActionsUtil.Scroll.Up(atomberg);
        ActionsUtil.Scroll.Up(atomberg);

        List<WebElement> newFans = getVisibleFanList();

        if (newFans.isEmpty()) {
            System.out.println("No additional fans after scroll.");
            return;
        }

        String newLastFan = newFans.get(newFans.size() - 1).getDomAttribute("content-desc");
        if (Objects.equals(newLastFan, lastFanName)) {
            System.out.println("No more devices beyond current list.");
            return;
        }

        // Remove duplicates due to overlap
        int overlapCount = 0;
        String secondLastInitial = initialFans.get(initialFans.size() - 2).getDomAttribute("content-desc");
        for (WebElement fan : newFans) {
            String n = fan.getDomAttribute("content-desc");
            if (n.equals(secondLastInitial) || n.equals(lastFanName)) overlapCount++;
        }

        if (overlapCount > 0) {
            newFans.subList(0, overlapCount).clear();
            System.out.println("Removed overlapping fans. Remaining: " + newFans.size());
        }

        controlMultipleFans(newFans);
    }

    /**
     * Simulates LED tap via coordinates (assumed hardware action).
     */
    public void LED() {
        ActionsUtil.Tap.withCoordinates(atomberg, 540, 1800);
        ActionsUtil.SSleep(10);
    }

    /**
     * Inspects child elements of each device button for clickable components.
     */
    public void deviceChildElements() {
        List<WebElement> devices = getVisibleFanList();
        if (devices.isEmpty()) {
            System.out.println("No devices found.");
            return;
        }

        // Remove last element assumed to be non-device (e.g., Add button)
        devices.remove(devices.size() - 1);
        System.out.println("Processing " + devices.size() + " devices.");

        for (int i = 0; i < devices.size(); i++) {
            if (i == 5) {
                ActionsUtil.Scroll.Up(atomberg);
                ActionsUtil.Scroll.Up(atomberg);
            }

            WebElement device = devices.get(i);
            String name = device.getDomAttribute("content-desc");
            System.out.println("Inspecting children of: " + name);

            List<WebElement> children = device.findElements(By.xpath(".//*"));
            List<WebElement> clickableChildren = children.stream()
                    .filter(el -> "true".equals(el.getDomAttribute("clickable")))
                    .collect(Collectors.toList());

            System.out.println("Clickable children: " + clickableChildren.size());
            for (WebElement child : clickableChildren) {
                String childDesc = child.getDomAttribute("content-desc");
                System.out.println("Clicking child: " + childDesc);
                child.click();
                ActionsUtil.SSleep(3);
                atomberg.navigate().back();
            }
        }
    }

    /**
     * Entry point: checks home screen and runs fan check if family not empty.
     */
    public void checkFan() {
        ScreenCheck screen = new ScreenCheck(atomberg);
        screen.homeScreen();

        WebElement emptyFamily = findOptionalElement(ADD_FIRST_DEVICE_ICON);
        if (emptyFamily == null) {
            checkFanOnline();
        } else {
            System.out.println("No smart devices added yet.");
        }
    }

    // === Utility Methods ===

    /**
     * Finds element without throwing exception. Returns null if not found.
     *
     * @param locator Locator strategy
     * @return WebElement or null
     */
    private WebElement findOptionalElement(By locator) {
        try {
            return atomberg.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Checks if element is present (without failing).
     *
     * @param locator Locator to check
     * @return true if present
     */
    private boolean isElementPresent(By locator) {
        return findOptionalElement(locator) != null;
    }

    /**
     * Clicks element with optional log message.
     *
     * @param locator By locator
     */
    private void clickElement(By locator) {
        try {
            atomberg.findElement(locator).click();
        } catch (Exception e) {
            System.out.println("Failed to click element: " + locator.toString());
        }
    }

    /**
     * Clicks element and logs action.
     *
     * @param locator By locator
     * @param actionName Description of action
     */
    private void clickAndWait(By locator, String actionName) {
        clickElement(locator);
        System.out.println(actionName);
    }

    /**
     * Sets implicit wait globally.
     *
     * @param duration Duration to set
     */
    private void setImplicitWait(Duration duration) {
        atomberg.manage().timeouts().implicitlyWait(duration);
    }

    /**
     * Sleep wrapper (seconds).
     *
     * @param seconds Seconds to sleep
     */
    private void sleep(int seconds) {
        ActionsUtil.sleep(seconds * 1000L);
    }

    // === Static Inner Class: Model Selection ===
    public static class Select {
        public static void Fan(AndroidDriver atomberg) {
            if (isElementPresent(atomberg, "//android.view.View[@content-desc=\"Pick the fan model you're having\"]")) {
                SixLED(atomberg);
            } else {
                handleOtherModels(atomberg);
            }
        }

        private static boolean isElementPresent(AndroidDriver driver, String xpath) {
            try {
                driver.findElement(By.xpath(xpath));
                return true;
            } catch (NoSuchElementException e) {
                return false;
            }
        }

        private static void handleOtherModels(AndroidDriver atomberg) {
            if (isElementPresent(atomberg, "//android.view.View[@content-desc=\"Select your device color\"]")) {
                FanModels models = new FanModels(atomberg);
                if (isElementPresent(atomberg, "//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]")) {
                    models.Aris();
                } else if (isElementPresent(atomberg, "//android.widget.ImageView[@content-desc=\"Regent Gray\"]")) {
                    models.Jaguar();
                } else {
                    models.Erica();
                }
                AppUtil.captureScreenshot(atomberg);
            } else {
                SixLED(atomberg);
            }
        }

        public static void SixLED(AndroidDriver atomberg) {
            int choice = new Random().nextInt(3);
            By[] options = {RENESA, STUDIO_PLUS, RENESA_PLUS};
            String[] labels = {"Renesa", "Studio+", "Renesa+"};

            try {
                WebElement model = atomberg.findElement(options[choice]);
                model.click();
                System.out.println(labels[choice] + " Selected");
                AppUtil.captureScreenshot(atomberg);
            } catch (Exception e) {
                System.out.println("Model selection failed: " + e.getMessage());
            }

            clickWhenReady(atomberg, CONTINUE_BUTTON);
            new FanModels(atomberg).SixLEDColorSelect();
        }

        private static void clickWhenReady(AndroidDriver driver, By locator) {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(locator)).click();
        }
    }

    // === Locator Constants (Centralized) ===
    private static final By ADD_BUTTON_XPATH = By.xpath(
            "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"
    );
    private static final By FAN_DISCOVERY_XPATH = By.xpath("//android.view.View[@content-desc=\"Atomberg Smart Fan\"]");
    private static final By CONNECT_BUTTON = By.xpath("//android.view.View[@content-desc=\"Connect\"]");
    private static final By NEXT_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Next\"]");
    private static final By FANS_TAB = By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]");
    private static final By BUY_NOW_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Buy Now!\"]");
    private static final By ADD_FIRST_DEVICE_ICON = By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]");

    // Fan Control Buttons
    private static final By SPEED_1 = By.xpath("//android.widget.Button[@content-desc=\"1\"]");
    private static final By SPEED_2 = By.xpath("//android.widget.Button[@content-desc=\"2\"]");
    private static final By SPEED_3 = By.xpath("//android.widget.Button[@content-desc=\"3\"]");
    private static final By SPEED_4 = By.xpath("//android.widget.Button[@content-desc=\"4\"]");
    private static final By SPEED_5 = By.xpath("//android.widget.Button[@content-desc=\"5\"]");
    private static final By BOOST_BUTTON = By.xpath(
            "(//android.view.View[@content-desc=\"Boost\"])[1]" // Prefer content-desc over deep hierarchy
    );
    private static final By POWER_BUTTON = By.xpath(
            "(//android.view.View[@content-desc=\"Power\"])[1]"
    );

    // Modal Messages
    private static final By CONNECTING_TO_LOCK_MODAL = By.xpath("//android.view.View[contains(@content-desc, 'Connecting to the Lock')]");
    private static final By COULD_NOT_ADD_LOCK = By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]");
    private static final By DEVICE_ALREADY_PAIRED = By.xpath("//android.view.View[@content-desc=\"Device already paired\"]");
    private static final By COULD_NOT_REACH_DEVICE = By.xpath("//android.view.View[contains(@content-desc, 'Could not reach')]");

    // Model Selection
    private static final By RENESA = By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]");
    private static final By STUDIO_PLUS = By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]");
    private static final By RENESA_PLUS = By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]");
    private static final By CONTINUE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Continue\"]");

    // === Reusable Action ===
    private void performBasicFanActions() {
        By[] actions = {SPEED_1, SPEED_2, SPEED_3, SPEED_4, SPEED_5, BOOST_BUTTON, POWER_BUTTON};
        String[] labels = {"Speed1", "Speed2", "Speed3", "Speed4", "Speed5", "Boost", "Power"};

        for (int i = 0; i < actions.length; i++) {
            clickAndWait(actions[i], labels[i]);
        }
    }
}