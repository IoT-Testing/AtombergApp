package AtombergTest; //To check 


import app.AppInitializer;
import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Atomberg {
	public static AndroidDriver driver;

	public static void main(String[] args) {

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
	// connecting with the appium server and opening the app
	private static void connectButton(AndroidDriver driver){
		try {
			// Locate all elements with "Atomberg Smart Water Purifier"
			List<WebElement> deviceElements = driver.findElements(By.xpath("//android.view.View[@content-desc=\"Atomberg Smart Water Purifier\"]"));
			if (deviceElements.isEmpty()) {
				System.out.println("No elements found with content-desc 'Atomberg Smart Water Purifier'.");
				return;
			}
			// Iterate over the list to find the correct device and its sibling "Connect" button
			System.out.println(deviceElements.size());
			for (WebElement device : deviceElements) {
				// Get the parent container of the target element
				WebElement parentElement = device.findElement(By.xpath("./.."));
				// Locate the "Connect" button within the same parent container
				WebElement connectButton = parentElement.findElement(By.xpath(
						".//android.view.View[@content-desc=\"Connect\"]"
				));
				// Click the "Connect" button
				connectButton.click();
				System.out.println("Clicked on the 'Connect' button next to 'Atomberg Smart Water Purifier'.");
				break; // Exit the loop after the first match
			}

		} catch (Exception e) {
			System.err.println("An error occurred: " + e.getMessage());
		}
	}
}
