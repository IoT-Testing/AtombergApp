package com.appTest.util;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriverException;
import app.util.ActionsUtil;

/**
 * TestHelper - Provides common utilities for test classes.
 * Centralizes repetitive operations and best practices.
 */
public class TestHelper {

    /**
     * Waits for an element to be visible with timeout.
     *
     * @param driver AndroidDriver instance
     * @param locator Element locator
     * @param timeoutSec Timeout in seconds
     * @return WebElement if found and visible
     * @throws RuntimeException if timeout expires
     */
    public static WebElement waitForElement(AndroidDriver driver, By locator, long timeoutSec) {
        final long POLLING_INTERVAL_MS = 500;
        final long TIMEOUT_MS = timeoutSec * 1000;
        long startTimeMs = System.currentTimeMillis();

        while ((System.currentTimeMillis() - startTimeMs) < TIMEOUT_MS) {
            try {
                WebElement element = driver.findElement(locator);
                if (element.isDisplayed()) {
                    System.out.println("✅ Found element: " + locator);
                    return element;
                }
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                // Expected during retry loop
            } catch (WebDriverException e) {
                System.err.println("⚠️  WebDriver error: " + e.getMessage());
            }
            ActionsUtil.sleep(POLLING_INTERVAL_MS);
        }

        throw new RuntimeException("❌ Element not found after " + timeoutSec + "s: " + locator);
    }

    /**
     * Safely checks if element exists and is displayed.
     *
     * @param driver AndroidDriver instance
     * @param locator Element locator
     * @return true if element exists and is displayed
     */
    public static boolean isElementDisplayed(AndroidDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    /**
     * Safely clicks element if it exists and is displayed.
     *
     * @param driver AndroidDriver instance
     * @param locator Element locator
     * @return true if click successful, false if element not found
     */
    public static boolean clickIfExists(AndroidDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            if (element.isDisplayed()) {
                element.click();
                System.out.println("✅ Clicked element: " + locator);
                return true;
            }
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            System.out.println("⚠️  Element not found for click: " + locator);
        }
        return false;
    }

    /**
     * Navigates back with max retries to prevent infinite loops.
     *
     * @param driver AndroidDriver instance
     * @param maxRetries Maximum back presses
     */
    public static void navigateBackWithLimit(AndroidDriver driver, int maxRetries) {
        for (int i = 0; i < maxRetries; i++) {
            try {
                driver.navigate().back();
                ActionsUtil.sleep(1000);
                System.out.println("Back pressed (" + (i + 1) + "/" + maxRetries + ")");
            } catch (Exception e) {
                System.err.println("Back press failed: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * Retry operation with exponential backoff.
     *
     * @param operation Operation to retry
     * @param maxRetries Maximum retry attempts
     * @param initialDelayMs Initial delay in milliseconds
     * @return true if operation succeeded
     */
    public static boolean retryOperation(
            TestOperation operation,
            int maxRetries,
            long initialDelayMs) {
        long delay = initialDelayMs;
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                operation.execute();
                System.out.println("✅ Operation succeeded on attempt " + attempt);
                return true;
            } catch (Exception e) {
                System.err.println("⚠️  Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt < maxRetries) {
                    ActionsUtil.sleep(delay);
                    delay *= 2; // Exponential backoff
                }
            }
        }
        System.err.println("❌ Operation failed after " + maxRetries + " attempts");
        return false;
    }

    /**
     * Functional interface for operations that can be retried.
     */
    @FunctionalInterface
    public interface TestOperation {
        void execute() throws Exception;
    }

    /**
     * Appium scroll action wrapper.
     *
     * @param driver AndroidDriver instance
     * @param direction Direction to scroll (UP, DOWN, LEFT, RIGHT)
     */
    public static void scroll(AndroidDriver driver, ScrollDirection direction) {
        try {
            int width = driver.manage().window().getSize().getWidth();
            int height = driver.manage().window().getSize().getHeight();

            switch (direction) {
                case UP:
                    driver.executeScript("mobile:scroll", new java.util.HashMap<String, Object>() {{
                        put("direction", "up");
                        put("percent", 0.5);
                    }});
                    break;
                case DOWN:
                    driver.executeScript("mobile:scroll", new java.util.HashMap<String, Object>() {{
                        put("direction", "down");
                        put("percent", 0.5);
                    }});
                    break;
            }
            System.out.println("✅ Scrolled " + direction);
        } catch (Exception e) {
            System.err.println("⚠️  Scroll failed: " + e.getMessage());
        }
    }

    public enum ScrollDirection {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * Safely gets text from element.
     *
     * @param driver AndroidDriver instance
     * @param locator Element locator
     * @return Element text or empty string if not found
     */
    public static String getElementText(AndroidDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.getText();
        } catch (NoSuchElementException e) {
            System.out.println("⚠️  Element not found: " + locator);
            return "";
        }
    }

    /**
     * Safely sets text to input field.
     *
     * @param driver AndroidDriver instance
     * @param locator Input field locator
     * @param text Text to enter
     * @return true if successful
     */
    public static boolean setText(AndroidDriver driver, By locator, String text) {
        try {
            WebElement element = driver.findElement(locator);
            element.clear();
            element.sendKeys(text);
            System.out.println("✅ Text entered: " + text);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("❌ Input field not found: " + locator);
            return false;
        }
    }
}

