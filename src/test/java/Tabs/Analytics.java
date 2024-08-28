package Tabs;
//Add First Device

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.awaitility.Awaitility;
import org.openqa.selenium.By; //Selenium Dependencies
import org.openqa.selenium.WebElement;
import Actions.Swipe;
import io.appium.java_client.ios.IOSDriver;

public class Analytics {
    private static WebElement analytics;
    private static WebElement moreTab;
    public static int i;

    public static void Show(IOSDriver driver) {
        //analytics have 2 different value 1: not selected  **/XCUIElementTypeStaticText[`name == "Analytics\nTab 1 of 3"`]
        // , 2: selected //XCUIElementTypeOther[@name="Analytics\nTab 1 of 3"]
        moreTab = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"More\n" +
                "Tab 3 of 3\"]"));
        try{
            analytics = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Analytics\n" +
                    "Tab 1 of 3\"]"));
        }catch(Exception e){}
        try{
            analytics = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Analytics\n" +
                    "Tab 1 of 3\"]"));
        }catch (Exception e){}
        analytics.click();

//        Assertions.assertTrue();
        System.out.println("switched to Analytics");
        WebElement FanCheck = null;
        try {
            FanCheck = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Please add a smart device to view analytics\"]"));
        } catch (Exception Exp) {
        }
        if(FanCheck != null)
        {
            switchFamily(driver);
        }
        if (FanCheck == null) {
            nextFan(driver);
        }
    }

    private static void fanChange(IOSDriver driver) {
        List<WebElement> FANS = driver.findElements(By.className("XCUIElementTypeStaticText"));
        List<WebElement> Fans = FANS.stream().filter(ele -> ele.getAttribute("name") != null).collect(Collectors.toList());
        List<WebElement> fans = Fans.stream().filter(ele -> ele.getAttribute("name").endsWith("Fan")).collect(Collectors.toList());

        for (WebElement fan : fans) {
            fan.click();
        }
    }

    private static void nextFan(IOSDriver driver) {
        fanChange(driver);
        List<WebElement> availFans = driver.findElements(By.className("XCUIElementTypeStaticText"));
        List<WebElement> fans = availFans.stream().filter(ele -> ele.getAttribute("name") != null).collect(Collectors.toList());
        fans.remove(fans.size() - 1);
        System.out.println(fans.size());

        for (int i = 0; i < fans.size(); i++) {

            List<WebElement> availFans1 = driver.findElements(By.className("XCUIElementTypeStaticText"));
            List<WebElement> anaFans = availFans1.stream().filter(ele -> ele.getAttribute("name") != null).collect(Collectors.toList());
            anaFans.remove(anaFans.size() - 1);
            System.out.println(anaFans.get(i).getAttribute("name"));
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

    private static void rateUs(IOSDriver driver) {
        List<WebElement> dialogueBox = driver.findElements(By.className("XCUIElementTypeButton"));
        List<WebElement> cancel = dialogueBox.stream().filter(webElement -> webElement.getAttribute("name").equals("Cancel")).collect(Collectors.toList());
        for (WebElement ele : cancel) {
            if (ele.getAttribute("name").equals("Cancel")) {
                ele.click();
                System.out.println("Canceled Rate us");
                analytics = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Analytics\n" +
                        "Tab 1 of 3\"]"));
                moreTab = driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"More\n" +
                        "Tab 3 of 3\"]"));
            }
        }
    }

    private static void info(IOSDriver driver) {
        int i;
        for (i = 0; i < 4; i++) {// there are 4 screens in analytics
            List<WebElement> ICONS = driver.findElements(By.xpath("//XCUIElementTypeStaticText[@clickable=\"true\"]"));
            List<WebElement> icons = ICONS.stream().filter(element -> element.getAttribute("name") == null).collect(Collectors.toList());
            icons.remove(icons.size() - 1);
            for (WebElement icon : icons) {
                System.out.println(icon.getAttribute("name"));
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
    public static void switchFamily(IOSDriver driver) {
        List<WebElement> elements = driver.findElements(By.className("XCUIElementTypeStaticText"));
        WebElement e = elements.get(0);
        String fam1 = e.getAttribute("name");
        System.out.println(e.getAttribute("name"));
        e.click();
        // tap on the Family name on the screen (top right corner)

        // getting the availble family list
        List<WebElement> rawFamilies = driver.findElements(By.className("XCUIElementTypeStaticText"));
        List<WebElement> FAMILIES = rawFamilies.stream().filter(fam -> fam.getAttribute("name") != null).collect(Collectors.toList());
        FAMILIES.remove(0);
        FAMILIES.remove(FAMILIES.size() - 1);
        int total = FAMILIES.size();
        System.out.println("number of FAMILIES present =" + total);
        for (int j = 0; j < total; j++) {
            List<WebElement> rawFamily = driver.findElements(By.className("XCUIElementTypeStaticText"));
            List<WebElement> families = rawFamily.stream().filter(fam -> fam.getAttribute("name") != null).collect(Collectors.toList());
            System.out.println("number of families present =" + total);
            families.remove(0);
            families.remove(families.size() - 1);
            System.out.println(families.get(i).getAttribute("name") + " is clicked");
            families.get(i).click();
            nextFan(driver);
        }

    }


    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }

}
