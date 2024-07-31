package AtombergTest; //To check 

import Actions.Tap;
import Devices.SO;
import Login.Email;

import java.io.IOException;
import java.net.URL;
import MoreTab.AccManage;
import java.time.Duration;

import Supports.Screen;
import Tabs.MoreTab;
import Widget.Widgets;
import io.appium.java_client.serverevents.CustomEvent;
import org.awaitility.Awaitility;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;
import java.util.stream.Collectors;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

public class App {
    public static AppiumDriver driver;

    public static void main(String[] args) {
        /*
          @author Rohit
         */
        try {
            openAtomberg();
            Email.Login(driver);
            MoreTab.Options(driver);
//            WebElement moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
//                    "Tab 3 of 3\"]"));
//            moreTab.click();

            sleep(5000);
            driver.quit();
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
//            Screen.recordStart();  //Screen Recording only works in those mobiles which have screenrecording tool in the device OS
            System.out.println("Appium driver initialized.");
        } catch (MalformedURLException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Method.captureScreenshot(driver);
            e.printStackTrace();
            return;
        } catch (Exception e) {
        }
        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }
    static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}


