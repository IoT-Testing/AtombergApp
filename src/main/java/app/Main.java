package app;

import app.Fan.FanManagement;
import app.Login.Email;
import app.resources.Locators.FanLocators;
import app.util.ActionsUtil;
import app.util.PermissionUtil;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class Main {
    private static final By bof = By.xpath("//android.view.View[@content-desc=\"Atomberg_R3_fea1f937004b1200\"]");

    private AndroidDriver driver;
    private final ServerInitializer server = new ServerInitializer();

    public static void main(String[] args) {
        Main main = new Main();
        try {
            main.runTestFlow();
        } catch (Exception e) {
            System.err.println("Test failed with exception: " + e.getMessage());
        } finally {
            main.quitDriverSafely();
        }
    }

    public void runTestFlow() throws Exception {
        initializeDriver();
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(5);
        ActionsUtil.Tap.withCoordinates(driver,540,1940);
        FanManagement fan = new FanManagement(driver);
        fan.addition(bof);
//        driver.openNotifications();
//        ActionsUtil.sleep(750);
//        ActionsUtil.Tap.withCoordinates(driver, 500, 500);
        By acceptBtn = By.id("android:id/button1");
        driver.findElement(acceptBtn).click();
        ActionsUtil.SSleep(6);
//        driver.openNotifications();
//        ActionsUtil.sleep(500);
//        ActionsUtil.Tap.withCoordinates(driver, 500, 500);
        driver.findElement(acceptBtn).click();
        bleFanAddition();
    }
    private boolean isElementPresent(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    private boolean clickElementWithRetry(By locator) {
        for (int i = 0; i < 5; i++) {
            try {
                WebElement el = driver.findElement(locator);
                if (el.isDisplayed() && Boolean.parseBoolean(el.getDomAttribute("clickable"))) {
                    el.click();
                    return true;
                }
            } catch (Exception ignored) {}
        }
        return false;
    }

    private void bleFanDeletion(){
        ActionsUtil.Tap.withCoordinates(driver, 700, 975);
        driver.findElement(By.xpath("//android.view.View[@index=\"4\"]")).click();
        driver.findElement(By.xpath("//android.view.View[@content-desc=\"Delete device\"]")).click();

    }

    private void bleFanAddition(){
        driver.findElement(FanLocators.NEXT_BUTTON).click();
        ActionsUtil.sleep(500);
        driver.findElement(FanLocators.CONTINUE_BUTTON).click();
    }




//TODO :  Custom timer
    //TODO : All Commands
    // === Setup Methods ===

    private void initializeDriver() throws Exception {
        AppInitializer initializer = new AppInitializer();
        initializer.initializeDriver(); // Connects to device
        this.driver = initializer.getDriver();

        // Set implicit wait
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        System.out.println("Driver initialized successfully.");
    }


    private void launchApp() {
        ActionsUtil.SSleep(2);
        driver.activateApp("com.atomberg.app");
        ActionsUtil.SSleep(3);
        System.out.println("App launched.");
    }

    private boolean isOnLoginScreen() {
        AppInitializer appCheck = new AppInitializer();
        appCheck.setDriver(driver);
        boolean onLogin = appCheck.checkMainScreen();
        System.out.println("On login screen: " + onLogin);
        return onLogin;
    }

    private void performLogin() throws Exception {
        String email = getEnvOrFallback("TEST_EMAIL", "iot.alpha@protonmail.com");
        String password = getEnvOrFallback("TEST_PASSWORD", "Atomberg@123");

        Email login = new Email(driver);
        try {
            login.email(email, password);
            System.out.println("Login successful.");
        } catch (Exception e) {
            System.err.println("Login failed: " + e.getMessage());
            throw e; // Re-throw after logging
        }
    }

    private void handlePermissions() {
        PermissionUtil.allow(driver);
        System.out.println("Permissions handled.");
    }


    // === Utility Methods ===

    public void quitDriverSafely() {
        if (driver != null) {
            try {
                driver.quit();
                System.out.println("Driver session ended.");
            } catch (Exception e) {
                System.err.println("Error during driver quit: " + e.getMessage());
            }
        }
    }

    private String getEnvOrFallback(String key, String fallback) {
        String value = System.getenv(key);
        return value != null ? value : fallback;
    }
}