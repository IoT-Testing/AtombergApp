package app.resources.Locators.iOS;

import org.openqa.selenium.By;

public class MoreTabLocators {
    // === Locator Constants ===
    public static final By NEW_COMPLAINT_BUTTON = By.xpath("//android.view.View[@content-desc=\"New complaint\"]");
    public static final By INSTALLATION_REQUEST_BUTTON = By.xpath("//android.view.View[@content-desc=\"Installation Request\"]");
    public static final By SERVICE_REQUEST_BUTTON = By.xpath("//android.view.View[@content-desc=\"New service (Water Purifiers)\"]");
    public static final By TRACK_COMPLAINTS_BUTTON = By.xpath("//android.view.View[@content-desc=\"Track complaints/ requests\"]");
    public static final By COMPLAINT_STATUS_HEADER = By.xpath("//android.view.View[@content-desc=\"Complaint/ Request status\"]");
    public static final By NO_COMPLAINTS_INDICATOR = By.xpath("(//android.view.View)[1]"); // Adjust based on actual UI
    public static final By MANUAL_BUTTON = By.xpath("//android.widget.ImageView[@content-desc=\"Manual\"]");
    public static final By CONNECTIVITY_TROUBLESHOOT = By.xpath("//android.widget.ImageView[@content-desc=\"Connectivity Troubleshoot\"]");
    public static final By EMAIL_US_BUTTON = By.xpath("//android.view.View[@content-desc=\"Email us\"]");
    public static final By CALL_US_BUTTON = By.xpath("//android.view.View[@content-desc=\"Call us\"]");
    public static final By HELP_HEADER = By.xpath("//android.view.View[@content-desc=\"Help\"]");
    public static final By VIDEO_TUTORIALS_LINK = By.xpath("//android.view.View[@content-desc=\"Video tutorials\"]");
    public static final By RETURN_TO_HOME_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Return to home\"]");
    public static final By OK_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Ok\"]");
    public static final By CANT_FIND_SERIAL_NUMBER = By.xpath("//android.view.View[@content-desc=\"Can\"t find serial number?\"]");
    public static final By DOWNLOAD_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Download\"]");
    public static final By APP_EMAIL_OPTION = By.xpath("//android.view.View[@content-desc=\"Email (app support)\"]");
    public static final By GENERIC_EMAIL_OPTION = By.xpath("//android.view.View[@content-desc=\"Email (generic support)\"]");
    public static final By CALL_OPTION = By.xpath("//android.view.View[@content-desc=\"Call\"]");
    public static final By CONTACT_SUPPORT_HEADER = By.xpath("//android.view.View[@content-desc=\"Contact Support\"]");
    public static final By THEME_BUTTON = By.xpath("//android.widget.ScrollView/android.widget.ImageView[5]");
    public static final By ELECTRICITY_UNIT_PRICE = By.xpath("//android.view.View[@content-desc=\"Electricity unit price\"]");
    public static final By UNIT_PRICE_INPUT = By.xpath("//android.widget.EditText[@text=\"7.0\"]");
    public static final By CHANGE_CURRENCY = By.xpath("//android.view.View[@content-desc=\"INR\"]");
    public static final By HELP_BUTTON = By.xpath("//android.view.View[@content-desc=\"Help\"]");
    public static final By CHANGE_PASSWORD = By.xpath("//android.view.View[@content-desc=\"Change password\"]");
    public static final By DELETE_ACCOUNT = By.xpath("//android.view.View[@content-desc=\"Delete account\"]");
    public static final By DEVELOPER_OPTIONS = By.xpath("//android.view.View[@content-desc=\"Developer options\"]");
    public static final By LOGOUT_BUTTON = By.xpath("//android.view.View[@content-desc=\"Logout\"]");
    public static final By YES_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Yes\"]");
    public static final By CANCEL_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]");
    public static final By MANAGE_FAMILY = By.xpath("//android.view.View[@content-desc=\"Manage family\"]");
    public static final By FAMILY_EDIT_ICON = By.xpath("(//android.view.View[@clickable=\"true\"])[2]"); // Adjust if needed
    public static final By LEAVE_HOME = By.xpath("//android.view.View[@content-desc=\"Leave home\"]");
    public static final By DELETE_HOME = By.xpath("//android.view.View[@content-desc=\"Delete home\"]");
    public static final By CREATE_HOME_BUTTON = By.xpath("//*[contains(@content-desc, \"Create a new smart home\")]");
    public static final By HOME_NAME_INPUT = By.xpath("//android.widget.EditText");
    public static final By CREATE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Create\"]");
    public static final By APP_TOUR = By.xpath("//android.widget.ImageView[@content-desc=\"App Tour\"]");
    public static final By CONNECT_ALEXA = By.xpath("//android.widget.ImageView[@content-desc=\"Connect Alexa\"]");
    public static final By CONNECT_GOOGLE = By.xpath("//android.widget.ImageView[@content-desc=\"Connect Google\"]");
    public static final By SMART_LOCKS_INSTALLATION = By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Installation\"]");
    public static final By SMART_LOCKS_FEATURES = By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks Features\"]");
    public static final By SMART_LOCKS_APP_SETUP = By.xpath("//android.widget.ImageView[@content-desc=\"Smart Locks App Setup\"]");
    public static final By SELECT_AND_LINK_DEVICE = By.xpath("//android.view.View[@content-desc=\"Select and link device\"]");
    public static final By OPTIONS_HEADER = By.xpath("//android.view.View[@content-desc=\"Options\"]");
    public static final By PROFILE_GREETING = By.xpath("//android.widget.ImageView[contains(@content-desc, \"Hi,\")]");
    public static final By CHANGE_AVATAR_BUTTON = By.xpath("//android.widget.ImageView[@content-desc=\"Change avatar\"]");
    public static final By AVATAR_OPTIONS = By.className("android.widget.ImageView");
    public static final By NAME_INPUT_FIELD = By.xpath("//android.widget.EditText[contains(@text, \"\")]");
    public static final By PHONE_EDIT_SECTION = By.xpath("//android.view.View[contains(@content-desc, \"+91\")]"); // Adjust based on actual content
    public static final By UPDATE_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Update\"]");
    public static final By SELECT_AND_LINK_DEVICE_HEADER = By.xpath("//android.view.View[@content-desc=\"Select and link device\"]");
    public static final By ALEXA_CONNECT_BUTTON = By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnect\"]");
    public static final By ALEXA_CONNECTED_BUTTON = By.xpath("//android.widget.ImageView[@content-desc=\"Amazon Alexa\nConnected\"]");
    public static final By PAIR_ALEXA_TEXT = By.xpath("//android.view.View[@content-desc=\"Pair Alexa\"]");
    public static final By LINK_BUTTON = By.xpath("//android.widget.Button[@content-desc=\"Link\"]");
    public static final By ACCOUNT_LINKING_GUIDE = By.xpath("//android.view.View[@content-desc=\"Account linking guide\"]");
    public static final By WEBVIEW_SIGNIN = By.xpath("//android.webkit.WebView[@text=\"Signin\"]");
    public static final By USERNAME_FIELD = By.id("signInFormUsername");
    public static final By PASSWORD_FIELD = By.id("signInFormPassword");
    public static final By SUBMIT_BUTTON = By.xpath("//android.widget.Button[@text=\"submit\"]");

}
