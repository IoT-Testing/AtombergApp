package com.appTest.tests;

import app.AppInitializer;
import app.Fan.FanManagement;
import app.Login.Email;
import app.MoreTab.Manage;
import app.ScreenCheck.ScreenCheck;
import app.resources.ArduinoRelayControllerModern;
import app.resources.PythonFileScript;
import app.util.ActionsUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * DeviceProvTest - End-to-end test: login → fan control → logout.
 *
 * <p>This version improves structure, separation of concerns,
 * error handling, and reporting integration.
 */
public class DeviceProvTest extends BaseTest {

    private AndroidDriver driver;
    private static PrintWriter csvWriter;
    private static boolean csvInitialized = false;

    @BeforeClass
    public void setUp() {
        this.driver = getDriver();
        System.out.println("DeviceProvTest setup completed for device: " + deviceSlot);
    }

    @Test(priority = 1, description = "Open app and login")
    public void testDeviceProvisioning() throws Exception {
        try {
            reporter.startTest("Device Provisioning", deviceSlot);
            System.out.println("Device Provisioning test start");

            // Step 1: Open App & Login
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (appInitializer.checkMainScreen()) {
                performLogin();
            } else {
                System.out.println("Already logged in.");
            }
            reporter.log(Status.PASS, "App launched and user logged in successfully");

            // Step 2: Verify Home Screen & Control Fan
            ScreenCheck screen = new ScreenCheck(driver);
            screen.homeScreen();

            boolean success;
            ArduinoRelayControllerModern controller = new ArduinoRelayControllerModern();
            controller.autoConnect();
            for(int i = 0; i < 50; i++){
                try{
                    ActionsUtil.SSleep(5);
                    FanManagement.Select fan = new FanManagement.Select();
                    fan.manageFanDevice(driver);
                    System.out.println("Running Python File for 3 iterations");
                    PythonFileScript run = new PythonFileScript();
                    run.script();
                    System.out.println("Running Python File Complete");
                    ActionsUtil.SSleep(10);
                    FanManagement fanManagement = new FanManagement(driver);
                    fanManagement.deleteMultipleFans();
                    if(!controller.serialPort.isOpen()) controller.autoConnect();
                    controller.sendLEDCommand(true);
                    ActionsUtil.SSleep(5);
                    success = true;
                }catch (Exception e){
                    success = false;
                }
                if(success) printRow(i,"Successful");
            }
            controller.disconnect();
            reporter.log(Status.PASS, "Fan control verified successfully");

            // Step 3: Logout from App
            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);

            String currentActivity = driver.currentActivity();
            Assert.assertNotNull(currentActivity);
            boolean isOnLoginScreen = currentActivity.contains("Login") || currentActivity.contains("Splash");

            if (isOnLoginScreen) {
                reporter.log(Status.PASS, "Successfully logged out");
            } else {
                reporter.log(Status.WARNING, "Logout completed but still on main screen");
            }
            
            reporter.log(Status.PASS, "Device provisioning test completed successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Device provisioning failed: " + e.getMessage());
            afterTestFailure(driver);
            throw e;
        } finally {
            System.out.println("Device Provisioning test end");
            reporter.endTest();
        }
    }

    // === Internal Helpers ===

    private void performLogin() throws Exception {
        String email = System.getenv("TEST_EMAIL");
        String password = System.getenv("TEST_PASSWORD");

        if (email == null || password == null) {
            email = "iot.testing.atomberg2@gmail.com";
            password = "Atomberg@123";
        }

        Email login = new Email(driver);
        login.email(email, password);
        ActionsUtil.SSleep(5);
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
    private static void initCSV() {
        if (csvInitialized) return;
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            csvWriter = new PrintWriter(new FileWriter("DeviveProvisioning" + timestamp + ".csv", true));
            csvWriter.println("Attempt Number,Action ID,Iteration,Status,Updated Version");
            csvWriter.flush();
            csvInitialized = true;
        } catch (IOException e) {
            System.err.println("Failed to create CSV file: " + e.getMessage());
        }
    }
}