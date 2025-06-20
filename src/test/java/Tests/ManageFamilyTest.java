package Tests;

import ExtentReports.ExtentReportAT;
import app.AppInitializer;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Test;

public class ManageFamilyTest {
    public AndroidDriver driver;
    public ServerInitializer server = new ServerInitializer();
    public String command;
    private final String deviceSlot;
    private final ExtentReportAT reporter;

    public ManageFamilyTest(String deviceSlot, ExtentReportAT reporter) {
        this.deviceSlot = deviceSlot;
        this.reporter = reporter;
    }

    @Test(priority = 1)
    void testManageFamily() {
        try {
            reporter.startTest("Open App", deviceSlot);
            System.out.println("OpenApp test start");
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.initializeDriverWithURL(server.service.getUrl(), command);
            driver = appInitializer.getDriver();
            ScreenRecording recording = new ScreenRecording(driver);
            recording.start();
            ActionsUtil.SSleep(2);
            driver.activateApp("com.atomberg.app");
            appInitializer.checkMainScreen();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            System.out.println("OpenApp test end");
            reporter.endTest();
        }
    }
}
