package com.appTest.listeners;

import com.appTest.models.AppiumTestResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.testng.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * DashboardReporter – writes test results to a JSON file for dashboard consumption.
 * FIX H5: The original {@code results} field was a plain static {@code ArrayList}.
 * In parallel runs ({@code parallel="tests"}) multiple threads call onTestSuccess /
 * onTestFailure / onTestSkipped concurrently → {@code ConcurrentModificationException}.
 * Fix: replaced with {@link CopyOnWriteArrayList} which is thread-safe for concurrent
 * writes without needing explicit synchronization. For write-heavy scenarios a
 * {@code ConcurrentLinkedQueue} would be preferable, but test suites produce at most
 * hundreds of results so CopyOnWriteArrayList's copy-on-write cost is negligible.
 */
public class DashboardReporter implements ITestListener {

    private static final AtomicLong ID_GEN      = new AtomicLong(1);
    private static final String     OUTPUT_DIR   = "test-results";
    private static final String     OUTPUT_FILE  = OUTPUT_DIR + "/dashboard-data.json";

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    /*
     * FIX H5: CopyOnWriteArrayList replaces ArrayList for concurrent safety.
     * Static field so all listener instances (one per parallel thread) share the
     * same result accumulator — matches the intended "one file per suite" design.
     */
    private static final List<AppiumTestResult> RESULTS = new CopyOnWriteArrayList<>();

    // ── ITestListener callbacks ──────────────────────────────────────────────

    @Override
    public void onTestSuccess(ITestResult result) {
        record(result, "passed", null, null);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        Throwable t = result.getThrowable();
        record(result, "failed",
                t != null ? t.getMessage() : "Unknown error",
                t != null ? stackTraceString(t) : null);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        Throwable t = result.getThrowable();
        record(result, "skipped",
                t != null ? t.getMessage() : "Skipped",
                null);
    }

    @Override
    public void onFinish(ITestContext context) {
        writeResultsToFile();
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private void record(ITestResult result, String status, String error, String stackTrace) {
        String platform = param(result, "platform",    System.getProperty("appium.platformName", "Android"));
        String device   = param(result, "deviceSlot",  System.getProperty("appium.deviceName",   "Unknown"));
        long   duration = (result.getEndMillis() - result.getStartMillis()) / 1_000;

        AppiumTestResult r = new AppiumTestResult.Builder()
                .id(ID_GEN.getAndIncrement())
                .name(result.getMethod().getMethodName())
                .status(status)
                .platform(platform)
                .device(device)
                .duration(duration)
                .timestamp(Instant.now().toString())
                .errorMessage(error)
                .stackTrace(stackTrace)
                .build();

        RESULTS.add(r);  // Thread-safe add
    }

    private void writeResultsToFile() {
        try {
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            MAPPER.writeValue(new File(OUTPUT_FILE), RESULTS);
            System.out.println("[DashboardReporter] Results written to: " + OUTPUT_FILE);
        } catch (IOException e) {
            System.err.println("[DashboardReporter] Failed to write results file: " + e.getMessage());
        }
    }

    private String param(ITestResult result, String paramName, String fallback) {
        // Try TestNG parameters first
        Object[] params = result.getParameters();
        // Parameters are positional, not named — use system properties as fallback
        String v = result.getTestContext().getCurrentXmlTest().getParameter(paramName);
        return (v != null && !v.isBlank()) ? v : fallback;
    }

    private String stackTraceString(Throwable t) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement e : t.getStackTrace()) {
            sb.append("  at ").append(e).append("\n");
            if (sb.length() > 2_000) { sb.append("  ... truncated"); break; }
        }
        return sb.toString();
    }
}