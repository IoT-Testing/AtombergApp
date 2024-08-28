package Login;

//Add First Device

import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;
import java.util.concurrent.TimeUnit;

public class Email {
    public static IOSDriver driver;

    public static void Login(IOSDriver driver) {//Main

        WebElement emailLoginButton = driver.findElement(By.xpath(
                "//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeImage[4]"));
        emailLoginButton.click();

        Method.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Email\"]"));
        emailField.click();
        emailField.sendKeys("Weker42331@huleos.com"); // Enter Email id
        Method.captureScreenshot(driver);
        emailField.getText();
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        Method.captureScreenshot(driver);

        WebElement continueButton = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]"));
        continueButton.click(); // Continue button
        Method.captureScreenshot(driver);
        sleep(1000);

        WebElement passwordField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Enter new password\"]"));
        driver.findElement(By.xpath("//XCUIElementTypeApplication[@name=\"Atomberg Home\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[3]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeImage[2]")).click();
//        passwordField.click();
        sleep(1000);
        passwordField.sendKeys("Atomberg@123");
        Method.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password

        WebElement continueButton1 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");


//        Permission.Allow(driver);

    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }


}