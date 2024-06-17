package Supports;

import Login.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import AtombergTest.Method;
import Devices.*;
import Tabs.MoreTab;
import io.appium.java_client.AppiumDriver;
import java.io.BufferedWriter;
import java.io.*;
import java.net.*;
import java.text.*;
import java.time.Duration;
import java.util.Date;

public class JUnitHTMLReporter {

	private static BufferedWriter junitWriter;

	@BeforeAll
	public static void setUp() throws IOException {
	    // Create the subfolder if it doesn't exist
	    File subfolder = new File("TestFiles");
	    if (!subfolder.exists()) {
	        subfolder.mkdir();
	    }
		// Create the HTML report file
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File junitReportFile = new File(subfolder, "junitReport" + timestamp + ".html");
		junitWriter = new BufferedWriter(new FileWriter(junitReportFile));

		// Write HTML header
		junitWriter.write("<html><head><title>JUnit Test Report</title></head><body>");
		junitWriter.write("<h1>JUnit Test Report - " + getCurrentTimestamp() + "</h1>");
	}

	@AfterAll
	public static void tearDown() throws IOException {
		// Write HTML footer and close the writer
		junitWriter.write("</body></html>");
		junitWriter.close();

		System.out.println("JUnit HTML report generated: junitReport");
	}

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
	void testA() throws IOException {

			try {
				openAtomberg();
				Email.Login(driver);
				boolean testPassed = true;
				writeTestResult("Login", testPassed);
			} catch (NoSuchElementException e) {
				boolean testPassed = false;
				writeTestResult("Login", testPassed);
			}
		}
	
	 @Test() 
     void testB() throws IOException{
		 try {
				 Add2.Fan(driver);
				 Select.Fan(driver);
				 sleep(3000); 
				 boolean testPassed = true;
				writeTestResult("Add Device", testPassed);
			}
				 catch(WebDriverException e) {
			 e.getCause(); 
				e.printStackTrace();
				boolean testPassed = false;
				writeTestResult("Add Device", testPassed);	
				driver.navigate().back();
                 }
		 }
	 /* 
*/
	@Test
	void testC() throws IOException {
		try {
			SO.Fan(driver);
			boolean testPassed = true;
			writeTestResult("DeviceControl", testPassed);
		} catch (NoSuchElementException e) {
			boolean testPassed = false;
			writeTestResult("DeviceControl", testPassed);
		}

	}
	
	 @Test
	 void testD() throws IOException {
		  try {
				MoreTab.Options(driver);
				boolean testPassed = true;
				writeTestResult("MoreTab", testPassed);
			} catch (NoSuchElementException e) {
				boolean testPassed = false;
				writeTestResult("MoreTab", testPassed);
			}
	 }
	 @Test
	 void testL() throws IOException{
		 try {
			 Log.Out(driver);
			 boolean testPassed = true;
			 writeTestResult("Logout", testPassed);
		 } catch (NoSuchElementException e) {
			 boolean testPassed = false;
			 writeTestResult("Logout", testPassed);
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

	private static void writeTestResult(String testName, boolean testPassed) throws IOException {
		String resultHtml = "<p>" + testName + ": ";
		if (testPassed) {
			resultHtml += "Passed</p>";
		} else {
			resultHtml += "Failed</p>";
		}
		junitWriter.write(resultHtml);
	}

	public static String getCurrentTimestamp() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void main(String[] args) {
		// Run the tests
		org.junit.runner.JUnitCore.main("JUnitHTMLReporter");
	}
}
