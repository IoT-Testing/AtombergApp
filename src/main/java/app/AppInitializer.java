package app;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class AppInitializer {
    public AppiumDriver driver;

    public AppiumDriver getDriver() {
        return driver;
    }

    public void setDriver(AppiumDriver driver) {
        this.driver = driver;
    }

    public void openApp() {
        //Capabilities to bet set According to the device to be tested
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        //Below caps opens Atomberg Home app from Scratch as if freshly installed.
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
        cap.setCapability("fullReset", "true");

        URL url = null;
        try {
            //Prerequisite: Turn on the Appium Server
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        driver = new AppiumDriver(url, cap);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        ActionsUtil.sleep(5000);
        AppUtil.captureScreenshot(driver);
    }

    public void checkMainScreen() {
        WebElement  isMainScreenDisplayed = null;
        try{
            isMainScreenDisplayed = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" + " with Atomberg\"]"));
        }catch(Exception ignored){}
        if (isMainScreenDisplayed != null) {
            System.out.println("Main Screen Displayed");
            Login login = new Login(driver);
            login.email();
        }
        else {
            System.out.println("Already logged in");
        }
    }

    public void initializeDriver(){
        //These caps only get the device, need to select Atomberg Home ap separately
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");

        URL url = null;
        try {
            url = new URL("http://192.168.9.234:7100/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        driver = new AppiumDriver(url, cap);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void initializeApkFile(){
        //These caps only get the device, need to select Atomberg Home ap separately
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        cap.setCapability("noReset", "true");
        String appPath = System.getProperty("user.dir") + "\\new_flutter_version.apk";
        cap.setCapability("app",appPath);

        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        driver = new AppiumDriver(url, cap);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    public void tapOnAppLogo(){
        //Prerequisites : Atomberg App should be on Home screen.
        ActionsUtil.Tap.withCoordinates(driver, 550, 2350);
        List<WebElement> elementList = driver.findElements(By.className("android.widget.TextView"));
        List<WebElement> apps = elementList.stream().filter(object->object.getAttribute("content-desc")!=null).collect(Collectors.toList());
        for(WebElement app : apps){
            if(app.getAttribute("content-desc").equals("Atomberg Home")){
                app.click();
                break;
            }
        }
        ActionsUtil.sleep(5000);
        AppUtil.captureScreenshot(driver);
    }

    public void login(String login, String pass) {
        WebElement emailLoginButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys(login); // Enter Email id
        AppUtil.captureScreenshot(driver);
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(driver);
        WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        AppUtil.captureScreenshot(driver);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement passwordField = driver.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys(pass);
        AppUtil.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");
        ActionsUtil.sleep(5000);
        PermissionUtil.allow(driver);
        List<WebElement> dialogueBox = driver.findElements(By.className("android.view.View"));
        List<WebElement> elementList = dialogueBox.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element : elementList){
            if (Objects.equals(element.getAttribute("content-desc"), "Use Alexa to control your smart fan(s) with voice"))
            {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                break;
            }
        }
    }

    public void email() {
        WebElement emailLoginButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys("hiwitaw422@wuzak.com"); // Enter Email id
        AppUtil.captureScreenshot(driver);
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(driver);
        WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        AppUtil.captureScreenshot(driver);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement passwordField = driver.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@1234");
        AppUtil.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");
        ActionsUtil.sleep(5000);
        PermissionUtil.allow(driver);
    }
}
