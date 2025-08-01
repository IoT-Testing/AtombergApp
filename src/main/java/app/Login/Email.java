package app.Login;

//Add First Device
import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

public class Email {
    public AndroidDriver atomberg;

    public Email(AndroidDriver driver){
        this.atomberg = driver;
    }

    public void Login() {//Main

        WebElement emailLoginButton = atomberg.findElement(By.xpath(
                "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
//        assert  emailLoginButton.isDisplayed();
        emailLoginButton.click();

        AppUtil.captureScreenshot(atomberg);
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        emailField.sendKeys("teboham827@agaseo.com"); // Enter Email id
        AppUtil.captureScreenshot(atomberg);
        emailField.getText();
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(atomberg);

        WebElement continueButton = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton.click(); // Continue button
        AppUtil.captureScreenshot(atomberg);
        sleep(1001);
        WebElement passwordField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@123");

        AppUtil.captureScreenshot(atomberg);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");

        PermissionUtil.allow(atomberg);
        List<WebElement> dialogueBox = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> elementList = dialogueBox.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element : elementList){
            if (Objects.equals(element.getDomAttribute("content-desc"), "Use Alexa to control your smart fan(s) with voice"))
            {
                atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                sleep(10);
                break;
            }
        }
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
        sleep(1000);
        WebElement passwordField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        passwordField.click();
        passwordField.sendKeys("Atomberg@1234");
        AppUtil.captureScreenshot(atomberg);
        System.out.println("Password entered..."); // Enter Password
        WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        continueButton1.click(); // Continue to Log in
        System.out.println("Continue...");

    }

    public void email(String email, String password) {
        WebElement emailLoginButton = atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
        emailLoginButton.click(); //
        AppUtil.captureScreenshot(atomberg);
        WebElement emailField = atomberg.findElement(By.xpath("//android.widget.EditText"));
        emailField.click();
        ActionsUtil.sleep(1500);
        emailField.sendKeys(email); // Enter Email id
        AppUtil.captureScreenshot(atomberg);
        System.out.println(" " + emailField.getText() + " ");
        System.out.println("Email Entered...");
        AppUtil.captureScreenshot(atomberg);
        WebElement continueButton = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
        String continueButtonClick = continueButton.getDomAttribute("clickable");
        assert continueButtonClick != null;
        if (continueButtonClick.equals("true"))
        {
            continueButton.click(); // Continue button
            AppUtil.captureScreenshot(atomberg);
            sleep(1000);
            WebElement passwordField = atomberg.findElement(By.xpath("//android.widget.EditText"));
            passwordField.click();
            passwordField.sendKeys(password);
            AppUtil.captureScreenshot(atomberg);
            System.out.println("Password entered..."); // Enter Password
            WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
            continueButton1.click(); // Continue to Log in
            System.out.println("Continue...");
        }
        else {
            System.out.println(" Incorrect Email or Password");
            atomberg.navigate().back();
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}