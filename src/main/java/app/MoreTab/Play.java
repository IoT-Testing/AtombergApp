package app.MoreTab;

import app.util.ActionsUtil;
import app.util.AppUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;

public class Play {
    public void Videos(AppiumDriver driver) {
        WebElement AppTour = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"App Tour\"]"));
        AppTour.click();

        AppUtil.captureScreenshot(driver);
        System.out.println("App Video Opened");

        VideoTryCatch(driver);
        VideoTryCatch(driver);
        ActionsUtil.sleep(5000);

        WebElement ConnectAlexa = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Alexa\"]"));
        ConnectAlexa.click();

        AppUtil.captureScreenshot(driver);
        System.out.println("Alexa Video Opened");

        VideoTryCatch(driver);
        VideoTryCatch(driver);
        VideoTryCatch(driver);
        ActionsUtil.sleep(5000);

        WebElement ConnectGoogle = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Connect Google\"]"));
        ConnectGoogle.click();
        AppUtil.captureScreenshot(driver);
        System.out.println("Google Home Video Opened");
        VideoTryCatch(driver);
        VideoTryCatch(driver);
        ActionsUtil.sleep(5000);

        WebElement SLAppSetup = null;
        try {
            SLAppSetup = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        } catch (Exception ignored) {
        }
        if (SLAppSetup == null) {
            ActionsUtil.Swipe.Left(driver, 0.80, 0.50);// Tab 0.35 narzo 0.50

            WebElement SLInstall = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
            SLInstall.click();
            ActionsUtil.sleep(2000);
        } else {
            WebElement SLInstall = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]"));
            SLInstall.click();
        }

        AppUtil.captureScreenshot(driver);
        System.out.println("SL installation Video Opened");

        VideoTryCatch(driver);
        VideoTryCatch(driver);
        ActionsUtil.sleep(5000);

        SLAppSetup = null;
        try {
            SLAppSetup = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        } catch (Exception ignored) {
        }
        if (SLAppSetup == null) {
            ActionsUtil.Swipe.Left(driver, 0.80, 0.45);// Tab 0.35 narzo 0.50
            ActionsUtil.sleep(2000);
            SLAppSetup = driver
                    .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]"));
        }
        WebElement SLFeatures = driver
                .findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Features\"]"));
        SLFeatures.click();
        ActionsUtil.sleep(2000);
        AppUtil.captureScreenshot(driver);
        System.out.println("SL Feature Video Opened");

        VideoTryCatch(driver);
        VideoTryCatch(driver);
        ActionsUtil.sleep(5000);

        SLAppSetup.click();
        ActionsUtil.sleep(2000);
        AppUtil.captureScreenshot(driver);
        System.out.println("App Setup for Lock Video Opened");
        VideoTryCatch(driver);
        VideoTryCatch(driver);

    }
    private static void VideoTryCatch(AppiumDriver driver) {
        WebElement VideoTutorials = null;

        try {
            VideoTutorials = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Help\"]"));
        } catch (Exception ignored) {
        }
        if (VideoTutorials == null) {
            System.out.println("Back");
            driver.navigate().back(); // 180, 1550 860, 1960


        }
    }
}
