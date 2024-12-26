package AtombergTest; //To check 


//import Supports.GoogleHome;
import app.Analytics.Analytics;
import app.AppInitializer;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.*;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Login.*;

import static app.util.ActionsUtil.sleep;

public class Atomberg {
	public static AppiumDriver driver;

	public static void main(String[] args) {
		AppInitializer appInitializer = new AppInitializer();
		appInitializer.openApp();
		driver= appInitializer.getDriver();
		appInitializer.checkMainScreen();
		WebElement AddButton = null;
		try {
			AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
		} catch (Exception ignored) {}
		if (AddButton != null) {
			AddButton.click();
			sleep(1000);
		} else {
			ActionsUtil.Tap.withCoordinates(driver, 540, 1850);
			sleep(1000);
		}
		System.out.println("Searching for Available devices");
		connectButton(driver);
	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
	// connecting with the appium server and opening the app
	private static void connectButton(AppiumDriver driver){
		try {
			// Locate all elements with "Atomberg Smart Water Purifier"
			List<WebElement> deviceElements = driver.findElements(By.xpath("//android.view.View[@content-desc=\"Atomberg Smart Water Purifier\"]"));
			if (deviceElements.isEmpty()) {
				System.out.println("No elements found with content-desc 'Atomberg Smart Water Purifier'.");
				return;
			}
			// Iterate over the list to find the correct device and its sibling "Connect" button
			System.out.println(deviceElements.size());
			for (WebElement device : deviceElements) {
				// Get the parent container of the target element
				WebElement parentElement = device.findElement(By.xpath("./.."));
				// Locate the "Connect" button within the same parent container
				WebElement connectButton = parentElement.findElement(By.xpath(
						".//android.view.View[@content-desc=\"Connect\"]"
				));
				// Click the "Connect" button
				connectButton.click();
				System.out.println("Clicked on the 'Connect' button next to 'Atomberg Smart Water Purifier'.");
				break; // Exit the loop after the first match
			}

		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
}
