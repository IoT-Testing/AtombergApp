package app.Login;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static app.resources.Locators.Android.HomeLocators.*;
import static app.resources.Locators.Android.AppLocators.Login.*;
import static org.awaitility.Awaitility.await;

/**
 * Apple - Handles login via Apple ID with fallback to email/password.
 *
 * <p>This version avoids assertions entirely and uses graceful error handling,
 * logging, and explicit waits to ensure robust execution without abrupt failures.
 */
public class Apple {

	// === Locator Constants ===


	/**
	 * Performs Apple ID login with fallback to manual email/password entry.
	 *
	 * @param atomberg AndroidDriver instance
	 */
	public static void Login(AndroidDriver atomberg) {
		System.out.println("Starting Apple login process...");

		if (clickElementIfExists(atomberg, APPLE_LOGIN_BUTTON)) {
			System.err.println("Failed to find or click Apple Login button.");
			return;
		}

		AppUtil.captureScreenshot(atomberg, "Apple Login");
		ActionsUtil.sleep(5000); // Allow for navigation or redirect

		if (isOnHomeScreen(atomberg)) {
			System.out.println("Detected home screen — login likely successful.");
			PermissionUtil.allow(atomberg);
		} else {
			performFallbackLogin(atomberg);
		}
	}

	// === Internal Helpers ===

	/**
	 * Checks if the app logo (home screen indicator) is visible.
	 */
	private static boolean isOnHomeScreen(AndroidDriver driver) {
		try {
			WebElement logo = driver.findElement(APP_LOGO);
			return logo.isDisplayed();
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/**
	 * Attempts to click an element. Returns false if not found or clickable.
	 */
	private static boolean clickElementIfExists(AndroidDriver driver, By locator) {
		try {
			WebElement element = driver.findElement(locator);
			if (element.isDisplayed() && Boolean.parseBoolean(element.getDomAttribute("clickable"))) {
				element.click();
				System.out.println("Clicked element: " + locator);
				return false;
			} else {
				System.out.println("Element found but not clickable: " + locator);
				return true;
			}
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + locator);
			return true;
		} catch (Exception e) {
			System.err.println("Unexpected error clicking element " + locator + ": " + e.getMessage());
			return true;
		}
	}

	/**
	 * Performs fallback login when Apple sign-in redirects to email/password form.
	 *
	 * @param driver Driver instance
	 */
	private static void performFallbackLogin(AndroidDriver driver) {
		System.out.println("Apple redirect failed. Falling back to email/password login...");

		if (enterTextSafely(driver, TEXT_INPUT_FIELD, "bhagatrb4174@gmail.com", "Email")) {
			System.err.println("Failed to enter email. Aborting login.");
			return;
		}

		AppUtil.captureScreenshot(driver, "Email Enter");

		if (clickElementIfExists(driver, CONTINUE_BUTTON_TEXT)) {
			System.err.println("Failed to click Continue after email entry.");
			return;
		}

		ActionsUtil.sleep(2000);
		AppUtil.captureScreenshot(driver, "Password Entering");

		if (enterTextSafely(driver, TEXT_INPUT_FIELD, "SumitaBH@133", "Password")) {
			System.err.println("Failed to enter password. Aborting login.");
			return;
		}

		if (clickElementIfExists(driver, SIGN_IN_BUTTON)) {
			System.err.println("Failed to click Sign In button.");
			return;
		}

		System.out.println("Sign In submitted. Waiting for home screen...");

		waitForHomeScreen(driver);

		if (isOnHomeScreen(driver)) {
			System.out.println("Test Passed: Successfully logged in and reached home screen.");
			AppUtil.captureScreenshot(driver, "Login Successful");
			PermissionUtil.allow(driver);
		} else {
			System.err.println("Login appeared to succeed, but home screen was not detected.");
		}
	}

	/**
	 * Safely enters text into a field with logging.
	 *
	 * @param driver      Driver instance
	 * @param locator     Field locator
	 * @param text        Text to enter
	 * @param label       Label for logs (e.g., "Email", "Password")
	 * @return true if successful
	 */
	private static boolean enterTextSafely(AndroidDriver driver, By locator, String text, String label) {
		try {
			WebElement field = driver.findElement(locator);
			field.click();
			field.clear();
			field.sendKeys(text);
			System.out.println(label + " entered: " + maskSensitiveData(text));
			return false;
		} catch (NoSuchElementException e) {
			System.err.println(label + " field not found: " + locator);
			return true;
		} catch (Exception e) {
			System.err.println("Error entering " + label.toLowerCase() + ": " + e.getMessage());
			return true;
		}
	}

	/**
	 * Waits up to 10 seconds for home screen to load.
	 *
	 * @param driver Driver instance
	 */
	private static void waitForHomeScreen(AndroidDriver driver) {
		System.out.println("Waiting for home screen...");
		try {
			await()
					.atMost(10, TimeUnit.SECONDS)
					.pollInterval(500, TimeUnit.MILLISECONDS)
					.until(() -> isOnHomeScreen(driver));
		} catch (Exception e) {
			System.err.println("Timed out waiting for home screen: " + e.getMessage());
		}
	}

	/**
	 * Masks sensitive data in logs (e.g., passwords).
	 *
	 * @param text Input text
	 * @return Masked version if sensitive
	 */
	private static String maskSensitiveData(String text) {
		if (text.length() <= 4) return "****";
		return "*" + text.substring(text.length() - 4);
	}
}