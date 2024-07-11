package AtombergTest; //To check 

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import Tabs.Automation;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import Actions.Tap;
import Login.Email;
import io.appium.java_client.AppiumDriver;

public class App {
    public static AppiumDriver driver;

    public static void main(String[] args) {
        /*
          @author Rohit
         */
        try {
            openAtomberg();
            Email.Login(driver);
            sleep(5000);
            Automation.TimeOfDay(driver);
            driver.close();
        } catch (Exception e) {
        }
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void openAtomberg() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
        cap.setCapability("apksigner",
                "C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tooBls\\34.0.0\\lib\\apksigner.jar");
        try {
            System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
            URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
            driver = new AndroidDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("Appium driver initialized.");
        } catch (MalformedURLException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Method.captureScreenshot(driver);
            e.printStackTrace();
            return;
        }
        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }

    public static void CDO(AppiumDriver driver) {
        WebElement DO = null;
        try {
            DO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.ImageView[1])"));
        } catch (Exception e) {
        }
        if (DO != null) {
            System.out.println("Fan Online");
        } else {
            System.out.println("Fan Offline");
        }
    }

    public static void AddButton(AppiumDriver driver) {
        System.out.println("searching element");
        List<WebElement> Elements = driver.findElements(By.className("android.widget.ImageView"));
        for (WebElement element : Elements) {
            if (element.equals(driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView")))) {
                driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView")).click();
            } else if (element.equals(driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]")))) {
                Tap.withCoordinates(driver, 540, 1940);
                sleep(1000);
            }
        }
    }

    public static void SwitchFamily(AppiumDriver driver) {
        List<WebElement> elements = driver.findElements(By.className("android.view.View"));
        WebElement element = elements.get(0);
        element.getAttribute("content-desc");
        element.click();
        System.out.println("Click on family");
        List<WebElement> familiesRaw = driver.findElements(By.className("android.view.View"));
        int i;
        List<WebElement> FAMILIES = familiesRaw.stream().filter(fam -> fam.getAttribute("content-desc") != null).collect(Collectors.toList());
        FAMILIES.remove(FAMILIES.size() - 1);
        for (WebElement family : FAMILIES) {
            System.out.println(family.getAttribute("content-desc"));
        }
        int total = FAMILIES.size();
        System.out.println(total);
        for (i = 0; i < total; i++) {
            System.out.println("i = " + i + " total = " + total);
            List<WebElement> families = driver.findElements(By.className("android.view.View"));
            System.out.println("..............");
            for (WebElement e : families) {
                System.out.println(e.getAttribute("content-desc"));
            }
            WebElement family = null;
            for (WebElement fam : families) {
                if (FAMILIES.get(i + 1).getAttribute("content-desc").equals(fam.getAttribute("content-desc"))) {
                    family = fam;
                    System.out.println("]]]]]]]]]]");
                }
            }
            assert family != null;
            System.out.println("Clicking on family " + family.getAttribute("content-desc"));
            family.click();
            System.out.println("ccccccccc");
//            if (driver.findElement(By.xpath("//android.view.View[@content-desc=\"Scan the below QR to join this family\"]")).isDisplayed()) {
//                driver.navigate().back();
            //continue;
//            }
            if (i < total - 1) {
                System.out.println("Rescan");
                elements = driver.findElements(By.className("android.view.View"));
                element = elements.get(0);
                element.getAttribute("content-desc");
                System.out.println(element.getAttribute("content-desc"));
                element.click();
                sleep(2500);

            }
           /* for (WebElement  : families) {
                System.out.println(family.getAttribute("content-desc"));
						if ((family.getAttribute("content-desc")) != null)
						{
							family.click();
							if(family.equals(driver.findElement(By.xpath("//android.view.View[@content-desc=\"Sweet Home\"]")))){
								continue;
							}
							if(driver.findElement(By.xpath("//android.view.View[@content-desc=\"Scan the below QR to join this family\"]")).isDisplayed()){
								driver.navigate().back();
							}
							if (i< total-1){
								elements = driver.findElements(By.className("android.view.View"));
								element = elements.get(0);
								element.getAttribute("content-desc");
								element.click();
								System.out.println("Rescan");
							}
						}

            }*/

        }

    }

    public static void deleteAutomation(AppiumDriver driver)
    {
        WebElement ToD = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Automation155806\n" +
                "1 fan, 10:30 AM\n" +
                "Frequency\n" +
                "Daily\n" +
                "Activity\n" +
                "Power Toggle\"]"));
        String AutoName = ToD.getAttribute("content-desc");
        boolean check = AutoName.startsWith("Automation");
        if (check)
        {
            ToD.click();
            WebElement delete = driver.findElement(By.xpath("//android.widget.ImageView/android.widget.Button"));
            delete.click();
            WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
            yes.click();
            assert driver.findElement(By.xpath("//android.view.View[@content-desc=\"Automation Removed Successfully\"]")).isDisplayed();
        }

    }
}


