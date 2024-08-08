package AtombergTest; //To check 

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.awaitility.Awaitility;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

        /*
          @author Rohit B. Bhagat
         */

public class App {
    public static IOSDriver driver;

    public static void main(String[] args) {
        try {
            openAtomberg();
        } catch (Exception e) {
        }
    }

    static void openAtomberg() {
        XCUITestOptions options = new XCUITestOptions();
        options.setAutomationName("XCUITest");
        options.setCapability("platformName", "iOS");
        options.setCapability("platFormVersion", "17.5.1");
        options.setCapability("udid", "00008030-000648993A11402E");
        options.setCapability("bundleId", "com.atomberg.app");
//        options.setCapability("deviceName", "iPhone 11");
        options.setCapability("allow-cors", "true");
        options.setCapability("usePrebuiltWDA", true);
//        cap.setCapability("apksigner", "\"C:\\Users\\Rohit Bhagat\\Desktop\\android-sdk\\build-tools\\34.0.0\\lib\\apksigner.jar\"");
//        cap.setCapability("idleTimeout", 20);
        try {
            System.out.println("Initializing IOS driver...");
            // Check if the Appium driver is initialized
            URL url = null;
            try {
                url = new URL("http://127.0.0.1:4723/");
            }catch (Exception e){
                e.printStackTrace();
            }
            assert url !=null;
            driver = new IOSDriver(url, options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//            Screen.recordStart();  //Screen Recording only works in those mobiles which have screenrecording tool in the device OS
            System.out.println("iOS driver initialized.");
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("Atomberg App Opened...");
        sleep(6000);
        Method.captureScreenshot(driver);

    }

    static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

}


