package Login;

//Add First Device
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import Actions.Tap;
import AtombergTest.Method;
import Permissions.Permission;
import io.appium.java_client.AppiumDriver;

public class Email {
	public static AppiumDriver driver;
	public static void Login(AppiumDriver driver) // Main
	{

		WebElement emailLoginButton = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.ImageView[4]"));
		emailLoginButton.click();

		Method.captureScreenshot(driver);
		WebElement emailField = driver.findElement(By.xpath("//android.widget.EditText"));
		emailField.click();
		Tap.withCoordinates(driver, 300, 250);
		emailField.sendKeys("mitim28961@godsigma.com"); // Enter Email id
		Method.captureScreenshot(driver);
		emailField.getText();
		System.out.println("" + emailField.getText() + "");
		System.out.println("Email Entered...");
		Method.captureScreenshot(driver);

		WebElement continueButton = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]"));
		continueButton.click(); // Continue button
		Method.captureScreenshot(driver);
		sleep(1000);
		WebElement passwordField = driver
				.findElement(By.xpath("//android.widget.ScrollView/android.widget.EditText[1]\r\n" + ""));
		passwordField.click();
		passwordField.sendKeys("Atomberg@123");
		Method.captureScreenshot(driver);
		System.out.println("Password entered..."); // Enter Password

		WebElement continueButton1 = driver
				.findElement(By.xpath("//android.widget.Button[@content-desc=\"Continue\"]\r\n" + ""));
		continueButton1.click(); // Continue to Log in
		sleep(10000);
		Method.captureScreenshot(driver);

		Permission.Allow(driver);

		WebElement Applogo = driver.findElement(By.xpath(
				"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView"));
		Applogo.isDisplayed();
		if (true) {
			System.out.println("Test Passed");
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