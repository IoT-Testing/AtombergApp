package Login;

//Add First Device

import Actions.Tap;
import AtombergTest.Method;
import Permissions.Permission;
import ZTests.LoginTest;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.ITestResult;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Email2 {
	public static AppiumDriver driver;
	public static boolean Login(AppiumDriver driver,String login, String pass){

		WebElement emailLoginButton = driver.findElement(By.xpath(

				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
		emailLoginButton.click();

		Method.captureScreenshot(driver);
		WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
		emailField.click();
		Tap.withCoordinates(driver, 300, 250);
		emailField.sendKeys(login); // Enter Email id
		Method.captureScreenshot(driver);
		emailField.getText();
		System.out.println(" " + emailField.getText() + " ");
		System.out.println("Email Entered...");
		Method.captureScreenshot(driver);

		WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
		continueButton.click(); // Continue button
		Method.captureScreenshot(driver);
		sleep(5000);
		WebElement passwordField = driver.findElement(By.xpath("//android.widget.EditText"));
		passwordField.click();
		passwordField.sendKeys(pass);

		Method.captureScreenshot(driver);
		System.out.println("Password entered..."); // Enter Password

		WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
		continueButton1.click(); // Continue to Log in
		System.out.println("Continue...");
		sleep(1000);
		WebElement incorrect = null;
		try{
			incorrect = driver.findElement(By.xpath("//android.view.View[@content-desc=\"! Incorrect password\"]"));
		}catch(Exception e){}
		if (incorrect!=null) {
			Method.captureScreenshot(driver);
			System.out.println("Login Failed as the password is incorrect");
			return LoginTest.LoginResult = false;
		}else {
			Method.captureScreenshot(driver);
			System.out.println("Test Passed");
			Permission.Allow(driver);
			return LoginTest.LoginResult = true;
		}
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}