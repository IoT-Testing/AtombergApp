package MoreTab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Actions.Scroll;
import Actions.Swipe;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Help {
	public static void RaC(AppiumDriver driver) {
		WebElement RaiseComplaint = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Raise a complaint\"]"));
		RaiseComplaint.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Raise Complaint");
		sleep(3000);
		driver.navigate().back();
		driver.navigate().back();

}
	public static void TC(AppiumDriver driver) {
		WebElement TrackComplaint = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Track complaints\"]"));
		TrackComplaint.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Track Complaint");
		sleep(2000);

		WebElement NoComplaints = null;

		try {
			NoComplaints = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View[1]"));
		} catch (Exception exp) {
		}
		if (NoComplaints != null) {
			System.out.println("No Complaints Raised.");
			
		}

		Method.captureScreenshot(driver);
		
		driver.navigate().back();
	
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
	public static void Manual(AppiumDriver driver) {}
	public static void Troubleshoot(AppiumDriver driver) {

		// Troubleshoot for Fans

				WebElement Fan = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Fan\"]"));
				Fan.click();
				Method.captureScreenshot(driver);
				System.out.println("Fan Troubleshoot");
				 

				WebElement Renesa = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]"));
				Renesa.click();
				Method.captureScreenshot(driver);
				System.out.println("Renesa");
				 
				EnterSerialNumber(driver);
				 

				WebElement RenesaPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa +\"]"));
				RenesaPlus.click();
				Method.captureScreenshot(driver);
				System.out.println("RenesaPlus");
				 
				driver.navigate().back();

				WebElement RenesaAlpha = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Alpha\"]"));
				RenesaAlpha.click();
				Method.captureScreenshot(driver);
				System.out.println("RenesaAlpha");
				 
				OK(driver);

				WebElement StudioPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio +\"]"));
				StudioPlus.click();
				Method.captureScreenshot(driver);
				System.out.println("StudioPlus");
				 
				EnterSerialNumber(driver);
				 

				WebElement Erica = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica\"]"));
				Erica.click();
				Method.captureScreenshot(driver);
				System.out.println("Erica");
				 
				EnterSerialNumber(driver);
				 

				WebElement Starlight = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Starlight\"]"));
				Starlight.click();
				Method.captureScreenshot(driver);
				System.out.println("Starlight");
				 
				ReturnToHome(driver);

				Scroll.Up(driver);

				WebElement Aris = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris\"]"));
				Aris.click();
				Method.captureScreenshot(driver);
				System.out.println("Aris");
				 
				ReturnToHome(driver);

				WebElement Efficio = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Efficio/ Efficio +\"]"));
				Efficio.click();
				Method.captureScreenshot(driver);
				System.out.println("Efficio");
				 
				OK(driver);

				WebElement Ikano = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ikano\"]"));
				Ikano.click();
				Method.captureScreenshot(driver);
				System.out.println("Ikano");
				 
				OK(driver);

				WebElement Ozeo = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ozeo\"]"));
				Ozeo.click();
				Method.captureScreenshot(driver);
				System.out.println("Ozeo");
				 
				OK(driver);

				WebElement Ameza = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ameza\"]"));
				Ameza.click();
				Method.captureScreenshot(driver);
				System.out.println("Ameza");
				 
				OK(driver);

				WebElement Other = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Other\"]"));
				Other.click();
				Method.captureScreenshot(driver);
				System.out.println("Other");
				 
				ReturnToHome(driver);
				driver.navigate().back();

		// Troubleshoot for Locks        
				WebElement Lock = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Lock\"]"));
				Lock.click();
				Method.captureScreenshot(driver);
				System.out.println("Lock Troubleshoot");
				
				driver.navigate().back();
				driver.navigate().back();

			}

    private static void ReturnToHome(AppiumDriver driver) {
				WebElement ReturnToHome = driver
						.findElement(By.xpath("//android.widget.Button[@content-desc=\"Return to home\"]"));
				ReturnToHome.click();
				Method.captureScreenshot(driver);
				System.out.println("Return To Home");
				
			}

	private static void EnterSerialNumber(AppiumDriver driver) {
				WebElement ManualEnter = driver.findElement(By.xpath("//android.widget.EditText"));
				ManualEnter.click();
				Method.captureScreenshot(driver);
				System.out.println("Enter Barcode Manually...");
				

				WebElement ScanBarcode = driver.findElement(By.xpath("//android.widget.EditText/android.widget.ImageView"));
				ScanBarcode.click();
				Method.captureScreenshot(driver);
				System.out.println("Scan Barcode ...");
				
				WebElement AllowCamera = null;
				try {
					sleep(2000);
					AllowCamera = driver.findElement(By.id("com.android.permissioncontroller:id/permission_message"));

				} catch (Exception exp) {

				}
				if (AllowCamera != null) {

					Method.captureScreenshot(driver);
					WebElement Camera = driver
							.findElement(By.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
					Camera.click();
					Method.captureScreenshot(driver);
					sleep(2000);
				}
				driver.navigate().back();

				WebElement ContactSupport = driver.findElement(
						By.xpath("//android.view.View[@content-desc=\"Can't find serial number? Contact support\"]"));
				ContactSupport.click();
				Method.captureScreenshot(driver);
				System.out.println("Contact Support ...");
				

				WebElement Email = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Email\"]"));
				Email.click();
				Method.captureScreenshot(driver);
				System.out.println("Email ...");
				
				driver.navigate().back();
				
				System.out.println("...");
				driver.navigate().back();
				
				System.out.println("...");

				WebElement Call = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Call\"]"));
				Call.click();
				Method.captureScreenshot(driver);
				System.out.println("Call ...");
				
				driver.navigate().back();
				driver.navigate().back();
				driver.navigate().back();
				

				Tap.withPercentage(driver, 0.07, 0.05); // tab 0.065, 0.05 narzo 0.07 , 0.10
				Method.captureScreenshot(driver);
				
				WebElement ID = null;
				try {
					ID = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Identify your device\"]"));
				} catch (Exception EXP) {
				}

				if (ID == null) {
					Tap.withPercentage(driver, 0.09, 0.1);
					Method.captureScreenshot(driver);
					System.out.println("Back to App ...");
					
				}
			}

	private static void OK(AppiumDriver driver) {
				WebElement OK = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Ok\"]"));
				OK.click();
				Method.captureScreenshot(driver);
				System.out.println("OK ...");
				
			}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
