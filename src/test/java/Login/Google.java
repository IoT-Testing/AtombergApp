package Login;
//Add First Device

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import Permissions.Permission;
import io.appium.java_client.ios.IOSDriver;

import static org.awaitility.Awaitility.await;

public class Google {
	public static IOSDriver driver;

	public static void Login(IOSDriver driver) // Main
	{
		WebElement GoogleLoginButton = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeImage[2]"));
		GoogleLoginButton.click();
		sleep(2000);
		Method.captureScreenshot(driver);
		WebElement Home = null;
		try {
			Home = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText[1]/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText[1]/XCUIElementTypeImage"));
		} catch (Exception exp) {
		}
		if (Home != null) {
			Permission.Allow(driver);
		} else {
			waitForElementAndClick(driver, By.xpath(
					"//android.webkit.WebView[@text=\"Sign in - Google Accounts\"]/XCUIElementTypeStaticText/XCUIElementTypeStaticText[4]"));
			sleep(10000);
			System.out.println("On Home Screen");
			Method.captureScreenshot(driver);
			WebElement appLogo =driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText[1]/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText[1]/XCUIElementTypeImage"));
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

	private static void waitForElementAndClick(IOSDriver driver, By by) {
		Awaitility.await().atMost(Duration.ofSeconds(60)).pollInterval(Duration.ofMillis(500)).until(() -> {
			WebElement element = driver.findElement(by);
			element.click();
			return true;
		});
	}
}