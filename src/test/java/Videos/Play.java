package Videos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Swipe;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Play {
	public static void Videos(AppiumDriver driver) {
		WebElement AppTour = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"App Tour\"]"));
		AppTour.click();
		
		Method.captureScreenshot(driver);
		System.out.println("App Video Opened");
		
		VideoTryCatch(driver);
		VideoTryCatch(driver);
		sleep(5000);

		WebElement ConnectAlexa = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Alexa\"]"));
		ConnectAlexa.click();
		
		Method.captureScreenshot(driver);
		System.out.println("Alexa Video Opened");
		
		VideoTryCatch(driver);
		VideoTryCatch(driver);
		VideoTryCatch(driver);
		sleep(5000);

		WebElement ConnectGoogle = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Google\"]"));
		ConnectGoogle.click();
		
		Method.captureScreenshot(driver);
		System.out.println("Google Home Video Opened");
		
		VideoTryCatch(driver);
		VideoTryCatch(driver);
		sleep(5000);

		WebElement SLAppSetup = null;
		try {
			SLAppSetup = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
		} catch (Exception exp) {
		}
		if (SLAppSetup == null) {
			Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50
			
			WebElement SLInstall = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
			SLInstall.click();
			sleep(2000);
		} else {
			WebElement SLInstall = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
			SLInstall.click();
		}

		Method.captureScreenshot(driver);
		System.out.println("SL installation Video Opened");
		
		VideoTryCatch(driver);
		VideoTryCatch(driver);
		sleep(5000);

		SLAppSetup = null;
		try {
			SLAppSetup = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
		} catch (Exception exp) {
		}
		if (SLAppSetup == null) {
			Swipe.Left(driver, 0.80, 0.45);// Tab 0.35 narzo 0.50
			sleep(2000);
			SLAppSetup = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
		}
		WebElement SLFeatures = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Features\"]"));
		SLFeatures.click();
		sleep(2000);
		Method.captureScreenshot(driver);
		System.out.println("SL Feature Video Opened");
		
		VideoTryCatch(driver);
		VideoTryCatch(driver);
		sleep(5000);

		SLAppSetup.click();
		sleep(2000);
		Method.captureScreenshot(driver);
		System.out.println("App Setup for Lock Video Opened");
		VideoTryCatch(driver);
		VideoTryCatch(driver);

	}
	public static void VideoTryCatch(AppiumDriver driver) {
		WebElement VideoTutorials = null;

		try {
			VideoTutorials = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Help\"]"));
		} catch (Exception exp) {
		}
		if (VideoTutorials == null) {
			System.out.println("Back");
			driver.navigate().back(); // 180, 1550 860, 1960
			

		}
	}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("......");
	}
}
