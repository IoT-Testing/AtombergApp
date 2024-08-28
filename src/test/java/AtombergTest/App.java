package AtombergTest; //To check 

import Devices.Add;
import Devices.Select;
import Login.Email;
import Tabs.Analytics;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static Tabs.Automation.switchFamily;
        /*
          @author Rohit B. Bhagat
         */

public class App {
    public static IOSDriver driver;

    public static void main(String[] args) {
        try {
            openAtomberg();
//          Email.Login(driver);
            Thread.sleep(2000);
            Analytics.Show(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void openAtomberg() {
        XCUITestOptions options = new XCUITestOptions();
        options.setAutomationName("XCUITest");
        options.setCapability("platformName", "iOS");
        options.setCapability("platFormVersion", "17.5.1");
        options.setCapability("udid", "00008120-001E55EA3481A01E");
        options.setCapability("bundleId", "com.atomberg.app");
        options.setCapability("allow-cors", "true");
        options.setCapability("usePrebuiltWDA", true);

        try {
            System.out.println("Initializing IOS driver...");
            // Check if the Appium driver is initialized
            URL url = null;
            try {
                url = new URL("http://127.0.0.1:4723/");
            }catch (Exception e){
                e.printStackTrace();
            }
            assert url !=null;
            driver = new IOSDriver(url, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//            Screen.recordStart();  //Screen Recording only works in those mobiles which have screenRecording tool in the device OS
            System.out.println("iOS driver initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);
    }

    static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

}


