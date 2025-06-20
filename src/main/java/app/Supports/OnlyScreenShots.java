package app.Supports;
//Add First Device

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities; //Selenium Dependencies for Mobile capabilities
import org.testng.Assert;

import AtombergTest.Method;
import io.appium.java_client.android.AndroidDriver;

public class OnlyScreenShots {
	public static AndroidDriver driver;

	public static void main(String[] args) {
		try {
			openAtomberg();
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
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
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://192.168.8.255:4723/wd/hub"); // URL of the Appium session
			driver = new AndroidDriver(url, cap);
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Assert.fail("Expected element to click not found");
			Method.captureScreenshot(driver);
			e.printStackTrace();
			return;
		}
		Method.captureScreenshot(driver);
		List<WebElement> el = driver.findElements(By.xpath("//android.widget.ImageView"));
		System.out.println("List Size: " + el.size() + "\n");

		for (WebElement element : el) {
			System.out.println("Element text: " + element.getDomAttribute("bounds") + "\n");
		}
		sleep(500);

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
}