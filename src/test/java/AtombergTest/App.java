package AtombergTest; //To check 

import Login.Email3;
import MoreTab.*;
import Login.Email;
import java.net.URL;
import java.time.Duration;

import Supports.TempMail;
import Tabs.Analytics;
import app.util.ActionsUtil;
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

        /*
          @author Rohit B. Bhagat
         */

public class App {
    public static AppiumDriver driver;

    public static void main(String[] args) {
        try {
//            TempMail.Email();
            openAtomberg();
            Email.Login(driver);
            deleteAutomations(driver);

        } catch (Exception e) {
            e.printStackTrace();
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
        } catch (Exception e) {}
        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }

    static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

    private static void deleteAutomations(AppiumDriver driver){
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automations\"]")).click();
        List<WebElement> Elements = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> Elements2 = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        List<WebElement> elements = Elements2.stream().filter(element -> element.getAttribute("content-desc").startsWith("Automation")).collect(Collectors.toList());
        for (WebElement e:elements){
            System.out.println(e.getAttribute("content-desc"));
            e.click();
            driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button")).click();
            driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]")).click();
            System.out.println("Automation Deleted");
            ActionsUtil.sleep(2500);

        }
    }


}


