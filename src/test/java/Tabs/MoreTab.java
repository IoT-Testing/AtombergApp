package Tabs;
// This whole code is for all the elements and sub-elements present in the more tab section
// the change in xpath may occur after the change in flutter libraries(happened once)
// Please note that the code might some time get error if the element xpath value is different
// Keep tabs on the path
//Prerequisites for More Tab section are : Alexa and Google Home should be disconnected as we are test for alexa and google connectivity

import org.awaitility.Awaitility;
import org.openqa.selenium.*; //Selenium Dependencies
import AtombergTest.Method;
import Actions.*; // Actions are create as per the apps convenience
import MoreTab.*; // to avoid creating an object of the class multiple time, it is imported
import Supports.*; // Alexa and Google Home
import io.appium.java_client.AppiumDriver;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class MoreTab {
    public static void Options(AppiumDriver driver) {
        WebElement moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                "Tab 3 of 3\"]"));// More tab has a static value.
        moreTab.click();
        System.out.println("MoreTab");
        sleep(1000); // wait till the tab is open
        Edit.Profile(driver); // Edit profile elements and flow is set properly, one can change the values at send keys.

		sleep(2000);
		Alexa.Connect(driver);  // Alexa connect have two different procedure: 1> to put the values 2> previous values
		Sleep(5000);
		GoogleHome.Connect(driver); // Google Home have two different procedure: 1> to put the values 2> previous values
        Sleep(3000);

        WebElement Theme = driver.findElement(By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]"));
        Theme.click(); // After the theme is changed the  rest of the app will run in that theme, one can add one more tap on the theme to go back to the previous theme
        System.out.println("Theme");
        Scroll.Up(driver);

        // Change Electricity Unit Price
        EUP.UnitPrice(driver);
        EUP.Currency(driver);
        sleep(1000);
        Tap.withPercentage(driver, 0.10, 0.10);

        // Manage Family
        Manage.Family(driver);
        driver.navigate().back();

        //Live Widget
        WebElement LiveWidget = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Enable live widget\"]"));
        LiveWidget.click();

        List<WebElement> popup = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = popup.stream().filter(element -> element.getAttribute("content-desc")!=null).collect(Collectors.toList());
        for (WebElement element: elements)
        {
            if(Objects.equals(element.getAttribute("content-desc"), "Yes")){
                element.click();
                break;
            }
        }
        WebElement successPopup = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Widget enabled!\"]"));
        System.out.println(successPopup.isDisplayed());
//        Assert.assertTrue(successPopup.isDisplayed());

        // Help
        WebElement HelpBt = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Help\"]"));
        HelpBt.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Help");

        Help.RaC(driver);               //Register a complaint
        Help.TC(driver);                //Track complaint
        Help.Videos(driver);            //Check all videos
        Help.Manual(driver);
        Help.Troubleshoot(driver);      // Check all the trouble shoot are working or not
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

    private static void Sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
