package app.Login;

//Add First Device
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;

import AtombergTest.Method;
import Permissions.Permission;
import io.appium.java_client.android.AndroidDriver;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class FB {
	public static void Login(AndroidDriver driver) // Main
	{
		WebElement FBLoginButton = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[3]")); // Click
																																																																	// on
																																																																	// Login
																																																																	// with
																																																																	// Facebook
		FBLoginButton.click();
		sleep(10000);
		WebElement Home = null;
		try {
			Home = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		} catch (Exception exp) {
		}
		if (Home != null) {
			Permission.Allow(driver);
		} else {
			WebElement Continue = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue as Rohit\"]"));
			Continue.click(); // Enter Email id
			Method.captureScreenshot(driver);

			sleep(10000);
			System.out.println("On Home Screen");
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