package Videos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Swipe;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Play {
	public static void Videos(AppiumDriver atomberg) {
		WebElement AppTour = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"App Tour\"]"));
		AppTour.click();
		
		Method.captureScreenshot(atomberg);
		System.out.println("App Video Opened");
		
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);
		sleep(5000);

		WebElement ConnectAlexa = atomberg
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Alexa\"]"));
		ConnectAlexa.click();
		
		Method.captureScreenshot(atomberg);
		System.out.println("Alexa Video Opened");
		
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);
		sleep(5000);

		WebElement ConnectGoogle = atomberg
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Google\"]"));
		ConnectGoogle.click();
		
		Method.captureScreenshot(atomberg);
		System.out.println("Google Home Video Opened");
		
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);
		sleep(5000);

		WebElement SLAppSetup = null;
		try {
			SLAppSetup = atomberg
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
		} catch (Exception exp) {
		}
		if (SLAppSetup == null) {
			Swipe.Left(atomberg, 0.80, 0.50);// Tab 0.35 narzo 0.50
			
			WebElement SLInstall = atomberg
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
			SLInstall.click();
			sleep(2000);
		} else {
			WebElement SLInstall = atomberg
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
			SLInstall.click();
		}

		Method.captureScreenshot(atomberg);
		System.out.println("SL installation Video Opened");
		
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);
		sleep(5000);

		SLAppSetup = null;
		try {
			SLAppSetup = atomberg
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
		} catch (Exception exp) {
		}
		if (SLAppSetup == null) {
			Swipe.Left(atomberg, 0.80, 0.45);// Tab 0.35 narzo 0.50
			sleep(2000);
			SLAppSetup = atomberg
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
		}
		WebElement SLFeatures = atomberg
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Features\"]"));
		SLFeatures.click();
		sleep(2000);
		Method.captureScreenshot(atomberg);
		System.out.println("SL Feature Video Opened");
		
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);
		sleep(5000);

		SLAppSetup.click();
		sleep(2000);
		Method.captureScreenshot(atomberg);
		System.out.println("App Setup for Lock Video Opened");
		videoTryCatch(atomberg);
		videoTryCatch(atomberg);

	}
	public static void videoTryCatch(AppiumDriver atomberg) {
		WebElement VideoTutorials = null;

		try {
			VideoTutorials = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Help\"]"));
		} catch (Exception exp) {
		}
		if (VideoTutorials == null) {
			System.out.println("Back");
			atomberg.navigate().back(); // 180, 1550 860, 1960
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
