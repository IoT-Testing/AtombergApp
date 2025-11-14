package app.resources.Locators.iOS;

import org.openqa.selenium.By;

public class LockLocators {
    public static final By ADD_BUTTON = By.xpath(
            "//android.widget.ImageView[@content-desc='Add']" // Simplified assumption
    );
    public static final By SMART_LOCK_INDICATOR = By.xpath("//android.view.View[@content-desc=\"Atomberg Smart Lock\"]");
    public static final By CONNECT_BUTTON = By.xpath("//android.view.View[@content-desc=\"Connect\"]");
    public static final By LOCK_TAB = By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]");
    public static final By UNLOCK_HANDLE = By.xpath("//android.view.View[@content-desc=\"Pull down to unlock\"]");
    public static final By SUCCESS_MESSAGE = By.xpath("//android.view.View[contains@content-desc=\"Added Successfully\"]");
    public static final By HISTORY_BUTTON = By.xpath("//android.view.View[@content-desc=\"History\"\"]");
    public static final By SETTINGS_BUTTON = By.xpath("//android.view.View[@content-desc=\"Settings]");
    public static final By ACCESS_KEYS_BUTTON = By.xpath("//android.view.View[@content-desc=\"Access\\nkeys\"]");
    public static final By USERS_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Users\"]");
    public static final By PREFERENCES_SECTION = By.xpath("//android.view.View[@content-desc=\"Preferences\"]");
    public static final By DONE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Done\"]");
    public static final By UPDATE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Update\"]");
    public static final By YES_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");

    // Switch indices in Preferences (assumed order)
    public static final int SILENT_MODE_SWITCH_INDEX = 0;
    public static final int PASSAGE_MODE_SWITCH_INDEX = 1;
    public static final int FINGERPRINT_SWITCH_INDEX = 2;
    public static final int CARD_SWITCH_INDEX = 3;
    public static final int PINS_SWITCH_INDEX = 4;
}
