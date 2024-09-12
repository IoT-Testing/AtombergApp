package Tabs;
//Add First Device

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import Actions.Swipe;
import io.appium.java_client.AppiumDriver;

public class Analytics {
    private static WebElement analytics;
    private static WebElement moreTab;

    public static void Show(AppiumDriver driver) {
        analytics = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Analytics\n" +
                "Tab 1 of 3\"]"));
        moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                "Tab 3 of 3\"]"));
        analytics.click();

//        Assertions.assertTrue();
        System.out.println("switched to Analytics");
        WebElement FanCheck = null;
        try {
            FanCheck = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Please add a smart fan to view analytics\"]"));
        } catch (Exception Exp) {
        }
        if (FanCheck == null) {
            nextFan(driver);
        }
    }

    private static void fanChange(AppiumDriver driver) {
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> Devices = Elements.stream().filter(ele -> ele.getAttribute("content-desc") != null).collect(Collectors.toList());
        List<WebElement> fans = Devices.stream().filter(ele -> Objects.requireNonNull(ele.getAttribute("content-desc")).endsWith("Fan")).collect(Collectors.toList());
        System.out.println("Number of Fans available = " +fans.size());
            for (WebElement fan : fans) {
                fan.click();
            }
    }
    
    private static void nextFan(AppiumDriver driver) {
        fanChange(driver);
        List<WebElement> availFans = driver.findElements(By.className("android.view.View"));
        List<WebElement> fans = availFans.stream().filter(ele -> ele.getAttribute("content-desc") != null).collect(Collectors.toList());
        fans.remove(fans.size() - 1);
        System.out.println(fans.size());
        if (fans.size() == 1) {
            for(WebElement fan : fans){
                fan.click();
                info(driver);}
        } else if(fans.size()>1) {
            for (int i = 0; i < fans.size(); i++) {

                List<WebElement> availFans1 = driver.findElements(By.className("android.view.View"));
                List<WebElement> anaFans = availFans1.stream().filter(ele -> ele.getAttribute("content-desc") != null).collect(Collectors.toList());
                anaFans.remove(anaFans.size() - 1);
                System.out.println(anaFans.get(i).getAttribute("content-desc"));
                anaFans.get(i).click();
                System.out.println(i);
                info(driver);
                System.out.println(i < (anaFans.size() - 1));
                if (i < (anaFans.size() - 1)) { // to go to the analytics screen and
                    moreTab.click();
                    rateUs(driver);
                    sleep(1500);
                    analytics.click();
                    rateUs(driver);
                }
                fanChange(driver);
            }
        }
    }

    private static void rateUs(AppiumDriver driver) {
        List<WebElement> dialogueBox = driver.findElements(By.className("android.widget.Button"));
        List<WebElement> cancel = dialogueBox.stream().filter(webElement -> Objects.equals(webElement.getAttribute("content-desc"), "Cancel")).collect(Collectors.toList());
        for (WebElement ele : cancel) {
            if (Objects.equals(ele.getAttribute("content-desc"), "Cancel")) {
                ele.click();
                System.out.println("Canceled Rate us");
                analytics = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Analytics\n" +
                        "Tab 1 of 3\"]"));
                moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                        "Tab 3 of 3\"]"));
            }
        }
    }

    private static void info(AppiumDriver driver) {
        int i;
        for (i = 0; i < 4; i++) {// there are 4 screens in analytics
            List<WebElement> ICONS = driver.findElements(By.xpath("//android.view.View[@clickable=\"true\"]"));
            System.out.println(ICONS.size());
            List<WebElement> icons = ICONS.stream().filter(element -> element.getAttribute("content-desc") == null).collect(Collectors.toList());
            System.out.println(icons.size());
            for (WebElement icon : icons) {
                System.out.println(icon.getAttribute("content-desc"));
                icon.click();
                sleep(2000);
                driver.navigate().back();
                sleep(1500);
            }
            if (i < 3) {// only three swipes for the screen
                Swipe.Left(driver, 0.75, 0.50);
                sleep(1500);
            }
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

}
