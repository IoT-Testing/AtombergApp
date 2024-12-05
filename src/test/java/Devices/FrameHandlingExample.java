package Devices;

import Actions.Tap;
import AtombergTest.Method;
import Login.Email;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import io.appium.java_client.AppiumDriver;

public class FrameHandlingExample {
	private static AppiumDriver driver;

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

	public static void main(String[] args) {

		openAtomberg();
		Email.Login(driver);
		// Assuming the structure of your XML and the desired element to interact with
		// For example, interacting with the element representing "Atomberg Smart Fan"
		WebElement AddButton = null;
		try {
			AddButton = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
		} catch (Exception exp) {
		}
		if (AddButton != null) {
			AddButton.click();
		} else {
			Tap.withCoordinates(driver, 540, 1940);
		}
		for (int count = 0; count < 5; count++) {
			sleep(15000);
			WebElement Device = null;
			try {
				Device = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Discovered devices\"]"));
			} catch (NoSuchElementException ignored) {
			}

			if (Device != null) // if No Device Available
			{
				String deviceName = "Atomberg Smart Fan";
				String connectButtonDesc = "Connect";

				driver.switchTo().frame(3);
				// Switch to the outermost FrameLayout (index 0)
				WebElement frameLayout = driver.findElement(By.className("android.view.View"));

				// Iterate through the child elements to find the desired View
				findElementInFrame(frameLayout, deviceName, connectButtonDesc);
				break;
			} else {

				WebElement tryAgain = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
				tryAgain.click();
				System.out.println("Trying Again...");

			}
		}
	}

	// Recursive function to iterate through frames and find the desired element
	private static void findElementInFrame(WebElement frameElement, String deviceName, String connectButtonDesc) {
		// Find all child elements within the current frame
		List<WebElement> childElements = frameElement.findElements(By.className("android.view.View"));

		for (WebElement element : childElements) {
			// Check if the element's content description matches the desired device name
			System.out.println(element.getDomAttribute("content-desc"));
			if (element.getDomAttribute("content-desc").equals(deviceName)) {
				// Found the desired device, now click on the connect button corresponding to it.

				WebElement connectButton = element.findElement(By.xpath("(//android.view.View[@content-desc="+connectButtonDesc+"])"));
				connectButton.click();
				System.out.println("Connect button clicked for " + deviceName);
				return; // Exit recursion once the operation is completed
			}

			// If the current element contains child elements, recursively search within
			// them
			/*	if (element.findElements(By.className("android.view.View")).size() > 0) {
				findElementInFrame(element, deviceName, connectButtonDesc);
				
			}*/
		}
	}

	// Dummy method to simulate Appium driver initialization
	public static void sleep(long millis) {
		Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
	}
	public static void AtombergSmartLockFrame(AppiumDriver driver) {
		// Locate the frame containing "Atomberg Smart Lock"
		WebElement smartLockElement = driver.findElement(By.xpath("//android.view.View[@content-desc='Atomberg Smart Lock']"));
		WebElement parentFrame = smartLockElement.findElement(By.xpath("//android.view.View"));

		// Retrieve all elements within this frame
		List<WebElement> elementsInFrame = parentFrame.findElements(By.xpath(".//*"));

		// Print details of elements within the frame
		for (WebElement element : elementsInFrame) {
			String desc = element.getDomAttribute("content-desc");
			String text = element.getText();
			String bounds = element.getDomAttribute("bounds");
			System.out.println("Element: " + element.getTagName() +
					", Content-desc: " + desc +
					", Text: " + text +
					", Bounds: " + bounds);
		}

		// Example: Click an element within the frame
		// Assuming you want to click the first button found in the frame
		for (WebElement element : elementsInFrame) {
			if (element.getTagName().equals("android.widget.Button")) {
				element.click();
				break;
			}
		}
	}

}
