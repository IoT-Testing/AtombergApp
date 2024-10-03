package Tabs;

import org.awaitility.Awaitility;
import org.openqa.selenium.*; //Selenium Dependencies
import AtombergTest.Method;
import Actions.*;
import MoreTab.*;
import Supports.*;
import io.appium.java_client.AppiumDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MoreTab {
    public static void Options(AppiumDriver driver) {
        WebElement moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                "Tab 3 of 3\"]"));
        moreTab.click();
        System.out.println("MoreTab");
        sleep(1000);
        Edit.Profile(driver);

		sleep(2000);
		Alexa.Connect(driver);
		Sleep(5000);
		GoogleHome.Connect(driver);
        Sleep(3000);

        WebElement Theme = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]"));
        Theme.click();
        System.out.println("Theme");
        Scroll.Up(driver);

        // Change Electricity Unit Price
        electricity.UnitPrice(driver);
        electricity.Currency(driver);
        sleep(1000);
        Tap.withPercentage(driver, 0.10, 0.10);

        //Add Live Widget

        // Manage Family
//        Manage.Family(driver);

        // Help
        WebElement HelpBt = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
        HelpBt.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Help");

        Help.RaC(driver);
        Help.TC(driver);
        Help.Videos(driver);
        Help.Manual(driver);
        Help.Troubleshoot(driver);
        Scroll.Up(driver);
        Help.email(driver);
        Help.call(driver);

        driver.navigate().back();
        // Rate Us
        sleep(1000);
        WebElement RateUs = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Rate us\"]"));
        RateUs.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Rate Us");
        driver.navigate().back();
        Scroll.Up(driver);

        // Privacy Policy
        WebElement PrivacyPolicy = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Privacy policy\"]"));
        PrivacyPolicy.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Privacy Policy");
        driver.navigate().back();
        sleep(1000);
        Scroll.Up(driver);

        AccManage.ChangePassword(driver);
        AccManage.DeleteAccount(driver);
        AccManage.DeveloperOptions(driver);
//        AccManage.Logout(driver);

    }
//    private static void LiveWidget(AppiumDriver driver)
//    {
//        List<WebElement> Element = driver.findElements(By.className(""));
//
//    }

    private static void sleep(long millis) {
        Awaitility.await().atLeast(millis, TimeUnit.MILLISECONDS);
    }

    private static void Sleep(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
