package app.Login;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.resources.Credentials.DEFAULT_EMAIL;
import static app.resources.Credentials.DEFAULT_PASSWORD;
import static app.resources.Locators.Android.AppLocators.Login.*;
import static app.util.AppUtil.waitForElement;

/**
 * Email – Handles login using email + password credentials.
 *
 * The Atomberg Home App uses a two-step login flow:
 *   1. Tap the Email login button (4th option, ImageView index="5")
 *   2. Enter email in the EditText → tap Continue
 *   3. Enter password in the same EditText → tap Continue
 */
public class Email {

    private final AndroidDriver atomberg;

    public Email(AndroidDriver driver) {
        this.atomberg = driver;
    }

    /**
     * Login with default credentials from {@link app.resources.Credentials}.
     * <p>
     * FIX H7: added null guard — throws a clear IllegalStateException instead of an
     * NPE deep inside performLogin() if setDriver() was never called.
     */
    public void Login() {
        if (atomberg == null)
            throw new IllegalStateException(
                    "Email.Login(): driver is null — call setDriver() or use Email(driver) constructor before login.");
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
                    "Email.email(): driver is null — pass a live AndroidDriver to the Email constructor.");
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

    // ── Internal helpers ──────────────────────────────────────────────────────

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
        System.out.println("Login submitted.");
        return true;
    }

    private void clickEmailLoginButton() {
        WebElement btn = waitForElement(atomberg, EMAIL_LOGIN_BUTTON, 10);
        btn.click();
        System.out.println("Email login option (4th option) selected.");
    }

    private void enterText(String text) {
        WebElement field = waitForElement(atomberg, EDIT_TEXT_FIELD, 10);
        field.click();
        field.clear();
        field.sendKeys(text);
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
            System.out.println("Continue tapped.");
        } catch (Exception e) {
            throw new RuntimeException("Failed to tap Continue button", e);
        }
    }

    /**
     * Post-login: handle OS permissions + optional Alexa popup.
     */
    private void handlePostLoginFlow() {
        PermissionUtil.allow(atomberg);

        List<WebElement> views = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> labeled = views.stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());

        for (WebElement el : labeled) {
            if (Objects.equals(el.getDomAttribute("content-desc"),
                    "Use Alexa to control your smart fan(s) with voice")) {
                try {
                    atomberg.findElement(
                            By.xpath("//android.widget.Button[@content-desc=\"Cancel\"]")).click();
                    System.out.println("Alexa popup dismissed.");
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
