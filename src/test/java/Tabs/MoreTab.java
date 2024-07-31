package Tabs;
//Add First Device

import org.awaitility.Awaitility;
import org.openqa.selenium.*; //Selenium Dependencies
import AtombergTest.Method;
import Actions.*;
import io.appium.java_client.AppiumDriver;
import Supports.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MoreTab {
	public static void Options(AppiumDriver driver) {

		WebElement moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
				"Tab 3 of 3\"]"));
		moreTab.click();
		sleep(1000);
		openProfile(driver);
		Method.captureScreenshot(driver);
		System.out.println("Tap on Edit Profile");

		WebElement ChangeAvatar = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Change avatar\"]"));
		ChangeAvatar.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap On Change Avatar");
		for(int i=1; i<25; i++) {
			WebElement Avatar1 = driver.findElement(By.xpath("//android.widget.ScrollView/android.view.View[2]/android.view.View/android.view.View/android.widget.ImageView["+i+"]"));
			Avatar1.click();
			Method.captureScreenshot(driver);
		}
		driver.navigate().back();

		Tap.withPercentage(driver, 0.25, 0.275);
		Method.captureScreenshot(driver);

		WebElement EditCountryCode = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"+91\"]/android.widget.EditText"));
		EditCountryCode.click();

		Method.captureScreenshot(driver);
		driver.navigate().back();
		sleep(250);

		WebElement EditNumber = driver.findElement(By.xpath("//android.view.View[@content-desc=\"+91\"]"));
		EditNumber.click();
		Method.captureScreenshot(driver);
		driver.navigate().back();
		sleep(250);
		driver.navigate().back();
