package Actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.testng.Assert;
import org.openqa.selenium.remote.DesiredCapabilities;
import AtombergTest.Method;
import AtombergTest.Monkey;
import Login.Email;
import io.appium.java_client.AppiumDriver;
// this is used to check the working of the code before integrating it into the main code
public class Check {
	private static AppiumDriver driver;

	//Initiating the driver (server URL & and App details)
	private void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://192.168.11.48:4723/wd/hub"); // URL of the Appium session
			driver = new AppiumDriver(url, cap);
			System.out.println("Appium driver initialized.");

		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Assert.fail("Expected element to click not found");
			Method.captureScreenshot(driver);
			e.printStackTrace();
			return;
		}
		System.out.println("Atomberg App Opened...");
		sleep(6000);
		Method.captureScreenshot(driver);
	}

	void testApp() {
		openAtomberg();
		Email.Login(driver);
		sleep(5000);
		Monkey.Run(driver);
	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
		//to avoid the whole code of awaitility
	}

}
