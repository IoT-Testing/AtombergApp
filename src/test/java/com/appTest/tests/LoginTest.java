package com.appTest.tests;

import app.Login.Email;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.AppUtil;
import com.aventstack.extentreports.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static app.resources.Credentials.*;
import static app.resources.Locators.Android.AppLocators.Login.*;
import static app.resources.Locators.Android.HomeLocators.MORE_TAB;

/**
 * LoginTest – validates email login, invalid credential handling, and logout.
 *
 * NOTE: @Listeners is intentionally omitted here.
 * It is declared on BaseTest and is inherited by all subclasses.
 * Re-declaring it here would register each listener twice.
 *
 * Test order:
 *   1. Valid login                  → asserts MORE_TAB visible (home screen reached)
 *   2. Logout                       → asserts LOGIN_SCREEN_INDICATOR visible
 *   3. Invalid password             → asserts INCORRECT_PASSWORD_MESSAGE visible
 *   4. Invalid email format         → asserts exception thrown (Continue blocked)
 *   5. Re-login (state restore)     → asserts MORE_TAB visible again
 */
public class LoginTest extends BaseTest {

    private Email  emailLogin;
    private Manage manage;

    @BeforeClass(dependsOnMethods = "setup")
    public void initActions() {
        emailLogin = new Email(driver);
        manage     = new Manage(driver);
        Assert.assertNotNull(driver, "Driver must be initialised before LoginTest");
        System.out.println("LoginTest ready on device: " + deviceSlot);
    }

    // ── 1. Valid login ────────────────────────────────────────────────────────

    @Test(priority = 1, description = "Login with valid credentials and verify home screen")
    public void testValidLogin() throws Exception {
        reporter.startTest("Valid Login", deviceSlot);
        try {
            ActionsUtil.SSleep(3);
            emailLogin.email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
            ActionsUtil.SSleep(5);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab must be visible after a successful login — confirms home screen reached");

            AppUtil.captureScreenshot(driver, "valid_login_success");
            reporter.log(Status.PASS, "Logged in as " + DEFAULT_EMAIL + " — home screen confirmed");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "valid_login_fail");
            reporter.log(Status.FAIL, "Valid login failed: " + e.getMessage());
            afterTestFailure();
            throw e;
        } finally {
            reporter.endTest();
        }
    }

    // ── 2. Logout ─────────────────────────────────────────────────────────────

    @Test(priority = 2, description = "Logout and verify return to login screen",
            dependsOnMethods = "testValidLogin")
    public void testLogout() {
        reporter.startTest("Logout", deviceSlot);
        try {
            manage.logout();
            ActionsUtil.SSleep(3);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, LOGIN_SCREEN_INDICATOR),
                    "Login screen indicator must be visible after logout");

            AppUtil.captureScreenshot(driver, "logout_success");
            reporter.log(Status.PASS, "Logout succeeded — login screen visible");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "logout_fail");
            reporter.log(Status.FAIL, "Logout failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 3. Invalid password ───────────────────────────────────────────────────

    @Test(priority = 3, description = "Wrong password shows incorrect-password error message",
            dependsOnMethods = "testLogout")
    public void testInvalidPasswordLogin() {
        reporter.startTest("Invalid Password Login", deviceSlot);
        try {
            // Attempt login with a known-wrong password.
            // Email.email() throws Exception("Invalid Password: …") after the app displays the error.
            // We catch only that expected exception; anything else (driver crash, network error,
            // IllegalArgumentException) propagates so the test correctly fails.
            try {
                emailLogin.email(DEFAULT_EMAIL, "WrongPass@999");
            } catch (Exception e) {
                // Re-throw if it is NOT the expected "incorrect password" error
                if (!e.getMessage().contains("Invalid Password") &&
                        !e.getMessage().contains("Incorrect password")) {
                    throw e;
                }
                // Otherwise it is the expected exception — continue to assertion below
                System.out.println("Expected exception caught: " + e.getMessage());
            }

            ActionsUtil.SSleep(2);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, INCORRECT_PASSWORD_MESSAGE),
                    "\"! Incorrect password\" error message must be displayed on screen");

            AppUtil.captureScreenshot(driver, "invalid_password_error");
            reporter.log(Status.PASS, "Incorrect-password message displayed as expected");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "invalid_password_fail");
            reporter.log(Status.FAIL, "Invalid password test failed unexpectedly: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 4. Invalid email format ───────────────────────────────────────────────

    @Test(priority = 4, description = "Invalid email format — Continue button must be blocked",
            dependsOnMethods = "testInvalidPasswordLogin")
    public void testInvalidEmailLogin() {
        reporter.startTest("Invalid Email Login", deviceSlot);
        try {
            Exception caught = null;
            try {
                emailLogin.email("not-an-email", DEFAULT_PASSWORD);
            } catch (Exception e) {
                caught = e;
            }

            Assert.assertNotNull(caught,
                    "An exception must be thrown when an invalid email is entered — " +
                            "Email.email() should detect that Continue is not clickable");

            Assert.assertTrue(
                    caught.getMessage() != null &&
                            (caught.getMessage().contains("invalid") ||
                                    caught.getMessage().contains("Invalid") ||
                                    caught.getMessage().contains("Continue")),
                    "Exception message must indicate the invalid-email / Continue-blocked condition; got: "
                            + caught.getMessage());

            AppUtil.captureScreenshot(driver, "invalid_email_blocked");
            reporter.log(Status.PASS, "Invalid email correctly blocked: " + caught.getMessage());
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "invalid_email_test_fail");
            reporter.log(Status.FAIL, "Invalid email test failed: " + e.getMessage());
            afterTestFailure();
            throw new RuntimeException(e);
        } finally {
            reporter.endTest();
        }
    }

    // ── 5. Re-login (state restore) ───────────────────────────────────────────

    @Test(priority = 5, description = "Re-login with valid credentials to restore clean state",
            dependsOnMethods = "testInvalidEmailLogin")
    public void testReLogin() throws Exception {
        reporter.startTest("Re-Login (state restore)", deviceSlot);
        try {
            // Navigate back to the login screen if still in the email-entry flow
            driver.navigate().back();
            ActionsUtil.SSleep(2);

            emailLogin.email(DEFAULT_EMAIL, DEFAULT_PASSWORD);
            ActionsUtil.SSleep(5);

            Assert.assertTrue(
                    AppUtil.isElementPresent(driver, MORE_TAB),
                    "More tab must be visible after re-login — confirms home screen reached");

            AppUtil.captureScreenshot(driver, "relogin_success");
            reporter.log(Status.PASS, "Re-login successful — home screen confirmed");
        } catch (Exception e) {
            AppUtil.captureScreenshot(driver, "relogin_fail");
            reporter.log(Status.FAIL, "Re-login failed: " + e.getMessage());
            afterTestFailure();
            throw e;
        } finally {
            reporter.endTest();
        }
    }
}
