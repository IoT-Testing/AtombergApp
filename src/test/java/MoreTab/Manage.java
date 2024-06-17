package MoreTab;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import Actions.Scroll;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;

public class Manage {
	public static AppiumDriver driver;
	public static void Family(AppiumDriver driver) {
	WebElement ManageFamily = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
	ManageFamily.click();
	Method.captureScreenshot(driver);
	
	System.out.println("Tap on Manage Family");
	}
	public static void Member(AppiumDriver driver) {
		
		WebElement RemoveMember = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Remove Member\"]"));
		RemoveMember.click();
		 
		System.out.println("Remove Member");
		Method.captureScreenshot(driver);
		
		WebElement Cancel2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
		Cancel2.click();

		WebElement MakeAdmin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Make Admin\"]"));
		MakeAdmin.click();
		 
		System.out.println("Make Admin");
		Method.captureScreenshot(driver);
		

		WebElement Cancel3 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
		Cancel3.click();
		driver.navigate().back();

		WebElement AddMember = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]"));
		AddMember.click();
		 
		Method.captureScreenshot(driver);
		sleep(3000);

		WebElement Share = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Share\"]"));
		Share.click();
		
		Method.captureScreenshot(driver);
		

		driver.navigate().back();
		driver.navigate().back();
		driver.navigate().back();
	}
	public static void AddHome(AppiumDriver driver) {
		
		WebElement AddHome = null;

		try {
			AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
		} catch (Exception exp) {
		}
		if (AddHome == null) {
			Scroll.Up(driver);
			
			AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
		}

		AddHome.click();
		
		Method.captureScreenshot(driver);
		

		Tap.withPercentage(driver, 0.50, 0.75); // Narzo 0.50, 0.625 Tab 0.50, 0.75
		
		Method.captureScreenshot(driver);
		

		WebElement HomeName = driver.findElement(By.xpath("//android.widget.EditText"));
		HomeName.click();
		
		HomeName.sendKeys("Script");
		Method.captureScreenshot(driver);
		

		WebElement Create = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create\"]"));
		Create.click();
		 
		Method.captureScreenshot(driver);
		sleep(5000);

	}
	public static void Home(AppiumDriver driver) {
		
		WebElement FamilyEdit = driver
				.findElement(By.xpath("//android.view.View[@content-desc=\"Manage Home\"]/android.view.View"));
		FamilyEdit.click();
		 
		System.out.println("Family Edit");
		Method.captureScreenshot(driver);
		

		WebElement LeaveHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Leave Home\"]"));
		LeaveHome.click();
		 
		System.out.println("Leave home");
		Method.captureScreenshot(driver);
		
		WebElement Cancel1 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
		Cancel1.click();
		
		WebElement DeleteHome = null;
		try {
			DeleteHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete Home\"]"));
		} catch (Exception exp) {
		}
		if (DeleteHome != null) {
			DeleteHome.click();
			 
			System.out.println("Delete home");
			Method.captureScreenshot(driver);
			
			WebElement Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
			Cancel.click(); // android.widget.Button[@content-desc="Cancel"]
			
		}
		driver.navigate().back();
	}
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
