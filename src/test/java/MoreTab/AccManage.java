package MoreTab;

import Actions.Scroll;
import java.util.List;
import AtombergTest.Method;
import org.openqa.selenium.By;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import io.appium.java_client.android.AndroidDriver;

public class AccManage {
    public static void ChangePassword(AndroidDriver driver) {
        WebElement ChangePassword = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
        ChangePassword.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Change Password");

        driver.navigate().back();
    }

    public static void DeleteAccount(AndroidDriver driver) {
        WebElement DeleteAccount = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
        DeleteAccount.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Delete Account");

        driver.navigate().back();
    }

    public static void DeveloperOptions(AndroidDriver driver) {
        WebElement DevOps = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
        DevOps.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Developer Options");
        driver.navigate().back();
    }

    public static void Logout(AndroidDriver driver) {
        checkLogout(driver);

        WebElement Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
        Logout.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Logout");
        WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        yes.click();
        /*driver.navigate().back();*/
    }

    private static void goToMore(AndroidDriver driver) {
        List<WebElement> MT = driver.findElements(By.className("android.widget.ImageView"));
        System.out.println(MT.size());
        System.out.println("More");
        List<WebElement> mt = MT.stream().filter(webElement -> webElement.getDomAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(mt.size());
        System.out.println("More1");
        List<WebElement> moreTab = mt.stream().filter(webElement -> webElement.getDomAttribute("selected").equals("true")).collect(Collectors.toList());
        System.out.println(moreTab.size());
        for (WebElement e : moreTab) {
            System.out.println(e.getDomAttribute("content-desc"));
        }
        if (moreTab.isEmpty()) {
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();

        } else if (!moreTab.get(0).getDomAttribute("content-desc").endsWith("Tab 3 of 3")) {
            System.out.println("tap on moreTab");
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();
        }

    }

    private static void checkLogout(AndroidDriver driver) {
        AccManage.goToMore(driver);
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = driver.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            System.out.println("here");
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getDomAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            System.out.println("here1");
            moreTab = mt.stream().filter(webElement -> webElement.getDomAttribute("content-desc").equals("Logout")).collect(Collectors.toList());
            System.out.println(moreTab.size());

            if (moreTab.isEmpty()) {
                Scroll.Up(driver);
            } else if (!moreTab.get(0).isDisplayed()) // Rare case where the Logout button is in the DOM but not on the Screen
            {
                System.out.println(!moreTab.get(0).isDisplayed());
                Scroll.Up(driver);
            }
        } while (moreTab.isEmpty());
    }
}
