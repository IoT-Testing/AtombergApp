package Devices;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class Select {
	// selecting the fan model whether it is a Six LED, Single LED, Aris, Jaguar
	public static void Fan(AppiumDriver driver) {
		WebElement ModelSelect = null;
		try {
			ModelSelect = driver
					.findElement(By.xpath("//android.view.View[@content-desc=\"Pick the fan model you're having\"]"));
			System.out.println("Six LED fan");
		} catch (Exception e) {
		}
		if (ModelSelect != null) {
			SixLED(driver);
			FanModels.SixLEDColorSelect(driver);
		} else {
			WebElement Others = null;
			try {
				Others = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select your device color\"]"));
				System.out.println("Select your fan color");
			} catch (Exception exp) {
			}
			if (Others != null) {
				WebElement Aris = null;
				WebElement Jaguar = null;
				try {
					Aris = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]"));
				} catch (Exception e) {
				}
				try {
					Jaguar = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Regent Gray\"]"));
				} catch (Exception e) {
				}
				if (Aris != null) {
					FanModels.Aris(driver);
				} else if (Jaguar != null) {
					FanModels.Jaguar(driver);
				} else {
					FanModels.Erica(driver);
				}
				Method.captureScreenshot(driver);
			}
			else 
			{
				SixLED(driver);
			}
		}
			AdditionProcess(driver);

	}

	//Fan addition process mostly of selecting the room of the fan
	public static void AdditionProcess(AppiumDriver driver) {
		WebElement Next = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Next\"]")); // Add 2nd
		// Device
		System.out.println("Next");
		Next.click();
		Method.captureScreenshot(driver);

		// Select Room

		WebElement MasterBedroom = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Master Bedroom\"]"));
		MasterBedroom.click();
		Method.captureScreenshot(driver);
		WebElement GuestRoom = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Guest Room\"]"));
		GuestRoom.click();
		Method.captureScreenshot(driver);
		WebElement Kitchen = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Kitchen\"]"));
		Kitchen.click();
		Method.captureScreenshot(driver);
		WebElement CommonBR = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Common Bedroom\"]"));
		CommonBR.click();
		Method.captureScreenshot(driver);
		WebElement Lobby = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Lobby\"]"));
		Lobby.click();
		Method.captureScreenshot(driver);
		WebElement Balcony = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Balcony\"]"));
		Balcony.click();
		Method.captureScreenshot(driver);
		WebElement LivingRoom = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Living Room\"]"));
		LivingRoom.click();
		Method.captureScreenshot(driver);
		WebElement ContinueToAdd = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
		ContinueToAdd.click();
		Method.captureScreenshot(driver);
		SearchWiFi(driver, "Better_Together");

		WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc=\"Skip\"]"))).click();
		Method.captureScreenshot(driver);
	}

	//to complete the process of sending the Wi-Fi credentials to the fan
	public static void SearchWiFi(AppiumDriver driver, String SearchString) {
		//// android.widget.EditText[@text="Better_Together"]
		for (int i = 1; i <= 10; i++) {
			WebElement element = null;
			try {
				element = driver.findElement(By.xpath("//android.widget.EditText[@text=\"" + SearchString + "\"]"));
			} catch (Exception e) {
			}
			if (element != null) {
				System.out.println(" " + SearchString + " Available");
				WebElement Password = driver.findElement(By.xpath(
						"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]"));
				Password.getText();
				if (!Objects.equals(Password.getText(), "987654321")) {
					Password.clear();
				}
				Password.click();
				Password.sendKeys("987654321");
				WebElement Continue = driver
						.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
				Continue.click();
				break;
			} else {
				WebElement WiFI = driver.findElement(By.xpath(
						"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[1]"));
				WiFI.click();
				WiFI.sendKeys("Better_Together");
				WebElement Password = driver.findElement(By.xpath(
						"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText[2]"));
				Password.click();
				Password.sendKeys("987654321");
				WebElement Continue = driver
						.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
				Continue.click();
			}
		}
	}

	//Six LED consist of 3 models Renesa, Renesa +, Studio +
	public static void SixLED(AppiumDriver driver) {
		int randomNumber = (int) (Math.random() * 3); // generate a random number between 0 and 5

		try {
			switch (randomNumber) {
			case 0:
				WebElement Renesa = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]")); // Add 1st
																										// Device
				Renesa.click();
				System.out.println("Renesa Selected");
				Method.captureScreenshot(driver);
				break;
			case 1:
				WebElement StudioPlus = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]"));
				StudioPlus.click();
				System.out.println("Studio+ Selected");
				Method.captureScreenshot(driver);
				break;
			case 2:
				WebElement RenesaPlus = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]"));
				RenesaPlus.click();
				System.out.println("Renesa+ Selected");
				Method.captureScreenshot(driver);
				break;

			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
		driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]")).click();

	}

}