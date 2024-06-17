package AtombergTest; //To check 

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import Login.Email;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

public class Monkey2 {
	public static AppiumDriver driver;

	public static void main(String[] args) {
		try {
			String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			System.out.println(" " + timestamp + " ");
			openAtomberg();
			Email.Login(driver);
			timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
			System.out.println(" " + timestamp + " "); // Adjusting the timeout duration for the 'adb' command
	        System.out.println("recording started");
			System.out.println("Monkey Started....");
			driver.executeScript("mobile:shell",
					Map.of("command", "monkey", "args", "-p com.atomberg.app --throttle 100 -v 1000"));
			System.out.println("Monkey finished....");
			driver.quit();
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.fillInStackTrace();
		}
	}

	public static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("platformVersion", "14");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		//cap.setCapability("apksigner","C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tooBls\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
            driver = new AppiumDriver(url, cap);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			e.fillInStackTrace();
			return;
		}
		System.out.println("Atomberg App Opened...");
		sleep(6000);

	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.fillInStackTrace();
		}
	}
}
