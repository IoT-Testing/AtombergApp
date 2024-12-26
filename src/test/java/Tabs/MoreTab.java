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
    public static void Options(AppiumDriver atomberg) {
        WebElement moreTab = atomberg.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                "Tab 3 of 3\"]"));
        moreTab.click();
        System.out.println("MoreTab");
        sleep(1000);
        Edit.Profile(atomberg);

		sleep(2000);
		Alexa.Connect(atomberg);
		Sleep(5000);
		GoogleHome.Connect(atomberg);
        Sleep(3000);

        WebElement Theme = atomberg.findElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]"));
        Theme.click();
        System.out.println("Theme");
        Scroll.Up(atomberg);

        // Change Electricity Unit Price
        electricity.UnitPrice(atomberg);
        electricity.Currency(atomberg);
        sleep(1000);
        Tap.withPercentage(atomberg, 0.10, 0.10);

        //Add Live Widget

        // Manage Family
//        Manage.Family(atomberg);

        // Help
        WebElement HelpBt = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
        HelpBt.click();
        Method.captureScreenshot(atomberg);
        System.out.println("Tap on Help");

        Help.RaC(atomberg);
        Help.TC(atomberg);
        Help.Videos(atomberg);
        Help.Manual(atomberg);
        Help.Troubleshoot(atomberg);
        Scroll.Up(atomberg);
        Help.email(atomberg);
        Help.call(atomberg);

        atomberg.navigate().back();
        // Rate Us
        sleep(1000);
        WebElement RateUs = atomberg.findElement(By.xpath("//android.view.View[@content-desc=\"Rate us\"]"));
        RateUs.click();
        Method.captureScreenshot(atomberg);
        System.out.println("Tap on Rate Us");
        atomberg.navigate().back();
        Scroll.Up(atomberg);

        // Privacy Policy
        WebElement PrivacyPolicy = atomberg
                .findElement(By.xpath("//android.view.View[@content-desc=\"Privacy policy\"]"));
        PrivacyPolicy.click();
        Method.captureScreenshot(atomberg);
        System.out.println("Tap on Privacy Policy");
        atomberg.navigate().back();
        sleep(1000);
        Scroll.Up(atomberg);

        AccManage.ChangePassword(atomberg);
        AccManage.DeleteAccount(atomberg);
        AccManage.DeveloperOptions(atomberg);
//        AccManage.Logout(atomberg);

    }
//    private static void LiveWidget(AppiumDriver atomberg)
//    {
//        List<WebElement> Element = atomberg.findElements(By.className(""));
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
