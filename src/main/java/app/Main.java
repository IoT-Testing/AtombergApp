package app;

import app.Fan.FanManagement;
import app.Login.Email;
import app.resources.Locators.Android.DeviceScreens.FanLocators;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import app.util.ScreenRecording;
import io.appium.java_client.android.options.UiAutomator2Options;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static app.resources.Locators.Android.HomeLocators.*;

/*appium server -ka 800 --use-plugins=device-farm -pa /wd/hub --plugin-device-farm-platform=android --relaxed-security --allow-insecure=*:session_discovery,uiautomator2:adb_shell --port 4723
*/
@Log4j2
public class Main {
    public static PrintWriter csvWriter;
    private static boolean csvInitialized = false;
    private static AndroidDriver driver;
//    private final ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.runTestFlow();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
        } finally {
            main.quitDriverSafely();
        }
    }

    public void runTestFlow() throws Exception {
        // Main.initializeDriver should set 'driver' to recorder driver
        initializeDriver();
        launchApp("recorder");
        ScreenRecording recorder = new ScreenRecording(driver);
        recorder.start();
        // Now initialize driver for Atomberg app, but keep the recorder session active
        launchApp("atomberg");
        if (isOnLoginScreen()) performLogin();
        handlePermissions();
        // Ensure we're on the home screen before proceeding
        FanManagement fan = new FanManagement(driver);
        fan.checkFanOnline();

        fan.speedCommands();
//        runTimerAndNavigate(fan::timerOne, driver);
//        runTimerAndNavigate(fan::timerTwo, driver);
//        runTimerAndNavigate(fan::timerThree, driver);
//        runTimerAndNavigate(fan::timerSix, driver);

//         bleFanDeletion();

        launchApp("recorder");
        recorder = new ScreenRecording(driver);
        recorder.stop();
    }

    void runTimerAndNavigate(Runnable timerMethod, AndroidDriver driver) {
        timerMethod.run();
        ActionsUtil.sleep(1500);
        FanManagement fan = new FanManagement(driver);
        fan.timerStop();
        ActionsUtil.sleep(1500);
        driver.navigate().back();
        ActionsUtil.sleep(1500);
    }


    private static boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void bleFanDeletion(){
        ActionsUtil.Tap.withCoordinates(driver, 700, 975);
        driver.findElement(By.xpath("//android.view.View[@index=\"4\"]")).click();
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete device\"]")).click();
    }

    private void bleFanAddition(){
        driver.findElement(FanLocators.NEXT_BUTTON).click();
        ActionsUtil.sleep(500);
        driver.findElement(FanLocators.CONTINUE_BUTTON).click();
    }

//TODO :  Custom timer
    //TODO : All Commands
    // === Setup Methods ===

    private void initializeDriver() throws Exception {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setPlatformVersion("15");
        options.setAutomationName("uiautomator2");
        try {
            URL url = new URL("http://127.0.0.1:4723/");
            driver = new AndroidDriver(url, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("Driver initialized successfully for the device");
        }catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void backToHome() {
        int attempts = 0;
        while (!isElementPresent(MORE_TAB) && attempts < 5) {
            log.info("Navigating back... attempt {}", ++attempts);
            driver.navigate().back();
            ActionsUtil.sleep(1000);
        }

        if (!isElementPresent(MORE_TAB)) {
            System.err.println("Failed to return to 'Home Screen' after 5 back presses.");
        }
    }

    private void launchApp(String appType) {
        ActionsUtil.SSleep(2);
        if ("recorder".equals(appType)) {
            driver.activateApp("com.hbisoft.hbrecorderexample");
            ActionsUtil.SSleep(3);
        }
        else if ("atomberg".equals(appType)) {
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(6);
            System.out.println("Atomberg Home App launched.");
        }
    }

    private boolean isOnLoginScreen() throws Exception {
        AppInitializer appCheck = new AppInitializer(driver);
        boolean onLogin = appCheck.checkMainScreen();
        return onLogin;
    }

    private void performLogin() throws Exception {
        String email = getEnvOrFallback("TEST_EMAIL", "iot.alpha@protonmail.com");
        String password = getEnvOrFallback("TEST_PASSWORD", "Atomberg@123");

        Email login = new Email(driver);
        try {
            login.email(email, password);
            System.out.println("Login successful.");
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            throw e; // Re-throw after logging
        }
    }

    private void handlePermissions() {
        PermissionUtil.allow(driver);
    }


    // === Utility Methods ===

    public void quitDriverSafely() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Driver session ended.");
            } catch (Exception e) {
                System.err.println("Error during driver quit: " + e.getMessage());
            }
        }
    }

    private String getEnvOrFallback(String key, String fallback) {
        String value = System.getenv(key);
        return value != null ? value : fallback;
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
        String row = String.format("%d,%s",
                attemptNumber,status);

        // Write to CSV
        initCSV();
        csvWriter.println(row);
        csvWriter.flush();

        // Print to console
        System.out.printf("%-15d | %-8s%n",
                attemptNumber, status);
    }

    public static void waitForElementPresence(AndroidDriver driver, By locator, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        // Now 'element' is guaranteed to be present in the DOM
    }
}

/*
 *         fan.addition(bof);
 *         driver.openNotifications();
 *         ActionsUtil.sleep(750);
 *         ActionsUtil.Tap.withCoordinates(driver, 500, 500);
 *         By acceptBtn = By.id("android:id/button1");
 *         driver.findElement(acceptBtn).click();
 *         ActionsUtil.SSleep(6);
 *         driver.openNotifications();
 *         ActionsUtil.sleep(500);
 *         ActionsUtil.Tap.withCoordinates(driver, 500, 500);
 *         driver.findElement(acceptBtn).click();
 *         bleFanAddition();
 *
 *
 *
 *         driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Aris Fan\n" +
                "Living Room\"]")).click();
        FanManagement fan = new FanManagement(driver);
        fan.speedCommands();
        runTimerAndNavigate(fan::timerOne, driver);
        runTimerAndNavigate(fan::timerTwo, driver);
        runTimerAndNavigate(fan::timerThree, driver);
        runTimerAndNavigate(fan::timerSix, driver);
 */