package ZTests;

import org.junit.jupiter.api.extension.TestWatcher;
import org.testng.internal.TestResult;

public interface Watcher extends TestWatcher {
    void onTestStart(TestResult result);

    void onTestSuccess(TestResult result);

    void onTestFailure(TestResult result);
}
