package app.Login;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import java.util.List;
import java.util.Objects;
import static app.resources.Credentials.DEFAULT_EMAIL;
import static app.resources.Credentials.DEFAULT_PASSWORD;
import static app.resources.Locators.Android.AppLocators.Login.*;
import static app.util.AppUtil.waitForElement;

/**
 * Email â€“ Handles login using email + password credentials.
 *
 * The Atomberg Home App uses a two-step login flow:
 *   1. Tap the Email login button (4th option, ImageView index="5")
 *   2. Enter email in the EditText â†’ tap Continue
 *   3. Enter password in the same EditText â†’ tap Continue
 */
public class Email {

    private final AndroidDriver atomberg;

    public Email(AndroidDriver driver) {
        this.atomberg = driver;
    }

    /**
     * Login with default credentials from {@link app.resources.Credentials}.
     * <p>
     * FIX H7: added null guard â€” throws a clear IllegalStateException instead of an
     * NPE deep inside performLogin() if setDriver() was never called.
     */
    public void Login() {
        if (atomberg == null)
            throw new IllegalStateException(
                    "Email.Login(): driver is null â€” call setDriver() or use Email(driver) constructor before login.");
        performLogin(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        handlePostLoginFlow();
    }

    /**
     * Login with explicit credentials and validation.
     * <p>
     * FIX H7: added null guard for atomberg, email, and password.
     *
     * @param email    User email
     * @param password User password
     * @throws Exception if email is invalid or password is incorrect
     */
    public void email(String email, String password) throws Exception {
        if (atomberg == null)
            throw new IllegalStateException(
                    "Email.email(): driver is null â€” pass a live AndroidDriver to the Email constructor.");
        if (email == null || email.trim().isEmpty())
            throw new IllegalArgumentException("Email cannot be null or empty");
        if (password == null || password.isEmpty())
            throw new IllegalArgumentException("Password cannot be null or empty");

        boolean continueClickable = performLogin(email, password);
        if (!continueClickable) {
            ActionsUtil.SSleep(1);
            throw new Exception("Invalid Email: Continue button was not clickable after entering email");
        }

        ActionsUtil.SSleep(2);
        if (isElementPresent(INCORRECT_PASSWORD_MESSAGE)) {
            throw new Exception("Invalid Password: Incorrect password error shown");
        }
    }

    // â”€â”€ Internal helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Executes the core two-step login flow.
     * Returns true if the Continue button was clickable after email entry.
     */
    private boolean performLogin(String email, String password) {
        clickEmailLoginButton();
        AppUtil.captureScreenshot(atomberg, "01_email_option_selected");

        enterText(email);
        AppUtil.captureScreenshot(atomberg, "02_email_entered");

        boolean clickable = isContinueClickable();
        if (!clickable) return false;

        clickContinue();
        AppUtil.captureScreenshot(atomberg, "03_after_email_continue");

        enterText(password);
        AppUtil.captureScreenshot(atomberg, "04_password_entered");

        clickContinue();
        return true;
    }

    private void clickEmailLoginButton() {
        WebElement btn = waitForElement(atomberg, EMAIL_LOGIN_BUTTON, 10);
        btn.click();
    }

    /**
     * Enters text into a Flutter text field.
     *
     * <p>Flutter fields resolve as {@code android.widget.EditText} for element
     * lookup but are backed by an {@code android.view.View}, so UiAutomator2
     * rejects {@code element.clear()/sendKeys()} with "Incorrect UI Element Class
     * 'android.view.View'". Instead, we tap to focus the field and type via the
     * device keyboard (W3C Actions), which targets the focused element and skips
     * the element-class check.</p>
     */
    private void enterText(String text) {
        WebElement field = waitForElement(atomberg, EDIT_TEXT_FIELD, 10);
        field.click();                 // focus the Flutter field
        ActionsUtil.sleep(400);
        try {
            field.clear();             // best-effort; no-op/throws on a non-editable View
        } catch (Exception ignore) {
            // Field is a Flutter View â€” nothing to clear via the element API.
        }
        new Actions(atomberg).sendKeys(text).perform();   // type into the focused field
    }

    private boolean isContinueClickable() {
        try {
            WebElement btn = atomberg.findElement(LOGIN_CONTINUE_BUTTON);
            return "true".equals(btn.getDomAttribute("clickable"));
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private void clickContinue() {
        try {
            atomberg.findElement(LOGIN_CONTINUE_BUTTON).click();
            logpoint("Continue tapped.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to tap Continue button", e);
        }
    }

    /**
     * Post-login: handle OS permissions + optional Alexa popup.
     */
    private void handlePostLoginFlow() {
        PermissionUtil.allow(atomberg);

        List<WebElement> views = atomberg.findElements(AppiumBy.className("android.view.View"));
        List<WebElement> labeled = views.stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .toList();

        for (WebElement el : labeled) {
            if (Objects.equals(el.getDomAttribute("content-desc"),
                    "Use Alexa to control your smart fan(s) with voice")) {
                try {
                    atomberg.findElement(
                            By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                    logpoint("Alexa popup dismissed.");
                } catch (Exception e) {
                    System.err.println("Could not dismiss Alexa popup: " + e.getMessage());
                }
                break;
            }
        }
    }

    private boolean isElementPresent(By locator) {
        try {
            return atomberg.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}

