package Supports;

import Actions.Swipe;
import io.appium.java_client.AppiumDriver;
import io.opentelemetry.exporter.logging.SystemOutLogRecordExporter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class GoogleHome {
	public static void Connect(AppiumDriver driver) {

		WebElement MoreTab = null;
		try {
			MoreTab = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
		} catch (Exception e) {
		}

		if (MoreTab != null) {
			GCheck(driver);
		} else {
			WebElement element = null;
			try {
				element = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Devices\"]"));
			} catch (Exception e) {
				System.out.println("Element not found");
			}
			if (element != null) {
				Swipe.screenRight(driver);
				GCheck(driver);
			}
			
		}
		driver.findElement(By.xpath("//android.view.View[@content-desc=\"Account linking guide\"]")).isDisplayed();
		if (true) {
			driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]")).click();
			sleep(5000);

			WebElement Continue = driver.findElement(By.xpath("//android.widget.Button[@text=\"Continue\"]"));
			Continue.click();
			WebElement SignIn = null;
			try {
				SignIn = driver.findElement(By.xpath("//android.widget.Button[@text=\"Sign In as Weker42331@huleos.com\"]"));
			}catch(Exception ignored){}
			if (SignIn == null) {
				WebElement Email = driver.findElement(By.xpath("//android.webkit.WebView[@text=\"Signin\"]/android.view.View/android.view.View/android.view.View[2]/android.view.View[2]/android.widget.EditText[1]"));
				Email.click();
				Email.sendKeys("Weker42331@huleos.com");

				WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@index=\"3\"]"));
				Password.click();
				Password.sendKeys("Atomberg@123");
				sleep(1000);

				driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();

			}
			else
			{
				SignIn.click();
			}
			sleep(10000);
				WebElement Done = driver.findElement(By.id("com.google.android.apps.chromecast.app:id/bottom_button"));
				Done.click();
				sleep(3000);

				WebElement Devices = driver.findElement(By.xpath("//android.widget.TextView[@resource-id=\"com.google.android.apps.chromecast.app:id/navigation_bar_item_small_label_view\" and @text=\"Devices\"]"));
				Devices.click();
			ADevices(driver);
			driver.navigate().back();
			driver.navigate().back();
			System.out.println("On More Tab");
		}

	}
	private static void ADevices(AppiumDriver driver)
	{
		List<WebElement> Fans = driver.findElements(By.className("android.view.ViewGroup"));
		for (WebElement fan: Fans)
		{
			System.out.println(fan.getAttribute("content-desc"));
		}
	}
	private static void GCheck(AppiumDriver driver){
		WebElement GoogleConnect = null;
		try {
			GoogleConnect = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Google\nConnect\"]"));
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
		if (GoogleConnect != null) {
			GoogleConnect.click();
		} else {
			System.out.println("Google is already connected");
		}
	}

	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}