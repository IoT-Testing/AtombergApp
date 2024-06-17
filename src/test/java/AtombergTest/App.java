package AtombergTest; //To check 

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import Actions.Scroll;
import Actions.Tap;
import Login.Email;
import io.appium.java_client.AppiumDriver;

public class App {
	public static AppiumDriver driver;

	public static void main(String[] args) {
		/**
		 * @author Rohit
		 */
		try {
			openAtomberg();
			Email.Login(driver);
			sleep(5000);
			AddButton(driver);
			for(int c = 0 ; c<10; c++) {
				sleep(15000);
				WebElement element = null;
				String xpathExpression = "//android.view.View[@content-desc=\"Atomberg Smart Fan\"]";
				// Search Fan Only
				try {
					element = driver.findElement(By.xpath(xpathExpression));
				} catch (NoSuchElementException ignored) {
				}

				if (element != null) // if device is available
				{
			List<WebElement> Connects = driver.findElements(By.xpath("(//android.view.View[@content-desc=\"Connect\"])"));
			System.out.println(Connects.size());
			if(Connects.size()>= 6)
			{
				Scroll.Up(driver);
			}
			for(WebElement connect : Connects) {
				connect.click();
				System.out.println("Connect Clicked");
				
					WebElement LAdd = null;
					WebElement LReset = null;
					WebElement FReset = null;
					try {
						LAdd = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\r\n"
								+ "Please don't press back button\"]"));
					}catch(Exception e) {}
					try {
						LReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]"));
					}catch(Exception e) {}
					try {
						FReset = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Device already paired\"]"));
					}catch(Exception e) {}
					
					if(LAdd !=null || LReset!=null || FReset!=null)
					{
						System.out.println("Device Already added");
						driver.navigate().back();
					}
					else
					{
						System.out.println("Device not added");
						
					}
			}
			
			
			
				}
				
				
				
			}			
		}
		catch (Exception exp) {
			System.out.println(exp.getCause());
			System.out.println(exp.getMessage());
			exp.printStackTrace();
		}
	}

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void openAtomberg() {
		DesiredCapabilities cap = new DesiredCapabilities();
		cap.setCapability("platformName", "Android");
		cap.setCapability("appPackage", "com.atomberg.app");
		cap.setCapability("appActivity", "com.atomberg.app.MainActivity");
		cap.setCapability("apksigner",
				"C:\\Users\\Rohit\\Desktop\\android-sdk\\build-tooBls\\34.0.0\\lib\\apksigner.jar");
		try {
			System.out.println("Initializing Appium driver..."); // Check if the Appium driver is initialized
			URL url = new URL("http://127.0.0.1:4723/wd/hub"); // URL of the Appium session
			driver = new AppiumDriver(url, cap);
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			System.out.println("Appium driver initialized.");
		} catch (MalformedURLException e) {
			System.out.println("Error initializing Appium driver: " + e.getMessage());
			Method.captureScreenshot(driver);
			e.printStackTrace();
			return;
		}		System.out.println("Atomberg App Opened...");
		sleep(6000);
		Method.captureScreenshot(driver);

	}
	public static void CDO(AppiumDriver driver)
	{
		WebElement DO = null;
		try {
			DO = driver.findElement(By.xpath("(//android.widget.Button/android.widget.ImageView[1])"));
		}catch(Exception e) {}
		if (DO != null)
		{
			
		}
		else
		{
			System.out.println("Fan Offline");
		}
	}
	public static void AddButton(AppiumDriver driver) {
		WebElement AddButton = null;
		try {
			AddButton = driver.findElement(By.xpath(
					"//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView"));
		} catch (Exception exp) {
		}
		if (AddButton != null) {
			AddButton.click();
		} else {
			Tap.withCoordinates(driver, 540, 1940);
		}
	}

}