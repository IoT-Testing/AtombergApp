package Tests;

import ExtentReports.ExtentReportAT;
import app.STF.Connect2;
import app.ServerInitializer;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class BaseTest {
    protected String deviceSlot;
    protected ExtentReportAT reporter;
    protected String command;
    protected ServerInitializer server = new ServerInitializer(); // or however you define it

    @BeforeClass
    @Parameters({"deviceSlot"})
    public void setup(String deviceSlot) throws IOException, UnsupportedFlavorException {
        this.deviceSlot = deviceSlot;
        reporter = new ExtentReportAT(deviceSlot);
        reporter.startTest("Device Setup", deviceSlot);
        Connect2 connect = new Connect2();
        connect.ipAddress();
        command = connect.copiedText;
        Runtime.getRuntime().exec(command);
        server.startServer();
    }

    @AfterClass
    public void tearDown() {
        server.stopServer();  // Optional
        reporter.endTest();  // Optional
    }

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

