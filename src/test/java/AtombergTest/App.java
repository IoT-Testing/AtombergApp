package AtombergTest; //To check 

import Actions.Tap;
import Login.TempMail;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/*
  @author Rohit B. Bhagat
 */
public class App {
    public static AppiumDriver driver;

    public static void main(String[] args) {
        try {
            /*openAtomberg();*/
            /*Guest.Mode(driver);*/
            TempMail.Email();
            TempMail.OTP();
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
        } catch (Exception e) {
        }
        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }

    static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

    public static void Fan(AppiumDriver driver) {

        WebElement AddButton = null;
        try {
            AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
        } catch (Exception exp) {
        }
        //Check if there are already fans present in the family of not
        if (AddButton != null) {
            AddButton.click();
            sleep(1000);
        } else {
            // if there are no devices added in the family the tap with the coordinates of the add button(xpath vaue is not available)
            Tap.withCoordinates(driver, 540, 1940);
            sleep(1000);
        }
        System.out.println("Searching for Available devices");
        sleep(15000);

        WebElement frame = driver.findElement(By.xpath("//android.widget.ScrollView"));
        driver.switchTo().frame(frame);

        WebElement elementWithinFrame = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[1]"));
        String name = elementWithinFrame.getAttribute("content-desc");
        System.out.println(name);
        driver.switchTo().defaultContent();

        List<WebElement> elements = driver.findElements(By.xpath("//android.widget.ScrollView"));
        for (WebElement e : elements) {
            System.out.println(e.getAttribute("content-desc"));
        }
    }
}


