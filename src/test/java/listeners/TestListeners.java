package listeners;


import io.appium.java_client.android.AndroidDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;


public class TestListeners implements ITestListener {

    protected AndroidDriver driver;
    protected String deviceSlot;

    @Override
    public void onTestStart(ITestResult result) {
        logpoint("â–¶ï¸ Test Started: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logpoint("âœ… Test Passed: " + result.getMethod().getMethodName());
    }

    public void afterTestFailure() {
        try {
            logpoint("ðŸ“± Checking screen on device: " + deviceSlot);
            String activity = driver.currentActivity();
            logpoint("ðŸ“ Current activity: " + activity);
            String appPackage = driver.getCurrentPackage();
            logpoint("ðŸ“¦ Current package: " + appPackage);
        } catch (Exception e) {
            logpoint("âš ï¸ Error in afterTestFailure(): " + e.getMessage());
        }
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        logpoint("â­ï¸ Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        // Optional override
    }

    @Override
    public void onStart(ITestContext context) {
        logpoint("ðŸ”µ Test Context Start: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        logpoint("ðŸŸ¢ Test Context Finish: " + context.getName());
    }
}

