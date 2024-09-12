package Supports;

import Actions.Tap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Alexa {
	public static void Connect(AppiumDriver driver) {

		WebElement SLD = null;
		try {
			SLD = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
		} catch (Exception e) {
		}
		if (SLD != null) {
			System.out.println("Already in More Tab");
			alexa(driver);
		} else {
			driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
					"Tab 3 of 3\"]")).click();
			alexa(driver);
		}

	}

	private static void alexa(AppiumDriver driver) {
		WebElement alexaConnect = null;
		try {
			alexaConnect = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnect\"]"));
		} catch (Exception e) {
		}
		if (alexaConnect != null) {
			System.out.println("Connecting With Alexa");
			alexaConnect.click();
			//check alexa linking guide
			ALG(driver);
			linkCheck(driver);
		} else {
			System.out.println("Alexa is already connected");
		}
	}

	private static void ALG(AppiumDriver driver) {

		driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Link\"]")).click();

		List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
		List<WebElement> elements = Elements.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
		List<WebElement> algL = elements.stream().filter(element -> Objects.equals(element.getAttribute("content-desc"), "Account linking guide")).collect(Collectors.toList());
		System.out.println(algL.size());
		if (!algL.isEmpty())
		{
			driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]")).click();
			System.out.println("Ok");
			sleep(5000);
			driver.findElement(By.xpath("//android.widget.TextView[@text=\"LINK\"]")).click();
			System.out.println("LINK");
			sleep(5000);
		}
	}

	private static void alexaCheck(AppiumDriver driver){
		WebElement CHECK = null;
		try {
			System.out.println("checking for Login Screen");
			CHECK = driver.findElement(By.xpath("//android.widget.TextView[@text=\"Sign in with your email and password\"]"));
		}catch(Exception e){}
		if(CHECK != null)
		{
			WebElement Email = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
			Email.click();
			Email.sendKeys("hiwitaw422@wuzak.com");
			WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
			Password.click();
			Password.sendKeys("Atomberg@123");
			sleep(1000);
			driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();
		}

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

	private static void linkCheck(AppiumDriver driver){
		List<WebElement> Success = driver.findElements(By.className("android.view.View"));
		List<WebElement> successM = Success.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
		System.out.println(successM.size());
		for(WebElement e:successM)
			if(Objects.equals(e.getAttribute("content-desc"), "Alexa Linked Successfully")){
				System.out.println("Alexa Linked Successfully");
				Tap.withPercentage(driver, 0.20,0.20); // Tap to remove the dialogue box
//				sleep(2000);
			}
		else{
				alexaCheck(driver);
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