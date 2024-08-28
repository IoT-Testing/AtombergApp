package MoreTab;

import Actions.Scroll;
import java.util.List;
import Actions.Tap;
import AtombergTest.Method;
import org.openqa.selenium.By;
import java.util.stream.Collectors;
import org.openqa.selenium.WebElement;
import io.appium.java_client.ios.IOSDriver;

public class AccManage {
    public static void ChangePassword(IOSDriver driver) {
        WebElement ChangePassword = driver
                .findElement(By.xpath("//XCUIElementTypeOther[@name=\"Change password\"]"));
        ChangePassword.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Change Password");

        Tap.withCoordinates(driver,35,125);
        Tap.withCoordinates(driver,35,125);
    }

    public static void DeleteAccount(IOSDriver driver) {
        WebElement DeleteAccount = driver
                .findElement(By.xpath("//XCUIElementTypeOther[@name=\"Delete account\"]"));
        DeleteAccount.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Delete Account");

        Tap.withCoordinates(driver,35,125);
        Tap.withCoordinates(driver,35,125);
    }

    public static void DeveloperOptions(IOSDriver driver){
        WebElement DevOps = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Developer options\"]"));
        DevOps.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Developer Options");
        Tap.withCoordinates(driver,35,125);
    }

    public static void Logout(IOSDriver driver) {
        checkLogout(driver);

        WebElement Logout = driver.findElement(By.xpath("//XCUIElementTypeOther[@name=\"Logout\"]"));
        Logout.click();
        Method.captureScreenshot(driver);
        System.out.println("Tap on Logout");
        WebElement yes = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Yes\"]"));
        yes.click();
        /*driver.navigate().back();*/
    }

    private static void goToMore(IOSDriver driver) {
        List<WebElement> MT = driver.findElements(By.className("XCUIElementTypeImage"));
        System.out.println(MT.size());
        System.out.println("More");
        List<WebElement> mt = MT.stream().filter(webElement -> webElement.getAttribute("name") != null).collect(Collectors.toList());
        System.out.println(mt.size());
        System.out.println("More1");
        List<WebElement> moreTab = mt.stream().filter(webElement -> webElement.getAttribute("name").startsWith("More")).collect(Collectors.toList());
        System.out.println(moreTab.size());
        for (WebElement e : moreTab) {
            System.out.println(e.getAttribute("name"));
        }
        if (!moreTab.isEmpty()) {
            driver.findElement(By.xpath("//XCUIElementTypeImage[@name=\"More\n" +
                    "Tab 3 of 3\"]")).click();
        }

    }

    private static void checkLogout(IOSDriver driver) {
        AccManage.goToMore(driver);
        List<WebElement> moreTab;
        do {
            List<WebElement> MT = driver.findElements(By.className("XCUIElementTypeOther"));
            System.out.println(MT.size());
            System.out.println("here");
            List<WebElement> mt = MT.stream().filter(webElement -> webElement.getAttribute("name") != null).collect(Collectors.toList());
            System.out.println(mt.size());
            System.out.println("here1");
            moreTab = mt.stream().filter(webElement -> webElement.getAttribute("name").equals("Logout")).collect(Collectors.toList());
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
