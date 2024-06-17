package Login;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.sql.Driver;

public class Guest {
    public static void Mode(AppiumDriver driver)
    {
        WebElement CWOLogin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Continue without login\"]"));
        CWOLogin.click();

        WebElement Yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        Yes.click();

    }

}
