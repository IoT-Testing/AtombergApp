package app.resources.Locators;

import org.openqa.selenium.By;

public class LoginLocators {
    public static final By LOGIN_SCREEN_INDICATOR = By.xpath("//android.view.View[@content-desc=\"Experience smart living \n with Atomberg\"]");
    public static final By CONTINUE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Continue\"]");
    //Apple Login Locators
    public static final By APPLE_LOGIN_BUTTON = By.xpath("//android.widget.ImageView[@index=\"2\"]");// First image assumed to be Apple login
    public static final By TEXT_INPUT_FIELD = By.xpath("//android.widget.EditText");
    public static final By CONTINUE_BUTTON_TEXT = By.xpath("//android.widget.Button[@content-desc=\"Continue\"]");
    public static final By SIGN_IN_BUTTON = By.xpath("//android.widget.Button[@text=\"Sign In\"]");
    // Home screen indicator
    public static final By APP_LOGO = By.xpath("//android.widget.ImageView[@index=\"0\"]");

    //Email Login Locators
    public static final By EMAIL_LOGIN_BUTTON = By.xpath("//android.widget.ImageView[@index=\"5\"]");// Simplified from deep hierarchy
    public static final By EDIT_TEXT_FIELD = By.xpath("//android.widget.EditText");
    public static final By CANCEL_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");
    public static final By INCORRECT_PASSWORD_MESSAGE = By.xpath("//android.view.View[@content-desc=\"! Incorrect password\"]");
    public static final By SHOW_PASSWORD_BUTTON = By.xpath("//android.widget.ScrollView/android.widget.ImageView[2]"); //Show Password Button
    public static final By FORGOT_PASSWORD = By.xpath("//android.view.View[@content-desc=\"Forgot password?\"]");
}
