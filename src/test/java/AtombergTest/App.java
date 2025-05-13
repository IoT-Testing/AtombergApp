package AtombergTest; //To check 

import java.net.URL;
import java.time.Duration;

import app.ServerInitializer;
import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.ScreenRecording;
import org.awaitility.Awaitility;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;
import java.util.stream.Collectors;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import static app.AndroidDriverCheck.AtombergHome.atomberg;

/*
  @author Rohit B. Bhagat
 */
public class App {
    public static AndroidDriver driver;
    int i;
    String fam1;
    List<WebElement> elements;
    WebElement element;
    int total;

    public static void main(String[] args) {
        try {
            ServerInitializer server = new ServerInitializer();
            ScreenRecording recording = new ScreenRecording(server.service.getUrl());
            recording.start();
            ActionsUtil.SSleep(5);
            recording.stop();
       } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void initializeDriver() {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        try {
            System.out.println("Initializing Appium driver...");
            URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
            App.driver = new AndroidDriver(url, cap);
            App.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            System.out.println("Appium driver initialized.");
        } catch (MalformedURLException e) {
            System.out.println("Error initializing Appium driver: " + e.getMessage());
            Method.captureScreenshot(driver);
            e.printStackTrace();
            sleep(1000);
            System.out.println("Atomberg App Opened...");
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        sleep(6000);
    }

    static void tapOnAppLogo(){
    //Prerequisites : Atomberg App should be on Home screen.
        ActionsUtil.Tap.withCoordinates(driver, 550, 2350);
        List<WebElement> elementList = driver.findElements(By.className("android.widget.TextView"));
        List<WebElement> apps = elementList.stream().filter(object->object.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for(WebElement app : apps){
            if(Objects.equals(app.getDomAttribute("content-desc"), "Atomberg Home")){
               app.click();
               break;
            }
        }
      ActionsUtil.sleep(5000);
      AppUtil.captureScreenshot(driver);
   }

    static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

    void totalFamilies(){
        elements = driver.findElements(By.className("android.view.View"));
        element = elements.get(0);
        fam1 = element.getDomAttribute("content-desc");
        System.out.println(element.getDomAttribute("content-desc"));
        element.click();
        // tap on the Family name on the screen (top right corner)
        // getting the available family list
        List<WebElement> rawFamilies = driver.findElements(By.className("android.view.View"));
        List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        FAMILIES.remove(FAMILIES.size() - 1);
        FAMILIES.remove(FAMILIES.size() - 1);
        total = FAMILIES.size();
        System.out.println("number of FAMILIES present =" + total);
    }

    void repeatStep(){
        if (i < total - 1) {
            elements = driver.findElements(By.className("android.view.View"));
            element = elements.get(i);
            fam1 = element.getDomAttribute("content-desc");
            element.click();
        }
    }

    int clickOnFamily(){
            List<WebElement> rawFamily = driver.findElements(By.className("android.view.View"));
            List<WebElement> families = rawFamily.stream().filter(fam -> fam.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println("number of families present =" + total);
            families.remove(families.size() - 1);
            families.remove(families.size() - 1);
            System.out.println("number of families1 present =" + total);
            if (Objects.equals(families.get(i).getDomAttribute("content-desc"), fam1)){
                driver.navigate().back();
            }
            else{
                families.get(i).click();
            }
            System.out.println(families.get(i).getDomAttribute("content-desc") + " is clicked");
            ActionsUtil.sleep(1500);
            return i;
    }
}


