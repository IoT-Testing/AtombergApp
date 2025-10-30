package Tests;

import app.AppInitializer;
import app.Login.Apple;
import app.Login.Email;
import app.Login.Google;
import app.MoreTab.Manage;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import com.aventstack.extentreports.Status;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;

public class secondlogintest extends BaseTest {

    private AndroidDriver driver;

    private AndroidDriver getDriverSafely() {
        AndroidDriver tempDriver = getDriver();
        if (tempDriver == null) {
            throw new IllegalStateException("Driver is not initialized. Appium session failed to start.");
        }
        return tempDriver;
    }

    private WebElement waitForElement(By locator, int timeoutInSeconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSeconds));
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new NoSuchElementException("Element not found within " + timeoutInSeconds + " seconds: " + locator, e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while waiting for element: " + locator, e);
        }
    }

    @Test(priority = 1)
    public void testCase1() {
        driver = null;
        try {
            reporter.startTest("Correct Credentials", deviceSlot);
            System.out.println("Test start");
            driver = getDriverSafely();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);
            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);
            // Check if already logged in
            if (appInitializer.checkMainScreen()) {
                Email login = new Email(driver);
                login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                ActionsUtil.sleep(5000);
                PermissionUtil.allow(driver);
                Manage manage = new Manage(driver);
                manage.logout();
            } else {
                // Try to detect home screen (indicating logged-in state)
                By homeView = By.xpath("//android.view.View[@content-desc=\"My Home\"]");
                try {
                    WebElement home = waitForElement(homeView, 10);
                    if (home.isDisplayed()) {
                        System.out.println("Already logged in, logging out first.");
                        Manage manage = new Manage(driver);
                        manage.logout();
                        ActionsUtil.SSleep(2);
                        Email login = new Email(driver);
                        login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
                        manage.logout(); // logout after test
                    }
                } catch (NoSuchElementException e) {
                    reporter.log(Status.WARNING, "No active session found. Proceeding with login.");
                }
            }

            driver.terminateApp("com.atomberg.app");
        } catch (IllegalStateException | WebDriverException e) {
            reporter.log(Status.FAIL, "Driver/App initialization failed: " + e.getMessage());
//            Assert.fail("Critical failure: " + e.getMessage());
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Unexpected error in test case 1: " + e.getMessage());
//            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    driver.terminateApp("com.atomberg.app");
                } catch (Exception e) {
                    System.out.println("Failed to terminate app during cleanup: " + e.getMessage());
                }
            }
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 2)
    public void testCase2() {
        driver = null;
        try {
            reporter.startTest("Incorrect Email", deviceSlot);
            System.out.println("Test start");

            driver = getDriverSafely();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(2);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (appInitializer.checkMainScreen()) {
                Email login = new Email(driver);
                try {
                    login.email("hiwitaw422wuzak.com", "Atomberg@1234"); // Invalid email format
                    // Verify error message appears
                    By errorMsg = By.xpath("//*[@text='Please enter a valid email address']");
                    waitForElement(errorMsg, 10);
                    reporter.log(Status.PASS, "Correctly rejected invalid email format.");
                } catch (AssertionError e) {
                    reporter.log(Status.FAIL, "Expected validation error not shown: " + e.getMessage());
                    throw e;
                }
            }

            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(10);
            driver.activateApp("com.atomberg.app");

        } catch (IllegalStateException | WebDriverException e) {
            reporter.log(Status.FAIL, "Driver/App failed: " + e.getMessage());
//            Assert.fail("Critical failure: " + e.getMessage());
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Unexpected error: " + e.getMessage());
//            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    driver.terminateApp("com.atomberg.app");
                } catch (Exception e) {
                    System.out.println("Cleanup failed: " + e.getMessage());
                }
            }
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 3)
    public void testCase3() {
        driver = null;
        try {
            reporter.startTest("Incorrect Password", deviceSlot);
            System.out.println("Test start");

            driver = getDriverSafely();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(2);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (appInitializer.checkMainScreen()) {
                Email login = new Email(driver);
                login.email("hiwitaw422@wuzak.com", "Atomberg@12345"); // Wrong password

                // Validate login failure
                By errorMsg = By.xpath("//*[@text='Invalid credentials']");
                waitForElement(errorMsg, 10);
                reporter.log(Status.PASS, "Correctly rejected incorrect password.");
            }
        } catch (NoSuchElementException e) {
            reporter.log(Status.FAIL, "Expected 'Invalid credentials' message not displayed: " + e.getMessage());
//            Assert.fail("Password validation failed.");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Error in test case 3: " + e.getMessage());
//            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.terminateApp("com.atomberg.app");
            }
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 4)
    public void testCase4() {
        driver = null;
        try {
            reporter.startTest("App Kill After Login", deviceSlot);
            System.out.println("Test start");

            driver = getDriverSafely();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(2);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (appInitializer.checkMainScreen()) {
                Email login = new Email(driver);
                login.email("hiwitaw422@wuzak.com", "Atomberg@1234");
            }

            // Kill app
            driver.terminateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);

            // Relaunch and verify session persistence
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(5);

            // Verify user is still logged in (e.g., home screen appears)
            By homeView = By.xpath("//android.view.View[@content-desc=\"My Home\"]");
            waitForElement(homeView, 15);
            reporter.log(Status.PASS, "User remained logged in after app restart.");

        } catch (NoSuchElementException e) {
            reporter.log(Status.FAIL, "Session not preserved after app kill: " + e.getMessage());
//            Assert.fail("Session management failed.");
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Unexpected error: " + e.getMessage());
            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    Manage manage = new Manage(driver);
                    manage.logout();
                } catch (Exception e) {
                    System.out.println("Logout during cleanup failed: " + e.getMessage());
                }
                try {
                    driver.terminateApp("com.atomberg.app");
                } catch (Exception e) {
                    System.out.println("App termination failed: " + e.getMessage());
                }
            }
            System.out.println("Test end");
            reporter.endTest();
        }
    }

    @Test(priority = 5)
    public void testcase5() {
        driver = null;
        try {
            reporter.startTest("Apple Login", deviceSlot);
            System.out.println("Apple Login test start");

            driver = getDriverSafely();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(2);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (appInitializer.checkMainScreen()) {
                Apple.Login(driver);
            }

            Manage manage = new Manage(driver);
            manage.logout();
            driver.terminateApp("com.atomberg.app");

        } catch (NoSuchElementException e) {
            reporter.log(Status.FAIL, "Apple login failed - element not found: " + e.getMessage());
//            Assert.fail("UI element missing.");
        } catch (WebDriverException e) {
            reporter.log(Status.FAIL, "Web driver error during Apple login: " + e.getMessage());
//            Assert.fail("Driver issue: " + e.getMessage());
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Unexpected error in Apple login: " + e.getMessage());
//            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    driver.terminateApp("com.atomberg.app");
                } catch (Exception e) {
                    System.out.println("Cleanup failed: " + e.getMessage());
                }
            }
            System.out.println("Apple Login test end");
            reporter.endTest();
        }
    }

    @Test(priority = 6)
    public void testcase6() {
        driver = null;
        try {
            reporter.startTest("Google Login", deviceSlot);
            System.out.println("Google Login test start");

            driver = getDriverSafely();
            driver.activateApp("com.atomberg.app");
            ActionsUtil.SSleep(2);

            AppInitializer appInitializer = new AppInitializer();
            appInitializer.setDriver(driver);

            if (appInitializer.checkMainScreen()) {
                Google.Login(driver);
            }

            Manage manage = new Manage(driver);
            manage.logout();
            driver.terminateApp("com.atomberg.app");

        } catch (NoSuchElementException e) {
            reporter.log(Status.FAIL, "Google login failed - element not found: " + e.getMessage());
//            Assert.fail("UI element missing.");
        } catch (WebDriverException e) {
            reporter.log(Status.FAIL, "Driver error during Google login: " + e.getMessage());
//            Assert.fail("Driver issue: " + e.getMessage());
        } catch (Exception e) {
            reporter.log(Status.FAIL, "Unexpected error in Google login: " + e.getMessage());
            Assert.fail("Unexpected exception: " + e.getMessage());
        } finally {
            if (driver != null) {
                try {
                    driver.terminateApp("com.atomberg.app");
                } catch (Exception e) {
                    System.out.println("Cleanup failed: " + e.getMessage());
                }
            }
            System.out.println("Google Login test end");
            reporter.endTest();
        }
    }
}