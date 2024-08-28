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
import io.appium.java_client.ios.IOSDriver;

public class FrameHandlingExample {
	private static IOSDriver driver;

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
			driver = new IOSDriver(url, cap);
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
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText[1]/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText[3]/XCUIElementTypeImage"));
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
				Device = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Discovered devices\"]"));
			} catch (NoSuchElementException ignored) {
			}

			if (Device != null) // if No Device Available
			{
				String deviceName = "Atomberg Smart Fan";
				String connectButtonDesc = "Connect";

				driver.switchTo().frame(3);
				// Switch to the outermost FrameLayout (index 0)
				WebElement frameLayout = driver.findElement(By.className("XCUIElementTypeStaticText"));

				// Iterate through the child elements to find the desired View
				findElementInFrame(frameLayout, deviceName, connectButtonDesc);
				break;
			} else {

				WebElement tryAgain = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Try Again\"]"));
				tryAgain.click();
				System.out.println("Trying Again...");

			}
		}
	}

	// Recursive function to iterate through frames and find the desired element
	private static void findElementInFrame(WebElement frameElement, String deviceName, String connectButtonDesc) {
		// Find all child elements within the current frame
		List<WebElement> childElements = frameElement.findElements(By.className("XCUIElementTypeStaticText"));

		for (WebElement element : childElements) {
			// Check if the element's content description matches the desired device name
			System.out.println(element.getAttribute("name"));
			if (element.getAttribute("name").equals(deviceName)) {
				// Found the desired device, now click on the connect button corresponding to it.

				WebElement connectButton = element.findElement(By.xpath("(//XCUIElementTypeStaticText[@name="+connectButtonDesc+"])"));
				connectButton.click();
				System.out.println("Connect button clicked for " + deviceName);
				return; // Exit recursion once the operation is completed
			}

			// If the current element contains child elements, recursively search within
			// them
			/*	if (element.findElements(By.className("XCUIElementTypeStaticText")).size() > 0) {
				findElementInFrame(element, deviceName, connectButtonDesc);
				
			}*/
		}
	}

	// Dummy method to simulate Appium driver initialization
	public static void sleep(long millis) {
		Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
	}
	public static void AtombergSmartLockFrame(IOSDriver driver) {
		// Locate the frame containing "Atomberg Smart Lock"
		WebElement smartLockElement = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Atomberg Smart Lock']"));
		WebElement parentFrame = smartLockElement.findElement(By.xpath("//XCUIElementTypeStaticText"));

		// Retrieve all elements within this frame
		List<WebElement> elementsInFrame = parentFrame.findElements(By.xpath(".//*"));

		// Print details of elements within the frame
		for (WebElement element : elementsInFrame) {
			String desc = element.getAttribute("name");
			String text = element.getText();
			String bounds = element.getAttribute("bounds");
			System.out.println("Element: " + element.getTagName() +
					", name: " + desc +
					", Text: " + text +
					", Bounds: " + bounds);
		}

		// Example: Click an element within the frame
		// Assuming you want to click the first button found in the frame
		for (WebElement element : elementsInFrame) {
			if (element.getTagName().equals("XCUIElementTypeButton")) {
				element.click();
				break;
			}
		}
	}

}
