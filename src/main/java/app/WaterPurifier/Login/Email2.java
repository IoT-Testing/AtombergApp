package app.WaterPurifier.Login;

//Add First Device

import Actions.Tap;
import AtombergTest.Method;
import Permissions.Permission;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Email2 {
	public static AndroidDriver driver;
	public static void Login(AndroidDriver driver,String login, String pass) // Main
	{

		WebElement emailLoginButton = driver.findElement(By.xpath(
				"//android.widget.ScrollView/android.widget.ImageView[4]"));
		emailLoginButton.click();

		Method.captureScreenshot(driver);
		WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
		emailField.click();
		Tap.withCoordinates(driver, 300, 250);
		emailField.sendKeys(login); // Enter Email id
		Method.captureScreenshot(driver);
		emailField.getText();
		System.out.println("" + emailField.getText() + "");
		System.out.println("Email Entered...");
		Method.captureScreenshot(driver);

		WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
		continueButton.click(); // Continue button
		Method.captureScreenshot(driver);
		sleep(1000);
		WebElement passwordField = driver
				.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[1]\r\n" + ""));
		passwordField.click();
		passwordField.sendKeys(pass);

		Method.captureScreenshot(driver);
		System.out.println("Password entered..."); // Enter Password


		WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
		continueButton1.click(); // Continue to Log in
		System.out.println("Continue...");
		WebElement appLogo =driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		await().atMost(10, TimeUnit.SECONDS).until(appLogo::isDisplayed);
		System.out.println("Test Passed");

		Permission.Allow(driver);

	}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}