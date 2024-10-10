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
    int i = 0;
    int total;
    String fam1;
    List<WebElement> elements;
    WebElement element;
    public static void main(String[] args) {
        try {
//            TempMail.Email();
            openAtomberg();
            Email.Login(driver);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void openAtomberg() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("automationName", "UiAutomator2");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
        cap.setCapability("apksigner", "\"C:\\Users\\Rohit Bhagat\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar\"");
//        cap.setCapability("idleTimeout", 20);
        try {
            System.out.println("Initializing Appium driver...");
            // Check if the Appium driver is initialized
            URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
            App.driver = new AndroidDriver(url, cap);
            App.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
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

    void totalFamilies(){
        elements = driver.findElements(By.className("android.view.View"));
        element = elements.get(0);
        String fam1 = element.getAttribute("content-desc");
        System.out.println(element.getAttribute("content-desc"));
        element.click();
        // tap on the Family name on the screen (top right corner)
        // getting the available family list
        List<WebElement> rawFamilies = driver.findElements(By.className("android.view.View"));
        List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getAttribute("content-desc") != null).collect(Collectors.toList());
        FAMILIES.remove(FAMILIES.size() - 1);
        FAMILIES.remove(FAMILIES.size() - 1);
        total = FAMILIES.size();
        System.out.println("number of FAMILIES present =" + total);
    }
    void repeatStep(){

        if (i < total - 1) {
            elements = driver.findElements(By.className("android.view.View"));
            element = elements.get(i);
            fam1 = element.getAttribute("content-desc");
            element.click();
        }

    }

    int clickOnFamily(){
            List<WebElement> rawFamily = driver.findElements(By.className("android.view.View"));
            List<WebElement> families = rawFamily.stream().filter(fam -> fam.getAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println("number of families present =" + total);
            families.remove(families.size() - 1);
            families.remove(families.size() - 1);
            System.out.println("number of families1 present =" + total);
            if (families.get(i).getAttribute("content-desc").equals(fam1)){
                driver.navigate().back();
            }
            else{
                families.get(i).click();
            }
            System.out.println(families.get(i).getAttribute("content-desc") + " is clicked");
            ActionsUtil.sleep(1500);
            return i;
    }
}


