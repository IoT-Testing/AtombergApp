package Tabs;

import io.appium.java_client.ios.IOSDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.*; //Selenium Dependencies
import AtombergTest.Method;
import Actions.*;
import MoreTab.*;
import java.util.concurrent.TimeUnit;

public class MoreTab {
    public static void Options(IOSDriver driver) {
        WebElement moreTab = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"More\nTab 3 of 3\"]"));
        moreTab.click();
        System.out.println("MoreTab");
        sleep(1000);
//        Edit.Profile(driver);

//		sleep(2000);
//		Alexa.Connect(driver);
//		Sleep(5000);
//		GoogleHome.Connect(driver);
//      Sleep(3000);
        Scroll.Up(driver);
        Sleep(1000);
        WebElement Theme = driver.findElement(By.xpath("//XCUIElementTypeWindow/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeOther[2]/XCUIElementTypeImage[5]"));
        Theme.click();
        System.out.println("Theme");

        // Change Electricity Unit Price
//        EUP.UnitPrice(driver);
//        EUP.Currency(driver);
//        sleep(1000);
//        Tap.withPercentage(driver, 0.10, 0.10);

        //Add Live Widget

        // Manage Family
//        Manage.Family(driver);
        Sleep(2000);
        // Help
        WebElement HelpBt = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Help\"]"));
        HelpBt.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Help");

        Help.RaC(driver);
        Help.TC(driver);
        Help.Videos(driver);
        Help.Manual(driver);
        Help.Troubleshoot(driver);
        WebElement help = null;
        try {
            help = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Help\"]"));
        }catch (Exception e){}
        if (help==null){
            Tap.withCoordinates(driver, 35, 125);
        }
        Scroll.Up(driver);
        Sleep(1000);
        Help.email(driver);
        Help.call(driver);
        Sleep(1000);
        Tap.withCoordinates(driver, 35, 125);
        // Rate Us
        sleep(1000);
        Scroll.Up(driver);
        Sleep(1000);
        WebElement RateUs = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Rate us\"]"));
        Sleep(500);
        RateUs.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Rate Us");
        Tap.withCoordinates(driver,35,125);
//        Scroll.Up(driver);

        // Privacy Policy
        WebElement PrivacyPolicy = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Privacy policy\"]"));
        PrivacyPolicy.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Privacy Policy");
        Tap.withCoordinates(driver,35,90);
        sleep(1000);
        Scroll.Up(driver);

        AccManage.ChangePassword(driver);
        AccManage.DeleteAccount(driver);
        AccManage.DeveloperOptions(driver);
//        AccManage.Logout(driver);

    }
//    private static void LiveWidget(IOSDriver driver)
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
