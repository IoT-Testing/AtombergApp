package MoreTab;

import Actions.Scroll;
import java.util.List;
import AtombergTest.Method;
import org.openqa.selenium.By;

import java.util.Objects;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import io.appium.java_client.AppiumDriver;

public class AccManage {
    //TODO : Add complete change password
    public static void ChangePassword(AppiumDriver driver) {
        WebElement ChangePassword = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Change password\"]"));
        ChangePassword.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Change Password");

        driver.navigate().back();
    }

    //Delete Account
    public static void DeleteAccount(AppiumDriver driver) {
        WebElement DeleteAccount = driver
                .findElement(By.xpath("//android.view.View[@content-desc=\"Delete account\"]"));
        DeleteAccount.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Delete Account");

        driver.navigate().back();
    }

    //Developer options
    public static void DeveloperOptions(AppiumDriver driver) {
        WebElement DevOps = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Developer options\"]"));
        DevOps.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Developer Options");
        driver.navigate().back();
    }

    //Logout button
    public static void Logout(AppiumDriver driver) {
        checkLogout(driver);

        WebElement Logout = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Logout\"]"));
        Logout.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Logout");
        WebElement yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        yes.click();
        /*driver.navigate().back();*/
    }

    //Go to more Tab
    private static void goToMore(AppiumDriver driver) {
        List<WebElement> MT = driver.findElements(By.className("android.widget.ImageView"));
        System.out.println(MT.size());
        System.out.println("More");
        List<WebElement> mt = MT.stream().filter(webElement -> webElement.getAttribute("content-desc") != null).collect(Collectors.toList());
        System.out.println(mt.size());
        System.out.println("More1");
        List<WebElement> moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getAttribute("selected"), "true")).collect(Collectors.toList());
        System.out.println(moreTab.size());
        for (WebElement e : moreTab) {
            System.out.println(e.getAttribute("content-desc"));
        }
        if (moreTab.isEmpty()) {
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();

        } else if (!Objects.requireNonNull(moreTab.get(0).getAttribute("content-desc")).endsWith("Tab 3 of 3")) {
            System.out.println("tap on moreTab");
            driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
                    "Tab 3 of 3\"]")).click();
        }

    }

    //Check the Logout button is available or not
    private static void checkLogout(AppiumDriver driver) {
        AccManage.goToMore(driver);
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = driver.findElements(By.className("android.view.View"));
            System.out.println(MT.size());
            System.out.println("here");
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getAttribute("content-desc") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            System.out.println("here1");
            moreTab = mt.stream().filter(webElement -> Objects.equals(webElement.getAttribute("content-desc"), "Logout")).collect(Collectors.toList());
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
