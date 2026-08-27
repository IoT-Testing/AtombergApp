package app.resources.Locators.Android.DeviceAdditionScreen;

import org.openqa.selenium.By;

public class SmartLock {
    public static final By COULD_NOT_ADD_LOCK = By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]");
    public static final By WRAPPING_UP_LOCK = By.xpath("//android.view.View[@content-desc=\"Wrapping up...\n"+
            "Please don't press back button\"]");
    public static final By COMPLETE_LOCK_ADDITION = By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\n" +
            "Please don't press back button\"]");
    public static final By CONNECTING_TO_LOCK_MODAL = By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\n" +
            "Please don't press back button\"]");
}
