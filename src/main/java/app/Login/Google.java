package app.Login;
//Add First Device

import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class Google {
	public static AndroidDriver driver;

	public static void Login(AndroidDriver driver) // Main
	{
		WebElement GoogleLoginButton = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[2]"));
		GoogleLoginButton.click();
		sleep(2000);
		AppUtil.captureScreenshot(driver);
		WebElement Home = null;
		try {
			Home = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		} catch (Exception ignored) {
		}
		if (Home != null) {
			PermissionUtil.allow(driver);
		} else {
			waitForElementAndClick(driver, By.xpath(
					"//android.webkit.WebView[@text=\"Sign in - Google Accounts\"]/android.view.View/android.view.View[4]"));
			sleep(10000);
			System.out.println("On Home Screen");
			AppUtil.captureScreenshot(driver);
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
		} catch (Exception ignored) {}
	}

	private static void waitForElementAndClick(AndroidDriver driver, By by) {
		Awaitility.await().atMost(Duration.ofSeconds(60)).pollInterval(Duration.ofMillis(500)).until(() -> {
			WebElement element = driver.findElement(by);
			element.click();
			return true;
		});
	}
}