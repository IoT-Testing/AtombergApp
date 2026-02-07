package app.BLEOnlyFans;

import app.resources.Locators.Android.DeviceScreens.FanLocators;
import app.util.Navigation;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class OpenAndControl {

    /*
    * mvn clean package -DskipTests
    *
    jpackage ^
  --input target ^
  --name "AtombergFanTest" ^
  --main-jar "atombergapp-test-0.0.1-SNAPSHOT.jar" ^
  --main-class app.BLEOnlyFans.TestRunner ^
  --type app-image ^
  --win-console ^
  --vendor "Atomberg" ^
  --app-version 1.0
    *
    * com.android.settings:id/pairing_accept_button
    *
    * echo @echo off > BoF.bat
    *
    *   @echo off
        AtombergFanTest.exe
        pause
    */


    private final AndroidDriver driver;
    private static final String APP_PACKAGE = "com.atomberg.app";
    private static PrintWriter csvWriter;
    private static boolean csvInitialized = false;
    // Locators
    private final By sleepOff = By.xpath("(//android.widget.ImageView[@content-desc=\"ON\"])[1]");
    private final By LEDOff = By.xpath("(//android.widget.ImageView[@content-desc=\"ON\"])[1]");
    private final By sleepOn = By.xpath("(//android.widget.ImageView[@content-desc=\"OFF\"])[3]");
    private final By LEDOn = By.xpath("(//android.widget.ImageView[@content-desc=\"OFF\"])[2]");
    private final By fanTile = By.xpath("//android.widget.ImageView[@content-desc=\"Fans\"]");
    private final By bluetoothBtn = By.xpath("//android.view.View[@index=\"3\"]");
    private final By bluetoothSuccess = By.xpath("//android.view.View[@content-desc=\"Device is connected via bluetooth\"]");
    private final By pair = By.id("com.android.settings:id/pairing_accept_button");

    public OpenAndControl(AndroidDriver driver) {
        this.driver = driver;
    }

    // ===== RETRY HELPER =====
    private boolean clickElementWithRetry(By locator) {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement el = driver.findElement(locator);
                if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                    el.click();
                    return true;
                }
            } catch (Exception ignored) {
                sleep(500);
            }
        }
        return false;
    }

    // ===== ACTION METHODS WITH STATE RETURN =====

    private boolean executeOpenDeviceControl(int attemptNumber) {
        String actionId = "Open Device Control";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            try {
                Navigation.openFanControl(driver);
                sleep(2000);

                if (isElementPresent(bluetoothBtn)) {
                    status = "Success";
                    success = true;
                }
            } catch (Exception e) {
                // ignore
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(500);
        }

        return success;
    }

    private boolean executeClickBluetooth(int attemptNumber) {
        String actionId = "click on Bluetooth";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            if (clickElementWithRetry(bluetoothBtn)) {
                sleep(500);
                if (isElementPresent(bluetoothSuccess)) {
                    status = "Success";
                    success = true;
                    driver.navigate().back();
                }
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeToggleSleep(int attemptNumber) {
        String actionId = "Toggle Sleep";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(sleepOff)) {
                if (clickElementWithRetry(sleepOff)) {
                    sleep(1000);
                    if (isElementPresent(sleepOn)) {
                        toggled = true;
                    }
                }
            } else if (isElementPresent(sleepOn)) {
                if (clickElementWithRetry(sleepOn)) {
                    sleep(1000);
                    if (isElementPresent(sleepOff)) {
                        toggled = true;
                    }
                }
            }

            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeToggleLED(int attemptNumber) {
        String actionId = "Toggle Sleep";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(sleepOff)) {
                if (clickElementWithRetry(sleepOff)) {
                    sleep(1000);
                    if (isElementPresent(sleepOn)) {
                        toggled = true;
                    }
                }
            } else if (isElementPresent(sleepOn)) {
                if (clickElementWithRetry(sleepOn)) {
                    sleep(1000);
                    if (isElementPresent(sleepOff)) {
                        toggled = true;
                    }
                }
            }

            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeSpeedOne(int attemptNumber) {
        String actionId = "Speed One";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.SPEED_1)) {
                if (clickElementWithRetry(FanLocators.SPEED_1)) {
                    sleep(500);
                    toggled = true;
                }
            }
            if (toggled) {
                status = "Success";
                success = true;
            }
            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeSpeedTwo(int attemptNumber) {
        String actionId = "Speed Two";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.SPEED_2)) {
                if (clickElementWithRetry(FanLocators.SPEED_2)) {
                    sleep(1000);
                    toggled = true;
                }
            }
            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeSpeedThree(int attemptNumber) {
        String actionId = "Speed 3";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.SPEED_3)) {
                if (clickElementWithRetry(FanLocators.SPEED_3)) {
                    sleep(1000);
                    toggled = true;
                }
            }
            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeSpeedFour(int attemptNumber) {
        String actionId = "Speed Four";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.SPEED_4)) {
                if (clickElementWithRetry(FanLocators.SPEED_4)) {
                    sleep(1000);
                    toggled = true;
                }
            }
            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeSpeedFive(int attemptNumber) {
        String actionId = "Speed 5";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.SPEED_5)) {
                if (clickElementWithRetry(FanLocators.SPEED_5)) {
                    sleep(1000);
                    toggled = true;
                }
            }
            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeBoost(int attemptNumber) {
        String actionId = "Execute Boost";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.BOOST_BUTTON)) {
                if (clickElementWithRetry(FanLocators.BOOST_BUTTON)) {
                    sleep(1000);
                    toggled = true;
                }
            }
            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }
            if (iteration == 5) {
                break;
            }
            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executePowerToggle(int attemptNumber) {
        String actionId = "Toggle Power";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            boolean toggled = false;

            if (isElementPresent(FanLocators.BOOST_BUTTON)) {
                if (clickElementWithRetry(FanLocators.BOOST_BUTTON)) {
                    sleep(1000);
                    {
                        toggled = true;
                    }
                }
            } else if (isElementPresent(sleepOn)) {
                if (clickElementWithRetry(sleepOn)) {
                    sleep(1000);
                    if (isElementPresent(sleepOff)) {
                        toggled = true;
                    }
                }
            }

            if (toggled) {
                status = "Success";
                success = true;
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    private boolean executeBackToHome(int attemptNumber) {
        String actionId = "Back To Home";
        int iteration = 1;
        boolean success = false;

        while (iteration <= 5) {
            String status = "Fail";

            try {

                driver.navigate().back();
                sleep(1500);
                ApplicationState appState = driver.queryAppState(APP_PACKAGE);
                if(appState.equals(ApplicationState.RUNNING_IN_BACKGROUND)) driver.activateApp(APP_PACKAGE);
                if (isElementPresent(fanTile)) {
                    status = "Success";
                    success = true;
                }
            } catch (Exception e) {
                status = "Fail";
            }

            printRow(attemptNumber, actionId, iteration, status);

            if (success) {
                break;
            }

            if (iteration == 5) {
                break;
            }

            iteration++;
            sleep(1000);
        }

        return success;
    }

    // ===== HELPER METHODS =====

    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // ===== MAIN EXECUTION PER ATTEMPT =====

    public void runOneAttempt(int attemptNumber) {
        // 1. Open Device Control
        boolean openDeviceSuccess = executeOpenDeviceControl(attemptNumber);

        if (!openDeviceSuccess) {
            executeBackToHome(attemptNumber);
            return;
        }

        // 2. Click on Bluetooth
        boolean bluetoothSuccess = executeClickBluetooth(attemptNumber);

        if (!bluetoothSuccess) {
            executeBackToHome(attemptNumber);
            return;
        }

        // 3. Toggle Sleep
        boolean toggleSleepSuccess = executeToggleSleep(attemptNumber);

        if (!toggleSleepSuccess) {
            executeBackToHome(attemptNumber);
            return;
        }

        // 4. Back To Home (only if all previous succeeded)
        executeBackToHome(attemptNumber);
    }

    private static void initCSV() {
        if (csvInitialized) return;
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            csvWriter = new PrintWriter(new FileWriter("test_results_" + timestamp + ".csv", true));
            csvWriter.println("Attempt Number,Action ID,Iteration,Status");
            csvWriter.flush();
            csvInitialized = true;
        } catch (IOException e) {
            System.err.println("Failed to create CSV file: " + e.getMessage());
        }
    }

    // Replace your printRow method
    private void printRow(int attemptNumber, String actionId, int iteration, String status) {
        // Format: clean, no extra spaces
        String row = String.format("%d,%s,%d,%s", attemptNumber, actionId, iteration, status);

        // Write to CSV
        initCSV();
        csvWriter.println(row);
        csvWriter.flush();

        // Optional: also print to console (for user visibility)
        System.out.printf("%-15d | %-20s | %-10d | %-8s%n",
                attemptNumber, actionId, iteration, status);
    }

    // Close CSV when done (call at end of run)
    public static void closeCSV() {
        if (csvWriter != null) {
            csvWriter.close();
        }
    }
}