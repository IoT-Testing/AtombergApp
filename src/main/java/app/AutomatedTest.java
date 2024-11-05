package app;

import app.Fan.FanManagement;
import app.MoreTab.Help;
import app.MoreTab.Manage;
import app.MoreTab.Play;
import app.MoreTab.Profile;
import app.ScreenCheck.ScreenCheck;
import app.util.ActionsUtil;
import app.util.ReadFromCSV;
import io.appium.java_client.AppiumDriver;
import java.util.List;

public class AutomatedTest {

    public AppiumDriver driver;
    public void run() {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        driver = appInitializer.getDriver();
        appInitializer.checkMainScreen();
        Manage manage = new Manage(driver);
        FanManagement fan = new FanManagement(driver);
        ScreenCheck screen = new ScreenCheck(driver);
        List<List<String>> credentials = ReadFromCSV.readFromCSV();
        for (List<String> credential : credentials) {
            String email = credential.get(0);
            String password = credential.get(1);
            appInitializer.login(email, password);
            ActionsUtil.sleep(1000);
            screen.homeScreen();
            fan.addFan();
            fan.additionProcess();
            manage.logout();
            ActionsUtil.sleep(2000);
        }
    }
}
