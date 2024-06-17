package Supports;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import Tabs.MoreTab;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import AtombergTest.Method;
import Devices.Add;
import Devices.SO;
import Devices.Select;
import Login.*;
import io.appium.java_client.AppiumDriver;

public class JUnitHTMLSampleTest extends JUnitHTMLReporter {

	public static AppiumDriver driver;

	public static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "14");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
			driver = new AppiumDriver(url, cap);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Assert.fail("Expected element to click not found");
			e.printStackTrace();
			return;
		}
		System.out.println("Atomberg App Opened...");
		sleep(6000);
		Method.captureScreenshot(driver);
	}

	@Test
	void testA() {
		try {
			openAtomberg();
			Email.Login(driver);
			System.out.println("Login Passed");
		} catch (Exception e) {
			e.getCause();
			e.printStackTrace();
			System.out.println("Login Failed");
		}
	}

	@Test()
	void testB() {
		try {
			Add.Device(driver,"Atomberg Smart Fan");
			sleep(3000);
			Select.Fan(driver);
		} catch (Exception e) {
			e.getCause();
			e.printStackTrace();
			System.out.println("Device Addition Failed");
		}
	}

	@Test
	void testC(){
		try {
			SO.Fan(driver);
		} catch (Exception e) {
			e.getCause();
			e.printStackTrace();
			System.out.println("Device Control Failed");
		}

	}
	@Test
	void testD() {
		try {
			MoreTab.Options(driver);
		} catch (Exception e) {
			e.getCause();
			e.printStackTrace();
			System.out.println("Failure in More Tab");
		}

	}

	@Test
	void testL()
	{

		try {
			Log.Out(driver);
		} catch (Exception e) {
			e.getCause();
			e.printStackTrace();
			System.out.println("Logout Failed");
		}
	}
	@BeforeAll
	static void beforeAll() {
		System.out.println("Test Started");
	}

	@AfterAll
	static void afterAll() {
		System.out.println("Test Ended");
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}