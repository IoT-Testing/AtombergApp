package Login;

import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Driver;

public class Guest {
    public static void Mode(IOSDriver driver)
    {
        WebElement CWOLogin = driver.findElement(By.xpath("//XCUIElementTypeStaticText[@name=\"Continue without login\"]"));
        CWOLogin.click();

        WebElement Yes = driver.findElement(By.xpath("//XCUIElementTypeButton[@name=\"Yes\"]"));
        Yes.click();

    }

}
