package app.Supports;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import app.util.AppUtil;
import org.awaitility.Awaitility;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.android.AndroidDriver;

public class NetOnOff {

	public static AndroidDriver driver;

	public static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "14");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
			driver = new AndroidDriver(url, cap);
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		System.out.println("Atomberg App Opened...");
		sleep(6000);
		AppUtil.captureScreenshot(driver);
	}

	public static void main(String[] args) {
		openAtomberg();

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

}