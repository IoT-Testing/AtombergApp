package app.resources.Locators;

import org.openqa.selenium.By;

public class FanLocators {
    // === Locator Constants (Centralized) ===
    public static final By ADD_BUTTON_XPATH = By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView");
    public static final By FAN_DISCOVERY_XPATH = By.xpath("//android.view.View[@content-desc=\"Atomberg Smart Fan\"]");
    public static final By CONNECT_BUTTON = By.xpath("//android.view.View[@content-desc=\"Connect\"]");
    public static final By NEXT_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Next\"]");
    public static final By FANS_TAB = By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]");
    public static final By BUY_NOW_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Buy Now!\"]");
    public static final By ADD_FIRST_DEVICE_ICON = By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]");
    public static final By FAN_MENU_BUTTON = By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[4]");
    public static final By BM_BOND_FAILURE = By.xpath("//android.view.View[@content-desc=\"Failed to create bond. BmBondStateEnum.none\"]");
    public static final By IDENTIFY_YOUR_DEVICE = By.xpath("//android.view.View[@content-desc=\"Identify your device\"]");
    public static final By REMOVE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Remove Device?\"]");
    public static final By YES = By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");
    public static final By DEVICE_REMOVED_SUCCESSFULLY = By.xpath("//android.view.View[@content-desc=\"Device Removed\nSuccessfully \uD83D\uDC4D\"]");
    public static final By TRY_AGAIN = By.xpath("//android.view.View[@content-desc=\"Try Again\"]");
    // Fan Control Buttons
    public static final By SPEED_1 = By.xpath("//android.widget.Button[@content-desc=\"1\"]");
    public static final By SPEED_2 = By.xpath("//android.widget.Button[@content-desc=\"2\"]");
    public static final By SPEED_3 = By.xpath("//android.widget.Button[@content-desc=\"3\"]");
    public static final By SPEED_4 = By.xpath("//android.widget.Button[@content-desc=\"4\"]");
    public static final By SPEED_5 = By.xpath("//android.widget.Button[@content-desc=\"5\"]");
    public static final By BOOST_BUTTON = By.xpath(
            "//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.widget.Button[5]" // Prefer content-desc over deep hierarchy
    );
    public static final By POWER_BUTTON = By.xpath("//android.widget.Button[@index=\"8\"]");

    //Fan Menu
    public static final By EDIT_DEVICE = By.xpath("//android.view.View[@content-desc=\"Edit device\"]");
    public static final By SHARE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Share device\"]");
    public static final By VIEW_ANALYTICS = By.xpath("//android.view.View[@content-desc=\"View analytics\"]");
    public static final By REGISTER_WARRANTY = By.xpath("//android.view.View[@content-desc=\"Register warranty\"]");
//    public static final By FIRMWARE_VERSION = By.xpath("//android.view.View[@content-desc=\"Firmware Version 1.0.0\"]");
    public static final By DELETE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Edit device\"]");


    // Modal Messages
    public static final By CONNECTING_TO_LOCK_MODAL = By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\n" +
            "Please don't press back button\"]");
    public static final By COULD_NOT_ADD_LOCK = By.xpath("//android.view.View[@content-desc=\"Could not add the lock\"]");
    public static final By DEVICE_ALREADY_PAIRED = By.xpath("//android.view.View[@content-desc=\"Device already paired\"]");
    public static final By COULD_NOT_REACH_DEVICE = By.xpath("//android.view.View[@content-desc=\"Could not reach\"]");
    public static final By COULD_NOT_CONNECT_PROPERLY = By.xpath("//android.view.View[@content-desc=\"Cannot connect to the device properly\"]");
    public static final By WRAPPING_UP_LOCK = By.xpath("//android.view.View[@content-desc=\"Wrapping up...\n"+
            "Please don't press back button\"]");
    public static final By COMPLETE_LOCK_ADDITION = By.xpath("//android.view.View[@content-desc=\"Connecting to the Lock...\n" +
        "Please don't press back button\"]");

    // Model Selection
    public static final By RENESA = By.xpath("//android.widget.ImageView[@content-desc=\"Renesa\"]");
    public static final By STUDIO_PLUS = By.xpath("//android.widget.ImageView[@content-desc=\"Studio+\"]");
    public static final By RENESA_PLUS = By.xpath("//android.widget.ImageView[@content-desc=\"Renesa+\"]");
    public static final By CONTINUE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Continue\"]");
    public static final By RENESA_HALO = By.xpath("//android.widget.ImageView[@content-desc=\"Renesa Halo\"]");
    public static final By LOAD_MORE = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By EFFICIO_ALPHA = By.xpath("");
    public static final By AMEZA = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By RENESA_ZEN = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By RENESA_ENZEL = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By EFFICIO = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By IKANO = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By RENESA_PRIME = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By AMEZA_PLUS = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");
    public static final By EFFICIO_PRIME = By.xpath("//android.widget.Button[@content-desc=\"Load More\"]");


    //Color option
    public static final By SELECT_COLOR = By.xpath("//android.view.View[@content-desc=\"Select your device color\"]");
    public static final By SELECT_FAN_MODEL = By.xpath("//android.view.View[@content-desc=\"Pick the fan model you're having\"]");
    public static final By DARK_TEAKWOOD = By.xpath("//android.widget.ImageView[@content-desc=\"Dark Teakwood\"]");
    public static final By REGENT_GREY = By.xpath("//android.widget.ImageView[@content-desc=\"Regent Gray\"]");

}
