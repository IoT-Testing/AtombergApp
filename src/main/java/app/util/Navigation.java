package app.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class Navigation {
    public static void openFanControl(AndroidDriver driver) {
        System.out.println("🧭 Opening Fan Control Screen...");
        ActionsUtil.Tap.withCoordinates(driver, 700, 975);
        System.out.println("✅ Fan tile clicked");
        ActionsUtil.sleep(5000);
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
