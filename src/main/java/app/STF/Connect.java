package app.STF;

import app.util.ActionsUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.time.Duration;

import static app.STF.RethinkDBLauncher.ip;

/**
 * Connect2 - Thread-safe utility to automate STF device access and ADB command retrieval.
 *
 * <p>Designed for parallel execution using {@link ThreadLocal}.
 * Uses JavaScript-based copy for reliability.
 */
public class Connect {
    private static final ThreadLocal<ChromeDriver> driver = new ThreadLocal<>();
    private static final ThreadLocal<String> clipboard = new ThreadLocal<>();

    // === Configuration Constants ===
    private static final String STF_URL = "http://192.168.56.1:4723/";//192.168.56.1
    private static final String USERNAME = "iot testing";
    private static final String EMAIL = "iot.testing@atomberg.com";

    // Locators
    private static final By USERNAME_FIELD = By.name("username");
    private static final By EMAIL_FIELD = By.name("email");
    private static final By LOGIN_BUTTON = By.xpath("//input[@value='Log In']");
    private static final By DEVICE_ADB_TEXTAREA = By.tagName("textarea");

    /**
     * Opens STF web portal, logs in, selects device, and copies ADB command.
     */
    public void ipAddress() throws IOException, UnsupportedFlavorException {
        try {
            setupDriver();
            navigateToStf();
            stfLogin();
            remoteDebug();
        } catch (Exception e) {
            System.err.println("Failed to complete STF connection flow: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Gets the copied ADB command for current thread.
     *
     * @return ADB connect string (e.g., "adb connect 192.168.56.101:7400")
     */
    public String getCopiedText() {
        return clipboard.get();
    }

    // === Internal Helpers ===

    /**
     * Initializes ChromeDriver with safe options.
     */
    private void setupDriver() {
        if (driver.get() != null) {
            System.out.println("Driver already exists for thread: " + Thread.currentThread().getId());
            return;
        }

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu");
        options.addArguments("--window-size=1920,1080"); // Avoid maximize issues
        options.addArguments("--remote-allow-origins=*");

        ChromeDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.set(chromeDriver);

        System.out.println("ChromeDriver initialized for thread: " + Thread.currentThread().getId());
    }

    /**
     * Navigates to STF URL.
     */
    private void navigateToStf() {
        ChromeDriver localDriver = driver.get();
        localDriver.get(STF_URL);
        System.out.println("STF Website opened: " + STF_URL);
    }

    /**
     * Logs into STF with predefined credentials.
     */
    private void stfLogin() {
        ChromeDriver localDriver = driver.get();
        System.out.println("Performing STF login...");

        findAndSendKeys(localDriver, USERNAME_FIELD, USERNAME, "Username");
        findAndSendKeys(localDriver, EMAIL_FIELD, EMAIL, "Email");

        clickElement(localDriver, LOGIN_BUTTON, "Login Button");
        ActionsUtil.SSleep(1); // Allow navigation
    }

    /**
     * Finds element, clicks, and sends keys safely.
     */
    private boolean findAndSendKeys(WebDriver driver, By locator, String value, String label) {
        try {
            WebElement el = driver.findElement(locator);
            el.click();
            el.clear();
            el.sendKeys(value);
            System.out.println(label + " entered: " + maskSensitiveData(value));
            return true;
        } catch (NoSuchElementException e) {
            System.err.println(label + " field not found: " + locator);
            return false;
        } catch (Exception e) {
            System.err.println("Error entering " + label + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Clicks element safely.
     */
    private boolean clickElement(WebDriver driver, By locator, String label) {
        try {
            WebElement el = driver.findElement(locator);
            el.click();
            System.out.println("Clicked: " + label);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println(label + " not found: " + locator);
            return false;
        } catch (Exception e) {
            System.err.println("Error clicking " + label + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Selects device and copies ADB command using JavaScript execCommand.
     */
    private void remoteDebug() throws IOException, UnsupportedFlavorException {
        ChromeDriver localDriver = driver.get();

        new STFDeviceSelect(localDriver);

        WebDriverWait wait = new WebDriverWait(localDriver, Duration.ofSeconds(10));
        try {
            wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(DEVICE_ADB_TEXTAREA, 1));
        } catch (TimeoutException e) {
            throw new RuntimeException("ADB textarea not loaded within timeout.", e);
        }

        java.util.List<WebElement> textAreas = localDriver.findElements(DEVICE_ADB_TEXTAREA);
        if (textAreas.size() < 2) {
            throw new RuntimeException("Expected at least 2 textareas; found " + textAreas.size());
        }

        WebElement adbField = textAreas.get(1);
        if (!adbField.isDisplayed()) {
            throw new RuntimeException("ADB command field is not visible.");
        }

        Actions actions = new Actions(localDriver);
        actions.moveToElement(adbField).click(adbField).perform();
        actions.setActivePointer(PointerInput.Kind.MOUSE, "mouse");
        actions.keyDown(Keys.CONTROL).sendKeys("c").keyUp(Keys.CONTROL).perform();
        ActionsUtil.sleep(500);

        // Retrieve from system clipboard
        Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        String copiedText;
        try {
            copiedText = (String) systemClipboard.getData(DataFlavor.stringFlavor);
        } catch (IllegalStateException ise) {
            throw new RuntimeException("Clipboard unavailable (headless mode?). Run in GUI environment.", ise);
        }

        if (copiedText == null || copiedText.trim().isEmpty()) {
            throw new RuntimeException("Clipboard returned empty or null text after copy operation.");
        }

        clipboard.set(copiedText.trim());
        System.out.println("Copied Text for Thread " + Thread.currentThread().getId() + ": " + copiedText.trim());
    }

    /**
     * Safely quits the driver for current thread.
     */
    public void releaseDevice() {
        ChromeDriver localDriver = driver.get();
        if (localDriver != null) {
            try {
                localDriver.quit();
                System.out.println("ChromeDriver closed for thread: " + Thread.currentThread().getId());
            } catch (Exception e) {
                System.err.println("Error closing driver: " + e.getMessage());
            } finally {
                driver.remove(); // Prevent memory leak
                clipboard.remove(); // Clean up copied text
            }
        } else {
            System.out.println("No driver to release for thread: " + Thread.currentThread().getId());
        }
    }

    /**
     * Masks sensitive data in logs.
     */
    private String maskSensitiveData(String value) {
        return value.length() > 4 ? "****" : value;
    }
}