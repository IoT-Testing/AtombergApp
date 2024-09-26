package ZTests;

import AtombergTest.Method;
import Login.Email;
import MoreTab.AccManage;
import MoreTab.Manage;
import Tabs.Analytics;
import Tabs.MoreTab;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;

import static ZTests.ExtentReportAT.endTest;
import static ZTests.ExtentReportAT.startTest;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    public static final ExtentReports extent = ExtentReportAT.getReportObjects();
    public static AppiumDriver driver;
    public static boolean LoginResult;
//TODO : implement the listeners /watchers

    /*  public static boolean MoreTabResult;
        public static boolean AnalyticsResult;
        public static boolean LogoutResult;
        public static boolean ProfileResult;
    */
    public static boolean ResultOpenApp;

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static boolean openAtomberg() {
        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
        cap.setCapability("apksigner",
                "C:\\Users\\Rohit Bhagat\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
        try {
            System.out.println("Initializing Appium Driver..."); // check if the appium driver is initialized

            URL url = new URL("http://127.0.0.1:4723/wd/hub");// url of the appium session
            driver = new AppiumDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("Appium Driver Initialized.");
        } catch (IOException e) {
            System.out.println("Error Initializing Appium Driver: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        System.out.println("Atomberg app opened...");
        Method.captureScreenshot(driver);
        WebElement LoginScreen = null;
        try {
            LoginScreen = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
                    " with Atomberg\"]"));
        } catch (Exception e) {
        }
        ResultOpenApp = LoginScreen != null;
        sleep(6000);

        return ResultOpenApp;
    }

    @AfterAll
    static void teardown() {
        if (extent != null) {
            extent.flush();
        }
    }

    @Order(1)
    @Test
        // opening the app
    void testOpenApp() {
        try {
            startTest("Open App");
            openAtomberg();
        } catch (Exception e) {
            ExtentReportAT.getTest().log(Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(2)
    @Test
        //login, there is one more login method, which is parameterising this test.
    void testLogin() {
        assumeTrue(ResultOpenApp, "Failed to open app");
        try {
            startTest("Login");
            Email.Login(driver);
            if (LoginResult) {
                ExtentReportAT.getTest().log(Status.PASS, "login test passed");
                ExtentReportAT.test.get().pass("Screenshot captured", MediaEntityBuilder.createScreenCaptureFromPath(Method.ScreenShot).build());
            } else {
                ExtentReportAT.getTest().log(Status.FAIL, "login failed as the password was incorrect, other tests are skipped");
                ExtentReportAT.test.get().fail("Screenshot captured", MediaEntityBuilder.createScreenCaptureFromPath(Method.ScreenShot).build());
                driver.navigate().back();
                driver.navigate().back();
            }
        } catch (Exception e) {
            ExtentReportAT.getTest().log(Status.FAIL, "login failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(3)
    @Test
    void testManageFamily() {
        assumeTrue(LoginResult, "as login failed, other tests are skipped");
        /* assumeTrue will check if the login was successful and then continue with the code
          If the login has failed there is no need to continue with the test */
        try {
            startTest("family");
            Manage.Family(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(Status.FAIL, "logout failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(4)
    @Test
    void testAnalytics() {
        assumeTrue(LoginResult, "as login failed, other tests are skipped");
         /* assumeTrue will check if the login was successful and then continue with the code
          If the login has failed there is no need to continue with the test */
        try {
            startTest("analytics");
            Analytics.Show(driver);
        } catch (Exception e) {
            e.printStackTrace();
            ExtentReportAT.getTest().log(Status.FAIL, "analytics failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(5)
    @Test
        //preconditions: please unlink alexa and google home sor testing the linking process

    void testMoreTab() {
        assumeTrue(LoginResult, "as login failed, other tests are skipped");
         /* assumeTrue will check if the login was successful and then continue with the code
          If the login has failed there is no need to continue with the test */
        try {
            startTest("more tab");
            MoreTab.Options(driver);

        } catch (Exception e) {
            ExtentReportAT.getTest().log(Status.FAIL, "more tab failed: " + e.getMessage());
        } finally {
            {
                endTest();
            }
        }
    }

    @Order(6)
    @Test
    void testLogout() {
        assumeTrue(LoginResult, "as login failed, other tests are skipped");
         /* assumeTrue will check if the login was successful and then continue with the code
          If the login has failed there is no need to continue with the test */
        try {
            startTest("logout");
            AccManage.Logout(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(Status.FAIL, "logout failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(7)
    @Test
    void testDriverClose() {
        driver.quit();
    }
}
