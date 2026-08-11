package com.appTest.tests;

import app.MoreTab.Profile;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.Test;

import static app.resources.Locators.Android.HomeLocators.MORE_TAB;

public class ManageProfileTest extends BaseTest {

    // No local driver field â€” uses BaseTest.driver directly via inheritance.

    // NOTE: must be public â€” TestNG silently ignores non-public @Test methods.
    @Test(priority = 2, description = "Edit user profile")
    public void testManageProfile() {
        try {
            reporter.startTest("Profile Edit", deviceSlot);
            logpoint("Profile Edit test start");
            Profile profile = new Profile(driver);
            profile.edit();
            Assert.assertTrue(AppUtil.isElementPresent(driver, MORE_TAB),
                    "Should return to the home screen (More tab) after editing the profile");
            reporter.log(Status.PASS, "Profile edited successfully");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Profile edit failed: " + e.getMessage());
            throw e;
        } finally {
            logpoint("Profile Edit test end");
            reporter.endTest();
        }
    }
}
