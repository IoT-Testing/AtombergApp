package Login;

import Actions.Scroll;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Log {
	public static void Out(IOSDriver driver) {
		try {
			WebElement MoreTab = null;
			try {
				MoreTab = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Select and link device\"]"));
			}catch(Exception ignored){}
			if(MoreTab == null) {
				Tap.withPercentage(driver, 0.83, 0.98);
			}
			sleep(1000);
			Scroll.Up(driver);
			WebElement Logout = null;
			try {
				Logout = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Logout\"]"));
			}catch(Exception ignored){}
			if(Logout == null) {
				Scroll.Up(driver);
				sleep(1000);
				Logout = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Logout\"]"));
			}
			Logout.click();
			Method.captureScreenshot(driver);
			System.out.println("Tap on Logout");
			sleep(2500);
			WebElement Ok = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Yes\"]"));
			Ok.click();

		} catch (Exception exp) {
			System.out.println(exp.getCause());
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
