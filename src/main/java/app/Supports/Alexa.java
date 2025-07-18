package app.Supports;

import app.util.ActionsUtil.Tap;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;
import app.util.ActionsUtil.Swipe;

import java.util.List;
import java.util.stream.Collectors;

public class Alexa {
	public static void Connect(AndroidDriver driver) {

		WebElement SLD = null;
		try {
			SLD = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
		} catch (Exception e) {
		}
		if (SLD != null) {
			alexa(driver);
		} else {
			driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
					"Tab 3 of 3\"]")).click();
			alexa(driver);
		}

	}

	private static void alexa(AndroidDriver driver) {
		WebElement alexaConnect = null;
		try {
			alexaConnect = driver
					.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnect\"]"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (alexaConnect != null) {
			alexaConnect.click();
		} else {
			System.out.println("Alexa is already connected");
		}
		//check alexa linking guide
		ALG(driver);
		linkCheck(driver);
	}

	private static void ALG(AndroidDriver driver) {

		driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Link\"]")).click();

		List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
		List<WebElement> elements = Elements.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
		List<WebElement> algL = elements.stream().filter(element -> element.getDomAttribute("content-desc").equals("Account linking guide")).collect(Collectors.toList());
		System.out.println(algL.size());
		if (!algL.isEmpty())
		{
			driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"OK\"]")).click();
			sleep(5000);
			driver.findElement(By.xpath("//android.widget.TextView[@text=\"LINK\"]")).click();
			sleep(5000);

			alexaCheck(driver);
		}
	}

	private static void alexaCheck(AndroidDriver driver){
		WebElement CHECK = null;
		try {
			CHECK = driver.findElement(By.xpath("//android.widget.TextView[@text=\"Sign in with your email and password\"]"));
		}catch(Exception e){}
		if(CHECK != null)
		{
			WebElement Email = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormUsername\"]"));
			Email.click();
			Email.sendKeys("Weker42331@huleos.com");
			WebElement Password = driver.findElement(By.xpath("//android.widget.EditText[@resource-id=\"signInFormPassword\"]"));
			Password.click();
			Password.sendKeys("Atomberg@123");
			sleep(1000);
			driver.findElement(By.xpath("//android.widget.Button[@text=\"submit\"]")).click();
		}

	}

	private static void back(AndroidDriver driver){
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

	private static void linkCheck(AndroidDriver driver){
		List<WebElement> Success = driver.findElements(By.className("android.view.View"));
		List<WebElement> successM = Success.stream().filter(element -> element.getDomAttribute("content-desc")!=null).collect(Collectors.toList());
		for(WebElement e:successM)
			if(e.getDomAttribute("content-desc").equals("Alexa Linked Successfully")){
				System.out.println("Alexa Linked Successfully");
				Tap.withPercentage(driver, 0.20,0.20);
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