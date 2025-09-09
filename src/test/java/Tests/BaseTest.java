package Tests;

import ExtentReports.ExtentReportAT;
import app.AppInitializer;
import app.STF.Connect2;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.applitools.eyes.appium.Eyes;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class BaseTest {
    protected String deviceSlot;
    protected ExtentReportAT reporter;
    protected String command;
    protected ServerInitializer server = new ServerInitializer();
    protected Eyes eyes;
    protected AndroidDriver driver;  // Optional if not already managed elsewhere

    @BeforeClass
    @Parameters({"deviceSlot"})
    public void setup(@Optional("0") String deviceSlot) throws IOException, UnsupportedFlavorException, InterruptedException {
        this.deviceSlot = deviceSlot;
        reporter = new ExtentReportAT(deviceSlot);
        reporter.startTest("Device Setup", deviceSlot);

        Connect2 connect = new Connect2();
        connect.ipAddress();
        command = connect.getCopiedText();
        ActionsUtil.SSleep(5);
        Runtime.getRuntime().exec(command);
        server.startServer();

        AppInitializer appInitializer = new AppInitializer();
        appInitializer.initializeDriverWithURL(server.service.getUrl(), command);
        driver = appInitializer.getDriver();
        ScreenRecording recording = new ScreenRecording(driver);
        recording.start();
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    @AfterClass
    public void tearDown() {
        // Safely abort if not already closed
        if (eyes != null) {
            eyes.abortIfNotClosed();
        }
        ScreenRecording recording = new ScreenRecording(driver);
        recording.stop();

        server.stopServer();       // Optional
        reporter.endTest();        // Optional
    }

    // Used by failed test recovery logic
    void afterTestFailure(AndroidDriver driver) {
        ApplicationState state = driver.queryAppState("com.atomberg.app");
        if (state.equals(ApplicationState.RUNNING_IN_FOREGROUND)) {
            WebElement homeScreen = null;
            while (homeScreen == null) {
                try {
                    homeScreen = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\nTab 3 of 3\"]"));
                } catch (Exception ignored) {
                }
                if (homeScreen == null) {
                    System.out.println("Back");
                    driver.navigate().back();
                }
            }
        } else {
            driver.activateApp("com.atomberg.app");
        }
    }
}
