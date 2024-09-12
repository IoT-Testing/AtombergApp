package Login;

import Actions.Scroll;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Log {

	//Login Methods
	public static void In(AppiumDriver driver) {
		try {

			int randomNumber = (int) (Math.random() * 4); // generate a random number between 0 and 5
			switch (randomNumber) {
				case 0:
					System.out.println("Login with Apple");
					Apple.Login(driver);
					break;
				case 1:
					System.out.println("Login with Email");
					Email.Login(driver);
					break;
				case 2:
					System.out.println("Login with Facebook");
					FB.Login(driver);
					break;

				case 3:
					System.out.println("Login with Google");
					Google.Login(driver);
					break;
			}
		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	//Logout from any screen
	public static void Out(AppiumDriver driver) {
		try {
			WebElement MoreTab = null;
			try {
				MoreTab = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
			}catch(Exception ignored){}
			if(MoreTab == null) {
				Tap.withPercentage(driver, 0.83, 0.98);
			}
			sleep(1000);
			Scroll.Up(driver);
			WebElement Logout = null;
			try {
				Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
			}catch(Exception ignored){}
			if(Logout == null) {
				Scroll.Up(driver);
				sleep(1000);
				Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
			}
			Logout.click();
			Method.captureScreenshot(driver);
			System.out.println("Tap on Logout");
			sleep(2500);
			WebElement Ok = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
			Ok.click();

		} catch (Exception exp) {
			System.out.println(exp.getMessage());
			exp.printStackTrace();
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