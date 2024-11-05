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
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", "14");
        cap.setCapability("appPackage", "com.atomberg.app");
        cap.setCapability("appActivity", "com.atomberg.app.MainActivity");

        URL url = null;
        try {
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
        boolean isMainScreenDisplayed = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" + " with Atomberg\"]")).isDisplayed();
        if (isMainScreenDisplayed) System.out.println("Main Screen Displayed");
        else System.out.println("No main screen yet");
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
        PermissionUtil.allowForBrowswerStack(driver);
        List<WebElement> dialogueBox = driver.findElements(By.className("android.view.View"));
        List<WebElement> elementList = dialogueBox.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element : elementList){
            if (element.getAttribute("content-desc").equals("Use Alexa to control your smart fan(s) with voice"))
            {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                break;
            }
        }
    }
}
