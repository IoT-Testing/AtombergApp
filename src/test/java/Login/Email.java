package Login;

//Add First Device

import app.util.PermissionUtil;
import io.appium.java_client.AppiumDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


public class Email {
    public static AppiumDriver driver;
    public static void Login(AppiumDriver driver) {//Main

        WebElement emailLoginButton = driver.findElement(By.xpath(
                "//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeImage[4]"));
//        assert  emailLoginButton.isDisplayed();
        emailLoginButton.click();

        Method.captureScreenshot(driver);
        WebElement emailField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Email\"]"));
        emailField.click();
        emailField.sendKeys("teboham827@agaseo.com"); // Enter Email id
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
        passwordField.click();
        passwordField.sendKeys("Atomberg@123");

        Method.captureScreenshot(driver);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");

        PermissionUtil.allowForBrowserStack(driver);
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

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}