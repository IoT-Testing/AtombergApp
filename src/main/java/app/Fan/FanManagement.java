package app.Fan;

import app.ScreenCheck.ScreenCheck;
import app.SmartDevice;
import app.Supports.AtombergFanStatus;
import app.resources.ArduinoRelayControllerModern;
import app.resources.Locators.Android.DeviceAdditionScreen.Phoenix;
import app.resources.Locators.Android.DeviceAdditionScreen.SmartLock;
import app.resources.PythonFileScript;
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
import static app.resources.Locators.Android.HomeLocators.*;
import static app.util.AppUtil.*;
import static app.resources.Locators.Android.DeviceAdditionScreen.Phoenix.*;
import static app.resources.Locators.Android.DeviceScreens.FanLocators.*;

/**
 * FanManagement - Manages fan-related operations in the Atomberg app.
 *
 */

public class  FanManagement implements SmartDevice {
    private static AndroidDriver atomberg;

    private final WebDriverWait wait; // For explicit waits

    public FanManagement(AndroidDriver driver) {
        atomberg = driver;
        // Initialize WebDriverWait with a 10-second timeout
        this.wait = new WebDriverWait(atomberg, Duration.ofSeconds(10));
    }
    /**
     * Adds a new fan by navigating to the add screen and attempting connection.
     * Retries scanning up to 10 times if no device is found.
     */
    @Override
    public void addition() {
        System.out.println("Searching for available devices...");
        for (int attempt = 0; attempt < 5; attempt++) {
            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1940);
            sleep(15); // Wait for scan results
            int previousElementY = 0;
            List<WebElement> fanElements = atomberg.findElements(FAN_DISCOVERY_XPATH);

            List<WebElement> connectButtons = atomberg.findElements(CONNECT_BUTTON);
            if(connectButtons.size() ==1 && !fanElements.isEmpty()){
                atomberg.findElement(CONNECT_BUTTON).click();
                ConnectionError error = handleConnectionErrors();
                if(error != ConnectionError.NO_ERROR) {
                    error.handle(atomberg);
                    continue;
                }
                else if(error.equals(ConnectionError.DEVICE_ALREADY_PAIRED)){
                    PythonFileScript run = new PythonFileScript();
                    run.script();
                }
                break;
            }
            else if (fanElements.size()>1){
                for (WebElement fanElement : fanElements) {
                    int deviceCenterY = getElementY(fanElement); // Y coordinates of "Atomberg Smart Fan"
                    if (previousElementY == deviceCenterY) continue;
                    if (fanElement != null) {
                        System.out.println("Atomberg Smart Fan detected.");
                        connectButtons = atomberg.findElements(CONNECT_BUTTON);
                        for (WebElement connect : connectButtons) {
                            ArduinoRelayControllerModern controller = new ArduinoRelayControllerModern();
                            int connectCenter = getElementY(connect); //Y coordinates of connect button
                            if (deviceCenterY == connectCenter) {
                                connect.click();
                                sleep(2);
                                ConnectionError error = handleConnectionErrors();
                                if(error != ConnectionError.NO_ERROR) {
                                    error.handle(atomberg);
                                    continue;
                                }
                                else if(error.equals(ConnectionError.DEVICE_ALREADY_PAIRED)){
                                    //TODO : Get nRF connect app process complete and add here.
//                                  PythonFileScript run = new PythonFileScript();
//                                  run.script();
                                    controller.sendLEDCommand(true);
                                    ActionsUtil.SSleep(15);
                                }
                                //TODO : Add Model selection.
                                By Model = By.xpath("//android.view.View[@content-desc=\"Model: Aris Gladius\"]");
                                System.out.println("Checking model");
                                if (isElementPresent(atomberg, Model))
                                    break;
                            }
                        }
                    }
                    previousElementY = deviceCenterY;
                    By Model = By.xpath("//android.view.View[@content-desc=\"Model: Aris Gladius\"]");
                    if (isElementPresent(atomberg, Model)) break;
                }
            }
            else if(fanElements.isEmpty())atomberg.navigate().back();
            //Specific for Arid Gladius model. later change to check according to the models
            By Model = By.xpath("//android.view.View[@content-desc=\"Model: Aris Gladius\"]");
            if (isElementPresent(atomberg, Model)) break;
        }
    }
    //    public void addition() {
