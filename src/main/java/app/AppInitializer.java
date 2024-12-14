package app;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
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
    public AppiumDriver atomberg;

    public AppiumDriver getDriver() {
        return atomberg;
    }

    public void setDriver(AppiumDriver driver) {
        this.atomberg = driver;
    }

    public void openApp(){
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage("com.atomberg.app");
        options.setAppActivity("com.atomberg.app.MainActivity");
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        atomberg = new AppiumDriver(url, options);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void checkMainScreen() {
        WebElement  isMainScreenDisplayed = null;
        try{
            isMainScreenDisplayed = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" + " with Atomberg\"]"));
        }catch(Exception ignored){}
        if (isMainScreenDisplayed != null) {
            System.out.println("Login Screen Displayed");
            Login login = new Login(atomberg);
            login.email();
        }
        else {
            System.out.println("Already logged in");
        }
    }

    public void initializeDriver(){
        //These caps only get the device, need to select Atomberg Home ap separately
        UiAutomator2Options options = new UiAutomator2Options();
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("appPackage", "com.atomberg.app");

        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        atomberg = new AppiumDriver(url, cap);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public void tapOnAppLogo(){
        //Prerequisites : Atomberg App should be on Home screen.
        ActionsUtil.Tap.withCoordinates(atomberg, 550, 2350);
        List<WebElement> elementList = atomberg.findElements(By.className("android.widget.ImageView"));
        List<WebElement> apps = elementList.stream().filter(object->object.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for(WebElement app : apps){
            if(Objects.equals(app.getDomAttribute("content-desc"), "Atomberg Home")){
                app.click();
                break;
            }
        }
        ActionsUtil.sleep(5000);
        AppUtil.captureScreenshot(atomberg);
    }

    public void login(String login, String pass) {
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(atomberg);
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys(login); // Enter Email id
        AppUtil.captureScreenshot(atomberg);
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(atomberg);
        WebElement continueButton = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        AppUtil.captureScreenshot(atomberg);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement passwordField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys(pass);
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");
        ActionsUtil.sleep(5000);
        PermissionUtil.allow(atomberg);
        List<WebElement> dialogueBox = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> elementList = dialogueBox.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element : elementList){
            if (Objects.equals(element.getDomAttribute("content-desc"), "Use Alexa to control your smart fan(s) with voice"))
            {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                break;
            }
        }
    }

    public void email() {
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(atomberg);
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys("hiwitaw422@wuzak.com"); // Enter Email id
        AppUtil.captureScreenshot(atomberg);
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(atomberg);
        WebElement continueButton = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        AppUtil.captureScreenshot(atomberg);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        WebElement passwordField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@1234");
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");
        ActionsUtil.sleep(5000);
        PermissionUtil.allow(atomberg);
    }
}
