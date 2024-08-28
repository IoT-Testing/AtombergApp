package Supports;

import Actions.Tap;
import com.sun.jarsigner.ContentSignerParameters;
import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GoogleHome {
	public static void Connect(IOSDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebElement SLD = null;
		try {
			SLD = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select and link device\"]"));
		} catch (Exception e) {
		}
		if (SLD != null) {
			googleHome(driver);			//Google Home
		} else {
			driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"More\n" +
					"Tab 3 of 3\"]")).click();
			googleHome(driver);
		}
		ALG(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	private static void googleHome(IOSDriver driver) {
		WebElement googleConnect = null;
		try{
			googleConnect = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"Google\nConnect\"]"));
		}catch (Exception e) {
		}
		if (googleConnect != null) {
			System.out.println("Connecting Google Home");
			googleConnect.click();
		} else {
			System.out.println("Google Home is already connected");
		}
	}

	public static void ALG(IOSDriver driver) {//Account Linking Guide
			List<WebElement> Elements = driver.findElements(By.className("XCUIElementTypeStaticText"));
			List<WebElement> elements = Elements.stream().filter(element -> element.getAttribute("name")!=null).collect(Collectors.toList());
			System.out.println(elements.size());
			List<WebElement> algL = elements.stream().filter(element -> element.getAttribute("name").equals("Account linking guide")).collect(Collectors.toList());

			if (!algL.isEmpty())
			{
				WebElement OK = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"OK\"]"));
				OK.click();
				sleep(1000);
				System.out.println("OK");
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				checkContinue(driver);

				gCheck(driver);// check if account is present

			}
		}
	private static void gCheck(IOSDriver driver){
		List<WebElement> CHECK =driver.findElements(By.className("XCUIElementTypeButton"));
		System.out.println("Check");
		System.out.println(CHECK.size());
		if(!CHECK.isEmpty())
		{
			WebElement signIn = driver.findElement(By.xpath("//XCUIElementTypeButton[@text=\"Sign In as Weker42331@huleos.com\"]"));
			signIn.click();
		}
		else{
			WebElement Email = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
			Email.click();
			Email.sendKeys("Weker42331@huleos.com");
			WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
			Password.click();
			Password.sendKeys("Atomberg@123");
			sleep(1000);
			driver.findElement(By.xpath("//XCUIElementTypeButton[@text=\"submit\"]")).click();
		}
		System.out.println("Account entered");
		Sleep(5000);
		WebElement done = driver.findElement(By.xpath("//XCUIElementTypeButton[@text=\"Done\"]"));
		done.click();
		back(driver);
}
	private static void back(IOSDriver driver){
		WebElement SLD =null;
		while(SLD == null)
		{
			driver.navigate().back();
			try {
				SLD=driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select and link device\"]"));
			}catch (Exception e)
			{}
		}
	}

	private static void checkContinue(IOSDriver driver){
		WebElement continueBt = null;
		try {
			continueBt = driver.findElement(By.xpath("//XCUIElementTypeButton[@text=\"Continue\"]"));
		}catch (Exception e){}
		if(continueBt != null)
		{
			continueBt.click();
		}
		else {
			Tap.withCoordinates(driver, 890, 1290);
		}
	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
	private static void Sleep(long millis) {
		try{Thread.sleep(millis);}catch (Exception e){}
	}
}