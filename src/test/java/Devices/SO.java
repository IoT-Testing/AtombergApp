package Devices;

import Actions.NumberPad;
import Actions.Tap;
import AtombergTest.Method;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Scroll;

import io.appium.java_client.AppiumDriver;

public class SO {//Search Online Fan
	public static void Fan(AppiumDriver driver) {

		try {
			sleep(2500);
			WebElement AddButton = null;
			try {
				AddButton = driver.findElement(By.xpath(
						"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
			} catch (Exception exp) {
				exp.getCause();
			}
			if (AddButton != null) {
			
			CDO(driver);
			}
			else
			{
				Add2.Fan(driver);
				CDO(driver);
			}
		} catch (Exception exp) {
			exp.printStackTrace();
			System.out.println(exp.getMessage());
			exp.printStackTrace();
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

	private static void CDO(AppiumDriver driver)
	{
		WebElement Fans = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]"));
		Fans.click();
		sleep(1500);
		WebElement FO = null;
		try {
			FO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.ImageView)"));
		}catch(Exception e) {}	

		if (FO != null)
		{
			System.out.println("Fan Online");
			List<WebElement> Device = driver.findElements(By.xpath("(//android.widget.Button/android.widget.ImageView[1])"));
			System.out.println(Device.size());
			for(WebElement element : Device)
			{
				System.out.println(element);
				element.click();
				Method.FanControl(driver);
				driver.navigate().back();			
 			}
			if(Device.size()>=4)
			{
				Scroll.Up(driver);	
			}
		}
		else
		{
			System.out.println("No Fan Offline");
		}
	}

	public static void Lock(AppiumDriver driver)
	{
		driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]")).click();
		WebElement LO = null;
		try {
			LO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.Button)"));
		} catch (Exception e) {
		}

		if (LO != null) {
			System.out.println("Lock Available");
			List<WebElement> Device = driver
					.findElements(By.xpath("//android.widget.Button/android.widget.ImageView[1]"));
			System.out.println(Device.size());
			for (WebElement element : Device) {
				System.out.println(element);
				element.click();
				System.out.println("Element clicked");
				LockControl(driver);
			}
		} else {
			System.out.println("No Lock Available");

		}
	}
	public static void LockControl(AppiumDriver driver) {
		sleep(7500);
		driver.findElement(By.xpath("//android.view.View[@content-desc=\"Pull down to unlock\"]")).click();
		System.out.println("Unlocking");
		sleep(1000);
		WebElement Unlocked = null;
		WebElement NoLock = null;

		try {
			Unlocked = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Unlocked\"]"));
		} catch (Exception ignored) {
		}
		try {
			NoLock = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not unlock\"]"));
		} catch (Exception ignored) {
		}
		if (Unlocked != null) {
			System.out.println("Successfully unlocked");
		}
		else if (NoLock != null)
		{
			System.out.println("Lock not available or Bluetooth off");
			driver.navigate().back();
		}
		else {
			System.out.println("Error");
			driver.navigate().back();
		}
//		history(driver);
//		sleep(5000);
//		driver.navigate().back();
		//AccessKeys(driver);
		lockSettings(driver);