//        System.out.println("Searching for available devices...");
//        WebDriverWait wait = new WebDriverWait(atomberg, Duration.ofSeconds(15));
//
//        for (int attempt = 0; attempt < 5; attempt++) {
//            ActionsUtil.Tap.withCoordinates(atomberg, 540, 1940);
//
//            try {
//                // Wait for fans or connect button to appear after tap
//                wait.until(driver -> !atomberg.findElements(FAN_DISCOVERY_XPATH).isEmpty()
//                        || !atomberg.findElements(CONNECT_BUTTON).isEmpty());
//
//                List<WebElement> fanElements = atomberg.findElements(FAN_DISCOVERY_XPATH);
//
//                List<WebElement> connectButtons = atomberg.findElements(CONNECT_BUTTON);
//
//                if (connectButtons.size() == 1 && !fanElements.isEmpty()) {
//                    WebElement connectButton = atomberg.findElement(CONNECT_BUTTON);
//                    wait.until(ExpectedConditions.elementToBeClickable(connectButton));
//                    connectButton.click();
//                    break;
//                }
//                else if (fanElements.size() > 1) {
//                    int previousElementY = 0;
//                    for (WebElement fanElement : fanElements) {
//                        int deviceCenterY = getElementY(fanElement);
//                        if (previousElementY == deviceCenterY) continue;
//                        if (fanElement != null) {
//                            System.out.println("Atomberg Smart Fan detected.");
//                            connectButtons = atomberg.findElements(CONNECT_BUTTON);
//                            for (WebElement connect : connectButtons) {
//                                int connectCenter = getElementY(connect);
//                                if (deviceCenterY == connectCenter) {
//                                    wait.until(ExpectedConditions.elementToBeClickable(connect));
//                                    connect.click();
//
//                                    // Instead of fixed sleep, wait dynamically if needed (e.g. wait for model element or error)
//                                    if (handleConnectionErrors()) {
//                                        // Connection error handled, continue to next fan
//                                        continue;
//                                    }
//
//                                    By model = By.xpath("//android.view.View[@content-desc=\"Model: Aris Gladius\"]");
//                                    System.out.println("Checking model");
//                                    if (isElementPresent(atomberg, model)) {
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                        previousElementY = deviceCenterY;
//
//                        By model = By.xpath("//android.view.View[@content-desc=\"Model: Aris Gladius\"]");
//                        if (isElementPresent(atomberg, model)) {
//                            break;
//                        }
//                    }
//                }
//                else if (fanElements.isEmpty()) {
//                    atomberg.navigate().back();
//                    // Optionally wait until back navigation completes or some known element appears
//                    wait.until(ExpectedConditions.presenceOfElementLocated(MORE_TAB));
//                }
//
//                // Specific for Aris Gladius model check
//                By model = By.xpath("//android.view.View[@content-desc=\"Model: Aris Gladius\"]");
//                if (isElementPresent(atomberg, model)) {
//                    break;
//                }
//
//            } catch (Exception e) {
//                System.out.println("Exception during addition attempt: " + e.getMessage());
//            }
//        }
//    }

    /**
     * Handles step to add new device, in this case Smart Fan.
     * Searches for "Atomberg Smart Fan", then clicks on "Connect" button besides it
     * Uses Coordinates to locate Connect button.
     */
    public void addition(By deviceLocator) {
        navigateToAddScreen(atomberg);

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
                int deviceCenter = getElementY(fanElement);
                // Click the connect button directly (more reliable than coordinates)
                WebElement connectButton = atomberg.findElement(CONNECT_BUTTON);

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

    public void deletion(){

    }

    public void control(){
        if (isElementPresent(atomberg, DEVICES)) {
            checkFanOnline();
        }
    }

    /**
     * Handles known error states post-connection attempt.
     *
     */
    public static ConnectionError handleConnectionErrors() {
        try {
            for (ConnectionError error : ConnectionError.values()) {
                if (isElementPresent(atomberg, error.getLocator())) {
                    return error;
                }
            }
            return ConnectionError.NO_ERROR;
        } catch (Exception e) {
            System.out.println("Error checking connection status: " + e.getMessage());
            return ConnectionError.NO_ERROR;
        }
         // No errors found — assume success
    }

    public enum ConnectionError {
        CONNECTING_TO_LOCK_MODAL(SmartLock.CONNECTING_TO_LOCK_MODAL) {
            @Override
            public boolean handle(AndroidDriver driver) {
                driver.navigate().back();
                System.out.println("Back: Connecting to Lock modal appeared.");
                return true;
            }
        },
        COULD_NOT_ADD_LOCK(SmartLock.COULD_NOT_ADD_LOCK) {
            @Override
            public boolean handle(AndroidDriver driver) {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                System.out.println("Cancel clicked: Could not add lock.");
                sleepStatic(1);
                return true;
            }
        },
        DEVICE_ALREADY_PAIRED(Phoenix.DEVICE_ALREADY_PAIRED) {
            @Override
            public boolean handle(AndroidDriver driver) {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                System.out.println("Cancel clicked: Device already paired.");
                sleepStatic(1);
                return true;
            }
        },
        COULD_NOT_REACH_DEVICE(Phoenix.COULD_NOT_REACH_DEVICE) {
            @Override
            public boolean handle(AndroidDriver driver) {
                System.out.println("Out of reach.");
                driver.navigate().back();
                driver.navigate().back();
                return true;
            }
        },
        COULD_NOT_CONNECT_PROPERLY(Phoenix.COULD_NOT_CONNECT_PROPERLY) {
            @Override
            public boolean handle(AndroidDriver driver) {
            backToHome();
            return true;
            }
        },
        OPERATION_FAILED(Phoenix.OPERATION_FAILED) {
            @Override
            public boolean handle(AndroidDriver driver) {
                backToHome();
                return false; // or true if you want to treat this as handled
            }
        },
        NO_ERROR(Phoenix.NO_ERROR) {
            @Override
            public boolean handle(AndroidDriver driver) {
                backToHome();
                return true;
            }
        };

        private final By locator;

        ConnectionError(By locator) {
            this.locator = locator;
        }
        private static void backToHome() {
            int attempts = 0;
            while (!isElementPresent(atomberg,MORE_TAB) && attempts < 5) {
                System.out.println("Navigating back... attempt " + (++attempts));
                atomberg.navigate().back();
                ActionsUtil.sleep(1000);
            }

            if (!isElementPresent(atomberg, MORE_TAB)) {
                System.err.println("Failed to return to 'Home Screen' after 5 back presses.");
            }
        }

        public By getLocator() {
            return locator;
        }

        public abstract boolean handle(AndroidDriver driver);

        // Static helper to sleep without instance
        static void sleepStatic(int seconds) {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
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
    /**
     * Completes the addition process: selects model, clicks Next, chooses room.
     */
    public void additionProcess() {
//        Select.Fan(atomberg);
        clickElementWithRetry(NEXT_BUTTON);
        captureScreenshot(atomberg, "Addition Process");
        AppUtil.additionProcess(atomberg);
        sleep(10);
//        if (isElementPresent(atomberg, SUCCESS_MESSAGE)) System.out.println("Device added successfully");
    }
    /**
     * Performs basic fan control actions in sequence: Speeds 1–5 → Boost → Power.
     */
    public void speedCommands() {
        performBasicFanActions();
    }
    /**
     * Pre-requisites: Fan control screen should be open.
     * Timer One on-off
     * Check for timer needs to be added.
     */
    public void timerOne(){
        clickElement(TIMER_ON);
        assert isElementPresent(atomberg,TIMER_SCREEN);
        clickElement(START_TIMER);
        AtombergFanStatus get = new AtombergFanStatus();
        String timer = get.fanStatus("timer");
        if(timer.equals("1")) System.out.println("Timer of "+timer+" hour successful");
    }
    /**
     * Pre-requisites: Fan control screen should be open.
     * Timer One on-off
     * Check for timer needs to be added.
     */
    public void timerStop(){
        if(isElementPresent(atomberg,START_TIMER)) clickElement(START_TIMER);
        else clickElement(TIMER_ON);
    }
    /**
     * Pre-requisites: Fan control screen should be open.
     * Timer is on-off
     * Check for timer needs to be added.
     */
    public void timerTwo(){
        clickElement(TIMER_ON);
        assert isElementPresent(atomberg,TIMER_SCREEN);
        clickElement(TIMER_TWO);
        assert Objects.equals(getElementBounds(atomberg, TIMER_TWO), "[403,1220][678,1495]");
        clickElement(START_TIMER);
        ActionsUtil.sleep(1000);
        AtombergFanStatus get = new AtombergFanStatus();
        String timer = get.fanStatus("timer");
        if(timer.equals("2")) System.out.println("Timer of "+timer+" hour successful");
    }
    /**
     * Pre-requisites: Fan control screen should be open.
     * Timer is on-off
     * Check for timer needs to be added.
     */
    public void timerThree(){
        clickElement(TIMER_ON);
        assert isElementPresent(atomberg,TIMER_SCREEN);
        clickElement(TIMER_TWO);
        clickElement(TIMER_THREE);
        assert Objects.equals(getElementBounds(atomberg, TIMER_THREE), "[403,1220][678,1495]");
        clickElement(START_TIMER);
        ActionsUtil.sleep(1000);
        AtombergFanStatus get = new AtombergFanStatus();
        String timer = get.fanStatus("timer");
        if(timer.equals("3")) System.out.println("Timer of "+timer+" hour successful");
    }
    /**
     * Pre-requisites: Fan control screen should be open.
     * Timer is on-off
     * Check for timer needs to be added.
     */
    public void timerSix(){
        clickElement(TIMER_ON);
        assert isElementPresent(atomberg,TIMER_SCREEN);
        clickElement(TIMER_TWO);
        clickElement(TIMER_THREE);
        clickElement(TIMER_SIX);
        assert Objects.equals(getElementBounds(atomberg, TIMER_SIX), "[403,1220][678,1495]");
        clickElement(START_TIMER);
        ActionsUtil.sleep(1000);
        AtombergFanStatus get = new AtombergFanStatus();
        String timer = get.fanStatus("timer");
        if(timer.equals("6")) System.out.println("Timer of "+timer+" hour successful");
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
                case 0 -> clickAndWait(SPEED_1);
                case 1 -> clickAndWait(SPEED_2);
                case 2 -> clickAndWait(SPEED_3);
                case 3 -> clickAndWait(SPEED_4);
                case 4 -> clickAndWait(SPEED_5);
                case 5 -> clickAndWait(BOOST_BUTTON);
                case 6 -> clickAndWait(POWER_BUTTON);
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

    private int getElementY(WebElement element) {
        int topY = element.getLocation().getY();
        int height = element.getRect().getHeight();
        return topY + (height / 2);
    }
    /**
     * Checks if any fans are online and controls them.
     * Scrolls if more than 4 fans exist.
     */

    /**
     * Checks if any fans are online and controls them.
     * Uses DeviceHierarchyManager to verify device status and handle parent-child elements.
     * Scrolls if more than 4 fans exist.
     */
    public void checkFanOnline() {
    DeviceHierarchyManager manager = new DeviceHierarchyManager(atomberg);

    try {
        // Click FANS_TAB to display device list
        WebElement fansTab = wait.until(ExpectedConditions.elementToBeClickable(FANS_TAB));
        fansTab.click();
        sleep(3);

        // Get all devices with their hierarchy and status info
        List<DeviceHierarchyManager.DeviceInfo> allDevices = manager.getAllDevicesInfo();

        if (allDevices.isEmpty()) {
            System.out.println("❌ No devices found on screen.");
            return;
        }

        System.out.println("✅ Found " + allDevices.size() + " devices");

        // Filter and process ONLINE devices only
        List<DeviceHierarchyManager.DeviceInfo> onlineDevices =
            manager.getDevicesByStatus(DeviceHierarchyManager.DeviceStatus.ONLINE);

        if (onlineDevices.isEmpty()) {
            System.out.println("⚠️  No online devices available.");
            return;
        }

        // Process each ONLINE device
        for (DeviceHierarchyManager.DeviceInfo device : onlineDevices) {
            System.out.println("\n📱 Processing: " + device.getDeviceName());
            System.out.println("   Status: " + device.getStatus().getDisplayName());
            System.out.println("   Model: " + device.getModelName());
            System.out.println("   Child Elements: " + device.getChildCount());

            // Control the device by clicking on it
            try {
                device.getDeviceElement().click();
                sleep(2);

                // Run fan speed commands on the open control screen
                speedCommands();

                // Navigate back to device list
                atomberg.navigate().back();
                sleep(1);

                System.out.println("✅ Successfully controlled: " + device.getDeviceName());
            } catch (Exception e) {
                System.err.println("❌ Error controlling device: " + e.getMessage());
                atomberg.navigate().back();
            }
        }

        // Generate and log device hierarchy report
        String report = manager.generateHierarchyReport();
        System.out.println(report);

    } catch (Exception e) {
        System.out.println("❌ Error during fan online check: " + e.getMessage());
    } finally {
        sleep(1);
    }
}

    private boolean clickElementWithRetry(By locator) {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement el = atomberg.findElement(locator);
                if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                    el.click();
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private String getElementBounds(AndroidDriver driver, By by) {
        WebElement element = driver.findElement(by);
        try {
            return element.getDomAttribute("bounds");
        } catch (Exception e) {
            return null;
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
            speedCommands();
            atomberg.navigate().back();
        }
    }

    public void deleteMultipleFans() {
        List<WebElement> fans = atomberg.findElements(REMOVE_DEVICE);//android.widget.ImageView
        if (!fans.isEmpty()){
            for (WebElement fan : fans) {
                //android.widget.Button[@index="0"] xPath of error triangle
                String name = fan.getDomAttribute("content-desc");
                System.out.println("Controlling fan: " + name);
                fan.click();
                if (isElementPresent(atomberg, REMOVE_DEVICE)) clickIfExists(atomberg, YES);
                if (isElementPresent(atomberg, DEVICE_REMOVED_SUCCESSFULLY))
                    System.out.println("Device Reset complete, please Restart the device");
            }
        }else {
            ActionsUtil.Tap.withCoordinates(atomberg, 730, 950);
            if (isElementPresent(atomberg, REMOVE_DEVICE)) clickIfExists(atomberg, YES);
            if (isElementPresent(atomberg, DEVICE_REMOVED_SUCCESSFULLY))
                System.out.println("Device Reset complete, please Restart the device");
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
//        ScreenCheck screen = new ScreenCheck(atomberg);
//        screen.homeScreen();

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
     */
    private void clickAndWait(By locator) {
        clickElement(locator);
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
        public static void Fan() {
            if (isElementPresent(atomberg, SELECT_FAN_MODEL )) {
                SixLED(atomberg);
            } else {
                handleOtherModels(atomberg);
            }
        }

        public void manageFanDevice(AndroidDriver atomberg) {
            FanManagement fan = new FanManagement(atomberg);
            fan.addition();
            ActionsUtil.SSleep(5);
            fan.additionProcess();
            ActionsUtil.SSleep(10);
//            fan.checkFan();
            ScreenCheck check = new ScreenCheck(atomberg);
            check.rateUsPopup();
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
        String[] labels = {"1", "2", "3", "4", "5", "6", "Power"};
        String[] command = {"speed", "speed", "speed", "speed", "speed", "speed", "power"};
        AtombergFanStatus get = new AtombergFanStatus();
        String power = get.fanStatus("power");
        String speed = get.fanStatus("speed");
        for (int i = 0; i < actions.length; i++) {
            if(i<actions.length-1) {
                clickAndWait(actions[i]);
                ActionsUtil.sleep(1000);
//                String state = get.fanStatus(command[i]);
//                if(state.equals(labels[i])) System.out.println(command[i]+labels[i]+" verified");
            }
            else{
                clickAndWait(actions[i]);
                ActionsUtil.sleep(1000);
//                String state = get.fanStatus(command[i]);
//                if(!state.equals(power)) System.out.println(command[i]+labels[i]+" verified");
//                else System.out.println("power "+ state);
            }
        }
    }


//    private boolean handleConnectionErrors() {
//        try {
//            if (isElementPresent(atomberg, CONNECTING_TO_LOCK_MODAL)) {
//                atomberg.navigate().back();
//                System.out.println("Back: Connecting to Lock modal appeared.");
//                return true;
//            }
//            if (isElementPresent(atomberg, COULD_NOT_ADD_LOCK)) {
//                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
//                System.out.println("Cancel clicked: Could not add lock.");
//                sleep(1);
//                return true;
//            }
//            if (isElementPresent(atomberg, DEVICE_ALREADY_PAIRED)) {
//                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
//                System.out.println("Cancel clicked: Device already paired.");
//                sleep(1);
//                return true;
//            }
//            if (isElementPresent(atomberg, COULD_NOT_REACH_DEVICE)) {
//                System.out.println("Out of reach.");
//                atomberg.navigate().back();
//                atomberg.navigate().back();
//                return true;
//            }
//            if(isElementPresent(atomberg,COULD_NOT_CONNECT_PROPERLY)){
//                return true;
//            }
//            if (isElementPresent(atomberg, OPERATION_FAILED)){
//                System.out.println(atomberg.findElement(OPERATION_FAILED).getDomAttribute("accessibility id"));
//            }
//        } catch (Exception e) {
//            System.out.println("Error checking connection status: " + e.getMessage());
//        }
//        return false; // No errors found — assume success
//    }
}