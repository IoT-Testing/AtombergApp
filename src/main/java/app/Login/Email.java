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
import static app.resources.Credentials.*;
import static app.resources.Locators.LoginLocators.*;
import static app.util.AppUtil.*;

/**
 * Email - Handles login using email/password credentials.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Eliminate duplication</li>
 *   <li>Externalize constants</li>
 *   <li>Improve error handling</li>
 *   <li>Use safe interaction patterns</li>
 *   <li>Follow single responsibility principle</li>
 * </ul>
 */
public class Email {
    private final AndroidDriver atomberg;

    public Email(AndroidDriver driver) {
        this.atomberg = driver;
    }

    /**
     * Main login method with default credentials.
     */
    public void Login() {
        performLogin(DEFAULT_EMAIL, DEFAULT_PASSWORD);
        handlePostLoginFlow();
    }

    /**
     * Legacy method with alternate hardcoded credentials.
     */
//    public void email() {
//        performLogin(ALT_EMAIL, ALT_PASSWORD);
//        handlePostLoginFlow();
//    }

    /**
     * Parameterized login with validation.
     *
     * @param email    User email
     * @param password User password
     * @throws Exception if invalid email or incorrect password detected
     */
    public void email(String email, String password) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        boolean canClickContinue = performLogin(email, password);

        if (!canClickContinue) {
            ActionsUtil.SSleep(1); // Allow UI update
            throw new Exception("Invalid Email: Continue button not clickable");
        }

        ActionsUtil.SSleep(2); // Wait for possible error

        if (isElementPresent(INCORRECT_PASSWORD_MESSAGE)) {
            throw new Exception("Invalid Password: Login failed due to incorrect password");
        }

        // Navigate back only if explicitly called from test (assumption)
        atomberg.navigate().back();
    }

    // === Internal Helpers ===

    /**
     * Performs shared login steps: enter email → continue → enter password → continue.
     *
     * @param email    Email address
     * @param password Password
     * @return true if 'Continue' button was clickable after email entry
     */
    private boolean performLogin(String email, String password) {
        clickEmailLoginButton();
        AppUtil.captureScreenshot(atomberg);

        enterEmail(email);
        AppUtil.captureScreenshot(atomberg);

        boolean isClickable = isContinueButtonClickable();
        if (!isClickable) return false;

        clickContinue();
        AppUtil.captureScreenshot(atomberg);

        enterPassword(password);
        AppUtil.captureScreenshot(atomberg);

        clickContinue();
        System.out.println("Login submitted.");
        return true;
    }

    /**
     * Clicks the initial 'Email Login' button.
     */
    private void clickEmailLoginButton() {
        WebElement button = waitForElement(atomberg, EMAIL_LOGIN_BUTTON, 10);
        button.click();
        System.out.println("Email login option selected.");
    }

    /**
     * Enters email into the input field.
     *
     * @param email Email to enter
     */
    private void enterEmail(String email) {
        WebElement field = waitForElement(atomberg, EDIT_TEXT_FIELD, 10);
        field.click();
        field.clear(); // Ensure no pre-filled text
        field.sendKeys(email);
        System.out.println("Email Entered: " + email);
    }

    /**
     * Enters password into the input field.
     *
     * @param password Password to enter
     */
    private void enterPassword(String password) {
        WebElement field = waitForElement(atomberg, EDIT_TEXT_FIELD, 10);
        field.click();
        field.clear();
        field.sendKeys(password);
        System.out.println("Password entered.");
    }

    /**
     * Checks if Continue button is enabled/clickable.
     *
     * @return true if clickable
     */
    private boolean isContinueButtonClickable() {
        try {
            WebElement continueBtn = atomberg.findElement(CONTINUE_BUTTON);
            String clickable = continueBtn.getDomAttribute("clickable");
            return "true".equals(clickable);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Clicks Continue button.
     */
    private void clickContinue() {
        clickElement(CONTINUE_BUTTON, "Continue Button");
    }

    /**
     * Handles post-login flows: permissions, Alexa popup.
     */
    private void handlePostLoginFlow() {
        PermissionUtil.allow(atomberg);

        List<WebElement> views = atomberg.findElements(By.className("android.view.View"));
        List<WebElement> labeledElements = views.stream()
                .filter(el -> el.getDomAttribute("content-desc") != null)
                .collect(Collectors.toList());

        for (WebElement el : labeledElements) {
            if (Objects.equals(el.getDomAttribute("content-desc"), "Use Alexa to control your smart fan(s) with voice")) {
                try {
                    atomberg.findElement(CANCEL_BUTTON).click();
                    System.out.println("Alexa popup dismissed.");
                    break;
                } catch (Exception e) {
                    System.err.println("Failed to dismiss Alexa popup: " + e.getMessage());
                }
            }
        }
    }

    // === Utility Methods ===

    /**
     * Safely checks if element is present.
     *
     * @param locator Locator to check
     * @return true if present
     */
    private boolean isElementPresent(By locator) {
        try {
            return atomberg.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Clicks element with logging.
     *
     * @param locator By strategy
     * @param label   Action label
     */
    private void clickElement(By locator, String label) {
        try {
            atomberg.findElement(locator).click();
            System.out.println(label + " clicked.");
        } catch (Exception e) {
            System.err.println("Failed to click " + label + ": " + e.getMessage());
            throw new RuntimeException("Interaction failed: " + label, e);
        }
    }

    // --- Removed Custom Sleep Wrapper ---
    // Note: The original used Awaitility just to sleep — unnecessary complexity.
    // We now use ActionsUtil.sleep() or explicit waits instead.
}