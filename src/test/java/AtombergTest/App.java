package AtombergTest; //To check 

import Login.Email;
import java.net.URL;
import java.time.Duration;
import app.util.ActionsUtil;
import org.awaitility.Awaitility;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.net.MalformedURLException;
import java.util.stream.Collectors;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

        /*
          @author Rohit B. Bhagat
         */

public class App {
    public static AppiumDriver driver;
    int i;
    String fam1;
    List<WebElement> elements;
    WebElement element;
    int total;

    public static void main(String[] args) {
        try {
            openAtomberg();
            Email.Login(driver);
            WebElement AddButton = null;
            try {
                AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
            } catch (Exception ignored) {}
            if (AddButton != null) {
                AddButton.click();
                sleep(1000);
            } else {
                ActionsUtil.Tap.withCoordinates(driver, 540, 1850);
                sleep(1000);
            }
            System.out.println("Searching for Available devices");
            for (int c = 0; c < 10; c++) {
                sleep(20000);
                WebElement element = null;
                String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Fan\"]";
                // Search Fan Only
                try {
                    element = driver.findElement(By.xpath(xpathExpression));
                } catch (NoSuchElementException ignored) {
                }

                if (element != null) // if device is available
                {
                    System.out.println("Fans Available");
                    List<WebElement> Connects = driver.findElements(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));
                    for (int i = 1; i <= Connects.size(); i++) {
                        WebElement Connect = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
                        System.out.println("Connect button at : " + i);
                        Connect.click();
                        List<WebElement> checkList = driver.findElements(By.className("android.view.View"));
                        List<WebElement> checkListElement = checkList.stream().filter(Object -> Object.getAttribute("content-desc")!=null).collect(Collectors.toList());
                        for(WebElement dialogueBox : checkListElement){
                            if(Objects.equals(dialogueBox.getAttribute("content-desc"), "Could not add the lock")){
                                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                                break;
                            } else if (Objects.equals(dialogueBox.getAttribute("content-desc"), "Device already paired")) {
                                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                                break;
                            } else if (Objects.equals(dialogueBox.getAttribute("content-desc"), "Connecting to the Lock...\nPlease don't press back button")) {
                                driver.navigate().back();
                                System.out.println("Back");
                                break;
                            } else if (Objects.equals(dialogueBox.getAttribute("content-desc"), "Could not reach\nthe device")){
                                System.out.println("Out of Reach");
                                driver.navigate().back();
                                driver.navigate().back();
                                System.out.println("Back");
                                break;
                            } else if (Objects.equals(dialogueBox.getAttribute("content-desc"), "Cannot connect to the device")) {
                                driver.navigate().back();
                                System.out.println("Back");
                                break;
                            } else break;
                        }
                        List<WebElement> newList = driver.findElements(By.className("android.view.View"));
                        List<WebElement> webElementList = newList.stream().filter(webElement -> webElement.getAttribute("content-desc")!=null).collect(Collectors.toList());
                        List<WebElement> identify =  webElementList.stream().filter(webElement -> Objects.equals(webElement.getAttribute("content-desc"), "Identify your device")).collect(Collectors.toList());
                        if(!identify.isEmpty()){
                            System.out.println("Breaking the loop");
                            break;}
                    }

                } else {
                    WebElement DD = null;//discovered devices
                    try {
                        DD = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[2]"));
                    } catch (Exception e) {System.out.println(e.getMessage());
                    }
                    if (DD != null) {
                        driver.findElement(By.xpath("//android.widget.Button")).click();
                    } else {
                        WebElement tryAgain = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
                        tryAgain.click();
                        System.out.println("Trying Again...");
                    }
                    continue;
                }
                System.out.println("Breaking the loop");
                break;
            }
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
            return;
        } catch (Exception e) {}
        System.out.println("Atomberg App Opened...");
        sleep(6000);
    }

    static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

    void totalFamilies(){
        elements = driver.findElements(By.className("android.view.View"));
        element = elements.get(0);
        fam1 = element.getAttribute("content-desc");
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
            if (Objects.equals(families.get(i).getAttribute("content-desc"), fam1)){
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


