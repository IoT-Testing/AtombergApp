package Supports;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;
import Actions.Swipe;

public class Alexa {
	public static void Connect(AppiumDriver driver) {

		WebElement MoreTab = null;
		try {
			MoreTab = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
		} catch (Exception e) {
		}

		if (MoreTab != null) {
			WebElement alexaConnect = null;
			try {
				alexaConnect = driver
						.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnect\"]"));
			} catch (Exception e) {
				System.out.println(e);
				e.printStackTrace();
			}
			if (alexaConnect != null) {
				alexaConnect.click();
			} else {
				System.out.println("Alexa is alredy connected");
			}
		} else {
			WebElement element = null;
			try {
				element = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Devices\"]"));
			} catch (Exception e) {
				System.out.println("Element not found");
			}
			if (element != null) {
				Swipe.screenRight(driver);
			}
			
			Connect(driver);
		}
		String pairAlexa = "Ensure that Alexa app is installed in your phone to pair it with the Atomberg Home";
		WebElement PairAlexa = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Ensure that Alexa app is installed in your phone to pair it with the Atomberg Home\"]"));
		if (pairAlexa == PairAlexa.getText()) {
			System.out.println(PairAlexa.getText());
			sleep(1000);
			System.out.println("Text on the dialogue box is correct");
		} else {
			System.out.println("Text on the dialogue box is incorrect");
		}
		
		driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Link\"]")).click();
		sleep(5000);
		driver.findElement(By.xpath("//android.view.View[@content-desc=\"Account linking guide\"]")).isDisplayed();
		if (true) {
			driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]")).click();
			sleep(5000);
			driver.findElement(By.xpath("//android.widget.TextView[@text=\"LINK\"]")).click();
			sleep(5000);

			WebElement Email = driver.findElement(By.xpath("//android.webkit.WebView[@text=\"Signin\"]/android.view.View/android.view.View/android.view.View[2]/android.view.View[2]/android.widget.EditText[1]"));
			Email.click();
			Email.sendKeys("Weker42331@huleos.com");
			
			WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@index=\"3\"]"));
			Password.click();
			Password.sendKeys("Atomberg@123");
			sleep(1000);

			driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();

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