package AtombergTest; //To check 


//import Supports.GoogleHome;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import Login.*;

public class Atomberg {
	public static IOSDriver driver;

	public static void main(String[] args) {
		openAtomberg(); // Open App through Appium
		Email.Login(driver); // Login

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

	// connecting with the appium server and opening the app
	public static void openAtomberg() {
		MutableCapabilities caps = new MutableCapabilities();
		caps.setCapability("platformName", "Android");
		caps.setCapability("appium:app", "storage:filename=Atomberg Home.apk");  // The filename of the mobile app
		caps.setCapability("appium:deviceName", "Android GoogleAPI Emulator");
		caps.setCapability("appium:platformVersion", "12.0");
		caps.setCapability("appium:automationName", "UiAutomator2");
		MutableCapabilities sauceOptions = new MutableCapabilities();
		sauceOptions.setCapability("username", "oauth-bhagatrb979-88f72");
		sauceOptions.setCapability("accessKey", "a5a5c0e7-2283-4857-8b38-6bf936ecc990");
		sauceOptions.setCapability("build", "appium-build-G6Y05");
		sauceOptions.setCapability("name", "<your test name>");
		sauceOptions.setCapability("deviceOrientation", "PORTRAIT");
		caps.setCapability("sauce:options", sauceOptions);
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("https://ondemand.eu-central-1.saucelabs.com:443/wd/hub");
			IOSDriver driver = new IOSDriver(url, caps);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
//			Method.captureScreenshot(driver);
			e.fillInStackTrace();
			return;
		}
		// fillInStackTrace()

		System.out.println("Atomberg App Opened...");
		sleep(6000);
//		Method.captureScreenshot(driver); // captures screenshot

	}
}
