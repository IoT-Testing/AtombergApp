package app.resources.Locators.Android;

import org.openqa.selenium.By;

public class HomeLocators {

    public static final By MORE_TAB =  By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
            "Tab 3 of 3\"]");
    public static final By ANALYTICS_TAB =  By.xpath("//android.widget.Button[@content-desc=\"Analytics\n" +
            "Tab 1 of 3\"]");
    public static final By ALEXA_POPUP = By.xpath("//android.view.View[@content-desc=\"Use Alexa to control your smart fan(s) with voice\"]");
    public static final By APP_LOGO = By.xpath("//android.widget.FrameLayout[@resource-id=\"android:id/content\"]/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[1]/android.widget.ImageView");
    public static final By AUTOMATION_TAB = By.xpath("//android.view.View[@content-desc=\"Automations\"]");
    public static final By ROOMS = By.xpath("//android.view.View[@content-desc=\"Rooms\"]");
    public static final By LOCKS = By.xpath("//android.widget.ImageView[@content-desc=\"Locks\"]");
    public static final By WATER_PURIFIERS = By.xpath("//android.widget.ImageView[@content-desc=\"Water Purifier\"]");
    public static final By MIC_ICON = By.xpath("//android.widget.Button[@index=\"5\"]");
    public static final By DEVICES = By.xpath("//android.view.View[@content-desc=\"Devices\"]");
}
