package ZTests;

import Devices.SO;
import Login.Email;
import MoreTab.AccManage;
import Supports.Screen;
import Tabs.Analytics;
import com.aventstack.extentreports.ExtentReports;
import io.appium.java_client.AppiumDriver;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import AtombergTest.Method;
import static ZTests.ExtentReportAT.endTest;
import static ZTests.ExtentReportAT.startTest;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    public static AppiumDriver driver;
    public static final ExtentReports extent = ExtentReportAT.getReportObjects();

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("......");
    }

    public static void openAtomberg() {
        DesiredCapabilities cap = new DesiredCapabilities();

        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
        cap.setCapability("apksigner",
                "C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
        try {
            System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized

            URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
            driver = new AppiumDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            Screen.recordStart();
            System.out.println("Appium driver initialized.");
        } catch (IOException | InterruptedException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Assert.assertTrue(driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
                    " with Atomberg\"]")).isDisplayed());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("Atomberg App Opened...");
        assert driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
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
    void testFanControl() {
        try {
            startTest("Fan Control");
            SO.Fan(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Fan Control failed: " + e.getMessage());
        } finally {
            endTest();
            Screen.recordStop();
        }
    }

    @Order(4)
    @Test
    void testAnalytics() {
        try {
            startTest("Analytics");
            Screen.recordStart();
            Analytics.Show(driver);
        } catch (Exception e) {

            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Analytics failed: " + e.getMessage());
        } finally {
            endTest();
            Screen.recordStop();
        }
    }

    @Order(5)
    @Test
    void testLogout() {
        try {
            startTest("Logout");
            Screen.recordStart();
            AccManage.Logout(driver);
        } catch (Exception e) {
            ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Logout failed: " + e.getMessage());
        } finally {
            endTest();
        }
    }

    @Order(6)
    @Test
    void testDriverClose() {
        driver.quit();
        Screen.recordStop();
    }

    @AfterAll
    static void tearDown() {
        if (extent != null) {
            extent.flush();
        }
    }
}
