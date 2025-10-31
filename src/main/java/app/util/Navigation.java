package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static app.util.AppUtil.isElementPresent;

public class Navigation {
    private static final By bluetoothBtn = By.xpath("//android.view.View[@index=\"3\"]");
    private static final By bluetoothSuccess = By.xpath("//android.view.View[@content-desc=\"Device is connected via bluetooth\"]");


    public static void openFanControl(AndroidDriver driver) {
        ActionsUtil.Tap.withCoordinates(driver, 500, 590);// for Narzo only

        ActionsUtil.sleep(5000);
        executeClickBluetooth(driver);
    }
    private static boolean clickElementWithRetry(AndroidDriver driver, By locator) {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement el = driver.findElement(locator);
                if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                    el.click();
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    private static boolean executeClickBluetooth(AndroidDriver driver) {
        String actionId = "click on Bluetooth";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            if (clickElementWithRetry(driver,bluetoothBtn)) {
                if (isElementPresent(driver,bluetoothSuccess)) {
                    status = "Success";
                    success = true;
                    driver.navigate().back();
                }
            }

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
        }

        return success;
    }


    private static boolean waitForVisible(AndroidDriver driver, By locator, long seconds) {
        for (int i = 0; i < seconds * 2; i++) {
            try {
                if (driver.findElement(locator).isDisplayed()) return true;
            } catch (Exception ignored) {}
            ActionsUtil.sleep(500);
        }
        return false;
    }
}
