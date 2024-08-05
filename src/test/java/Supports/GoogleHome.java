package Supports;

import Actions.Tap;
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
			List<WebElement> algL = elements.stream().filter(element -> element.getAttribute("content-desc").equals("Account linking guide")).collect(Collectors.toList());

			if (!algL.isEmpty())
			{
				driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]")).click();
				sleep(1000);
				System.out.println("OK");
				sleep(5000);
				driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]")).click();
				System.out.println("Continue");

				gCheck(driver);// check if account is present

			}
		}
	private static void gCheck(AppiumDriver driver){
		List<WebElement> CHECK =driver.findElements(By.className("android.widget.Button"));
		System.out.println("Check");
		List<WebElement> check = CHECK.stream().filter(element -> element.getAttribute("text").equals("Sign In as Weker42331@huleos.com")).collect(Collectors.toList());
		System.out.println(check.size());
		if(!CHECK.isEmpty())
		{
			CHECK.get(0).click();
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

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
}