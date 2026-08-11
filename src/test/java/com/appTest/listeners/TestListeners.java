package com.appTest.listeners;

import io.appium.java_client.android.AndroidDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * TestListeners â€“ TestNG lifecycle listener for console logging.
 *
 * FIX C13: The original implementation had {@code driver} and {@code deviceSlot}
 * as instance fields that were never populated â€” a TestNG listener is NOT the same
 * object as the test class, so those fields are always null, causing NPE in
 * {@code afterTestFailure()}.
 *
 * Fix: extract driver from the test instance via {@link ITestResult#getInstance()}
 * only when needed, with a null guard. This is the correct ITestListener pattern.
 */
public class TestListeners implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
        try {
            logpoint("â–¶  Test Started:  [" + getSlot(result) + "] " + result.getMethod().getMethodName());
        } catch (Exception e) {
            logpoint("âš   onTestStart error: " + e.getMessage());
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logpoint("âœ… Test Passed:   [" + getSlot(result) + "] " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String slot = getSlot(result);
        logpoint("âŒ Test Failed:   [" + slot + "] " + result.getMethod().getMethodName());

        // FIX C13: Extract driver from the test instance, not from an unpopulated field.
        AndroidDriver driver = extractDriver(result);
        if (driver != null) {
            try {
                logpoint("   Current activity: " + driver.currentActivity());
                logpoint("   Current package:  " + driver.getCurrentPackage());
            } catch (Exception e) {
                logpoint("   Could not query device state: " + e.getMessage());
            }
        }

        if (result.getThrowable() != null) {
            logpoint("   Cause: " + result.getThrowable().getMessage());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logpoint("â­  Test Skipped: [" + getSlot(result) + "] " + result.getMethod().getMethodName());
    }

    @Override
    public void onStart(ITestContext context) {
        logpoint("ðŸ”µ Context Start: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.printf("ðŸŸ¢ Context Finish: %s â€” passed=%d, failed=%d, skipped=%d%n",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    // â”€â”€ Helpers â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€â”€

    /**
     * Safely reads the deviceSlot field from the test instance via reflection.
     * Returns "?" if the instance is not a BaseTest or the field is null.
     */
    private String getSlot(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof com.appTest.tests.BaseTest base) {
            String slot = base.deviceSlot;
            return slot != null ? slot : "?";
        }
        return "?";
    }

    /**
     * FIX C13: Extracts the AndroidDriver from the test instance.
     * Returns null safely if the instance is not a BaseTest or driver is null.
     */
    private AndroidDriver extractDriver(ITestResult result) {
        Object instance = result.getInstance();
        if (instance instanceof com.appTest.tests.BaseTest base) {
            return base.driver;
        }
        return null;
    }
}
