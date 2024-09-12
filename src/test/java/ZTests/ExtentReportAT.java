package ZTests;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.appium.java_client.AppiumDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportAT {
    public static ExtentReports extent;
    public static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();
    public ExtentReportAT() {

    }
    public static ExtentReports getReportObjects() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        String basePath = System.getProperty("user.dir") + "\\reports\\Atomberg";
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String newFolderPath = basePath + date;
        File newFolder = new File(newFolderPath);
        if (!newFolder.exists())
        {
            newFolder.mkdir();
        }
        String s = timestamp + ".html";
        String path = newFolderPath+"\\"+ s;
        ExtentSparkReporter reporter = new ExtentSparkReporter(path);
        reporter.config().setReportName("App Test Reports");
        reporter.config().setDocumentTitle("Test Reports");

        extent = new ExtentReports();
        extent.attachReporter(reporter);
        extent.setSystemInfo("Tester","Rohit Bhagat");
        System.out.println("Report created at " + path);

        return extent;
    }
    public static void startTest(String testName) {
        ExtentTest extentTest = extent.createTest(testName);
        test.set(extentTest);
        startTime.set(System.currentTimeMillis());
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Test Started at " + new SimpleDateFormat("HH:mm:ss").format(new Date(startTime.get())));
    }

    public static void endTest() {
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime.get();

        // Convert duration to readable format
        long millis = duration % 1000;
        long second = (duration / 1000) % 60;
        long minute = (duration / (1000 * 60)) % 60;
        long hour = (duration / (1000 * 60 * 60)) % 24;

        String timeFormatted = String.format("%02d:%02d:%02d.%03d", hour, minute, second, millis);

        ExtentTest extentTest = test.get();
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Test Ended at " + new SimpleDateFormat("HH:mm:ss").format(new Date(endTime)));
        extentTest.log(com.aventstack.extentreports.Status.INFO, "Test Duration: " + timeFormatted);
        extent.getStats();
        extent.flush();
    }

    public static ExtentTest getTest() {
        return test.get();
    }

}
