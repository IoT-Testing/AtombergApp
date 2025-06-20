package Tests;

import AtombergTest.Method;
import app.WaterPurifier.Login.Email;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Map;


public class Monkey {
	public static AndroidDriver driver;
	static ExtentReports extent = new ExtentReports();

	@BeforeSuite
	public static void config(){
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String path = System.getProperty("user.dir")+"\\reports\\Atomberg"+timestamp+".html";
		ExtentSparkReporter reporter = new ExtentSparkReporter(path);
		reporter.config().setReportName("App Test Reports");
		reporter.config().setDocumentTitle("Test Reports");

		extent = new ExtentReports();
		extent.attachReporter(reporter);
		extent.setSystemInfo("Tester","Rohit Bhagat");
	}

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
			driver = new AndroidDriver(url, cap);
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
 void testMonkey()
  {

	  extent.createTest("Monkey Test");
	  String timestamp;
	  openAtomberg();

	  Email.Login(driver);
	  timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	  System.out.println(" " + timestamp + " "); // Adjusting the timeout duration for the 'adb' command
	  System.out.println("Monkey Started....");
	  driver.executeScript("mobile:shell",
			  Map.of("command", "monkey", "args", "-p com.atomberg.app --throttle 50 -v 1000"));
	  System.out.println("Monkey finished....");
	  driver.quit();
	  extent.flush();
  }
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("......");
	}
}
