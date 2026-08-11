package app.Supports;
//Add First Device

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.util.ActionsUtil;
import app.util.AppUtil;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities; //Selenium Dependencies for Mobile capabilities


import io.appium.java_client.android.AndroidDriver;

public class OnlyScreenShots {
	public static AndroidDriver driver;

	public static void main(String[] args) {
		try {
			openAtomberg();
		} catch (Exception exp) {
			logpoint(exp.getCause());
			logpoint(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void openAtomberg() // Main
	{
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("automationName", "UiAutomator2");
		cap.setCapability("deviceName", "POCO M6 Pro 5G");
		cap.setCapability("udid", "e5b51506054a");
		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "13");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
		cap.setCapability("dumpAppPackageInfo", true);
		try {
			logpoint("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://192.168.8.255:4723/wd/hub"); // URL of the Appium session
			driver = new AndroidDriver(url, cap);
			logpoint("Appium driver initialized.");
		} catch (MalformedURLException e) {
			logpoint("Error initializing Appium driver: " + e.getMessage());
			AppUtil.captureScreenshot(driver, "driver initialized");
			return;
		}
		AppUtil.captureScreenshot(driver,"driver successful");
		List<WebElement> el = driver.findElements(By.xpath("//android.widget.ImageView"));
		logpoint("List Size: " + el.size() + "\n");

		for (WebElement element : el) {
			logpoint("Element text: " + element.getDomAttribute("bounds") + "\n");
		}
		sleep(500);

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
}
