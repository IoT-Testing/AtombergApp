package AtombergTest;

//adb logcat *:E | findstr "com.atomberg.app"
// robot -d Output Test/Login.robot
//adb shell monkey -p com.atomberg.app -v 5000 --throttle 100

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class Method {
	public static String ScreenShot;
	//Capture Screenshot
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
		ScreenShot = "C:\\Users\\Rohit Bhagat\\Desktop\\Rohit\\Appium Screenshots\\NEWMOBILES\\Trail1\\Screenshot_" + timestamp + ".png";
		return ScreenShot;
	}
	//Speed and power control of the fans are added.

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//a random number will be selected from the array to set the pin of the lock while addition process
	public static int Array() {
		int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 0};

		Random random = new Random();
		int randomIndex = random.nextInt(numbers.length);
		return numbers[randomIndex];
	}

	// Lock addition process
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