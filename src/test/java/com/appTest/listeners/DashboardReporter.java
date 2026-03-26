package com.appTest.listeners;

import com.appTest.models.AppiumTestResult;
import org.testng.*;
import com.fasterxml.jackson.databind.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DashboardReporter implements ITestListener {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
    private static final String OUTPUT_FILE = "test-results/dashboard-data.json";
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // Store results in memory for batch write
    private static final List<AppiumTestResult> results = new ArrayList<>();

    @Override
    public void onTestStart(ITestResult result) {
        // Optional: log test start
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        saveResult(result, "passed", null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String errorMsg = result.getThrowable() != null ?
                result.getThrowable().getMessage() : "Unknown error";
        String stackTrace = result.getThrowable() != null ?
                getStackTraceAsString(result.getThrowable()) : null;
        saveResult(result, "failed", errorMsg, stackTrace);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        saveResult(result, "skipped", result.getThrowable() != null ?
                result.getThrowable().getMessage() : "Test skipped");
    }

    @Override
    public void onFinish(ITestContext context) {
        // Write all results to file when suite completes
        writeResultsToFile();

        // Optional: Send to API endpoint
        // sendToDashboardAPI();
    }

    private void saveResult(ITestResult result, String status, String errorMessage) {
        saveResult(result, status, errorMessage, null);
    }

    private void saveResult(ITestResult result, String status, String errorMessage, String stackTrace) {
        // Extract platform/device from TestNG parameters or capabilities
        String platform = extractParameter(result, "platform",
                System.getProperty("appium.platformName", "Android"));
        String device = extractParameter(result, "deviceName",
                System.getProperty("appium.deviceName", "Unknown"));

        // Calculate duration in seconds
        long duration = (result.getEndMillis() - result.getStartMillis()) / 1000;

        AppiumTestResult testResult = new AppiumTestResult.Builder()
                .id(ID_GENERATOR.getAndIncrement())
                .name(result.getMethod().getMethodName())
                .status(status)
                .platform(platform)
                .device(device)
                .duration(duration)
                .timestamp(Instant.now().toString())
                .className(result.getTestClass().getName())
                .errorMessage(errorMessage)
                .stackTrace(stackTrace)
                .build();

        results.add(testResult);
    }

    private String extractParameter(ITestResult result, String paramName, String defaultValue) {
        // Try TestNG parameters first
        if (result.getTestContext().getCurrentXmlTest().getParameter(paramName) != null) {
            return result.getTestContext().getCurrentXmlTest().getParameter(paramName);
        }
        // Fallback to system property
        return System.getProperty("appium." + paramName, defaultValue);
    }

    private String getStackTraceAsString(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        throwable.printStackTrace(new java.io.PrintWriter(sw));
        return sw.toString();
    }

    private void writeResultsToFile() {
        try {
            // Ensure directory exists
            new File("test-results").mkdirs();

            // Write as JSON array (append mode for multiple runs)
            String json = MAPPER.writeValueAsString(results);
            Files.write(
                    Paths.get(OUTPUT_FILE),
                    json.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
            System.out.println("✓ Dashboard results written to: " + OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("✗ Failed to write dashboard results: " + e.getMessage());
        }
    }

//   //  Optional: Send directly to a backend API
//    private void sendToDashboardAPI() {
//        // Uncomment and configure if using REST API approach
//
//        try {
//            OkHttpClient client = new OkHttpClient();
//            String json = MAPPER.writeValueAsString(results);
//
//            RequestBody body = RequestBody.create(
//                json,
//                MediaType.parse("application/json; charset=utf-8")
//            );
//
//            Request request = new Request.Builder()
//                .url("http://localhost:3000/api/test-results") // Your API endpoint
//                .post(body)
//                .build();
//
//            client.newCall(request).execute();
//        } catch (Exception e) {
//            System.err.println("Failed to send to API: " + e.getMessage());
//        }
//
//    }
}