package Login;

import Actions.Tap;
import Permissions.Permission;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class Guest {
    //TODO : Complete this and Try all the guest mode Functionality
    public static void Mode(AppiumDriver driver) {
        WebElement CWOLogin = driver.findElement(By.xpath("//android.view.View[@content-desc=\"Continue without login\"]"));
        CWOLogin.click();

        WebElement Yes = driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Yes\"]"));
        Yes.click();

        WebElement homeScreen = driver.findElement(By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]"));
        assert homeScreen.isDisplayed();
        assumeTrue(homeScreen.isDisplayed(), "not guest mode");
        addButton(driver);
        Permission.Allow(driver);
    }

    private static void addButton(AppiumDriver driver) {
        Tap.withCoordinates(driver, 540, 1850);
    }

}
