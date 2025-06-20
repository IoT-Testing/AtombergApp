package app.Login;

//Add First Device

import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Email {
    public static AndroidDriver driver;

    public static void Login(AndroidDriver driver) {//Main

        WebElement emailLoginButton = driver.findElement(By.xpath(
                "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
//        assert  emailLoginButton.isDisplayed();
        emailLoginButton.click();

        Method.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys("teboham827@agaseo.com"); // Enter Email id
        Method.captureScreenshot(driver);
        emailField.getText();
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        Method.captureScreenshot(driver);

        WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        Method.captureScreenshot(driver);
        sleep(1001);
        WebElement passwordField = driver.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@123");

        Method.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");

        PermissionUtil.allow(driver);
        List<WebElement> dialogueBox = driver.findElements(By.className("android.view.View"));
        List<WebElement> elementList = dialogueBox.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element : elementList){
            if (Objects.equals(element.getDomAttribute("content-desc"), "Use Alexa to control your smart fan(s) with voice"))
            {
                driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                sleep(10);
                break;
            }
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}