/*
		sleep(2000);
		Alexa.Connect(driver);
		sleep(2000);
		GoogleHome.Connect(driver);*/

		WebElement Theme = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]"));
		Theme.click();
		Scroll.Up(driver);
		// Change Electricity Unit Price
		WebElement UnitPrice = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]"));
		UnitPrice.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Unit Price");
		 
		WebElement ChangeAmount = driver.findElement(By.xpath("//android.widget.EditText[@text=\"7.0\"]"));
		ChangeAmount.click();
		Method.captureScreenshot(driver);
		WebElement ChangeCurrency = driver.findElement(By.xpath("//android.view.View[@content-desc=\"INR\"]"));
		ChangeCurrency.click();
		Method.captureScreenshot(driver);

		driver.navigate().back();
		driver.navigate().back();
		driver.navigate().back();

		// Manage Family
		WebElement ManageFamily = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
		ManageFamily.click();
		Method.captureScreenshot(driver);
		
		System.out.println("Tap on Manage Family");
		Tap.withPercentage(driver, 0.25, 0.25); //
		ManageHome(driver);
		Tap.withCoordinates(driver, 975, 875);// Narzo 650, 600 Tab 1125, 520 POCO 975, 875
		ManageMember(driver);
		AddHome(driver);
		Tap.withPercentage(driver, 0.75, 0.25);
		ManageHome(driver);
		driver.navigate().back();
		driver.navigate().back();

		//Live Widget
		WebElement widgetEnable = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Enable live widget\"]"));
		widgetEnable.click();
		List<WebElement> dialogueBox=driver.findElements(By.className("android.view.View"));

		// Help
		WebElement Help = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
		Help.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Help");
		driver.navigate().back();

		WebElement RaiseComplaint = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Raise a complaint\"]"));
		RaiseComplaint.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Raise Complaint");
		sleep(3000);
		driver.navigate().back();
		driver.navigate().back();

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

		Videos(driver);

		WebElement Manual = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Manual\"]"));
		Manual.click();
		Method.captureScreenshot(driver);
		System.out.println("Manual");
		
		driver.navigate().back();

		WebElement TroubleShoot = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Troubleshoot\"]"));
		TroubleShoot.click();
		Method.captureScreenshot(driver);
		System.out.println("Troubleshoot");
		
		Troubleshoot(driver);

		driver.navigate().back();
		// Rate Us
		sleep(1000);
		WebElement RateUs = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Rate us\"]"));
		RateUs.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Rate Us");
		
		driver.navigate().back();

		// Privacy Policy
		WebElement PrivacyPolicy = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Privacy policy\"]"));
		PrivacyPolicy.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Privacy Policy");
		
		driver.navigate().back();
		sleep(1000);
		Scroll.Up(driver);
		
		
		// Change Password
		WebElement ChangePassword = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
		ChangePassword.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Change Password");
		
		driver.navigate().back();

		// Delete Account
		WebElement DeleteAccount = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
		DeleteAccount.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Delete Account");
		
		driver.navigate().back();
		
		WebElement DevOps = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
		DevOps.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Developer Options");
		
		driver.navigate().back();

		// Logout
		WebElement Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
		Logout.click();
		Method.captureScreenshot(driver);
		System.out.println("Tap on Logout");
		
		driver.navigate().back();

	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}

	private static void openProfile(AppiumDriver driver) {
		List<WebElement> ELEMENTS = driver.findElements(By.className("android.widget.ImageView"));
		List<WebElement> elements = ELEMENTS.stream().filter(element -> element.getAttribute("content-desc") !=null).collect(Collectors.toList());
		for (WebElement e: elements)
		{
			if(e.getAttribute("content-desc").startsWith("Hi,"))
			{
				e.click();
				System.out.println("Edit Profile");
			}
		}
	}

	public static void ManageHome(AppiumDriver driver) {
		
		WebElement FamilyEdit = driver
				.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
		FamilyEdit.click();
		 
		System.out.println("Family Edit");
		Method.captureScreenshot(driver);
		
		WebElement LeaveHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
		LeaveHome.click();
		System.out.println("Leave home");
		Method.captureScreenshot(driver);
		
		WebElement Cancel1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
		Cancel1.click();
		
		WebElement DeleteHome = null;
		try {
			DeleteHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
		} catch (Exception exp) {
		}
		if (DeleteHome != null) {
			DeleteHome.click();
			 
			System.out.println("Delete home");
			Method.captureScreenshot(driver);
			
			WebElement Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
			Cancel.click(); // android.widget.Button[@content-desc="Cancel"]
			
		}
		driver.navigate().back();
	}

	public static void ManageMember(AppiumDriver driver) {
		
		WebElement RemoveMember = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Remove Member\"]"));
		RemoveMember.click();
		 
		System.out.println("Remove Member");
		Method.captureScreenshot(driver);
		
		WebElement Cancel2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
		Cancel2.click();

		WebElement MakeAdmin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Make Admin\"]"));
		MakeAdmin.click();
		 
		System.out.println("Make Admin");
		Method.captureScreenshot(driver);
		

		WebElement Cancel3 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
		Cancel3.click();
		driver.navigate().back();

		WebElement AddMember = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]"));
		AddMember.click();
		 
		Method.captureScreenshot(driver);
		sleep(3000);

		WebElement Share = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Share\"]"));
		Share.click();
		
		Method.captureScreenshot(driver);
		

		driver.navigate().back();
		driver.navigate().back();
		driver.navigate().back();
	}

	public static void AddHome(AppiumDriver driver) {
		
		WebElement AddHome = null;

		try {
			AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
		} catch (Exception exp) {
		}
		if (AddHome == null) {
			Scroll.Up(driver);
			
			AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
		}

		AddHome.click();
		
		Method.captureScreenshot(driver);
		

		Tap.withPercentage(driver, 0.50, 0.75); // Narzo 0.50, 0.625 Tab 0.50, 0.75
		
		Method.captureScreenshot(driver);
		

		WebElement HomeName = driver.findElement(By.xpath("//android.widget.EditText"));
		HomeName.click();
		
		HomeName.sendKeys("Script");
		Method.captureScreenshot(driver);
		

		WebElement Create = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create\"]"));
		Create.click();
		 
		Method.captureScreenshot(driver);
		sleep(5000);

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
}
