package app;

import app.Lock.LockManagement;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.appmanagement.ApplicationState;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class AutomatedTest {

    public AppiumDriver atomberg;

    public void run() throws IOException, UnsupportedFlavorException {
        AppInitializer appInitializer = new AppInitializer();
        appInitializer.openApp();
        appInitializer.checkMainScreen();
        atomberg = appInitializer.getDriver();

        LockManagement lock = new LockManagement(atomberg);
        lock.checkLock();
        AndroidDriver driver = (AndroidDriver) atomberg;
        ApplicationState appState = driver.queryAppState("com.atomberg.app");
        System.out.println(appState);
    }
}