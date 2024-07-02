package ZTests;

import Devices.*;
import Login.Email;
import com.aventstack.extentreports.ExtentReports;
import io.appium.java_client.AppiumDriver;
import ZTests.ExtentReportAT;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import AtombergTest.Method;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest {
    public static AppiumDriver driver;
    static ExtentReports extent = ExtentReportAT.getReportObjects();

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
            System.out.println("Appium driver initialized.");
        } catch (MalformedURLException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Assert.fail("Expected element to click not found");
            e.printStackTrace();
            return;
        }
        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }

    @Order(1)
    @Test
    void testOpenApp() {
        ExtentReportAT.getReportObjects();
        extent.createTest("Opening App");
        openAtomberg();
        Email.Login(driver);
        extent.flush();
    }
    @Order(2)
    @Test
    void testAddFan() {
        extent.createTest("Fan Addition");
        Add2.Fan(driver);
        Select.Fan(driver);
        sleep(3000);
        extent.flush();
    }
    @Order(3)
    @Test
    void testAddLock() {
        extent.createTest("Lock Addition");
        Add2.Lock(driver);
        Method.AddLock(driver);
    }
    @Order(4)
    @Test
    void testControlFan() {
        extent.createTest("Fan Control");
        SO.Fan(driver);
        extent.flush();
    }
    @Order(5)
//    @Test
//    void testControlLock() {
//        extent.createTest("Lock Control");
//        SO.Lock(driver);
//        extent.flush();
//    }
//    @Order(6)
    @Test
    void driverClose(){
        driver.quit();
    }

}
