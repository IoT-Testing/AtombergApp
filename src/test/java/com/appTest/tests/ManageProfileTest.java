package com.appTest.tests;

import ExtentReports.ExtentReportAT;
import app.MoreTab.Profile;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.testng.annotations.Test;

public class ManageProfileTest extends BaseTest {
    public AndroidDriver driver;

    @Test(priority = 2, description = "Edit user profile")
    void testManageProfile() {
        try {
            reporter.startTest("Profile Edit", deviceSlot);
            System.out.println("Profile Edit test start");
            driver = getDriver();
            Profile profile = new Profile(driver);
            profile.edit();
            reporter.log(Status.PASS, "Profile edited successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Profile edit failed: " + e.getMessage());
            throw e;
        } finally {
            System.out.println("Profile Edit test end");
            reporter.endTest();
        }
    }
}
