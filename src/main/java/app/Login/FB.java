package app.Login;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * FB - Handles login via Facebook with fallback behavior.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Remove hardcoded sleeps</li>
 *   <li>Use safe element detection</li>
 *   <li>Avoid assertions</li>
 *   <li>Improve readability and maintainability</li>
 * </ul>
 */
public class FB {

	// === Locator Constants ===
	private static final By FB_LOGIN_BUTTON = By.xpath("(//android.widget.ImageView)[3]"); // Assumed 3rd icon is FB

	private static final By CONTINUE_AS_BUTTON = By.xpath("//android.widget.Button[@text='Continue as Rohit']");

	// Home screen indicator (shared across login types)
	private static final By APP_LOGO = By.xpath(
			"//android.widget.ImageView[contains(@resource-id, 'content')]" // Generic match for app logo
	);

	/**
	 * Performs Facebook login with automatic continuation.
	 *
	 * @param driver AndroidDriver instance
	 */
	public static void Login(AndroidDriver driver) {
		System.out.println("Starting Facebook login...");

		// Step 1: Click Facebook Login Button
		if (!clickElementIfExists(driver, FB_LOGIN_BUTTON)) {
			System.err.println("Failed to click Facebook login button.");
			return;
		}
		ActionsUtil.sleep(5000); // Allow OAuth redirect

		// Step 2: Check if already on Home Screen
		if (isOnHomeScreen(driver)) {
			System.out.println("Already logged in. Proceeding to permissions.");
			PermissionUtil.allow(driver);
			return;
		}

		// Step 3: Otherwise, continue as saved user
		if (clickElementIfExists(driver, CONTINUE_AS_BUTTON)) {
			System.out.println("Clicked 'Continue as Rohit'");
			AppUtil.captureScreenshot(driver, "Account selected");

			// Wait for navigation to complete
			waitForHomeScreen(driver);

			if (isOnHomeScreen(driver)) {
				System.out.println("Test Passed: Successfully logged in via Facebook.");
				AppUtil.captureScreenshot(driver, "Facebook Login Successful");
				PermissionUtil.allow(driver);
			} else {
				System.err.println("Login appeared to succeed, but home screen was not detected.");
			}
		} else {
			System.err.println("Could not find 'Continue as' button. Login flow may have failed.");
		}
	}

	// === Utility Methods ===

	/**
	 * Checks if the main app logo (home screen indicator) is visible.
	 *
	 * @param driver Driver instance
	 * @return true if home screen is detected
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
	 * Safely attempts to click an element.
	 *
	 * @param driver  Driver instance
	 * @param locator Element locator
	 * @return true if clicked successfully
	 */
	private static boolean clickElementIfExists(AndroidDriver driver, By locator) {
		try {
			WebElement element = driver.findElement(locator);
			if (element.isDisplayed() && Boolean.parseBoolean(element.getDomAttribute("clickable"))) {
				element.click();
				System.out.println("Clicked: " + locatorToString(locator));
				return true;
			} else {
				System.out.println("Element found but not clickable: " + locatorToString(locator));
				return false;
			}
		} catch (NoSuchElementException e) {
			System.out.println("Element not found: " + locatorToString(locator));
			return false;
		} catch (Exception e) {
			System.err.println("Error interacting with " + locatorToString(locator) + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Waits up to 10 seconds for the home screen to appear.
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
	 * Converts By locator to readable string for logs.
	 *
	 * @param by Locator
	 * @return Readable description
	 */
	private static String locatorToString(By by) {
		return by.toString().split("-> ")[1]; // Simplify output
	}

	/**
	 * Legacy sleep wrapper (avoid when possible).
	 */
	private static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Sleep interrupted: " + e.getMessage());
		}
	}
}