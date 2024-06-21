package AtombergTest; //To check 


//import Supports.GoogleHome;
import io.appium.java_client.AppiumDriver;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import Actions.*;
import Devices.*;
import java.net.*;
import java.time.Duration;
import java.util.List;
import Login.*;

public class Atomberg {
	public static AppiumDriver driver;

	public static void main(String[] args) {
		openAtomberg();
		Email.Login(driver);
		SO.Lock(driver);

	}


	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.fillInStackTrace();
		}
		System.out.println("......");
	}

	public static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner", "C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tooBls\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
			driver = new AppiumDriver(url, cap);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Method.captureScreenshot(driver);
			e.fillInStackTrace();
			return;
		}// fillInStackTrace()
		System.out.println("Atomberg App Opened...");
		sleep(6000);
		Method.captureScreenshot(driver);

	}
}
