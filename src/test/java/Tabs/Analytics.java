package Tabs;
//Add First Device

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import Actions.Swipe;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Analytics {
	public static void Show(AppiumDriver driver) {
		WebElement FanCheck = null;
		try {

			FanCheck = driver.findElement(
					By.xpath("//android.view.View[@content-desc=\"Please add a smart fan to view analytics\"]"));

		} catch (Exception Exp) {
		}
		if (FanCheck != null) {
			System.out.println("Add a Fan or Change Family");

			Tap.withPercentage(driver, 0.1, 0.92);
			Method.captureScreenshot(driver);
			sleep(1000);
			WebElement Family = driver.findElement(By.xpath("//android.view.View[@content-desc=\"My Home\"]"));
			Family.click();
			sleep(2000);

		}
		Method.captureScreenshot(driver);
		Tap.withPercentage(driver, 0.58, 0.139); // Bill Saved
		sleep(1000);
		System.out.println("Bill Saved");
		Method.captureScreenshot(driver);
		Tap.withCoordinates(driver, 300, 170);// Random Tap

		Swipe.Left(driver, 0.75, 0.50); // CO2 saved
		sleep(1000);
		Method.captureScreenshot(driver);
		Tap.withPercentage(driver, 0.58, 0.139); // nokia & narzo 0.65, 0.173 //Tab 0.58, 0.139
		sleep(1000);
		System.out.println("CO2 Saved");
		Method.captureScreenshot(driver);
		Tap.withCoordinates(driver, 300, 170);// Random Tap

		Swipe.Left(driver, 0.75, 0.50); // Energy Saved
		sleep(1000);
		Method.captureScreenshot(driver);
		Tap.withPercentage(driver, 0.59, 0.14);
		sleep(1000);
		System.out.println("Energy Saved");
		Method.captureScreenshot(driver);
		Tap.withCoordinates(driver, 300, 170);// Random Tap

		Swipe.Left(driver, 0.75, 0.50); // Runtime
		sleep(1000);
		Method.captureScreenshot(driver);
		System.out.println("Runtime");
		Tap.withPercentage(driver, 0.58, 0.139);
		sleep(1000);
		Method.captureScreenshot(driver);
		Tap.withCoordinates(driver, 300, 170);// Random Tap

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

}
