package app;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import static java.lang.Thread.sleep;

public class Login {
    public AndroidDriver atomberg;

    public Login(AndroidDriver driver){
        this.atomberg = driver;
    }

    public void email() {
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click(); //
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

    public void emailForDebug() {
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();
        AppUtil.captureScreenshot(atomberg);
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
//        emailField.sendKeys("tiposi3964@nozamas.com"); // Enter Email id
//        AppUtil.captureScreenshot(driver);
//        System.out.println(" " + emailField.getText() + " ");
//        System.out.println("Email Entered...");
//        AppUtil.captureScreenshot(driver);
//        WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
//        continueButton.click(); // Continue button
//        AppUtil.captureScreenshot(driver);
//        try {
//            sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        WebElement passwordField = driver.findElement(By.xpath("//android.widget.EditText"));
//        passwordField.click();
//        passwordField.sendKeys("Atomberg@123");
//        AppUtil.captureScreenshot(driver);
//        System.out.println("Password entered..."); // Enter Password
//        WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
//        continueButton1.click(); // Continue to Log in
//        System.out.println("Continue...");
//        ActionsUtil.sleep(5000);
//        PermissionUtil.allow(driver);
    }
}
