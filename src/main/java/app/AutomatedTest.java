package app;

import app.Connectivity.AIConnectivity;
import app.Fan.FanManagement;
import app.MoreTab.Manage;
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
            AIConnectivity connect = new AIConnectivity(driver);
            connect.Alexa();
            connect.GoogleHome();
            Manage manage = new Manage(driver);
            manage.Logout();
            ActionsUtil.sleep(2000);
        }
    }
}
