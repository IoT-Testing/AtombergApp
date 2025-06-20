package Tests;

import ExtentReports.ExtentReportAT;
import app.AppInitializer;
import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import Listeners.TestListeners;


@Listeners({TestListeners.class})
public class OpenAppTest {

    public AndroidDriver driver;
    public ServerInitializer server = new ServerInitializer();
    public String command;
    protected String deviceSlot;
    protected ExtentReportAT reporter;

    @BeforeClass
    @Parameters({"deviceSlot"})
    public void setup(String deviceSlot) {
        this.deviceSlot = deviceSlot;
        reporter = new ExtentReportAT(deviceSlot);
        reporter.startTest("Device Setup", deviceSlot);
        System.out.println("Setup done for deviceSlot: " + deviceSlot);
        // Add Connect2/command/STF connection here if needed
    }

    @Test(priority = 1)
    void testOpenApp() {
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
