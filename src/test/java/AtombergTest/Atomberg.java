package AtombergTest; //To check 


//import Supports.GoogleHome;
import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
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
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner", "C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tooBls\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
			driver = new IOSDriver(url, cap);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Method.captureScreenshot(driver);
			e.fillInStackTrace();
			return;
		}
		// fillInStackTrace()

		System.out.println("Atomberg App Opened...");
		sleep(6000);
		Method.captureScreenshot(driver); // captures screenshot

	}
}
