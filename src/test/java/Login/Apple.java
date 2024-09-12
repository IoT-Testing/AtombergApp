package Login;
//Add First Device

import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import Permissions.Permission;
import io.appium.java_client.AppiumDriver;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Apple {

	public static void Login(AppiumDriver driver) // Main
	{
		WebElement AppleLogin = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[1]")); 
		AppleLogin.click();
		Method.captureScreenshot(driver);
		sleep(5000);

		WebElement Home = null;
		try {
			Home = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		} catch (Exception exp) {
		}
		if (Home != null) {
			Permission.Allow(driver);
		} else {
			WebElement emailField = driver.findElement(By.className("android.widget.EditText"));
			emailField.sendKeys("bhagatrb4174@gmail.com"); // Enter Email id
			Method.captureScreenshot(driver);
			emailField.getText();
			System.out.println(" " + emailField.getText() + " ");
			sleep(1000);
			System.out.println("Email Entered...");
			Method.captureScreenshot(driver);
			sleep(1000);

			WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
			continueButton.click(); // Continue button //android.widget.Button[@text="Continue"]
			sleep(2000);
			Method.captureScreenshot(driver);

			WebElement passwordField = driver.findElement(By.xpath(
					"//android.webkit.WebView[@text=\"Sign in with Apple ID\"]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]/android.view.View/android.widget.EditText"));
			passwordField.click();
			passwordField.sendKeys("SumitaBH@133");
			Method.captureScreenshot(driver);
			System.out.println("Password entered..."); // Enter Password

			WebElement SignIn = driver.findElement(By.xpath("//android.widget.Button[@text=\"Sign In\"]"));
			SignIn.click(); // Continue to Log in
			sleep(2000);
			System.out.println("On Home Screen");
			Method.captureScreenshot(driver);
			WebElement continueButton1 = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
			continueButton1.click(); // Continue button //android.widget.Button[@text="Continue"]
			sleep(10000);
			Method.captureScreenshot(driver);

			WebElement appLogo =driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
			assert appLogo.isDisplayed();
			await().atMost(10, TimeUnit.SECONDS).until(appLogo::isDisplayed);
			System.out.println("Test Passed");
			Permission.Allow(driver);
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