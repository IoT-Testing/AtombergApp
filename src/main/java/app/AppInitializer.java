package app;

import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class AppInitializer {
    private AppiumDriver driver;

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
        cap.setCapability("apksigner", "/Users/himanshuchoudhary/apksigner.jar");
        URL url = null;
        try {
            url = new URL("http://localhost:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        driver = new AppiumDriver(url, cap);
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        AppUtil.captureScreenshot(driver);
    }

    public void checkMainScreen() {
        boolean isMainScreenDisplayed = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" + " with Atomberg\"]")).isDisplayed();
        if (isMainScreenDisplayed) System.out.println("Main Screen Displayed");
        else System.out.println("No main screen yet");
    }

    public void login() {
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
        try {
            sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        PermissionUtil.allow(driver);
    }
}
