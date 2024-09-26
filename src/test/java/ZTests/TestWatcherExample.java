package ZTests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import java.util.Optional;

public class TestWatcherExample implements TestWatcher {

    @Override
    public void testDisabled(ExtensionContext context, Optional<String> reason) {
        TestWatcher.super.testDisabled(context, reason);
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        TestWatcher.super.testSuccessful(context);
        ExtentReportAT.getTest().log(Status.PASS, "Test Passed");
        System.out.println("TEST SUCCESSFUL");
    }

    @Override
    public void testAborted(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testAborted(context, cause);
        System.out.println("TEST ABORTED");
    }

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        TestWatcher.super.testFailed(context, cause);
        System.out.println("TEST FAILED");
        ExtentReportAT.getTest().log(Status.FAIL, "Test failed: " + cause.getMessage());

    }

    ExtentReports extent = ExtentReportAT.getReportObjects();
    ExtentTest test;

   /* @Override
    public void onTestStart(TestResult result)
    {
        test = extent.createTest(result.getMethod().getMethodName());
    }
    @Override
    public void onTestSuccess(TestResult result)
    {
        test.log(Status.PASS, "Test Passed");

    }
    @Override
    public void onTestFailure(TestResult result) {
        test.fail(result.getThrowable());
    }*/

}
