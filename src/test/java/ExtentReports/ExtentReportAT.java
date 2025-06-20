package ExtentReports;

import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ExtentReportAT {
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testNode = new ThreadLocal<>();
    private static final ConcurrentHashMap<String, ExtentTest> parentMap = new ConcurrentHashMap<>();

    public ExtentReportAT(String deviceSlot) {
        if (extent == null) {
            String basePath = System.getProperty("user.dir") + "\\reports\\Atomberg";
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String reportDir = basePath + date;

            File dir = new File(reportDir);
            if (!dir.exists()) dir.mkdirs();

            String filePath = reportDir + "\\" + timestamp + ".html";
            ExtentSparkReporter reporter = new ExtentSparkReporter(filePath);
            reporter.config().setDocumentTitle("Parallel Test Report");
            reporter.config().setReportName("Device Execution Summary");

            extent = new ExtentReports();
            extent.attachReporter(reporter);
            extent.setSystemInfo("Tester", "Rohit Bhagat");
            System.out.println("Report created at " + filePath);
        }

        // Only create the parent node once per deviceSlot
        parentMap.computeIfAbsent(deviceSlot, slot -> extent.createTest("Device " + slot));
    }

    public void startTest(String testName, String deviceSlot) {
        ExtentTest parent = parentMap.get(deviceSlot);
        ExtentTest node = parent.createNode(testName);
        testNode.set(node);
    }

    public void log(Status status, String message) {
        testNode.get().log(status, message);
    }

    public void endTest() {
        extent.flush();
    }
    public Status getCurrentStatus() {
        return testNode.get().getStatus();
    }

}
