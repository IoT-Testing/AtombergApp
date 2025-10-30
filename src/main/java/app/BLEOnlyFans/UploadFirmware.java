package app.BLEOnlyFans;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;


public class UploadFirmware extends FirmwareVersionChecker {
    private final AndroidDriver driver;
    private final FirmwareVersionChecker csvLogger;

    public UploadFirmware(AndroidDriver driver, FirmwareVersionChecker csvLogger) {
        super(driver);
        this.driver = driver;
        this.csvLogger = csvLogger;
    }

    public boolean runProgressiveFWUpload(String expectedVersion,int attemptNumber) {
        String actionId = "Uninterrupted OTA";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";
            String versionForLogging = ""; // Always empty for pause-resume

            try {
                // Step 1: Click Start (if not already started)
                try {
                    driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
                    System.out.println("▶️ Start button clicked.");
                } catch (Exception e) {
                    System.out.println("⚠️ 'Start' button not found or already running.");
                }
                ActionsUtil.sleep(40000);
                status = "Success";
                success = true;
            } catch (Exception e) {
                // Ignore and retry
            }
            // Log to the shared CSV (versionForLogging is always empty)
//            csvLogger.printRow(attemptNumber, actionId, iteration, status, versionForLogging);
            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
        }
        return success;
    }

    /**
     * Checks if an element is present and visible
     */
    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

}