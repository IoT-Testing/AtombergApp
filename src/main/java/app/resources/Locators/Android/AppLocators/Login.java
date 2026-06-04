package app.resources.Locators.Android.AppLocators;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

public class Login {
    // Login screen indicator
    public static final By LOGIN_SCREEN_INDICATOR = By.xpath("//android.view.View[@content-desc=\"Experience smart living \n with Atomberg\"]");
    public static final By LOGIN_CONTINUE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Continue\"]");

    //Apple Login Locators
    public static final By APPLE_LOGIN_BUTTON = By.xpath("//android.widget.ImageView[@index=\"2\"]");// First image assumed to be Apple login
    public static final By TEXT_INPUT_FIELD = By.xpath("//android.widget.EditText");
    public static final By CONTINUE_BUTTON_TEXT = By.xpath("//android.widget.Button[@content-desc=\"Continue\"]");
    public static final By SIGN_IN_BUTTON = By.xpath("//android.widget.Button[@text=\"Sign In\"]");

    //Google Login Locators
    public static final By GOOGLE_LOGIN_BUTTON = By.xpath("//android.widget.ImageView[@index=\"3\"]");// Third image assumed to be Google login
//    public static final By GOOGLE_EMAIL_FIELD = By.xpath("//android.widget.EditText[@index=\"0\"]"); // Google email field
//    public static final By GOOGLE_PASSWORD_FIELD = By.xpath("//android.widget.EditText[@index=\"1\"]"); // Google password field
//    public static final By GOOGLE_NEXT_BUTTON = By.xpath("//android.widget.Button[@text=\"Next\"]"); // Google next button

    //Facebook Login Locators
    public static final By FACEBOOK_LOGIN_BUTTON = By.xpath("//android.widget.ImageView[@index=\"4\"]");// Second image assumed to be Facebook login
    public static final By FACEBOOK_EMAIL_FIELD = By.id("m_login_email"); // Facebook email field
    public static final By FACEBOOK_PASSWORD_FIELD = By.id("m_login_password"); // Facebook password field
    public static final By FACEBOOK_LOGIN = By.id("Log in"); // Facebook login submit button

    //Email Login Locators
    public static final By EMAIL_LOGIN_BUTTON = By.xpath("//android.widget.ImageView[@index=\"5\"]");// Simplified from deep hierarchy
    public static final By EDIT_TEXT_FIELD = By.xpath("//android.widget.EditText");//Same one used for password input as well
    public static final By CANCEL_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");
    public static final By INCORRECT_PASSWORD_MESSAGE = By.xpath("//android.view.View[@content-desc=\"! Incorrect password\"]");
    public static final By SHOW_PASSWORD_BUTTON = By.xpath("//android.widget.ScrollView/android.widget.ImageView[2]"); //Show Password Button
    public static final By FORGOT_PASSWORD = By.xpath("//android.view.View[@content-desc=\"Forgot password?\"]");
}
