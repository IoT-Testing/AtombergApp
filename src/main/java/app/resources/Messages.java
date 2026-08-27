package app.resources;

import org.openqa.selenium.By;

public class Messages {
    public static final String LOGIN_SUCCESS = "Logged in successfully";
    public static final String LOGOUT_SUCCESS = "Logout Successful";
    public static final String ALEXA_LINKED = "Alexa Linked Successfully";
    public static final String RO_ADDED = "Water Purifier Added Successfully";
    public static final String FAN_OFFLINE = "Fan is offline";
    public static final By PASSAGE_MODE_PROMPT_1 = By.xpath("//android.view.View[contains(@content-desc, 'enabling passage mode')]");
    public static final By PASSAGE_MODE_PROMPT_2 = By.xpath("//android.view.View[@content-desc='Please read this below. Do you still want to continue?']");
    public static final By PASSAGE_MODE_SUCCESS = By.xpath("//android.view.View[@content-desc='Passage Mode Enabled Successfully']");
    public static final By FP_DISABLED_MSG = By.xpath("//android.view.View[@content-desc='All Fingerprints Disabled Successfully!']");
    public static final By FP_ENABLED_MSG = By.xpath("//android.view.View[@content-desc='All Fingerprints Enabled Successfully!']");
    public static final By CARD_NOT_AVAILABLE = By.xpath("//android.view.View[@content-desc='No Cards present for this Lock.']");
    public static final By ALEXA_LINK_SUCCESS = By.xpath("//android.view.View[@content-desc='Alexa Linked Successfully']");
    public static final By UNLINK_SUCCESS = By.xpath("//android.view.View[@content-desc='Unlink Successful']");

}