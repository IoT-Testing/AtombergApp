package Listeners;

import io.appium.java_client.android.AndroidDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestListeners implements ITestListener {

    protected AndroidDriver driver;
    protected String deviceSlot;

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("▶️ Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✅ Test Passed: " + result.getMethod().getMethodName());
    }

    public void afterTestFailure() {
        try {
            System.out.println("📱 Checking screen on device: " + deviceSlot);
            String activity = driver.currentActivity();
            System.out.println("📍 Current activity: " + activity);
            String appPackage = driver.getCurrentPackage();
            System.out.println("📦 Current package: " + appPackage);
        } catch (Exception e) {
            System.out.println("⚠️ Error in afterTestFailure(): " + e.getMessage());
        }
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⏭️ Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Optional override
    }

    @Override
    public void onStart(ITestContext context) {
        System.out.println("🔵 Test Context Start: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        System.out.println("🟢 Test Context Finish: " + context.getName());
    }
}
