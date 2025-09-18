package app.resources.Locators;

import org.openqa.selenium.By;

public class AnalyticsLocators {
    // === Locator Constants ===
    public static final By NO_DEVICES_MESSAGE = By.xpath("//android.view.View[@content-desc=\"Please add a smart device to view analytics\"]");
    public static final By SCRIM = By.xpath("//android.view.View[@content-desc=\"Scrim\"]");
    public static final By CANCEL_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");
    public static final By CLICKABLE_ICONS = By.xpath("//android.view.View[@clickable=\"true\"]");
    public static final By CONFETTI_IMAGE = By.className("android.widget.ImageView");
}
