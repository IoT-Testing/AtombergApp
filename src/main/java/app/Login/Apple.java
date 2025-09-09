package app.Login;
//Add First Device
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Apple {

	public static void Login(AndroidDriver atomberg) // Main
	{
		WebElement AppleLogin = atomberg.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[1]")); 
		AppleLogin.click();
		AppUtil.captureScreenshot(atomberg);
		sleep(5000);

		WebElement Home = null;
		try {
			Home = atomberg.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		} catch (Exception ignored) {
		}
		if (Home != null) {
			PermissionUtil.allow(atomberg);
		} else {
			WebElement emailField = atomberg.findElement(By.className("android.widget.EditText"));
			emailField.sendKeys("bhagatrb4174@gmail.com"); // Enter Email id
			AppUtil.captureScreenshot(atomberg);
			emailField.getText();
			System.out.println(" " + emailField.getText() + " ");
			sleep(1000);
			System.out.println("Email Entered...");
			AppUtil.captureScreenshot(atomberg);
			sleep(1000);

			WebElement continueButton = atomberg.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
			continueButton.click(); // Continue button //android.widget.Button[@text="Continue"]
			sleep(2000);
			AppUtil.captureScreenshot(atomberg);

			WebElement passwordField = atomberg.findElement(By.xpath(
					"//android.widget.EditText[@resource-id=\"password_text_field\"]"));
			passwordField.click();
			passwordField.sendKeys("SumitaBH@133");
			AppUtil.captureScreenshot(atomberg);
			System.out.println("Password entered..."); // Enter Password

			WebElement SignIn = atomberg.findElement(By.xpath("//android.widget.Button[@text=\"Sign In\"]"));
			SignIn.click(); // Continue to Log in
			sleep(2000);
			System.out.println("On Home Screen");
			AppUtil.captureScreenshot(atomberg);
			WebElement continueButton1 = atomberg.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
			continueButton1.click(); // Continue button //android.widget.Button[@text="Continue"]
			sleep(10000);
			AppUtil.captureScreenshot(atomberg);

			WebElement appLogo =atomberg.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
			assert appLogo.isDisplayed();
			await().atMost(10, TimeUnit.SECONDS).until(appLogo::isDisplayed);
			System.out.println("Test Passed");
			PermissionUtil.allow(atomberg);
		}
	}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException ignored) {
		}
	}
}