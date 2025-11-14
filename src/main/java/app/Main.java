package app;

import app.Fan.FanManagement;
import app.Login.Email;
import app.resources.ArduinoRelayControllerModern;
import app.resources.Locators.FanLocators;
import app.resources.PythonFileScript;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
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

import static app.resources.Locators.FanLocators.*;
import static app.resources.Locators.HomeLocators.MORE_TAB;
import static app.util.AppUtil.isElementPresent;

public class Main {
    private static final By bof = By.xpath("//android.view.View[@content-desc=\"Atomberg_R3_fea1f937004b1200\"]");
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
        initializeDriver();
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(5);
        boolean success = false;
        ArduinoRelayControllerModern controller = new ArduinoRelayControllerModern();
        controller.autoConnect();
        for(int i = 1; i<= 20;i++){
            try{
                ActionsUtil.SSleep(5);
                FanManagement.Select fan = new FanManagement.Select();
                fan.manageFanDevice(driver);
                if (isElementPresent(ADDING_THE_DEVICE)) {
                    ActionsUtil.SSleep(30);
                }
                System.out.println("30 second wait complete");
                FanManagement.ConnectionError error = FanManagement.handleConnectionErrors();
                if(error != FanManagement.ConnectionError.NO_ERROR) {
                    if(error.equals(FanManagement.ConnectionError.OPERATION_FAILED)){
                        error.handle(driver);
                        printRow(i, "Failed");
                        controller.sendLEDCommand(true);
                        continue;
                    }
                    else error.handle(driver);
                }
                else {
                    success = true;
                    System.out.println("Running Python File for 2 iterations");
                    PythonFileScript run = new PythonFileScript();
                    run.script();
                    System.out.println("Running Python File Complete");
                    ActionsUtil.SSleep(10);
                    FanManagement fanManagement = new FanManagement(driver);
                    fanManagement.deleteMultipleFans();
                    if (!controller.serialPort.isOpen()) controller.autoConnect();
                    controller.sendLEDCommand(true);
                    ActionsUtil.SSleep(5);
                }
            }catch (Exception e){
                success = false;
                System.out.println(e.getMessage());
            }
            if(success)printRow(i,"Successful");
        }
        controller.disconnect();
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
        AppInitializer initializer = new AppInitializer();
        initializer.initializeDriver(); // Connects to device
        this.driver = initializer.getDriver();

        // Set implicit wait
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        System.out.println("Driver initialized successfully.");
    }
    private static void backToHome() {
        int attempts = 0;
        while (!isElementPresent(MORE_TAB) && attempts < 5) {
            System.out.println("Navigating back... attempt " + (++attempts));
            driver.navigate().back();
            ActionsUtil.sleep(1000);
        }

        if (!isElementPresent(MORE_TAB)) {
            System.err.println("Failed to return to 'Home Screen' after 5 back presses.");
        }
    }
    private void launchApp() {
        ActionsUtil.SSleep(2);
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(3);
        System.out.println("App launched.");
    }

    private boolean isOnLoginScreen() {
        AppInitializer appCheck = new AppInitializer();
        appCheck.setDriver(driver);
        boolean onLogin = appCheck.checkMainScreen();
        System.out.println("On login screen: " + onLogin);
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
        System.out.println("Permissions handled.");
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
            csvWriter.println("Attempt Number,Action ID,Iteration,Status,Updated Version");
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

/**
 *         fan.addition(bof);
 * //        driver.openNotifications();
 * //        ActionsUtil.sleep(750);
 * //        ActionsUtil.Tap.withCoordinates(driver, 500, 500);
 *         By acceptBtn = By.id("android:id/button1");
 *         driver.findElement(acceptBtn).click();
 *         ActionsUtil.SSleep(6);
 * //        driver.openNotifications();
 * //        ActionsUtil.sleep(500);
 * //        ActionsUtil.Tap.withCoordinates(driver, 500, 500);
 *         driver.findElement(acceptBtn).click();
 *         bleFanAddition();
 */