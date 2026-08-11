package ExtentReports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ExtentReportAT - Thread-safe wrapper for ExtentReports with multi-device support.
 */
public class ExtentReportAT {

    // === Static Fields ===
    private static volatile ExtentReports extent; // Volatile for safe publication
    private static final Object INIT_LOCK = new Object(); // Lock for lazy init

    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, ExtentTest> parentMap = new ConcurrentHashMap<>();

    private static final String REPORT_BASE_DIR = System.getProperty("user.dir") + File.separator +
            "reports" + File.separator + "Atomberg";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yyyyMMdd_HHmmss");

    /**
     * Constructor initializes global ExtentReports instance (once).
     *
     * @param deviceSlot Unique identifier for device (e.g., "Pixel_5", "OnePlus_8")
     */
    public ExtentReportAT(String deviceSlot) {
        initializeExtentIfNeeded();
        createParentTestIfAbsent(deviceSlot);
    }

    /**
     * Lazily initializes ExtentReports in a thread-safe manner.
     */
    private void initializeExtentIfNeeded() {
        if (extent == null) {
            synchronized (INIT_LOCK) {
                if (extent == null) { // Double-checked locking
                    extent = createExtentReports();
                }
            }
        }
    }

    /**
     * Creates and configures the ExtentReports instance.
     *
     * @return Configured ExtentReports object
     */
    private ExtentReports createExtentReports() {
        String dateFolder = DATE_FORMAT.format(new Date());
        String reportDirPath = REPORT_BASE_DIR + File.separator + dateFolder;

        File reportDir = new File(reportDirPath);
        if (!reportDir.exists()) {
            boolean created = reportDir.mkdirs();
            if (!created) {
                System.err.println("Failed to create report directory: " + reportDirPath);
            }
        }

        String timestamp = TIMESTAMP_FORMAT.format(new Date());
        String filePath = reportDirPath + File.separator + timestamp + ".html";

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(filePath);
        sparkReporter.config().setDocumentTitle("Atomberg Automation Report");
        sparkReporter.config().setReportName("Test Execution Summary");
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setEncoding("utf-8");

        ExtentReports ext = new ExtentReports();
        ext.attachReporter(sparkReporter);
        ext.setSystemInfo("Tester", "Rohit Bhagat");
        ext.setSystemInfo("Environment", "Production");

        System.out.println("Report generated at: " + filePath);
        return ext;
    }

    /**
     * Ensures a parent test node exists for the given device slot.
     *
     * @param deviceSlot Device identifier
     */
    private void createParentTestIfAbsent(String deviceSlot) {
        parentMap.computeIfAbsent(deviceSlot, slot -> {
            ExtentTest parent = extent.createTest("Device: " + slot)
                    .assignCategory(slot)
                    .pass("Test session started for device");
            return parent;
        });
    }

    /**
     * Registers a parent node for the given device slot if one does not already
     * exist. Needed by multi-device tests (e.g. the two-phone Device Sharing
     * suite) that report under several slots — "Admin_Device", "Member_Device",
     * "Admin+Member" — from a single reporter instance.
     *
     * @param deviceSlot Device identifier to register
     */
    public void registerDevice(String deviceSlot) {
        createParentTestIfAbsent(deviceSlot);
    }

    /**
     * Starts a new child test under the device-specific parent node.
     *
     * @param testName    Name of the test
     * @param deviceSlot  Device identifier
     */
    public void startTest(String testName, String deviceSlot) {
        ExtentTest parent = parentMap.get(deviceSlot);
        if (parent == null) {
            throw new IllegalStateException("No parent test found for device: " + deviceSlot);
        }

        ExtentTest node = parent.createNode(testName);
        testNode.set(node);
        System.out.println("Started test: " + testName + " [Device: " + deviceSlot + "]");
    }

    /**
     * Logs a message with optional screenshot.
     *
     * @param status         Log status (PASS, FAIL, INFO, etc.)
     * @param message        Message to log
     * @param screenshotPath Path to screenshot (can be null)
     */
    public void log(Status status, String message, String screenshotPath) {
        ExtentTest test = testNode.get();
        if (test == null) {
            System.err.println("No active test node. Cannot log: " + message);
            return;
        }

        if (screenshotPath != null && !screenshotPath.trim().isEmpty()) {
            try {
                test.log(status, message,
                        com.aventstack.extentreports.MediaEntityBuilder
                                .createScreenCaptureFromPath(screenshotPath)
                                .build());
            } catch (Exception e) {
                System.err.println("Failed to attach screenshot: " + screenshotPath);
                test.log(status, message + " [Screenshot failed to attach]");
            }
        } else {
            test.log(status, message);
        }
    }

    /**
     * Logs a message without screenshot.
     *
     * @param status  Log status
     * @param message Message to log
     */
    public void log(Status status, String message) {
        log(status, message, null);
    }

    /**
     * Ends the current test and flushes the report.
     */
    public void endTest() {
        ExtentTest current = testNode.get();
        if (current != null) {
            testNode.remove(); // Prevent memory leak
        }
        extent.flush(); // Always flush to write to disk
    }

    /**
     * Gets the current test status.
     *
     * @return Current status or null if no test running
     */
    public Status getCurrentStatus() {
        ExtentTest test = testNode.get();
        return test != null ? test.getStatus() : null;
    }

    /**
     * Shuts down the reporter and releases resources.
     * Should be called once at suite level.
     */
    public void close() {
        if (extent != null) {
            extent.flush();
            extent = null;
        }
        parentMap.clear();
        System.out.println("ExtentReports closed and resources released.");
    }
}