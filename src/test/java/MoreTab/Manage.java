package MoreTab;

import org.awaitility.Awaitility;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import Actions.Scroll;
import Actions.Tap;
import AtombergTest.Method;
import io.appium.java_client.android.AndroidDriver;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Manage {
    private int memberSize;
    public static void Family(AndroidDriver driver) {
        WebElement ManageFamily = null;
        while (ManageFamily == null) {
            Scroll.Up(driver);
            try {
                ManageFamily = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Manage family\"]"));
            } catch (Exception e) {
            }
        }
        ManageFamily.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Manage Family");

        List<WebElement> elements = driver.findElements(By.className("android.widget.ImageView"));
        List<WebElement> families = elements.stream().filter(element -> element.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        for(WebElement family : families)
        {
            System.out.println(family.getDomAttribute("content-desc"));
            family.click();
            // insert manage home code
            countMember(driver);
            driver.navigate().back();
        }
    }

    public static void Member(AndroidDriver driver) {

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

    public static void AddHome(AndroidDriver driver) {

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
        Tap.withPercentage(driver, 0.50, 0.75); // Narzo 0.50, 0.625 Tab 0.50, 0.75

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

    public static void Home(AndroidDriver driver) {

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

    private static void countMember(AndroidDriver driver){
        List<WebElement> Elements = driver.findElements(By.className("android.view.View"));
        List<WebElement> elements = Elements.stream().filter(element -> element.getDomAttribute("clickable").equals("true")).collect(Collectors.toList());
        System.out.println(elements.size());
        for (WebElement e: elements){
            System.out.println(e.getTagName());
        }
    }

    private static void sleep(long millis) {
        Awaitility.await().atMost(millis, TimeUnit.MILLISECONDS);
    }
}
