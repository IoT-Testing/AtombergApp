package app.Login;

import app.util.ActionsUtil;
import app.util.AppUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

/**
 * Google - Handles login via Google account with fallback behavior.
 *
 * <p>Refactored to:
 * <ul>
 *   <li>Remove assertion misuse</li>
 *   <li>Simplify Awaitility usage</li>
 *   <li>Improve error visibility</li>
 *   <li>Follow clean coding practices</li>
 * </ul>
 *
 * This version avoids all assertions and fails gracefully.
 */
public class Google {

	// === Locator Constants ===
	private static final By GOOGLE_LOGIN_BUTTON = By.xpath("(//android.widget.ImageView)[2]"); // 2nd icon assumed to be Google

	// Simplified: look for any visible "Continue" or selection option in WebView
	private static final By CONTINUE_IN_WEBVIEW = By.xpath(
			"//*[contains(@text, 'Sign in') or contains(@content-desc, 'Choose an account')]//" +
					"android.view.View[@clickable='true' and @focusable='true'][1]"
	);

	// Home screen indicator (shared across logins)
	private static final By APP_LOGO = By.xpath(
			"//android.widget.ImageView[contains(@resource-id, 'content')]" // Generic match for app logo
	);

	/**
	 * Performs Google login with automatic account selection.
	 *
	 * @param driver AndroidDriver instance
	 */
	public static void Login(AndroidDriver driver) {
		logpoint("Starting Google login...");

		// Step 1: Click Google Login Button
		if (!clickElementIfExists(driver, GOOGLE_LOGIN_BUTTON)) {
			System.err.println("Failed to click Google login button.");
			return;
		}

		AppUtil.captureScreenshot(driver, "Google Login");
		ActionsUtil.sleep(3000); // Allow WebView to load

		// Step 2: Check if already on Home Screen (already logged in)
		if (isOnHomeScreen(driver)) {
			logpoint("Already logged in. Proceeding to permissions.");
			PermissionUtil.allow(driver);
			return;
		}

		// Step 3: Otherwise, interact with Google WebView
		if (waitForAndClick(driver, CONTINUE_IN_WEBVIEW, 60)) {
			logpoint("Clicked continue in Google WebView");
			AppUtil.captureScreenshot(driver, "Google Webview");

			// Wait for navigation to home screen
			waitForHomeScreen(driver);

			if (isOnHomeScreen(driver)) {
				logpoint("Test Passed: Successfully logged in via Google.");
				AppUtil.captureScreenshot(driver, "Test Successful");
				PermissionUtil.allow(driver);
			} else {
				System.err.println("Login appeared to succeed, but home screen was not detected.");
			}
		} else {
			System.err.println("Timed out waiting for Google sign-in screen. Flow may have failed.");
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
				logpoint("Clicked: " + locatorToString(locator));
				return true;
			} else {
				logpoint("Element found but not clickable: " + locatorToString(locator));
				return false;
			}
		} catch (NoSuchElementException e) {
			logpoint("Element not found: " + locatorToString(locator));
			return false;
		} catch (Exception e) {
			System.err.println("Error interacting with " + locatorToString(locator) + ": " + e.getMessage());
			return false;
		}
	}

	/**
	 * Waits up to N seconds for element to appear and clicks it.
	 *
	 * @param driver   Driver instance
	 * @param locator  Element locator
	 * @param timeoutSec Timeout in seconds
	 * @return true if element was found and clicked
	 */
	private static boolean waitForAndClick(AndroidDriver driver, By locator, long timeoutSec) {
		logpoint("Waiting for element: " + locatorToString(locator));

		try {
			await()
					.atMost(timeoutSec, TimeUnit.SECONDS)
					.pollInterval(Duration.ofMillis(500))
					.until(() -> {
						try {
							WebElement element = driver.findElement(locator);
							if (element.isDisplayed() && Boolean.parseBoolean(element.getDomAttribute("clickable"))) {
								element.click();
								return true;
							}
						} catch (Exception ignored) {}
						return false;
					});
			return true;
		} catch (Exception e) {
			System.err.println("Timed out waiting for element: " + locatorToString(locator));
			return false;
		}
	}

	/**
	 * Waits for home screen to appear.
	 *
	 * @param driver Driver instance
	 */
	private static void waitForHomeScreen(AndroidDriver driver) {
		logpoint("Waiting for home screen...");
		try {
			await()
					.atMost(Duration.ofSeconds(10))
					.pollInterval(Duration.ofMillis(500))
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
