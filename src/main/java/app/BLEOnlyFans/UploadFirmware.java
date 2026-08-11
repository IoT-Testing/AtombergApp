package app.BLEOnlyFans;

import app.util.ActionsUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;


public class UploadFirmware extends FirmwareVersionChecker {
    private final AndroidDriver driver;

    public UploadFirmware(AndroidDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public boolean runProgressiveFWUpload() {
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            try {
                // Step 1: Click Start (if not already started)
                try {
                    driver.findElement(By.xpath("//android.widget.Button[@content-desc=\"Start\"]")).click();
                    logpoint("â–¶ï¸ Start button clicked.");
                } catch (Exception e) {
                    logpoint("âš ï¸ 'Start' button not found or already running.");
                }
                ActionsUtil.sleep(25000);
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
