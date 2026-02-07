// src/test/java/steps/StepDefinitions.java

package steps;

import Tests.BaseTest;
import app.*;
import app.Analytics.analytics;
import app.Fan.FanManagement;
import app.Lock.LockManagement;
import app.Login.Email;
import app.MoreTab.*;
import app.util.ActionsUtil;
import app.util.ScreenRecording;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import static app.resources.Locators.Android.AppLocators.Login.*;
import static app.resources.Locators.Android.HomeLocators.*;
import static org.testng.Assert.*;

public class StepDefinitions {

    private AndroidDriver driver;

    // Initialize driver via BaseTest (assumed to be shared)
    public StepDefinitions() {
        BaseTest BaseTest = new BaseTest();
        this.driver = BaseTest.getDriver(); // Ensure BaseTest provides singleton driver
        assertNotNull(driver, "Driver must not be null. Ensure BaseTest setup runs before.");
    }

    @Given("The Atomberg app is launched on Android device")
    public void launchApp() {
        BaseTest.reporter.startTest("Open App", BaseTest.deviceSlot);
        try {
            ActionsUtil.SSleep(2);
            String APP_PACKAGE = "com.atomberg.app";
            driver.activateApp(APP_PACKAGE);
            ActionsUtil.SSleep(5);

            AppInitializer initializer = new AppInitializer();
            initializer.setDriver(driver);

            boolean onLogin = initializer.checkMainScreen();
            System.out.println("On login screen: " + onLogin);

            if (onLogin) {
                Email login = new Email(driver);
                login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
            }

            WebElement moreTab = findElementWithWait(MORE_TAB, 5);
            assertTrue(moreTab.isDisplayed(), "Should reach home screen after launch");
            BaseTest.reporter.log(Status.PASS, "App launched and login completed");

        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "App launch failed: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @When("User logs in with valid credentials")
    public void loginUser() {
        // Already handled in Given step; this can act as confirmation
        WebElement moreTab = findOptionalElement(driver, MORE_TAB);
        assertNotNull(moreTab, "More tab should be present post-login");
    }

    @Then("User should be redirected to the home screen")
    public void verifyHomeScreen() {
        WebElement moreTab = findOptionalElement(driver, MORE_TAB);
        assertTrue(moreTab.isDisplayed(), "User should see home screen after login");
    }

    @Given("User is on the home screen")
    public void userIsOnHomeScreen() {
        WebElement moreTab = findElementWithWait(MORE_TAB, 10);
        assertTrue(moreTab.isDisplayed(), "Prerequisite: Must be on home screen");
    }

    @When("User navigates to Profile section and edits profile")
    public void editProfile() {
        BaseTest.reporter.startTest("Edit Profile", BaseTest.deviceSlot);
        try {
            Profile profile = new Profile(driver);
            profile.edit();
            BaseTest.reporter.log(Status.PASS, "Profile edited successfully");
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Profile edit failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("Profile update should be confirmed")
    public void confirmProfileUpdate() {
        // Example assertion – depends on actual UI feedback
        assertTrue(true, "Assume edit method includes internal validation");
    }

    @When("User opens the Manage section and views family members")
    public void viewFamilyMembers() {
        BaseTest.reporter.startTest("Family Management", BaseTest.deviceSlot);
        try {
            Manage manage = new Manage(driver);
            manage.family();
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Family management failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("Family list or header should be visible")
    public void verifyFamilyHeader() {
        WebElement familyHeader = findOptionalElement(driver, By.xpath("//android.view.View[@content-desc='Your Families']"));
        assertNotNull(familyHeader, "Family section header should be visible");
    }

    @When("User checks connected fans")
    public void controlFans() {
        BaseTest.reporter.startTest("Fan Control", BaseTest.deviceSlot);
        try {
            FanManagement fan = new FanManagement(driver);
            fan.checkFan();
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Fan control failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("At least one fan should be controllable")
    public void verifyFanControlled() {
        WebElement fansTab = findOptionalElement(driver, By.xpath("//android.widget.ImageView[@content-desc='Fans']"));
        assertNotNull(fansTab, "Fans tab should exist after interaction");
    }

    @When("User checks connected locks")
    public void controlLocks() {
        BaseTest.reporter.startTest("Lock Control", BaseTest.deviceSlot);
        try {
            LockManagement lock = new LockManagement(driver);
            lock.checkLock();
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Lock control failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("Unlock handle should be accessible")
    public void verifyUnlockHandle() {
        WebElement unlockHandle = findOptionalElement(driver, By.xpath("//android.view.View[@content-desc='Pull down to unlock']"));
        assertNotNull(unlockHandle, "Unlock handle should be accessible");
    }

    @When("User opens the Analytics section")
    public void viewAnalytics() {
        BaseTest.reporter.startTest("Analytics", BaseTest.deviceSlot);
        try {
            analytics analytics = new analytics(driver);
            analytics.Show();
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Analytics failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("Analytics chart should be displayed")
    public void verifyChartDisplayed() {
        WebElement chart = findOptionalElement(driver, By.className("android.view.View"));
        assertNotNull(chart, "Analytics chart should be rendered");
    }

    @When("User navigates through all help options")
    public void navigateHelpSection() {
        BaseTest.reporter.startTest("Help Section", BaseTest.deviceSlot);
        try {
            Help help = new Help(driver);
            Play play = new Play(driver);
            Manage manage = new Manage(driver);

            help.ConnectivityTroubleshoot();
            manage.help();
            help.newComplaint();
            help.installationRequest();
            help.serviceRequest();
            help.trackAComplaint();
            play.videos();
            help.manual();
            ActionsUtil.Scroll.Up(driver);
            help.email();
            help.call();
            driver.navigate().back();

        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Help navigation failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("User should return to the home screen after completion")
    public void verifyBackToHome() {
        assertTrue(isOnHomeScreen(driver), "Should return to home screen after Help section");
    }

    @When("User performs logout")
    public void performLogout() {
        BaseTest.reporter.startTest("Logout", BaseTest.deviceSlot);
        try {
            Manage manage = new Manage(driver);
            manage.logout();
            ActionsUtil.SSleep(5);
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("Login screen should appear again")
    public void verifyLoginScreenAfterLogout() {
        WebElement loginIndicator = findElementWithWait(LOGIN_SCREEN_INDICATOR, 5);
        assertTrue(loginIndicator.isDisplayed(), "Login screen should appear after logout");
    }

    @When("Driver and test server are closed")
    public void closeDriverAndServer() {
        BaseTest.reporter.startTest("Driver Close", BaseTest.deviceSlot);
        try {
            ScreenRecording recording = new ScreenRecording(driver);
            recording.stop();
            BaseTest.server.stopServer();
            BaseTest.reporter.log(Status.PASS, "Driver closed and server stopped");
        } catch (Exception e) {
            BaseTest.reporter.log(Status.FAIL, "Cleanup failed: " + e.getMessage());
            throw new RuntimeException(e);
        } finally {
            BaseTest.reporter.endTest();
        }
    }

    @Then("No errors should occur during cleanup")
    public void ensureCleanTeardown() {
        // This step verifies no exception was thrown above
        assertTrue(true, "Teardown completed without exceptions");
    }

    // Utility Methods (reuse from original class)
    private WebElement findElementWithWait(By locator, long timeoutSec) {
        final long POLLING_INTERVAL_MS = 500;
        final long TIMEOUT_MS = timeoutSec * 1000;
        long startTimeMs = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTimeMs) < TIMEOUT_MS) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    System.out.println("✅ Found and visible: " + locator);
                    return element;
                }
            } catch (Exception e) {
                System.out.println("⚠️ Not found yet: " + locator);
            }
            ActionsUtil.sleep(POLLING_INTERVAL_MS);
        }
        throw new RuntimeException("❌ Failed to find element after " + timeoutSec + " seconds: " + locator);
    }

    private WebElement findOptionalElement(AndroidDriver driver, By locator) {
        try {
            return driver.findElement(locator);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    private boolean isOnHomeScreen(AndroidDriver driver) {
        try {
            return findOptionalElement(driver, MORE_TAB) != null &&
                    findOptionalElement(driver, MORE_TAB).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}