package ZTests;

import AtombergTest.Method;
import AtombergTest.Monkey;
import Login.Email;
import com.aventstack.extentreports.ExtentReports;
import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import static ZTests.ExtentReportAT.*;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;


public class monkeytest1 {
	public static AppiumDriver driver;
	public static final ExtentReports extent = ExtentReportAT.getReportObjects();

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();

		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "14");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized

			URL url = new URL("http://127.0.0.1:4723/wd/hub");// URL of the Appium session
			driver = new AppiumDriver(url, cap);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (IOException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Assert.assertTrue(driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
					" with Atomberg\"]")).isDisplayed());
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		System.out.println("Atomberg App Opened...");
		assert driver.findElement(By.xpath("//android.view.View[@content-desc=\"Experience smart living \n" +
				" with Atomberg\"]")).isDisplayed();
		sleep(6000);
		Method.captureScreenshot(driver);
	}

	@Order(2)
	@Test // Openeing the app
	void testOpenApp() {
		try {
			startTest("Open App");
			openAtomberg();
		} catch (Exception e) {
			ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "App Open failed: " + e.getMessage());
		} finally {
			endTest();
		}
	}

	@Order(1)
	@Test //Login, There is one more login method, which is Parameterising this test.
	void testLogin() {
		try {
			startTest("Login");
			Email.Login(driver);
		} catch (Exception e) {
			ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Login failed: " + e.getMessage());
		} finally {
			endTest();
		}
	}

	@Order(3)
	@Test
	void testMonkey() {
		try {
			Monkey.Run(driver);
			driver.quit();
		} catch (Exception e) {
			ExtentReportAT.getTest().log(com.aventstack.extentreports.Status.FAIL, "Analytics failed: " + e.getMessage());
		} finally {
			endTest();
		}

	}


	@AfterAll
	static void tearDown() {
		if (extent != null) {
			extent.flush();
		}
	}
}
//
//      startTest("Monkey Test");
//      openAtomberg();
//		Email.Login(driver);
