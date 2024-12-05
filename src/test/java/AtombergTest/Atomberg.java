package AtombergTest; //To check 


//import Supports.GoogleHome;
import app.Analytics.Analytics;
import app.AppInitializer;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import Login.*;

public class Atomberg {
	public static AppiumDriver driver;

	public static void main(String[] args) {
		AppInitializer appInitializer = new AppInitializer();
		appInitializer.initializeDriver();
		driver= appInitializer.getDriver();
		appInitializer.tapOnAppLogo();
		appInitializer.checkMainScreen();
		Analytics analytics = new Analytics(driver);
		analytics.Show();
	}

	private static void sleep(long millis) {
		Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
	}
	// connecting with the appium server and opening the app
}
