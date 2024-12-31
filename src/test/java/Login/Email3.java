package Login;

//Add First Device

import AtombergTest.Method;
import Permissions.Permission;
import Supports.TempMail;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

public class Email3 {
    public static AndroidDriver driver;

    public static void Login(AndroidDriver driver) {//Main

        WebElement emailLoginButton = driver.findElement(By.xpath(
                "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();

        Method.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys(TempMail.mail); // Enter Email id
        Method.captureScreenshot(driver);
        emailField.getText();
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        Method.captureScreenshot(driver);

        WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        Method.captureScreenshot(driver);
        sleep(1000);
        WebElement passwordField = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[1]"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@123");
        WebElement passwordField2 = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[2]"));
        passwordField.click();                                                  //android.widget.ScrollView/android.widget.EditText[2]
        passwordField2.sendKeys("Atomberg@123");

        Method.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password


        WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");

        TempMail.OTP();
        WebElement otp = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[1]"));
        otp.sendKeys(TempMail.OTP);

        driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Verify OTP\"]")).click();
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }


}