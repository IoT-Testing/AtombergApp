package MoreTab;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Scroll;
import Actions.Swipe;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

import java.util.concurrent.TimeUnit;

public class Help {
    public static void RaC(AppiumDriver driver) {
        // Tap on Raise a complaint
        WebElement RaiseComplaint = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Raise a complaint\"]"));
        RaiseComplaint.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Raise Complaint");
        Awaitility.await().until(() -> driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[1]")).isDisplayed());
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

    private static void VideoTryCatch(AppiumDriver driver) {
        WebElement VideoTutorials = null;
        try {
            VideoTutorials = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Video tutorials\"]"));
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
            Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50
            sleep(2000);
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

        SLAppSetup = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        SLAppSetup.click();
        sleep(2000);
        Method.captureScreenshot(driver);
        System.out.println("App Setup for Lock Video Opened");
        VideoTryCatch(driver);
        VideoTryCatch(driver);

    }

    public static void Manual(AppiumDriver driver) {
        WebElement manual = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Manual\"]"));
        manual.click();
        System.out.println("Manual Open");
        sleep(1000);
        driver.findElement(By.id("android:id/button1")).click();
        Sleep(2500);
        driver.navigate().back();
    }

    public static void Troubleshoot(AppiumDriver driver) {

        driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Troubleshoot\"]")).click();
        // Troubleshoot for Fans

        WebElement fan = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Fan\"]"));
        fan.click();
        Method.captureScreenshot(driver);
        System.out.println("Fan Troubleshoot");

        WebElement renesa = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]"));
        renesa.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa");
        OK(driver);

        WebElement renesaSmart = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Smart\"]"));
        renesaSmart.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa Smart");
        ReturnToHome(driver);

        WebElement renesaPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]"));
        renesaPlus.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa Plus");
        OK(driver);

        WebElement renesaSmartPlus = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Smart+\"]"));
        renesaSmartPlus.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa Smart +");
        EnterSerialNumber(driver);

        WebElement studioPlus = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]"));
        studioPlus.click();
        Method.captureScreenshot(driver);
        OK(driver);

        WebElement studioSmartPlus = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Studio Smart+\"]"));
		studioSmartPlus.click();
        Method.captureScreenshot(driver);
        System.out.println("Studio Plus");
		ReturnToHome(driver);

		WebElement erica =driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica\"]"));
		erica.click();
		Method.captureScreenshot(driver);
		System.out.println("Erica");
		OK(driver);

		WebElement ericaSmart =driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Erica Smart\"]"));
		ericaSmart.click();
		Method.captureScreenshot(driver);
		System.out.println("Erica Smart");
		ReturnToHome(driver);
		Scroll.Up(driver);

        WebElement starlight = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Starlight\"]"));
        starlight.click();
        Method.captureScreenshot(driver);
        System.out.println("Starlight");
        ReturnToHome(driver);

        WebElement aris = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris\"]"));
        aris.click();
        Method.captureScreenshot(driver);
        System.out.println("Aris");
		ReturnToHome(driver);

		WebElement arisContour = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Aris Contour\"]"));
		arisContour.click();
		Method.captureScreenshot(driver);
		System.out.println("Aris");
		ReturnToHome(driver);

		WebElement renesaAlpha = driver
				.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Alpha\"]"));
		renesaAlpha.click();
		Method.captureScreenshot(driver);
		System.out.println("RenesaAlpha");
		OK(driver);


		WebElement efficio = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Efficio\"]"));
        efficio.click();
        Method.captureScreenshot(driver);
        System.out.println("Efficio");
        OK(driver);

        WebElement ikano = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ikano\"]"));
        ikano.click();
        Method.captureScreenshot(driver);
        System.out.println("Ikano");
        OK(driver);
		Scroll.Up(driver);

        WebElement ozeo = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ozeo\"]"));
        ozeo.click();
        Method.captureScreenshot(driver);
        System.out.println("Ozeo");
        OK(driver);

        WebElement ameza = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Ameza\"]"));
        ameza.click();
        Method.captureScreenshot(driver);
        System.out.println("Ameza");
        OK(driver);

        WebElement other = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Other\"]"));
        other.click();
        Method.captureScreenshot(driver);
        System.out.println("Other");
		OK(driver);
        driver.navigate().back();

        // Troubleshoot for Locks
        WebElement lock = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Atomberg Lock\"]"));
        lock.click();
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
        ManualEnter.click(); // Tap on the edit text field
        Method.captureScreenshot(driver);
        System.out.println("Enter Barcode Manually...");

        WebElement ScanBarcode = driver.findElement(By.xpath("//android.widget.EditText/android.widget.ImageView"));
        ScanBarcode.click();// Scan the barcode by opening the camera
        Method.captureScreenshot(driver);
        System.out.println("Scan Barcode ...");

        WebElement AllowCamera = null;// Checking if the permission for Camera is asked
        try {
            sleep(2000);
            AllowCamera = driver.findElement(By.id("com.android.permissioncontroller:id/permission_message"));

        } catch (Exception exp) {}
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

        WebElement Call = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Call\"]"));
        Call.click();
        Method.captureScreenshot(driver);
        System.out.println("Call ...");

		WebElement id = null;
		while(id == null)
		{
			driver.navigate().back();
			try {
				id=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Identify your device\"]"));
			}catch (Exception e)
			{}
		}

    }

    private static void OK(AppiumDriver driver) {
        WebElement OK = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Ok\"]"));
        OK.click();
        Method.captureScreenshot(driver);
        System.out.println("OK ...");

    }

	public static void email(AppiumDriver driver)
	{
		WebElement emailUs = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Email us\"]"));
		emailUs.click();
		driver.navigate().back();
	}

	public static void call(AppiumDriver driver)
	{
		WebElement callUs =driver.findElement(By.xpath("//android.view.View[@content-desc=\"Call us\"]"));
		callUs.click();
		WebElement id = null;
		while(id == null)
		{
			driver.navigate().back();
			try {
				id=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
			}catch (Exception e)
			{}
		}
	}

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

    private static void Sleep(long millis){
        try {
            Thread.sleep(millis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
