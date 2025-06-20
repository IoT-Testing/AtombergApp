package Tests;

import ExtentReports.ExtentReportAT;
import app.MoreTab.Profile;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Test;

public class ManageProfileTest {
    public AndroidDriver driver;
    private final String deviceSlot;
    private final ExtentReportAT reporter;

    public ManageProfileTest(String deviceSlot, ExtentReportAT reporter) {
        this.deviceSlot = deviceSlot;
        this.reporter = reporter;
    }

    @Test(priority = 2, dependsOnMethods = "Test.OpenAppTest")
    void testManageProfile() {
        try {
            reporter.startTest("Profile Edit", deviceSlot);
            System.out.println("Profile Edit test start");
            Profile profile = new Profile(driver);
            profile.edit();
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            System.out.println("Profile Edit test end");
            reporter.endTest();
        }
    }
}
