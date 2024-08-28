package MoreTab;

import org.awaitility.Awaitility;
import org.mozilla.javascript.ast.WhileLoop;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Actions.Scroll;
import Actions.Swipe;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Help {
    public static void RaC(IOSDriver driver) {
        WebElement RaiseComplaint = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Raise a complaint\"]"));
        RaiseComplaint.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Raise Complaint");
        Awaitility.await().until(() -> driver.findElement(By.xpath("//XCUIElementTypeTextField[@index=\"6\"]")).isDisplayed());
        Tap.withCoordinates(driver, 35, 125);
    }

    public static void TC(IOSDriver driver) {
        WebElement TrackComplaint = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Track complaints\"]"));
        TrackComplaint.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Track Complaint");
        sleep(2000);

        WebElement NoComplaints = null;
        try {
            NoComplaints = driver.findElement(By.xpath(
                    "//XCUIElementTypeScrollView"));
        } catch (Exception exp) {}
        if (NoComplaints != null) {
            System.out.println("No Complaints Raised.");
        }
        Tap.withCoordinates(driver,35,126);
    }

    public static void Videos(IOSDriver driver) {
        WebElement AppTour = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"App Tour\"]"));
        AppTour.click();
        Method.captureScreenshot(driver);
        System.out.println("App Video Opened");
        Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);
        sleep(2000);
        WebElement ConnectAlexa = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Connect Alexa\"]"));
        ConnectAlexa.click();
        Method.captureScreenshot(driver);
        System.out.println("Alexa Video Opened");
        Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);
        sleep(2000);

        WebElement ConnectGoogle = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Connect Google\"]"));
        ConnectGoogle.click();
        Method.captureScreenshot(driver);
        System.out.println("Google Home Video Opened");
        Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);
        sleep(5000);

        WebElement SLAppSetup = null;
        try {
            SLAppSetup = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Smart Locks App Setup\"]"));
        } catch (Exception exp) {
        }
        if (SLAppSetup == null) {
            Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50
            WebElement SLInstall = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Smart Locks Installation\"]"));
            SLInstall.click();
            sleep(2000);
        } else {
            WebElement SLInstall = driver
                    .findElement(By.xpath("//XCUIElementTypeImage[@name=\"Smart Locks Installation\"]"));
            SLInstall.click();
        }
        Method.captureScreenshot(driver);
        System.out.println("SL installation Video Opened");
        Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);
        sleep(5000);

        SLAppSetup = null;
        try {
            SLAppSetup = driver
                    .findElement(By.xpath("//XCUIElementTypeImage[@name=\"Smart Locks App Setup\"]"));
        } catch (Exception exp) {
        }
        if (SLAppSetup == null) {
            Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50
            sleep(2000);
        }
        WebElement SLFeatures = driver
                .findElement(By.xpath("//XCUIElementTypeImage[@name=\"Smart Locks Features\"]"));
        SLFeatures.click();
        sleep(2000);
        Method.captureScreenshot(driver);
        System.out.println("SL Feature Video Opened");
        Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);

        sleep(5000);

        SLAppSetup = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Smart Locks App Setup\"]"));
        SLAppSetup.click();
        sleep(2000);
        Method.captureScreenshot(driver);
        System.out.println("App Setup for Lock Video Opened");
        try{Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);
    }catch (Exception e){e.printStackTrace();}
    }

    public static void Manual(IOSDriver driver) {
        WebElement manual = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Manual\"]"));
        manual.click();
        System.out.println("Manual Open");
        sleep(1000);
        try{Tap.withCoordinates(driver,22,35);
            Tap.withCoordinates(driver,22,35);
            Tap.withCoordinates(driver,22,35);
        }catch (Exception e){e.printStackTrace();}

    }

    public static void Troubleshoot(IOSDriver driver) {

        driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Troubleshoot\"]")).click();
        // Troubleshoot for Fans

        WebElement fan = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Atomberg Fan\"]"));
        fan.click();
        Method.captureScreenshot(driver);
        System.out.println("Fan Troubleshoot");

        WebElement renesa = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa\"]"));
        renesa.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa");
        OK(driver);

        WebElement renesaSmart = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa Smart\"]"));
        renesaSmart.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa Smart");
        ReturnToHome(driver);

        WebElement renesaPlus = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa+\"]"));
        renesaPlus.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa Plus");
        OK(driver);

        WebElement renesaSmartPlus = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa Smart+\"]"));
        renesaSmartPlus.click();
        Method.captureScreenshot(driver);
        System.out.println("Renesa Smart +");
        EnterSerialNumber(driver);

        WebElement studioPlus = driver
                .findElement(By.xpath("//XCUIElementTypeImage[@name=\"Studio+\"]"));
        studioPlus.click();
        Method.captureScreenshot(driver);
        OK(driver);

        WebElement studioSmartPlus = driver
                .findElement(By.xpath("//XCUIElementTypeImage[@name=\"Studio Smart+\"]"));
		studioSmartPlus.click();
        Method.captureScreenshot(driver);
        System.out.println("Studio Plus");
		ReturnToHome(driver);

		WebElement erica =driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Erica\"]"));
		erica.click();
		Method.captureScreenshot(driver);
		System.out.println("Erica");
		OK(driver);

		WebElement ericaSmart =driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Erica Smart\"]"));
		ericaSmart.click();
		Method.captureScreenshot(driver);
		System.out.println("Erica Smart");
		ReturnToHome(driver);
		Scroll.Up(driver);
        Sleep(1500);
        WebElement starlight = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Aris Starlight\"]"));
        starlight.click();
        Method.captureScreenshot(driver);
        System.out.println("Starlight");
        ReturnToHome(driver);

        WebElement aris = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Aris\"]"));
        aris.click();
        Method.captureScreenshot(driver);
        System.out.println("Aris");
		ReturnToHome(driver);

		WebElement arisContour = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Aris Contour\"]"));
		arisContour.click();
		Method.captureScreenshot(driver);
		System.out.println("Aris");
		ReturnToHome(driver);

		WebElement renesaAlpha = driver
				.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Renesa Alpha\"]"));
		renesaAlpha.click();
		Method.captureScreenshot(driver);
		System.out.println("RenesaAlpha");
		OK(driver);


		WebElement efficio = driver
                .findElement(By.xpath("//XCUIElementTypeImage[@name=\"Efficio\"]"));
        efficio.click();
        Method.captureScreenshot(driver);
        System.out.println("Efficio");
        OK(driver);

        WebElement ikano = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Ikano\"]"));
        ikano.click();
        Method.captureScreenshot(driver);
        System.out.println("Ikano");
        OK(driver);
		Scroll.Up(driver);
        Sleep(2000);
        WebElement ozeo = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Ozeo\"]"));
        sleep(1000);
        ozeo.click();
        Method.captureScreenshot(driver);
        System.out.println("Ozeo");
        OK(driver);

        WebElement ameza = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Ameza\"]"));
        ameza.click();
        Method.captureScreenshot(driver);
        System.out.println("Ameza");
        OK(driver);

        WebElement other = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Other\"]"));
        other.click();
        Method.captureScreenshot(driver);
        System.out.println("Other");
		OK(driver);
        Tap.withCoordinates(driver,35,125);

        // Troubleshoot for Locks
        WebElement lock = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Atomberg Lock\"]"));
        lock.click();
        Method.captureScreenshot(driver);
        System.out.println("Lock Troubleshoot");
        WebElement ReturnToHome = null;
        try{                                                            //XCUIElementTypeButton[@name="Return to Home"]
            ReturnToHome = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Return to Home\"]"));
        }catch (Exception e){}
        if(ReturnToHome!=null){
            ReturnToHome.click();
        }
        WebElement troubleshoot = null;
        try{
            troubleshoot = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Troubleshoot\"]"));
        }catch (Exception e){}
        if (troubleshoot!=null){
            Tap.withCoordinates(driver,35,125);
        }

    }

    private static void ReturnToHome(IOSDriver driver) {
        WebElement ReturnToHome = null;
        try{                                                            //XCUIElementTypeButton[@name="Return to Home"]
            ReturnToHome = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Return to home\"]"));
        }catch (Exception e){}
        if(ReturnToHome!=null){
            ReturnToHome.click();
        }
        Method.captureScreenshot(driver);
        System.out.println("Return To Home");

    }

    private static void EnterSerialNumber(IOSDriver driver) {
        WebElement ManualEnter = driver.findElement(By.xpath("//XCUIElementTypeTextField[@name=\"eg. 2D20A5EDH4XXXXX\"]"));
        ManualEnter.click();
        Method.captureScreenshot(driver);
        System.out.println("Enter Barcode Manually...");

        WebElement ScanBarcode = driver.findElement(By.xpath("//XCUIElementTypeApplication[@name=\"Atomberg Home\"]/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[3]/XCUIElementTypeImage"));
        ScanBarcode.click();
        Method.captureScreenshot(driver);
        System.out.println("Scan Barcode ...");

        WebElement AllowCamera = null;
        try {
            sleep(2000);
            AllowCamera = driver.findElement(By.xpath("//XCUIElementTypeAlert[@name=\"“Atomberg Home” Would Like to Access the Camera\"]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeScrollView[1]/XCUIElementTypeOther[1]"));
        } catch (Exception exp) {
        }
        if (AllowCamera != null) {

            Method.captureScreenshot(driver);
            WebElement Camera = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Allow\"]"));
            Camera.click();
            Method.captureScreenshot(driver);

        }
        sleep(2000);
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Cancel\"]")).click();
        WebElement ContactSupport = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Can't find serial number? Contact support\"]"));
        ContactSupport.click();
        Method.captureScreenshot(driver);
        System.out.println("Contact Support ...");

        WebElement Email = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Email\"]"));
        Email.click();
        Method.captureScreenshot(driver);
        System.out.println("Email ...");

        Tap.withCoordinates(driver, 22, 35);
        Tap.withCoordinates(driver, 22, 35);
        Tap.withCoordinates(driver, 22, 35);

        WebElement Call = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Call\"]"));
        Call.click();
        Method.captureScreenshot(driver);
        System.out.println("Call ...");
        Tap.withCoordinates(driver,190,790);
        Tap.withCoordinates(driver, 50, 50);
        Tap.withCoordinates(driver, 50, 50);
        Tap.withCoordinates(driver, 50, 50);
        Sleep(2000);
        driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Check\"]")).click();
        Sleep(2000);
        Tap.withCoordinates(driver, 50, 50);
        Tap.withCoordinates(driver, 50, 50);
        Sleep(2000);
        Tap.withCoordinates(driver, 35, 125);
    }

    private static void OK(IOSDriver driver) {
        WebElement OK = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Ok\"]"));
        OK.click();
        Method.captureScreenshot(driver);
        System.out.println("OK ...");

    }

	public static void email(IOSDriver driver) {
		WebElement emailUs = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Email us\"]"));
		emailUs.click();
        Sleep(1000);
        Tap.withCoordinates(driver,22,35);
        Tap.withCoordinates(driver,22,35);
	}

	public static void call(IOSDriver driver) {
		WebElement callUs =driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Call us\"]"));
		callUs.click();
        Sleep(1000);
        Tap.withCoordinates(driver,190,790);
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
