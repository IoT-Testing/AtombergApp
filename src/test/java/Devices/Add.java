package Devices;

import Actions.Tap;
import AtombergTest.Method;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;

public class Add {
	@SuppressWarnings("unused")
	public static void Device(AppiumDriver driver, String searchText) {
		WebElement AddButton = null;
		try {
			AddButton = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
		} catch (Exception exp) {
		}
		if (AddButton != null) {
			AddButton.click();
			sleep(1000);
		} else {
			Tap.withCoordinates(driver, 540, 1940);
			sleep(1000);
		}

			sleep(15000);
			WebElement element = null;
			String xpathExpression = "(//android.view.View[@content-desc=\"" + searchText + "\"])";
			// Search Fan Only
			try {
				element = driver.findElement(By.xpath(xpathExpression));
			} catch (NoSuchElementException ignored) {
			}
			int count = 0; // no. of retry
			if (element != null) // if device is available
			{
				Method.captureScreenshot(driver);
				WebElement connect = driver.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));
				connect.click();
				System.out.println("Connect button clicked.");
				// Connect to fan
				sleep(5000);
				if (searchText == "Atomberg Smart Fan") {
					sleep(5000);
					WebElement ConnectLock = null; // check if a lock connect button is clicked
					try {
						ConnectLock = driver
								.findElement(By.xpath("//android.widget.Button[@content-desc=\"How to reset?\"]"));
					} catch (NoSuchElementException ignored) {
					}
					WebElement dialogue = null;
					try {
						dialogue = driver.findElement(By.xpath(
								"//android.view.View[@content-desc=\"Device already paired\"]"));
					} catch (NoSuchElementException ignored) {
					}


					for (int i = 1; i <= 10; i++)// assumed 10 maximum devices
					{
					if (ConnectLock != null || dialogue != null) {
						WebElement Cancel = driver
								.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
						Cancel.click();
						i++; // next connect button index
						System.out.println("Next connect");
						WebElement NewConnect = driver
								.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
						NewConnect.click();
						System.out.println("Connect button ckicked");
						continue;
					}
					}
				} else if (searchText == "Atomberg Smart Lock") {
					
					sleep(5000);
					WebElement ConnectFan = null; // check if a lock connect button is clicked
					try {
						ConnectFan = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
					} catch (NoSuchElementException ignored) {
					}
					WebElement dialogue = null;
					try {
						dialogue = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]"));
					} catch (NoSuchElementException ignored) {
					}

					for (int i = 1; i <= 10; i++)// assumed 10 maximum devices
					{
					if (ConnectFan != null || dialogue != null) {

						driver.navigate().back();
						try {
							WebElement device = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Discovered devices\"]"));
						} catch (NoSuchElementException e) {
							sleep(5000);
						}
						i++; // next connect button index
						System.out.println("Next connect");
						WebElement NewConnect = driver
								.findElement(By.xpath("(//android.view.View[@content-desc=\"Connect\"])[" + i + "]"));
						NewConnect.click();
						System.out.println("Connect button ckicked");
						WebElement Device = null;
						try {
							Device = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
						} catch (NoSuchElementException e) {
							continue;
						}
						if (Device != null) {
							break;
						}
					}
					}
				}
			} else {

				WebElement Device = null;
				try {
					Device = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Discovered devices\"]"));
				} catch (NoSuchElementException ignored) {
				}
				if (Device == null) // if No Device Available
				{

					WebElement tryAgain = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Try Again\"]"));
					tryAgain.click();
					count++;
					System.out.println("Trying Again...." + count);
					if (count == 10) {
						System.out.println("No device found");
						
					} else {
						sleep(15000);
					}
				} 
			}
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}