package browserstack;


import AtombergTest.Monkey;
import Login.Email;
import app.MoreTab.Manage;
import app.ScreenCheck.ScreenCheck;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.MutableCapabilities;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.net.URL;
import java.time.Duration;


public class FirstTest extends AppiumTest {
    public AndroidDriver driver;
    public IOSDriver check;
@Test
    public void test() throws Exception {
        Manage manage = new Manage(driver);
        ScreenCheck screen = new ScreenCheck(driver);
        Email.Login(check);
        Monkey.run(driver);
    }

    @BeforeMethod(alwaysRun=true)
    public void setUp() throws Exception {
        MutableCapabilities capabilities = new UiAutomator2Options();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("appPackage", "com.atomberg.app");
        capabilities.setCapability("automationName", "UiAutomator2");
        capabilities.setCapability("appActivity", "com.atomberg.app.MainActivity");
        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"),capabilities);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterMethod(alwaysRun=true)
    public void tearDown(){
        driver.quit();
    }
}
