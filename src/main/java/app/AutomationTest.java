package app;

import app.Analytics.Analytics;
import app.Automations.Automation;
import app.util.ActionsUtil;
import io.appium.java_client.AppiumDriver;

public class AutomationTest {

    public AppiumDriver driver;

    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        appInitializer.login();
        Automation automation = new Automation(driver);
        automation.TimeOfDay();
        ActionsUtil.sleep(2000);
        automation.QuickAccess();
        ActionsUtil.sleep(5000);
        automation.deleteAutomations();
    }
}
