package Login;

//Add First Device

import Actions.Tap;
import ZTests.AppTest;
import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import Permissions.Permission;
import io.appium.java_client.AppiumDriver;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Email {
    public static AppiumDriver driver;


    public static boolean Login(AppiumDriver driver) {//Main

        WebElement emailLoginButton = driver.findElement(By.xpath(
                "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click();

        Method.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys("hiwitaw422@wuzak.com"); // Enter Email id
        Method.captureScreenshot(driver);
        emailField.getText();
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        Method.captureScreenshot(driver);

        WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        Method.captureScreenshot(driver);
        sleep(1000);
        WebElement passwordField = driver.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@1234");

        Method.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password


        WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");
        WebElement incorrect = null;
        try{
            incorrect = driver.findElement(By.xpath("//android.view.View[@content-desc=\"! Incorrect password\"]"));
        }catch(Exception e){}
        if (incorrect==null) {
            Tap.withCoordinates(driver, 540, 2150);
            sleep(1000);
            WebElement appLogo = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
            await().atMost(10, TimeUnit.SECONDS).until(appLogo::isDisplayed);
            System.out.println("Test Passed");
            Permission.Allow(driver);

            return AppTest.ITestResult = true;
        }else {
            System.out.println("Login Failed as the password is incorrect");
            return AppTest.ITestResult = false;
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }


}