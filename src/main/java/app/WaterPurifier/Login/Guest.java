package app.WaterPurifier.Login;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Driver;

public class Guest {
    public static void Mode(AndroidDriver driver)
    {
        WebElement CWOLogin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Continue without login\"]"));
        CWOLogin.click();

        WebElement Yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        Yes.click();

    }

}
