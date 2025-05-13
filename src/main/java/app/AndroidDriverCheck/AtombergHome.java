package app.AndroidDriverCheck;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static java.lang.Thread.sleep;

public class AtombergHome {
    public static AndroidDriver atomberg;
    public static void main(String[] args) throws IOException, UnsupportedFlavorException, InterruptedException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setAppPackage("com.atomberg.app");
        options.setAppActivity("com.atomberg.app.MainActivity");
//        options.setUdid("24c04d85");
        URL url = null;
        try {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        } catch (MalformedURLException e) {
            System.out.println("Malformed URL exception " + e.getMessage());
        }
        assert url != null;
        atomberg = new AndroidDriver(url, options);
        atomberg.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(atomberg);
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        ActionsUtil.sleep(1500);
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
