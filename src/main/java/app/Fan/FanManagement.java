package app.Fan;

import app.ScreenCheck.ScreenCheck;
import app.SmartDevice;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import static app.util.AppUtil.*;
import static app.resources.Locators.FanLocators.*;

/**
 * FanManagement - Manages fan-related operations in the Atomberg app.
 *
 */

public class FanManagement implements SmartDevice {
    private static AndroidDriver atomberg;
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
    public void addition() {
        navigateToAddScreen(atomberg);

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

    private Point getCenter(WebElement element) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(Objects.requireNonNull(element.getDomAttribute("bounds")));

        // Use a loop to find each number and store it.
        int[] coords = new int[4];
        int i = 0;
        while (matcher.find()) {
            coords[i] = Integer.parseInt(matcher.group());
            i++;
        }

        // Assign the extracted values to more readable variables
        int x1 = coords[0];
        int y1 = coords[1];
        int x2 = coords[2];
        int y2 = coords[3];

        // Calculate the center
        int centerX = (x1 + x2) / 2;
        int centerY = (y1 + y2) / 2;
        Point center = new Point(centerX, centerY);
        System.out.println("Center coordinates: (" + centerX + ", " + centerY + ")");
        return center;
    }

    /**
     * Handles step to add new device, in this case Smart Fan.
     * Searches for "Atomberg Smart Fan", then clicks on "Connect" button besides it
     * Uses Coordinates to locate Connect button.
     */
    public void addition(By deviceLocator) {
        navigateToAddScreen(atomberg);

        System.out.println("🔍 Searching for available devices...");

        final int MAX_SCAN_ATTEMPTS = 10;
        final int SCAN_INTERVAL_MS = 3000; // 3 seconds between scans

        for (int attempt = 1; attempt <= MAX_SCAN_ATTEMPTS; attempt++) {
            System.out.println("🔄 Scan attempt #" + attempt + " of " + MAX_SCAN_ATTEMPTS);

            // Wait for scan results to populate
            sleep(SCAN_INTERVAL_MS);

            try {
                // Check if fan device is visible in scan results
                WebElement fanElement = atomberg.findElement(deviceLocator);

                System.out.println("✅ Atomberg Smart Fan detected");

                // Click the connect button directly (more reliable than coordinates)
                WebElement connectButton = atomberg.findElement(CONNECT_BUTTON);
                connectButton.click();

                System.out.println("🖱️ Tapped Connect button");

                // Wait for connection process to start
                sleep(2000);
                return; // Successfully initiated connection

            } catch (NoSuchElementException e) {
                System.out.println("⚠️ Fan not found in scan results (attempt " + attempt + ")");

                // Only attempt to handle no device on final attempt
                if (attempt == MAX_SCAN_ATTEMPTS) {
                    System.out.println("❌ No devices found after " + MAX_SCAN_ATTEMPTS + " attempts");
                    handleNoDeviceFound();
                    throw new DeviceNotFoundException("No Atomberg devices detected during BLE scan");
                }
            } catch (Exception e) {
                System.out.println("⚠️ Unexpected error during scan: " + e.getMessage());
                // Consider adding specific error handling for different exception types
            }
        }
    }

    public static class DeviceNotFoundException extends RuntimeException {
        public DeviceNotFoundException(String message) {
            super(message);
        }
    }

    public void deletion(){}

    public void control(){}

    /**
     * Handles known error states post-connection attempt.
     *
     * @return true if an error was handled (retry needed), false if successful
     */
    private boolean handleConnectionErrors() {
        try {
            if (isElementPresent(atomberg, CONNECTING_TO_LOCK_MODAL)) {
                atomberg.navigate().back();
                System.out.println("Back: Connecting to Lock modal appeared.");
                return true;
            }
            if (isElementPresent(atomberg, COULD_NOT_ADD_LOCK)) {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                System.out.println("Cancel clicked: Could not add lock.");
                sleep(1);
                return true;
            }
            if (isElementPresent(atomberg, DEVICE_ALREADY_PAIRED)) {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                System.out.println("Cancel clicked: Device already paired.");
                sleep(1);
                return true;
            }
            if (isElementPresent(atomberg, COULD_NOT_REACH_DEVICE)) {
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
        captureScreenshot(atomberg, "Addition Process");
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

     */
    private WebElement findOptionalElement(By locator) {
        try {
            return atomberg.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
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
            if (isElementPresent(atomberg, SELECT_FAN_MODEL )) {
                SixLED(atomberg);
            } else {
                handleOtherModels(atomberg);
            }
        }

        public void manageFanDevice() {
            FanManagement fan = new FanManagement(atomberg);

            fan.addition();
            fan.additionProcess();
            fan.checkFan();
            fan.fanControl();

            System.out.println("Fan operations completed.");
        }


        private static void handleOtherModels(AndroidDriver atomberg) {
            if (isElementPresent(atomberg, SELECT_COLOR)) {
                FanModels models = new FanModels(atomberg);
                if (isElementPresent(atomberg, DARK_TEAKWOOD )) {
                    models.Aris();
                } else if (isElementPresent(atomberg,REGENT_GREY)) {
                    models.Jaguar();
                } else {
                    models.Erica();
                }
                captureScreenshot(atomberg, "Fan Model Selection");
            } else {
                SixLED(atomberg);
            }
        }

        public static void SixLED(AndroidDriver atomberg) {
            int choice = new Random().nextInt(3);
            By[] options = {RENESA, STUDIO_PLUS, RENESA_PLUS, RENESA_HALO, LOAD_MORE};
            String[] labels = {"Renesa", "Studio+", "Renesa+", "Renesa Halo", "Load More"};

            try {
                WebElement model = atomberg.findElement(options[choice]);
                model.click();
                System.out.println(labels[choice] + " Selected");
                captureScreenshot(atomberg, labels[choice]);
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

    // === Reusable Action ===
    private void performBasicFanActions() {
        By[] actions = {SPEED_1, SPEED_2, SPEED_3, SPEED_4, SPEED_5, BOOST_BUTTON, POWER_BUTTON};
        String[] labels = {"Speed1", "Speed2", "Speed3", "Speed4", "Speed5", "Boost", "Power"};

        for (int i = 0; i < actions.length; i++) {
            clickAndWait(actions[i], labels[i]);
        }
    }
}