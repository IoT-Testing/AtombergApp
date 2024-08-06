package Supports;

import Actions.Tap;
import com.sun.jarsigner.ContentSignerParameters;
import io.appium.java_client.AppiumDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class GoogleHome {
	public static void Connect(AppiumDriver driver) {
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		WebElement SLD = null;
		try {
			SLD = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
		} catch (Exception e) {
		}
		if (SLD != null) {
			googleHome(driver);			//Google Home
		} else {
			driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
					"Tab 3 of 3\"]")).click();
			googleHome(driver);
		}
		ALG(driver);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
	}

	private static void googleHome(AppiumDriver driver) {
		WebElement googleConnect = null;
		try{
			googleConnect = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Google\nConnect\"]"));
		}catch (Exception e) {
		}
		if (googleConnect != null) {
			System.out.println("Connecting Google Home");
			googleConnect.click();
		} else {
			System.out.println("Google Home is already connected");
		}
	}

	public static void ALG(AppiumDriver driver) {//Account Linking Guide
			List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
			List<WebElement> elements = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
			System.out.println(elements.size());
			List<WebElement> algL = elements.stream().filter(element -> element.getAttribute("content-desc").equals("Account linking guide")).collect(Collectors.toList());

			if (!algL.isEmpty())
			{
				WebElement OK = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]"));
				OK.click();
				sleep(1000);
				System.out.println("OK");
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
				checkContinue(driver);

				gCheck(driver);// check if account is present

			}
		}
	private static void gCheck(AppiumDriver driver){
		List<WebElement> CHECK =driver.findElements(By.className("android.widget.Button"));
		System.out.println("Check");
		System.out.println(CHECK.size());
		if(!CHECK.isEmpty())
		{
			WebElement signIn = driver.findElement(By.xpath("//android.widget.Button[@text=\"Sign In as Weker42331@huleos.com\"]"));
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
			driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();
		}
		System.out.println("Account entered");
		Sleep(5000);
		WebElement done = driver.findElement(By.xpath("//android.widget.Button[@text=\"Done\"]"));
		done.click();
		back(driver);
}
	private static void back(AppiumDriver driver){
		WebElement SLD =null;
		while(SLD == null)
		{
			driver.navigate().back();
			try {
				SLD=driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
			}catch (Exception e)
			{}
		}
	}

	private static void checkContinue(AppiumDriver driver){
		WebElement continueBt = null;
		try {
			continueBt = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
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