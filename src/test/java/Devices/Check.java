package Devices;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import AtombergTest.Method;
import Login.Email;
import io.appium.java_client.AppiumDriver;

public class Check {
	public static AppiumDriver driver;

	public static void main(String[] args) {
		openAtomberg();
		Email.Login(driver);
		Lock(driver);
	}

	public static void Lock(AppiumDriver driver) {
		/**
		 * @author Rohit
		 */
		try {
			sleep(5000);
			WebElement AddButton = null;
			try {
				AddButton = driver.findElement(By.xpath(
						"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
			} catch (Exception exp) {
			}
			if (AddButton != null) {

				CLA(driver);
			} else {
				Add.Device(driver, "Atomberg Smart Lock");
				CLA(driver);
			}
		} catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("......");
	}

	public static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tooBls\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
			driver = new AppiumDriver(url, cap);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Method.captureScreenshot(driver);
			e.printStackTrace();
			return;
		}
		System.out.println("Atomberg App Opened...");
		sleep(6000);
		Method.captureScreenshot(driver);

	}

	public static void CLA(AppiumDriver driver) {

		driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]")).click();
		WebElement LO = null;
		try {
			LO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.Button)"));
		} catch (Exception e) {
		}

		if (LO != null) {
			System.out.println("Lock Available");
			List<WebElement> Device = driver
					.findElements(By.xpath("//android.widget.Button/android.widget.ImageView[1]"));
			System.out.println(Device.size());
			for (WebElement element : Device) {
				System.out.println(element);
				element.click();
				System.out.println("Element clicked");
				LockControl(driver);
			}
		} else {
			System.out.println("No Lock Available");
			// android.view.View[@content-desc="Living Room"]/android.view.View[2]

		}
	}

	public static void LockControl(AppiumDriver driver) {// driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		sleep(12000);
		driver.findElement(By.xpath("//android.view.View[@content-desc=\"Pull down to unlock\"]")).click();
		Boolean Unlock = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Unlocked\"]")).isDisplayed();
		if (Unlock == true) {
			System.out.println("Successfully unlocked");
		} else {
			System.out.println("Some error");
		}
		History(driver);
	}

	public static void History(AppiumDriver driver) {
		WebElement history = null;
		try {
			history = driver.findElement(By.xpath("//android.view.View[@content-desc=\"History\"]"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (history != null) {
			history.click();
		}
		List<WebElement> elements = driver.findElements(By.xpath("//android.view.View"));
		for (WebElement element : elements) {

			System.out.println(element.getAttribute("content-desc"));
		if (element.getAttribute("content-desc")== "Only the latest 404 history logs are synced")
		{
			break;
		}
		}

	}

}