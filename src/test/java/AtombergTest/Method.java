package AtombergTest;

//adb logcat *:E | findstr "com.atomberg.app"
// robot -d Output Test/Login.robot
//adb shell monkey -p com.atomberg.app -v 5000 --throttle 100
//import Devices.SO;
import io.appium.java_client.ios.IOSDriver;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import Actions.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;


public class Method {
	public static String captureScreenshot(IOSDriver driver) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		try {
			File screenshotFile = driver.getScreenshotAs(OutputType.FILE);

			String destinationFilePath = "/Users/himanshu/WidgetTest/screenshots/" + timestamp + ".png";
			FileUtils.copyFile(screenshotFile, new File(destinationFilePath));

			System.out.println("Appium screenshot saved as: " + destinationFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return System.getProperty("user.dir")+ "//reports//" + timestamp+".png";
	}

	public static void FanControl(IOSDriver driver) {

		WebElement Speed1 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"1\"]"));
		Speed1.click();
		System.out.println("Speed1");
		Method.captureScreenshot(driver);


		WebElement Speed2 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"2\"]"));
		Speed2.click();
		System.out.println("Speed2");
		Method.captureScreenshot(driver);

		WebElement Speed3 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"3\"]"));
		Speed3.click();
		System.out.println("Speed3");
		Method.captureScreenshot(driver);

		WebElement Speed4 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"4\"]"));
		Speed4.click();
		System.out.println("Speed4");
		Method.captureScreenshot(driver);

		WebElement Speed5 = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"5\"]"));
		Speed5.click();
		System.out.println("Speed5");
		Method.captureScreenshot(driver);

		WebElement Boost = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeButton[5]"));
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

	public static void SearchWiFi(IOSDriver driver, String SearchString) {
		Tap.withCoordinates(driver,50,50);
		//// android.widget.EditText[@text="Better_Together"]
		for (int i = 1; i <= 10; i++) {
			WebElement element = null;
			try {														//XCUIElementTypeTextField[@value="Better_Together"]
				element = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value=\"" + SearchString + "\"]"));
			} catch (Exception e) {
			}
			if (element != null) {
				System.out.println(" " + SearchString + " Available");
				Tap.withCoordinates(driver,370,790);  //355,755 iphone 15
				Tap.withCoordinates(driver,370,790);
				WebElement Password = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Enter Wi-Fi password\"]"));
				driver.findElement(By.xpath("//XCUIElementTypeApplication[@name=\"Atomberg Home\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[4]/XCUIElementTypeOther")).click();
				sleep(1000);
				Password.sendKeys("123@ToMb^rg#2425");
				WebElement Continue = driver
						.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]"));
				Continue.click();
				break;
			} else {
				WebElement WiFI = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Enter Wi-Fi name\"]"));
				WiFI.click();
				WiFI.sendKeys("Better_Together");
				WebElement Password = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"Enter Wi-Fi password\"]"));
				Password.click();
				Password.sendKeys("987654321");
				WebElement Continue = driver
						.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]"));
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

	public static void AdditionProcess(IOSDriver driver) {
		WebElement Next = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Next\"]")); // Add 2nd
																											// Device
		System.out.println("Next");
		Next.click();

		// Select Room

		WebElement MasterBedroom = driver
				.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Master Bedroom\"]"));
		MasterBedroom.click();
		Method.captureScreenshot(driver);
		WebElement GuestRoom = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Guest Room\"]"));
		GuestRoom.click();
		Method.captureScreenshot(driver);
		WebElement Kitchen = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Kitchen\"]"));
		Kitchen.click();
		Method.captureScreenshot(driver);
		WebElement CommonBR = driver
				.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Common Bedroom\"]"));
		CommonBR.click();
		Method.captureScreenshot(driver);
		WebElement Lobby = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Lobby\"]"));
		Lobby.click();
		Method.captureScreenshot(driver);
		WebElement Balcony = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Balcony\"]"));
		Balcony.click();
		Method.captureScreenshot(driver);
		WebElement LivingRoom = driver
				.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Living Room\"]"));
		LivingRoom.click();
		Method.captureScreenshot(driver);
		WebElement ContinueToAdd = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Continue\"]"));
		ContinueToAdd.click();
		Method.captureScreenshot(driver);
		SearchWiFi(driver, "Better_Together");
		Method.captureScreenshot(driver);

		WebDriverWait Wait = new WebDriverWait(driver, Duration.ofSeconds(60));
		Wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//XCUIElementTypeStaticText[@name=\"Skip\"]"))).click();
		Method.captureScreenshot(driver);

	}

	public static void AddLock(IOSDriver driver) {
		for (int i = 3 ; i <9 ; i++)
		{
			WebElement Pin = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/XCUIElementTypeStaticText/android.widget.EditText["+i+"]"));
			String randomNumber = String.valueOf(Array());
			Pin.sendKeys(randomNumber);
		}
		WebElement Save = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Save\"]"));
		 Save.click();
		sleep(5000);
		WebElement SuccessMessage = null;
		try {
			sleep(2000);
			SuccessMessage = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Added Successfully \uD83D\uDC4D\"]"));
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
