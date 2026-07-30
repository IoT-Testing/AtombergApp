package app.WaterPurifier.Login;

//Add First Device
import app.util.PermissionUtil;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import app.util.AppUtil.*;
import app.util.PermissionUtil.*;
import io.appium.java_client.android.AndroidDriver;

import java.security.Permission;
import java.util.concurrent.TimeUnit;

import static app.util.AppUtil.captureScreenshot;
import static org.awaitility.Awaitility.await;

public class FB {
	public static void Login(AndroidDriver driver) // Main
	{
		WebElement FBLoginButton = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[3]"));
		FBLoginButton.click();
		sleep(10000);
		WebElement Home = null;
		try {
			Home = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		} catch (Exception exp) {
		}
		if (Home != null) {
			PermissionUtil.allow(driver);
		} else {
			WebElement Continue = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue as Rohit\"]"));
			Continue.click(); // Enter Email id
			captureScreenshot(driver, "Continue");
			sleep(10000);
			System.out.println("On Home Screen");
			captureScreenshot(driver, "Home Screen");
			WebElement appLogo =driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
			assert appLogo.isDisplayed();
			await().atMost(10, TimeUnit.SECONDS).until(appLogo::isDisplayed);
			System.out.println("Test Passed");
			PermissionUtil.allow(driver);
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