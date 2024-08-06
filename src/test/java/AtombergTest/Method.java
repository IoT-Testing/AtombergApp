package AtombergTest;

//adb logcat *:E | findstr "com.atomberg.app"
// robot -d Output Test/Login.robot
//adb shell monkey -p com.atomberg.app -v 5000 --throttle 100
//import Devices.SO;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Actions.*;
import io.appium.java_client.AppiumDriver;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;


public class Method {
	public static String captureScreenshot(AppiumDriver driver) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		try {
			File screenshotFile = driver.getScreenshotAs(OutputType.FILE);

			String destinationFilePath = "C:\\Users\\Rohit Bhagat\\Desktop\\Rohit\\Appium Screenshots\\NEWMOBILES\\Trail1\\Screenshot_"
					+ timestamp + ".png";
			FileUtils.copyFile(screenshotFile, new File(destinationFilePath));

			System.out.println("Appium screenshot saved as: " + destinationFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return System.getProperty("user.dir")+ "//reports//" + timestamp+".png";
	}

	public static void FanControl(AppiumDriver driver) {

		WebElement Speed1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"1\"]"));
		Speed1.click();
		System.out.println("Speed1");
		Method.captureScreenshot(driver);


		WebElement Speed2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"2\"]"));
		Speed2.click();
		System.out.println("Speed2");
		Method.captureScreenshot(driver);

		WebElement Speed3 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"3\"]"));
		Speed3.click();
		System.out.println("Speed3");
		Method.captureScreenshot(driver);

		WebElement Speed4 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"4\"]"));
		Speed4.click();
		System.out.println("Speed4");
		Method.captureScreenshot(driver);

		WebElement Speed5 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"5\"]"));
		Speed5.click();
		System.out.println("Speed5");
		Method.captureScreenshot(driver);

		WebElement Boost = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[5]"));
		Boost.click();
		System.out.println("Boost");
		Method.captureScreenshot(driver);

	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

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

	public static int Array() {
		int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

		Random random = new Random();
		int randomIndex = random.nextInt(numbers.length);
		return numbers[randomIndex];
	}

	public static void AdditionProcess(AppiumDriver driver) {
		WebElement Next = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Next\"]")); // Add 2nd
																											// Device
		System.out.println("Next");
		Next.click();
		SearchWiFi(driver, "Better_Together");
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
		WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//android.view.View[@content-desc=\"Skip\"]"))).click();
		Method.captureScreenshot(driver);
	}

	public static void AddLock(AppiumDriver driver) {
		for (int i = 3 ; i <9 ; i++)
		{

			WebElement Pin = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.EditText["+i+"]"));
			String randomNumber = String.valueOf(Array());
			Pin.sendKeys(randomNumber);
		}
		WebElement Save = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Save\"]"));
		 Save.click();
		sleep(5000);
		WebElement SuccessMessage = null;
		try {
			sleep(2000);
			SuccessMessage = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Added Successfully \uD83D\uDC4D\"]"));
		}
		catch(NoSuchElementException e){
		}
		if (SuccessMessage != null) {
			System.out.println("Lock Added Successfully");
			sleep(1500);
		}
		sleep(3000);

	}

}
