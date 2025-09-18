package app.resources.Locators;

import org.openqa.selenium.By;

public class HomeLocators {
    public static By MORE_TAB =  By.xpath("//android.widget.ImageView[@content-desc=\"More\n" +
            "Tab 3 of 3\"]");
    public static By ANALYTICS_TAB =  By.xpath("//android.widget.Button[@content-desc=\"Analytics\n" +
            "Tab 1 of 3\"]");
    public static By ALEXA_POPUP = By.xpath("//android.view.View[@content-desc=\"Use Alexa to control your smart fan(s) with voice\"]");
}
