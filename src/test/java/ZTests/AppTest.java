package ZTests;

import Devices.SO;
import Login.Email;
import MoreTab.*;
import Tabs.Analytics;
import Tabs.MoreTab;
import com.aventstack.extentreports.ExtentReports;
import io.appium.java_client.ios.IOSDriver;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import AtombergTest.Method;
import static ZTests.ExtentReportAT.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    public static IOSDriver driver;
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
                "C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
        try {
            System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized

            URL url = new URL("http://127.0.0.1:4723/wd/hub");// URL of the Appium session
            driver = new IOSDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("Appium driver initialized.");
        } catch (IOException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Assert.assertTrue(driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Experience smart living \n" +
                    " with Atomberg\"]")).isDisplayed());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Atomberg App Opened...");
        assert driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Experience smart living \n" +
                " with Atomberg\"]")).isDisplayed();
        sleep(6000);
        Method.captureScreenshot(driver);
    }

    @Order(1)
    @Test
    void testOpenApp() {
        try {
            startTest("Open App");
            openAtomberg();
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "App Open failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(2)
    @Test
    void testLogin() {
        try {
            startTest("Login");
            Email.Login(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Login failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(3)
    @Test
    void testManageFamily(){
        try {
            startTest("Family");
            Manage.Family(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(4)
    @Test
    void testFanControl() {
        try {
            startTest("Fan Control");
            SO.Fan(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Fan Control failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(5)
    @Test
    void testAnalytics() {
        try {
            startTest("Analytics");
            Analytics.Show(driver);
        } catch (Exception e) {
            e.printStackTrace();
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Analytics failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(6)
    @Test // Preconditions: Please Unlink Alexa and google home sor testing the linking process

    void testMoreTab() {
        try {
            startTest("More Tab");
            MoreTab.Options(driver);

        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "More Tab failed: " + e.getMessage());
        } finally {
            {
                endTest();
            }
        }
    }

    @Order(7)
    @Test
    void testLogout() {
        try {
            startTest("Logout");
            AccManage.Logout(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Logout failed: " + e.getMessage());
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
    static void tearDown() {
        if (extent != null) {
            extent.flush();
        }
    }
}
