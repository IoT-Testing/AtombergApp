package MoreTab;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Scroll;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.AppiumDriver;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Manage {
    public static void Family(AppiumDriver driver) {
        WebElement MT = null;
        try {
            MT = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Select and link device\"]"));
        } catch (Exception e) {
        }
        if (MT == null) {
            WebElement moreTab = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]"));// More tab has a static value.
            moreTab.click();
        }
        WebElement ManageFamily = null;
        while (ManageFamily == null) {
            Scroll.Up(driver);
            try {  // try catch will check for the manage family element on the DOM.
                ManageFamily = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
            } catch (Exception e) {
            }
        }// this loop will execute until the element is available in the DOM
        ManageFamily.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Manage Family");

        List<WebElement> elements = driver.findElements(By.className("android.widget.ImageView")); // this will get the list of all the elements in the DOM with className
        List<WebElement> families = elements.stream().filter(element -> element.getAttribute("content-desc") != null).collect(Collectors.toList());
        //here it will remove all the null  elements and keep only the required one
        for(WebElement family : families)
        {
            System.out.println(family.getAttribute("content-desc")); // print the content-desc i.e. the text of the element
            family.click(); // Clicking on the element
            // insert manage home code
            countMember(driver);
            driver.navigate().back();
        }
        driver.navigate().back();
    }

    public static void Member(AppiumDriver driver) {

        WebElement RemoveMember = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Remove Member\"]"));
        RemoveMember.click();
        System.out.println("Remove Member");
        Method.captureScreenshot(driver);

        WebElement Cancel2 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel2.click();
        WebElement MakeAdmin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Make Admin\"]"));
        MakeAdmin.click();

        System.out.println("Make Admin");
        Method.captureScreenshot(driver);

        WebElement Cancel3 = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel3.click();
        driver.navigate().back();

        WebElement AddMember = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]"));
        AddMember.click();

        Method.captureScreenshot(driver);
        sleep(3000);

        WebElement Share = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Share\"]"));
        Share.click();
        Method.captureScreenshot(driver);

        driver.navigate().back();
        driver.navigate().back();
        driver.navigate().back();
    }

    public static void AddHome(AppiumDriver driver) {

        WebElement AddHome = null;

        try { //Checking if the '+' Add button is in the screen or not
           AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
        } catch (Exception exp) {
        }
        if (AddHome == null) {
            Scroll.Up(driver);

            AddHome = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add\"]"));
        }

        AddHome.click();

        Method.captureScreenshot(driver);
        Tap.withPercentage(driver, 0.50, 0.75); //
        Method.captureScreenshot(driver);
        WebElement HomeName = driver.findElement(By.xpath("//android.widget.EditText"));
        HomeName.click();
        HomeName.sendKeys("Script");
        Method.captureScreenshot(driver);


        WebElement Create = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Create\"]"));
        Create.click();

        Method.captureScreenshot(driver);
        sleep(5000);

    }

    public static void Home(AppiumDriver driver) {

        WebElement FamilyEdit = driver.findElement(By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View[2]"));
        FamilyEdit.click();
        System.out.println("Family Edit");
        Method.captureScreenshot(driver);

        WebElement LeaveHome = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Leave home\"]"));
        LeaveHome.click();
        System.out.println("Leave home");
        Method.captureScreenshot(driver);

        WebElement Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel.click();
        System.out.println("Cancel");
        Method.captureScreenshot(driver);

        WebElement delete =driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete home\"]"));
        delete.click();

        Cancel = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]"));
        Cancel.click(); // android.widget.Button[@content-desc="Cancel"]
        driver.navigate().back();
    }

    private static void countMember(AppiumDriver driver){
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> Objects.equals(element.getAttribute("clickable"), "true")).collect(Collectors.toList());
        System.out.println(elements.size());
        for (WebElement e: elements){
            System.out.println(e.getTagName());
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}
