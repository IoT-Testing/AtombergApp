package app;

import app.MoreTab.Manage;
import app.MoreTab.Profile;
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
        List<List<String>> credentials = ReadFromCSV.readFromCSV();
        for (List<String> credential : credentials) {
            String email = credential.get(0);
            String password = credential.get(1);
            appInitializer.login(email, password);
            ActionsUtil.Tap.withCoordinates(driver, 540, 2140);
            Manage manage = new Manage(driver);
            manage.goToMore();
            Profile profile = new Profile(driver);
            profile.edit();
            manage.Logout();
            ActionsUtil.sleep(2000);
        }
    }
}
