package app;

import app.util.ActionsUtil;
import app.util.Navigation;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import static app.BLEOnlyFans.ProgressivePauseResume.PAUSE_RESUME_CYCLES;
import static app.resources.Locators.Android.DeviceScreens.BLEFan.*;
import static app.util.ActionsUtil.sleep;


public class MainTwo {

    // Example locators - adjust selectors to match your app's UI
    private static final By START_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Start\"]");
    private static final By STOP_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Stop\"]");
    private static final By PAUSE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Pause\"]");
    private static final By RESUME_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Resume\"]");
    private static final By OTA = By.xpath("//android.widget.Button[@content-desc=\"OTA Test\"]");

    public static PrintWriter csvWriter;
    private static boolean csvInitialized = false;
    private static AndroidDriver driver;
    private int currentAttempt = 1;
    private static final String DEVICE_PREFIX = "atomberg_W2_";
    public AppiumDriverLocalService service;
    private static final String APPIUM_GUI_URL = "http://127.0.0.1:4723";

    public static void main(String[] args) {
        MainTwo main = new MainTwo();
        try {
            main.runTestFlowOne();
        } catch (Throwable e) {
            System.err.println("Test failed with exception: " + e.getMessage());
        } finally {
            main.quitDriverSafely();
        }
    }


    public void runTestFlowOne() throws Throwable {

        ServerInitializer serverInitializer = new ServerInitializer();
        serverInitializer.startServer();
//        initializeDriver();
//        driver.activateApp("com.atomberg.app");
//        Navigation.openFanControl(driver);
//        driver.activateApp("com.example.flutter_ble_ota");
//        ActionsUtil.SSleep(5);
//        OTATest();
//        for (int i = 1; i <10; i++){
//            startLog();
//            selectFileWithScroll(i);
//            scan();
//            WebElement device = findElementByContentDescStartsWith(DEVICE_PREFIX);
//            if(device != null){
//                device.click();
//            }
//            startOTA();
//        }
    }

    private void scan(){
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Scan\"]")).click();
    }

    private void startOTA(){
        driver.findElement(START_BUTTON).click();
    }

    private void OTATest(){
        driver.findElement(OTA).click();
        System.out.println("OTA Test clicked");
    }

    private void startLog(){
        driver.findElement(By.xpath("//android.widget.Button[@index=\"1\"]")).click();
    }

    private boolean waitForElement(By locator, int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startTime) < (timeoutSeconds * 1000)) {
            if (isElementPresent(locator)) {
                return true;
            }
            sleep(500);
        }
        return false;
    }

    private String getAttribute(WebElement el, String attr) {
        try {
            return el.getDomAttribute(attr);
        } catch (Exception e) {
            return null;
        }
    }

    private WebElement findElementByContentDescStartsWith(String prefix) {
        List<WebElement> candidates = driver.findElements(AppiumBy.className("android.view.View"));
        return candidates.stream()
                .map(el -> getAttribute(el, "content-desc"))
                .filter(Objects::nonNull)
                .filter(desc -> desc.startsWith(prefix))
                .findFirst()
                .flatMap(desc -> driver.findElements(AppiumBy.className("android.view.View")).stream()
                        .filter(el -> Objects.equals(getAttribute(el, "content-desc"), desc))
                        .findFirst())
                .orElse(null);
    }

    private void selectFileWithScroll(int i) {
        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"File\"]")).click();
        sleep(5000);
        System.out.println("🔍 Scrolling to find: " + i);
        By fileLocator = By.xpath("//android.widget.TextView[@resource-id='android:id/title' and @text='4.1." + i + ".bin']");
                                                 //android.widget.TextView[@resource-id="android:id/title" and @text="4.1.1.bin"]
        boolean found = false;
        int scrolls = 0;
        final int MAX_SCROLLS = 10; // Prevent infinite loop

        while (!found && scrolls < MAX_SCROLLS) {
            try {
                WebElement fileEl = driver.findElement(fileLocator);
                if (fileEl.isDisplayed()) {
                    fileEl.click();
                    System.out.println("✅ File selected: 4.1." + i +".bin");
                    return;
                }
            } catch (Exception ignored) {}
            ActionsUtil.Scroll.Up(driver);

            scrolls++;
        }

        throw new RuntimeException("❌ Could not find or click file: 4.1. " + i +
                ".bin | Total scrolls attempted: " + scrolls);
    }

    public void runProgressivePauseResume() {
        System.out.println("⏱ Starting high-accuracy pause-resume using coordinate taps...");

        // Initialize CSV on first run
//        initCSV();
        // Run firmware verification and pause-resume sequence
        boolean success = executePauseResumeSequence(currentAttempt);

        if (success) {
            System.out.println("✅ Firmware verification and pause-resume completed successfully!");
        } else {
            System.out.println("❌ Failed to complete firmware verification and pause-resume sequence");
        }

        currentAttempt++; // Increment for next run
    }

    private boolean executePauseResumeSequence(int attemptNumber) {
        String actionId = "Pause-Resume Cycle";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            try {
                // Step 1: Click Start (if not already started)
                try {
                    driver.findElement(START_BUTTON).click();
                    System.out.println("▶️ Start button clicked.");
                } catch (Exception e) {
                    System.out.println("⚠️ 'Start' button not found or already running.");
                }
                sleep(HOLD_DURATION_MS);

                // Step 2: Perform pause-resume cycles
                boolean allCyclesSuccessful = true;
                for (int i = 0; i < PAUSE_RESUME_CYCLES; i++) {
                    // ⏸️ Pause: Tap at center-bottom
//                    ActionsUtil.Tap.withCoordinates(driver, 370, 1220);// for narzo only
                    ActionsUtil.Tap.withCoordinates(driver, 500, 2020);// for Poco only
                    sleep(HOLD_DURATION_MS);

                    // ▶️ Resume: Tap again
//                    ActionsUtil.Tap.withCoordinates(driver, 370, 1220);// for narzo only
                    ActionsUtil.Tap.withCoordinates(driver, 500, 2020);// for Poco only
                    sleep(HOLD_DURATION_MS);

                    System.out.println("🔁 Cycle " + (i + 1) + "/" + PAUSE_RESUME_CYCLES + " completed");

                    // Check if we're still connected
                    if (!isDeviceConnected()) {
                        allCyclesSuccessful = false;
                        break;
                    }
                }
                if(isElementPresent(RESUME)) driver.findElement(RESUME).click();

                if (allCyclesSuccessful) {
                    status = "Success";
                    success = true;
                }
            } catch (Exception e) {
                // Ignore and retry
            }
            String versionForLogging = "";


            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean isDeviceConnected() {
        try {
            return !Objects.requireNonNull(driver.getPageSource()).contains("Device not connected");
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Throwable e) {
            return false;
        }
    }

    private void initializeDriver() throws Throwable {
        AppInitializer initializer = new AppInitializer(driver);
        initializer.initializeDriver(); // Connects to device

        // Set implicit wait
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        System.out.println("Driver initialized successfully.");
    }

    // === Utility Methods ===

    public void quitDriverSafely() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Driver session ended.");
            } catch (Throwable e) {
                System.err.println("Error during driver quit: " + e.getMessage());
            }
        }
    }

    private static void initCSV() {
        if (csvInitialized) return;
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            csvWriter = new PrintWriter(new FileWriter("DeviceProvisioningTest/DeviveProvisioning" + timestamp + ".csv", true));
            csvWriter.println("Attempt Number,Status");
            csvWriter.flush();
            csvInitialized = true;
        } catch (IOException e) {
            System.err.println("Failed to create CSV file: " + e.getMessage());
        }
    }

    void printRow(int attemptNumber, String status) {
        // Format: clean, no extra spaces
        String row = String.format("%d,%s", attemptNumber,status);

        // Write to CSV
        initCSV();
        csvWriter.println(row);
        csvWriter.flush();

        // Print to console
        System.out.printf("%-15d | %-8s%n", attemptNumber, status);
    }

    public static void waitForElementPresence(AndroidDriver driver, By locator, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        // Now 'element' is guaranteed to be present in the DOM
    }
}
