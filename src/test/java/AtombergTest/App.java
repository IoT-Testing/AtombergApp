package AtombergTest; //To check 

import Login.Email;
import java.net.URL;
import java.time.Duration;

import Tabs.Analytics;
import org.awaitility.Awaitility;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
        /*
          @author Rohit B. Bhagat
         */
public class App {
    public static AppiumDriver driver;

    public static void main(String[] args) {
        try {
            openAtomberg();
            Email.Login(driver); // As in Android script, MainActivity i=of the app is mentioned, it starts from scratch
            Analytics.Show(driver);
        } catch (Exception e) {
        }
    }

    static void openAtomberg() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
        cap.setCapability("apksigner", "\"C:\\Users\\Rohit Bhagat\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar\"");
//        cap.setCapability("idleTimeout", 20);
        try {
            System.out.println("Initializing Appium driver...");
            // Check if the Appium driver is initialized
            URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
            driver = new AndroidDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//            Screen.recordStart();  //Screen Recording only works in those mobiles which have screen recording tool in the device OS
            System.out.println("Appium driver initialized.");
        } catch (MalformedURLException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Method.captureScreenshot(driver);
            e.printStackTrace();
            return;
        } catch (Exception e) {}
        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }

    static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

}


