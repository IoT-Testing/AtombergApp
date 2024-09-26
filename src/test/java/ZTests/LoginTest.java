package ZTests;

import AtombergTest.Method;
import Login.Email2;
import MoreTab.AccManage;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import static ZTests.ExtentReportAT.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

//@ExtendWith(TestWatcherExample.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginTest {
    public static AppiumDriver driver;
    public static boolean LoginResult;
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
            driver = new AppiumDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("Appium driver initialized.");
        } catch (IOException e) {
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
    @Test // Opening the app
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
    @ParameterizedTest
    @CsvSource({"hiwitaw422@wuzak.com,Atomberg@134",
            "gawob65213@kkoup.com,Atomberg@123",
            /*"mopibo8392@trackden.com,Atomberg@123",
            "vodadi6751@wuzak.com,Atomberg@123",
            "hanoni8273@wikfee.com,Atomberg@098",
            "teboham827@agaseo.com,Atomberg@123",
            "mitim28961@godsigma.com,Atomberg@123",
            "weker42331@huleos.com,Atomberg@123"*/
    })
    void
    testLogin(String login, String password) throws Exception {
           try {
               startTest("Login");
               extent.setSystemInfo("Email Used", login);

               Email2.Login(driver, login, password);
               if (LoginResult) {
                   ExtentReportAT.getTest().log(Status.PASS, "Login Test Passed");
                   ExtentReportAT.test.get().pass("Screenshot captured", MediaEntityBuilder.createScreenCaptureFromPath(Method.ScreenShot).build());
               } else {
                   test.get().info(login);
                   extent.getStats();
                   ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Login failed as the password was incorrect");
                   ExtentReportAT.test.get().fail("Screenshot captured", MediaEntityBuilder.createScreenCaptureFromPath(Method.ScreenShot).build());
                   driver.navigate().back();
                   driver.navigate().back();
               }
           }catch(Exception e){
               e.printStackTrace();
           }
           finally {
            endTest();
           }
            testLogout();
    }


    void testLogout(){
        assumeTrue(LoginResult, "Login Failed");
        try{
        startTest("Logout");
        AccManage.Logout(driver);
        endTest();
    }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Order(3)
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
