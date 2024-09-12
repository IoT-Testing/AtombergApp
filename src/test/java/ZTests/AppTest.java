package ZTests;

import Login.Email;
import MoreTab.*;
import Tabs.Analytics;
import Tabs.MoreTab;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import AtombergTest.Method;
import static ZTests.ExtentReportAT.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    private static final Logger log = LogManager.getLogger(AppTest.class);
    public static AppiumDriver driver;
    public static boolean ITestResult;
    public static final ExtentReports extent = ExtentReportAT.getReportObjects();

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void openAtomberg() {
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
        }
        System.out.println("Atomberg app opened...");
        assert driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
                " with Atomberg\"]")).isDisplayed();
        sleep(6000);
        Method.captureScreenshot(driver);
    }

    @Order(1)
    @Test // opening the app
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
    @Test //login, there is one more login method, which is parameterising this test.
    void testLogin() {
        try {
            startTest("Login");
            Email.Login(driver);
            if (ITestResult)
            {
                ExtentReportAT.getTest().log(Status.PASS, "login test passed");
            } else{
                ExtentReportAT.getTest().log(Status.FAIL, "login failed as the password was incorrect, other tests are skipped");
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
    void testManageFamily(){
        assumeTrue(ITestResult, "as login failed, other tests are skipped");
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
//    void testFanControl() {
//        try {
//            startTest("fan control");
//            so.fan(driver);
//        } catch (Exception e) {
//            ExtentReportAT.getTest().log(Status.FAIL, "fan control failed: " + e.getMessage());
//        } finally {
//            endTest();
//        }
//    }
//
//    @order(5)
//    @test
    void testAnalytics() {
        assumeTrue(ITestResult, "as login failed, other tests are skipped");
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

    @Order(6)
    @Test // preconditions: please unlink alexa and google home sor testing the linking process

    void testMoreTab() {
        assumeTrue(ITestResult, "as login failed, other tests are skipped");
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

    @Order(7)
    @Test
    void testLogout() {
        assumeTrue(ITestResult, "as login failed, other tests are skipped");
        try {
            startTest("logout");
            AccManage.Logout(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(Status.FAIL, "logout failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(8)
    @Test
    void testDriverClose() {
        driver.quit();
    }

    @AfterAll
    static void teardown() {
        if (extent != null) {
            extent.flush();
        }
    }
}