//		driver.navigate().back();
//		driver.navigate().back();

	}
	public static void history(AppiumDriver driver) {
		WebElement history = null;
		try {
			history = driver.findElement(By.xpath("//android.view.View[@content-desc=\"History\"]"));
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		if (history != null) {
			history.click();
		}
	}
		public static void lockSettings(AppiumDriver driver){
			WebElement settings = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Settings\"]"));
			settings.click();
			Passcode(driver);
			sleep(1000);
			WebElement users = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Users\"]"));
			users.click();
			sleep(1000);
			driver.navigate().back();
			PBCSettings(driver);


		}
		public static void AccessKeys(AppiumDriver driver){

		WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
		AccessKeys.click();
		Passcode(driver);
		KeyType(driver);


		}
		public static void Passcode(AppiumDriver driver)
		{
			Tap.withCoordinates(driver, 540, 880);
			sleep(500);
			NumberPad.one(driver);
			NumberPad.one(driver);
			NumberPad.one(driver);
			NumberPad.two(driver);
			NumberPad.two(driver);
			NumberPad.two(driver);
			NumberPad.done(driver);
		}
		public static void KeyType(AppiumDriver driver)
		{
			List<WebElement> KEYS = driver.findElements(By.className("android.widget.Button"));
			int i;
			int total = KEYS.size();
			for (i=0; 1<total; i++){
				List<WebElement> Keys = driver.findElements(By.className("android.widget.Button"));
				WebElement key = Keys.get(i);
				boolean OTP = key.getAttribute("content-desc").endsWith("OTP");
				boolean New = key.getAttribute("content-desc").endsWith("NEW");
				boolean pin = key.getAttribute("content-desc").startsWith("PIN");
				System.out.println(OTP || New);
				key.click();
				if (OTP || New)
				{
					sleep(1000);
					List<WebElement> RemoteOTPs = driver.findElements(By.className("android.view.View"));
					for (WebElement otp : RemoteOTPs)
					{
						System.out.println(otp.getAttribute("content-desc"));
					}
				}

				driver.navigate().back();

				if (i < (total-1))
				{
					WebElement AccessKeys = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Access\nkeys\"]"));
					AccessKeys.click();
					Passcode(driver);
				}
			}
		}
		public static void PBCSettings(AppiumDriver driver){
		WebElement PBC = null;
		try {
			PBC = driver.findElement(By.xpath("//android.view.View[@content-desc=\"PINs, biometric and card settings\"]"));
		}catch (Exception e){}
		if (PBC != null) {
			List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
			int i;
			int total = TS.size();
			System.out.println(total);
			for (i = 0; 1 < total; i++) {
				List<WebElement> ts = driver.findElements(By.className("android.widget.Switch"));
				if (i == 0) {
					WebElement PassageMode = ts.get(i);

					PassageMode.click();
					passageMode(driver);
					sleep(1000);
				}
				if(i==1) {
					WebElement FPEnable = ts.get(i);
					FPEnable.click();
					fingerprint(driver);
				}
//				if (i==2)
//				{
//					WebElement CardEnable = ts.get(i);
//					CardEnable.click();
//				}
//				if (i==3)
//				{
//					WebElement Pins = ts.get(i);
//				}
			}
		}
		}
		public static void passageMode(AppiumDriver driver)
		{
			WebElement PMDisabled = null;
			try{
				PMDisabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode disabled successfully\"]"));
			}
			catch(Exception e){}
			if (PMDisabled == null){
				WebElement psmText = driver.findElement(By.xpath("//android.view.View[@content-desc=\"You are enabling passage mode. Enabling this mode will allow anyone to enter the house without any authentication. Do you want to continue?\"]"));
				boolean allow = psmText.getAttribute("content-desc").endsWith("Do you want to continue?");
				if (allow)
				{
					WebElement Yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
					Yes.click();
				}
				WebElement psmText2 = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Please read this below. Do you still want to continue?\"]"));
				boolean allow2 = psmText2.getAttribute("content-desc").startsWith("Please read this below");
				if(allow2) {
					WebElement Yes2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
					Yes2.click();
				}
				WebElement PMEnabled = null;
				try {
					PMEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Passage Mode Enabled Successfully\"]"));
				}catch(Exception e){}
				if (PMEnabled != null)
				{
					System.out.println("Passage Mode Enabled Successfully");
				}

			}
			else{
				System.out.println("Passage Mode Disabled Successfully. Enabling it again");
				List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
				WebElement PassageMode = TS.get(0);
				PassageMode.click();
				passageMode(driver);
			}

		}
		public static void fingerprint(AppiumDriver driver)
		{
			WebElement FPDisabled = null;
			try {
				FPDisabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Disabled Successfully!\"]"));
			}catch(Exception e){}
			WebElement FPEnabled = null;
			try {
				FPEnabled = driver.findElement(By.xpath("//android.view.View[@content-desc=\"All Fingerprints Enabled Successfully!\"]"));
			}catch(Exception e){}
			if (FPDisabled != null)
			{
				System.out.println("All Fingerprints Disabled Successfully!\n Enabling them again");
				List<WebElement> TS = driver.findElements(By.className("android.widget.Switch"));
				WebElement FPEnable = TS.get(1);
				FPEnable.click();
			}
			else if (FPEnabled != null){
				System.out.println("All Fingerprints Disabled Successfully");
			}
		}
}