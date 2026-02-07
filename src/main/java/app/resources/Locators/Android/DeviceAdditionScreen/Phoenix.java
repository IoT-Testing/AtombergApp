package app.resources.Locators.Android.DeviceAdditionScreen;

import org.openqa.selenium.By;

public class Phoenix {
    public static final By ADD_BUTTON_XPATH = By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[3]/android.widget.ImageView");
    public static final By CONNECT_BUTTON = By.xpath("//android.view.View[@content-desc=\"Connect\"]");
    public static final By ADD_FIRST_DEVICE_ICON = By.xpath("//android.widget.ImageView[@content-desc=\"Add your first smart device\"]");
    public static final By REMOVE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Remove Device?\"]");
    public static final By ADDING_THE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Adding the device\n" +
            "This process can take 1 minute\n" +
            "Please don't press back button\"]");
    public static final By ADDED_SUCCESSFULLY = By.xpath("//android.view.View[@content-desc=\"Added Successfully \uD83D\uDC4D\"]");
    public static final By DEVICE_REMOVED_SUCCESSFULLY = By.xpath("//android.view.View[@content-desc=\"Device Removed\nSuccessfully \uD83D\uDC4D\"]");
    public static final By YES = By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");
    public static final By FANS_TAB = By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]");
    public static final By OPERATION_FAILED = By.xpath("//android.view.View[@content-desc=\"Operation Failed\n" +
            "\n" +
            "Diagnosis: The device is able to connect to the provided Wi-Fi network and access internet. However, communication with the Atomberg server failed. Please ensure that the internet speed is good, and then retry after some time.\"]");
    //Fan Menu
    public static final By EDIT_DEVICE = By.xpath("//android.view.View[@content-desc=\"Edit device\"]");
    public static final By SHARE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Share device\"]");
    public static final By VIEW_ANALYTICS = By.xpath("//android.view.View[@content-desc=\"View analytics\"]");
    public static final By REGISTER_WARRANTY = By.xpath("//android.view.View[@content-desc=\"Register warranty\"]");
    public static final By DELETE_DEVICE = By.xpath("//android.view.View[@content-desc=\"Edit device\"]");

    //Errors
    public static final By NO_ERROR = By.xpath("");
    public static final By DEVICE_ALREADY_PAIRED = By.xpath("//android.view.View[@content-desc=\"Device already paired\"]");
    public static final By COULD_NOT_REACH_DEVICE = By.xpath("//android.view.View[@content-desc=\"Could not reach\"]");
    public static final By COULD_NOT_CONNECT_PROPERLY = By.xpath("//android.view.View[@content-desc=\"Cannot connect to the device properly\"]");
}
