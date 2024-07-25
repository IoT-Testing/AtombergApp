package Supports;

import io.appium.java_client.AppiumDriver;

import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.screenrecording.*;
import org.testng.Assert;
import org.testng.annotations.*;
import AtombergTest.Method;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.io.FileOutputStream;
import java.io.IOException;

public class screenRecord {

	// ATUTestRecorder recorder;

	public static AppiumDriver driver;

	@BeforeTest
	public void setup() {

		try {
			// Use a empty DesiredCapabilities object
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("platformName", "Android");
			cap.setCapability("appPackage", "com.atomberg.app");
			cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
			cap.setCapability("apksigner",
					"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar");
			cap.setCapability("dumpAppPackageInfo", true);

			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://192.168.11.48:4723/wd/hub"); // URL of the Appium session
			driver = new AppiumDriver(url, cap);
			System.out.println("Appium driver initialized.");
			((CanRecordScreen) driver).startRecordingScreen();

		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Assert.fail("Expected element to click not found");
			Method.captureScreenshot(driver);
			e.printStackTrace();
			return;
		}
	}

	@Test
	public void testSample() throws InterruptedException {
		System.out.println("I am inside Sample test");
		System.out.println("test");

	}

	@AfterTest
	public void tearDown() throws IOException, InterruptedException {
		System.out.println("inside tear down");

		String media = ((CanRecordScreen) driver).stopRecordingScreen();
		DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd HH-mm-ss");
		Date date = new Date();
		String dir = "videos" + File.separator + dateFormat.format(date);

		File videoDir = new File(dir);
		if (!videoDir.exists()) {
			videoDir.mkdirs();
		}

		try (// String DFarm_Path = System.getenv("DEVICEFARM_LOG_DIR");
				FileOutputStream stream = new FileOutputStream(videoDir + File.separator + "test" + ".mp4")) {
			stream.write(org.apache.commons.codec.binary.Base64.decodeBase64(media));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		// Stop appium server when test Is ended.
		// Stop video recording.

		driver.quit();

	}

}
